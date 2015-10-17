package org.usfirst.frc.team20.robot;

public class T20ForkToWidth implements T20Command{

	private double width;
	private boolean finished;
	private boolean started;
	
	public T20ForkToWidth(double width) {
		this.width = width;
		this.finished = false;
		this.started = false;
	}
	
	@Override public void execute() {
		if(finished)
			return;
		if(!started){
			System.out.println("<Fork To Width width=" + this.width + ">");
			started = !started;
		}
		Motors.forksMotor.setXEU(width);
		if(Math.abs(Motors.forksMotor.getXEU() - width) < 2){
			this.finished = true;
			System.out.println("</Fork To Width>");
		}
	}

	@Override public boolean isFinished() {
		return this.finished;
	}

	@Override public T20Command copy() {
		return new T20ForkToWidth(this.width);
	}
}
