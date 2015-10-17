package org.usfirst.frc.team20.robot;

public class T20sTrayGotClass {
	public static final int TRAY_EXTENDED = 1, TRAY_RETRACTED = 0,
			TRAY_POS_UNDEF = -1;
	public static final boolean TRAY_EXTEND = true, TRAY_RETRACT = false;
	static boolean trayExtended = false;
	static boolean traySetPoint = TRAY_RETRACT;

	static int trayPosPV = TRAY_POS_UNDEF;

	public static void extendTray() {
		if (Motors.driver.getRawButton(1)) {
			Motors.trayMotor.set(1);
			trayExtended = true;
			trayPosPV = TRAY_POS_UNDEF;
		}
	}

	public static void retractTray() {
		if (Motors.driver.getRawButton(2)) {
			Motors.trayMotor.set(-1);
			trayExtended = false;
			trayPosPV = TRAY_POS_UNDEF;
		}
	}

	public static void bigBrotherTray() {
		if (Robot.trayMotorFilteredCurrent > 10) {
			stopTray();
		}
	}

	public static void stopTray() {
		Motors.trayMotor.set(0);
		if (trayExtended = true) {
			trayPosPV = TRAY_EXTENDED;
		} else {
			trayPosPV = TRAY_RETRACTED;
		}
	}
}
