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
	
	private static final double FORK_WIDE_TOTE = 25,
		FORK_OPEN = 31;
	
	private static final int STATE_PLACE = 0, STATE_RELEASE = 1, STATE_LOWER = 2, STATE_GRAB = 3, STATE_LIFT = 4;
	
	public T20AutoStack(T20CANServoEnc elevator, T20CANServoEncForks fork) {
		this.elevator = elevator;
		this.fork = fork;
		this.interrupted = true;
	}
	
	public void setElevatorPositions(double... elevatorPositions){
		this.elevatorPositions = elevatorPositions;
	}
	
	public double getForkSetpoint(){
		switch(state){
		case STATE_PLACE:
			return FORK_WIDE_TOTE;
		case STATE_RELEASE:
			return FORK_OPEN;
		case STATE_LOWER:
			return FORK_OPEN;
		case STATE_GRAB:
			return FORK_WIDE_TOTE;
		case STATE_LIFT:
			return FORK_WIDE_TOTE;
		default:
			return fork.getSetpoint();
		}
	}
	
	public double getElevatorSetpoint(){
		switch(state){
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
	
	public void interrupt(){
		this.interrupted = true;
	}
	
	public boolean isInterrupted(){
		return this.interrupted;
	}
	
	public void start(){
		this.interrupted = false;
		this.state = STATE_PLACE;
	}
	
	public void calculate(){
		if(interrupted)
			return;
		double elevatorSetPoint = this.getElevatorSetpoint();
		double forkSetPoint = this.getForkSetpoint();
		double elevatorPosition = -this.elevator.getXEU();
		double forkPosition = this.fork.getXEU();
	
		SmartDashboard.putString("Calculated Elevator SP", String.valueOf(elevatorSetPoint));
		SmartDashboard.putString("Calculated Fork SP", String.valueOf(forkSetPoint));
		SmartDashboard.putString("Calculated Elevator Position", String.valueOf(elevatorPosition));
		SmartDashboard.putString("Calculated Fork Position", String.valueOf(forkPosition));
		
		if(Math.abs(forkSetPoint - forkPosition) > 1 || Math.abs(elevatorSetPoint - elevatorPosition) > 2)
			return;
		
		switch(state){
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
