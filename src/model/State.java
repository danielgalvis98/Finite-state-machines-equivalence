package model;

import java.util.HashMap;
/**
 * Represents a state
 * @author 
 *
 */
public abstract class State {
	/**
	 * represents the name of the state
	 */
	private String name;
	/**
	 * is the transition function
	 */
	private HashMap<String, State> stateTransition;
	/**
	 * construct a state
	 * @param name
	 */
	public State (String name) {
		this.name = name;
		stateTransition = new HashMap<String, State>();
		
	}
	/**
	 * gets the transition state
	 * @param input element of the input alphabet
	 * @return State
	 */
	public State getTransitionState (String input) {
		return stateTransition.get(input);
	}
	/**
	 * Counts the total of transitions
	 * @return int
	 */
	public int totalTransitions () {
		return stateTransition.size();
	}
	
	/**
	 * adds a element to the transition function
	 * @param input an element of the input alphabet
	 * @param newState the state that the input element produces
	 */
	public void addTransitionState (String input, State newState) {
		stateTransition.put(input, newState);
	}
	/**
	 * returns if a transition for the input element exists
	 * @param input input element
	 * @return true in case that it exists
	 */
	public boolean transitionExists (String input) {
		return stateTransition.containsKey(input);
	}
	/**
	 * sets the name of the state
	 * @param n
	 */
	public void setName(String n) {
		name = n;
	}
	/**
	 * Returns the transition function
	 * @return HashMap<String, State>
	 */
	public HashMap<String, State> getStateTransition(){
		return stateTransition;
	}
	/**
	 * returns the output produces by an input element
	 * @param input
	 * @return
	 */
	public abstract String getOutput(String input);
	/**
	 * returns the na,e of the state
	 * @return
	 */
	public String getName() {
		return name;
	}

}
