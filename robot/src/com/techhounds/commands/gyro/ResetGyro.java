package com.techhounds.commands.gyro;

import com.techhounds.subsystems.GyroSubsystem;

import edu.wpi.first.wpilibj.command.Command;

public class ResetGyro extends Command {
	
	private Direction direction; 
	
	public static enum Direction {
		ROTATION, LEAN, TILT
	}
	
	public ResetGyro(Direction direction) {
		this.direction = direction;
	}

	@Override
	protected void initialize() {
		
		GyroSubsystem instance = GyroSubsystem.getInstance();
		
		if(direction == Direction.TILT)
			instance.gyroy = instance.createTiltGyro();
		else if(direction == Direction.ROTATION)
			instance.gyrox = instance.createRotationGyro();
		else if(direction == Direction.LEAN)
			instance.gyroz = instance.createLeanGyro();
		else {
			// do nothing
		}
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
