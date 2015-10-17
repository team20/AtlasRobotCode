package org.usfirst.frc.team20.robot;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class CopyofAutoStack {

	T20CANServoEncForks fork;
	T20CANServoEnc elevator;
	private int state;
	private boolean interrupted;
	private double elevatorSetPoint;
	private double forkSetPoint;
	private double[] elevatorPositions;
	private Trapezoidal trap;
	private long startTime;
	private int lastState;
	private double currentSetpoint;
	private int parity;
	private double x;
	private double y;

	private static final double FORK_NARROW_TOTE = 15.5, FORK_OPEN = 25;

	private static final int STATE_STAGE = 0, STATE_GRAB = 1, STATE_LIFT = 2;

	public CopyofAutoStack(T20CANServoEnc elevator, T20CANServoEncForks fork) {
		this.elevator = elevator;
		this.fork = fork;
		this.interrupted = true;
		this.lastState = -1;
		this.startTime = 0;
		this.trap = Trapezoidal.create()
				.withAcceleration(5)
				.withDeceleration(5)
				.withMaxSetPointDistance(30);
	}

	public void setElevatorPositions(double... elevatorPositions) {
		this.elevatorPositions = elevatorPositions;
	}

	public double getForkSetpoint() {
		switch (state) {
		case STATE_STAGE:
		case STATE_GRAB:
			return FORK_NARROW_TOTE;
		case STATE_LIFT:
			return FORK_NARROW_TOTE;
		default:
			return fork.getXEU();
		}
	}

	public double getElevatorSetpoint() {
		return this.getElevatorSetpoint(this.state);
	}
	
	public double getElevatorSetpoint(int state) {
		switch (state) {
		case STATE_STAGE:
			return elevatorPositions[0];
		case STATE_GRAB:
			return elevatorPositions[0];
		case STATE_LIFT:
			return elevatorPositions[1];
		default:
			return elevator.getSetpoint();
		}
	}
	
	public double getCascadingElevatorSetpoint(){
		if(this.lastState == -1){
			y = this.getElevatorSetpoint();
			this.lastState = 1;
		}
		x = elevator.getSetpoint();
		//return (y = y * .9 + x * .1);
		return x;
	}

	public void interrupt() {
		this.interrupted = true;
	}

	public boolean isInterrupted() {
		return this.interrupted;
	}

	public void start() {
		this.interrupted = false;
		this.state = STATE_STAGE;
	}

	public void calculate() {
		if (interrupted)
			return;
		double elevatorSetPoint = this.getElevatorSetpoint();
		double forkSetPoint = this.getForkSetpoint();
		double elevatorPosition = this.elevator.getXEU();
		double forkPosition = this.fork.getXEU();

		SmartDashboard.putString("Calculated Elevator SP",
				String.valueOf(elevatorSetPoint));
		SmartDashboard.putString("Calculated Fork SP",
				String.valueOf(forkSetPoint));
		SmartDashboard.putString("Calculated Elevator Position",
				String.valueOf(elevatorPosition));
		SmartDashboard.putString("Calculated Fork Position",
				String.valueOf(forkPosition));

		if (Math.abs(forkSetPoint - forkPosition) > 1
				|| Math.abs(elevatorSetPoint - elevatorPosition) > 2)
			return;

		switch (state) {
		case STATE_STAGE:
			state = STATE_GRAB;
		case STATE_GRAB:
			state = STATE_LIFT;
			break;
		case STATE_LIFT:
			this.interrupted = true;
		}
	}
}
