package org.usfirst.frc.team20.robot;

public interface T20Node extends T20Command{

	@Override void execute();

	@Override boolean isFinished();
	
	@Override T20Command copy();
	
	void addChild(T20Command child);
}

