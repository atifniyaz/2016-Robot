package com.techhounds.commands;

import com.techhounds.commands.angler.SetStateUp;
import com.techhounds.commands.collector.SetCollectorPower;
import com.techhounds.commands.shooter.SetShooterPower;

import edu.wpi.first.wpilibj.command.CommandGroup;

public class EmergencyRelease extends CommandGroup {
    
    public  EmergencyRelease() {
    	addSequential(new SetStateUp());
        addParallel(new SetShooterPower(-1));
        addSequential(new SetCollectorPower(-1, true));
        
    }
}
