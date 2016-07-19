package com.techhounds.commands;

import com.techhounds.OI;

import edu.wpi.first.wpilibj.command.Command;

/**
 *
 */
public class SwitchControllerToEndGame extends Command {

	private boolean toSwitch;
	
    public SwitchControllerToEndGame(boolean toSwitch) {
    	this.toSwitch = toSwitch;
    }

    // Called just before this Command runs the first time
    protected void initialize() {
    	if(toSwitch)
    		OI.getInstance().initializeEndGame(OI.getInstance().getOperator());
    	else
    		OI.getInstance().setupOperator();
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
    }
}
