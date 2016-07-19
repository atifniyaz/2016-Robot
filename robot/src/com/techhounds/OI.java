package com.techhounds;

import com.techhounds.commands.EmergencyRelease;
import com.techhounds.commands.EndEmergencyRelease;
import com.techhounds.commands.LoadTrajectories;
import com.techhounds.commands.MatchSetup;
import com.techhounds.commands.SetEndGame;
import com.techhounds.commands.SetFlashlight;
import com.techhounds.commands.SpeedTest;
import com.techhounds.commands.UpdateController;
import com.techhounds.commands._experimental.RecordProfile;
import com.techhounds.commands.angler.SetStateDown;
import com.techhounds.commands.angler.SetStateUp;
import com.techhounds.commands.auton.RetrieveAuton;
import com.techhounds.commands.auton.RotateUsingVision;
import com.techhounds.commands.collector.SetCollectorPower;
import com.techhounds.commands.drive.DriveDistanceStraight;
import com.techhounds.commands.drive.LockWinch;
import com.techhounds.commands.drive.RunWinch;
import com.techhounds.commands.drive.ToggleDriveDirection;
import com.techhounds.commands.drive_auton.RunControlLoop;
import com.techhounds.commands.drive_auton.WriteControlLoopHeading;
import com.techhounds.commands.gyro.RotateUsingGyro;
import com.techhounds.commands.gyro.SaveCurrentAngle;
import com.techhounds.commands.servos.SetWinchEnable;
import com.techhounds.commands.shooter.Fire;
import com.techhounds.commands.shooter.PreFire;
import com.techhounds.commands.shooter.SetShooterPower;
import com.techhounds.commands.shooter.StopFire;
import com.techhounds.commands.shooter.ToggleAlignVision;
import com.techhounds.lib.hid.ControllerMap;
import com.techhounds.lib.hid.ControllerMap.Direction;
import com.techhounds.lib.hid.DPadButton;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class OI {

	private static OI instance;

	private ControllerMap driver, operator, currentDriver;
	private SendableChooser driverChooser, operatorChooser;

	// DRIVER AND OPERATOR CONTROLS
	final int collector_IN = 				ControllerMap.Key.RT;
	final int collector_OUT = 				ControllerMap.Key.RB;
	final int collectorAngler_UP = 			ControllerMap.Key.Y;
	final int collectorAngler_DOWN = 		ControllerMap.Key.A;
	
	final int shooter_START = 				ControllerMap.Key.X;
	final int shooter_STOP = 				DPadButton.Direction.LEFT;
	final int shooter_START_ALT = 			DPadButton.Direction.UP;
	final int shooter_FIRE = 				ControllerMap.Key.B;
	final int shooter_EMERGENCY_RELEASE = 	DPadButton.Direction.RIGHT;
	
	final int flashlight_TOGGLE = 			DPadButton.Direction.DOWN;
	final int drive_DIRECTION_TOGGLE = 		ControllerMap.Key.START;
	
	final int vision_SINGLE_ALIGN = 		ControllerMap.Key.LT;
	final int vision_CONT_ALIGN = 			ControllerMap.Key.LB;
	
	final int toggleGameMode = ControllerMap.Key.BACK;
	final int normalGameMode = DPadButton.Direction.RIGHT;

	final int winch_RUN_UP = 				ControllerMap.Key.Y;
	final int winch_RUN_DOWN = 				ControllerMap.Key.A;
	final int winch_LOCK = 					ControllerMap.Key.LT;
	final int winch_ENABLE = 				ControllerMap.Key.X;
	final int winch_DISABLE =				ControllerMap.Key.RB;
	final int winch_UNLOCK = 				ControllerMap.Key.LB;

	private OI() {

		driverChooser = createControllerChooser(true);
		operatorChooser = createControllerChooser(false);

		SmartDashboard.putData("Driver Controller Chooser", driverChooser);
		SmartDashboard.putData("Operator Controller Chooser", operatorChooser);

		driver = new ControllerMap(new Joystick(RobotMap.OI_Constants.DRIVER_PORT),
				(ControllerMap.Type) driverChooser.getSelected());
		operator = new ControllerMap(new Joystick(RobotMap.OI_Constants.OPERATOR_PORT),
				(ControllerMap.Type) operatorChooser.getSelected());

		setup();
	}

	private void setup() {
		if (Robot.oneControllerMode) {
			setUpController(driver);
		} else {
			setupDriver();
			setupOperator();
		}
		currentDriver = driver;
		setupSmartDashboard();
	}

	/**
	 * @return Gets the only instance of the OI
	 */
	public static OI getInstance() {
		return instance == null ? instance = new OI() : instance;
	}

	/**
	 * Gets the Driver Controller Ready with its Buttons
	 */
	public void setupDriver() {
		setUpController(driver);
	}

	/**
	 * Gets the Operator Controller Ready with its Buttons
	 */
	public void setupOperator() {
		setUpController(operator);
	}

	/**
	 * Setup Single Controller Control
	 */
	public void setUpController(ControllerMap controller) {

		controller.clearButtons();
		
		controller.getButton(collector_IN)
			.whenPressed(new SetCollectorPower(RobotMap.Collector.inPower))
			.whenReleased(new SetCollectorPower());

		controller.getButton(collector_OUT)
			.whenPressed(new SetCollectorPower(RobotMap.Collector.outPower))
			.whenReleased(new SetCollectorPower());

		controller.getButton(collectorAngler_UP)
			.whenPressed(new SetStateUp());

		controller.getButton(collectorAngler_DOWN)
			.whenPressed(new SetStateDown());
		
		controller.getButton(shooter_START)
			.whenPressed(new PreFire(72.7));

		controller.getButton(shooter_START_ALT)
				.whenPressed(new PreFire(69));

		controller.getButton(shooter_STOP)
				.whenPressed(new SetShooterPower());
		
		controller.getButton(shooter_FIRE)
				.whenPressed(Fire.getInstance())
				.whenReleased(new StopFire());
		
		controller.getButton(shooter_EMERGENCY_RELEASE)
			.whenPressed(new EmergencyRelease())
			.whenReleased(new EndEmergencyRelease());


		if (controller == driver) {
			controller.getButton(drive_DIRECTION_TOGGLE)
				.whenPressed(new ToggleDriveDirection());
		}

		controller.getButton(vision_SINGLE_ALIGN)
				.whenPressed(new RotateUsingVision(4));

		 controller.getButton(vision_CONT_ALIGN)
		 	.whenPressed(new ToggleAlignVision(true))
				.whenReleased(new ToggleAlignVision(false));
	
		controller.getButton(flashlight_TOGGLE)
				.whenPressed(new SetFlashlight());
		
		if (controller == operator) {
			controller.getMultiButton(ControllerMap.Key.BACK, ControllerMap.Key.START)
				.whenPressed(new SetEndGame(true));
		}
	}

	public void initializeEndGame(ControllerMap controller) {
		
		controller.clearButtons();
		
		controller.getButton(winch_RUN_UP)
				.whileHeld(new RunWinch("Winch Up Power", .5));
		
		controller.getButton(winch_ENABLE)
				.whenPressed(new SetWinchEnable(!RobotMap.Servo.WINCH_ENABLE_IS_UP_DEFAULT));
		
		controller.getButton(winch_DISABLE)
				.whenPressed(new SetWinchEnable(RobotMap.Servo.WINCH_ENABLE_IS_UP_DEFAULT));
		
		controller.getButton(winch_RUN_DOWN)
				.whileHeld(new RunWinch("Winch Down Power", -.5));
		
		controller.getButton(winch_LOCK)
				.whenPressed(new LockWinch(true));
		
		controller.getButton(winch_UNLOCK)
				.whenPressed(new LockWinch(false));
		
		controller.getButton(normalGameMode)
				.whenPressed(new SetEndGame(false));
	}

	/**
	 * Gets the Smart Dashboard Ready with Commands (Act as Buttons)
	 */
	public void setupSmartDashboard() {
		
		if(Robot.isDebugState) {
			SmartDashboard.putData("Run Selected Auton", new RetrieveAuton());
			SmartDashboard.putData("Match Set Up", new MatchSetup());
			
			SmartDashboard.putData("Rotate 60 Degrees", new RotateUsingGyro(60));
	
			SmartDashboard.putData("Save Angle", new SaveCurrentAngle());
			SmartDashboard.putData("Drive Distance Straight", new DriveDistanceStraight(1000, 0.5, 0.2, null, true));
	
			SmartDashboard.putData("Update The Controllers", new UpdateController());
			
		}
		
		SmartDashboard.putData("Record Profile", new RecordProfile());
		
		SmartDashboard.putData("Reload Trajectories", new LoadTrajectories());
		SmartDashboard.putData("Speed Test", new SpeedTest());
		
		SmartDashboard.putData("LowBar Two Ball Back", new RunControlLoop("LowBarTwoBall-Back"));
		SmartDashboard.putData("LowBar TWo Ball Cross", new RunControlLoop("LowBarTwoBall-Cross"));
		SmartDashboard.putData("LowBar One Ball Cross", new RunControlLoop("LowBarOneBall"));
		SmartDashboard.putData("Write Heading", new WriteControlLoopHeading());
	}

	/**
	 * Gets the Driver's Gamepad
	 */
	public ControllerMap getDriver() {
		return driver;
	}

	/**
	 * Gets the Operator's Gamepad
	 */
	public ControllerMap getOperator() {
		return operator;
	}

	/**
	 * Create Chooser for Controllers
	 */
	private SendableChooser createControllerChooser(boolean driver) {
		
		SendableChooser chooser = new SendableChooser();
		chooser.addObject("Logitech", ControllerMap.Type.LOGITECH);
		chooser.addDefault("XBOX ONE", ControllerMap.Type.XBOX_ONE);
		chooser.addObject("XBOX 360", ControllerMap.Type.XBOX_360);
		chooser.addObject("Playstation 4", ControllerMap.Type.PS4);
		return chooser;
	}

	/**
	 * Update Controllers
	 */
	public void updateControllers() {
		driver.setControllerType((ControllerMap.Type) driverChooser.getSelected());
		operator.setControllerType((ControllerMap.Type) operatorChooser.getSelected());

		setup();
	}

	/**
	 * Update dashboard when Called
	 */
	public void updatePeriodic() {
		SmartDashboard.putString("Driver Type", driver.getType().toString());
		SmartDashboard.putString("Operator Type", operator.getType().toString());
	}

	public void switchDriver() {
		if (currentDriver.equals(driver)) {
			currentDriver = operator;
		} else {
			currentDriver = driver;
		}
	}

	public double getSteer() {
		if (Math.abs(currentDriver.getAxis(ControllerMap.Direction.RIGHT_HORIZONTAL)) < .25
				&& currentDriver != operator)
			return operator.getAxis(ControllerMap.Direction.RIGHT_HORIZONTAL) * .4;
		else
			return currentDriver.getAxis(ControllerMap.Direction.RIGHT_HORIZONTAL);
	}

	public double getRightForward() {
		return Robot.rangeCheck(currentDriver.getAxis(Direction.LEFT_VERTICAL) - getSteer());
	}

	public double getLeftForward() {
		return Robot.rangeCheck(currentDriver.getAxis(Direction.LEFT_VERTICAL) + getSteer());
	}

	public double getRightBackward() {
		return Robot.rangeCheck(-currentDriver.getAxis(Direction.LEFT_VERTICAL) - getSteer());
	}

	public double getLeftBackward() {
		return Robot.rangeCheck(-currentDriver.getAxis(Direction.LEFT_VERTICAL) + getSteer());
	}
}
