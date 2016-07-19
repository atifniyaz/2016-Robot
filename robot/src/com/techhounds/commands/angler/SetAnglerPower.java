package com.techhounds.commands.angler;

import com.techhounds.subsystems.AnglerSubsystem;

import edu.wpi.first.wpilibj.command.Command;

public class SetAnglerPower extends Command {
	
	private AnglerSubsystem angler;
	private double power;

    public SetAnglerPower(double power) {
    	angler = AnglerSubsystem.getInstance();
    	requires(angler);
    	this.power = power;
    }
    
    public SetAnglerPower(){
    	this(0);
    }

    protected void initialize() {
    	angler.disableControl();
    	angler.setPower(power);
    }

    protected void execute() {
    
    }

    protected boolean isFinished() {
        return true;
    }

    protected void end() {
    	
    }

    protected void interrupted() {
    	end();
    }
}
