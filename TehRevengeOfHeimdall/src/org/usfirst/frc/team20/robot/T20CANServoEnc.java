/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.usfirst.frc.team20.robot;

import edu.wpi.first.wpilibj.CANTalon;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

//import java.lang.*;

/**
 *
 * @author Team 20
 * 
 *         Extends CANJaguar to provide simple use of CAN based motor controller
 *         where position control using potentiometer feedback to controller.
 * 
 */
public class T20CANServoEnc extends CANTalon {
	private int masterId;

	private double p;
	private double i;
	private double d;
	private int position;
	private double requestedX;
	private double controllerX;
	private double zero;
	private double span;
	private double deadBand;
	private boolean enabled;
	private String canName;
	private double scaleXDZero;
	private double scaleXDSpan;
	private String scaleXDUOM;
	private boolean homed;
	public CANTalon[] slaves;
	private double homeIncrementTicks;

	// never use this constructor.
	// CanServoPos(int masterId) throws CANTimeoutException {
	// super(masterId);
	// }

	/**
	 * Use this constructor only.
	 * 
	 * @param masterId
	 *            Id of can device.
	 * @param turns
	 *            Potentiometer turns.
	 * @param p
	 *            Gain of CAN device PID controller.
	 * @param i
	 *            Integral term of CAN device PID controller.
	 * @param d
	 *            Derivative term of CAN device PID controller.
	 * @param zero
	 *            Lower set-point limit in turns.
	 * @param span
	 *            Upper set-point limit in turns.
	 * @param deadBand
	 *            In turns. Used to disable PID action and disable control when
	 *            abs(feedback - setpoint) < deadBand.
	 * 
	 * @throws CANTimeoutException
	 */
	T20CANServoEnc(int masterId, int[] slaves, double p, double i, double d,
			double zero, double span, double deadBand) {
		super(masterId);
		this.slaves = new CANTalon[slaves.length];
		this.masterId = masterId;
		this.deadBand = deadBand;
		this.p = p;
		this.i = i;
		this.d = d;
		this.zero = zero;
		this.span = span;
		this.homeIncrementTicks = -((span - zero) * .01);
		this.changeControlMode(ControlMode.Position);
		// this.configMaxOutputVoltage(12);
		this.setPID(p, i, d);
		this.setFeedbackDevice(FeedbackDevice.QuadEncoder);
		this.disableControl();
		super.setPosition(zero);
		this.controllerX = zero;
		this.requestedX = zero;
		this.scaleXDZero = 0;
		this.scaleXDSpan = 1;
		this.scaleXDUOM = "%";
		for (int j = 0; j < slaves.length; j++) {
			CANTalon slave = new CANTalon(slaves[j]);
			slave.changeControlMode(ControlMode.Follower);
			slave.set(masterId);
			this.slaves[j] = slave;
		}
		this.homed = false;
	}

	/**
	 * setXDScale set the conversion between engineering units and turns for <br>
	 * servo drive. <br>
	 * 
	 * setX sets the set-point in turns. setXEU will set the set-point scaled<br>
	 * to engineering units. The scaling is performed by setting the XDScale.<br>
	 * <br>
	 * I.E. <br>
	 * With zero = 5 and span = 7 turns (set in constructor)<br>
	 * setXDScale(0, 360, "deg");<br>
	 * <br>
	 * A call to SetXEU would yield the following.<br>
	 * setXEU(0) would give 5 turns<br>
	 * setXEU(180) would give 6 turns<br>
	 * setXEU(360) would give 7 turns<br>
	 * <br>
	 * XDScale may also be inverted. so if zero and span are 5 to 7 turns <br>
	 * and XDScale is set zeroXD = 360 and spanXD = 0. <br>
	 * then following would be true<br>
	 * setXDScale(360, 0, "deg");<br>
	 * <br>
	 * A call to SetXEU would yield the following.<br>
	 * setXEU(0) would give 7 turns<br>
	 * setXEU(180) would give 6 turns<br>
	 * setXEU(360) would give 5 turns<br>
	 * <br>
	 * By default XDScale is 0-1.<br>
	 * Engineering units are informational only.<br>
	 * <br>
	 * 
	 * @param zeroXD
	 *            XDScale zero parameter<br>
	 * @param spanXD
	 *            XDScale span parameter<br>
	 * @param engineeringUnits
	 *            Text informational only.<br>
	 */
	public void setXDScale(double zeroXD, double spanXD, String engineeringUnits) {
		this.scaleXDZero = zeroXD;
		this.scaleXDSpan = spanXD;
		this.scaleXDUOM = engineeringUnits;
	}

	/**
	 * Sets the position of controller to setpoint scaled for engineering units.
	 * see setXDScale for details.
	 * 
	 * @param setPoint
	 *            Set-point in engineering units.
	 * 
	 */
	public void setXEU(double setPoint) {
		SmartDashboard.putString("elevator setpoint EU", String.valueOf(setPoint));
		this.set(this.getEUToTicks(setPoint));
	}

