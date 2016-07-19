package com.techhounds.commands;

import com.techhounds.RobotMap;
import com.techhounds.RobotMap.Angler;
import com.techhounds.commands.angler.SetAnglerPosition;
import com.techhounds.commands.collector.SetCollectorPower;
import com.techhounds.commands.servos.ReleaseClimber;
import com.techhounds.commands.servos.SetWinchEnable;
import com.techhounds.commands.shooter.SetShooterPower;

import edu.wpi.first.wpilibj.command.CommandGroup;

public class MatchSetup extends CommandGroup {
    
    public  MatchSetup() {
    	addParallel(new SetAnglerPosition(Angler.UP));
    	addParallel(new SetCollectorPower());
    	addParallel(new SetShooterPower());
    	addParallel(new ReleaseClimber(false));
    	//addParallel(new LockWinch(false));
    	addParallel(new SwitchControllerToNormal());
    	//addParallel(new UpdateController());
    	//addParallel(new SetScissorsOne(RobotMap.Servo.SCISSOR_ONE_IS_UP_DEFAULT));
    	//addParallel(new SetScissorsTwo(RobotMap.Servo.SCISSOR_TWO_IS_UP_DEFAULT));
    	addParallel(new SetWinchEnable(RobotMap.Servo.WINCH_ENABLE_IS_UP_DEFAULT));
    	//addParallel(new SetWinchLock(RobotMap.Servo.WINCH_LOCK_IS_UP_DEFAULT));
    }
}
