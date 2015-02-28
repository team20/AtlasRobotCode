package org.usfirst.frc.team20.robot;

import edu.wpi.first.wpilibj.CANTalon.ControlMode;

public class StackPlace {
	public final int ELEVATOR_MASTER_PORT = 7, ELEVATOR_SLAVE_ONE = 4,
			ELEVATOR_SLAVE_TWO = 3, ELEVATOR_SLAVE_THREE = 8;

	Elevator elevator = new Elevator(ELEVATOR_MASTER_PORT, ELEVATOR_SLAVE_ONE,
			ELEVATOR_SLAVE_TWO, ELEVATOR_SLAVE_THREE);

	public void PlaceStack() {
		Elevator.master.changeControlMode(ControlMode.PercentVbus);
		if (!Sensors.elevatorShort.get()) {
			elevator.set(-1);
		} else if (Sensors.elevatorShort.get()) {
			elevator.set(0);
		}
	}

}
