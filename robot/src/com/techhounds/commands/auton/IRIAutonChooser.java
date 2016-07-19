package com.techhounds.commands.auton;

import com.techhounds.RobotMap.Collector;
import com.techhounds.commands.angler.SetAnglerPosition;
import com.techhounds.commands.auton.AutonChooser.BallOrigin;
import com.techhounds.commands.auton.AutonChooser.Defense;
import com.techhounds.commands.collector.SetCollectorPower;
import com.techhounds.commands.collector.WaitForBeanBreak;
import com.techhounds.commands.drive_auton.FinishedControlLoop;
import com.techhounds.commands.drive_auton.RunControlLoop;
import com.techhounds.commands.drive_auton.StartControlLoop;
import com.techhounds.commands.drive_auton.StopControlLoop;
import com.techhounds.commands.drive_auton.WaitForControlLoop;
import com.techhounds.commands.drive_auton.WriteControlLoopHeading;
import com.techhounds.commands.shooter.SetShooterPower;
import com.techhounds.commands.shooter.SetShooterSpeed;
import com.techhounds.commands.vision.FinishedVisionLoop;
import com.techhounds.commands.vision.StartVisionLoop;
import com.techhounds.commands.vision.StopVisionLoop;
import com.techhounds.subsystems.DriveSubsystem;

import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.CommandGroup;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;

public class IRIAutonChooser {
	
	private static IRIAutonChooser instance;
	private SendableChooser start, cross, post, chevalReturn;
	
	private IRIAutonChooser() {

		start = new SendableChooser();
		start.addObject("Low Bar (1)", Start.POS_1);
		start.addObject("Position 2", Start.POS_2);
		start.addDefault("Position 3", Start.POS_3);
		start.addObject("Position 4", Start.POS_4);
		start.addObject("Secret Passage (5)", Start.POS_5);
		
		cross = new SendableChooser();
		cross.addObject("A: Cheval De Frise (Left Return)", Cross.CHEVAL_DE_FRISE_A);
		cross.addObject("A: Cheval De Frise (Right Return)", Cross.CHEVAL_DE_FRISE_B);
		cross.addObject("B: Moat", Cross.MOAT);
		cross.addObject("B: Ramparts", Cross.RAMPARTS);
		cross.addObject("D: Rock Wall", Cross.ROCK_WALL);
		cross.addDefault("D: Rough Terrain", Cross.ROUGH_TERRAIN);
		cross.addObject("Low Bar", Cross.LOW_BAR);
		
		chevalReturn = new SendableChooser();
		chevalReturn.addObject("Low Bar", Cross.LOW_BAR);
		chevalReturn.addObject("B: Moat", Cross.MOAT);
		chevalReturn.addObject("B: Ramparts", Cross.RAMPARTS);
		chevalReturn.addObject("C: Sally Port", Cross.SALLY_PORT);
		chevalReturn.addObject("C: Drawbridge", Cross.DRAWBRIDGE);
		chevalReturn.addObject("D: Rock Wall", Cross.ROCK_WALL);
		chevalReturn.addDefault("D: Rough Terrain", Cross.ROUGH_TERRAIN);
		
		post = new SendableChooser();
		post.addDefault("Straighten", Post.STRAIGHTEN);
		post.addObject("Go Back", Post.GO_BACK);
		post.addObject("Attempt Two Ball", Post.TWO_BALL);
	}
	
	public enum Start {
		POS_1, POS_2, POS_3, POS_4, POS_5, POS_2_CHEVAL_BACK
	}
	
	public enum Cross {
		LOW_BAR, 
		CHEVAL_DE_FRISE_A, CHEVAL_DE_FRISE_B,
		SALLY_PORT, DRAWBRIDGE,
		MOAT, RAMPARTS,
		ROUGH_TERRAIN, ROCK_WALL
	}
	
	public enum Post {
		STRAIGHTEN,
		GO_BACK,
		TWO_BALL
	}
	
	public static IRIAutonChooser getInstance() {
		return instance == null ? instance = new IRIAutonChooser() : instance;
	}
	
	private Start getStart() {
		return (Start) start.getSelected();
	}
	
	private Cross getCross() {
		return (Cross) cross.getSelected();
	}
	
	private Post getPost() {
		return (Post) post.getSelected();
	}
	
	private Cross getChevalReturn() {
		return (Cross) chevalReturn.getSelected();	
	}
	
	public Command getAutonCommand() {
		return new AutonomousRoutine();
	}

	private class AutonomousRoutine extends CommandGroup {
		
		public AutonomousRoutine() {
			this(getStart(), getCross(), getChevalReturn(), getPost());
		}
		
