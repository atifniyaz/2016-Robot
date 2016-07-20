package com.techhounds.commands.drive_profile;

import com.techhounds.lib.trajectory.TrajectoryLoader;
import com.techhounds.lib.trajectory.TrajectoryPair;
import com.techhounds.subsystems.controllers.MotionControlLoop;

import edu.wpi.first.wpilibj.command.Command;

public class StartControlLoop extends Command {

	private TrajectoryPair trajectory;
	private String index;
	
	public StartControlLoop(TrajectoryPair trajectory) {
		this.trajectory = trajectory;
	}
	
	public StartControlLoop(String index) {
		this.index = index;
	}
	
	@Override
	protected void initialize() {
		
		if(trajectory == null) {
			MotionControlLoop.getInstance().useHeading(true);
			TrajectoryPair trajectory = TrajectoryLoader.getInstance().getTrajectory(index);
			MotionControlLoop.getInstance().setTrajectory(trajectory.getLeft(), trajectory.getRight());
		} else {
			MotionControlLoop.getInstance().useHeading(true);
			MotionControlLoop.getInstance().setTrajectory(trajectory.getLeft(), trajectory.getRight());
		}
		
		MotionControlLoop.getInstance().start();
	}

	@Override
	protected void execute() {
		// TODO Auto-generated method stub
		
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
