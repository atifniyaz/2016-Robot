package com.techhounds.subsystems;

import com.techhounds.Robot;
import com.techhounds.RobotMap;
import com.techhounds.RobotMap.Angler;
import com.techhounds.lib.util.HoundMath;
import com.techhounds.lib.util.HoundSubsystem;

import edu.wpi.first.wpilibj.CANTalon;
import edu.wpi.first.wpilibj.CANTalon.TalonControlMode;
import edu.wpi.first.wpilibj.PIDController;
import edu.wpi.first.wpilibj.PIDOutput;
import edu.wpi.first.wpilibj.PIDSource;
import edu.wpi.first.wpilibj.PIDSourceType;
import edu.wpi.first.wpilibj.livewindow.LiveWindow;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class AnglerSubsystem extends HoundSubsystem {

	private CANTalon angler;
	private PIDController pid;

	private static AnglerSubsystem instance;

	private double P = 0.004, I = 0, D = 0.005;
	private double TOLERANCE = 10;
	private int state = 2;

	private AnglerSubsystem() {

		angler = new CANTalon(Angler.MOTOR);
		angler.setInverted(RobotMap.Angler.IS_INVERTED);
		angler.enableForwardSoftLimit(false);
		angler.enableReverseSoftLimit(false);
		angler.changeControlMode(TalonControlMode.PercentVbus);
		angler.configPotentiometerTurns(1);
		angler.setFeedbackDevice(CANTalon.FeedbackDevice.AnalogPot);

		LiveWindow.addActuator("Angler", "Motor", angler);

		pid = new PIDController(P, I, D, new PIDSource() {

			@Override
			public void setPIDSourceType(PIDSourceType pidSource) {
			}

			@Override
			public PIDSourceType getPIDSourceType() {
				return PIDSourceType.kDisplacement;
			}

			@Override
			public double pidGet() {
				return HoundMath.checkRange(angler.getAnalogInRaw(), Angler.UP, Angler.DOWN);
			}

		}, new PIDOutput() {

			@Override
			public void pidWrite(double output) {
				setPower(output < 0 ? output - .1 : output);
			}

		}, 1 / 200.0);

		pid.setOutputRange(-.45, .45);
		pid.setInputRange(Angler.UP, Angler.DOWN);
		pid.setAbsoluteTolerance(TOLERANCE);
	}

	public void setPosition(double position) {
		pid.setSetpoint(HoundMath.checkRange(position, Angler.UP, Angler.DOWN));
		pid.enable();
	}

	public double getSetPoint() {
		return pid.getSetpoint();
	}

	public boolean onTarget() {
		return pid.onTarget();
	}

	public void stopPower() {
		pid.disable();
		angler.set(0);
	}

	public void disableControl() {
		pid.disable();
	}

	public void setPower(double pow) {
		angler.set(HoundMath.checkRange(pow));
	}

	public double getPosition() {
		return angler.getAnalogInRaw();
	}

	public boolean reachedTarget(double tolerance) {
		return Math.abs(pid.getError()) < tolerance;
	}

	public double getError() {
		return pid.getError();
	}

	public static AnglerSubsystem getInstance() {
		return instance == null ? instance = new AnglerSubsystem() : instance;
	}

	public void initDefaultCommand() {

	}

	public int getState() {
		return state;
	}

	public void increaseState() {
		state = state + 1;
		if (state >= 2) {
			state = 2;
		}
	}

	public void decreaseState() {
		state = state - 1;
		if (state <= 0) {
			state = 0;
		}
	}

	public void defaultState() {
		state = 2;
	}

	@Override
	public void updatePeriodic() {
		if (Robot.isDebugState) {
			SmartDashboard.putNumber("Angler_Position", getPosition());
			SmartDashboard.putNumber("Angler_Error: ", pid.getError());
			SmartDashboard.putData("Angler Pid", pid);
		}
	}
}
