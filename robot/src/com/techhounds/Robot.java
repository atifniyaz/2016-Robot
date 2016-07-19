package com.techhounds;

import com.techhounds.commands.MatchSetup;
import com.techhounds.commands.Rumble;
import com.techhounds.commands.SetFlashlight;
import com.techhounds.commands.UpdateSmartDashboard;
import com.techhounds.commands.auton.AutonChooser;
import com.techhounds.commands.auton.RetrieveAuton;
import com.techhounds.commands.drive_auton.MotionControlLoop;
import com.techhounds.commands.servos.SetWinchLock;
import com.techhounds.commands.vision.RotationLoop;
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

import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.command.Scheduler;
import edu.wpi.first.wpilibj.livewindow.LiveWindow;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class Robot extends IterativeRobot {
	
	private static final String GAME_STATE = "GameState";
	
	public static boolean oneControllerMode = false;
	public static boolean isDebugState = false;
	
    public void robotInit() {
    	
    	SmartDashboard.putString(GAME_STATE, "robotInit");
    	
    	initSubsystems();
    	
    	MotionControlLoop.getInstance();
    	RotationLoop.getInstance();
    	
    	TrajectoryLoader.getInstance();
    	OI.getInstance();
    	AutonChooser.getInstance();
    	
    	new UpdateSmartDashboard().start();
    	new MatchSetup().start();
    	new SetWinchLock(false).start();
    	
    	System.out.println("*** TECHHOUNDS IS READY TO ROBOT ***");
    }
    
    public void disabledInit(){
    	
       	SmartDashboard.putString(GAME_STATE, "disabled");
       	
    	MotionControlLoop.getInstance().stop();
    	RotationLoop.getInstance().stop();
    	
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
    	new Rumble().start();
    	
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
    
    public static double inchToMeter(double inches) {
    	return inches * 0.0254;
    }	
    
    public static double rangeCheck(double power){
		return rangeCheck(power, -1, 1);
	}
	
	public static double rangeCheck(double power, double min, double max) {
		return Math.max(Math.min(power, max), min);
	}
}
