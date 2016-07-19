package com.techhounds.commands.shooter;

import com.techhounds.commands.collector.SetCollectorPower;

import edu.wpi.first.wpilibj.command.CommandGroup;
import edu.wpi.first.wpilibj.command.WaitCommand;

public class Fire extends CommandGroup {

	private static Fire instance;
	
	private Fire() {
		//addSequential(new WaitForShooterReady());
		addSequential(new SetCollectorPower(1, true));
		addSequential(new WaitCommand(1));
		addSequential(new SetCollectorPower(0, true));
		addSequential(new SetShooterPower());
	}
	
	public static Fire getInstance() {
		return instance == null ? instance = new Fire() : instance;
	}
	
	@Override
	public void cancel() {
		super.cancel();
		(new SetShooterPower()).start();
		(new SetCollectorPower(0, true)).start();
	}
}
