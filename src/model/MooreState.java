package model;
/**
 * Represents a Moore state
 * @author 
 *
 */
public class MooreState extends State{
	/**
	 * is the output of the state
	 */
	private String stateOutput;

	/**
	 * Constructor of the Moore state
	 * @param name the name of the state
	 * @param stateOutput the output of the state
	 */
	public MooreState(String name, String stateOutput) {
		super(name);
		this.stateOutput = stateOutput;
	}

	@Override
	public String getOutput(String input) {
		return stateOutput;
	}
	

}
