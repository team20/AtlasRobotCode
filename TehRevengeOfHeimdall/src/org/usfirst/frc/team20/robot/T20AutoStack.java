package org.usfirst.frc.team20.robot;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class T20AutoStraighten {

	T20CANServoEncForks fork;
	T20CANServoEnc elevator;
	private int state;
	private boolean interrupted;
	private double elevatorSetPoint;
	private double forkSetPoint;
	private double[] elevatorPositions;
	
	private static final double FORK_WIDE_TOTE = 25,
		FORK_OPEN = 31;
	
	private static final int  
	 STATE_POS0 = 0, STATE_STR0 = 1, STATE_REL0 = 2, 
	 STATE_POS1 = 3, STATE_STR1 = 4, STATE_REL1 = 5,
	 STATE_POS2 = 6, STATE_STR2 = 7, STATE_REL2 = 8,
	 STATE_POS3 = 9, STATE_STR3 = 10, STATE_REL3 = 11,
	 STATE_POS4 = 12, STATE_STR4 = 13;
	
	public T20AutoStraighten(T20CANServoEnc elevator, T20CANServoEncForks fork) {
		this.elevator = elevator;
		this.fork = fork;
		this.interrupted = true;
	}
	
	public void setElevatorPositions(double... elevatorPositions){
		this.elevatorPositions = elevatorPositions;
	}
	
	public double getForkSetpoint(){
		switch(state){
		case STATE_POS0:
			return fork.getXEU();
		case STATE_STR0:
			return FORK_WIDE_TOTE;
		case STATE_REL0:
			return FORK_OPEN;
		case STATE_POS1:
			return FORK_OPEN;
		case STATE_STR1:
			return FORK_WIDE_TOTE;
		case STATE_REL1:
			return FORK_OPEN;
		case STATE_POS2:
			return FORK_OPEN;
		case STATE_STR2:
			return FORK_WIDE_TOTE;
		case STATE_REL2:
			return FORK_OPEN;
		case STATE_POS3:
			return FORK_OPEN;
		case STATE_STR3:
			return FORK_WIDE_TOTE;
		case STATE_REL3:
			return FORK_OPEN;
		case STATE_POS4:
			return FORK_OPEN;
		case STATE_STR4:
			return FORK_WIDE_TOTE;
		default:
			return fork.getXEU();
		}
	}
	
	public double getElevatorSetpoint(){
		switch(state){
		case STATE_POS0:
			return elevatorPositions[0];
		case STATE_STR0:
			return elevatorPositions[0];
		case STATE_REL0:
			return elevatorPositions[0];
		case STATE_POS1:
			return elevatorPositions[1];
		case STATE_STR1:
			return elevatorPositions[1];
		case STATE_REL1:
			return elevatorPositions[1];
		case STATE_POS2:
			return elevatorPositions[2];
		case STATE_STR2:
			return elevatorPositions[2];
		case STATE_REL2:
			return elevatorPositions[2];
		case STATE_POS3:
			return elevatorPositions[3];
		case STATE_STR3:
			return elevatorPositions[3];
		case STATE_REL3:
			return elevatorPositions[3];
		case STATE_POS4:
			return elevatorPositions[4];
		case STATE_STR4:
			return elevatorPositions[4];
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
		this.state = STATE_POS0;
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
		
		if(Math.abs(forkSetPoint - forkPosition) > 2 || Math.abs(elevatorSetPoint - elevatorPosition) > 2)
			return;
		
		switch(state){
		case STATE_POS0:
			state = STATE_STR0;
			break;
		case STATE_STR0:
			state = STATE_REL0;
			break;
		case STATE_REL0:
			state = STATE_POS1;
			break;
		case STATE_POS1:
			state = STATE_STR1;
			break;
		case STATE_STR1:
			state = STATE_REL1;
			break;
		case STATE_REL1:
			state = STATE_POS2;
			break;
		case STATE_POS2:
			state = STATE_STR2;
			break;
		case STATE_STR2:
			state = STATE_REL2;
			break;
		case STATE_REL2:
			state = STATE_POS3;
			break;
		case STATE_POS3:
			state = STATE_STR3;
			break;
		case STATE_STR3:
			state = STATE_REL3;
			break;
		case STATE_REL3:
			state = STATE_POS4;
			break;
		case STATE_POS4:
			state = STATE_STR4;
			break;
		case STATE_STR4:
			this.interrupted = true;
		}
	}
}
