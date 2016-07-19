package com.techhounds.commands.drive;

import com.techhounds.Robot;
import com.techhounds.RobotMap;
import com.techhounds.subsystems.DriveSubsystem;
import com.techhounds.subsystems.GyroSubsystem;

import edu.wpi.first.wpilibj.PIDController;
import edu.wpi.first.wpilibj.PIDOutput;
import edu.wpi.first.wpilibj.PIDSource;
import edu.wpi.first.wpilibj.PIDSourceType;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class DriveDistanceStraight extends Command implements PIDSource, PIDOutput {

	private DriveSubsystem drive;
	private GyroSubsystem gyro;
	private double targetDist;
	private PIDController pid;
	private double lastPower;
	private double minPower;
	private Double initAngle;
	private boolean useSaveAngle;
	
	private double offsetAngle;
	private boolean pidOnTarget;
	private Boolean [] lightSensorReading;
	
	private Double timeOut;
	private int i;

	public DriveDistanceStraight(double dist, double max, double min, Double timeOut, double angle) {
		this(dist, max, min, timeOut);
		initAngle = angle;
	}
	
	public DriveDistanceStraight(double dist, double max, double min, Double timeOut, boolean useSaveAngle) {
		this(dist, max, min, timeOut, useSaveAngle, 0);
	}
	
	public DriveDistanceStraight(double dist, double max, double min, Double timeOut, boolean useSaveAngle, double offsetAngle) {
		this(dist, max, min, timeOut);
		this.offsetAngle = offsetAngle;
		this.useSaveAngle = useSaveAngle;
	}
	
	public DriveDistanceStraight(double dist) {
		this(dist, 1);
	}
	public DriveDistanceStraight(double dist, Boolean [] waitForOnDefense){
		this(dist);
		lightSensorReading = waitForOnDefense;
	}

	public DriveDistanceStraight() {
		this(30, .4);
	}

	public DriveDistanceStraight(double dist, double max, double min) {
		drive = DriveSubsystem.getInstance();
		gyro = GyroSubsystem.getInstance();
		requires(drive);
		pid = new PIDController(.025, 0, .004, this, this);
		pid.setOutputRange(-Math.abs(max), Math.abs(max));
		pid.setAbsoluteTolerance(2.5);
		minPower = min;
		this.targetDist = dist;
		//SmartDashboard.putData("Drive distance pid", pid);
		//SmartDashboard.putNumber("Power to Drive", 0.3);
		//SmartDashboard.putNumber("Min Power To Drive", min);
	}
	
	public DriveDistanceStraight(double dist, double max, double min, Double timeOut) {
		this(dist, max, min);
		this.timeOut = timeOut;
	}

	public DriveDistanceStraight(double dist, double max) {
		this(dist, max, RobotMap.DriveTrain.MIN_STRAIGHT_POWER);
	}

	@Override
	protected void initialize() {
		// minPower = SmartDashboard.getNumber("Min Power To Drive", .2);
		
		drive.encodersReset();
		
		if(useSaveAngle) {
			initAngle = gyro.getStoredAngle() + offsetAngle;
		} else {
			initAngle = initAngle == null ? gyro.getRotation() : initAngle;
		}
		
		double curDist = drive.countsToDist(drive.getAvgDistance());
		lastPower = 0;
		i = 0;
		pid.setSetpoint(targetDist + curDist);
		pid.enable();

	}

	@Override
	protected void execute() {
		SmartDashboard.putNumber("Drive Error", pid.getError());
		// TODO Auto-generated method stub

	}

	@Override
	protected boolean isFinished() {
		if(timeOut != null){
			if(timeSinceInitialized() > timeOut){
				return true;
			}
		}
		
		if(lightSensorReading != null){
			if(lightSensorReading[i] == drive.onDefense()) {
				i++;
			}
			
			return i >= lightSensorReading.length;
		}

		return pidOnTarget = pid.onTarget();
	}

	@Override
	protected void end() {
		pid.disable();
		drive.setPower(0, 0);
		// TODO Auto-generated method stub

	}

	@Override
	protected void interrupted() {
		end();
		// TODO Auto-generated method stub

	}

	@Override
	public void pidWrite(double output) {
		double pidOutput = output;
		double difference = output - lastPower;
		if (difference > .1) {
			output = lastPower + .1;
		} else if (difference < -.1) {
			output = lastPower - .1;
		}

		//if(output < minPower && output > -minPower)
		//	output = output > 0 ? minPower : -minPower;

		// If PID wants us to move negative, make sure we are at least negative
		// min power same for positive condition
		//if ((pidOutput < 0) && (output > -minPower)) {
		//	output = -minPower;
		//} else if ((pidOutput > 0) && (output < minPower)) {
		//	output = minPower;
		//}
		
		//output = output > 0 ? minPower : -minPower;

		if (pidOnTarget)
			output = 0;
		
		double currAngle = gyro.getRotation() - initAngle;
		double offset;
		
		double MAX_OFFSET = .25;
		
		if(currAngle > 9.11346) {
			offset = MAX_OFFSET;
		} else if(currAngle < -9.01606) {
			offset = -MAX_OFFSET;
		} else {
			// A power between -.21 and .21
			offset = MAX_OFFSET * ((-0.000136434109 * Math.pow(currAngle, 3) + 0.0000199335555 * Math.pow(currAngle, 2)
				+ 0.03363132991 * (currAngle)) / .201608);
		}
		
		offset *= Math.abs(output);
		
		lastPower = output;
	
		drive.setPower(Robot.rangeCheck(output + offset), Robot.rangeCheck(output - offset));

		// TODO Auto-generated method stub

	}

	@Override
	public void setPIDSourceType(PIDSourceType pidSource) {
	}

	@Override
	public PIDSourceType getPIDSourceType() {
		return PIDSourceType.kDisplacement;
	}

	@Override
	public double pidGet() {
		return drive.countsToDist(drive.getAvgDistance());
	}

}
