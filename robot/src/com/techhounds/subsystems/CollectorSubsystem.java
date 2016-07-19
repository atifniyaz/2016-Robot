package com.techhounds.subsystems;

import com.techhounds.Robot;
import com.techhounds.RobotMap;
import com.techhounds.lib.util.HoundMath;
import com.techhounds.lib.util.HoundSubsystem;

import edu.wpi.first.wpilibj.CANTalon;
import edu.wpi.first.wpilibj.PowerDistributionPanel;
import edu.wpi.first.wpilibj.livewindow.LiveWindow;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class CollectorSubsystem extends HoundSubsystem {

	private static CollectorSubsystem instance;
	private CANTalon motor;
	private PowerDistributionPanel panel;

	private CollectorSubsystem() {

		panel = new PowerDistributionPanel();

		motor = new CANTalon(RobotMap.Collector.MOTOR);
		motor.setInverted(getInverted());
		motor.enableBrakeMode(true);
		LiveWindow.addActuator("collector", "motor", motor);
	}

	public void setPower(double power) {
		motor.set(HoundMath.checkRange(power));
	}

	public void setToPercentVBus() {
		motor.changeControlMode(CANTalon.TalonControlMode.PercentVbus);
	}

	public void stopPower() {
		motor.set(0);
	}

	public double getCurrent() {
		return panel.getCurrent(RobotMap.Collector.PDP);
	}

	public double getPower() {
		return motor.get();
	}

	public boolean getIsIn() {
		if (getPower() > 0) {
			return true;
		} else {
			return false;
		}
	}

	public boolean getIsOut() {
		if (!getIsIn() && getPower() != 0) {
			return true;
		} else {
			return false;
		}
	}

	public boolean getInverted() {
		return RobotMap.Collector.IS_INVERTED;
	}

	public void updatePeriodic() {
		if (Robot.isDebugState) {
			SmartDashboard.putBoolean("Collector_Is_Going_Inward", getIsIn());
			SmartDashboard.putBoolean("Collector_Is_Going_Outward", getIsOut());
			SmartDashboard.putNumber("Collector_Power", getPower());
			SmartDashboard.putNumber("Collector Current", getCurrent());
		}
	}

	public static CollectorSubsystem getInstance() {
		return instance == null ? instance = new CollectorSubsystem() : instance;
	}

	protected void initDefaultCommand() {

	}
}