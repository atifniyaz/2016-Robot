package com.techhounds.commands.drive_auton;

import edu.wpi.first.wpilibj.command.Command;

public class FinishedControlLoop extends Command {

	@Override
	protected void initialize() {
		
	}

	@Override
	protected void execute() {
		
	}

	@Override
	protected boolean isFinished() {
		return MotionControlLoop.getInstance().finished();
	}

	@Override
	protected void end() {
		MotionControlLoop.getInstance().stop();
	}

	@Override
	protected void interrupted() {
		// TODO Auto-generated method stub
		
	}

}
