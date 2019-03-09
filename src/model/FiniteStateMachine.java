package model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Stack;

public class FiniteStateMachine {
	public static char MOORE = 'o';
	public static char MEALY = 'e';

	private char type;
	private HashSet<String> inputAlphabet;
	private HashSet<String> outputAlphabet;
	private ArrayList<State> states;

	private HashMap<State, Integer> indexesOfPartition; 
	public FiniteStateMachine(char type) {
		inputAlphabet = new HashSet<String>();
		outputAlphabet = new HashSet<String>();
		states = new ArrayList<State>();
		this.type = type;
	}

	public void setInputAlphabet (HashSet<String> input) {
		inputAlphabet = input;
	}

	public void setOutputAlphabet (HashSet<String> output) {
		inputAlphabet = output;
	}

	public String[] getInputAlphabetArray() {
		Object[] a= inputAlphabet.toArray();
		String[] toR=  new String[a.length];
		for(int i=0;i<toR.length;i++) {
			toR[i]= (String) a[i];
		}
		return toR;
	}

	public ArrayList<ArrayList<State>> firstPartiononing (){
		HashMap<String, ArrayList<State>> groups= new HashMap<String, ArrayList<State>>();
		for(int i=0;i<states.size();i++) {
			String str="";
			for(String x : inputAlphabet) {
				str+=states.get(i).getOutput(x);
			}
			if(groups.containsKey(str)) {
				groups.get(str).add(states.get(i));
			}else {
				ArrayList<State> toAdd= new ArrayList<State>();
				toAdd.add(states.get(i));
				groups.put(str, toAdd);
			}

		}

		ArrayList<ArrayList<State>> toR= new ArrayList<ArrayList<State>>();
		for (String v : groups.keySet() ) {
			toR.add(groups.get(v));
		}
		return toR;
	}
	public ArrayList<ArrayList<State>> obtaintPartitions(){
		ArrayList<ArrayList<State>> firstPartition= firstPartiononing();
		initializeHashOfIndexOfPartition(firstPartition);
		
		return auxToObtainPartitions(firstPartition, firstPartition.size());
	}
	private ArrayList<ArrayList<State>> auxToObtainPartitions(ArrayList<ArrayList<State>> arr, int quantOfPartitions){
		ArrayList<ArrayList<State>> newPartition = new ArrayList<ArrayList<State>>();
		for(int i=0;i<arr.size();i++) {
			ArrayList<State> candidateOfNewClass= new ArrayList<State>();
			ArrayList<State> actualClass= arr.get(i);
			ArrayList<State> newActualClass= new ArrayList<State>();
			HashSet<Integer> positionsOfSucesors= new HashSet<Integer>();
			for(int j=0;j<actualClass.size();j++) {
				if(j==0) {
					newActualClass.add(actualClass.get(j));
					for(State x:actualClass.get(j).getStateTransition().values()) {
						positionsOfSucesors.add(indexesOfPartition.get(x));
					}
				}else {
					boolean sameClassesOfSucesors=true;
					for(State x:actualClass.get(j).getStateTransition().values()) {
						if(!positionsOfSucesors.contains(indexesOfPartition.get(x))) {
							sameClassesOfSucesors=false;
						}
					}
					if(!sameClassesOfSucesors) {
						State toMove= actualClass.get(j);
						candidateOfNewClass.add(toMove);
					}else {
						newActualClass.add(actualClass.get(j));
					}
				}
			}
			newPartition.add(newActualClass);
			newPartition.add(candidateOfNewClass);
		}
		
		if(newPartition.size()==arr.size()) {
			return newPartition;
		}else {
			initializeHashOfIndexOfPartition(newPartition);
			return auxToObtainPartitions(newPartition, newPartition.size());
		}
		
	}
	
	public void initializeHashOfIndexOfPartition(ArrayList<ArrayList<State>> arr) {
		indexesOfPartition= new HashMap<State, Integer>();
		for(int i=0;i<arr.size();i++) {
			ArrayList<State> partition= arr.get(i);
			for(int j=0;j<partition.size();j++) {
				indexesOfPartition.put(partition.get(j), i);
			}
		}
	}

	public HashSet<String> getInputAlphabet(){
		return inputAlphabet;
	}

	public HashSet<String> getOutputAlphabet(){
		return outputAlphabet;
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
		if (type == MEALY) {
			if (!inputAlphabet.contains(input)) {
				throw new Exception("El elemento de entrada debe de pertencer al alfabeto S");
			}

			if (!outputAlphabet.contains(output)) {
				throw new Exception("El elemento de salida debe de pertenecer al alfabeto R");
			}

			MealyState theState = (MealyState) states.get(onState);
			theState.addOutput(input, output);	
		}
	}

	public boolean isComplete() {
		boolean complete = true;
		int stateIndex = 0;
		while (complete && stateIndex < states.size()) {
			State act = states.get(stateIndex);
			complete &= inputAlphabet.size() == act.totalTransitions();
			if (type == MEALY)
				complete &= inputAlphabet.size() == ((MealyState) act).totalTransitions();
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
	public ArrayList<State> getStates(){
		return states;
	}

	public char getType() {
		return type;
	}
}
