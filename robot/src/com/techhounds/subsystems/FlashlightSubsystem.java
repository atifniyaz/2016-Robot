package com.techhounds.subsystems;

import com.techhounds.RobotMap;
import com.techhounds.lib.util.HoundSubsystem;

import edu.wpi.first.wpilibj.Relay;

public class FlashlightSubsystem extends HoundSubsystem {

	private Relay flashlight;
	private static FlashlightSubsystem instance;

	private FlashlightSubsystem() {
		flashlight = new Relay(RobotMap.Flashlight.FLASHLIGHT);
		flashlight.setDirection(Relay.Direction.kForward);
	}

	public static FlashlightSubsystem getInstance() {
		return instance == null ? instance = new FlashlightSubsystem() : instance;
	}

	public void on() {
		flashlight.set(Relay.Value.kOn);
	}

	public void off() {
		flashlight.set(Relay.Value.kOff);
	}

	public void toggle() {
		flashlight.set(flashlight.get() == Relay.Value.kOn ? Relay.Value.kOff : Relay.Value.kOn);
	}

	public void initDefaultCommand() {

	}

	@Override
	public void updatePeriodic() {

	}
}
