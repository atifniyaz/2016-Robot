package com.techhounds.commands.shooter;

import com.techhounds.subsystems.ShooterSubsystem;

import edu.wpi.first.wpilibj.command.Command;

/**
 *
 */
public class IncrementShooterSpeed extends Command {
	ShooterSubsystem shooter;
	double increment;
    public IncrementShooterSpeed(double increment) {
    	shooter = ShooterSubsystem.getInstance();//Doesn't need requires because it calls a new command
    	this.increment = increment;
        // Use requires() here to declare subsystem dependencies
        // eg. requires(chassis);
    }

    // Called just before this Command runs the first time
    protected void initialize() {
    	new SetShooterSpeed(shooter.getSetPoint() + increment).start();
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
