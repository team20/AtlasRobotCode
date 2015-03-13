package org.usfirst.frc.team20.robot;

import edu.wpi.first.wpilibj.Timer;
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
	private static final double ELEVATOR_COOLDOWN = .365;
	private static double elevatorPositionEU = 0;
	private static boolean manualControl = false;
	public static int level = 0;

	public static boolean trayExtended = false;

	private static double elevatorPos[] = { .1, 12.6, 24.7, 36.8, 48.9, 60 };
	private static double elevatorTrayPos[] = { 7.125, 12.6, 24.7, 36.8, 48.9,
			60 };
	static double forksSetpoint = 0;
	static boolean trippedForks = false;

	public static void opControls() {
		double elevatorActual = 0;
		double analogElevator = Motors.operator.getRawAxis(3);
		double analogFork = Motors.operator.getRawAxis(0);
		int povVal = Motors.operator.getPOV();

		// Claw Code
		// KnoxKode for PID Forks
		if (analogFork > .1 || analogFork < -.1) {
			manualFork = true;
		} else {
			manualFork = false;
		}

		SmartDashboard.putString("Manual forks override:", "" + manualFork);
		SmartDashboard.putString("Voltage too forks:",
				"" + Motors.forksMotor.getOutputVoltage());
		SmartDashboard.putString("Most recent fork setpoint", ""
				+ Motors.forksMotor.getSetpoint());
		SmartDashboard.putString("Forks position",
				"" + Motors.forksMotor.getPosition());
		SmartDashboard.putString("Elevator Outlier Difference", ""
				+ Motors.elevatorMaster.outlierDiff);
		SmartDashboard.putString("Elevator Outlier Talon", ""
				+ Motors.elevatorMaster.outlierTalon);

		// Motors.forksMotor.changeControlMode(ControlMode.PercentVbus);
		// Motors.forksMotor.set(-analogFork);

		if (manualFork == true) {
			forksSetpoint += -analogFork * 1.5;
			if (forksSetpoint < Motors.forksMotor.scaleXDSpan) {
				forksSetpoint = Motors.forksMotor.scaleXDSpan;
			} else if (forksSetpoint > Motors.forksMotor.scaleXDZero) {
				forksSetpoint = Motors.forksMotor.scaleXDZero;
			}
		}

		if (manualFork = false) {
			if (Motors.operator.getPOV() == 270) {
				forksSetpoint = 34.5;
				Motors.forksMotor.tripped = false;
				// Maybe widest setting
			} else if (Motors.operator.getPOV() == 180) {
				forksSetpoint = 17.25;
				Motors.forksMotor.tripped = false;
				// Maybe container
			} else if (Motors.operator.getPOV() == 0) {
				forksSetpoint = 25.375;
				Motors.forksMotor.tripped = false;
				// Maybe tote wide?
			} else if (Motors.operator.getPOV() == 90) {
				forksSetpoint = 16;
				Motors.forksMotor.tripped = false;
				// Maybe tote narrow
			}
		}

		if (Motors.operator.getRawButton(10)) {
			forksSetpoint = 30.8125;
			// open at HP
		}

		SmartDashboard.putString("POV VALUGH",
				String.valueOf(Motors.operator.getPOV()));
		// }
		/*
		 * double talCur = Motors.forksMotor.getOutputCurrent(); talFil = talFil
		 * * .95 + talCur * .05; if (talFil > 10) { trippedForks = true;
		 * forksSetpoint = Motors.forksMotor.getXEU(); }
		 */

		Motors.forksMotor.setXEU(forksSetpoint);

		SmartDashboard.putString("Manual forks override:", "" + manualFork);
		SmartDashboard.putString("Voltage too forks:",
				"" + Motors.forksMotor.getOutputVoltage());
		SmartDashboard.putString("Forks position",
				"" + Motors.forksMotor.getPosition());
		SmartDashboard.putString("Local Filter Current to forks", "" + talFil);
		SmartDashboard.putString("Filtered Current to forks", ""
				+ Motors.forksMotor.totalOutputCurrent);
		SmartDashboard.putString("Instant Current to forks", ""
				+ Motors.forksMotor.getOutputCurrent());
		SmartDashboard.putString("forks Setpoint:", "" + forksSetpoint);
		SmartDashboard.putString("Forks Actual Setpoint", ""
				+ Motors.forksMotor.getSetpoint());
		SmartDashboard.putString("forks motor tripped:", "" + trippedForks);
		SmartDashboard.putString("forks Homed:", "" + Motors.forksMotor.homed);

		// End KnoxKode
		// IF CLAW POSITIONS DO NOT WORK USE THE MANUAL OVERRIDE BELOW!
		// Motors.forksMotor.changeControlMode(ControlMode.PercentVbus);
		// Motors.forksMotor.set(analogFork);
		// Fork Code End

		// Tray Code
		if (Motors.operator.getRawButton(6)) {
			Motors.trayMotor.set(1);
			if (Robot.trayMotorFilteredCurrent > 15) {
				Motors.trayMotor.set(0);
			}
			trayExtended = true;
		}

		if (Motors.operator.getRawButton(8)) {
			Motors.trayMotor.set(-1);
			if (Robot.trayMotorFilteredCurrent > 15) {
				Motors.trayMotor.set(0);
			}
			trayExtended = false;
		}
		SmartDashboard.putString("Tray Current:", ""
				+ Robot.trayMotorFilteredCurrent);

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

		// if (elevatorCooldown.get() == 0) {
		// if ((inc || dec) && manualControl == true) {
		// manualControl = false;
		// for (double i = 0; i < 12.1; i += 0.1) {
		// if ((Motors.elevatorMaster.getXEU() - offset) % 12.1 == 0) {
		// level = (Motors.elevatorMaster.getXEU() - offset) / 12.1;
		// } else {
		// toNextLevelUp += i;
		// }
		// }
		// level -= 1;
		// }
		// if (inc) {
		// if (level < 5) {
		// ++level;
		// }
		// elevatorPositionEU = offset + toNextLevelUp + (level * 12.1);
		// }
		// if (dec) {
		// if (level > 0) {
		// --level;
		// }
		// elevatorPositionEU = offset - (12.1 - toNextLevelUp) + level
		// * 12.1;
		// }
		//
		// if (inc || dec) {
		// elevatorCooldown.start();
		// }
		// }
		if (Motors.operator.getRawButton(9)) {
			Motors.elevatorMaster.tripped = false;
			Motors.forksMotor.tripped = false;
		}

		if (analogElevator > .1 || analogElevator < -.1) {
			if (manualControl == false) {
				manualControl = true;
			} else {
				manualControl = false;
			}
			elevatorPositionEU = Motors.elevatorMaster.getXEU() * -1
					+ (-15 * analogElevator);
		}

		if (elevatorCooldown.get() == 0) {
			if (inc || dec) {
				elevatorCooldown.start();
				manualControl = false;
				if (inc) {
					if (level < 5) {
						++level;
					}
					if (trayExtended) {
						elevatorPositionEU = elevatorTrayPos[level];
					} else {
						elevatorPositionEU = elevatorPos[level];
					}
				}

				if (dec) {
					if (level > 0) {
						--level;
					}
					if (trayExtended) {
						elevatorPositionEU = elevatorTrayPos[level];
					} else {
						elevatorPositionEU = elevatorPos[level];
					}
				}
			}
		}

		SmartDashboard.putString("level", String.valueOf(level));

		// switch to manual control
<<<<<<< HEAD
=======

		if (Motors.operator.getRawButton(12)
				&& (analogElevator > .1 || analogElevator < -.1)) {
			if (manualControl == false){
				manualControl = true;
			}
			elevatorPositionEU = Motors.elevatorMaster.getXEU() * -1
					+ (-30 * analogElevator);
		}
>>>>>>> origin/master

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
		SmartDashboard.putString("Elevator Total ",
				String.valueOf(Motors.elevatorMaster.totalOutputCurrent));
		SmartDashboard.putString("Elevator tripped on current", ""
				+ Motors.elevatorMaster.tripped);
		SmartDashboard.putString("Forks Tripped on current", ""
				+ Motors.forksMotor.tripped);

		//
		// Motors.elevatorMaster.enableControl();

		if (Motors.operator.getRawButton(12)) {
			Motors.elevatorMaster.homed = false;
			elevatorPositionEU = 0;
			level = 0;
		}

		Motors.elevatorMaster.setXEU(elevatorPositionEU);

		SmartDashboard.putString("EU value",
				String.valueOf(Motors.elevatorMaster.getXEU()));

		// manual control for elevator

		//
		// if (analogElevator > .1 || analogElevator < -.1) {
		// Motors.elevatorMaster
		// .setXEU(Motors.elevatorMaster.getXEU()*-1+(-30*analogElevator)); }

		// Elevator Code End
		if (Motors.operator.getRawButton(11) && Motors.operator.getRawButton(1)) {
			hirenTestBool = true;
			step = 0;
		}

		if (hirenTestBool) {
			hirensTestMethod();
		}

	}

	public static boolean hirenTestBool = false;
	public static int step = 0;

	public static void hirensTestMethod() {
		// don't flux with this
		if (hirenTestBool == true) {

			if (step == 0) {
				elevatorPositionEU = elevatorTrayPos[1] + .7;
				level = 1;
				step = 1;
			}
			if (Motors.elevatorMaster.getXEU() < 14 && step == 1) {
				forksSetpoint = 34;
				step = 2;
			}
			if (Motors.forksMotor.getXEU() > 32.5 && step == 2) {
				elevatorPositionEU = elevatorTrayPos[0];
				level = 0;
				step = 3;
			}
			if (Motors.elevatorMaster.getXEU() < 8.5 && step == 3) {
				forksSetpoint = 25.375;
				step = 4;
			}
			if (Motors.forksMotor.getXEU() < 26 && step == 4) {
				elevatorPositionEU = elevatorTrayPos[3];
				level = 3;
				step = 5;
			}

			Motors.elevatorMaster.setXEU(elevatorPositionEU);
			Motors.forksMotor.setXEU(forksSetpoint);
			if (step == 5) {
				hirenTestBool = false;
			}
		}
	}
}
