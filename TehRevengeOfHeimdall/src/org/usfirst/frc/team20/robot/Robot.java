package org.usfirst.frc.team20.robot;

import edu.wpi.first.wpilibj.CANTalon.ControlMode;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.Timer;

/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the IterativeRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the manifest file in the resource
 * directory.
 */
public class Robot extends IterativeRobot {

	private AutoModes autoModes = new AutoModes();
	private int autoModeChooser = 0;
	private static long startTime;
	private T20Node autoTree;
	public static double lastautoTreeEncoderPosition;

	/**
	 * This function is run when the robot is first started up and should be
	 * used for any initialization code.
	 */
	public void robotInit() {

		startTime = System.currentTimeMillis();
		Motors.initi();
		lastautoTreeEncoderPosition = Motors.elevatorMaster.getPosition();

		Motors.trayMotor.changeControlMode(ControlMode.PercentVbus);

		// PID

		// Motors.forksMotor.setFeedbackDevice(FeedbackDevice.QuadEncoder);
		// Motors.forksMotor.changeControlMode(CANTalon.ControlMode.Position);
		// Motors.forksMotor.setPosition(0);
		// Motors.forksMotor.setPID(OperatorControls.p, OperatorControls.i,
		// OperatorControls.d);
		// Motors.forksMotor.setCloseLoopRampRate(OperatorControls.ramp);
		// Motors.forksMotor.enableControl();

		Sensors.elevatorShort.requestInterrupts();
		Sensors.elevatorShort.setUpSourceEdge(true, false);
	}

	/**
	 * This function is called periodically during autonomous
	 */
	public int counter = 0;

	/*
	 * @Override public void autonomousInit(){
	 * Motors.fLeft.changeControlMode(ControlMode.Position);
	 * 
	 * Motors.bLeft.changeControlMode(ControlMode.Position);
	 * Motors.fRight.changeControlMode(ControlMode.Position);
	 * Motors.bRight.changeControlMode(ControlMode.Position); }
	 * 
	 * @Override public void autonomousPeriodic() {
	 * if(!autoCommand.isFinished()) autoCommand.execute();
	 * SmartDashboard.putString("Back Left Encoder: ",""+
	 * (Motors.bLeft.getEncPosition()));
	 * SmartDashboard.putString("Back Right Encoder: ", "" +
	 * (Motors.bRight.getEncPosition()));
	 * SmartDashboard.putString("Front Left Encoder: ", "" +
	 * (Motors.fLeft.getEncPosition()));
	 * SmartDashboard.putString("Front Right Encoder: ", "" +
	 * (Motors.fRight.getEncPosition())); Timer.delay(0.001); }
	 */
	public static Timer hTime = new Timer();
	final double STOP_MOVE = 2.5;

	@Override
	public void autonomousInit() {
		hTime.start();
		autoTree = new T20SeriesNode();
		autoTree.addChild(new T20ElevatorToPosition(6.648));
		autoTree.addChild(new T20ForkToWidth(30.75));
		autoTree.addChild(new T20ElevatorToPosition(.25));

		// Node declarations
		T20Node vBusElevatorParallel = new T20ParallelNode();
		vBusElevatorParallel.addChild(new T20ElevatorToPosition(42));
		vBusElevatorParallel.addChild(new T20DriveStraightVBus(.05, 50));

		T20Node waitLowerElevatorSeries = new T20SeriesNode();
		waitLowerElevatorSeries.addChild(new T20Wait(1250));
		waitLowerElevatorSeries.addChild(new T20ElevatorToPosition(15));
		T20Node driveElevatorParallel = new T20ParallelNode();
		driveElevatorParallel
				.addChild(new T20DriveStraight(200, 80.8, 50, 100));
		driveElevatorParallel.addChild(waitLowerElevatorSeries);

		T20Node waitLowerElevatorSeries2 = new T20SeriesNode();
		waitLowerElevatorSeries2.addChild(new T20Wait(550));
		waitLowerElevatorSeries2.addChild(new T20ElevatorToPosition(.25));
		T20Node openForkParallel = new T20ParallelNode();
		openForkParallel.addChild(waitLowerElevatorSeries2);
		openForkParallel.addChild(new T20ForkToWidth(
				Motors.forksMotor.scaleXDZero - 4));

		T20Node openForkParallel2 = new T20ParallelNode();
		openForkParallel2.addChild(waitLowerElevatorSeries2.copy());
		openForkParallel2.addChild(new T20ForkToWidth(
				Motors.forksMotor.scaleXDZero - .5));

		// cycle 1
		autoTree.addChild(new T20ForkToWidth(25.375));
		autoTree.addChild(vBusElevatorParallel);
		autoTree.addChild(driveElevatorParallel);
		autoTree.addChild(new T20ElevatorToPosition(6.8));
		autoTree.addChild(openForkParallel);

		// cycle2
		autoTree.addChild(new T20ForkToWidth(25.375));
		autoTree.addChild(vBusElevatorParallel.copy());
		autoTree.addChild(driveElevatorParallel.copy());
		autoTree.addChild(new T20ElevatorToPosition(6.8));
		autoTree.addChild(openForkParallel2);

		autoTree.addChild(new T20ForkToWidth(24.875));
		autoTree.addChild(new T20ElevatorToPosition(4));
		autoTree.addChild(new T20DriveLateral(1, 3725));
		autoTree.addChild(new T20Wait(500));
		autoTree.addChild(new T20ForkToWidth(Motors.forksMotor.scaleXDZero));
		// T20Node lowerRodAndDrive = new T20ParallelNode();
		// lowerRodAndDrive.addChild(new T20DriveStraight(200, 24, 50, 100));
		// lowerRodAndDrive.addChild(new T20ExtendRod(1, 500));
		// autoTree.addChild(lowerRodAndDrive);
		// autoTree.addChild(new T20DriveStraight(1000, -24, -1000, -100));
	}

