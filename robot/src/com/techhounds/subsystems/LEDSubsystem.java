package com.techhounds.subsystems;

import com.techhounds.RobotMap;
import com.techhounds.commands.SetLEDMode;
import com.techhounds.lib.util.HoundSubsystem;

import edu.wpi.first.wpilibj.DigitalOutput;
import edu.wpi.first.wpilibj.livewindow.LiveWindow;

public class LEDSubsystem extends HoundSubsystem {

	private DigitalOutput led, led2, collect;

	public enum mode {
		FORWARDUP, REVERSEUP, FORWARDDOWN, REVERSEDOWN
	};

	public static LEDSubsystem instance;

	private LEDSubsystem() {
		led = new DigitalOutput(RobotMap.LED.DIO_MODE_0);
		led2 = new DigitalOutput(RobotMap.LED.DIO_MODE_1);
		collect = new DigitalOutput(RobotMap.LED.DIO_COLLECTOR);

		LiveWindow.addActuator("LED", "Mode Bit 0", led);
		LiveWindow.addActuator("LED", "Mode Bit 1", led2);
		LiveWindow.addActuator("LED Collector", "Collector", collect);
	}

	public void set(boolean state, boolean state2) {
		led.set(state);
		led2.set(state2);
	}

	public void setCollect(boolean state) {
		collect.set(state);
	}

	public void initDefaultCommand() {
		setDefaultCommand(new SetLEDMode());
	}

	public static LEDSubsystem getInstance() {
		return instance == null ? instance = new LEDSubsystem() : instance;
	}

	@Override
	public void updatePeriodic() {
		// TODO Auto-generated method stub

	}
}
