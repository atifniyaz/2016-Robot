package com.techhounds.commands;

import com.techhounds.commands.collector.SetCollectorPower;
import com.techhounds.commands.shooter.SetShooterPower;

import edu.wpi.first.wpilibj.command.CommandGroup;

public class EndEmergencyRelease extends CommandGroup {
    
    public  EndEmergencyRelease() {
    	addParallel(new SetShooterPower(0));
    	addSequential(new SetCollectorPower());
    }
}
