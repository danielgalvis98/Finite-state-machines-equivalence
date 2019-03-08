package model;

import java.util.HashMap;

public abstract class State {
	private String name;
	
	private HashMap<String, State> stateTransition;
	
	public State (String name) {
		this.name = name;
		stateTransition = new HashMap<String, State>();
	}
	
	public State getTransitionState (String input) {
		return stateTransition.get(input);
	}
	
	public int totalTransitions () {
		return stateTransition.size();
	}
	
	
	public void addTransitionState (String input, State newState) {
		stateTransition.put(input, newState);
	}
	
	public boolean transitionExists (String input) {
		return stateTransition.containsKey(input);
	}
	public void setName(String n) {
		name = n;
	}
	
	public abstract String getOutput(String input);
	
	public String getName() {
		return name;
	}

}
