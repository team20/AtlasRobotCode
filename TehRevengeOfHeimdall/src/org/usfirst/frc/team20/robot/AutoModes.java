package org.usfirst.frc.team20.robot;

import edu.wpi.first.wpilibj.CANTalon;
import edu.wpi.first.wpilibj.CANTalon.FeedbackDevice;
import edu.wpi.first.wpilibj.Gyro;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.Scheduler;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
//import edu.wpi.first.wpilibj.DriverStation;
//import edu.wpi.first.wpilibj.IterativeRobot;
//import edu.wpi.first.wpilibj.communication.FRCNetworkCommunicationsLibrary;
//import edu.wpi.first.wpilibj.networktables2.util.Stack;

public class AutoModes {
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
		//this is the main autonomous mode 
		public Autonomous1() {
	
		}
	}

	class Autonomous2 {
		public Autonomous2() {
	
		}
	}

	class Autonomous3 {
		public Autonomous3() {
		
		}
	}

	class Autonomous4 {
		public Autonomous4() {
	
		}
	}

	class Autonomous5 {
		public Autonomous5() {
	
		}
	}

	class Autonomous6 {
		public Autonomous6() {
	
		}
	}

	class Autonomous7 {
		public Autonomous7() {
	
		}
	}

	class Autonomous8 {
		public Autonomous8() {
	
		}
	}

	class Autonomous9 {
		public Autonomous9() {
	
		}
	}

	class Autonomous10 {
		public Autonomous10() {
	
		}
	}
	
	public void autonomousInit() {
		autonomousCommand = (Command) chooser.getSelected();
		autonomousCommand.start();
	}

	public void autonomousPeriodic() {
		Scheduler.getInstance().run();
	}
}
