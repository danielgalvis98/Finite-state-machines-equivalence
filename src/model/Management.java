package model;

import java.util.ArrayList;
/**
 * Is the class that manages the states machines
 * @author 
 *
 */
public class Management {

	/**
	 * Is the machine that results of the sum direct machine
	 */
	private FiniteStateMachine directSumMachine;
	/**
	 * The first machine
	 */
	private FiniteStateMachine m1; 
	/**
	 * The second machine
	 */
	private FiniteStateMachine m2;
	/**
	 * Constructor for the management class
	 * @param inputAlphabet the input alphabet of the machines
	 * @param outputAlphabet the output alphabet of the machines
	 * @param type the type of the machines
	 * @param totStatesMachine1 total states of the machine 1
	 * @param totStatesMachine2 total states of the machine 2
	 */
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
	
	//juanma
	/**
	 * returns the machine 1
	 * @return FiniteStateMachine
	 */
	public FiniteStateMachine getMachine1() {
		return m1;
	}
	/**
	 * returns the machine 2
	 * @return FiniteStateMachine
	 */
	public FiniteStateMachine getMachine2() {
		return m2;
	}
	//
	/**
	 * returns the total of states in machine 1
	 * @return int total of states in machine 1
	 */
	public int getTotalStatesm1 () {
		return m1.totStates;
	}
	/**
	 * returns the total of states in machine 2
	 * @return int total of states in machine 2
	 */
	public int getTotalStatesm2 () {
		return m2.totStates;
	}
	/**
	 * renames the states and does the direct sum of the machines
	 */
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
	/**
	 * it verifies the equivalence of the two machines
	 * @return true if the two machines are equivalent, false in other case
	 */
	public boolean verifyEquivalenceByMachine () {
		renameStatesAndDoDirectSum();
		ArrayList<ArrayList<State>> partitions = directSumMachine.obtaintPartitions();
		for (int i = 0; i < partitions.size(); i++) {
			System.out.println("");
			for (int j = 0; j < partitions.get(i).size(); j++) {
				System.out.print(partitions.get(i).get(j).getName() + "");
			}
		}
		boolean containM1 = false;		
		boolean containM2 = false;		
		boolean checker = false;
		for (int i = 0; i < partitions.size() && checker; i++) {
			containM1 = false;	
			containM2 = false;	
			for (int j = 0; j < partitions.get(i).size() && !containM1 || containM2; j++) {
				State state = partitions.get(i).get(j);			
				if(m1.getStates().indexOf(state) != -1 && !containM1) {
					containM1 = true;
				}	
				if(m2.getStates().indexOf(state) != -1 && !containM2) {
					containM2 = true;
				}
			}
			
			checker = containM1 && containM2;
		}
		return checker;
	}

	

}
