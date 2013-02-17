package com.jakemadethis.pinball.io;

/**
 * A connection between an input and output
 * @author Jake
 *
 */
public class Connection {
	private Slot input;
	private Signal output;
	private String[] args;

	public Connection(Slot input, Signal output, String... args) {
		this.input = input;
		this.output = output;
		this.args = args;
	}
	
	public Slot getInput() { return input; }
	public Signal getOutput() { return output; }
	
	public static Connection add(SignalHandler outputHandler, String outputName, SlotHandler target, String inputName, String... args) {
		Signal output = outputHandler.get(outputName);
		if (output == null) throw new RuntimeException(outputName+" does not exist");
		Slot input = target.get(inputName);
		if (input == null) throw new RuntimeException(inputName+" does not exist");
		return add(input, output, args);
	}
	public static Connection add(Slot input, Signal output, String... args) {
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
