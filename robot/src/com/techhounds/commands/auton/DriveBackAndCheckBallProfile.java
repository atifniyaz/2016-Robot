package com.techhounds.commands.auton;

import com.techhounds.RobotMap;
import com.techhounds.commands.collector.SetCollector;
import com.techhounds.commands.collector.SetCollectorPower;
import com.techhounds.commands.collector.WaitForBeanBreak;
import com.techhounds.commands.drive.DriveDistance;
import com.techhounds.commands.drive.DriveDistanceStraight;
import com.techhounds.commands.drive_auton.RunControlLoop;

import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.CommandGroup;

public class DriveBackAndCheckBallProfile extends CommandGroup {
	
	private Command drive, ballCheck;
	
	public DriveBackAndCheckBallProfile(String profile){
		addParallel(drive = new RunControlLoop(profile));
		addSequential(ballCheck = new WaitForBeanBreak(true));
		addSequential(new SetCollectorPower(0.0, true));
	}

	@Override
	protected boolean isFinished() {
		if(ballCheck != null && drive != null) {
			if(!ballCheck.isRunning()) {
				return false;
			} else if(!drive.isRunning()){
				return true;
			} else {
				return false;
			}
		} else {
			return false;
		}
	}
	
	@Override
	public void end() {
	}
}
