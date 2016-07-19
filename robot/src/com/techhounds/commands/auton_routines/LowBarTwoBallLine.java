package com.techhounds.commands.auton_routines;

import com.techhounds.RobotMap;
import com.techhounds.RobotMap.Angler;
import com.techhounds.commands.angler.SetAnglerPosition;
import com.techhounds.commands.auton.RotateUsingVisionContinuous;
import com.techhounds.commands.collector.SetCollectorPower;
import com.techhounds.commands.collector.WaitForBeanBreak;
import com.techhounds.commands.drive_auton.FinishedControlLoop;
import com.techhounds.commands.drive_auton.RunControlLoop;
import com.techhounds.commands.drive_auton.StartControlLoop;
import com.techhounds.commands.drive_auton.StopControlLoop;
import com.techhounds.commands.drive_auton.WaitForControlLoop;
import com.techhounds.commands.drive_auton.WriteControlLoopHeading;
import com.techhounds.commands.gyro.RotateToLastAngle;
import com.techhounds.commands.gyro.RotateUsingGyro;
import com.techhounds.commands.gyro.SaveCurrentAngle;
import com.techhounds.commands.shooter.SetShooterPower;
import com.techhounds.commands.shooter.SetShooterSpeed;
import com.techhounds.subsystems.DriveSubsystem;

import edu.wpi.first.wpilibj.command.CommandGroup;
import edu.wpi.first.wpilibj.command.WaitCommand;

public class LowBarTwoBallLine extends CommandGroup {

	public LowBarTwoBallLine() {
		requires(DriveSubsystem.getInstance());

		addParallel(new WriteControlLoopHeading());
		addParallel(new SaveCurrentAngle());
		addParallel(new SetCollectorPower(0.6, true));
		addParallel(new SetAnglerPosition((Angler.COLLECTING)), 0.1);
		addSequential(new WaitForBeanBreak(true));
		
		addParallel(new SetCollectorPower(0, true));
		addParallel(new SetShooterSpeed(72.7));
		addSequential(new RunControlLoop("LowBarOneBall"));
		
		CommandGroup grop = new CommandGroup();
		grop.addParallel(new SetCollectorPower(-.4, true));
		grop.addSequential(new WaitForBeanBreak(false), .375);
		grop.addParallel(new SetCollectorPower(.4, true));
		grop.addSequential(new WaitForBeanBreak(true),.375);
		grop.addSequential(new SetCollectorPower(0, true));
		
		//addSequential(grop);
		addSequential(new WaitCommand(.1));
		addSequential(new RotateUsingVisionContinuous(), 1.35);
		addSequential(new WaitCommand(.1));
		shoot();
		addSequential(new RotateToLastAngle(20), 1);
		
		addSequential(new StartControlLoop("LowBarTwoBall-Back"));
		addParallel(new SetAnglerPosition((RobotMap.Angler.COLLECTING + RobotMap.Angler.DOWN) / 2), 0.1);
		addSequential(new WaitForControlLoop(185));
		addParallel(new SetAnglerPosition(RobotMap.Angler.UP), 0.1);
		addParallel(new SetCollectorPower(0.6, true));
		addSequential(new WaitForControlLoop(215));
		addParallel(new SetAnglerPosition(RobotMap.Angler.COLLECTING), 0.1);
		addSequential(new FinishedControlLoop());
		addSequential(new StopControlLoop());
		addSequential(new WaitForBeanBreak(true));
		addSequential(new SetCollectorPower(0.0, true));
		addParallel(new SetShooterSpeed(72.7));
		addSequential(new StartControlLoop("LowBarTwoBall-Cross"));
		addSequential(new FinishedControlLoop());
		addSequential(new StopControlLoop());

		addSequential(new WaitCommand(.1));
		addSequential(new RotateUsingVisionContinuous(), 1.25);
		addSequential(new WaitCommand(.1));
		shoot();
	}
	
	public void shoot() {
		addSequential(new SetCollectorPower(1, true));
		addSequential(new WaitForBeanBreak(false));
		addParallel(new SetCollectorPower(0, true));
		addSequential(new SetShooterPower());
	}
}
