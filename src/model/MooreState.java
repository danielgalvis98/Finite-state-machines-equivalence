package model;

public class MooreState extends State{
	private String stateOutput;

	public MooreState(String name, String stateOutput) {
		super(name);
		this.stateOutput = stateOutput;
	}

	@Override
	public String getOutput(String input) {
		return stateOutput;
	}
	

}
