package model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

public class Management {

	private FiniteStateMachine directSumMachine;
	private FiniteStateMachine m1; 
	private FiniteStateMachine m2;
	
	public Management (String [] inputAlphabet, String [] outputAlphabet, char type, int totStatesMachine1, int totStatesMachine2) {
		m1 = new FiniteStateMachine(type, totStatesMachine1);
		m2 = new FiniteStateMachine(type, totStatesMachine2);
		for (String x : inputAlphabet) {
			m1.addInputAlphabetElement(x);
			m2.addInputAlphabetElement(x);
		}
		
		for (String x : outputAlphabet) {
			m1.addOutputAlphabetElement(x);
			m2.addOutputAlphabetElement(x);
		}
	}
	public void renameStatesAndDoDirectSum() {
		ArrayList<State> s1= m1.getStates();
		ArrayList<State> s2= m2.getStates();
		if(s1.get(0) instanceof MealyState) {
			directSumMachine = new FiniteStateMachine(FiniteStateMachine.MEALY, m1.getTotalStates() + m2.getTotalStates());
		}else {
			directSumMachine = new FiniteStateMachine(FiniteStateMachine.MOORE, m1.getTotalStates() + m2.getTotalStates());
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
	

}
