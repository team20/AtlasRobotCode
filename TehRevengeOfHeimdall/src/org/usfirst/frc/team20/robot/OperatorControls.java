package org.usfirst.frc.team20.robot;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class OperatorControls {

	private static boolean manualFork = false;

	// For Elevator
	private static Timer elevatorCooldown = new Timer();
	private static final double ELEVATOR_COOLDOWN = .365;
	public static double elevatorPositionEU = 0;
	private static boolean manualControl = false;
	public static int level = 0;
	public static boolean toteCatcherBool = false;

	protected static double elevatorPos[] = { 0, 12.6, 24.7, 36.8, 48.9, 60 };
	protected static double elevatorTrayPos[] = { 4.5, 18.25, 24.7, 36.8, 48.9,
			60 };

	protected static double elevatorPosPlace[] = { 1, 3, 5, 14 };
	protected static double elevatorTrayPlace[] = { 2, 4.5, 9, 14 };

	private static T20AutoStack autoStack = new T20AutoStack(
			Motors.elevatorMaster, Motors.forksMotor);
	private static CopyofAutoStack autoCatchStack = new CopyofAutoStack(
			Motors.elevatorMaster, Motors.forksMotor);
	private static T20AutoStraighten autoStraighten = new T20AutoStraighten(
			Motors.elevatorMaster, Motors.forksMotor);
	private static T20AutoPlace autoPlace = new T20AutoPlace(
			Motors.elevatorMaster, Motors.forksMotor);

	public static double forksSetpoint = Motors.forksMotor.scaleXDSpan;
	static boolean trippedForks = false;

	private static int step = 0;

	// For tray
	public static final boolean TRAY_EXTEND = true, TRAY_RETRACT = false;
	public static boolean trayExtended = false;
	public static boolean traySetPoint = TRAY_RETRACT;

	public static void opControls() {
		double elevatorActual = 0;
		double analogElevator = Motors.operator.getRawAxis(5);
		double analogFork = Motors.operator.getRawAxis(0);
		int povVal = Motors.operator.getPOV();

		// Claw Code
		// KnoxKode for PID Forks
		if (analogFork > .1 || analogFork < -.1) {
			manualFork = true;
		} else {
			manualFork = false;
		}

		if (manualFork == true) {
			forksSetpoint += -analogFork * 1.5;
			if (forksSetpoint < Motors.forksMotor.scaleXDSpan) {
				forksSetpoint = Motors.forksMotor.scaleXDSpan;
			} else if (forksSetpoint > Motors.forksMotor.scaleXDZero) {
				forksSetpoint = Motors.forksMotor.scaleXDZero;
			}
		}

		// if (manualFork = false) {
		if (Motors.operator.getPOV() == 270) {
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
			forksSetpoint = 15.5;
			Motors.forksMotor.tripped = false;
			// Maybe tote narrow
		}

		if (Motors.operator.getRawButton(8)) {
			forksSetpoint = 30.8125;
			// open at HP
		}

		// Elevator Code

		// preset Positions
		double offset = 0.5;
		double toNextLevelUp = 0;

		if (trayExtended) {
			offset = 7.1;
		}

		boolean inc = Motors.operator.getRawButton(5);
		boolean dec = Motors.operator.getRawButton(6);

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
		if (Motors.operator.getRawButton(7)) {
			elevatorPositionEU = Motors.elevatorMaster.getXEU();
			Motors.elevatorMaster.tripped = false;
			Motors.forksMotor.tripped = false;
		}

		if (analogElevator > .1 || analogElevator < -.1) {
			if (manualControl == false) {
				manualControl = true;
			} else {
				manualControl = false;
			}
			elevatorPositionEU = elevatorPositionEU + (-1.5 * analogElevator);
			if (-elevatorPositionEU < Motors.elevatorMaster.span) {
				elevatorPositionEU = -Motors.elevatorMaster.span;
			} else if (-elevatorPositionEU > Motors.elevatorMaster.zero) {
				elevatorPositionEU = -Motors.elevatorMaster.zero;
			}
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

		SmartDashboard.putString("Elevator SetPoint!",
				String.valueOf(Motors.elevatorMaster.getEncPosition()));

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

		if (Motors.operator.getRawButton(10)) {
			Motors.elevatorMaster.homed = false;
			elevatorPositionEU = 0;
			level = 0;
		}
		if (Motors.operator.getRawButton(9)) {
			Motors.forksMotor.homed = false;
			forksSetpoint = Motors.forksMotor.scaleXDSpan;
		}

		if (!autoStack.isInterrupted()) {
			autoStack.calculate();
			elevatorPositionEU = autoStack.getElevatorSetpoint();
			forksSetpoint = autoStack.getForkSetpoint();
		}
		if (!autoStraighten.isInterrupted()) {
			autoStraighten.calculate();
			elevatorPositionEU = autoStraighten.getElevatorSetpoint();
			forksSetpoint = autoStraighten.getForkSetpoint();
		}
		if (!autoCatchStack.isInterrupted()) {
			autoCatchStack.calculate();
			elevatorPositionEU = autoCatchStack.getElevatorSetpoint();
			forksSetpoint = autoCatchStack.getForkSetpoint();
		}
		if (!autoPlace.isInterrupted()) {
			int trayPos = autoPlace.getTrayPosition();
			if (trayPos != autoPlace.TRAY_POS_UNDEF) {
				if (trayPos == autoPlace.TRAY_EXTENDED)
					autoPlace.setElevatorPositions(elevatorTrayPlace);
				else
					autoPlace.setElevatorPositions(elevatorPosPlace);
				
				autoPlace.calculate();
				elevatorPositionEU = autoPlace.getElevatorSetpoint();
				forksSetpoint = autoPlace.getForkSetpoint();
				traySetPoint = autoPlace.getTraySetPoint();
			}
		}

		// manual control for elevator

		//
		// if (analogElevator > .1 || analogElevator < -.1) {
		// Motors.elevatorMaster
		// .setXEU(Motors.elevatorMaster.getXEU()*-1+(-30*analogElevator)); }

		// Elevator Code End

		if (Motors.operator.getRawButton(3)) {
			if (trayExtended)
				autoStack.setElevatorPositions(elevatorTrayPos);
			else
				autoStack.setElevatorPositions(elevatorPos);
			autoStack.start();
		}
		if (Motors.operator.getRawButton(2)) {
				autoStack.setElevatorPositions(1.75, 24.7,38 ,38,48.9);
				autoStack.start();
		}
		if(Motors.operator.getRawButton(1)){		
				autoStack.setElevatorPositions(12.6, 24.7, 40 ,48.9);
				autoStack.start();
			}
			
		
		// if (Motors.operator.getRawButton(2) &&
		// Motors.operator.getRawButton(8)) {
		// if (trayExtended)
		// autoStraighten.setElevatorPositions(elevatorTrayPos);
		// else
		// autoStraighten.setElevatorPositions(elevatorPos);
		// autoStraighten.start();
		// }
		// if (Motors.operator.getRawButton(3) &&
		// Motors.operator.getRawButton(8)) {
		// if (autoPlace.getTrayPosition() == autoPlace.TRAY_EXTENDED)
		// autoPlace.start();
		// }
		if (Motors.operator.getRawButton(4)) {
			autoStraighten.interrupt();
			autoStack.interrupt();
			autoPlace.interrupt();
		}

		Motors.forksMotor.setXEU(forksSetpoint);
		if (Motors.forksMotor.homed)
			Motors.elevatorMaster.setXEU(elevatorPositionEU);
	}

	public static void oneRCAuto() {
		Timer hTime = new Timer();
		final double ONE_RC_AUTO_STOP_MOVE = 2.5;
		hTime.start();
		double forksSet = 17.25;
		Motors.elevatorMaster.setXEU(1);
		Motors.forksMotor.setXEU(forksSet);

		if (Math.abs(Motors.forksMotor.getXEU() - forksSet) < 5
				&& Motors.elevatorMaster.homed) {

			Motors.elevatorMaster.setXEU(5);
			if (hTime.get() < ONE_RC_AUTO_STOP_MOVE) {
				Motors.bLeft.set(.5);
				Motors.fLeft.set(.5);
				Motors.bRight.set(-.5);
				Motors.fRight.set(-.5);
			}
			if (hTime.get() > ONE_RC_AUTO_STOP_MOVE) {
				hTime.stop();
				Motors.bLeft.set(0);
				Motors.fLeft.set(0);
				Motors.bRight.set(0);
				Motors.fRight.set(0);
			}
		}
		if (hTime.get() > ONE_RC_AUTO_STOP_MOVE) {
			hTime.stop();
			Motors.bLeft.set(0);
			Motors.fLeft.set(0);
			Motors.bRight.set(0);
			Motors.fRight.set(0);
		}
	}

}