package com.techhounds;

import java.io.File;
import java.util.HashMap;

import com.techhounds.lib.trajectory.Trajectory;
import com.techhounds.lib.trajectory.TrajectoryPair;
import com.techhounds.lib.trajectory.TrajectorySerializer;

public class TrajectoryLoader {
	
	private static TrajectoryLoader instance;
	public static TrajectoryPair recorded;
	
	public static final String [] FILES_TO_LOAD = {
			"LowBarOneBall",				// 2 Ball - First Low Bar Pass
			"LowBarTwoBall-Back",			// 2 Ball - Second Low Bar Pass Backwards
			"LowBarTwoBall-Cross",			// 2 Ball - Second Low Bar Pass
			"LowBarAutoBall-Cross",			// 1 Ball - Low Bar Pass Forward
			"LowBarAutoBall-Back",			// 1 Ball - Low Bar Pass Backward
			"RockWall-Cross",				// Rock Wall Cross
			"RockWall-Back",				// Rock Wall Back
			"PositionFive-Back",
			"PositionFive-Forward"
	};
	
	public static HashMap<String, TrajectoryPair> loadedTrajectories;
	
	private TrajectoryLoader() {
		loadTrajectories();
	}
	
	public void loadTrajectories() {
		(new LoadFiles()).run();
	}

	public class LoadFiles implements Runnable {
		
		public void run() {
			
			loadedTrajectories = new HashMap<>();
			
			for(int i = 0; i < FILES_TO_LOAD.length; i++) {
				
				Trajectory left2 = TrajectorySerializer.readFromCSV(new File("/trajectory/" + FILES_TO_LOAD[i] + "/left.csv"));
				Trajectory right2 = TrajectorySerializer.readFromCSV(new File("/trajectory/" + FILES_TO_LOAD[i] + "/right.csv"));
				
				loadedTrajectories.put(FILES_TO_LOAD[i], new TrajectoryPair(left2, right2));
			}
		}
	}
	
	public TrajectoryPair getTrajectory(String name) {
		return loadedTrajectories.get(name);
	}
	
	public static TrajectoryLoader getInstance() {
		return instance == null ? instance = new TrajectoryLoader() : instance;
	}
}
