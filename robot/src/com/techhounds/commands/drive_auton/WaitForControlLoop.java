package com.techhounds.commands.drive_auton;

import edu.wpi.first.wpilibj.command.Command;

public class WaitForControlLoop extends Command {

	private int segment;
	
	public WaitForControlLoop(int segment) {
		this.segment = segment;
	}
	
	@Override
	protected void initialize() {
		
	}

	@Override
	protected void execute() {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected boolean isFinished() {
		// TODO Auto-generated method stub
		return MotionControlLoop.getInstance().currentSegment() >= segment;
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
