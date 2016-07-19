package com.techhounds.commands.shooter;

import com.techhounds.subsystems.ShooterSubsystem;

import edu.wpi.first.wpilibj.command.Command;

/**
 *
 */
public class SetShooterPower extends Command {
	
	private ShooterSubsystem shoot;
	private boolean change;
	private double power;
	
    public SetShooterPower(double power) {
    	this(power, false);
    }
    
    public SetShooterPower(double power, boolean change) {
    	shoot = ShooterSubsystem.getInstance();
    	requires(shoot);
    	this.power = power;
    	this.change = change;
    }
    
    public SetShooterPower(){
    	this(0);
    }

    // Called just before this Command runs the first time
    protected void initialize() {
    	shoot.setPower(!change ? power : shoot.getPower() + power);
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
