package com.techhounds.subsystems;

import com.techhounds.Robot;
import com.techhounds.RobotMap;
import com.techhounds.commands.drive.DriveWithGamepad;
import com.techhounds.lib.util.HoundSubsystem;

import edu.wpi.first.wpilibj.AnalogInput;
import edu.wpi.first.wpilibj.BuiltInAccelerometer;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.PIDController;
import edu.wpi.first.wpilibj.PIDOutput;
import edu.wpi.first.wpilibj.PIDSource;
import edu.wpi.first.wpilibj.PIDSourceType;
import edu.wpi.first.wpilibj.PowerDistributionPanel;
import edu.wpi.first.wpilibj.Spark;
import edu.wpi.first.wpilibj.livewindow.LiveWindow;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class DriveSubsystem extends HoundSubsystem {

	private Spark left;
	private Spark right;
	private boolean rotating;
	public static boolean isForward = false;

	private PIDController gyroPID;
	private PIDController drivePID;

	private Encoder rightEncoder;
	private Encoder leftEncoder;

	public static double driver_p;
	public static double driver_i;
	public static double driver_d;

	private final double gyro_p;
	private final double gyro_i;
	private final double gyro_d;

	private final double percentTolerable = .1;
	private static final double CNT_TO_DIST = .0393686;

	private BuiltInAccelerometer accelerometer;
	private PowerDistributionPanel panel;

	private static DriveSubsystem instance;
	private GyroSubsystem gyro;
	private AnalogInput lightSensor;

	private boolean debugging = false;

	private DriveSubsystem() {

		accelerometer = new BuiltInAccelerometer();
		panel = new PowerDistributionPanel();
		gyro = GyroSubsystem.getInstance();

		left = new Spark(RobotMap.DriveTrain.DRIVE_LEFT_MOTOR);
		left.setInverted(RobotMap.DriveTrain.DRIVE_LEFT_IS_INVERTED);

		right = new Spark(RobotMap.DriveTrain.DRIVE_RIGHT_MOTOR);
		right.setInverted(RobotMap.DriveTrain.DRIVE_RIGHT_IS_INVERTED);

		rightEncoder = new Encoder(RobotMap.DriveTrain.ENC_RIGHT_A, RobotMap.DriveTrain.ENC_RIGHT_B);
		leftEncoder = new Encoder(RobotMap.DriveTrain.ENC_LEFT_A, RobotMap.DriveTrain.ENC_LEFT_B);

		lightSensor = new AnalogInput(RobotMap.DriveTrain.LIGHT_SENSOR);
		LiveWindow.addActuator("drive", "left motors", left);
		LiveWindow.addActuator("drive", "right motors", right);
		LiveWindow.addSensor("drive", "left encoder", leftEncoder);
		LiveWindow.addSensor("drive", "right encoder", rightEncoder);

		driver_p = .025;
		driver_i = 0;
		driver_d = .004;

		gyro_p = 0.05;
		gyro_i = 0;
		gyro_d = 0.08;

		gyroPID = new PIDController(gyro_p, gyro_i, gyro_d, new PIDSource() {

			@Override
			public void setPIDSourceType(PIDSourceType pidSource) {
			}

			@Override
			public PIDSourceType getPIDSourceType() {
				return PIDSourceType.kDisplacement;
			}

			@Override
			public double pidGet() {
				return getRotationX();
			}

		}, new PIDOutput() {

			@Override
			public void pidWrite(double output) {
				setPower(output, -output);
			}
		});
		gyroPID.setOutputRange(-1, 1);

		SmartDashboard.putData("Gyro PID", gyroPID);
		SmartDashboard.putData("DRIVE SUBSYSTEM", this);
	}

	public static DriveSubsystem getInstance() {
		if (instance == null)
			instance = new DriveSubsystem();
		return instance;
	}

	public double getAccerometerX() {
		return accelerometer.getX();
	}

	public double getAccerometerY() {
		return accelerometer.getY();
	}

	public double getAccerometerZ() {
		return accelerometer.getZ();

	}

	public double getRotationX() {
		return gyro.getRotation();
	}

	public void setLeftPower(double power) {
		left.set(Robot.rangeCheck(power));
	}

	public void setRightPower(double speed) {
		right.set(Robot.rangeCheck(speed));
	}

	public void rotateWithPower(double power) {
		right.set(-power);
		left.set(power);
	}

	public void encodersReset() {
		rightEncoder.reset();
		leftEncoder.reset();
	}

	public void setPower(double right, double left) {
		this.left.set(Robot.rangeCheck(left));
		this.right.set(Robot.rangeCheck(right));
	}

	public double getLeftCurrent() {
		return (panel.getCurrent(RobotMap.DriveTrain.DRIVE_LEFT_PDP_1)
				+ panel.getCurrent(RobotMap.DriveTrain.DRIVE_LEFT_PDP_2)
				+ panel.getCurrent(RobotMap.DriveTrain.DRIVE_LEFT_PDP_3)) / 3;
	}

	public double getRightCurrent() {
		return (panel.getCurrent(RobotMap.DriveTrain.DRIVE_RIGHT_PDP_1)
				+ panel.getCurrent(RobotMap.DriveTrain.DRIVE_RIGHT_PDP_2)
				+ panel.getCurrent(RobotMap.DriveTrain.DRIVE_RIGHT_PDP_3)) / 3;
	}

	public double getLeftPower() {
		return left.get();
	}

	public double getRightPower() {
		return right.get();
	}

	public double getLeftSpeed() {
		return leftEncoder.getRate() * CNT_TO_DIST;
	}

	public double getRightSpeed() {
		return rightEncoder.getRate() * CNT_TO_DIST;
	}

	public double getLeftAcceleration() {
		return getLeftSpeed() / leftEncoder.getPeriod();
	}

	public double getRightAcceleration() {
		return getRightSpeed() / rightEncoder.getPeriod();
	}

	public double getAvgSpeed() {
		return (getLeftSpeed() + getRightSpeed()) / 2;
	}

	private double lastSpeed;

	public double getLeftFilteredSpeed() {
		double speed = countsToDist(getLeftSpeed());

		if (Math.abs(speed - lastSpeed) > 290)
			speed = lastSpeed;
		lastSpeed = speed;
		return speed;
	}

	public double getRightFilteredSpeed() {
		double speed = countsToDist(getRightSpeed());

		if (Math.abs(speed - lastSpeed) > 290)
			speed = lastSpeed;
		lastSpeed = speed;
		return speed;
	}

	public double getLeftDistance() {
		return -leftEncoder.getDistance() * CNT_TO_DIST;
	}

	public double getRightDistance() {
		return rightEncoder.getDistance() * CNT_TO_DIST;
	}

	public double getAvgDistance() {
		return (getLeftDistance() + getRightDistance()) / 2;
	}

	public double getPIDError() {
		return gyroPID.getError();
	}

	public double countsToDist(double counts) {
		return counts;// counts * .0393686;
	}

	public int getLightReading() {
		return lightSensor.getValue();
	}

	public boolean onDefense() {
		return getLightReading() < RobotMap.DriveTrain.LIGHT_THRESHOLD;// Be
																		// aware
																		// that
																		// the
																		// greater
																		// than
																		// or
																		// less
																		// than
																		// may
																		// need
																		// to be
																		// switched
	}

	public void updatePeriodic() {

		if (Robot.isDebugState) {

			SmartDashboard.putNumber("Light Sensor Value", getLightReading());
			SmartDashboard.putBoolean("Light Sensor On Defense", onDefense());

			SmartDashboard.putNumber("Driver Left Power", getLeftPower());
			SmartDashboard.putNumber("Driver Right Powers", getRightPower());

			SmartDashboard.putNumber("Accelerometer X", getAccerometerX());
			SmartDashboard.putNumber("Accelerometer Y", getAccerometerY());
			SmartDashboard.putNumber("Accelerometer Z", getAccerometerZ());

			SmartDashboard.putNumber("Left Speed", countsToDist(getLeftSpeed()));
			SmartDashboard.putNumber("Right Speed", countsToDist(getRightSpeed()));

			SmartDashboard.putNumber("Rotation X", getRotationX());

			SmartDashboard.putNumber("Left Distance", getLeftDistance());
			SmartDashboard.putNumber("Right Distance", getRightDistance());
			SmartDashboard.putNumber("Avg Distance", countsToDist(getAvgDistance()));

			SmartDashboard.putNumber("Left Speed", countsToDist(getLeftSpeed()));
			SmartDashboard.putNumber("Right Speed", countsToDist(getRightSpeed()));

			SmartDashboard.putNumber("Left Acceleration", countsToDist(getLeftAcceleration()));
			SmartDashboard.putNumber("Right Acceleration", countsToDist(getRightAcceleration()));
		}

		SmartDashboard.putBoolean("DRIVING COLLECTOR FIRST", !isForward);
	}

	protected void initDefaultCommand() {
		setDefaultCommand(new DriveWithGamepad());
	}
}
