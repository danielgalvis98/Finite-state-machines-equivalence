package model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Stack;

/**
 * 	The class that represents a finite state machine
 * @author 
 *
 */
public class FiniteStateMachine {
	/**
	 * Constant that represents a Moore machine
	 */
	public final static char MOORE = 'o';
	/**
	 * Constant that represents a Mealy machine
	 */
	public final static char MEALY = 'e';

	/**
	 * Variable that contains the type of the machine (Moore o Mealy)
	 */
	private char type;
	/**
	 * Is a hashset that contains the input alphabet of the machine
	 */
	private HashSet<String> inputAlphabet;
	/**
	 * Is a hashset that contains the output alphabet of the machine
	 */
	private HashSet<String> outputAlphabet;
	/**
	 * List of all of the states of the machine
	 */
	private ArrayList<State> states;
	/**
	 * quantity of states of the machine
	 */
	int totStates;

	/**
	 * Is a hashmap that contains in what class a state is in. The class is a list and all the classes are
	 * in list
	 */
	private HashMap<State, Integer> indexesOfPartition; 
	/**
	 * Initializes the finite state machine
	 * @param char- type the type of the machine
	 * @param totStates int- quantity of states
	 */
	public FiniteStateMachine(char type, int totStates) {
		inputAlphabet = new HashSet<String>();
		outputAlphabet = new HashSet<String>();
		states = new ArrayList<State>();
		this.type = type;
		this.totStates = totStates;
	}

	/**
	 * puts the hashset of the input alphabet in the atributte 'inputAlphabet'
	 * @param input HashSet<String>- the hashset that contains the input alphabet
	 */
	public void setInputAlphabet (HashSet<String> input) {
		inputAlphabet = input;
	}

	/**
	 * puts the hashset of the output alphabet
	 * @param output HashSet<String>- hashset of the output alphabet
	 */
	public void setOutputAlphabet (HashSet<String> output) {
		inputAlphabet = output;
	}
	
	/**
	 * returns the quantity of states
	 * @return int- quantity of states
	 */
	public int getTotalStates() {
		return totStates;
	}

	/**
	 * returns an array that contains the input alphabet
	 * @return String[]- inputAlphabet
	 */
	public String[] getInputAlphabetArray() {
		Object[] a= inputAlphabet.toArray();
		String[] toR=  new String[a.length];
		for(int i=0;i<toR.length;i++) {
			toR[i]= (String) a[i];
		}
		return toR;
	}

	/**
	 * Algorithm responsible for doing the first partitioning
	 * @return ArrayList<ArrayList<State>>- List of Lists, each list is a class
	 */
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
	/**
	 * Method that obtains the equivalence classes
	 * @return ArrayList<ArrayList<State>> list of lists, each list represents a equivalence class
	 */
	public ArrayList<ArrayList<State>> obtaintPartitions(){
		ArrayList<ArrayList<State>> firstPartition= firstPartiononing();
		initializeHashOfIndexOfPartition(firstPartition);
		
		return auxToObtainPartitions(firstPartition, firstPartition.size());
	}
	/**
	 * is an auxiliar method that obtains the equivalence classes
	 * @param arr the first time it is the first partitioning, then it is the set of equivalence classes
	 * @param quantOfPartitions is the number of equivalence classes within arr
	 * @return ArrayList<ArrayList<State>> list of lists, each list represents a equivalence class
	 */
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
			if(candidateOfNewClass.size()!=0)
				newPartition.add(candidateOfNewClass);
		}
		
		if(newPartition.size()==arr.size()) {
			return newPartition;
		}else {
			initializeHashOfIndexOfPartition(newPartition);
			return auxToObtainPartitions(newPartition, newPartition.size());
		}
		
	}
	/**
	 * It hashes in what class a state is in
	 * @param arr the set of equivalence classes
	 */
	public void initializeHashOfIndexOfPartition(ArrayList<ArrayList<State>> arr) {
		indexesOfPartition= new HashMap<State, Integer>();
		for(int i=0;i<arr.size();i++) {
			ArrayList<State> partition= arr.get(i);
			for(int j=0;j<partition.size();j++) {
				indexesOfPartition.put(partition.get(j), i);
			}
		}
	}

	/**
	 * returns the hashset of input alphabet
	 * @return HashSet<String> the hashset that contains the input alphabet
	 */
	public HashSet<String> getInputAlphabet(){
		return inputAlphabet;
	}
	/**
	 * returns the hashset of output alphabet
	 * @return HashSet<String> the hashset that contains the output alphabet
	 */
	public HashSet<String> getOutputAlphabet(){
		return outputAlphabet;
	}

	/**
	 * adds an element to the input alphabet
	 * @param inputElement element to add
	 */
	public void addInputAlphabetElement (String inputElement) {
		inputAlphabet.add(inputElement);

	}
	/**
	 * adds an element to the output alphabet
	 * @param outputElement element to add
	 */
	public void addOutputAlphabetElement (String outputElement) {
		outputAlphabet.add(outputElement);
	}
	/**
	 * Adds a new state to the machine
	 * @param newState state to add
	 */
	public void addState (State newState) {
		states.add(newState);
	}
	/**
	 * adds a transition between two states
	 * @param inputElement the input associated to the transition
	 * @param fromState the start of the transition
	 * @param toState the state where the transition ends
	 * @throws Exception when the input element doesn't exist in the input alphabet
	 */
	public void addStateTransition (String inputElement, int fromState, int toState) throws Exception {
		if (!inputAlphabet.contains(inputElement)) {
			throw new Exception("El elemento de entrada debe de pertencer al alfabeto S");
		}

		State firstState = states.get(fromState);
		State directedState = states.get(toState);

		firstState.addTransitionState(inputElement, directedState);
	}
	/**
	 * adds the output of a transition
	 * @param input the input of the transition
	 * @param onState the actual state that will produce the output
	 * @param output element of the output alphabet
	 * @throws Exception if the output element doesn't exist in the output alphabet
	 */
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

	/**
	 * returns if for each state it has a transition for each input alphabet
	 * @return true if each state has a transition for each input alphabet or false in other case
	 */
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
	/**
	 * Deletes all inaccesible states 
	 */
	public void deleteInaccessibleStates() {
		HashSet<State> accesibleStates = getAccesibleStates();
		for (State st : states) {
			if (!accesibleStates.contains(st)) {
				states.remove(st);
			}
		}
	}
	/**
	 * returns all accesible states
	 * @return hashset that contains all accesible states
	 */
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
	/**
	 * returns the states of the machine
	 * @return list of states
	 */
	public ArrayList<State> getStates(){
		return states;
	}

	/**
	 * returns the type of the machine	
	 * @return 'o' if it is moore machine or e in contrary case
	 */
	public char getType() {
		return type;
	}
}
