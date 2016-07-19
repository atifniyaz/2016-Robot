package com.techhounds.commands;

import com.techhounds.RobotMap.Angler;
import com.techhounds.commands.angler.SetAnglerPosition;
import com.techhounds.commands.servos.ReleaseClimber;
import com.techhounds.commands.servos.SetWinchEnable;
import com.techhounds.subsystems.AnglerSubsystem;

import edu.wpi.first.wpilibj.command.CommandGroup;

/**
 *
 */
public class SetEndGame extends CommandGroup {
    
	private boolean dontDoEndGame;
	
    public  SetEndGame(boolean endGame) {
    	if(endGame) {
    		addParallel(new ReleaseClimber(true));
    		addParallel(new SwitchControllerToEndGame(true));
    	} else {
    		addSequential(new SwitchControllerToEndGame(false));
    	}
    }
    
    @Override
    public void initialize() {
    	dontDoEndGame = false;
    	if(AnglerSubsystem.getInstance().getPosition() != Angler.COLLECTING) {
    		dontDoEndGame = true;
    		return;
    	}
    	
    	super.initialize();
    }
    
    @Override
    public boolean isFinished() {
    	if(dontDoEndGame) {
    		return true;
    	}
    	
    	return super.isFinished();
    }
}
