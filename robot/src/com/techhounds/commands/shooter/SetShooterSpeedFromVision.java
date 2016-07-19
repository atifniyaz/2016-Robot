package com.techhounds.commands.shooter;

import com.techhounds.subsystems.ShooterSubsystem;

import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 *
 */
public class SetShooterSpeedFromVision extends Command {

	ShooterSubsystem shooter;
    public SetShooterSpeedFromVision() {
    	shooter = ShooterSubsystem.getInstance();//Doesn't require, just calls a new setShooterSpeed
        // Use requires() here to declare subsystem dependencies
        // eg. requires(chassis);
    }

    // Called just before this Command runs the first time
    protected void initialize() {
    	double setSpeed = SmartDashboard.getNumber("Vision_Shoot_Speed");
    	new SetShooterSpeed(setSpeed).start();
    	
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
