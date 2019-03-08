package model;

import java.util.ArrayList;
import java.util.HashMap;

public class Management {

	private FiniteStateMachine m;
	private String[] inputAlphabet;
	public void renameStatesAndDoDirectSum(FiniteStateMachine m1, FiniteStateMachine m2) {
		ArrayList<State> s1= m1.getStates();
		ArrayList<State> s2= m2.getStates();
		inputAlphabet = m1.getInputAlphabet();
		if(s1.get(0) instanceof MealyState) {
			m = new FiniteStateMachine(FiniteStateMachine.MEALY);
		}else {
			m = new FiniteStateMachine(FiniteStateMachine.MOORE);
			
		}
		for(int i=0;i<m1.getStates().size()+m2.getStates().size();i++) {
			if(i<s1.size()) {
				s1.get(i).setName(i+"");
				m.addState(s1.get(i));
			}else {
				s2.get(i-s2.size()-1).setName(i+"");
				m.addState(s2.get(i-s2.size()));
			}
		}
	}
	
	public ArrayList<ArrayList<State>> firstPartitioning() {
		HashMap<String, ArrayList<State>> groups= new HashMap<String, ArrayList<State>>();
		ArrayList<State> s= m.getStates();
		if(m!= null) {
			if(m.getType()== FiniteStateMachine.MEALY) {
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
