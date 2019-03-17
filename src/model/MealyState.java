package model;

import java.util.HashMap;
/**
 * Class that represents a Mealy state
 * @author 
 *
 */
public class MealyState extends State{

	private HashMap<String, String> outputFunction;
	
	public MealyState(String name) {
		super(name);
		outputFunction = new HashMap<String, String>();
		
	}
	

	/**
	 * returns the output of an input element
	 */
	@Override
	public String getOutput(String input) {
		return outputFunction.get(input);
	}
	/**
	 * adds an output element that an input element produces
	 * @param input input element of the alphabet
	 * @param output output element of the alphabet
	 */
	public void addOutput(String input, String output) {
		outputFunction.put(input, output);
	}
	/**
	 * returns the total of outputs
	 * @return int total of outputs
	 */
	public int getTotalOutputs() {
		return outputFunction.size();
	}

}
