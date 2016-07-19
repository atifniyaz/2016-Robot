package com.techhounds.commands;

import com.techhounds.OI;
import com.techhounds.lib.util.HoundSubsystem;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.command.Command;

public class UpdateSmartDashboard extends Command {

	private Timer timer;

	public UpdateSmartDashboard() {
		setRunWhenDisabled(true);
		timer = new Timer();
	}

	protected void initialize() {
		timer.reset();
		timer.start();
	}

	protected void execute() {

		if (timer.get() >= 1 / 200.0) {
			HoundSubsystem.updateSubsystemsPeriodic();
			OI.getInstance().updatePeriodic();
			timer.reset();
		}

	}

	protected boolean isFinished() {
		return false;
	}

	protected void end() {
	}

	protected void interrupted() {
	}
}
