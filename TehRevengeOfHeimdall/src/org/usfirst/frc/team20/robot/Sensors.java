package org.usfirst.frc.team20.robot;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.Gyro;

public class Sensors {
	
	// Tray Extended and Tray Retracted Switches to determine location of the
	// Tray
	public static DigitalInput trayExtended = new DigitalInput(3);
	public static DigitalInput trayRetracted = new DigitalInput(2);

	// Elevator Short Switch is used to determine when the elevator is at ground
	// level and to reset zero on the Elevator Encoder
	public static DigitalInput elevatorShort = new DigitalInput(1);

	// Forks Opened Switch zeros the Fork Encoder and determines when the Fork
	// is fully opened
	public static DigitalInput forkOpenedFull = new DigitalInput(9);

	// Gyro used for Fieldcentric Driving and Auto
	public static Gyro gyro = new Gyro(0);
}
