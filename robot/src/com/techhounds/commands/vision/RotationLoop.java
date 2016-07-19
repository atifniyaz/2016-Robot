package com.techhounds.commands.vision;

import java.util.Timer;
import java.util.TimerTask;

import com.techhounds.subsystems.DriveSubsystem;
import com.techhounds.subsystems.GyroSubsystem;

import edu.wpi.first.wpilibj.PIDController;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class RotationLoop {

	private static RotationLoop instance;

	private static double p = .1, 
			i = 0, 
			d = .12;
	
	protected DriveSubsystem drive;
	protected GyroSubsystem gyro;
	
	private PIDController pid;
	
	private Timer timer_frames, timer_pid;
	private boolean framesCounting;
	private boolean visionOnTarget;
	
	public static RotationLoop getInstance() {
		return instance == null ? instance = new RotationLoop() : instance;
	}
	
	public void start() {
		if(pid == null) {
			RotationLoopSources source = new RotationLoopSources();
			
			visionOnTarget = false;
			framesCounting = false;
			
			pid = new PIDController(p, i, d, source ,source);
	    	pid.setOutputRange(-.65, .65);
	    	pid.setAbsoluteTolerance(1);
	    	
	    	timer_pid.scheduleAtFixedRate(new ConstantRotation(), 0, 20);
	    }
	}
	
	public void stop() {
		if(pid != null) {
			pid.disable();
			pid = null;
	
			visionOnTarget = true;
		}
	}
	
	public boolean isFinished() {
		return visionOnTarget;
	}
	
	public class ConstantRotation extends TimerTask {

		private double lastFrame = -1;
		
		@Override
		public void run() {
			
			double frame = SmartDashboard.getNumber("FrameCount", -1);
			
			if(lastFrame < frame) {

				double angleOff = SmartDashboard.getNumber("OffCenterDegreesX", 0);
				
				/* if(angleOff < 0)
					angleOff--;
				else if(angleOff > 0)
					angleOff++; */
				
				if(Math.abs(angleOff) < 1) {
					visionOnTarget = true;
				} else {
					pid.setSetpoint(angleOff + gyro.getRotation());
					visionOnTarget = false;
					if(!pid.isEnabled())
						pid.enable();
				}
				
				lastFrame = frame;
	    	} else {
	    	}
		}
	}
}
