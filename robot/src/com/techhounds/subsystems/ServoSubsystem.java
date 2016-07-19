package com.techhounds.subsystems;

import com.techhounds.Robot;
import com.techhounds.RobotMap;
import com.techhounds.lib.util.HoundSubsystem;

import edu.wpi.first.wpilibj.Servo;
import edu.wpi.first.wpilibj.livewindow.LiveWindow;

public class ServoSubsystem extends HoundSubsystem {

	private Servo servo;
	private double min, max;
	private static ServoSubsystem instance_winch_enable, instance_winch_lock, instance_scissor_one,
			instance_scissor_two;

	public ServoSubsystem(int port) {
		this(port, 0, 1);
	}

	public ServoSubsystem(int port, double min, double max) {
		servo = new Servo(port);
		LiveWindow.addActuator("Servo_" + port, "Servo_" + port, servo);
		this.min = min;
		this.max = max;

	}

	public static ServoSubsystem getWinchEnable() {
		return instance_winch_enable == null ? instance_winch_enable = new ServoSubsystem(RobotMap.Servo.WINCH_ENABLE,
				RobotMap.Servo.WINCH_ENABLE_MIN, RobotMap.Servo.WINCH_ENABLE_MAX) : instance_winch_enable;
	}

	public static ServoSubsystem getWinchLock() {
		return instance_winch_lock == null ? instance_winch_lock = new ServoSubsystem(RobotMap.Servo.WINCH_LOCK,
				RobotMap.Servo.WINCH_LOCK_MIN, RobotMap.Servo.WINCH_LOCK_MAX) : instance_winch_lock;
	}

	public static ServoSubsystem getScissorOne() {
		return instance_scissor_one == null ? instance_scissor_one = new ServoSubsystem(RobotMap.Servo.SCISSOR_ONE,
				RobotMap.Servo.SCISSOR_ONE_MIN, RobotMap.Servo.SCISSOR_ONE_MAX) : instance_scissor_one;
	}

	public static ServoSubsystem getScissorTwo() {
		return instance_scissor_two == null ? instance_scissor_two = new ServoSubsystem(RobotMap.Servo.SCISSOR_TWO,
				RobotMap.Servo.SCISSOR_TWO_MIN, RobotMap.Servo.SCISSOR_TWO_MAX) : instance_scissor_two;
	}

	public void setPosition(double position) {
		servo.set(Robot.rangeCheck(position, min, max));
	}

	public void set(boolean setMax) {
		servo.set(Robot.rangeCheck(setMax ? max : min));
	}

	public double getPosition() {
		return servo.get();
	}

	public boolean getIsMax() {
		return getPosition() >= max;
	}

	public void initDefaultCommand() {

	}

	@Override
	public void updatePeriodic() {

	}
}
