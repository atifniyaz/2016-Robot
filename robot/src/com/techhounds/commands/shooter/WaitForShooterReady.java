package com.techhounds.commands.shooter;

import com.techhounds.subsystems.ShooterSubsystem;

import edu.wpi.first.wpilibj.command.Command;

public class WaitForShooterReady extends Command {

	private ShooterSubsystem shooter;
	private Double timeout;
	private Integer useCount;
	private double cnt;
	
	public WaitForShooterReady() {
		this(0.0);
		this.timeout = null;
	}
	
	public WaitForShooterReady(double timeout) {
		shooter = ShooterSubsystem.getInstance();
		this.timeout = timeout;
	}
	
	public WaitForShooterReady(int counts, boolean dosentmatter) {
		this();
		useCount = counts;
	}
	
	@Override
	protected void initialize() {
		// TODO Auto-generated method stub
		cnt = 0;
	}

	@Override
	protected void execute() {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected boolean isFinished() {
	   if(timeout != null) {
		   if(timeSinceInitialized() > timeout) {
		   		return true;
		   }
	   }
	   
	   if(useCount != null) {
		   if(shooter.onTarget()) {
			   cnt++;
			   if(cnt >= 3) {
				   return true;
			   } else {
				   return false;
			   }
		   } else {
			   return false;
		   }
	   } else {
		   return shooter.onTarget();
	   }
	}

	@Override
	protected void end() {
		
	}

	@Override
	protected void interrupted() {
		end();
	}

}
