package com.techhounds.commands;

import com.techhounds.TrajectoryLoader;

import edu.wpi.first.wpilibj.command.Command;

public class LoadTrajectories extends Command {

	@Override
	protected void initialize() {
		TrajectoryLoader.getInstance().loadTrajectories();
	}

	@Override
	protected void execute() {
	}

	@Override
	protected boolean isFinished() {
		return false;
	}

	@Override
	protected void end() {
	}

	@Override
	protected void interrupted() {
	}

}
