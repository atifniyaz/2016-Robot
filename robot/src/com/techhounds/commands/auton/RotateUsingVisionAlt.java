package com.techhounds.commands.auton;

import java.util.Timer;
import java.util.TimerTask;

import com.techhounds.lib.util.PeriodicCommand;
import com.techhounds.subsystems.DriveSubsystem;
import com.techhounds.subsystems.GyroSubsystem;

import edu.wpi.first.wpilibj.PIDController;
import edu.wpi.first.wpilibj.PIDOutput;
import edu.wpi.first.wpilibj.PIDSource;
import edu.wpi.first.wpilibj.PIDSourceType;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 *
 */
public class RotateUsingVisionAlt extends PeriodicCommand implements PIDSource, PIDOutput {
	
	private static double p = .1, 
			i = 0, 
			d = .12;
	
	protected DriveSubsystem drive;
	protected GyroSubsystem gyro;
	
	private PIDController pid;
	private Timer timer;
	
	private boolean framesCounting;
	private boolean visionOnTarget = false;
	
	public RotateUsingVisionAlt() {
    	super(100.0);
		requires(drive = DriveSubsystem.getInstance());
		gyro = GyroSubsystem.getInstance();
		
    	pid = new PIDController(p, i, d, this, this, 10);
    	pid.setOutputRange(-.65, .65);
    	pid.setAbsoluteTolerance(1);
    }
 
	@Override
	protected void init() {
		
    	visionOnTarget = false;
    	
    	timer = new Timer();
    	timer.scheduleAtFixedRate(new CheckForFrames(), 0, 20);
	}

	@Override
	protected void doRun() {
		
		if(framesCounting) {

			double angleOff = SmartDashboard.getNumber("OffCenterDegreesX", 0);
			
			/* if(angleOff < 0)
				angleOff--;
			else if(angleOff > 0)
				angleOff++; */
			
			if(Math.abs(angleOff) < .5) {
				visionOnTarget = true;
			} else {
				pid.setSetpoint(angleOff + gyro.getRotation());
				
				if(!pid.isEnabled())
					pid.enable();
			}
    	} else {
    		pid.disable();
    	}
	}

	@Override
	protected boolean doFinish() {
		return visionOnTarget;
	}
	
    protected void end() {	timer.cancel(); pid.disable(); }
    
    protected void interrupted() {	end(); }

	@Override
	public void pidWrite(double output) {
		/*if(Math.abs(output) < MIN_TURN_POWER){
			if(pid.onTarget()){
				output = 0;
			}else if(output > 0){
				output = MIN_TURN_POWER;
			}else if(output < 0){
				output = -MIN_TURN_POWER;
			}
		}*/
		
		drive.rotateWithPower(output);
	}

	@Override
	public void setPIDSourceType(PIDSourceType pidSource) {}

	@Override
	public PIDSourceType getPIDSourceType() {	return PIDSourceType.kDisplacement;	}

	@Override
	public double pidGet() {
		return gyro.getRotation();
	}
	
	public class CheckForFrames extends TimerTask {

		private double lastFrame = -1;
		
		@Override
		public void run() {
			
			double frame = SmartDashboard.getNumber("FrameCount", -1);
			
			if(lastFrame < frame) {
				framesCounting = true;
				frame = lastFrame;
			} else {
				framesCounting = false;
			}
		}
	}
}
