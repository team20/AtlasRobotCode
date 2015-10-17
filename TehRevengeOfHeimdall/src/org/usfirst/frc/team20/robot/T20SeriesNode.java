package org.usfirst.frc.team20.robot;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class T20SeriesNode implements T20Node{

	private List<T20Command> commands;
	private T20Command current;
	private Iterator<T20Command> iter;

	{
		commands = new ArrayList<>();
	}

	@Override public void execute() {
		if(iter == null){
			iter = commands.iterator();
			System.out.println("<Series members=" + commands.size() + ">");
		}
		if(current == null || current.isFinished()){
			if(!iter.hasNext()){
				System.out.println("</Series>");
				return;
			}
			current = iter.next();
		}
		current.execute();
	}

	@Override public boolean isFinished() {
		if(iter == null)
			return false;
		return !iter.hasNext() && current.isFinished();
	}

	@Override public void addChild(T20Command child){
		this.commands.add(child);
	}

	@Override public T20Command copy() {
		T20SeriesNode copy = new T20SeriesNode();
		for(T20Command child : commands){
			copy.addChild(child.copy());
		}
		return copy;
	}
}