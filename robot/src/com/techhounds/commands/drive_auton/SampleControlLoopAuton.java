package com.techhounds.commands.drive_auton;

import com.techhounds.commands.shooter.SetShooterSpeed;
import com.techhounds.subsystems.DriveSubsystem;

import edu.wpi.first.wpilibj.command.CommandGroup;

public class SampleControlLoopAuton extends CommandGroup {

	public SampleControlLoopAuton(String index) {
		requires(DriveSubsystem.getInstance());
		addSequential(new StartControlLoop(index));

		addSequential(new WaitForControlLoop(74));
		addSequential(new SetShooterSpeed(71));
		
		addSequential(new FinishedControlLoop());
		addSequential(new StopControlLoop());
	}
}
