package org.usfirst.frc.team20.robot;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;


public class DriverControls {
  static int test = 0;
  static double angle = 0;
	
  public static void robotDrive() {
	

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
		
		if (Motors.driver.getRawButton(1)) {
			Motors.trayMotor.set(1);
		}
		if (Motors.driver.getRawButton(2)) {
			Motors.trayMotor.set(-1);
		}
		if (Motors.driver.getRawButton(3)) {
			Motors.trayMotor.set(0);
		}
		if (Motors.trayMotor.getOutputCurrent() > 20) {
			Motors.trayMotor.set(0);

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
			Motors.trayMotor.set(1);
		}
		if (Motors.driver.getRawButton(2)) {
			Motors.trayMotor.set(-1);
		}
		if (Motors.driver.getRawButton(3)
				|| Motors.trayMotor.getOutputCurrent() > 20) {
			Motors.trayMotor.set(0);
		}
		double ratio = -filter(Motors.driver.getRawAxis(5));
		double speed = ratio * filter(Motors.driver.getRawAxis(1));
		double strafe = ratio * filter(Motors.driver.getRawAxis(0));
		double lturn = filter(Motors.driver.getRawAxis(3));
		double rturn = filter(Motors.driver.getRawAxis(2));
		
		test++;

		if (Motors.driver.getRawButton(5)) {
			Sensors.gyro.reset();
			msecs = 0;
			holdAngle = 0;
		}
		//start
		double values[] = new double[4];// FL BL FR BR
		
		if (test != 1)
		{
			//angle = Sensors.gyro.getAngle();
			
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
//end
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

		SmartDashboard.putString("Values", java.util.Arrays.toString(values));
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