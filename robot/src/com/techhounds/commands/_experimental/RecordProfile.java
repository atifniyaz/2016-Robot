package com.techhounds.commands._experimental;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import com.techhounds.TrajectoryLoader;
import com.techhounds.lib.trajectory.Trajectory;
import com.techhounds.lib.trajectory.TrajectoryPair;
import com.techhounds.lib.util.HoundMath;
import com.techhounds.lib.util.PeriodicCommand;
import com.techhounds.subsystems.DriveSubsystem;

public class RecordProfile extends PeriodicCommand {
	
	private DriveSubsystem drive = DriveSubsystem.getInstance();
	
	private double lastT, lastLeftVelocity, lastRightVelocity;
	private double lastLeftAcc, lastRightAcc;
	private double currHeading;
	
	private double maxAcc;
	private double minAcc;
	
	public ArrayList<Trajectory.Segment> segmentsLeft = new ArrayList<>();
	public ArrayList<Trajectory.Segment> segmentsRight = new ArrayList<>();
	
	public RecordProfile() {
		super(100.0);
	}
	
	@Override
	protected void init() {
		lastT = System.currentTimeMillis();
		lastLeftVelocity = 0;
		lastRightVelocity = 0;
		maxAcc = 0;
		
		segmentsLeft = new ArrayList<>();
		segmentsRight = new ArrayList<>();
		
		drive.encodersReset();
		currHeading = drive.getRotationX();
	}

	@Override
	protected void doRun() {
		
		double dt = 0.01;
		
		double x = dt / 0.01;
		double y = dt / 0.01;
		
		double leftPosition = drive.countsToDist(drive.getLeftDistance());
		double rightPosition = drive.countsToDist(drive.getRightDistance());
		
		double leftVelocity = drive.getLeftFilteredSpeed();//drive.countsToDist(drive.getLeftSpeed());
		double rightVelocity = drive.getRightFilteredSpeed();//drive.countsToDist(drive.getRightSpeed());
		
		double currT = timeSinceInitialized();
		double measuredDt = (currT - lastT);
		lastT = currT;
		
		double leftAcceleration = (leftVelocity - lastLeftVelocity) / measuredDt;
		double rightAcceleration = (rightVelocity - lastRightVelocity) / measuredDt;
		
		if(leftAcceleration > maxAcc) {
			maxAcc = leftAcceleration;
		}
		
		if(leftAcceleration < minAcc) {
			minAcc = leftAcceleration;
		}
		
		double leftJerk = (leftAcceleration - lastLeftAcc) / measuredDt;
		double rightJerk = (rightAcceleration - lastRightAcc) / measuredDt;
		
		double heading = HoundMath.d2r(HoundMath.boundAngleNeg180to180Degrees(-drive.getRotationX() - currHeading));
		
		lastLeftVelocity = leftVelocity;
		lastRightVelocity = rightVelocity;
		
		lastLeftAcc = leftAcceleration;
		lastRightAcc = rightAcceleration;
		
		Trajectory.Segment segmentLeft = new Trajectory.Segment(dt, x, y, leftPosition, leftVelocity, leftAcceleration, leftJerk, heading);
		Trajectory.Segment segmentRight = new Trajectory.Segment(dt, x, y, rightPosition, rightVelocity, rightAcceleration, rightJerk, heading);
		
		segmentsLeft.add(segmentLeft);
		segmentsRight.add(segmentRight);
	}

	@Override
	protected boolean doFinish() {
		return false;
	}

	@Override
	protected void end() {
		Trajectory.Segment[] left = new Trajectory.Segment[segmentsLeft.size()];
		Trajectory.Segment[] right = new Trajectory.Segment[segmentsRight.size()];
		
		Thread thread = new Thread(new SaveProfile());
		thread.start();

		segmentsLeft.toArray(left);
		segmentsRight.toArray(right);

		TrajectoryLoader.recorded = new TrajectoryPair(new Trajectory(left), new Trajectory(right));
	}
	
	public class SaveProfile implements Runnable {

		@Override
		public void run() {

			Trajectory.Segment[] left = new Trajectory.Segment[segmentsLeft.size()];
			Trajectory.Segment[] right = new Trajectory.Segment[segmentsRight.size()];
			
			segmentsLeft.toArray(left);
			segmentsRight.toArray(right);

			Trajectory leftTrajectory = new Trajectory(left);
			Trajectory rightTrajectory = new Trajectory(right);
			
			Date date = Calendar.getInstance().getTime();
			String currTime = "RECORDED_" + date.getDate() + "-" + (1 + date.getMonth()) + "-" + (1900 + date.getYear()) + "-AT-" + date.getHours() + "-" + date.getMinutes() + "-" + date.getSeconds();
			
			String leftFile = "/trajectory/" + currTime + "/left.csv";
			String rightFile = "/trajectory/" + currTime + "/right.csv";

			
			File fileLeft = new File(leftFile);
			File fileRight = new File(rightFile);
			
			fileLeft.getParentFile().mkdirs();

			try {
				PrintWriter printLeft = new PrintWriter(fileLeft);
				PrintWriter printRight = new PrintWriter(fileRight);

				serializeTrajectory(printLeft, leftTrajectory);
				serializeTrajectory(printRight, rightTrajectory);
				
				printLeft.close();
				printRight.close();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
		}
	}
	
	 private void serializeTrajectory(PrintWriter print, Trajectory trajectory) {
		    String content = "";
		    for (int i = 0; i < trajectory.getLength(); ++i) {
		      Trajectory.Segment segment = trajectory.getSegment(i);
		      print.println(String.format(
		              "%.3f,%.3f,%.3f,%.3f,%.3f,%.3f,%.3f,%.3f",
		              segment.dt, segment.x, segment.y, 
		              segment.pos, segment.vel, segment.acc, segment.jerk,
		              segment.heading));
		    }
	  }
}
