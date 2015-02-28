package org.usfirst.frc.team20.robot;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;

import edu.wpi.first.wpilibj.CANTalon;
import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.CANTalon.ControlMode;
import edu.wpi.first.wpilibj.CANTalon.FeedbackDevice;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.DriverStation;

/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the IterativeRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the manifest file in the resource
 * directory.
 */
public class Robot extends IterativeRobot {
	File file;
	FileWriter fr;
	BufferedWriter wr;
	

	public static double lastCycleEncoderPosition;

	/**
	 * This function is run when the robot is first started up and should be
	 * used for any initialization code.
	 */
	public void robotInit() {
		
		Motors Motor = new Motors();
		Motors.initi();
		lastCycleEncoderPosition = Motors.elevatorMaster.getPosition();

		Motors.trayMotor.changeControlMode(ControlMode.PercentVbus);

		// PID
	
		Motors.forksMotor.setFeedbackDevice(FeedbackDevice.QuadEncoder);
		Motors.forksMotor.changeControlMode(CANTalon.ControlMode.Position);
		Motors.forksMotor.setPosition(0);
		Motors.forksMotor.setPID(OperatorControls.p, OperatorControls.i,
				OperatorControls.d);
		Motors.forksMotor.setCloseLoopRampRate(OperatorControls.ramp);
		Motors.forksMotor.enableControl();
		
		Sensors.elevatorShort.requestInterrupts();
		Sensors.elevatorShort.setUpSourceEdge(true, false);
	}

	/**
	 * This function is called periodically during autonomous
	 */
	public int counter = 0;

	public void autonomousPeriodic() {

		if (counter == 0) {
			double talCur = Motors.forksMotor.getOutputCurrent();
			Motors.forksMotor.set(-85000);
			OperatorControls.talFil = OperatorControls.talFil * .9 + talCur
					* .1;
			if (OperatorControls.talFil > 15) {
				Motors.forksMotor.setPosition(0);
				Motors.forksMotor.set(0);
				counter = 1;
			}
		}
	}

	//@Override
//	public void teleopInit() {
//		lastCycleEncoderPosition = Motors.elevatorMaster.getPosition();
//		teleopPeriodic1();
		
//	}

	/**
	 * This function is called periodically during operator control
	 */
	public static boolean fieldCentric = true;
	public static int test = 0;
	public void teleopPeriodic() {
		
		SmartDashboard.putString("Elevator Master22 =", ""
				+ test++);
		if (fieldCentric) {
			DriverControls.fieldDrive();
			
		}else{
			DriverControls.robotDrive();
			
		}
		if (Motors.driver.getRawButton(6)) {
			fieldCentric = !fieldCentric;
			
		}
		
		SmartDashboard.putString("Teh Toggle of TEH field CentriC =", ""+fieldCentric);
		
		OperatorControls.opControls();
		
		counter = 0;
		Timer.delay(.001);
	}

	/**
	 * This function is called periodically during test mode
	 */
	public void testPeriodic() {
		if (Motors.operator.getRawButton(5)) {
			SmartDashboard.putString("Elevator Position ==" , "set 500");
			Motors.elevatorMaster.setPosition(500);
		}
		
		if (Motors.operator.getRawButton(7)) {
			 Motors.elevatorMaster.setPosition(1000);
			 SmartDashboard.putString("Elevator Position ==" , "set 500");
		}
		
		/*
		SmartDashboard.putString("Elevator Master22 =", ""
				+ test++);
		if (fieldCentric) {
			DriverControls.fieldDrive();
			
		}else{
			DriverControls.robotDrive();
			
		}
		if (Motors.driver.getRawButton(6)) {
			fieldCentric = !fieldCentric;
			
		}
		
		SmartDashboard.putString("Teh Toggle of TEH field CentriC =", ""+fieldCentric);
		
		OperatorControls.opControls();
			
		counter = 0;

		SmartDashboard.putString("Elevator Master =", ""
				+ Motors.elevatorMaster.getOutputCurrent());
		SmartDashboard.putString("Elevator slave one =", ""
				+ Motors.elevatorSlaveOne.getOutputCurrent());
		SmartDashboard.putString("Elevator slave two =", ""
				+ Motors.elevatorSlaveTwo.getOutputCurrent());
		SmartDashboard.putString("Elevator slave three =", ""
				+ Motors.elevatorSlaveThree.getOutputCurrent());
jjjh
		SmartDashboard.putString("Elevator Master ==", ""
				+ Motors.elevatorMaster.getOutputVoltage());
		SmartDashboard.putString("Elevator slave one ==", ""
				+ Motors.elevatorSlaveOne.getOutputVoltage());
		SmartDashboard.putString("Elevator slave two ==", ""
				+ Motors.elevatorSlaveTwo.getOutputVoltage());
		SmartDashboard.putString("Elevator slave three ==", ""
				+ Motors.elevatorSlaveThree.getOutputVoltage());
		*/

		Timer.delay(.001);
	}

}
