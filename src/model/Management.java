package model;

import java.util.ArrayList;
import java.util.HashMap;

public class Management {

	private FiniteStateMachine directSumMachine;
	private FiniteStateMachine m1; 
	private FiniteStateMachine m2;
	private String[] inputAlphabet;
	
	public void renameStatesAndDoDirectSum() {
		ArrayList<State> s1= m1.getStates();
		ArrayList<State> s2= m2.getStates();
		inputAlphabet = m1.getInputAlphabetArray();
		if(s1.get(0) instanceof MealyState) {
			directSumMachine = new FiniteStateMachine(FiniteStateMachine.MEALY);
		}else {
			directSumMachine = new FiniteStateMachine(FiniteStateMachine.MOORE);
		}
		directSumMachine.setInputAlphabet(m1.getInputAlphabet());
		directSumMachine.setOutputAlphabet(m1.getOutputAlphabet());
		for(int i=0;i<m1.getStates().size()+m2.getStates().size();i++) {
			if(i<s1.size()) {
				s1.get(i).setName(i+"");
				directSumMachine.addState(s1.get(i));
			}else {
				s2.get(i-s2.size()-1).setName(i+"");
				directSumMachine.addState(s2.get(i-s2.size() - 1));
			}
		}
	}
	
	public ArrayList<ArrayList<State>> firstPartitioning() {
		HashMap<String, ArrayList<State>> groups= new HashMap<String, ArrayList<State>>();
		ArrayList<State> s= directSumMachine.getStates();
		if(directSumMachine!= null) {
			if(directSumMachine.getType()== FiniteStateMachine.MEALY) {
				for(int i=0;i<s.size();i++) {
					String str="";
					for(int j=0;j<inputAlphabet.length;j++) {
						str+=s.get(i).getOutput(inputAlphabet[j]);
					}
					if(groups.containsKey(str)) {
						groups.get(str).add(s.get(i));
					}else {
						ArrayList<State> toAdd= new ArrayList<State>();
						toAdd.add(s.get(i));
						groups.put(str, toAdd);
					}
						
				}
			}else {
				//TODO
			}
		}
		
		ArrayList<ArrayList<State>> toR= new ArrayList<ArrayList<State>>();
		for (String v : groups.keySet() ) {
			toR.add(groups.get(v));
		}
		return toR;
	}

}
