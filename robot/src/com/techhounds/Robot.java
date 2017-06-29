package com.techhounds;

import com.techhounds.commands.MatchSetup;
import com.techhounds.commands.SetFlashlight;
import com.techhounds.commands.UpdateSmartDashboard;
import com.techhounds.commands.auton.AutonChooser;
import com.techhounds.commands.auton.RetrieveAuton;
import com.techhounds.commands.climber.SetClimberLock;
import com.techhounds.lib.trajectory.TrajectoryLoader;
import com.techhounds.subsystems.AnglerSubsystem;
import com.techhounds.subsystems.BeamBreakSubsystem;
import com.techhounds.subsystems.CollectorSubsystem;
import com.techhounds.subsystems.DriveSubsystem;
import com.techhounds.subsystems.FlashlightSubsystem;
import com.techhounds.subsystems.GyroSubsystem;
import com.techhounds.subsystems.LEDSubsystem;
import com.techhounds.subsystems.ServoSubsystem;
import com.techhounds.subsystems.ShooterSubsystem;
import com.techhounds.subsystems.VisionSubsystem;
import com.techhounds.subsystems.controllers.MotionControlLoop;

import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.command.Scheduler;
import edu.wpi.first.wpilibj.livewindow.LiveWindow;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class Robot extends IterativeRobot {
	
	private static final String GAME_STATE = "GameState";
	
	public static boolean oneControllerMode = false;
	public static boolean isDebugState = false;
	public static boolean isManuallyDisabled = false;
	
	public boolean isDisabled(){
		return super.isDisabled() || isManuallyDisabled;
	}
	
    public void robotInit() {
    	
    	SmartDashboard.putString(GAME_STATE, "robotInit");
    	
    	initSubsystems();
    	
    	MotionControlLoop.getInstance();
    	
    	TrajectoryLoader.getInstance();
    	OI.getInstance();
    	AutonChooser.getInstance();
    	
    	new UpdateSmartDashboard().start();
    	new MatchSetup().start();
    	new SetClimberLock(false).start();
    	
    	System.out.println("*** TECHHOUNDS IS READY TO ROBOT ***");
    }
    
    public void disabledInit(){
    	
       	SmartDashboard.putString(GAME_STATE, "disabled");
       	
    	MotionControlLoop.getInstance().stop();
    	
    	new SetFlashlight(false).start();
    }
    
	public void disabledPeriodic() {
		Scheduler.getInstance().run();
	}

	public void autonomousInit() {
		
    	SmartDashboard.putString(GAME_STATE, "auton");
    	
		new RetrieveAuton().start();

    	System.out.println("*** TECHHOUNDS IS READY TO AUTON ***");
    }

    public void autonomousPeriodic() {
        Scheduler.getInstance().run();
    }

    public void teleopInit() {
    	SmartDashboard.putString(GAME_STATE, "teleop");
    	
    	new MatchSetup().start();
    	
    	System.out.println("*** TECHHOUNDS IS READY TO TELEOP ***");
    }

    public void teleopPeriodic() {
        Scheduler.getInstance().run();
    }
    
    public void testPeriodic() {
        LiveWindow.run();
    }
    
    private void initSubsystems() {
    	AnglerSubsystem.getInstance();
    	BeamBreakSubsystem.getInstance();
    	CollectorSubsystem.getInstance();
    	DriveSubsystem.getInstance();
    	GyroSubsystem.getInstance();
    	ServoSubsystem.getWinchEnable();
    	ServoSubsystem.getScissorOne();
    	ServoSubsystem.getWinchLock();
    	ShooterSubsystem.getInstance();
    	VisionSubsystem.getInstance();
    	LEDSubsystem.getInstance();
    	FlashlightSubsystem.getInstance();
    }
}
