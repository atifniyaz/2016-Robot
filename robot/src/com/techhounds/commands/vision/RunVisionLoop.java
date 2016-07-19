package com.techhounds.commands.vision;

import com.techhounds.subsystems.DriveSubsystem;

import edu.wpi.first.wpilibj.command.CommandGroup;

public class RunVisionLoop extends CommandGroup {

	public RunVisionLoop() {
		requires(DriveSubsystem.getInstance());
		addSequential(new StartVisionLoop());
		addSequential(new FinishedVisionLoop(), 6);
		addSequential(new StopVisionLoop());
	}
	
	public RunVisionLoop(double timeout) {
		requires(DriveSubsystem.getInstance());
		addSequential(new StartVisionLoop());
		addSequential(new FinishedVisionLoop(), timeout);
		addSequential(new StopVisionLoop());
	}
}
