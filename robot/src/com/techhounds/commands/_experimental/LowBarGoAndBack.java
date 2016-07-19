package com.techhounds.commands._experimental;

import com.techhounds.RobotMap.Angler;
import com.techhounds.TrajectoryLoader;
import com.techhounds.commands.angler.SetAnglerPosition;
import com.techhounds.commands.auton.RotateUsingVisionContinuous;
import com.techhounds.commands.collector.SetCollectorPower;
import com.techhounds.commands.collector.WaitForBeanBreak;
import com.techhounds.commands.drive_auton.FinishedControlLoop;
import com.techhounds.commands.drive_auton.RunControlLoop;
import com.techhounds.commands.drive_auton.WaitForControlLoop;
import com.techhounds.commands.gyro.RotateToLastAngle;
import com.techhounds.commands.gyro.SaveCurrentAngle;
import com.techhounds.commands.shooter.SetShooterPower;
import com.techhounds.commands.shooter.SetShooterSpeed;
import com.techhounds.lib.trajectory.TrajectoryPair;

import edu.wpi.first.wpilibj.command.CommandGroup;
import edu.wpi.first.wpilibj.command.WaitCommand;

public class LowBarGoAndBack extends CommandGroup {

	public LowBarGoAndBack() {
		
		TrajectoryPair go = TrajectoryLoader.getInstance()
				.getTrajectory(TrajectoryLoader.FILES_TO_LOAD[0]);
		
		addSequential(new RunControlLoop(go));
		addSequential(new WaitForControlLoop(161));
		addParallel(new SetShooterSpeed(71));
		addSequential(new WaitForControlLoop(202));
		addSequential(new SetAnglerPosition(Angler.UP));
		addSequential(new FinishedControlLoop());
		addSequential(new SaveCurrentAngle());
		addSequential(new RotateUsingVisionContinuous(3));
		addSequential(new WaitCommand(.5));
		
		// Fire
		addSequential(new SetCollectorPower(1));
		addSequential(new WaitForBeanBreak(false));
		addSequential(new WaitCommand(.1));
		addSequential(new SetCollectorPower(0));
		addSequential(new SetShooterPower());
		addSequential(new RotateToLastAngle());
		
		TrajectoryPair go2 = TrajectoryLoader.getInstance()
				.getTrajectory(TrajectoryLoader.FILES_TO_LOAD[1]);
		
		addSequential(new RunControlLoop(go2));
		addSequential(new SetAnglerPosition(Angler.COLLECTING));
		addSequential(new FinishedControlLoop());
	}
}
