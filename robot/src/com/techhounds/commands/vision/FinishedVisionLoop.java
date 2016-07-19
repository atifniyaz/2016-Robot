package com.techhounds.commands.vision;

import edu.wpi.first.wpilibj.command.Command;

public class FinishedVisionLoop extends Command {

	@Override
	protected void initialize() {
		
	}

	@Override
	protected void execute() {
		
	}

	@Override
	protected boolean isFinished() {
		return RotationLoop.getInstance().isFinished();
	}

	@Override
	protected void end() {
		
	}

	@Override
	protected void interrupted() {
		// TODO Auto-generated method stub
		
	}

}
