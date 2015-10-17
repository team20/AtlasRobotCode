package org.usfirst.frc.team20.robot;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class T20ParallelNode implements T20Node{

	private boolean started;
	private List<T20Command> commands;

	{
		this.commands = new ArrayList<>();
		this.started = false;
	}

	@Override public void execute() {
		if(commands.size() == 0){
			System.out.println("</Parallel>");
			return;
		}
		if(!started){
			this.started = true;
			System.out.println("<Parallel members=" + commands.size() + ">");
		}
		for(Iterator<T20Command> iter = commands.iterator(); iter.hasNext();){
			T20Command command = iter.next();
			if(command.isFinished())
				iter.remove();
			else
				command.execute();
		}
	}

	@Override public boolean isFinished() {
		return commands.size() == 0;
	}

	@Override public void addChild(T20Command child){
		this.commands.add(child);
	}
	
	@Override public T20Command copy() {
		T20ParallelNode copy = new T20ParallelNode();
		for(T20Command node : this.commands){
			copy.addChild(node.copy());
		}
		return copy;
	}
}