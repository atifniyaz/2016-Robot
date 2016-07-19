package com.techhounds.commands.drive;

import com.techhounds.subsystems.GyroSubsystem;

import edu.wpi.first.wpilibj.command.Command;

public class WaitForTiltPattern extends Command {

	private Motion [] pattern;
	private Double [] timeout;
	
	private GyroSubsystem gyro;
	private int i;
	private double angleThreshold;
	
	public static enum Motion {
		TILT_BACK, TILT_FORWARD, FLAT
	}
	
	public static final Motion [] DEFENSE_CROSS_FORWARD = new Motion [] {
			Motion.FLAT, Motion.TILT_BACK, Motion.TILT_FORWARD, Motion.FLAT	
	};
	
	public static final Motion [] DEFENSE_CROSS_BACKWARD = new Motion [] {
			Motion.FLAT, Motion.TILT_FORWARD, Motion.TILT_BACK, Motion.FLAT	
	};
	
	public static final Double [] DEFENSE_CROSS_TIMEOUTS = new Double [] {
			0.0, 0.125, 0.375, 0.5
	};
	
	public WaitForTiltPattern(Motion [] pattern) {
		this(pattern, 5);
	}
	
	public WaitForTiltPattern(Motion [] pattern, double angleThreshold) {
		this(pattern, null, angleThreshold);
	}
	
	public WaitForTiltPattern(Motion [] pattern, Double [] timeout, double angleThreshold) {
		this.pattern = pattern;
		
		if(timeout != null && pattern.length != timeout.length) {
			timeout = null;
		}
		
		this.timeout = timeout;
		this.angleThreshold = angleThreshold;
		gyro = GyroSubsystem.getInstance();
	}
	
	@Override
	protected void initialize() {
		i = 0;
	}

	@Override
	protected void execute() {
		
	}

	@Override
	protected boolean isFinished() {
		double tilt = gyro.getLean();//.getTilt();
		
		if(pattern[i] == Motion.TILT_BACK) {
			if(tilt <= -Math.abs(angleThreshold))
				timeoutAndIncrement();
		} else if(pattern[i] == Motion.TILT_FORWARD) {
			if(tilt >= Math.abs(angleThreshold))
				timeoutAndIncrement();
		} else if(pattern[i] == Motion.FLAT) {
			if(tilt < Math.abs(angleThreshold) && tilt > -Math.abs(angleThreshold))
				timeoutAndIncrement();
		} else {
			return true;
		}
		
		return i == pattern.length;
	}

	public void timeoutAndIncrement() {
		double targetTime = timeSinceInitialized() + (timeout != null ? timeout[i] : 0);
		
		while(timeSinceInitialized() < targetTime) { /* DO NOTHING */ }
		i++;
	}
	
	@Override
	protected void end() {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void interrupted() {
		// TODO Auto-generated method stub
		end();
	}
}
