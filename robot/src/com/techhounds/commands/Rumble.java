package com.techhounds.commands;

import com.techhounds.OI;
import com.techhounds.lib.hid.ControllerMap;
import com.techhounds.subsystems.BeamBreakSubsystem;
import com.techhounds.subsystems.ShooterSubsystem;

import edu.wpi.first.wpilibj.command.Command;

public class Rumble extends Command {

	private ControllerMap driver, operator;
	private ShooterSubsystem shooter;
	private BeamBreakSubsystem beam;
	
	private boolean ballCurrentlyPresent;
	
	public Rumble() {
		driver = OI.getInstance().getDriver();
		operator = OI.getInstance().getOperator();
		
		shooter = ShooterSubsystem.getInstance();
		beam = BeamBreakSubsystem.getInstance();
	}
	
	@Override
	protected void initialize() {
		ballCurrentlyPresent = false;
	}

	@Override
	protected void execute() {
		if(shooter.getController().isEnabled() && shooter.onTarget()) {
			startRumble();
    	} else {
    			stopRumble();
    	}
	}

	@Override
	protected boolean isFinished() {
		return false;
	}

	@Override
	protected void end() {
	}

	@Override
	protected void interrupted() {
	}
	
	private void startRumble() {
		driver.startRumble();
		operator.startRumble();
	}
	
	private void stopRumble() {
		driver.stopRumble();
		operator.stopRumble();
	}

}
