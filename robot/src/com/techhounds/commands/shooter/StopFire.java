package com.techhounds.commands.shooter;

import edu.wpi.first.wpilibj.command.Command;

public class StopFire extends Command {

	public StopFire() {
	}

	@Override
	protected void initialize() {
		Fire.getInstance().cancel();
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
