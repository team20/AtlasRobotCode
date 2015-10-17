package org.usfirst.frc.team20.robot;

public class T20ElevatorToPosition implements T20Command{

	private double position;
	private boolean finished;
	private boolean started;
	
	public T20ElevatorToPosition(double position){
		this.position = position;
		this.started = false;
		this.finished = false;
	}
	
	@Override public void execute() {
		if(finished)
			return;
		if(!started){
			started = !started;
			System.out.println("<Elevator To Position position=" + this.position + ">");
		}
		Motors.elevatorMaster.setXEU(position);
		if(Math.abs(Motors.elevatorMaster.getXEU() - position) < 2){
			this.finished = true;
			System.out.println("</Elevator To Position>");
		}
	}

	@Override public boolean isFinished() {
		return this.finished;
	}

	@Override public T20Command copy() {
		return new T20ElevatorToPosition(this.position);
	}
}
