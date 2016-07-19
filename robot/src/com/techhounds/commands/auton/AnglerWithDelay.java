package com.techhounds.commands.auton;

import com.techhounds.commands.angler.SetAnglerPosition;

import edu.wpi.first.wpilibj.command.CommandGroup;
import edu.wpi.first.wpilibj.command.WaitCommand;

public class AnglerWithDelay extends CommandGroup {

	public AnglerWithDelay(Double position, double time) {
		addSequential(new WaitCommand(time));
		addSequential(new SetAnglerPosition(position));
	}
}
