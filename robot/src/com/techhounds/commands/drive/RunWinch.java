package com.techhounds.commands.drive;

import com.techhounds.subsystems.DriveSubsystem;

import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 *
 */
public class RunWinch extends Command {

    String label;
    DriveSubsystem drive;
	public RunWinch(String label, double defaultValue) {
		
		drive = DriveSubsystem.getInstance();
    	this.label = label;
    	SmartDashboard.putNumber(label, defaultValue);
    	requires(drive);
	}

    // Called just before this Command runs the first time
    protected void initialize() {
    	double winchSpeed = SmartDashboard.getNumber(label, 0);
    	drive.setPower(winchSpeed, winchSpeed);
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
        return false;
    }

    // Called once after isFinished returns true
    protected void end() {
    	drive.setPower(0, 0);
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
    	end();
    }
}
