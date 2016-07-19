package com.techhounds;

public interface RobotMap {
	
	interface OI_Constants {
		final int DRIVER_PORT = 0;
		final int OPERATOR_PORT = 1;
	}
	
	interface Angler {
		final boolean IS_INVERTED = true;
		int MOTOR = 23;
		
		double ANGLER_FORWARD_LIMIT = 0;
		double ANGLER_REVERSE_LIMIT = 1;
		
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
	
	interface Defenses {
		//If a double has DONE after it, please don't change it, as that should be an accurate value for the variable.
		//In inches, objective should be to cross a defense and end with wheels just past it, around 125 in.
		//Standardized, back of robot is 74 in from front of defense.
		
		final double MOAT_SPEED					= 1.0;//.48;//DONE
		final double MOAT_DISTANCE 				= 165 - 8;// + 48; + 45;//DONE
		
		final double LOW_BAR_SPEED 				= .8;//DONE
		final double LOW_BAR_DISTANCE 			= 127.5 + 16 + 24 + 36 + 12;//DONE

		final double RAMPARTS_SPEED 			= .55;//DONE
		final double RAMPARTS_DISTANCE 			= 139.5 + 48 - 24;//DONE
		
		final double ROCK_WALL_SPEED 			= 1.0;//
		final double ROCK_WALL_DISTANCE			= 143.5 + 8;//
		
		final double ROUGH_TERRAIN_SPEED 		= 1.0;//SET
		final double ROUGH_TERRAIN_DISTANCE 	= 139.5 + 1;//
		
		final double PORTCULLIS_SPEED_1 		= .5;//Moving to Portcullis
		final double PORTCULLIS_SPEED_2 		= .3;//after lowering collector
		final double PORTCULLIS_SPEED_3 		= .75;//while opening Portcullis and after
		final double PORTCULLIS_DISTANCE_1 		= 60;//Moving to Portcullis
		final double PORTCULLIS_DISTANCE_2 		= 20;//after lowering collector
		final double PORTCULLIS_DISTANCE_3 		= 130;//while opening Portcullis and after
		
		final double CDF_SPEED_1 				= .8;//before lowering CDF
		final double CDF_SPEED_2 				= .9;//after
		final double CDF_DISTANCE_1 			= 46;//before lowering CDF
		final double CDF_DISTANCE_2				= 130;//after
		
		final double TO_DEFENSE_SPEED			= .5;//DONE
		final double DEFENSE_DISTANCE			= 45;//DONE
	}
	
	interface LED {
		final static int DIO_MODE_0 = 10;
		final static int DIO_MODE_1 = 11;
		
		final static int DIO_COLLECTOR = 13;
		final static int DIO_DIRECTION = 17;
	}
}