	@Override
	public void autonomousPeriodic() {
		if (autoModeChooser == 0) {
			
		} else if (autoModeChooser == 1) {
			autoModes.stepCanGrab();
		} else if (autoModeChooser == 2) {
			autoTree.execute();
		}

	}

	@Override
	public void disabledInit() {
		autoTree = new T20SeriesNode();
		if(Motors.driver.getRawButton(5)){
			autoModeChooser = 1;
		}else{
			autoModeChooser =0;
		}
		if(Motors.driver.getRawButton(2)){
			autoModeChooser = 2;
		}else{
			autoModeChooser =0;
		}
	}

	public static boolean fieldCentric = false;
	public static int test = 0;
	protected static double trayMotorFilteredCurrent = 0;

	@Override
	public void teleopInit() {
		Motors.fLeft.changeControlMode(ControlMode.PercentVbus);
		Motors.fRight.changeControlMode(ControlMode.PercentVbus);
		Motors.bLeft.changeControlMode(ControlMode.PercentVbus);
		Motors.bRight.changeControlMode(ControlMode.PercentVbus);
		if (Motors.elevatorMaster.homed) {
			Motors.elevatorMaster.disableControl();
			OperatorControls.elevatorPositionEU = Motors.elevatorMaster
					.getXEU();
			OperatorControls.forksSetpoint = Motors.forksMotor.getXEU();
			Motors.elevatorMaster.enableControl();
		}

	}

	private T20Stabilizer stable = new T20Stabilizer();

	@Override
	public void teleopPeriodic() {

		SmartDashboard.putString("ELEVATOR HEIGHT::",
				String.valueOf(Motors.elevatorMaster.getXEU()));
		SmartDashboard.putString("DRIVEBASE POSITION::",
				String.valueOf(Motors.fLeft.getXEU()));
		SmartDashboard.putString("FORK WIDTH::",
				String.valueOf(Motors.forksMotor.getXEU()));

		trayMotorFilteredCurrent = trayMotorFilteredCurrent * .9
				+ Motors.trayMotor.getOutputCurrent() * .1;

		if (Motors.driver.getRawButton(6)) {
			fieldCentric = !fieldCentric;
		}

		if (fieldCentric == true) {
			DriverControls.fieldDrive();
		} else {
			DriverControls.robotDrive();
		}

		if (Motors.driver.getRawButton(5)) {
			Motors.fLeft.setPosition(0);
			Motors.fRight.setPosition(0);
			Motors.bLeft.setPosition(0);
			Motors.bRight.setPosition(0);
		}

		OperatorControls.opControls();
		T20Stabilizer.bigBrotherStabilizer();
		Timer.delay(.001);
	}

	public void testPeriodic() {

	}

	public static long getTime() {
		return System.currentTimeMillis() - startTime;
	}
}