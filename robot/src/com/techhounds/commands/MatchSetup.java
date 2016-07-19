package com.techhounds.commands;

import com.techhounds.RobotMap;
import com.techhounds.RobotMap.Angler;
import com.techhounds.commands.angler.SetAnglerPosition;
import com.techhounds.commands.climber.ReleaseClimber;
import com.techhounds.commands.climber.SetClimberPTO;
import com.techhounds.commands.collector.SetCollectorPower;
import com.techhounds.commands.shooter.SetShooterPower;

import edu.wpi.first.wpilibj.command.CommandGroup;

public class MatchSetup extends CommandGroup {
    
    public  MatchSetup() {
    	addParallel(new SetAnglerPosition(Angler.UP));
    	addParallel(new SetCollectorPower());
    	addParallel(new SetShooterPower());
    	addParallel(new ReleaseClimber(false));
    	addParallel(new SwitchControllerToNormal());
    	addParallel(new SetClimberPTO(RobotMap.Servo.WINCH_ENABLE_IS_UP_DEFAULT));
    }
}
