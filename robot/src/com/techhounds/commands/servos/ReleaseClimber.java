package com.techhounds.commands.servos;

import com.techhounds.subsystems.ServoSubsystem;

import edu.wpi.first.wpilibj.command.Command;

/**
 *
 */
public class ReleaseClimber extends Command {
	
	private ServoSubsystem scissorOne;
	
	private boolean setMax;

    public ReleaseClimber() {
    	this(ServoSubsystem.getScissorOne().getIsMax());
        // Use requires() here to declare subsystem dependencies
        // eg. requires(chassis);
    }
    
    public ReleaseClimber(boolean setMax){
    	scissorOne = ServoSubsystem.getScissorOne();
    	requires(scissorOne);
    	this.setMax = setMax;
    }

    // Called just before this Command runs the first time
    protected void initialize() {
    	scissorOne.set(setMax);
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
