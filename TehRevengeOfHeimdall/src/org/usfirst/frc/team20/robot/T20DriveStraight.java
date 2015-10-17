package org.usfirst.frc.team20.robot;

import edu.wpi.first.wpilibj.CANTalon.ControlMode;

public class T20DriveStraight implements T20Command{
	
	private Trapezoidal trapezoidal;
	private boolean finished;
	private boolean started;
	private long startTime;
	
	public T20DriveStraight(double maxSetpointDistance, double distance, double acceleration, double deceleration){
		trapezoidal = Trapezoidal.create()
				.withAcceleration(acceleration)
				.withDeceleration(deceleration)
				.withDistance(distance)
				.withMaxSetPointDistance(maxSetpointDistance);
		this.finished = false;
		this.started = false;
	}
	
	@Override
	public void execute() {
		if(finished){
			return;
		}
		if(!started){
			Motors.fLeft.changeControlMode(ControlMode.Position);
			Motors.fLeft.setPosition(0);
			Motors.fRight.changeControlMode(ControlMode.Position);
			Motors.fRight.setPosition(0);
			Motors.bLeft.changeControlMode(ControlMode.Position);
			Motors.bLeft.setPosition(0);
			Motors.bRight.changeControlMode(ControlMode.Position);
			Motors.bRight.setPosition(0);
			if(Math.abs(Motors.fRight.getEncPosition()) < 100 && Math.abs(Motors.fLeft.getEncPosition()) < 100
					&& Math.abs(Motors.bRight.getEncPosition()) < 100 && Math.abs(Motors.bLeft.getEncPosition()) < 100){
				this.started = true;
				System.out.println("<Drive Straight Command accel=" + trapezoidal.getAcceleration() 
						+ " decel=" + trapezoidal.getDeceleration() + " distance=" + trapezoidal.getDistance() + ">");
			}else{
				return;
			}
			this.startTime = Robot.getTime();
		}
		double setPoint = trapezoidal.getPositionByTime(Robot.getTime() - this.startTime);
		//setPoint = trapezoidal.getDistance();
		System.out.println("SETPOINT::" + Motors.bLeft.getXEU());
		Motors.bLeft.setXEU(setPoint);
		Motors.bRight.setXEU(setPoint);
		Motors.fLeft.setXEU(setPoint);
		Motors.fRight.setXEU(setPoint);
		if(Math.abs(trapezoidal.getDistance() - Motors.bLeft.getXEU()) < 1){
			Motors.bLeft.setXEU(trapezoidal.getDistance());
			Motors.bRight.setXEU(trapezoidal.getDistance());
			Motors.fLeft.setXEU(trapezoidal.getDistance());
			Motors.fRight.setXEU(trapezoidal.getDistance());
			this.finished = true;
			System.out.println("</Drive Straight>");
		}
	}

	@Override public boolean isFinished() {
		return finished;
	}

	@Override public T20Command copy() {
		return new T20DriveStraight(trapezoidal.getMaxSetPointDistance(),
			trapezoidal.getDistance(),
			trapezoidal.getAcceleration(),
			trapezoidal.getDeceleration());
	}
}