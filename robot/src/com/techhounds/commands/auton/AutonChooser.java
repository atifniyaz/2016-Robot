package com.techhounds.commands.auton;

import com.techhounds.RobotMap;
import com.techhounds.RobotMap.Angler;
import com.techhounds.commands.angler.SetAnglerPosition;
import com.techhounds.commands.auton.IRIAutonChooser.Start;
import com.techhounds.commands.auton_routines.LowBarTwoBallLine;
import com.techhounds.commands.collector.SetCollectorPower;
import com.techhounds.commands.collector.WaitForBeanBreak;
import com.techhounds.commands.drive.DriveDistance;
import com.techhounds.commands.drive.DriveDistanceStraight;
import com.techhounds.commands.drive_auton.FinishedControlLoop;
import com.techhounds.commands.drive_auton.RunControlLoop;
import com.techhounds.commands.drive_auton.StartControlLoop;
import com.techhounds.commands.drive_auton.StopControlLoop;
import com.techhounds.commands.drive_auton.WaitForControlLoop;
import com.techhounds.commands.drive_auton.WriteControlLoopHeading;
import com.techhounds.commands.gyro.RotateToLastAngle;
import com.techhounds.commands.gyro.RotateUsingGyro;
import com.techhounds.commands.gyro.SaveCurrentAngle;
import com.techhounds.commands.shooter.SetShooterPower;
import com.techhounds.commands.shooter.SetShooterSpeed;
import com.techhounds.subsystems.DriveSubsystem;

