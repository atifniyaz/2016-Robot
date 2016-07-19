package com.techhounds.commands.angler;

import edu.wpi.first.wpilibj.command.CommandGroup;
import edu.wpi.first.wpilibj.command.WaitCommand;

public class AnglerWithDelay extends CommandGroup {

	public AnglerWithDelay(Double position, double time) {
		addSequential(new WaitCommand(time));
		addSequential(new SetAnglerPosition(position));
	}
}
