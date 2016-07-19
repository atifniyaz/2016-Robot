package com.techhounds.commands.drive_auton;

import com.techhounds.lib.trajectory.TrajectoryPair;
import com.techhounds.subsystems.DriveSubsystem;

import edu.wpi.first.wpilibj.command.CommandGroup;

public class RunControlLoop extends CommandGroup {

	public RunControlLoop() {
		requires(DriveSubsystem.getInstance());
		addSequential(new WriteControlLoopHeading());
		addSequential(new StartControlLoop());
		addSequential(new FinishedControlLoop());
		addSequential(new StopControlLoop());
	}
	
	public RunControlLoop(TrajectoryPair trajectory) {
		requires(DriveSubsystem.getInstance());
		addSequential(new StartControlLoop(trajectory));
		addSequential(new FinishedControlLoop());
		addSequential(new StopControlLoop());
	}
	
	public RunControlLoop(String index) {
		requires(DriveSubsystem.getInstance());
		addSequential(new StartControlLoop(index));
		addSequential(new FinishedControlLoop());
		addSequential(new StopControlLoop());
	}
}
