package org.usfirst.frc.team20.robot;

public class T20Stabilizer {

	public static void stabilize() {
		Motors.stabilizerMotor.set(-.45);
	}

	public static void letItGo() {
		Motors.stabilizerMotor.set(.45);
	}

	public static void stopStabilizer() {
		Motors.stabilizerMotor.set(0);
	}

	public static void bigBrotherStabilizer() {
		if (Motors.stabilizerMotor.getOutputCurrent() > 3) {
			stopStabilizer();
		}
	}
}