		public AutonomousRoutine(Start start, Cross cross, Post post) {
			this(start, cross, null, post);
		}
		
		public AutonomousRoutine(Start start, Cross cross, Cross back, Post post) {
			requires(DriveSubsystem.getInstance());
			
			if((start == Start.POS_1 || cross == Cross.LOW_BAR) && post == Post.TWO_BALL) {
				//addSequential(new IRITwoBallLowBar());
				return;
			}
			
			// Start Shooter
			addSequential(new SetShooterSpeed(71, true));
			
			// Start Drive Over Defense
			startDefenseDrive(cross, true);
			
			if(cross == Cross.CHEVAL_DE_FRISE_A) {
				startChevalDrive(start, true, true);
			} else if(cross == Cross.CHEVAL_DE_FRISE_B) {
				startChevalDrive(start, false, true);
			} else
				startPositionDrive(start, true);
			
			addSequential(new StartVisionLoop());
			addSequential(new FinishedVisionLoop(), 1.0);
			
			shoot();
			addSequential(new StopVisionLoop());
			
			
		}
		
		public void startDefenseDrive(Cross cross, boolean isForward) {
			String typeCross = isForward ? "-Cross" : "-Back";
			
			if(cross == Cross.CHEVAL_DE_FRISE_A || cross == Cross.CHEVAL_DE_FRISE_B) {
				//addSequential(new WriteControlLoopHeading(180));
			} else
				addSequential(new WriteControlLoopHeading());
			
			switch(cross) {
				case LOW_BAR:
					addSequential(new RunControlLoop("LowBar" + typeCross));
				case MOAT:
					addSequential(new RunControlLoop("Moat" + typeCross));
					break;
				case RAMPARTS:
					addSequential(new RunControlLoop("Ramparts" + typeCross));
					break;
				case ROCK_WALL:
					addSequential(new RunControlLoop("RockWall" + typeCross));
					break;
				case ROUGH_TERRAIN:
					addSequential(new RunControlLoop("RoughTerrain" + typeCross));
					break;
				case CHEVAL_DE_FRISE_A:
				case CHEVAL_DE_FRISE_B:
					addSequential(new CrossCDF());
					/*	Experimental Code
					addSequential(new StartControlLoop("ChevalDeFrise" + typeCross));
					addSequential(new WaitForControlLoop(50));
					addSequential(new SetAnglerPosition(Collector.COLLECTOR_DOWN));
					addSequential(new WaitForControlLoop(100));
					addSequential(new SetAnglerPosition(Collector.COLLECTOR_UP));
					addSequential(new FinishedControlLoop());
					addSequential(new StopControlLoop()); */
					break;
				case SALLY_PORT:
				case DRAWBRIDGE:
					addSequential(new RunControlLoop("SallyPort" + typeCross));
					break;
				default:
					break;
			}
		}
		
		public void startPositionDrive(Start start, boolean isForward) {

			String typeCross = isForward ? "-Cross" : "-Back";
			
			switch(start) {
				case POS_2:
					addSequential(new RunControlLoop("PositionTwo" + typeCross));
					break;
				case POS_2_CHEVAL_BACK:
					addSequential(new RunControlLoop("PositionTwo" + typeCross));
					break;
				case POS_5:
					addSequential(new RunControlLoop("PositionFive" + typeCross));
					break;
				default:
					break;
			}
		}
		
		public void startChevalDrive(Start start, boolean isLeft, boolean isForward) {

			String typeCross = isForward ? "-Cross" : "-Back";
			
			if(start == Start.POS_5) {
				if(isForward) {
					addSequential(new RunControlLoop("CHEVAL_5-4-Cross"));
				} else {
					addSequential(new RunControlLoop("CHEVAL_5-4-Back"));
				}
			} else if(start == Start.POS_4) {
				if(isForward) {
					if(isLeft) {
						addSequential(new RunControlLoop("CHEVAL_4-3-Cross"));
					} else {
						addSequential(new RunControlLoop("CHEVAL_4-5-Cross"));
					}
				} else {
					if(isLeft) {
						addSequential(new RunControlLoop("CHEVAL_4-5-Back"));
					} else {
						addSequential(new RunControlLoop("CHEVAL_4-3-Back"));
					}
				}
			} else if(start == Start.POS_3) {
				
			} else if(start == Start.POS_2) {
				
			} else {
				
			}
		}
		
		public void shoot() {
			addSequential(new SetCollectorPower(1, true));
			addSequential(new WaitForBeanBreak(false));
			addParallel(new SetCollectorPower(0, true));
			addSequential(new SetShooterPower());
		}
	}
}
