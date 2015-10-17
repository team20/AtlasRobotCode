package org.usfirst.frc.team20.robot;

import edu.wpi.first.wpilibj.CANTalon.ControlMode;

public class T20ExtendRod implements T20Command {

	private double speed;
	private long time;
	private boolean finished;
	private boolean started;
	private long startTime;
	
	public T20ExtendRod(double speed, long time){
		this.speed = speed;
		this.time = time;
		this.finished = false;
		this.started = false;
	}
	
	@Override public void execute() {
		if(finished){
			return;
		}
		if(!started){
			System.out.println("<Extend Rod time=" + this.time + ">");
			this.started = true;
			this.startTime = Robot.getTime();
			
		}
		Motors.stabilizerMotor.set(-this.speed);//inverted
		if(Robot.getTime() - this.startTime > this.time){
			this.finished = true;
			System.out.println("</Extend Rod>");
		}
	}

	@Override
	public boolean isFinished() {
		return this.finished;
	}

	@Override
	public T20Command copy() {
		return new T20ExtendRod(this.speed, this.time);
	}

}
