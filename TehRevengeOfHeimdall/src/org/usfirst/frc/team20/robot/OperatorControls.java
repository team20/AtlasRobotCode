package org.usfirst.frc.team20.robot;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.CANTalon.ControlMode;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class OperatorControls {

	// For Claw
	public static double p = .5;
	public static double i = .0001;
	public static double d = .5;
	public static double ramp = 1500;
	public static double talFil = 0;
	private static boolean manualFork = false;

	// For Elevator
	private static Timer elevatorCooldown = new Timer();
	private static final double ELEVATOR_COOLDOWN = .7;
	private static double elevatorPositionEU = 0;
	private static boolean manualControl = false;
	public static double level = 0;

	public static boolean trayExtended = false;

	// private static double elevatorPos[] = { .5, 12.6, 24.7, 36.8, 48.9, 60 };

	public static void opControls() {
		double elevatorActual = 0;
		double analogElevator = Motors.operator.getRawAxis(3);
		double analogFork = Motors.operator.getRawAxis(0);
		int povVal = Motors.operator.getPOV();

		// Claw Code
		// KnoxKode for PID Forks
		if (Motors.operator.getRawButton(11)) {
			manualFork = true;
		} else {
			manualFork = false;
		}
		if (manualFork == true && (analogFork > .1 || analogFork < -.1)) {
			Motors.forksMotor.set(Motors.forksMotor.getEncPosition()
					+ (analogFork * 400));
		}

		if (manualFork = false) {
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
		// IF CLAW POSITIONS DO NOT WORK USE THE MANUAL OVERRIDE BELOW!
		// Motors.forksMotor.changeControlMode(ControlMode.PercentVbus);
		// Motors.forksMotor.set(analogFork);
		// Fork Code End

		// Tray Code
		if (Motors.operator.getRawButton(6)) {
			Motors.trayMotor.set(1);
			if (Motors.trayMotor.getOutputCurrent() > 15) {
				Motors.trayMotor.set(0);
			}
			trayExtended = true;
		}

		if (Motors.operator.getRawButton(8)) {
			Motors.trayMotor.set(-1);
			if (Motors.trayMotor.getOutputCurrent() > 15) {
				Motors.trayMotor.set(0);
			}
			trayExtended = false;
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

		// Elevator Code

		// preset Positions
		double offset = 0.5;
		double toNextLevelUp = 0;

		if (trayExtended) {
			offset = 7.5;
		}

		boolean inc = Motors.operator.getRawButton(7);
		boolean dec = Motors.operator.getRawButton(5);

		if (elevatorCooldown.get() > ELEVATOR_COOLDOWN) {
			elevatorCooldown.stop();
			elevatorCooldown.reset();
		}

		if (elevatorCooldown.get() == 0) {
			if ((inc || dec) && manualControl == true) {
				manualControl = false;
				for (double i = 0; i < 12.1; i += 0.1) {
					if ((Motors.elevatorMaster.getXEU() - offset) % 12.1 == 0) {
						level = (Motors.elevatorMaster.getXEU() - offset) / 12.1;
					} else {
						toNextLevelUp += i;
					}
				}
				level -= 1;
			}
			if (inc) {
				if (level < 5) {
					++level;
				}
				elevatorPositionEU = offset + toNextLevelUp + (level * 12.1);
			}
			if (dec) {
				if (level > 0) {
					--level;
				}
				elevatorPositionEU = offset - (12.1 - toNextLevelUp) + level
						* 12.1;
			}

			if (inc || dec) {
				elevatorCooldown.start();
			}
		}
		SmartDashboard.putString("level", String.valueOf(level));

		// switch to manual control

		if (Motors.operator.getRawButton(12)
				&& (analogElevator > .1 || analogElevator < -.1)) {
			manualControl = true;
			elevatorPositionEU = Motors.elevatorMaster.getXEU() * -1
					+ (-30 * analogElevator);
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
		//
		// Motors.elevatorMaster.enableControl();
		Motors.elevatorMaster.setXEU(elevatorPositionEU);
		SmartDashboard.putString("EU value",
				String.valueOf(Motors.elevatorMaster.getXEU()));

		// manual control for elevator

		/*
		 * if (analogElevator > .1 || analogElevator < -.1) {
		 * Motors.elevatorMaster
		 * .setXEU(Motors.elevatorMaster.getXEU()*-1+(-30*analogElevator)); }
		 */
		// Elevator Code End

	}

}
