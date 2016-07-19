package com.techhounds.commands.auton;

import edu.wpi.first.wpilibj.command.Command;

public class RetrieveAuton extends Command {

	private Command autonCommand;
	
	@Override
	protected void initialize() {
		autonCommand = AutonChooser.getInstance().createAutonCommand();
		autonCommand.start();
	}

	@Override
	protected void execute() {
		// TODO Auto-generated method stub

	}

	@Override
	protected boolean isFinished() {
		return !autonCommand.isRunning();
	}

	@Override
	protected void end() {
		autonCommand.cancel();
	}

	@Override
	protected void interrupted() {
		end();
	}

}
