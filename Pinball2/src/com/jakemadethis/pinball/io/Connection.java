package com.jakemadethis.pinball.io;

/**
 * A connection between an input and output
 * @author Jake
 *
 */
public class Connection {
	private Input input;
	private Output output;

	public Connection(Input input, Output output) {
		this.input = input;
		this.output = output;
	}
	
	public Input getInput() { return input; }
	public Output getOutput() { return output; }
	
	public static Connection add(OutputHandler outputHandler, String outputName, InputHandler target, String inputName) {
		Output output = outputHandler.get(outputName);
		if (output == null) throw new RuntimeException(outputName+" does not exist");
		Input input = target.get(inputName);
		if (input == null) throw new RuntimeException(inputName+" does not exist");
		return add(input, output);
	}
	public static Connection add(Input input, Output output) {
		Connection c = new Connection(input, output);
		input.add(c);
		output.add(c);
		
		return c;
	}

	public void invoke() {
		input.invoke(this);
	}
}
