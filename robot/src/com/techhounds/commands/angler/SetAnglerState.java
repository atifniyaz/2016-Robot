package com.techhounds.commands.angler;

import com.techhounds.RobotMap.Angler;
import com.techhounds.commands.collector.SetCollectorPower;
import com.techhounds.subsystems.AnglerSubsystem;
import com.techhounds.subsystems.BeamBreakSubsystem;

import edu.wpi.first.wpilibj.command.Command;

public class SetAnglerState extends Command {
	
	private AnglerSubsystem angler;
	private boolean stateUp;

    public SetAnglerState(boolean stateUp) {
    	angler = AnglerSubsystem.getInstance();
    	this.stateUp = stateUp;
    }
    
    protected void initialize() {
    	
    	if(stateUp) {
    		angler.increaseState();
    	} else {
    		angler.decreaseState();
    	}
    	
    	if(angler.getState() == 0) {
    		new SetAnglerPosition(Angler.DOWN).start();
    	} else if(angler.getState() == 1) {
    		new SetAnglerPosition(Angler.COLLECTING).start();
    	} else if(angler.getState() == 2) {
    		new SetAnglerPosition(Angler.UP).start();
    	} else {
    		System.out.println("ERROR: Angler State Error");
    	}
    }

    protected void execute() {
    }

    protected boolean isFinished() {
        return true;
    }
    
    protected void end() {
    }

    protected void interrupted() {
    }
}
