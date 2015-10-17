package org.usfirst.frc.team20.robot;

import edu.wpi.first.wpilibj.CANTalon;
import edu.wpi.first.wpilibj.CANTalon.FeedbackDevice;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Talon;

/**
 * 
 * @author robotics
 *
 */
public class Motors {
	// TODO Assign proper port numbers

	// Driver and Operator Controller PORTS
	private static final int DRIVER_JOYSTICK_SLOT = 0;
	private static final int OPERATOR_JOYSTICK_SLOT = 1;

	// Initialize PORTS
	public static final int FRONT_LEFT_PORT = 1, BACK_LEFT_PORT = 10,
			FRONT_RIGHT_PORT = 2, BACK_RIGHT_PORT = 9,
			ELEVATOR_MASTER_PORT = 7, TRAY_PORT = 6, CAN_GRABBER_PORT = 6,
			FORKS_PORT = 5, LEFT_ROLLER_PORT = 2, RIGHT_ROLLER_PORT = 1,
			STABILIZER_PORT = 11;

	// Elevator Slave PORTS
	public static final int ELEVATOR_SLAVE_ONE = 4, ELEVATOR_SLAVE_TWO = 3,
			ELEVATOR_SLAVE_THREE = 8;

	// CANTalons and Roller Talons PORTS
	public static T20CANServoEncWheel fLeft, bLeft, fRight, bRight;

	public static CANTalon trayMotor, stabilizerMotor, canGrabberMotor;

	public static T20CANServoEnc elevatorMaster;

	public static T20CANServoEncForks forksMotor;

	public static Talon rollersLeft, rollersRight;

	// Initialize Controllers
	public static Joystick driver = new Joystick(DRIVER_JOYSTICK_SLOT);
	public static Joystick operator = new Joystick(OPERATOR_JOYSTICK_SLOT);

	private static final double ELEVATOR_P = 1.3, ELEVATOR_I = 0.00001,
			ELEVATOR_D = 0; // d was .09 i was .0001 p was .7
	public static double FORKS_P = .5;
	public static double FORKS_I = .0001;
	public static double FORKS_D = .2;

	public static double DRIVE_P = 2.5;
	public static double DRIVE_I = .0002;
	public static double DRIVE_D = .2;

	// Rest of the INITIALIZATIONS
	public static void initi() {

		// Initialize Drive Train
		fLeft = new T20CANServoEncWheel(FRONT_LEFT_PORT, DRIVE_P, DRIVE_I,
				DRIVE_D, 0, 9000, 116);
		fLeft.setXDScale(0, -96, "inches");
		fLeft.setFeedbackDevice(FeedbackDevice.QuadEncoder);
		fLeft.setPosition(0);
		fLeft.reverseSensor(true);
		// fLeft.setVoltageRampRate(1000);

		fRight = new T20CANServoEncWheel(FRONT_RIGHT_PORT, DRIVE_P, DRIVE_I,
				DRIVE_D, 0, 9000, 116);
		fRight.setXDScale(0, 96, "inches");
		fRight.setFeedbackDevice(FeedbackDevice.QuadEncoder);
		fRight.setPosition(0);
		fRight.reverseSensor(true);
		// fRight.setVoltageRampRate(1000);

		bLeft = new T20CANServoEncWheel(BACK_LEFT_PORT, DRIVE_P, DRIVE_I,
				DRIVE_D, 0, 9090, 116);
		bLeft.setXDScale(0, -96, "inches");
		bLeft.setFeedbackDevice(FeedbackDevice.QuadEncoder);
		bLeft.setPosition(0);
		bLeft.reverseSensor(true);
		// bLeft.setVoltageRampRate(1000);

		bRight = new T20CANServoEncWheel(BACK_RIGHT_PORT, DRIVE_P, DRIVE_I,
				DRIVE_D, 0, 9090, 116);
		bRight.setXDScale(0, 96, "inches");
		bRight.setFeedbackDevice(FeedbackDevice.QuadEncoder);
		bRight.setPosition(0);
		bRight.reverseSensor(true);
		// bRight.setVoltageRampRate(1000);

		// Initialize Tray
		trayMotor = new CANTalon(TRAY_PORT);
		
		//Initialize CanGrabber
		canGrabberMotor = new CANTalon(CAN_GRABBER_PORT);

		// Initialize Stabilizer
		stabilizerMotor = new CANTalon(STABILIZER_PORT);

		// Initialize Elevator
		elevatorMaster = new T20CANServoEnc(ELEVATOR_MASTER_PORT, new int[] {
				ELEVATOR_SLAVE_ONE, ELEVATOR_SLAVE_TWO, ELEVATOR_SLAVE_THREE },
				ELEVATOR_P, ELEVATOR_I, ELEVATOR_D, 0, -10000, 0);
		elevatorMaster.setCloseLoopRampRate(4400);
		elevatorMaster.setXDScale(0, 60, "inches");
		elevatorMaster.reverseSensor(true);
		elevatorMaster.currentLimit = 100;

		// Initialize Carriage
		forksMotor = new T20CANServoEncForks(FORKS_PORT, new int[0], FORKS_P,
				FORKS_I, FORKS_D, 0, 144000, 1000);
		forksMotor.setCloseLoopRampRate(1500);

		forksMotor.setXDScale(36.75, 10, "inches");// 10, 36.75 ; 7.375, 34.375
		forksMotor.currentLimit = 30;

		rollersLeft = new Talon(LEFT_ROLLER_PORT);
		rollersRight = new Talon(RIGHT_ROLLER_PORT);
	}
}
