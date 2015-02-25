package org.usfirst.frc.team20.robot;

import edu.wpi.first.wpilibj.CANTalon;
import edu.wpi.first.wpilibj.CANTalon.ControlMode;

public class Elevator {

	public CANTalon master;
	public CANTalon slave1;
	public CANTalon slave2;
	public CANTalon slave3;

	public Elevator(int m, int s1, int s2, int s3) {
		master = new CANTalon(m);
		slave1 = new CANTalon(s1);
		slave2 = new CANTalon(s2);
		slave3 = new CANTalon(s3);
		master.changeControlMode(ControlMode.PercentVbus);
		
		slave1.changeControlMode(ControlMode.Follower);
		slave1.set(m);
		slave2.changeControlMode(ControlMode.Follower);
		slave2.set(m);
		slave3.changeControlMode(ControlMode.Follower);
		slave3.set(m);	
	}

	public void set(double percentVoltage){
//		master.changeControlMode(ControlMode.PercentVbus);
		master.set(percentVoltage);
	}
	
	public void setPosition(int encoderPos){
		//TODO: Decide if we can trust the code provided
		
//		master.changeControlMode(ControlMode.Position);
//		master.set(encoderPos);	
	}
	
	public void checkElevator(){
		if (!Sensors.elevatorShort.get()) {
			master.changeControlMode(ControlMode.Position);
			master.setPosition(0);
			master.changeControlMode(ControlMode.PercentVbus);
		}
	}
}
