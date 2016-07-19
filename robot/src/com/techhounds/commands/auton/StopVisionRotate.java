package com.techhounds.commands.auton;

import edu.wpi.first.wpilibj.command.Command;

public class StopVisionRotate extends Command {

	public StopVisionRotate() {
	}

	@Override
	protected void initialize() {
		VisionRotateToTarget.getInstance().cancel();
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
