package com.techhounds.commands;

import com.techhounds.RobotMap;
import com.techhounds.commands.angler.SetAnglerPosition;
import com.techhounds.commands.collector.SetCollectorPower;
import com.techhounds.commands.shooter.SetShooterPower;

import edu.wpi.first.wpilibj.command.CommandGroup;

public class EmergencyRelease extends CommandGroup {
    
    public  EmergencyRelease() {
    	addSequential(new SetAnglerPosition(RobotMap.Angler.UP));
        addParallel(new SetShooterPower(-1));
        addSequential(new SetCollectorPower(-1, true));
        
    }
}
