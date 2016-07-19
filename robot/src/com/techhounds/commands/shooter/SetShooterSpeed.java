package com.techhounds.commands.shooter;

import com.techhounds.subsystems.ShooterSubsystem;

import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 *
 */
public class SetShooterSpeed extends Command {

	private double speed;
	private ShooterSubsystem shooter;
	private int cnt;
	private boolean useVision;
	private Boolean finish = false;
	
    public SetShooterSpeed(double speed) {
    	this.speed = speed;
    	shooter = ShooterSubsystem.getInstance();
    	requires(shooter);
    	this.useVision = false;
   
    }
    
    public SetShooterSpeed(boolean useVision) {
    	this(69);
    	this.useVision = useVision;
   
    }
    
    public SetShooterSpeed(double speed, boolean finish) {
    	this(speed);
    	this.finish = finish;
    }

    // Called just before this Command runs the first time
    protected void initialize() {
    	
    	if(useVision) {
    		boolean outerworksShot = SmartDashboard.getBoolean("Outerworks Shot", false);
    		
    		if(outerworksShot) {
    			shooter.setSpeed(71);
    		} else {
    			shooter.setSpeed(69);
    		}
    	} else {
    		shooter.setSpeed(speed);
    	}
    	
    	cnt = 0;
    	shooter.resetFilteredSpeed2();
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
    }

    private Long currTime;
    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
    	
    	if(finish != null && finish) {
    		return true;
    	}
    	
    	if(shooter.onTarget())
    		cnt++;
    	else
    		cnt = 0;
    	
        return cnt >= 3;
        /*
    	if(shooter.onTarget()) {
    		if(currTime == null)
    			currTime = System.currentTimeMillis();
    		
    		if(System.currentTimeMillis() - currTime > 1000) {
    			SmartDashboard.putNumber("Time To Get To Speed", timeSinceInitialized());
    			return true;
    		}
    	} else {
    		currTime = null;
    	}
    	
		return false;
    */}

    // Called once after isFinished returns true
    protected void end() {
    	//Locks pid power, because PID is disabled in setPower
    	//shooter.setPower(shooter.getPower());
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
    }
}
