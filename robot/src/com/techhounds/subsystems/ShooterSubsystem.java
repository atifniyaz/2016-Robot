package com.techhounds.subsystems;

import java.io.File;
import java.io.PrintWriter;

import com.techhounds.Robot;
import com.techhounds.RobotMap;
import com.techhounds.lib.util.HoundMath;
import com.techhounds.lib.util.HoundSubsystem;

import edu.wpi.first.wpilibj.CANTalon;
import edu.wpi.first.wpilibj.CANTalon.TalonControlMode;
import edu.wpi.first.wpilibj.Counter;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.PIDController;
import edu.wpi.first.wpilibj.PIDOutput;
import edu.wpi.first.wpilibj.PIDSource;
import edu.wpi.first.wpilibj.PIDSourceType;
import edu.wpi.first.wpilibj.livewindow.LiveWindow;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class ShooterSubsystem extends HoundSubsystem {

	private static ShooterSubsystem instance;

	private CANTalon shooter;
	private Counter count;
	private PIDController controller;
	private PrintWriter print;

	private static final double MAX_SPEED = 100;
	public static final double P = .025, I = 0, D = .075, F = .005;

	private double lastSpeed;
	private double pidLastSpeed;
	private double speedThreshold;
	private double lastFilter2Speed;

	private ShooterSubsystem() {
		shooter = new CANTalon(RobotMap.Shooter.MOTOR);
		shooter.setInverted(getInverted());
		shooter.enableBrakeMode(true);

		try {
			File f = new File("/home/lvuser/shooter_enc.csv");
			f.createNewFile();
			print = new PrintWriter(f);

			print.println("Original Speed, Filtered Speed,Filter2 Speed,Power");
		} catch (Exception e) {
			e.printStackTrace();
		}

		count = new Counter(new DigitalInput(RobotMap.Shooter.DIO));
		count.setSamplesToAverage(5);
		count.setDistancePerPulse(1);

		controller = new PIDController(P, I, D, F, new PIDSource() {

			public void setPIDSourceType(PIDSourceType pidSource) {
			}

			public PIDSourceType getPIDSourceType() {
				// TODO Auto-generated method stub
				return PIDSourceType.kRate;
			}

			public double pidGet() {
				double speed = getFilteredSpeed2();
				return pidLastSpeed = speed;
			}

		}, new PIDOutput() {

			public void pidWrite(double output) {
				if (pidLastSpeed < getSetPoint() * 0.25) {
					shooter.set(0.6);
				} else if (pidLastSpeed < getSetPoint() * 0.75) {
					shooter.set(0.8);
				} else {
					shooter.set(output);
				}
			}

		}, 1 / 100.0);
		controller.setAbsoluteTolerance(2);
		controller.setOutputRange(0, 1);

		LiveWindow.addActuator("shooter", "motor", shooter);
		LiveWindow.addSensor("shooter", "counter", count);

		// Filter stuff
		lastFilter2Speed = 0;
	}

	public void setSpeed(double setPoint) {
		pidLastSpeed = 0;
		controller.setSetpoint(setPoint);
		setThreshold(setPoint);
		controller.enable();
	}

	public boolean onTarget() {
		return Math.abs(controller.getError()) < 1;
	}

	public void setPower(double power) {
		controller.disable();
		shooter.changeControlMode(TalonControlMode.PercentVbus);
		shooter.set(HoundMath.checkRange(power, -.5, 1));
	}

	public void stopPower() {
		shooter.set(0);
	}

	public void setThreshold(double setPoint) {
		speedThreshold = setPoint * 1.3;
	}

	public double getSpeed() {
		double speed = count.getRate();
		if (speed == Double.NaN || speed == Double.POSITIVE_INFINITY || speed == Double.NEGATIVE_INFINITY) {
			speed = lastSpeed;
		}
		if (speed > MAX_SPEED) {
			speed = lastSpeed;
		}

		lastSpeed = speed;
		return speed;
	}

	public double getFilteredSpeed() {
		double speed = getSpeed();
		if (speed > speedThreshold) {
			speed = lastSpeed;
		}
		lastSpeed = speed;
		return speed;

	}

	public double getFilteredSpeed2() {
		double speed = getSpeed();
		if (Math.abs(speed - lastFilter2Speed) < 15) {
			lastFilter2Speed = speed;
		}
		return lastFilter2Speed;
	}

	public void resetFilteredSpeed2() {
		lastFilter2Speed = getSpeed();
	}

	public double getFilteredSpeed2NoCompute() {
		return lastFilter2Speed;
	}

	public double getDistance() {
		return count.getDistance();
	}

	public double getCount() {
		return count.get();
	}

	public void resetEncoder() {
		count.reset();
	}

	public boolean getInverted() {
		return RobotMap.Shooter.IS_INVERTED;
	}

	public double getPower() {
		return shooter.get();
	}

	public double getCurrent() {
		return 0;
	}

	public PIDController getController() {
		return controller;
	}

	public void updatePeriodic() {
		if (Robot.isDebugState) {
			SmartDashboard.putNumber("Shooter_Power", getPower());
			SmartDashboard.putNumber("Shooter Speed", getSpeed());
			SmartDashboard.putNumber("Shooter Distance", getDistance());
			SmartDashboard.putNumber("Shooter Error", controller.getError());
			SmartDashboard.putNumber("Shooter Count", getCount());
			SmartDashboard.putNumber("Shooter Period", count.getPeriod());
			SmartDashboard.putNumber("Shooter Speed Filtered", getFilteredSpeed());
			SmartDashboard.putNumber("Shooter Speed Filtered 2", getFilteredSpeed2NoCompute());

		}

		print.println(getSpeed() + "," + getFilteredSpeed() + "," + getFilteredSpeed2NoCompute() + "," + getPower());

		SmartDashboard.putNumber("Shooter Speed", getFilteredSpeed());
		SmartDashboard.putBoolean("Shooter PID OnTarget", controller.isEnabled() ? onTarget() : false);
		SmartDashboard.putBoolean("Shooter PID Running", controller.isEnabled());
	}

	public void initDefaultCommand() {

	}

	public double getSetPoint() {
		return controller.getSetpoint();
	}

	public static ShooterSubsystem getInstance() {
		return instance == null ? instance = new ShooterSubsystem() : instance;
	}
}
