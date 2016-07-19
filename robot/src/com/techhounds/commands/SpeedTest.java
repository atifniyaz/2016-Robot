package com.techhounds.commands;

import com.techhounds.commands.shooter.SetShooterSpeed;

import edu.wpi.first.wpilibj.command.CommandGroup;
import edu.wpi.first.wpilibj.command.WaitCommand;

public class SpeedTest extends CommandGroup {

	public SpeedTest() {
		addSequential(new SetShooterSpeed(72.4, true));
		addSequential(new WaitCommand(6));
		addSequential(new SetShooterSpeed(0.0, true));
	}
}
