package com.techhounds.commands.auton;

import com.techhounds.RobotMap;
import com.techhounds.RobotMap.Angler;
import com.techhounds.commands.angler.SetAnglerPosition;
import com.techhounds.commands.drive.DriveDistance;
import com.techhounds.commands.gyro.RotateToLastAngle;
import com.techhounds.commands.gyro.RotateToPreviousAngle;
import com.techhounds.commands.gyro.RotateUsingGyro;
import com.techhounds.commands.gyro.SaveCurrentAngle;
import com.techhounds.commands.shooter.SetShooterSpeed;

import edu.wpi.first.wpilibj.command.CommandGroup;
import edu.wpi.first.wpilibj.command.WaitCommand;

/**
 *
 */
public class CrossCDF extends CommandGroup {
    
    public  CrossCDF() {
    	
    	addSequential(new SaveCurrentAngle());//saves angle
    	addSequential(new SetAnglerPosition(Angler.UP));
    	addSequential(new DriveDistance(-RobotMap.Defenses.CDF_DISTANCE_1 + 6 + 6, -RobotMap.Defenses.CDF_SPEED_1, -RobotMap.DriveTrain.MIN_STRAIGHT_POWER, 3));//drives up on to ramp to its position
    	addSequential(new SetAnglerPosition(Angler.DOWN), .5);//lowers collector onto defense
    	addParallel(new DriveDistance(-RobotMap.Defenses.CDF_DISTANCE_2 - 6, -RobotMap.Defenses.CDF_SPEED_2, -RobotMap.DriveTrain.MIN_STRAIGHT_POWER, 3));//drives over defense and waits a moment before...
    	addSequential(new AnglerWithDelay(Angler.UP, .5), 2);//...raising collector to prevent damage to it
    	addSequential(new WaitCommand(0.25));
    	addSequential(new SaveCurrentAngle(180, true));
    	addSequential(new RotateToLastAngle(0, 1, true), 2);
    }
}
