package org.usfirst.frc.team20.robot;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class T20AutoStack {

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

	private static final double FORK_WIDE_TOTE = 15.5, FORK_OPEN = 25;

	private static final int STATE_STAGE = 0, STATE_PLACE = 1,
			STATE_RELEASE = 2, STATE_LOWER = 3, STATE_GRAB = 4, STATE_LIFT = 5;

	public T20AutoStack(T20CANServoEnc elevator, T20CANServoEncForks fork) {
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
		case STATE_PLACE:
			return fork.getXEU();
		case STATE_RELEASE:
			return FORK_OPEN;
		case STATE_LOWER:
			return FORK_OPEN;
		case STATE_GRAB:
			return FORK_WIDE_TOTE;
		case STATE_LIFT:
			return FORK_WIDE_TOTE;
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
			return elevatorPositions[1]+4;
		case STATE_PLACE:
			return elevatorPositions[1];
		case STATE_RELEASE:
			return elevatorPositions[1];
		case STATE_LOWER:
			return elevatorPositions[0];
		case STATE_GRAB:
			return elevatorPositions[0];
		case STATE_LIFT:
			return elevatorPositions[3];
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
			state = STATE_PLACE;
		case STATE_PLACE:
			state = STATE_RELEASE;
			break;
		case STATE_RELEASE:
			state = STATE_LOWER;
			break;
		case STATE_LOWER:
			state = STATE_GRAB;
			break;
		case STATE_GRAB:
			state = STATE_LIFT;
			break;
		case STATE_LIFT:
			this.interrupted = true;
		}
	}
}
