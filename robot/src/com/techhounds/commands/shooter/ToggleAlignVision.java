package com.techhounds.commands.shooter;

import com.techhounds.commands.auton.RotateUsingVisionContinuous;

import edu.wpi.first.wpilibj.command.Command;

public class ToggleAlignVision extends Command{

	private boolean running;
	
	public ToggleAlignVision(boolean running) {
		this.running = running;
	}
	
	@Override
	protected void initialize() {
		if(running) {
			RotateUsingVisionContinuous.rotate = new RotateUsingVisionContinuous();
			RotateUsingVisionContinuous.getInstance().start();
		} else {
			RotateUsingVisionContinuous.getInstance().cancel();
		}
	}

	@Override
	protected void execute() {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected boolean isFinished() {
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
