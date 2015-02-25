package org.usfirst.frc.team20.robot;
import edu.wpi.first.wpilibj.Talon;

public class Rollers {
	public Talon leftRollers;
	public Talon rightRollers;
	
	public Rollers(int left, int right){
		leftRollers = new Talon(left);
		rightRollers = new Talon(right);
	}
	
	public void rollIn(){
		leftRollers.set(-1);
		rightRollers.set(1);
	}
	
	public void rollout(){
		leftRollers.set(1);
		rightRollers.set(-1);
	}
	
	public void stopRoll(){
		leftRollers.set(0);
		rightRollers.set(0);
	}
	
	public void DOABARRELROLL(){
		leftRollers.set(1);
		rightRollers.set(1);
	}
}
