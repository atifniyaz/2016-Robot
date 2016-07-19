package com.techhounds.commands.drive_profile;

import com.techhounds.subsystems.DriveSubsystem;
import com.techhounds.subsystems.controllers.MotionControlLoop;

import edu.wpi.first.wpilibj.command.Command;

public class WriteControlLoopHeading extends Command  {

	@Override
	protected void initialize() {
		MotionControlLoop.getInstance().setCurrHeading(-DriveSubsystem.getInstance().getRotationX());
	}

	@Override
	protected void execute() {
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
