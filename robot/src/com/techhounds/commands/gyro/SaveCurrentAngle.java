package com.techhounds.commands.gyro;

import com.techhounds.subsystems.GyroSubsystem;

import edu.wpi.first.wpilibj.command.Command;

/**
 *
 */
public class SaveCurrentAngle extends Command {
	
	//Use this command before going across a defense in auton
	
	private GyroSubsystem gyro;
	private double offset;
	
	private boolean useCurrentSavedAngle;
	
    public SaveCurrentAngle() {
    	this(0);
    }
    
    public SaveCurrentAngle(double offset) {
    	gyro = GyroSubsystem.getInstance();
    }
    
    public SaveCurrentAngle(double offsetFromCurrentSavedAngle, boolean dontCare) {
    	this(offsetFromCurrentSavedAngle);
    	useCurrentSavedAngle = true;
    }

    // Called just before this Command runs the first time
    protected void initialize() {
    	if(useCurrentSavedAngle) {
    		gyro.storeCurrentAngle(gyro.getStoredAngle() + offset, false);
    	} else {
        	gyro.storeCurrentAngle(offset);
    	}
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
        return true;
    }

    // Called once after isFinished returns true
    protected void end() {
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
    	end();
    }
}
