package org.usfirst.frc.team20.robot;

import edu.wpi.first.wpilibj.CANTalon.ControlMode;

public class T20DriveLateral implements T20Command {

	private double speed;
	private long time;
	private boolean finished;
	private boolean started;
	private long startTime;

	public T20DriveLateral(double speed, long time) {
		this.speed = speed;
		this.time = time;
		this.finished = false;
		this.started = false;
	}

	@Override
	public void execute() {
		if (finished) {
			return;
		}
		if (!started) {
			this.started = true;
			System.out.println("<Drive Lateral speed=" + this.speed + " time="
					+ this.time + ">");
			this.startTime = Robot.getTime();
			Motors.fLeft.changeControlMode(ControlMode.PercentVbus);
			Motors.fRight.changeControlMode(ControlMode.PercentVbus);
			Motors.bLeft.changeControlMode(ControlMode.PercentVbus);
			Motors.bRight.changeControlMode(ControlMode.PercentVbus);
		}
		Motors.fLeft.set(speed);// inverted
		Motors.fRight.set(speed);
		Motors.bRight.set(-speed + .2);
		Motors.bLeft.set(-speed + .2);// inverted
		if (Robot.getTime() - this.startTime > this.time) {
			Motors.fLeft.set(0);// inverted
			Motors.fRight.set(0);
			Motors.bRight.set(0);
			Motors.bLeft.set(0);// inverted
			this.finished = true;
			System.out.println("</Drive Lateral>");
		}
	}

	@Override
	public boolean isFinished() {
		return this.finished;
	}

	@Override
	public T20Command copy() {
		return new T20DriveLateral(this.speed, this.time);
	}
}