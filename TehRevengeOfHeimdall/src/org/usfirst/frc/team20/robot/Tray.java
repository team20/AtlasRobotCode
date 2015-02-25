package org.usfirst.frc.team20.robot;

import edu.wpi.first.wpilibj.CANTalon;
import edu.wpi.first.wpilibj.CANTalon.ControlMode;

public class Tray {
	public static CANTalon trayMotor;
	public Tray(int port){
		trayMotor = new CANTalon(port);
		trayMotor.changeControlMode(ControlMode.PercentVbus);
	}

	public void trayExtend(){
		trayMotor.set(-1);
	}
	
	public void trayRetract(){
		trayMotor.set(1);
	}
	
	public void checkTray(){
		if (trayMotor.getOutputCurrent() > 20 || !Sensors.trayExtened.get() || !Sensors.trayRetracted.get()) {
			trayMotor.set(0);
		}
	}
}
