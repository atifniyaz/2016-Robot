package com.techhounds.commands;

import com.techhounds.subsystems.FlashlightSubsystem;

import edu.wpi.first.wpilibj.command.Command;

public class SetFlashlight extends Command {

	private Boolean on;
	private FlashlightSubsystem flashlight;
	
	public SetFlashlight() {
		setRunWhenDisabled(true);
		flashlight = FlashlightSubsystem.getInstance();
		this.on = null;
	}
	
	public SetFlashlight(boolean on) {
		setRunWhenDisabled(true);
		flashlight = FlashlightSubsystem.getInstance();
		this.on = on;
	}
	
	@Override
	protected void initialize() {
		if(on == null) {
			flashlight.toggle();
		} else {
			if(on) { 
				flashlight.on();
			} else {
				flashlight.off();
			}
		}	
	}

	@Override
	protected void execute() {
	}

	@Override
	protected boolean isFinished() {
		return true;
	}

	@Override
	protected void end() {
	}

	@Override
	protected void interrupted() {
	}

}
