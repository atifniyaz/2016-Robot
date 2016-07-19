package com.techhounds;

public interface RobotMap {
	
	interface JoystickPort {
		final int DRIVER = 0;
		final int OPERATOR = 1;
	}
	
	interface Angler {
		final boolean IS_INVERTED = true;
		int MOTOR = 23;
		
		double UP = 382 + 96;
		double COLLECTING = 643 + 96 - 20;
		double DOWN = 732 + 96;
		double AUTON_COLLECTING = 670;
	}
	
	interface Flashlight {
		final int FLASHLIGHT = 0;
	}
	
	interface Collector{
		final int MOTOR = 17;
		final boolean IS_INVERTED = false;
		final int PDP = 11;
		final int BEAM_BREAK_DIO = 5;
		
		final double inPower = .6, outPower = -1;
	}
	
	interface Shooter{
		final int MOTOR = 13;
		final boolean IS_INVERTED = true;
		final int DIO = 4;
	}
	
	interface DriveTrain {
		final int DRIVE_LEFT_MOTOR = 1;
		final boolean DRIVE_LEFT_IS_INVERTED = false;
		final int DRIVE_RIGHT_MOTOR = 0;
		final boolean DRIVE_RIGHT_IS_INVERTED = true;
		final int ENC_RIGHT_A = 2;
		final int ENC_LEFT_A = 0;
		final int ENC_RIGHT_B = 3;
		final int ENC_LEFT_B = 1;
		final int LIGHT_SENSOR = 0;
		
		final int DRIVE_LEFT_PDP_1 = 0;
		final int DRIVE_LEFT_PDP_2 = 1;
		final int DRIVE_LEFT_PDP_3 = 2;
		final int DRIVE_RIGHT_PDP_1 = 13;
		final int DRIVE_RIGHT_PDP_2 = 14;
		final int DRIVE_RIGHT_PDP_3 = 15;
		
		final double MIN_TURN_POWER = .45;
		final double MIN_STRAIGHT_POWER = .35;
		final int LIGHT_THRESHOLD = 280;
		
		final double MAX_VELOCITY = 145;
		final double MAX_ACCELERATION = 72.5;
	}
	
	interface Ultrasonic {
		final int ULTRASONIC = -1;
	}
	
	interface LightSensor{
		final int LIGHT_SENSOR = -1;
		
	}
	
	interface Servo {
		final int WINCH_ENABLE = 3;
		final int WINCH_LOCK = 2;
		final int SCISSOR_ONE = 5;
		final int SCISSOR_TWO = -1;
		final double WINCH_ENABLE_MAX = .5;
		final double WINCH_ENABLE_MIN = .2;
		final double WINCH_LOCK_MAX = 1;
		final double WINCH_LOCK_MIN = 0;
		final double SCISSOR_ONE_MAX = 0.9;
		final double SCISSOR_ONE_MIN = 0.05;
		final double SCISSOR_TWO_MAX = 1;
		final double SCISSOR_TWO_MIN = 0;
		final boolean WINCH_ENABLE_IS_UP_DEFAULT = true;
		final boolean WINCH_LOCK_IS_UP_DEFAULT = false;
		final boolean SCISSOR_ONE_IS_UP_DEFAULT = false;
		final boolean SCISSOR_TWO_IS_UP_DEFAULT = false;
	}
	
	interface LED {
		final static int DIO_MODE_0 = 10;
		final static int DIO_MODE_1 = 11;
		
		final static int DIO_COLLECTOR = 13;
		final static int DIO_DIRECTION = 17;
	}
}
