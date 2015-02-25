package org.usfirst.frc.team20.robot;

import edu.wpi.first.wpilibj.Joystick;

public class DriveControls {
	boolean fieldDrive;
	DriveTrain drivetrain;
	Joystick joy;

	public DriveControls(int port) {
		drivetrain = new DriveTrain(1, 10, 2, 9);
		joy = new Joystick(0);
	}

	public void control() {
		double sp = joy.getRawAxis(1);
		double rt = joy.getRawAxis(3);
		double lt = joy.getRawAxis(2);
		double st = joy.getRawAxis(0);
		double mag = joy.getRawAxis(5);
		if(joy.getRawButton(6)){
			fieldDrive = !fieldDrive;
		}
		if(joy.getRawButton(5)){
			drivetrain.resetGyro();
		}
		if (fieldDrive) {
			drivetrain.fieldDrive(sp, rt, lt, st, mag);
		}else{
			drivetrain.robotDrive(sp, rt, lt, st,mag);
		}
		if(joy.getRawButton(1)){
			Robot.tray.trayRetract();
		}
		if(joy.getRawButton(2)){
			Robot.tray.trayExtend();
		}
		
	}
	
	

}
