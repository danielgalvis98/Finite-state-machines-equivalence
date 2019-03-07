package model;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Stack;

public abstract class FiniteStateMachine {
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
	
	public void addState (State newState) {
		states.add(newState);
	}
	
	public void addStateTransition (String inputElement, int fromState, int toState) throws Exception {
		if (!inputAlphabet.contains(inputElement)) {
			throw new Exception("El elemento de entrada debe de pertencer al alfabeto S");
		}
		
		State firstState = states.get(fromState);
		State directedState = states.get(toState);
		
		firstState.addTransitionState(inputElement, directedState);
	}
	
	public void addOutputTransition (String input, int onState, String output) throws Exception{
		if (!inputAlphabet.contains(input)) {
			throw new Exception("El elemento de entrada debe de pertencer al alfabeto S");
		}
		
		if (!outputAlphabet.contains(output)) {
			throw new Exception("El elemento de salida debe de pertenecer al alfabeto R");
		}
		
		MealyState theState = (MealyState) states.get(onState);
		theState.addOutput(input, output);
	}
	
	public boolean isComplete() {
		boolean complete = true;
		int stateIndex = 0;
		while (complete && stateIndex < states.size()) {
			State act = states.get(stateIndex);
			complete &= inputAlphabet.size() == act.totalTransitions();
		}
		return complete;
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
