package org.usfirst.frc.team20.robot;

import edu.wpi.first.wpilibj.CANTalon;
import edu.wpi.first.wpilibj.CANTalon.ControlMode;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Talon;

/**
 * 
 * @author Hiren 'BhavTsar' Bhavsar <bhavsar.hsb@gmail.com>
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
			ELEVATOR_MASTER_PORT = 7, TRAY_PORT = 6, FORKS_PORT = 5,
			LEFT_ROLLER_PORT = 2, RIGHT_ROLLER_PORT = 1;

	// Elevator Slave PORTS
	public static final int ELEVATOR_SLAVE_ONE = 4, ELEVATOR_SLAVE_TWO = 3,
			ELEVATOR_SLAVE_THREE = 8;

	// CANTalons and Roller Talons PORTS
	public static CANTalon fLeft, bLeft, fRight, bRight, 
			trayMotor, forksMotor;
	
	public static T20CANServoEnc elevatorMaster;

	public static Talon rollersLeft, rollersRight;

	// Initialize Controllers
	public static Joystick driver = new Joystick(DRIVER_JOYSTICK_SLOT);
	public static Joystick operator = new Joystick(OPERATOR_JOYSTICK_SLOT);

	private static final double ELEVATOR_P = 0.7, ELEVATOR_I = 0.0001, ELEVATOR_D = 0.09;
	
	// Rest of the INITIALIZATIONS
	public static void initi(){
		// Initialize Drive Train
		fLeft = new CANTalon(FRONT_LEFT_PORT);
		fRight = new CANTalon(FRONT_RIGHT_PORT);
		bLeft = new CANTalon(BACK_LEFT_PORT);
		bRight = new CANTalon(BACK_RIGHT_PORT);

		// Initialize Tray
		trayMotor = new CANTalon(TRAY_PORT);

		// Initialize Elevator
		elevatorMaster = new T20CANServoEnc(ELEVATOR_MASTER_PORT, 
				new int[]{ELEVATOR_SLAVE_ONE, ELEVATOR_SLAVE_TWO, ELEVATOR_SLAVE_THREE},
				ELEVATOR_P, ELEVATOR_I, ELEVATOR_D, 0, -9400, 0);
		elevatorMaster.setCloseLoopRampRate(4400);
		elevatorMaster.setXDScale(0, 60, "inches");
		elevatorMaster.reverseSensor(true);
		
		// Initialize Carriage
		forksMotor = new CANTalon(FORKS_PORT);
		rollersLeft = new Talon(LEFT_ROLLER_PORT);
		rollersRight = new Talon(RIGHT_ROLLER_PORT);
	}
}
