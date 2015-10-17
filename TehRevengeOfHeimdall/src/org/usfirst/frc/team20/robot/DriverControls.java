package org.usfirst.frc.team20.robot;

import edu.wpi.first.wpilibj.CANTalon.ControlMode;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class DriverControls {
	static int test = 0;
	static double angle = 0;
	static int trayPosPV = T20AutoPlace.TRAY_POS_UNDEF;
	static double bothStrafe = 0;
	static double bothSpeed = 0;
	static T20Stabilizer stable = new T20Stabilizer();

	public static void bothDrive() {

		if (Motors.driver.getRawButton(1)) {
			Motors.trayMotor.set(.5);
			OperatorControls.trayExtended = true;
		}
		if (Motors.driver.getRawButton(2)) {
			Motors.trayMotor.set(-.5);
			OperatorControls.trayExtended = false;
		}
		if (Motors.driver.getRawButton(3)
				|| Motors.trayMotor.getOutputCurrent() > 20) {
			Motors.trayMotor.set(0);
		}

		int povVal = Motors.driver.getPOV();
		Motors.fLeft.changeControlMode(ControlMode.PercentVbus);
		Motors.bLeft.changeControlMode(ControlMode.PercentVbus);
		Motors.fRight.changeControlMode(ControlMode.PercentVbus);
		Motors.bRight.changeControlMode(ControlMode.PercentVbus);

		if (povVal != -1) {
			if (povVal == 270) {
				bothStrafe = -.75;
			}
			if (povVal == 90) {
				bothStrafe = .75;
			}
			if (povVal == 180) {
				bothSpeed = .75;
			}
			if (povVal == 0) {
				bothSpeed = -.75;
			}

			double lturn = Motors.driver.getRawAxis(3);
			double rturn = Motors.driver.getRawAxis(2);

			double values[] = new double[4];// FL BL FR BR

			values[0] = -(bothSpeed + rturn - lturn + bothStrafe);// FL
			values[1] = -(bothSpeed + rturn - lturn - bothStrafe);// BL
			values[2] = (bothSpeed + lturn - rturn - bothStrafe);// FR
			values[3] = (bothSpeed + lturn - rturn + bothStrafe);// BR

			for (int i = 0; i < 4; i++) {
				if (values[i] > 1)
					values[i] = 1;
				if (values[i] < -1)
					values[i] = -1;
			}

			Motors.fLeft.set(values[0]);
			Motors.bLeft.set(values[1]);
			Motors.fRight.set(values[2]);
			Motors.bRight.set(values[3]);
		} else {
			double speed = filter(Motors.driver.getRawAxis(1));
			double strafe = filter(Motors.driver.getRawAxis(0));
			double lturn = Motors.driver.getRawAxis(3);
			double rturn = Motors.driver.getRawAxis(2);

			if (Motors.driver.getRawButton(5)) {
				Sensors.gyro.reset();
				msecs = 0;
				holdAngle = 0;
			}
			if (Motors.driver.getRawButton(7) && Motors.driver.getRawButton(9)) {
				Motors.fLeft.changeControlMode(ControlMode.PercentVbus);
				Motors.fRight.changeControlMode(ControlMode.PercentVbus);
				Motors.bLeft.changeControlMode(ControlMode.PercentVbus);
				Motors.bRight.changeControlMode(ControlMode.PercentVbus);
			}

			// start
			double values[] = new double[4];// FL BL FR BR

			if (test != 1) {
				angle = Sensors.gyro.getAngle();

			}

			if ((lturn == 0 && rturn == 0 && speed == 0 && strafe == 0)
					&& !(angle > lastGyro + 1f || angle < lastGyro - 1f)) {
				if (angle > holdAngle + 8f) {
					Motors.fLeft.set(-1);
					Motors.bLeft.set(-1);
					Motors.fRight.set(-1);
					Motors.bRight.set(-1);
				} else if (angle < holdAngle - 8f) {
					Motors.fLeft.set(1);
					Motors.bLeft.set(1);
					Motors.fRight.set(1);
					Motors.fLeft.set(1);
				} else {
					Motors.fLeft.set(0);
					Motors.bLeft.set(0);
					Motors.fRight.set(0);
					Motors.fLeft.set(0);
				}
			} else {
				holdAngle = angle;
			}
			// end
			lastGyro = angle;
			angle -= .0001f * msecs;
			angle *= Math.PI;
			angle /= 180;
			double theta = Math.atan(speed / strafe);

			if (!Double.isNaN(theta)) {
				angle = theta - angle;
			}
			SmartDashboard.putString("Angle Calculated", String.valueOf(angle));
			if (strafe < 0)
				angle -= Math.PI;

			double magnitude = Math.sqrt(speed * speed + strafe * strafe);
			speed = Math.sin(angle) * magnitude;
			strafe = Math.cos(angle) * magnitude;

			values[0] = -(speed + rturn - lturn + strafe);// FL
			values[1] = -(speed + rturn - lturn - strafe);// BL
			values[2] = (speed + lturn - rturn - strafe);// FR
			values[3] = (speed + lturn - rturn + strafe);// BR

			System.out.println("fLeft Value: " + values[0] + "Control Mode: "
					+ Motors.fLeft.getControlMode());
			System.out.println("bLeft Value: " + values[1] + "Control Mode: "
					+ Motors.bLeft.getControlMode());
			System.out.println("fRight Value: " + values[2] + "Control Mode: "
					+ Motors.fRight.getControlMode());
			System.out.println("bRight Value: " + values[3] + "Control Mode: "
					+ Motors.bRight.getControlMode());

			for (int i = 0; i < 4; i++) {
				if (values[i] > 1)
					values[i] = 1;
				if (values[i] < -1)
					values[i] = -1;
			}

			Motors.fLeft.set(values[0]);
			Motors.bLeft.set(values[1]);
			Motors.fRight.set(values[2]);
			Motors.bRight.set(values[3]);
			++msecs;
		}
	}

	public static void robotDrive() {

		Motors.fLeft.changeControlMode(ControlMode.PercentVbus);
		Motors.bLeft.changeControlMode(ControlMode.PercentVbus);
		Motors.fRight.changeControlMode(ControlMode.PercentVbus);
		Motors.bRight.changeControlMode(ControlMode.PercentVbus);

		double speed = Motors.driver.getRawAxis(1);
		double strafe = Motors.driver.getRawAxis(0);
		double lturn = Motors.driver.getRawAxis(3);
		double rturn = Motors.driver.getRawAxis(2);

		double values[] = new double[4];// FL BL FR BR

		values[0] = -(speed + rturn - lturn + strafe);// FL
		values[1] = -(speed + rturn - lturn - strafe);// BL
		values[2] = (speed + lturn - rturn - strafe);// FR
		values[3] = (speed + lturn - rturn + strafe);// BR

		for (int i = 0; i < 4; i++) {
			if (values[i] > 1)
				values[i] = 1;
			if (values[i] < -1)
				values[i] = -1;
		}

		Motors.fLeft.set(values[0]);
		Motors.bLeft.set(values[1]);
		Motors.fRight.set(values[2]);
		Motors.bRight.set(values[3]);
		SmartDashboard.putString("Gyro: ",
				String.valueOf(Sensors.gyro.getAngle()));

		/*
		 * System.out.println("Values" + java.util.Arrays.toString(values));
		 * System.out.println("Front Left " + Motors.fLeft.getEncPosition());
		 * SmartDashboard.putString("Front Left",
		 * String.valueOf(Motors.fLeft.getEncPosition()));
		 * System.out.println("Back Left " + Motors.bLeft.getEncPosition());
		 * SmartDashboard.putString("Back Left",
		 * String.valueOf(Motors.bLeft.getEncPosition()));
		 * System.out.println("Front Right " + Motors.fRight.getEncPosition());
		 * SmartDashboard.putString("Front Right",
		 * String.valueOf(Motors.fRight.getEncPosition()));
		 * System.out.println("Back Right " + Motors.bRight.getEncPosition());
		 * SmartDashboard.putString("Back Right",
		 * String.valueOf(Motors.bRight.getEncPosition()));
		 * System.out.println("Hello");
		 */

		if (Motors.driver.getRawButton(1)) {
			Motors.trayMotor.set(1);
			OperatorControls.trayExtended = true;
			trayPosPV = T20AutoPlace.TRAY_POS_UNDEF;
		}
		if (Motors.driver.getRawButton(2)) {
			Motors.trayMotor.set(-1);
			OperatorControls.trayExtended = false;
			trayPosPV = T20AutoPlace.TRAY_POS_UNDEF;
		}
		if (Motors.driver.getRawButton(3)) {
			Motors.trayMotor.set(0);
			trayPosPV = T20AutoPlace.TRAY_POS_UNDEF;

		}
		if (Robot.trayMotorFilteredCurrent > 10) {
			Motors.trayMotor.set(0);
			if (OperatorControls.trayExtended = true) {
				trayPosPV = T20AutoPlace.TRAY_EXTENDED;
			} else {
				trayPosPV = T20AutoPlace.TRAY_RETRACTED;
			}
		}

		if (Motors.driver.getPOV() == 270) {
			Motors.canGrabberMotor.set(0);

		}
		if (Motors.driver.getPOV() == 180) {
			Motors.canGrabberMotor.set(-.5);

		}
		if (Motors.driver.getPOV() == 0) {
			Motors.canGrabberMotor.set(.5);

		}
		if (Motors.driver.getPOV() == 90) {
			Motors.canGrabberMotor.set(0);

		}
		if (Motors.driver.getPOV() == -1) {
			Motors.canGrabberMotor.set(0);
		}

	}

	private static double filter(double input) {
		if (input < .05f && input > -.05f)
			return 0;
		return input;
	}

	static long msecs = 0;
	static double holdAngle = 0;
	static double lastGyro = 0;

	public static void fieldDrive() {

		if (Motors.driver.getRawButton(1)) {
			Motors.trayMotor.set(.5);
			OperatorControls.trayExtended = true;
		}
		if (Motors.driver.getRawButton(2)) {
			Motors.trayMotor.set(-.5);
			OperatorControls.trayExtended = false;
		}
		if (Motors.driver.getRawButton(3)
				|| Motors.trayMotor.getOutputCurrent() > 20) {
			Motors.trayMotor.set(0);
		}

		if (Motors.driver.getPOV() == 270) {
			Motors.canGrabberMotor.set(-.2);

		}
		if (Motors.driver.getPOV() == 180) {
			Motors.canGrabberMotor.set(0);

		}
		if (Motors.driver.getPOV() == 0) {
			Motors.canGrabberMotor.set(0);

		}
		if (Motors.driver.getPOV() == 90) {
			Motors.canGrabberMotor.set(.2);

		}
		if (Motors.driver.getPOV() == -1) {
			Motors.canGrabberMotor.set(0);
		}
		SmartDashboard.putString("Tray Extended", ""
				+ OperatorControls.trayExtended);

		double speed = filter(Motors.driver.getRawAxis(1));
		double strafe = filter(Motors.driver.getRawAxis(0));
		double lturn = filter(Motors.driver.getRawAxis(3));
		double rturn = filter(Motors.driver.getRawAxis(2));

		test++;

		if (Motors.driver.getRawButton(5)) {
			Sensors.gyro.reset();
			msecs = 0;
			holdAngle = 0;
		}
		if (Motors.driver.getRawButton(7) && Motors.driver.getRawButton(9)) {
			Motors.fLeft.changeControlMode(ControlMode.PercentVbus);
			Motors.fRight.changeControlMode(ControlMode.PercentVbus);
			Motors.bLeft.changeControlMode(ControlMode.PercentVbus);
			Motors.bRight.changeControlMode(ControlMode.PercentVbus);
		}

		// start
		double values[] = new double[4];// FL BL FR BR

		if (test != 1) {
			angle = Sensors.gyro.getAngle();

		}

		if ((lturn == 0 && rturn == 0 && speed == 0 && strafe == 0)
				&& !(angle > lastGyro + 1f || angle < lastGyro - 1f)) {
			if (angle > holdAngle + 8f) {
				Motors.fLeft.set(-1);
				Motors.bLeft.set(-1);
				Motors.fRight.set(-1);
				Motors.bRight.set(-1);
			} else if (angle < holdAngle - 8f) {
				Motors.fLeft.set(1);
				Motors.bLeft.set(1);
				Motors.fRight.set(1);
				Motors.fLeft.set(1);
			} else {
				Motors.fLeft.set(0);
				Motors.bLeft.set(0);
				Motors.fRight.set(0);
				Motors.fLeft.set(0);
			}
		} else {
			holdAngle = angle;
		}
		// end
		lastGyro = angle;
		angle -= .0001f * msecs;
		angle *= Math.PI;
		angle /= 180;
		double theta = Math.atan(speed / strafe);

		if (!Double.isNaN(theta)) {
			angle = theta - angle;
		}
		SmartDashboard.putString("Angle Calculated", String.valueOf(angle));
		if (strafe < 0)
			angle -= Math.PI;

		double magnitude = Math.sqrt(speed * speed + strafe * strafe);
		speed = Math.sin(angle) * magnitude;
		strafe = Math.cos(angle) * magnitude;

		values[0] = -(speed + rturn - lturn + strafe);// FL
		values[1] = -(speed + rturn - lturn - strafe);// BL
		values[2] = (speed + lturn - rturn - strafe);// FR
		values[3] = (speed + lturn - rturn + strafe);// BR

		System.out.println("fLeft Value: " + values[0] + "Control Mode: "
				+ Motors.fLeft.getControlMode());
		System.out.println("bLeft Value: " + values[1] + "Control Mode: "
				+ Motors.bLeft.getControlMode());
		System.out.println("fRight Value: " + values[2] + "Control Mode: "
				+ Motors.fRight.getControlMode());
		System.out.println("bRight Value: " + values[3] + "Control Mode: "
				+ Motors.bRight.getControlMode());

		for (int i = 0; i < 4; i++) {
			if (values[i] > 1)
				values[i] = 1;
			if (values[i] < -1)
				values[i] = -1;
		}

		Motors.fLeft.set(values[0]);
		Motors.bLeft.set(values[1]);
		Motors.fRight.set(values[2]);
		Motors.bRight.set(values[3]);
		++msecs;

	}

}