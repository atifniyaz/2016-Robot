package com.techhounds.commands.drive;

import com.techhounds.OI;
import com.techhounds.Robot;
import com.techhounds.subsystems.DriveSubsystem;

import edu.wpi.first.wpilibj.PIDController;
import edu.wpi.first.wpilibj.PIDOutput;
import edu.wpi.first.wpilibj.PIDSource;
import edu.wpi.first.wpilibj.PIDSourceType;
import edu.wpi.first.wpilibj.command.Command;

/**
 *
 */
public class DriveWithSpeed extends Command {
	DriveSubsystem drive;
	double speed;

	private PIDController leftPID;
	private PIDController rightPID;

	private static final double LEFT_Kp = 0, LEFT_Ki = 0, LEFT_Kd = 0;
	private static final double RIGHT_Kp = 0, RIGHT_Ki = 0, RIGHT_Kd = 0;

	public DriveWithSpeed(double speed) {
		this.speed = speed;
	
		drive = DriveSubsystem.getInstance();
		requires(drive);
	
		leftPID = new PIDController(LEFT_Kp, LEFT_Ki, LEFT_Kd , new SpeedData(true), new SpeedData(true));
		rightPID = new PIDController(RIGHT_Kp, RIGHT_Ki, RIGHT_Kd , new SpeedData(false), new SpeedData(false));
	}


	// Called just before this Command runs the first time
	protected void initialize() {
		rightPID.setSetpoint(speed);
		leftPID.setSetpoint(speed);//double check that these are nececarry with Mr. B, we've never tested This command before
		
		rightPID.enable();
		leftPID.enable();
	}

	// Called repeatedly when this Command is scheduled to run
	protected void execute() {
	}

	// Make this return true when this Command no longer needs to run execute()
	protected boolean isFinished() {
		return false;
	}

	// Called once after isFinished returns true
	protected void end() {
		rightPID.disable();
		leftPID.disable();
	}

	// Called when another command which requires one or more of the same
	// subsystems is scheduled to run
	protected void interrupted() {
		end();
	}

	private class SpeedData implements PIDSource, PIDOutput {

		private boolean isLeft;

		public SpeedData(boolean isLeft) {
			this.isLeft = isLeft;
		}

		@Override
		public void pidWrite(double output) {
			if(isLeft)
				drive.setLeftPower(output);
			else
				drive.setRightPower(output);
		}

		@Override
		public void setPIDSourceType(PIDSourceType pidSource) {}

		@Override
		public PIDSourceType getPIDSourceType() {
			return PIDSourceType.kRate;
		}

		@Override
		public double pidGet() {
			return isLeft ? drive.getLeftSpeed() : drive.getRightSpeed();
		}
	}
}
