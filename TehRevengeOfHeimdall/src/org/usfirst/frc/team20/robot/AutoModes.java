package org.usfirst.frc.team20.robot;

public class AutoModes {
<<<<<<< HEAD
=======
  SendableChooser chooser;
	Command autonomousCommand;
	Gyro g1;
	Joystick stick = new Joystick(0);
	CANTalon tal1 = new CANTalon(2);
	
	// window motor PID values
	double g1value;
	double p = .5;
	double i = 0.0001;
	double d = .5;
	double ramp = 1500;
	double test;
	double position = 0;
	
	public void robotInit() {
		chooser = new SendableChooser();
		chooser.addDefault("Autonomous 1/Main Autonomous", new Autonomous1());
		chooser.addObject("Autonomous 2", new Autonomous2());
		chooser.addObject("Autonomous 3", new Autonomous3());
		chooser.addObject("Autonomous 4", new Autonomous4());
		chooser.addObject("Autonomous 5", new Autonomous5());
		chooser.addObject("Autonomous 6", new Autonomous6());
		chooser.addObject("Autonomous 7", new Autonomous7());
		chooser.addObject("Autonomous 8", new Autonomous8());
		chooser.addObject("Autonomous 9", new Autonomous9());
		chooser.addObject("Autonomous 10", new Autonomous10());
		SmartDashboard.putData("Autonomous Selector", chooser);
		tal1.setFeedbackDevice(FeedbackDevice.QuadEncoder);
		tal1.changeControlMode(CANTalon.ControlMode.Position);
		tal1.setPosition(0);
		tal1.setPID(p, i, d);
		tal1.setCloseLoopRampRate(ramp);
		tal1.enableControl();

		Gyro g1 = new Gyro(1);
		g1.initGyro();
		g1.reset();
		
	class Autonomous1 {
		//insert main autonomous code here
	}
	class Autonomous2 {
		//insert code here
	}
	class Autonomous3 {
		//insert code here
	}
	class Autonomous4 {
		//insert code here
	}
	class Autonomous5 {
		//insert code here
	}
	class Autonomous6 {
		//insert code here
	}
	class Autonomous7 {
		//insert code here
	}
	class Autonomous8 {
		//insert code here
	}
	class Autonomous9 {
		//insert code here
	}
	class Autonomous10 {
		//insert code here
	}
	
	public void autonomousInit() {
		autonomousCommand = (Command) chooser.getSelected();
		autonomousCommand.start();
	}
>>>>>>> origin/master

}