	/**
	 * 
	 * Sets the setpoint for CAN motor controller also performs disable and
	 * enable function for controller.
	 * 
	 * This needs to be call periodically to perform dead-band function.
	 * 
	 * @param x
	 *            Requested set-point in turns.
	 * 
	 */
	@Override public final void set(double x) {
		this.enableControl();
		if(!homed){
			home();
			return;
		}
		this.requestedX = x;
		super.set(this.requestedX);
		this.controllerX = this.requestedX;
		SmartDashboard.putString("elevator setpoint", String.valueOf(this.requestedX));
		SmartDashboard.putString("elevator setpoint Actual", String.valueOf(this.getSetpoint()));		
		
		// do nothing if command out of bounds
		if ((this.span < this.zero) && (this.requestedX > this.zero) || (this.requestedX < this.span)) {
			return;
		}
		if ((this.span > this.zero) && (this.requestedX < this.zero) || (this.requestedX > this.span)) {
			return;
		}
		// check to see if the can position is close enough
		this.position = super.getEncPosition();
		// if we are in position turn off controller if it is on
		if (this.enabled
				&& Math.abs(this.position - this.requestedX) < this.deadBand) {
			this.disableControl();
			return;
		}

		// only send can command if it is new position change is significant.
		if (Math.abs(this.position - this.requestedX) > this.deadBand) {
			super.set(this.requestedX);
			this.controllerX = this.requestedX;
			this.enableControl();
		}
		
	}// set

	public double getXEU() {
		return this.getTicksToEU(super.getEncPosition());
	}

	/**
	 * Sets human readable name for CAN Controller Not required but can be nice
	 * for toString.
	 * 
	 * @param canName
	 */
	public void setCanName(String canName) {
		this.canName = canName;
	}

	/**
	 * Gets human readable name if set.
	 * 
	 * @return Name of CAN Device
	 */
	public String getCanName() {
		return this.canName;
	}

	/**
	 * Enables CAN Motor controller. Not normally used as this is taken care of
	 * by setX
	 * 
	 */
	public void enableControl() {
		if (!this.enabled)
			super.enableControl();
		this.enabled = true;
	}

	/**
	 * Disables CAN Motor controller. Not normally used as this is taken care of
	 * by setX
	 * 
	 */
	public final void disableControl() {
		if (this.enabled)
			super.disableControl();
		this.enabled = false;
	}
	
	public void setHomeEU(double EU){
		this.setHome((int)this.getEUToTicks(EU));
	}
	
	public void setHome(int position){
		this.position = position;
		this.requestedX = position;
		this.controllerX = position;
		super.setPosition(position);
	}
	
	public void autoHome(){
		this.homed = false;
		this.home();
	}
	
	public void setHomeAtCurrentPosition(int pos){
		
	}
	
	private void home(){
		int pos = super.getEncPosition();
		SmartDashboard.putString("home position", String.valueOf(pos + this.homeIncrementTicks));
		super.setPosition(pos);
		super.set(pos + this.homeIncrementTicks);
		if(super.isFwdLimitSwitchClosed() || super.isRevLimitSwitchClosed()){
			this.homed = true;
			super.setPosition(this.zero);
			this.set(this.zero);
			this.position = (int)this.zero;
			
		}
		SmartDashboard.putString("homed", String.valueOf(this.homed));
	}

	/**
	 * returns turns scaled from engineering units. see XD scale.
	 * 
	 * @param setPointEU
	 * @return
	 */
	private double getEUToTicks(double engUnits) {
		double rangeTicks;
		double rangeXD;
		double pctXdScale;

		rangeTicks = this.span - this.zero; // range in turns
		rangeXD = this.scaleXDSpan - this.scaleXDZero; // range in XD units
														// (Engineering units)
		pctXdScale = (engUnits - this.scaleXDZero) / rangeXD;
		return (pctXdScale * rangeTicks + this.zero);
	}

	private double getTicksToEU(double turns) {
		double rangeXD;
		double pctTurns;

		rangeXD = this.scaleXDSpan - this.scaleXDZero; // range in XD units
														// (Engineering units)
		pctTurns = (turns - this.zero) / this.span;
		return (pctTurns * rangeXD + this.scaleXDZero);

	}

	/**
	 * 
	 * @return String information about CAN Motor controller status. To be used
	 *         for debugging only. Is very CAN bus intensive.
	 * 
	 */
	public String toString() {
		String buffer;
		buffer = "masterId = " + masterId;
		buffer = buffer + " " + this.canName;
		buffer = buffer + "\n Zero = " + zero;
		buffer = buffer + " Span = " + span;
		buffer = buffer + " XDZero = " + this.scaleXDZero;
		buffer = buffer + " XDSpan = " + this.scaleXDSpan;
		buffer = buffer + "\n XD UOM = " + this.scaleXDUOM;
		buffer = buffer + " position = " + (position + ".000").substring(0, 4);
		buffer = buffer + ", deadBand = " + deadBand;
		buffer = buffer + "\n p = " + p;
		buffer = buffer + ", i = " + i;
		buffer = buffer + ", d = " + d;
		buffer = buffer + ", requestedX = " + requestedX;
		buffer = buffer + ", controllerX = " + controllerX;
		buffer = buffer + "\n enabled = " + enabled;
		buffer = buffer + "\n Control Mode = " + this.getControlMode().value;
		buffer = buffer + " position = " + this.getEncPosition();
		buffer = buffer + "\n P,I,D = " + this.getP() + ", " + this.getI()
				+ ", " + this.getD();
		buffer = buffer + "\n output current = " + this.getOutputCurrent();
		buffer = buffer + " output voltage = " + this.getOutputVoltage();

		return buffer;
	}

}
