package org.usfirst.frc.team20.robot;

import edu.wpi.first.wpilibj.CANTalon.ControlMode;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class OperatorControls {

	// For Claw Positions
	public static double p = .5;
	public static double i = .0001;
	public static double d = .5;
	public static double ramp = 1500;
	public static double talFil = 0;

	private static double elevatorPositionEU = 0;

	private static double level0 = 1;
	private static double level1 = 13.1;
	private static double level2 = 20.6;
	private static double level3 = 32.1;
	private static double level4 = 44.8;
	private static double level5 = 56.9;
	private static double level6 = 60;

	// TODO Update Axis Values!
	public static void opControls() {
		int elevatorPos = 0;
		double elevatorEnc = Motors.elevatorMaster.getEncPosition();
		double analogElevator = Motors.operator.getRawAxis(1);
		double analogFork = -Motors.operator.getRawAxis(2);
		double clawState = Motors.operator.getPOV();

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
		if (Motors.operator.getRawButton(5)) {
			Motors.forksMotor.set(200);
		}
		double talCur = Motors.forksMotor.getOutputCurrent();
		talFil = talFil * .9 + talCur * .1;
		if (talFil > 15) {
			Motors.forksMotor.set(Motors.forksMotor.getPosition());
		}
		if (Motors.operator.getRawButton(12)) {
			Motors.forksMotor.setPosition(0);
			Motors.elevatorMaster.set(Motors.elevatorMaster.getEncPosition());
		}
		// Motors.forksMotor.changeControlMode(ControlMode.PercentVbus);
		// Motors.forksMotor.set(analogFork);

		if (talFil > 15) {
			Motors.forksMotor.set(Motors.forksMotor.getPosition());
		}
		// End KnoxKode
		// Fork Code End

		// Tray Code
		if (Motors.operator.getRawButton(6)) {
			if (Sensors.trayExtended.get()) {
				Motors.trayMotor.set(-1);
				if (Motors.trayMotor.getOutputCurrent() > 15) {
					Motors.trayMotor.set(0);
				}
			}
			if (Sensors.trayRetracted.get()) {
				Motors.trayMotor.set(1);
				if (Motors.trayMotor.getOutputCurrent() > 15) {
					Motors.trayMotor.set(0);
				}
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

		if (Motors.operator.getRawButton(7)) {
			++elevatorPos;
			if(elevatorPos > 7){
		}
			}

//		if (Motors.operator.getRawButton(5)) {
//			elevatorPositionEU = 2;
//		}

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
