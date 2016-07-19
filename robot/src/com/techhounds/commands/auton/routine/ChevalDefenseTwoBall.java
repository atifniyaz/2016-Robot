package com.techhounds.commands.auton.routine;

import com.techhounds.AutoMap;
import com.techhounds.RobotMap;
import com.techhounds.RobotMap.Angler;
import com.techhounds.commands.angler.AnglerWithDelay;
import com.techhounds.commands.angler.SetAnglerPosition;
import com.techhounds.commands.auton.AutonChooser.Defense;
import com.techhounds.commands.collector.SetCollectorPower;
import com.techhounds.commands.collector.WaitForBeanBreak;
import com.techhounds.commands.drive.DriveDistance;
import com.techhounds.commands.drive.DriveDistanceStraight;
import com.techhounds.commands.drive_profile.FinishedControlLoop;
import com.techhounds.commands.drive_profile.RunControlLoop;
import com.techhounds.commands.drive_profile.StartControlLoop;
import com.techhounds.commands.drive_profile.StopControlLoop;
import com.techhounds.commands.drive_profile.WaitForControlLoop;
import com.techhounds.commands.gyro.RotateToLastAngle;
import com.techhounds.commands.gyro.SaveCurrentAngle;
import com.techhounds.commands.shooter.SetShooterPower;
import com.techhounds.commands.shooter.SetShooterSpeed;
import com.techhounds.commands.vision.RotateUsingVisionContinuous;

import edu.wpi.first.wpilibj.command.CommandGroup;
import edu.wpi.first.wpilibj.command.WaitCommand;

public class ChevalDefenseTwoBall extends CommandGroup {

	public ChevalDefenseTwoBall(Defense defense) {
		addSequential(new SaveCurrentAngle());//saves angle
    	addSequential(new SetAnglerPosition(Angler.UP));
    	addSequential(new DriveDistance(-AutoMap.CDF_DISTANCE_1 + 6 + 6, -AutoMap.CDF_SPEED_1, -RobotMap.DriveTrain.MIN_STRAIGHT_POWER, 3));//drives up on to ramp to its position
    	addSequential(new SetAnglerPosition(Angler.DOWN), .5);//lowers collector onto defense
    	addParallel(new DriveDistance(-AutoMap.CDF_DISTANCE_2 - 6, -AutoMap.CDF_SPEED_2, -RobotMap.DriveTrain.MIN_STRAIGHT_POWER, 3));//drives over defense and waits a moment before...
    	addSequential(new AnglerWithDelay(Angler.UP, .5), 2);//...raising collector to prevent damage to it
    	addSequential(new WaitCommand(0.25));
    	addParallel(new SetShooterSpeed(74.5, true));
    	addSequential(new SaveCurrentAngle(180, true));
    	addSequential(new RunControlLoop("ChevalToDefense"));
    	addSequential(new WaitCommand(.1));
    	addSequential(new RotateUsingVisionContinuous(), 1.25);
    	addSequential(new WaitCommand(.1));
    	
    	fireShooter();
    	
    	addParallel(new SetCollectorPower(0.8, true));
    	addParallel(new SetAnglerPosition(RobotMap.Angler.COLLECTING), 0.1);
    	crossDefense(defense, false);
    	
    	addSequential(new WaitForBeanBreak(true));
    	addParallel(new SetCollectorPower(0, true));
    	addParallel(new SetAnglerPosition(RobotMap.Angler.UP), 0.1);
    	crossDefense(defense, true);
    	
    	addSequential(new WaitCommand(.1));
    	addSequential(new RotateUsingVisionContinuous(), 1.25);
    	addSequential(new WaitCommand(.1));
    	
    	fireShooter();
	}
	
	public void crossDefense(Defense defense, boolean isForward) {
		
		double factor = isForward ? 1 : -1;
		
		switch(defense) {
			case LOW_BAR:
				
				if(isForward) {
					addParallel(new SetAnglerPosition(Angler.COLLECTING));
					addSequential(new RunControlLoop("LowBarAutoBall-Cross"));
				} else {
					addSequential(new StartControlLoop("LowBarAutoBall-Back"));
					addParallel(new SetAnglerPosition((RobotMap.Angler.COLLECTING + RobotMap.Angler.DOWN) / 2), 0.1);
					addSequential(new WaitForControlLoop(215));
					addParallel(new SetAnglerPosition(RobotMap.Angler.UP), 0.1);
					addSequential(new FinishedControlLoop());
					addSequential(new StopControlLoop());
				}
				
				break;
			
			case MOAT:
				addSequential(new DriveDistanceStraight(
						factor * AutoMap.MOAT_DISTANCE, 
						factor * AutoMap.MOAT_SPEED, 
						factor * RobotMap.DriveTrain.MIN_STRAIGHT_POWER, 5.0, true));
				break;
				
			case RAMPARTS:
				addSequential(new DriveDistanceStraight(
						-AutoMap.RAMPARTS_DISTANCE, 
						-AutoMap.RAMPARTS_SPEED, 
						-RobotMap.DriveTrain.MIN_STRAIGHT_POWER, 5.0, true));
				
				if(isForward)
					addSequential(new RotateToLastAngle(180), 3);
				
				break;
				
			case ROCK_WALL:
				addSequential(new DriveDistanceStraight(
						factor * AutoMap.ROCK_WALL_DISTANCE, 
						factor * AutoMap.ROCK_WALL_SPEED , 
						factor * RobotMap.DriveTrain.MIN_STRAIGHT_POWER, 5.0, true));
				break;
				
			case ROUGH_TERRAIN:
				addSequential(new DriveDistanceStraight(
						factor * AutoMap.ROUGH_TERRAIN_DISTANCE, 
						factor * AutoMap.ROUGH_TERRAIN_SPEED, 
						factor * RobotMap.DriveTrain.MIN_STRAIGHT_POWER, 5.0, true));
				break;
		
			case CHEVAL_DE_FRISE:
				addSequential(new CrossCDF());
				break;
				
			default:
				addSequential(new DriveDistance(
						factor * AutoMap.DEFENSE_DISTANCE, 
						factor * AutoMap.TO_DEFENSE_SPEED));
				return;
		}
	}
	
	public void fireShooter() {

		addSequential(new SetCollectorPower(1, true));
		addSequential(new WaitForBeanBreak(false));
		addParallel(new SetCollectorPower(0, true));
		addSequential(new SetShooterPower());
	}
}
