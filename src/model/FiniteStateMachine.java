package model;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Stack;

public class FiniteStateMachine {
	private HashSet<String> inputAlphabet;
	private HashSet<String> outputAlphabet;
	private ArrayList<State> states;
	
	public FiniteStateMachine() {
		inputAlphabet = new HashSet<String>();
		outputAlphabet = new HashSet<String>();
		states = new ArrayList<State>();
	}
	
	public void addInputAlphabetElement (String inputElement) {
		inputAlphabet.add(inputElement);
	}
	
	public void addOutputAlphabetElement (String outputElement) {
		outputAlphabet.add(outputElement);
	}
	
	public void deleteInaccessibleStates() {
		HashSet<State> accesibleStates = getAccesibleStates();
		for (State st : states) {
			if (!accesibleStates.contains(st)) {
				states.remove(st);
			}
		}
	}
	
	public HashSet<State> getAccesibleStates (){
		HashSet<State> accesibleStates = new HashSet<State>();
		State init = states.get(0);
		Stack <State> stackTravel = new Stack<>();
		stackTravel.add(init);
		while (!stackTravel.isEmpty()) {
			State act = stackTravel.pop();
			accesibleStates.add(act);
			for (String x : inputAlphabet) {
				State newState = act.getTransitionState(x);
				if (!accesibleStates.contains(newState)) {
					accesibleStates.add(newState);
					stackTravel.add(newState);
				}
			}
		}
		return accesibleStates;
	}
}
