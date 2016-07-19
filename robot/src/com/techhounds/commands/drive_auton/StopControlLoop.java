package com.techhounds.commands.drive_auton;

import edu.wpi.first.wpilibj.command.Command;

public class StopControlLoop extends Command {

	@Override
	protected void initialize() {
		MotionControlLoop.getInstance().stop();
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
