package com.techhounds.commands.gyro;

import com.techhounds.subsystems.DriveSubsystem;
import com.techhounds.subsystems.GyroSubsystem;

import edu.wpi.first.wpilibj.PIDController;
import edu.wpi.first.wpilibj.PIDOutput;
import edu.wpi.first.wpilibj.PIDSource;
import edu.wpi.first.wpilibj.PIDSourceType;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 *
 */
public class RotateUsingGyro extends Command implements PIDSource, PIDOutput {
	
	//private static double p = .1/*.05*/, i = 0, d = .12;//.08;
	private static double p = .05, i = 0, d = .08;
	private DriveSubsystem drive;
	protected GyroSubsystem gyro;
	private PIDController pid;
	protected double angle;
	private Double timeOut;
	public static final boolean DEBUG = true;
	//min turn power can be less here, as robot should already be moving
	private double MIN_TURN_POWER = .3;
	private double MIN_STRAIT_POWER = .16;
	private Boolean relative;
	/**
	 * Command to perform a relative rotation
	 * @param angle, in degrees, positive for clockwise, negative for counter-clockwise.
	 */
	
	
    public RotateUsingGyro(double angle) {
    	gyro = GyroSubsystem.getInstance();
    	drive = DriveSubsystem.getInstance();
    	requires(drive);
    	pid = new PIDController(p, i, d, this, this);
    	this.angle = angle;
    	if(DEBUG){
    		SmartDashboard.putData("Gyro PID", pid);
    	}
    	pid.setOutputRange(-.65, .65);
    	pid.setAbsoluteTolerance(1);
    	
        // Use requires() here to declare subsystem dependencies
        // eg. requires(chassis);
    }
    
    public RotateUsingGyro(double angle, boolean notRelative) {
    	this(angle);
    	relative = notRelative;
    }
    
    public RotateUsingGyro(double angle, double maxPower, boolean doesntMatter) {
    	this(angle);
    	pid.setOutputRange(-Math.abs(maxPower), Math.abs(maxPower));
    }
    
    public RotateUsingGyro(double angle, double timeOut, double doesntMatter) {
    	this(angle);
    	this.timeOut = timeOut;
    }
    
    /*this used to change the min power, if stuff breaks, change it back*/
    
    public RotateUsingGyro(double angle, double maxPower){
		this(angle);
		pid.setOutputRange(-maxPower, maxPower);
	}
    
    public RotateUsingGyro(double angle, double minTurnPower, double timeOut, double doesntMatter) {
    	this(angle);
    	MIN_TURN_POWER = minTurnPower;
    	this.timeOut = timeOut;
    }

    // Called just before this Command runs the first time
    protected void initialize() {
    	//angle = angle < 0 ? angle - .5 : angle + .5;
    	
    	if(relative != null)
    		pid.setSetpoint(angle);
    	else
    		pid.setSetpoint(angle + pidGet());
    	pid.enable();
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
    	if(DEBUG){
    		SmartDashboard.putNumber("GyroPID Error", pid.getError());
    	}
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
    	if(timeOut != null) {
    		if(timeSinceInitialized() > timeOut) {
    			return true;
    		}
    	}
    	
        return Math.abs(pid.getError()) < 1;
    }

    // Called once after isFinished returns true
    protected void end() {
    	pid.disable();
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
    	end();
    }

	@Override
	public void pidWrite(double output) {
		/*if(Math.abs(output) < MIN_TURN_POWER){
			if(pid.onTarget()){
				output = 0;
			}else if(output > 0){
				output = MIN_TURN_POWER;
			}else if(output < 0){
				output = -MIN_TURN_POWER;
			}
		}*/
		if(DEBUG){
			SmartDashboard.putNumber("GyroPID Output", output);
		}
		drive.rotateWithPower(output);
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setPIDSourceType(PIDSourceType pidSource) {
	}

	@Override
	public PIDSourceType getPIDSourceType() {
		// TODO Auto-generated method stub
		return PIDSourceType.kDisplacement;
	}

	@Override
	public double pidGet() {
		if(DEBUG){
			SmartDashboard.putNumber("Gyro Rotation", gyro.getRotation());
		}
		// TODO Auto-generated method stub
		return gyro.getRotation();
	}
}
