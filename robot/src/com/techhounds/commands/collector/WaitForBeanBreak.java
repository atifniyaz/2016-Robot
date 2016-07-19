package com.techhounds.commands.collector;

import com.techhounds.lib.util.PeriodicCommand;
import com.techhounds.subsystems.BeamBreakSubsystem;

public class WaitForBeanBreak extends PeriodicCommand {

	private BeamBreakSubsystem beanBreak;
	private boolean waitForIn;

	public WaitForBeanBreak(boolean waitForIn) {
		beanBreak = BeamBreakSubsystem.getInstance();
		this.waitForIn = waitForIn;
	}

	@Override
	protected void end() {
		
	}

	@Override
	protected void interrupted() {
		end();
	}

	@Override
	protected void init() {
	}

	@Override
	protected void doRun() {
	}

	@Override
	protected boolean doFinish() {
		return waitForIn == beanBreak.ballPresent();
	}

}
