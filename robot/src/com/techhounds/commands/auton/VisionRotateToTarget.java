package com.techhounds.commands.auton;

import com.techhounds.RobotMap;
import com.techhounds.commands.gyro.RotateUsingGyro;

import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 *
 */
public class VisionRotateToTarget extends Command {

	//private double maxAngle = 0.5;
	private double lastFrame = 0;
	//DriveSubsystem drive;
	RotateUsingGyro rotateCommand;
	private boolean done = false;
	private double checkFrame;

	private static VisionRotateToTarget instance;
	
	public VisionRotateToTarget() {
		//drive = DriveSubsystem.getInstance();
		//requires(drive);
		// Use requires() here to declare subsystem dependencies
		// eg. requires(chassis);
	}
	
	public static VisionRotateToTarget getInstance() {
		if(instance == null){
			instance = new VisionRotateToTarget();
		}
		return instance;
	}

	// Called just before this Command runs the first time
	protected void initialize() {
		lastFrame = SmartDashboard.getNumber("FrameCount", -1);
		rotateCommand = null;
		done = false;
		checkFrame = 0;
	}

	// Called repeatedly when this Command is scheduled to run
	protected void execute() {
		double frame = SmartDashboard.getNumber("FrameCount", -1);

		if(rotateCommand != null) {
			if(!rotateCommand.isRunning()) {
				rotateCommand = null;
				double anglesOffCenter = SmartDashboard.getNumber("OffCenterDegreesX", 0);
				done = (Math.abs(anglesOffCenter)) < 1;
				checkFrame = frame + 40;
				lastFrame = frame;
				SmartDashboard.putBoolean("VISION done", done);
				SmartDashboard.putNumber("VISION Off Center", anglesOffCenter);	
			}
		} else {
			if(checkFrame < frame && lastFrame < frame) {
				double angleOff = SmartDashboard.getNumber("OffCenterDegreesX", 0);// * .75;
				SmartDashboard.putNumber("VISION TARGET ANGLE", angleOff);
				(rotateCommand = new RotateUsingGyro(angleOff, RobotMap.DriveTrain.MIN_TURN_POWER, 2, 0)).start();
			}
		}
		
		
		
		// If rotating, wait until it is done
/*		if (rotateCommand != null) {
			if (!rotateCommand.isRunning()) {
				checkFrame = frame + 20;
				rotateCommand = null;
			}
		} else if ((checkFrame != 0) && (frame >= checkFrame)) {
			// Finished rotation, see if we are still off
			checkFrame = 0;
			double anglesOffCenter = SmartDashboard.getNumber("OffCenterDegreesX");
			done = (Math.abs(anglesOffCenter) < 1);
			SmartDashboard.putBoolean("VISION done", done);
			SmartDashboard.putNumber("VISION Off Center", anglesOffCenter);
			lastFrame = lastFrame - 1;
		} else if (rotateCommand == null && frame != lastFrame) {
			// Check to see if we have a new frame we can use to do our rotation
			// with
			double anglesOffCenter = -SmartDashboard.getNumber("OffCenterDegreesX");
			lastFrame = frame;
			rotateCommand = new RotateUsingGyro(anglesOffCenter);
			rotateCommand.start();
		}
*/
	}

	// Make this return true when this Command no longer needs to run execute()
	protected boolean isFinished() {
		return done || timeSinceInitialized() >= 1.5;
	}

	// Called once after isFinished returns true
	protected void end() {
		if(rotateCommand != null) rotateCommand.cancel();
	}
	
	public void cancel() {
		super.cancel();
		if(rotateCommand != null) rotateCommand.cancel();
	}

	// Called when another command which requires one or more of the same
	// subsystems is scheduled to run
	protected void interrupted() {
		end();
	}
}