import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.CommandGroup;
import edu.wpi.first.wpilibj.command.WaitCommand;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class AutonChooser {

	private static AutonChooser instance;
	
	private SendableChooser chooseStart;
	private SendableChooser chooseDefense;
	private SendableChooser chooseBallOrigin;
	private SendableChooser choosePost;
	
	private AutonChooser() {
		setupDashboard();
	}
	
	public static AutonChooser getInstance() {
		return instance == null ? instance = new AutonChooser() : instance;
	}
	
	public enum Defense {
		LOW_BAR, PORTCULLIS, CHEVAL_DE_FRISE, MOAT, RAMPARTS,
		ROCK_WALL, ROUGH_TERRAIN, REACH_DEFENSE, DO_NOTHING
	}
	
	public enum BallOrigin {
		LINE, DO_NOTHING
	}
	
	public enum Post {
		STRAIGHTEN, CROSS, TWO_BALL, NOTHING, ONLY_TWO_BALL
	}
	
	private int getStart() {
		return ((Number) chooseStart.getSelected()).intValue();
	}
	
	private Defense getDefense() {
		return ((Defense) chooseDefense.getSelected());
	}
	
	private BallOrigin getBallOrigin() {
		return ((BallOrigin) chooseBallOrigin.getSelected());		
	}
	
	private Post getPost() {
		return ((Post) choosePost.getSelected());
	}

	public void setupDashboard() {
		chooseStart = new SendableChooser();
		chooseStart.addObject("Low Bar (1)", new Integer(1));
		chooseStart.addObject("Position 2", new Integer(2));
		chooseStart.addDefault("Position 3", new Integer(3));
		chooseStart.addObject("Position 4", new Integer(4));
		chooseStart.addObject("Secret Passage (5)", new Integer(5));
		
		chooseDefense = new SendableChooser();
		chooseDefense.addObject("A: Portcullis", Defense.PORTCULLIS);
		chooseDefense.addObject("A: Cheval De Frise", Defense.CHEVAL_DE_FRISE);
		chooseDefense.addObject("B: Moat", Defense.MOAT);
		chooseDefense.addObject("B: Ramparts", Defense.RAMPARTS);
		chooseDefense.addObject("D: Rock Wall", Defense.ROCK_WALL);
		chooseDefense.addDefault("D: Rough Terrain", Defense.ROUGH_TERRAIN);
		chooseDefense.addObject("Low Bar", Defense.LOW_BAR);
		chooseDefense.addObject("Reach Defense", Defense.REACH_DEFENSE);
		chooseDefense.addObject("Do Nothing", Defense.DO_NOTHING);
		
		chooseBallOrigin = new SendableChooser();
		chooseBallOrigin.addObject("Ball From Midline", BallOrigin.LINE);
		chooseBallOrigin.addDefault("Do Nothing", BallOrigin.DO_NOTHING);
	
		choosePost = new SendableChooser();
		choosePost.addObject("Straighten Out", Post.STRAIGHTEN);
		choosePost.addObject("Cross Defenses For Midline", Post.CROSS);
		choosePost.addDefault("Do Nothing", Post.NOTHING);
		choosePost.addObject("Attempt Two Ball", Post.TWO_BALL);

		SmartDashboard.putData("ChooseStart", chooseStart);
		SmartDashboard.putData("ChooseDefense", chooseDefense);
		SmartDashboard.putData("ChooseBallOrigin", chooseBallOrigin);
		SmartDashboard.putData("ChoosePost", choosePost);
	}
	
	public boolean isValid(){
		return true;
	}
	
	public Command createAutonCommand() {
		return new AutonCommand();
	}
	
	private class AutonCommand extends CommandGroup {
		
		public AutonCommand() {
			this(getStart(), getDefense(), getBallOrigin(), getPost());
		}
		
		public AutonCommand(int start, Defense defense, BallOrigin ball, Post post) {
			requires(DriveSubsystem.getInstance());
			
			// Save Initial Angle
			if(post != Post.ONLY_TWO_BALL) {
				addParallel(new WriteControlLoopHeading());
				addSequential(new SaveCurrentAngle());
			}
			
			boolean isNotCrossTwo = post != Post.TWO_BALL && post != Post.CROSS && post != Post.ONLY_TWO_BALL;
			boolean isEitherCrossTwo = post == Post.TWO_BALL || post == Post.CROSS || post == Post.ONLY_TWO_BALL;
			boolean isTwoBall = post == Post.TWO_BALL || post == Post.ONLY_TWO_BALL;
			
			if(start == 1 && post == Post.TWO_BALL) {
				addSequential(new LowBarTwoBallLine());
				return;
			}
			
			if(ball == BallOrigin.LINE) {
				
				addParallel(new SetAnglerPosition(Angler.COLLECTING + 10));
		    	addParallel(new SetCollectorPower(0.8, true));
		    	addSequential(new WaitForBeanBreak(true));
		    	addSequential(new SetCollectorPower(0));
		    	
		    	adjustBall();
			}
			
			if(defense != Defense.LOW_BAR)
				addParallel(new SetAnglerPosition(Angler.UP));
			
			if(isEitherCrossTwo) {
				startShooter(defense, start, post);

				crossDefense(defense, true, isTwoBall, post, start);
				
			} else {

				crossDefense(defense, true, isTwoBall, post, start);
				startShooter(defense, start, post);
			}
			
			if(defense == Defense.RAMPARTS || defense == Defense.CHEVAL_DE_FRISE || defense == Defense.PORTCULLIS)
				addSequential(new SaveCurrentAngle(180, true));
			
			
			switch(start) {
				case 1:
					//addSequential(new SetAnglerPosition(Angler.UP, 1.0));
					//addSequential(new RotateUsingGyro(58), 2); //44

					//if(isNotCrossTwo)
					//	addSequential(new DriveDistance(44, RobotMap.Defenses.TO_DEFENSE_SPEED, RobotMap.DriveTrain.MIN_STRAIGHT_POWER, 2));
					break;
				case 2:
					addSequential(new DriveDistanceStraight(60, 1, RobotMap.DriveTrain.MIN_STRAIGHT_POWER, 3.0, true));
					addSequential(new RotateUsingGyro(53), 0.6);
				case 3:
					addSequential(new RotateToLastAngle(10, 1, true), 0.5);
				case 4:
					break;
				case 5:
					addSequential(new RotateToLastAngle(-20, 1, true), 0.5);
					break;
				default:
					break;
			}

			if(isTwoBall){
				
				/* New Rotate Using Vision
				addSequential(new StartVisionLoop());
				addSequential(new FinishedVisionLoop(), 1.0);
				 
				fireShooter();
				
				addSequential(new StopVisionLoop());
				 */
				addSequential(new WaitCommand(.2));
				addSequential(new RotateUsingVisionContinuous(1.8));
				addSequential(new WaitCommand(.2));
				fireShooter();
			}else if(post == Post.ONLY_TWO_BALL) {
				addSequential(new WaitCommand(.2));
				addSequential(new RotateUsingVisionContinuous(2.05));
				addSequential(new WaitCommand(.2));
				fireShooter();
			}else{
				addSequential(new RotateUsingVisionContinuous(3.0));
				addSequential(new WaitCommand(.25));
				
				fireShooter();
			}
			
			if(post == Post.ONLY_TWO_BALL)
				post = Post.STRAIGHTEN;
			
			if(post == Post.STRAIGHTEN) {
				addSequential(new RotateToLastAngle());
			} else if(post == Post.TWO_BALL || post == Post.CROSS) {
				
				addSequential(new RotateToLastAngle(), 0.6);
				
				if(getStart() == 2) {
					addSequential(new DriveDistanceStraight(-60, -1, -RobotMap.DriveTrain.MIN_STRAIGHT_POWER, 3.0, true));
				}
				
				
				crossDefense(defense, false, isEitherCrossTwo, null, start);
				
				if(post == Post.TWO_BALL) {
					addParallel(new SetCollectorPower(0.75, true));
					addParallel(new AnglerWithDelay(Angler.COLLECTING, 0.15));
					
					if(getStart() == 5) {
						
						addParallel(new RunControlLoop("PositionFive-Back"));
						addSequential(new WaitForBeanBreak(true));
						addSequential(new SetCollectorPower(0.0,true));
						
						addSequential(new RunControlLoop("PositionFive-Forward"));
					} else {
						addSequential(new DriveBackAndCheckForBall(-36, -0.4));
						addSequential(new SetCollectorPower(0.0, true));
					}
					addSequential(new SetAnglerPosition(Angler.UP), .25);
					adjustBall();
					addSequential(new AutonCommand(getStart(), getDefense(), BallOrigin.DO_NOTHING, Post.ONLY_TWO_BALL));
				}
			}
		}
		
		public void adjustBall() {

			CommandGroup adjBall = new CommandGroup();
			adjBall.addParallel(new SetCollectorPower(-.4, true));
			adjBall.addSequential(new WaitForBeanBreak(false), .375);
			adjBall.addParallel(new SetCollectorPower(.4, true));
			adjBall.addSequential(new WaitForBeanBreak(true),.375);
			
			addParallel(adjBall);
		}
		
		public void crossDefense(Defense defense, boolean isForward, boolean isTwoBall, Post post, int start) {
			
			double factor = isForward ? 1 : -1;
			double power = isForward ? 1 : -1;
			double dist = 0;
			if(post == Post.ONLY_TWO_BALL) {
				dist = 24;
			} else if(start == 5 && defense == Defense.ROUGH_TERRAIN && !isForward) {
				dist = 25;
			}else if(defense == Defense.ROUGH_TERRAIN && !isForward) {
				dist = 12;
			}
			
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
							factor * RobotMap.Defenses.MOAT_DISTANCE + dist, 
							factor * RobotMap.Defenses.MOAT_SPEED, 
							factor * RobotMap.DriveTrain.MIN_STRAIGHT_POWER, 5.0, true));
					break;
					
				case RAMPARTS:
					addSequential(new DriveDistanceStraight(
							-RobotMap.Defenses.RAMPARTS_DISTANCE + dist, 
							-RobotMap.Defenses.RAMPARTS_SPEED, 
							-RobotMap.DriveTrain.MIN_STRAIGHT_POWER, 5.0, true));
					
					if(isForward)
						addSequential(new RotateToLastAngle(180), 3);
					
					break;
					
				case ROCK_WALL:
					/*
					if(isForward) {
						addSequential(new RunControlLoop("RockWall-Cross"));
					} else {
						addSequential(new RunControlLoop("RockWall-Back"));
					}*/
					addSequential(new DriveDistanceStraight(
							factor * RobotMap.Defenses.ROCK_WALL_DISTANCE + dist, 
							isTwoBall ? factor * RobotMap.Defenses.ROCK_WALL_SPEED : factor * 0.6 , 
							factor * RobotMap.DriveTrain.MIN_STRAIGHT_POWER, 5.0, true));
					break;
					
				case ROUGH_TERRAIN:
					addSequential(new DriveDistanceStraight(
							factor * RobotMap.Defenses.ROUGH_TERRAIN_DISTANCE + dist, 
							isTwoBall ? power * RobotMap.Defenses.ROUGH_TERRAIN_SPEED : factor * 0.75, 
							factor * RobotMap.DriveTrain.MIN_STRAIGHT_POWER, 5.0, true));
					break;
			
				case CHEVAL_DE_FRISE:
					addSequential(new CrossCDF());
					break;
					
				default:
					addSequential(new DriveDistance(
							factor * RobotMap.Defenses.DEFENSE_DISTANCE, 
							factor * RobotMap.Defenses.TO_DEFENSE_SPEED));
					return;
			}
		}
		
		public void startShooter(Defense defense, int start, Post post) {

			if(defense == Defense.LOW_BAR){
				addSequential(new SetShooterSpeed(69, true));
			} else if(defense == Defense.CHEVAL_DE_FRISE) {
				addSequential(new SetShooterSpeed(69, true));
			}else {
				addSequential(new SetShooterSpeed(71, true));
			}
			
			if(defense == Defense.MOAT) {
				addSequential(new SetShooterSpeed(69, true));
			}
			
			if(defense == Defense.ROUGH_TERRAIN && (post == Post.TWO_BALL || 
					post == Post.ONLY_TWO_BALL)) {
				addSequential(new SetShooterSpeed(72.3, true));
			}
			if(start == 3) {
				addSequential(new SetShooterSpeed(71.8, true));
			} else if(start == 5) {
				addSequential(new SetShooterSpeed(71.5, true));
			}
		}
		
		public void fireShooter() {

			addSequential(new SetCollectorPower(1, true));
			addSequential(new WaitForBeanBreak(false));
			addParallel(new SetCollectorPower(0, true));
			addSequential(new SetShooterPower());
		}
	}
}
