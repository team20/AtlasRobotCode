package org.usfirst.frc.team20.robot;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class OperatorControls {

	// For Claw Positions
	public static double p = .5;
	public static double i = .0001;
	public static double d = .5;
	public static double ramp = 1500;
	public static double talFil = 0;

	private static Timer elevatorCooldown = new Timer();
	private static final double ELEVATOR_COOLDOWN = .7;
	
	private static double elevatorPositionEU = 0;
	private static boolean trayBool = false;
	private static double elevatorPos[] = { .5, 12.6, 24.7, 36.8, 48.9, 60 };
	public static int level = 0;

	// TODO Update Axis Values!
	public static void opControls() {
		double elevatorActual = 0;
		double analogElevator = Motors.operator.getRawAxis(3);
		double analogFork = -Motors.operator.getRawAxis(0);

		// Claw Code
		// KnoxKode for PID Forks
		if (Motors.operator.getPOV() == 270) {
			Motors.forksMotor.set(25000);
		}
		if (Motors.operator.getPOV() == 180) {
			Motors.forksMotor.set(59000);
		}
		if (Motors.operator.getPOV() == 0) {
			Motors.forksMotor.set(16000);
		}
		if (Motors.operator.getPOV() == 90) {
			Motors.forksMotor.set(75000);
		}
		if (Motors.operator.getRawButton(9)) {
			Motors.forksMotor.set(200);
		}
		double talCur = Motors.forksMotor.getOutputCurrent();
		talFil = talFil * .9 + talCur * .1;
		if (talFil > 15) {
			Motors.forksMotor.set(Motors.forksMotor.getPosition());
		}

		if (talFil > 15) {
			Motors.forksMotor.set(Motors.forksMotor.getPosition());
		}
		// End KnoxKode
		// Fork Code End

		// Tray Code
		if (Motors.operator.getRawButton(6)) {
			Motors.trayMotor.set(1);
			if (Motors.trayMotor.getOutputCurrent() > 15) {
				Motors.trayMotor.set(0);
			}
		}

		if (Motors.operator.getRawButton(8)) {
			Motors.trayMotor.set(-1);
			if (Motors.trayMotor.getOutputCurrent() > 15) {
				Motors.trayMotor.set(0);
			}
		}

		// End Tray Code

		// Roller Code
		if (Motors.operator.getRawButton(2)) {
			Motors.rollersLeft.set(-1);
			Motors.rollersRight.set(1);
		}
		if (Motors.operator.getRawButton(4)) {
			Motors.rollersLeft.set(1);
			Motors.rollersRight.set(-1);
		}
		if (Motors.operator.getRawButton(1)) {
			Motors.rollersLeft.set(0);
			Motors.rollersRight.set(0);
		}
		if (Motors.operator.getRawButton(3)) {
			Motors.rollersLeft.set(.5);
			Motors.rollersRight.set(.5);
		}
		// End Roller Code

		// Elevator Code TODO
		int offset = 0;
		if (Sensors.trayExtended.get()) {
			offset = 8;
		}

		boolean inc = Motors.operator.getRawButton(7);
		boolean dec = Motors.operator.getRawButton(5);

		if(elevatorCooldown.get() > ELEVATOR_COOLDOWN){
			elevatorCooldown.stop();
			elevatorCooldown.reset();
		}
		
		if (elevatorCooldown.get() == 0) {
			if (inc) {
				if (level < 5) {
					++level;
				}
				elevatorPositionEU = elevatorPos[level];
				// elevatorPositionEU = .5 + offset + (level * 12.1);
			}
			if (dec) {
				if (level > 0) {
					--level;
				}
				elevatorPositionEU = elevatorPos[level];

			}
			
			if (inc || dec) {
				elevatorCooldown.start();
			}
		}
		SmartDashboard.putString("level", String.valueOf(level));

		if (Motors.operator.getRawButton(12)
				&& (analogElevator > .1 || analogElevator < -.1)) {

		}

		SmartDashboard
				.putString("EU value", String.valueOf(elevatorPositionEU));
		SmartDashboard.putString("Slave 0 Current", String
				.valueOf(Motors.elevatorMaster.slaves[0].getOutputCurrent()));
		SmartDashboard.putString("Slave 1 Current", String
				.valueOf(Motors.elevatorMaster.slaves[1].getOutputCurrent()));
		SmartDashboard.putString("Slave 2 Current", String
				.valueOf(Motors.elevatorMaster.slaves[2].getOutputCurrent()));
		SmartDashboard.putString("Master ",
				String.valueOf(Motors.elevatorMaster.getOutputCurrent()));

		Motors.elevatorMaster.enableControl();
		Motors.elevatorMaster.setXEU(elevatorPositionEU);

		// Elevator Code End

	}

}
