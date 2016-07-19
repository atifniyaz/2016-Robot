package com.techhounds.commands.auton;

import com.techhounds.commands.drive.DriveDistance;

import edu.wpi.first.wpilibj.command.CommandGroup;
import edu.wpi.first.wpilibj.networktables.NetworkTable;

/**
 *
 */
public class VisionDriveDistance extends CommandGroup {
    
	private double targetDistance = 0;
	private NetworkTable netTable;
	
    public  VisionDriveDistance() {
        
    	NetworkTable.setServerMode();
    	NetworkTable.initialize();
    	netTable = NetworkTable.getTable("SmartDashboard");
    	
    	if(netTable != null) {
    		targetDistance = netTable.getNumber("DistanceToTarget", 0);
    	}
    	
    	addSequential(new DriveDistance(targetDistance));
    }
}
