package org.usfirst.frc.team20.robot;


public class T20Wait implements T20Command{
	private long time;
	private boolean finished;
	private boolean started;
	private long startTime;
	
	public T20Wait(long time) {
		this.time = time;
		this.finished = false;
		this.started = false;
	}
	
	@Override public void execute() {
		if(finished){
			return;
		}
		if(!started){
			this.started = true;
			System.out.println("<Wait time=" + this.time + ">");
			this.startTime = Robot.getTime();
		}
		if(Robot.getTime() - this.startTime > this.time){
			this.finished = true;
			System.out.println("</Wait>");
		}
	}

	@Override public boolean isFinished() {
		return this.finished;
	}

	@Override public T20Command copy() {
		return new T20Wait(this.time);
	}
}
