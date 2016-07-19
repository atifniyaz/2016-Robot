package com.techhounds.commands.collector;

import com.techhounds.subsystems.BeamBreakSubsystem;
import com.techhounds.subsystems.CollectorSubsystem;

import edu.wpi.first.wpilibj.command.Command;

public class SetCollectorPower extends Command {
	
	private CollectorSubsystem collect;
	private BeamBreakSubsystem beam;
	private boolean isShooting;
	
	private double power;
    public SetCollectorPower(double power) {
    	this(power, false);
    }
    
    public SetCollectorPower(){
    	this(0);
    }
    public SetCollectorPower(double power, double timeOut){
    	
    }
    public SetCollectorPower(double power, boolean isShooting){
     	collect = CollectorSubsystem.getInstance();
    	beam = BeamBreakSubsystem.getInstance();
    	
    	requires(collect);
    	this.power = -power;
    	this.isShooting = isShooting;
    }

    // Called once after isFinished returns true
    protected void end() {
    	if(!isShooting)
    	collect.setPower(0);
    }

    protected void interrupted() {
    	end();
    }

	@Override
	protected void initialize() {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void execute() {
    	if(beam.ballPresent() && power < 0 && !isShooting){
    		collect.setPower(0);
    	}else{
    		collect.setPower(power);
    	}
	}

	@Override
	protected boolean isFinished() {
		return isShooting;
	}
}
