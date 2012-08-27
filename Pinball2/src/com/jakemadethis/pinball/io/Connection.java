package com.jakemadethis.pinball.io;

/**
 * A connection between an input and output
 * @author Jake
 *
 */
public class Connection {
	private Input input;
	private Output output;
	private String[] args;

	public Connection(Input input, Output output, String... args) {
		this.input = input;
		this.output = output;
		this.args = args;
	}
	
	public Input getInput() { return input; }
	public Output getOutput() { return output; }
	
	public static Connection add(OutputHandler outputHandler, String outputName, InputHandler target, String inputName, String... args) {
		Output output = outputHandler.get(outputName);
		if (output == null) throw new RuntimeException(outputName+" does not exist");
		Input input = target.get(inputName);
		if (input == null) throw new RuntimeException(inputName+" does not exist");
		return add(input, output, args);
	}
	public static Connection add(Input input, Output output, String... args) {
		Connection c = new Connection(input, output, args);
		input.add(c);
		output.add(c);
		
		return c;
	}
	
	public String[] getArgs() {
		return args;
	}

	public void invoke() {
		input.invokeFromConnection(this);
	}
}
