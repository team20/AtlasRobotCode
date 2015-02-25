package org.usfirst.frc.team20.robot;

import edu.wpi.first.wpilibj.CANTalon;
import edu.wpi.first.wpilibj.CANTalon.ControlMode;

public class Forks {
	
	public CANTalon forks;
	
	public Forks(int port){
		forks = new CANTalon(port);
		forks.changeControlMode(ControlMode.PercentVbus);
	}

	public void set(double percentVoltage){
		forks.changeControlMode(ControlMode.PercentVbus);
		forks.set(percentVoltage);
	}
	
	public void setPosition(int position){
		//TODO: Decide if we can trust the code provided
		
//		forks.changeControlMode(ControlMode.Position);
//		forks.set(position);
		
	}
	
	public void checkForks(){
		if (forks.getOutputCurrent() > 15) {
			forks.set(0);
		}
	}
}
