package com.techhounds.commands.vision;

import edu.wpi.first.wpilibj.command.Command;

public class StopVisionLoop extends Command {

	@Override
	protected void initialize() {
		RotationLoop.getInstance().stop();
	}

	@Override
	protected void execute() {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected boolean isFinished() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	protected void end() {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void interrupted() {
		// TODO Auto-generated method stub
		
	}

}
