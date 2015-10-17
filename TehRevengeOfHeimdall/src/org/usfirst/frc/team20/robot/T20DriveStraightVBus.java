package org.usfirst.frc.team20.robot;

import edu.wpi.first.wpilibj.CANTalon.ControlMode;

public class T20DriveStraightVBus implements T20Command{
	private double speed;
	private long time;
	private boolean finished;
	private boolean started;
	private long startTime;
	
	public T20DriveStraightVBus(double speed, long time) {
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
			System.out.println("<Drive VBus time=" + this.time + ">");
			this.started = true;
			this.startTime = Robot.getTime();
			Motors.fLeft.changeControlMode(ControlMode.PercentVbus);
			Motors.fRight.changeControlMode(ControlMode.PercentVbus);
			Motors.bLeft.changeControlMode(ControlMode.PercentVbus);
			Motors.bRight.changeControlMode(ControlMode.PercentVbus);
		}
		Motors.fLeft.set(-speed);//inverted
		Motors.fRight.set(speed);
		Motors.bRight.set(speed);
		Motors.bLeft.set(-speed);//inverted
		if(Robot.getTime() - this.startTime > this.time){
			this.finished = true;
			System.out.println("</Drive VBus>");
		}
	}

	@Override public boolean isFinished() {
		return this.finished;
	}

	@Override public T20Command copy() {
		return new T20DriveLateral(this.speed, this.time);
	}
}
