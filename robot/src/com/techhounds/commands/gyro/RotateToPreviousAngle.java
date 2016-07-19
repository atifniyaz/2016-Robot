package com.techhounds.commands.gyro;

import com.techhounds.subsystems.GyroSubsystem;

import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.WaitCommand;

/**
 *
 */
public class RotateToPreviousAngle extends Command {
	
	//This command will return the angle stored in the GyroSubsystem, defaults to zero, and an offset angle can be added,
	//to simplify rotation commands
	
	private GyroSubsystem gyro;
	private double offset = 0;
	private Command command;
	
    public RotateToPreviousAngle() {
    	gyro = GyroSubsystem.getInstance();
        // Use requires() here to declare subsystem dependencies
        // eg. requires(chassis);
    }
    
    public RotateToPreviousAngle(double offset) {
    	this();
    	this.offset = offset;
    }

    // Called just before this Command runs the first time
    protected void initialize() {
    	(command = new RotateUsingGyro(gyro.getStoredAngle() - gyro.getRotation() + offset, 4, 0)).start();
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
        return !command.isRunning();
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
