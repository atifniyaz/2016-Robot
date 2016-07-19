package com.techhounds.commands.shooter;

import com.techhounds.commands.auton.RotateUsingVision;
import com.techhounds.subsystems.GyroSubsystem;

import edu.wpi.first.wpilibj.command.Command;

public class AlignUsingVision extends Command {

	public RotateUsingVision currentCommand;
	private GyroSubsystem gyro;
	private Double timeout;
	private Double currTime;
	private static AlignUsingVision instance;
	
	public AlignUsingVision() {
		gyro = GyroSubsystem.getInstance();
	}
	
	public static AlignUsingVision getInstance() {
		return instance == null ? instance = new AlignUsingVision() : instance;
	}
	
	public AlignUsingVision(double timeout) {
		this();
		this.timeout = timeout;
	}
	
	@Override
	protected void initialize() {
		currTime = null;
		(currentCommand = new RotateUsingVision()).start();
	}

	@Override
	protected void execute() {
		
	}

	@Override
	protected boolean isFinished() {
		if(timeout != null) {
			if(timeout < timeSinceInitialized()) {
				return !currentCommand.isRunning();
			}
		}
		
		if(!currentCommand.isRunning()) {
			
			if(currTime == null) {
				currTime = timeSinceInitialized();
			}
		
			if(timeSinceInitialized() - currTime < .375) {
				return false;
			}
			
			currTime = null;
			
			if(Math.abs(currentCommand.getSetpoint() - gyro.getRotation()) < .25) {
				currentCommand.cancel();
				return true;
			} else {
				currentCommand.cancel();
				(currentCommand = new RotateUsingVision(2)).start();
				return false;
			}
		} else {
			return false;
		}
	}

	@Override
	protected void end() {
		currentCommand.cancel();
	}

	@Override
	protected void interrupted() {
		end();
	}
}
