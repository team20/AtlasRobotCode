package org.usfirst.frc.team20.robot;

import edu.wpi.first.wpilibj.CANTalon.ControlMode;
import edu.wpi.first.wpilibj.Joystick.RumbleType;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class OperatorControls {

	// For Claw Positions
	public static double p = .5;
	public static double i = .0001;
	public static double d = .5;
	public static double ramp = 1500;
	public static double talFil = 0;

	public static int POVal = 0;

	private static Timer elevatorTimer = new Timer();
	private static double ELEVATOR_COOLDOWN = .1;
	private static boolean maintainSetpoint = false;
	private static boolean trayBool = false;

	// TODO Update Axis Values!
	public static void opControls() {
		double elevatorEnc = Motors.elevatorMaster.getEncPosition();
		double analogElevator = Motors.operator.getRawAxis(1);
		double analogFork = -Motors.operator.getRawAxis(2);
		double clawState = Motors.operator.getPOV();
		
		//Interrupt
		Motors.forksMotor.changeControlMode(ControlMode.PercentVbus);
		Motors.elevatorMaster.changeControlMode(ControlMode.PercentVbus);
		Motors.elevatorMaster.set(0);
		Motors.forksMotor.set(0);
		Motors.rollersLeft.set(0);
		Motors.rollersRight.set(0);
		Motors.trayMotor.set(0);
		//End Interrupt
		
		// Claw Code
		// KnoxKode for PID Forks
		// if (Motors.operator.getPOV() == 270) {
		// Motors.forksMotor.set(25000);
		// POVal = 270;
		// }
		// if (Motors.operator.getPOV() == 0) {
		// Motors.forksMotor.set(59000);
		// POVal = 180;
		// }
		// if (Motors.operator.getPOV() == 90) {
		// Motors.forksMotor.set(16000);
		// POVal = 0;
		// }
		// if (Motors.operator.getPOV() == 180) {
		// Motors.forksMotor.set(75000);
		// POVal = 90;
		// }
		// if (Motors.operator.getPOV() == 135) {
		// Motors.forksMotor.set(200);
		// }
		// double talCur = Motors.forksMotor.getOutputCurrent();
		// talFil = talFil * .9 + talCur * .1;
		// SmartDashboard.putString("Current fork = ", "" + talFil);
		// SmartDashboard.putString("Fork enc = ",
		// "" + Motors.forksMotor.getEncPosition());
		// if (talFil > 15) {
		// Motors.forksMotor.set(Motors.forksMotor.getPosition());
		// }
		// SmartDashboard.putString("Fork sp = ",
		// "" + Motors.forksMotor.getSetpoint());
		// if (Motors.operator.getRawButton(12)) {
		// Motors.forksMotor.setPosition(0);
		// Motors.elevatorMaster.set(Motors.elevatorMaster.getEncPosition());
		// }
		Motors.forksMotor.changeControlMode(ControlMode.PercentVbus);
		Motors.forksMotor.set(analogFork);
		if (Motors.forksMotor.getOutputCurrent() > 15) {
			Motors.operator.setRumble(RumbleType.kRightRumble, 1);
			Motors.operator.setRumble(RumbleType.kLeftRumble, 1);
			Motors.forksMotor.set(0);
		} else {
			Motors.operator.setRumble(RumbleType.kRightRumble, 0);
			Motors.operator.setRumble(RumbleType.kLeftRumble, 0);
		}

		// if (talFil > 15) {
		// Motors.forksMotor.set(Motors.forksMotor.getPosition());
		// }
		// End KnoxKode
		// Fork Code End
		
		// Tray Code TODO Once Ele encoder works
		if (Motors.operator.getRawButton(6)) {
			trayBool = !trayBool;
			if (trayBool /*&& 
		Motors.elevatorMaster.getEncPosition()<400*/) {
				Motors.trayMotor.set(1);
			} else if (!trayBool) {
				Motors.trayMotor.set(-1);
			}
			if (!Motors.operator.getRawButton(6)
					&& (Motors.trayMotor.getOutputCurrent() > 20
							|| !Sensors.trayExtened.get() || !Sensors.trayRetracted
								.get())) {
				Motors.trayMotor.set(0);
			}
		}
		// End Tray Code

		// Roller Code
		if (Motors.operator.getRawButton(2)) {
			Motors.rollersLeft.set(-1);
			Motors.rollersRight.set(1);
		}
		if (Motors.operator.getRawButton(10)) {
			Motors.rollersLeft.set(1);
			Motors.rollersRight.set(-1);
		}
		if (Motors.operator.getRawButton(1)) {
			Motors.rollersLeft.set(0);
			Motors.rollersRight.set(0);
		}
		//TODO
//		if (Motors.operator.getRawButton(3)) {
//			Motors.rollersLeft.set(.5);
//			Motors.rollersRight.set(.5);
//		}
		// End Roller Code

		// Elevator Code

		// Zero The Elevator When It Reaches The Bottom
		if (!Sensors.elevatorShort.get()) {
			Motors.elevatorMaster.changeControlMode(ControlMode.Position);
			Motors.elevatorMaster.setPosition(0);
			Motors.elevatorMaster.changeControlMode(ControlMode.PercentVbus);

			Motors.operator.setRumble(RumbleType.kRightRumble, .5f);
			Motors.operator.setRumble(RumbleType.kLeftRumble, .5f);
		} else {
			Motors.operator.setRumble(RumbleType.kRightRumble, 0);
			Motors.operator.setRumble(RumbleType.kLeftRumble, 0);
		}

		// boolean decrement = Motors.operator.getRawButton(5);
		// boolean increment = Motors.operator.getRawButton(7);
		//
		// if((increment || decrement) && elevatorTimer.get() == 0)
		// elevatorTimer.start();
		//
		// if(elevatorTimer.get() >= ELEVATOR_COOLDOWN){
		// elevatorTimer.stop();
		// elevatorTimer.reset();
		// }
		//
		// if (decrement && elevatorTimer.get() == 0) {
		// elevatorTimer.start();
		//
		// if (level > 0) {
		// level--;
		// }
		// maintainSetpoint = true;
		// setPoint = converLevel(level);
		// }else if (increment && elevatorTimer.get() == 0) {
		// elevatorTimer.start();
		// if (level < 5) {
		// level++;
		// }
		// maintainSetpoint = true;
		// setPoint = converLevel(level);
		// }
		// if (Motors.operator.getRawAxis(1) > .2 ||
		// Motors.operator.getRawAxis(1) < -.2) {
		// maintainSetpoint = false;
		// Motors.elevatorMaster.set(Motors.operator.getRawAxis(1));
		// setPoint = Motors.elevatorMaster.getPosition();
		// }else{
		// if(!(Math.abs(Motors.elevatorMaster.getPosition() -
		// Robot.lastCycleEncoderPosition) < 10) && !maintainSetpoint){
		// setPoint = Motors.elevatorMaster.getPosition();
		// }
		// goToPosition(setPoint);
		// }
		//
		// SmartDashboard.putString("Elevator pos", ""+level);
		// SmartDashboard.putString("Elevator ENC Val",""+Motors.elevatorMaster.getPosition());
		// // Elevator Code End
		//
		// Robot.lastCycleEncoderPosition = Motors.elevatorMaster.getPosition();
		//
		// }
		// public static int level = 0;
		// public static double setPoint = 0;
		// public static int level0 = 0;
		// public static int level1 = 2000;
		// public static int level2 = 4000;
		// public static int level3 = 6000;
		// public static int level4 = 8000;
		// public static int level5 = 10000;
		//
		// public static int converLevel(int level){
		// switch (level) {
		// case 0:
		// return level0;
		// case 1:
		// return level1;
		// case 2:
		// return level2;
		// case 3:
		// return level3;
		// case 4:
		// return level4;
		// case 5:
		// return level5;
		// default:
		// return -1;
		// }
	}

	public static void goToPosition(double target) {
		// double location = Motors.elevatorMaster.getPosition();
		// double displacement = target - location;
		// double rampStart = 2000;
		// if (displacement < rampStart && displacement > 200) {
		// Motors.elevatorMaster.set(-displacement * 3 /(rampStart));
		// }else if (displacement > -rampStart && displacement < -200) {
		// Motors.elevatorMaster.set(displacement * 3/(-rampStart));
		// }else if (displacement > rampStart) {
		// Motors.elevatorMaster.set(-1);
		// }else if (displacement < -rampStart) {
		// Motors.elevatorMaster.set(1);
		// }else{
		// Motors.elevatorMaster.set(0);
		// }
		// }
	}
}
