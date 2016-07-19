package com.techhounds.commands.gyro;

public class RotateToLastAngle extends RotateUsingGyro {

	public RotateToLastAngle() {
		super(0);
	}
	
	public RotateToLastAngle(double angle) {
		super(angle);
		// TODO Auto-generated constructor stub
	}
	
	public RotateToLastAngle(double angle, double maxPower, boolean doesntMatter) {
		super(angle, maxPower, doesntMatter);
	}
	
	@Override
	public void initialize() {
		angle = gyro.getStoredAngle() - gyro.getRotation() + angle;
		super.initialize();
	}

}
