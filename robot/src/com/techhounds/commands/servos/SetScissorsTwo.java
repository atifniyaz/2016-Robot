package com.techhounds.commands.servos;

import com.techhounds.subsystems.ServoSubsystem;

import edu.wpi.first.wpilibj.command.Command;

/**
 *
 */
public class SetScissorsTwo extends Command {
	
	private ServoSubsystem scissorTwo;
	
	private boolean setMax;

    public SetScissorsTwo() {
    	this(ServoSubsystem.getScissorTwo().getIsMax());
        // Use requires() here to declare subsystem dependencies
        // eg. requires(chassis);
    }
    
    public SetScissorsTwo(boolean setMax) {
    	scissorTwo = ServoSubsystem.getScissorTwo();
    	requires(scissorTwo);
    	this.setMax = setMax;
    }
    
    // Called just before this Command runs the first time
    protected void initialize() {
    	scissorTwo.set(setMax);
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
