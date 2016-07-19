package com.techhounds.commands.gyro;

import com.techhounds.subsystems.GyroSubsystem;

import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.WaitCommand;

/**
 *
 */
public class WaitForPreviousTilt extends Command {
	
	private GyroSubsystem gyro;
	private double tilt;

    public WaitForPreviousTilt() {
    	gyro = GyroSubsystem.getInstance();
        // Use requires() here to declare subsystem dependencies
        // eg. requires(chassis);
    }

    // Called just before this Command runs the first time
    protected void initialize() {
    	tilt = gyro.getStoredTilt();
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
    	if(gyro.getTilt() == tilt){
    		WaitCommand wait = new WaitCommand(.03);
    		wait.start();
    		if(!wait.isRunning()){
    			if(gyro.getTilt() <= tilt + 2 && gyro.getTilt() >= tilt - 2){
    				end();
    			}
    		}
    	}
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
        return false;
    }

    // Called once after isFinished returns true
    protected void end() {
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
    }
}
