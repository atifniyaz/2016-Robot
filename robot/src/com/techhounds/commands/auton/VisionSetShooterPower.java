package com.techhounds.commands.auton;

import com.techhounds.commands.shooter.SetShooterPower;

import edu.wpi.first.wpilibj.command.CommandGroup;
import edu.wpi.first.wpilibj.networktables.NetworkTable;

public class VisionSetShooterPower extends CommandGroup {
    
	private double targetDistance = 0;
	private double defaultShootPower = 69;
	private NetworkTable netTable;
	
    public VisionSetShooterPower() {
        
    	NetworkTable.setServerMode();
    	NetworkTable.initialize();
    	netTable = NetworkTable.getTable("SmartDashboard");
    	
    	if(netTable != null) {
    		targetDistance = netTable.getNumber("DistanceToTarget", 0);
    	} 
    	
    	//TODO convert from distance to power
    	
    	addSequential(new SetShooterPower(convertDistanceToPower(targetDistance)));
    }
    
    public double convertDistanceToPower(double distance){
    	if(distance == 0){
    		return defaultShootPower;
    	}else{
    		return targetDistance();
    	}
    }
    
    public double targetDistance() {
    	double x = targetDistance;
    	return ((.002462672556 * x * x) - (.505949214913 * x) + 93.1903623953);
    }
}
