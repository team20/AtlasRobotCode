package org.usfirst.frc.team20.robot;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class T20AutoPlace {

	T20CANServoEncForks fork;
	T20CANServoEnc elevator;
	private int state;
	private boolean interrupted;
	private double[] elevatorPositions;
	private Trapezoidal trapezoidal;
	private long startTime;
	private int lastState;
	
	private static final double FORK_WIDE_TOTE = 25.375,
		FORK_OPEN = 31;
	
	public static final int TRAY_EXTENDED = 1, TRAY_RETRACTED = 0, TRAY_POS_UNDEF = -1;
	private static final boolean TRAY_EXTEND = true, TRAY_RETRACT = false;
	
	private static final int 
	    STATE_REL1 = 0, STATE_LOWER = 1, STATE_GRAB = 2, STATE_LIFT = 3, STATE_TRAY_RET = 4, STATE_PLACE = 5, STATE_REL2 = 6;
	
	public T20AutoPlace(T20CANServoEnc elevator, T20CANServoEncForks fork) {
		this.elevator = elevator;
		this.fork = fork;
		this.interrupted = true;
		this.startTime = 0;
		this.lastState = -1;
		trapezoidal = Trapezoidal.create()
			.withAcceleration(50)
			.withDeceleration(50)
			.withMaxSetPointDistance(10);
	}
	
	public void setElevatorPositions(double... elevatorPositions){
		this.elevatorPositions = elevatorPositions;
	}
	
	public double getForkSetpoint(){
		switch(state){
		case STATE_REL1:
			return FORK_OPEN;
		case STATE_LOWER:
			return FORK_OPEN;
		case STATE_GRAB:
			return FORK_WIDE_TOTE;
		case STATE_LIFT:
			return FORK_WIDE_TOTE;
		case STATE_TRAY_RET:
			return FORK_WIDE_TOTE;
		case STATE_PLACE:
			return FORK_WIDE_TOTE;
		case STATE_REL2:
			return FORK_OPEN;
		default:
			return fork.getXEU();
		}
	}
	
	public double getElevatorSetpoint(){
		double x;
		
		switch(state){
		case STATE_REL1:
			x = this.elevator.getXEU();
			break;
		case STATE_LOWER:
			x = elevatorPositions[1];
			break;
		case STATE_GRAB:
			x = elevatorPositions[1];
			break;
		case STATE_LIFT:
			x = elevatorPositions[3];
			break;
		case STATE_TRAY_RET:
			x = elevatorPositions[3];
			break;
		case STATE_PLACE:
			x = elevatorPositions[2];
			break;
		case STATE_REL2:
			x = elevatorPositions[0];
			break;
		default:
			x = this.elevator.getXEU();
		}
		if(state != lastState){
			startTime = Robot.getTime();
			this.trapezoidal = trapezoidal.withDistance(x);
		}
		lastState = state;
		return this.trapezoidal.getPositionByTime(Robot.getTime() - this.startTime);
	}
	
	public boolean getTraySetPoint(){
		switch(state){
		case STATE_REL1:
			return TRAY_EXTEND;
		case STATE_LOWER:
			return TRAY_EXTEND;
		case STATE_GRAB:
			return TRAY_EXTEND;
		case STATE_LIFT:
			return TRAY_EXTEND;
		case STATE_TRAY_RET:
			return TRAY_RETRACT;
		case STATE_PLACE:
			return TRAY_RETRACT;
		case STATE_REL2:
			return TRAY_RETRACT;
		default:
			return TRAY_RETRACT;
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
		this.state = STATE_REL1;
	}
	
	public int getTrayPosition(){
//		if (!Sensors.trayExtended.get())
//			return TRAY_EXTENDED;
//		if (!Sensors.trayRetracted.get())
//			return TRAY_RETRACTED;
//		return TRAY_POS_UNDEF;
		return DriverControls.trayPosPV;
	}
	
	public void calculate(){
		if(interrupted)
			return;
		
		double elevatorSetPoint = this.trapezoidal.getDistance();
		double forkSetPoint = this.getForkSetpoint();
		double elevatorPosition = this.elevator.getXEU();
		double forkPosition = this.fork.getXEU();
		boolean traySetPoint = this.getTraySetPoint();
	    int trayPosition = this.getTrayPosition();
	    
		SmartDashboard.putString("Calculated Elevator SP", String.valueOf(elevatorSetPoint));
		SmartDashboard.putString("Calculated Fork SP", String.valueOf(forkSetPoint));
		SmartDashboard.putString("Calculated Elevator Position", String.valueOf(elevatorPosition));
		SmartDashboard.putString("Calculated Fork Position", String.valueOf(forkPosition));
		
		if (trayPosition == this.TRAY_POS_UNDEF) return;
		if (trayPosition == this.TRAY_EXTENDED && traySetPoint != this.TRAY_EXTEND) return;
		if (trayPosition == this.TRAY_RETRACTED && traySetPoint != this.TRAY_RETRACT) return;
		
		if(	Math.abs(forkSetPoint - forkPosition) > 1 || Math.abs(elevatorSetPoint - elevatorPosition) > 2)
			return;
		
		// next state
		switch(state){
		case STATE_REL1:
			state = STATE_LOWER;
			break;
		case STATE_LOWER:
			state = STATE_GRAB;
			break;
		case STATE_GRAB:
			state = STATE_LIFT;
			break;
		case STATE_LIFT:
			state = STATE_TRAY_RET;
			break;
		case STATE_TRAY_RET:
			state = STATE_PLACE;
			break;
		case STATE_PLACE:
			state = STATE_REL2;
			break;
		case STATE_REL2:
			this.interrupted = true;
		}
	}
}
