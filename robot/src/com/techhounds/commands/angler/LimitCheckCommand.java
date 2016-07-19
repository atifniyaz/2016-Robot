package com.techhounds.commands.angler;

import edu.wpi.first.wpilibj.command.Command;

import com.techhounds.RobotMap.Angler;
import com.techhounds.subsystems.AnglerSubsystem;

/**
 *
 */
public class LimitCheckCommand extends Command {
	
	private AnglerSubsystem angler;
	public double forLim = Angler.ANGLER_FORWARD_LIMIT;
	public double revLim = Angler.ANGLER_REVERSE_LIMIT;

    public LimitCheckCommand() {
    	angler = AnglerSubsystem.getInstance();
    }

    // Called just before this Command runs the first time
    protected void initialize() {
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
    	if(angler.getPosition() < revLim){
    		angler.stopPower();
    		angler.setPosition(revLim);
    	}else if(angler.getPosition() > forLim){
    		angler.stopPower();
    		angler.setPosition(forLim);
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
    	end();
    }
}
