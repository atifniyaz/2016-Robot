package com.techhounds.commands.drive;

import java.io.File;
import java.io.PrintWriter;

import com.techhounds.OI;
import com.techhounds.lib.util.PeriodicCommand;
import com.techhounds.subsystems.DriveSubsystem;

import edu.wpi.first.wpilibj.Timer;

/**
 *
 */
public class DriveWithGamepad extends PeriodicCommand {
	
	DriveSubsystem drive;
	PrintWriter toFileLeft;
	double initTime;

    public DriveWithGamepad() {
    	drive = DriveSubsystem.getInstance();
    	requires(drive);
    }
    
    protected void end() {
    	toFileLeft.close();
    }

	@Override
	protected void init() {
		// TODO Auto-generated method stub
		String leftFile = "/home/lvuser/ACCELERATION_DATA.csv";
		File leftFile1 = new File(leftFile);
		
		try {
			leftFile1.getParentFile().mkdirs();
			toFileLeft = new PrintWriter(leftFile1);
		} catch(Exception e) {
			e.printStackTrace();
		}
		

    	initTime = Timer.getFPGATimestamp();
	}

	@Override
	protected void doRun() {

    	if(DriveSubsystem.isForward){
    		// If Driver Turn is less than .25, then we will do magic ;)
    		// Driver has override over Operator for Turning
    		// 65% power for turning
    		drive.setPower(.5*OI.getInstance().getRightBackward(), OI.getInstance().getLeftBackward());
    	}else{
    		drive.setPower(.5*OI.getInstance().getRightForward(), OI.getInstance().getLeftForward());	
    	}
		
    	toFileLeft.println((Timer.getFPGATimestamp() - initTime) + "," + drive.getLeftDistance() + "," + drive.getLeftSpeed() + "," + drive.getRightDistance() + "," + drive.getRightSpeed());
	}

	@Override
	protected boolean doFinish() {
		return false;
	}
}
