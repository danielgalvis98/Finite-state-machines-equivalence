package model;

import java.util.HashMap;

public class MealyState extends State{

	private HashMap<String, String> outputFunction;
	
	public MealyState(String name) {
		super(name);
		outputFunction = new HashMap<String, String>();
		
	}
	

	@Override
	public String getOutput(String input) {
		return outputFunction.get(input);
	}
	
	public void addOutput(String input, String output) {
		outputFunction.put(input, output);
	}
	
	public int getTotalOutputs() {
		return outputFunction.size();
	}

}
