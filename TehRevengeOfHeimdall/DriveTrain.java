package org.usfirst.frc.team20.robot;

import edu.wpi.first.wpilibj.CANTalon;
import edu.wpi.first.wpilibj.Joystick.RumbleType;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class DriveTrain {
	
	static CANTalon fLeft;
	static CANTalon bLeft;
	static CANTalon fRight;
	static CANTalon bRight;
	public DriveTrain(int fl, int bl, int fr, int br){
		fLeft = new CANTalon(fl);
		bLeft = new CANTalon(bl);
		fRight = new CANTalon(fr);
		bRight = new CANTalon(br);
		
		
		
	}

	

	public void robotDrive(double sp, double rt, double lt, double st,double mag) {

		// Get Analog Inputs
		double speed  = sp;
		double rturn = rt;
		double lturn = lt;
		double strafe = st;
		double magnum = mag;
		// End get Analog Inputs

		// Assign Wheels

		double values[] = new double[4];// FL BL FR BR
		values[0] = magnum*(speed + rturn - lturn + strafe)/2;// FL
		values[1] = magnum*(speed + rturn - lturn - strafe)/2;// BL
		values[2] = -magnum*(speed + lturn - rturn - strafe)/2;// FR
		values[3] = -magnum*(speed + lturn - rturn + strafe)/2;// BR

		for (int i = 0; i < 4; i++) {
			if (values[i] > 1)
				values[i] = 1;
			if (values[i] < -1)
				values[i] = -1;
		}

//		Motors.fLeft.set(values[0]);
//		Motors.bLeft.set(values[1]);
//		Motors.fRight.set(values[2]);
//		Motors.bRight.set(values[3]);

		// End Assign Wheels

		// Driver Tray Control

		// End Driver Tray Control
	}

	private static double filter(double input) {
		if (input < .05f && input > -.05f)
			return 0;
		return input;
	}

	static long msecs = 0;
	static double holdAngle = 0;
	static double lastGyro = 0;

	public void fieldDrive(double sp, double rt, double lt, double st, double mag) {

		// Get Analog Signals

		double speed  = sp;
		double strafe = st;
		double lturn  = lt;
		double rturn  = rt;
		double magnum = mag;

		// End Get Analog Signals

		// Gyro Reset 0 degrees

		

		// End Gyro Reset

		// For da LOLZ
//		if (Motors.driver.getRawButton(11)) {
//			Motors.operator.setRumble(RumbleType.kRightRumble, 1);
//			Motors.operator.setRumble(RumbleType.kLeftRumble, 1);
//		}
//
//		if (Motors.driver.getRawButton(12)) {
//			Motors.operator.setRumble(RumbleType.kRightRumble, 0);
//			Motors.operator.setRumble(RumbleType.kLeftRumble, 0);
//		}
		// End
		double values[] = new double[4];// FL BL FR BR
		double angle = Sensors.gyro.getAngle();
		if ((lturn == 0 && rturn == 0 && speed == 0 && strafe == 0)
				&& !(angle > lastGyro + 1f || angle < lastGyro - 1f)) {
			if (angle > holdAngle + 8f) {
				fLeft.set(-1);
				bLeft.set(-1);
				fRight.set(-1);
				bRight.set(-1);
			} else if (angle < holdAngle - 8f) {
				fLeft.set(1);
				bLeft.set(1);
				fRight.set(1);
				fLeft.set(1);
			} else {
				fLeft.set(0);
				bLeft.set(0);
				fRight.set(0);
				fLeft.set(0);
			}
		} else {
			holdAngle = angle;
		}
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

		values[0] = magnum*(speed + rturn - lturn + strafe);// FL
		values[1] = magnum*(speed + rturn - lturn - strafe);// BL
		values[2] = -magnum*(speed + lturn - rturn - strafe);// FR
		values[3] = -magnum*(speed + lturn - rturn + strafe);// BR

		SmartDashboard.putString("Values", java.util.Arrays.toString(values));
		for (int i = 0; i < 4; i++) {
			if (values[i] > 1)
				values[i] = 1;
			if (values[i] < -1)
				values[i] = -1;
		}

		fLeft.set(values[0]);
		bLeft.set(values[1]);
		fRight.set(values[2]);
		bRight.set(values[3]);
		++msecs;
	}
	public void resetGyro(){	
			Sensors.gyro.reset();
			msecs = 0;
			holdAngle = 0;		
	}
	public void letsGetReadyToRumble(){
		
			Robot.operator.setRumble(RumbleType.kRightRumble, 1);
			Robot.operator.setRumble(RumbleType.kLeftRumble, 1);
		
		
	}
	public void noRumble(){
	

		
			Robot.operator.setRumble(RumbleType.kRightRumble, 0);
			Robot.operator.setRumble(RumbleType.kLeftRumble, 0);
		
	}
}
