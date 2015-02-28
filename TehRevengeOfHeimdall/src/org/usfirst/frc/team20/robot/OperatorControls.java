package org.usfirst.frc.team20.robot;

import edu.wpi.first.wpilibj.Joystick;

public class OperatorControls {

	public final int ELEVATOR_MASTER_PORT = 7, FORKS_PORT = 5,
			LEFT_ROLLER_PORT = 2, RIGHT_ROLLER_PORT = 1,
			ELEVATOR_SLAVE_ONE = 4, ELEVATOR_SLAVE_TWO = 3,
			ELEVATOR_SLAVE_THREE = 8;

	Rollers rollers = new Rollers(LEFT_ROLLER_PORT, RIGHT_ROLLER_PORT);

	Forks forks = new Forks(FORKS_PORT);
	
	StackPlace stacker = new StackPlace();

	Elevator elevator = new Elevator(ELEVATOR_MASTER_PORT, ELEVATOR_SLAVE_ONE,
			ELEVATOR_SLAVE_TWO, ELEVATOR_SLAVE_THREE);

	Joystick Operator = new Joystick(1);

	public T20CANServoEnc elevatorMaster;
	private final double ELEVATOR_P = 0.8, ELEVATOR_I = 0.0001,
			ELEVATOR_D = 0.05;

	// For Claw Positions
	public double p = .5;
	public double i = .0001;
	public double d = .5;
	public double ramp = 1500;
	public double talFil = 0;

	public int POVal = 0;

	public boolean trayBool = false;

	public void opControls() {
		elevator.checkElevator();
		forks.checkForks();

		elevatorMaster = new T20CANServoEnc(ELEVATOR_MASTER_PORT, new int[] {
				ELEVATOR_SLAVE_ONE, ELEVATOR_SLAVE_TWO, ELEVATOR_SLAVE_THREE },
				ELEVATOR_P, ELEVATOR_I, ELEVATOR_D, 0, -28000, 0);
		elevatorMaster.setXDScale(0, 60, "inches");
		elevatorMaster.reverseSensor(true);

		double elevatorEnc = Elevator.master.getEncPosition();
		double analogElevator = Operator.getRawAxis(3);
		double analogFork = -Operator.getRawAxis(0);
		double forkState = Operator.getPOV();

		// Interrupt!
		if (Operator.getRawButton(9)) {
			rollers.stopRoll();
			forks.set(0);
			elevator.set(0);
		}
		
		// End Interrupt!

		// Forks Code
		forks.set(analogFork);
		// End Forks Code

		// Rollers Code
		if (Operator.getRawButton(2)) {
			rollers.rollIn();
		}

		if (Operator.getRawButton(1)) {
			rollers.stopRoll();
		}

		if (Operator.getRawButton(3)) {
			rollers.DOABARRELROLL();
		}

		if (Operator.getRawButton(4)) {
			rollers.rollout();
		}
		// End Rollers Code

		// Elevator Code
		if (Operator.getRawButton(5)) {
			elevatorMaster.setPosition(500);
		}
		if (Operator.getRawButton(7)) {
			elevatorMaster.setPosition(900);
		}
		if (Operator.getRawButton(12)) {
			stacker.PlaceStack();
		}
		// Elevator Code
		
		// Tray Code
	
		// End Tray Code

		// KnoxKode for PID Forks
		// if (Operator.getPOV() == 270) {
		// forks.setPosition(25000);
		// POVal = 270;
		// }
		// if (Operator.getPOV() == 0) {
		// forks.setPosition(59000);
		// POVal = 180;
		// }
		// if (Operator.getPOV() == 90) {
		// forks.setPosition(16000);
		// POVal = 0;
		// }
		// if (Operator.getPOV() == 180) {
		// forks.setPosition(75000);
		// POVal = 90;
		// }
		// if (Operator.getPOV() == 135) {
		// forks.setPosition(200);
		// }
		// double talCur = forks.forks.getOutputCurrent();
		// talFil = talFil * .9 + talCur * .1;
		// SmartDashboard.putString("Current fork = ", "" + talFil);
		// SmartDashboard.putString("Fork enc = ",
		// "" + forks.forks.getEncPosition());
		// if (talFil > 15) {
		// Motors.forksMotor.set(Motors.forksMotor.getPosition());
		// }
		// SmartDashboard.putString("Fork sp = ",
		// "" + forks.forks.getSetpoint());
		// if (Operator.getRawButton(12)) {
		// Motors.forksMotor.setPosition(0);
		// Motors.elevatorMaster.set(Motors.elevatorMaster.getEncPosition());
		// }
		// if (talFil > 15) {
		// Motors.forksMotor.set(Motors.forksMotor.getPosition());
		// }
		// End KnoxKode

	}
}
