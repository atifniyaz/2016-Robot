package com.techhounds.commands.drive_auton;

import java.util.Timer;
import java.util.TimerTask;

import com.techhounds.lib.trajectory.Trajectory;
import com.techhounds.lib.trajectory.TrajectoryFollower;
import com.techhounds.lib.util.HoundMath;
import com.techhounds.subsystems.DriveSubsystem;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class MotionControlLoop {

	private static MotionControlLoop instance;
	private DriveSubsystem drive = DriveSubsystem.getInstance();
	private Timer timer = null;
	private boolean heading = true;
	
	private TrajectoryFollower leftFollower, rightFollower;
	private boolean tuneTurn;

	public static MotionControlLoop getInstance() {
		return instance == null ? instance = new MotionControlLoop() : instance;
	}
	
	private MotionControlLoop() {
		SmartDashboard.putBoolean("Tune Turn?", false);
		SmartDashboard.putNumber("Kturn_Motion", Kturn);
	}
	
	private double Kp = 0.0, Ki = 0.0, Kd = 0.0, Kv = 1 / 105.0, Ka = 0.00007, Kturn = -0.006;
	private double currHeading;
	
	private int currSeg;
	private double lastError;
	
	private Trajectory left, right;
	
	public void useHeading(boolean heading) {
		this.heading = heading;
	}
	
	public void configurePIDVA(double Kp, double Ki, double Kd, double Kv, double Ka, double Kturn) {
		this.Kp = Kp;
		this.Ki = Ki;
		this.Kd = Kd;
		this.Kv = Kv;
		this.Ka = Ka;
		this.Kturn = Kturn;
	}
	
	public void setTrajectory(Trajectory left, Trajectory right) {
		this.left = left; this.right = right;
	}
	
	public void start() {
		if(timer == null) {
			timer = new Timer();
			
			//currHeading = -drive.getRotationX();
			currSeg = 0;
			lastError = 0;
			
			drive.encodersReset();
			
			leftFollower = new TrajectoryFollower(left);
			rightFollower = new TrajectoryFollower(right);
			
			tuneTurn = SmartDashboard.getBoolean("Tune Turn?", false);
			Kturn = SmartDashboard.getNumber("Kturn_Motion", Kturn);

			leftFollower.setGains(Kp, Ki, Kd, Kv, Ka);
			rightFollower.setGains(Kp, Ki, Kd, Kv, Ka);
			
			timer.scheduleAtFixedRate(new DriveTask(), 0, (long) ((1 / 100.0) * 1000));
		}
	}
	
	public void stop() {
		if(timer != null) {
			timer.cancel();
			timer = null;
		}
	}
	
	public int currentSegment() {
		return currSeg;
	}
	
	public boolean finished() {
		return leftFollower.isFinished();
	}
	
	public class DriveTask extends TimerTask {

		@Override
		public void run() {
			
			double leftSpeed = leftFollower.calculateOutput(drive.getLeftDistance());
			double rightSpeed = rightFollower.calculateOutput(drive.getRightDistance());

			if(tuneTurn) {
				leftSpeed = 0; rightSpeed = 0;
			}
			
			double turn = 0;//leftFollower.calculateHeading(drive.getRotationX() - ProfileDrive.this.currHeading);
			
			double goalHeading = HoundMath.boundAngleNeg180to180Degrees(HoundMath.r2d(leftFollower.getHeading()));
			currSeg++;

			double currHeading = HoundMath.boundAngleNeg180to180Degrees(-drive.getRotationX() - MotionControlLoop.this.currHeading);
			double angleDiff = goalHeading - currHeading;

			turn = heading ? Kturn * angleDiff : 0;// calculateTurn(angleDiff);
			
			drive.setPower(rightSpeed - turn, leftSpeed + turn);
		}
		
		public double calculateTurn(double currAngle) {
			double MAX_OFFSET = .25;
			double offset;
			
			if(currAngle > 9.11346) {
				offset = MAX_OFFSET;
			} else if(currAngle < -9.01606) {
				offset = -MAX_OFFSET;
			} else {
				// A power between -.21 and .21
				offset = MAX_OFFSET * ((-0.000136434109 * Math.pow(currAngle, 3) + 0.0000199335555 * Math.pow(currAngle, 2)
					+ 0.03363132991 * (currAngle)) / .201608);
			}
			
			return offset;
		}
	}
	
	public void setCurrHeading(double heading) {
		this.currHeading = heading;
	}
}
