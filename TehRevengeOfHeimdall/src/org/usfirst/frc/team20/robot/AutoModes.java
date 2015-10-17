	package org.usfirst.frc.team20.robot;

import edu.wpi.first.wpilibj.Timer;

public class AutoModes {

	final double ONE_RC_AUTO_STOP_MOVE = 2.5;
	final double JUST_MOVE_STOP = 2.4;

	private static double toteStackPos[] = { 0, 12.6, 24.7, 45.7, 48.9, 60 };

	private static CopyofAutoStack autoStack = new CopyofAutoStack(
			Motors.elevatorMaster, Motors.forksMotor);

	public void oneRCAuto() {
		double forksSet = 17.25;
		Motors.forksMotor.setXEU(forksSet);

		if (Math.abs(Motors.forksMotor.getXEU() - forksSet) < 5 || Motors.forksMotor.currentLimited
				&& Motors.elevatorMaster.homed) {

			Motors.elevatorMaster.setXEU(Motors.elevatorMaster.getXEU()+3);
			if (Robot.hTime.get() < ONE_RC_AUTO_STOP_MOVE) {
				Motors.bLeft.set(.5);
				Motors.fLeft.set(.5);
				Motors.bRight.set(-.6);
				Motors.fRight.set(-.6);
				Motors.elevatorMaster.setXEU(Motors.elevatorMaster.getXEU()+1);
			}
			if (Robot.hTime.get() >= ONE_RC_AUTO_STOP_MOVE) {
				Robot.hTime.stop();
				Motors.bLeft.set(0);
				Motors.fLeft.set(0);
				Motors.bRight.set(0);
				Motors.fRight.set(0);
			}
		}
		System.out.println(Robot.hTime.get());
		if (Robot.hTime.get() > ONE_RC_AUTO_STOP_MOVE) {
			Robot.hTime.stop();
			Motors.bLeft.set(0);
			Motors.fLeft.set(0);
			Motors.bRight.set(0);
			Motors.fRight.set(0);
		}
	}

	public void RCLift() {

	}

	public void toteStack() {

		if (Robot.hTime.get() < 1.5) {
			autoStack.setElevatorPositions(toteStackPos);
			autoStack.start();
		}
		if (Robot.hTime.get() > 1.5 && Robot.hTime.get() < 2.2) {
			Motors.bLeft.set(.5);
			Motors.fLeft.set(.5);
			Motors.bRight.set(-.5);
			Motors.fRight.set(-.5);
		}
	}

	public void stackStarter() {
		if (Robot.hTime.get() < 1) {
			Motors.elevatorMaster.homed = false;
			Motors.forksMotor.homed = false;
		}
		if (Robot.hTime.get() > 1.1) {
			OperatorControls.level = 3;
			Motors.elevatorMaster.setXEU(OperatorControls.elevatorTrayPos[3]);

		}
		if (Motors.elevatorMaster.getXEU() > 31) {
			Motors.forksMotor.setXEU(31);
			Motors.trayMotor.set(1);
		}
		if (Robot.hTime.get() > 6) {
			Motors.trayMotor.set(0);
		}

	}
	
	public void stepCanGrab(){
		if(Robot.hTime.get() < 1.1){
			Motors.bLeft.set(.3);
			Motors.fLeft.set(.3);
			Motors.bRight.set(-.3);
			Motors.fRight.set(-.3);
		}
		if(Robot.hTime.get() > 1.0 && Robot.hTime.get() < 5.1){
			Motors.bLeft.set(0);
			Motors.fLeft.set(0);
			Motors.bRight.set(0);
			Motors.fRight.set(0);
			Motors.canGrabberMotor.set(-.5);//Need To Update
		}
		if(Robot.hTime.get() > 5.0  && Robot.hTime.get()<6.1){
			Motors.bLeft.set(-.3);
			Motors.fLeft.set(-.3);
			Motors.bRight.set(0);
			Motors.fRight.set(0);
			Motors.canGrabberMotor.set(0);
		}
		if(Robot.hTime.get() > 6.0  && Robot.hTime.get()<7.1){
			Motors.bLeft.set(.3);
			Motors.fLeft.set(.3);
			Motors.bRight.set(0);
			Motors.fRight.set(0);
			Motors.canGrabberMotor.set(0);
		}
		if(Robot.hTime.get() > 7.0  && Robot.hTime.get()<8.1){
			Motors.bLeft.set(0);
			Motors.fLeft.set(0);
			Motors.bRight.set(.3);
			Motors.fRight.set(.3);
			Motors.canGrabberMotor.set(0);
		}
		if(Robot.hTime.get() > 8.0  && Robot.hTime.get()<9.1){
			Motors.bLeft.set(0);
			Motors.fLeft.set(0);
			Motors.bRight.set(-.3);
			Motors.fRight.set(-.3);
			Motors.canGrabberMotor.set(0);
		}
		if(Robot.hTime.get() > 9.0  && Robot.hTime.get()<10.1){
			Motors.bLeft.set(0);
			Motors.fLeft.set(0);
			Motors.bRight.set(0);
			Motors.fRight.set(0);
			Motors.canGrabberMotor.set(-.5);
		}
		if(Robot.hTime.get() > 10.0  && Robot.hTime.get()<11.1){
			Motors.bLeft.set(-.3);
			Motors.fLeft.set(-.3);
			Motors.bRight.set(.3);
			Motors.fRight.set(.3);
			Motors.canGrabberMotor.set(0);
		}
		if(Robot.hTime.get() > 11.0  && Robot.hTime.get()<13.1){
			Motors.bLeft.set(0);
			Motors.fLeft.set(0);
			Motors.bRight.set(0);
			Motors.fRight.set(0);
			Motors.canGrabberMotor.set(0);
		}
	}
}
