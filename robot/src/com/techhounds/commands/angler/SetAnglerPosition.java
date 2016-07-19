package com.techhounds.commands.angler;

import com.techhounds.subsystems.AnglerSubsystem;

import edu.wpi.first.wpilibj.command.Command;

public class SetAnglerPosition extends Command {
	
	private AnglerSubsystem angle;
	private Double position;
	private Double timeout;

    public SetAnglerPosition(Double position) {
    	angle = AnglerSubsystem.getInstance();
    	requires(angle);
    	this.position = position;
    	this.timeout = null;
    }
    
    public SetAnglerPosition(Double position, Double timeout) {
    	angle = AnglerSubsystem.getInstance();
    	requires(angle);
    	this.position = position;
    	this.timeout = timeout;
    }
    
    public SetAnglerPosition(){
    	this(null);
    }

    protected void initialize() {
    	if(position == null)
    		angle.setPosition(angle.getPosition());
    	else
    		angle.setPosition(position);
    }
    
    protected void execute() {
    }

    protected boolean isFinished() {
    	if(timeout != null) {
    		if(timeout < timeSinceInitialized()) {
    			return true;
    		}
    	}
    	return angle.onTarget();
    }

    protected void end() {
    }

    protected void interrupted() {
    	end();
    }
}
