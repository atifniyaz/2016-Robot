package com.techhounds.commands.auton;

import com.techhounds.RobotMap;
import com.techhounds.commands.drive.DriveDistance;
import com.techhounds.commands.gyro.RotateToPreviousAngle;
import com.techhounds.commands.gyro.SaveCurrentAngle;

import edu.wpi.first.wpilibj.command.CommandGroup;

/**
 *
 */
public class CrossDefense extends CommandGroup {
	
	//This command assumes the robot is in front of the defense, and only needs to drive forward over the obstacle
	
	private static double defaultCrossingSpeed = .5;
	private static double defaultDistance = 143.5;
	
	public CrossDefense(){
		this(defaultDistance, defaultCrossingSpeed);
	}
	
	public CrossDefense(double crossingSpeed, boolean isDifferentCrossingSpeed){
		this(defaultDistance, crossingSpeed);
	}
	
	public CrossDefense(double distance){
		this(distance, defaultCrossingSpeed);
	}
	
	public CrossDefense(double distance, double crossingSpeed){
		 this(distance, crossingSpeed, RobotMap.DriveTrain.MIN_STRAIGHT_POWER);
    }
	public CrossDefense(double distance, double crossingMaxSpeed, double crossingMinSpeed){
		addSequential(new SaveCurrentAngle());
		 addSequential(new DriveDistance(distance, crossingMaxSpeed, crossingMinSpeed));
		 addSequential(new RotateToPreviousAngle());
	}
	public CrossDefense(double distance, double crossingMaxSpeed, double crossingMinSpeed, double timeout){
		addSequential(new SaveCurrentAngle());
		addSequential(new DriveDistance(distance, crossingMaxSpeed, crossingMinSpeed, timeout));
		addSequential(new RotateToPreviousAngle());
	}
}
