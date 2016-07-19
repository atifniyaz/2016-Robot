package com.techhounds.commands.drive;

import com.techhounds.Robot;
import com.techhounds.subsystems.DriveSubsystem;
import com.techhounds.subsystems.GyroSubsystem;

import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.CommandGroup;

public class DriveUntilTiltPatternWithPower extends CommandGroup {

	private Command tiltCommand;
	private DriveSubsystem drive;
	private GyroSubsystem gyro;
	
	private double power;
	private Double initAngle;
	private Boolean useSaveAngle;
	
	public DriveUntilTiltPatternWithPower(double power) {
		
		if(power > 0) {
			addParallel(tiltCommand = new CheckForTiltPattern(CheckForTiltPattern.DEFENSE_CROSS_FORWARD, 
					CheckForTiltPattern.DEFENSE_CROSS_TIMEOUTS, 5));
		} else {
			addParallel(tiltCommand = new CheckForTiltPattern(CheckForTiltPattern.DEFENSE_CROSS_BACKWARD, 
					CheckForTiltPattern.DEFENSE_CROSS_TIMEOUTS, 5));
		}
		
		requires(drive = DriveSubsystem.getInstance());
		gyro = GyroSubsystem.getInstance();
		this.power = power;
	}
	
	public DriveUntilTiltPatternWithPower(double power, boolean useSaveAngle, double angleThreshold) {
		
		if(power > 0) {
			addParallel(tiltCommand = new CheckForTiltPattern(CheckForTiltPattern.DEFENSE_CROSS_FORWARD, 
					CheckForTiltPattern.DEFENSE_CROSS_TIMEOUTS, angleThreshold));
		} else {
			addParallel(tiltCommand = new CheckForTiltPattern(CheckForTiltPattern.DEFENSE_CROSS_BACKWARD, 
					CheckForTiltPattern.DEFENSE_CROSS_TIMEOUTS, angleThreshold));
		}
		
		requires(drive = DriveSubsystem.getInstance());
		gyro = GyroSubsystem.getInstance();
		this.power = power;
		this.useSaveAngle = useSaveAngle;
	}
	
	public DriveUntilTiltPatternWithPower(double power, boolean useSaveAngle) {
		this(power, useSaveAngle, 5);
	}

	public DriveUntilTiltPatternWithPower(double power, CheckForTiltPattern.Motion [] motion, Double [] timeout) {
		addParallel(tiltCommand = new CheckForTiltPattern(motion, timeout, 5));
		requires(drive = DriveSubsystem.getInstance());		
		gyro = GyroSubsystem.getInstance();
		this.power = power;
	}
	
	public DriveUntilTiltPatternWithPower(double power, CheckForTiltPattern.Motion [] motion, Double [] timeout, boolean useSaveAngle) {
		addParallel(tiltCommand = new CheckForTiltPattern(motion, timeout, 5));
		requires(drive = DriveSubsystem.getInstance());		
		gyro = GyroSubsystem.getInstance();
		this.power = power;
		this.useSaveAngle = useSaveAngle;
	}
	
	public DriveUntilTiltPatternWithPower(double power, CheckForTiltPattern.Motion [] motion, Double [] timeout, double angleThreshold) {
		addParallel(tiltCommand = new CheckForTiltPattern(motion, timeout, angleThreshold));
		requires(drive = DriveSubsystem.getInstance());
		gyro = GyroSubsystem.getInstance();
		this.power = power;
	}
	
	public void setInitAngle(double angle) {
		this.initAngle = angle;
	}
	
	@Override
	public void initialize() {
		super.initialize();
		
		if(useSaveAngle != null && useSaveAngle) {
			setInitAngle(gyro.getStoredAngle());
		} else {
			setInitAngle(gyro.getRotation());
		}
	}
	
	@Override
	public void execute() {
		super.execute();
		
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
		
		offset *= Math.abs(power);
		drive.setPower(Robot.rangeCheck(power + offset), Robot.rangeCheck(power - offset));
	}
	
	@Override
	public boolean isFinished() {
		if(!tiltCommand.isRunning()) {
			return true;
		} else {
			return super.isFinished();
		}
	}
	
	@Override
	public void end() {
		drive.setPower(0, 0);
		super.end();
	}
}