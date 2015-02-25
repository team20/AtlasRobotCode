package org.usfirst.frc.team20.robot;

import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the IterativeRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the manifest file in the resource
 * directory.
 */
public class Robot extends IterativeRobot {

	public static double lastCycleEncoderPosition;

	public static final int FRONT_LEFT_PORT = 1, BACK_LEFT_PORT = 10,
			FRONT_RIGHT_PORT = 2, BACK_RIGHT_PORT = 9,
			ELEVATOR_MASTER_PORT = 7, TRAY_PORT = 6, FORKS_PORT = 5,
			LEFT_ROLLER_PORT = 2, RIGHT_ROLLER_PORT = 1,
			ELEVATOR_SLAVE_ONE = 4, ELEVATOR_SLAVE_TWO = 3,
			ELEVATOR_SLAVE_THREE = 8;

	Tray tray = new Tray(TRAY_PORT);
	
	Rollers rollers = new Rollers(LEFT_ROLLER_PORT, RIGHT_ROLLER_PORT);
	
	Forks forks = new Forks(FORKS_PORT);
	
	Elevator elevator = new Elevator(ELEVATOR_MASTER_PORT, ELEVATOR_SLAVE_ONE,
			ELEVATOR_SLAVE_TWO, ELEVATOR_SLAVE_THREE);

	/**
	 * This function is run when the robot is first started up and should be
	 * used for any initialization code.
	 */
	public void robotInit() {
		// Motors.forksMotor.setFeedbackDevice(FeedbackDevice.QuadEncoder);
		// Motors.forksMotor.changeControlMode(CANTalon.ControlMode.Position);
		// Motors.forksMotor.setPosition(0);
		// Motors.forksMotor.setPID(OperatorControls.p, OperatorControls.i,
		// OperatorControls.d);
		// Motors.forksMotor.setCloseLoopRampRate(OperatorControls.ramp);
		// Motors.forksMotor.enableControl();
	}

	/**
	 * This function is called periodically during autonomous
	 */
	public int counter = 0;

	public void autonomousPeriodic() {

	}

	@Override
	public void teleopInit() {

	}

	/**
	 * This function is called periodically during operator control
	 */
	public static boolean fieldCentric = true;

	public void teleopPeriodic() {

		elevator.checkElevator();
		forks.checkForks();
		tray.checkTray();

		SmartDashboard.putString("Elevator Master = ",
				" " + elevator.master.getOutputCurrent());
		SmartDashboard.putString("Elevator slave one =", ""
				+ elevator.slave1.getOutputCurrent());
		SmartDashboard.putString("Elevator slave two =", ""
				+ elevator.slave2.getOutputCurrent());
		SmartDashboard.putString("Elevator slave three =", ""
				+ elevator.slave3.getOutputCurrent());

		SmartDashboard.putString("Elevator Master ==", ""
				+ elevator.master.getOutputVoltage());
		SmartDashboard.putString("Elevator slave one ==", ""
				+ elevator.slave1.getOutputVoltage());
		SmartDashboard.putString("Elevator slave two ==", ""
				+ elevator.slave2.getOutputVoltage());
		SmartDashboard.putString("Elevator slave three ==", ""
				+ elevator.slave3.getOutputVoltage());

	}

	/**
	 * This function is called periodically during test mode
	 */
	public void testPeriodic() {

	}

}
