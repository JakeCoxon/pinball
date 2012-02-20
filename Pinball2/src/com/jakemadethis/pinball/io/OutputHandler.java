package com.jakemadethis.pinball.io;

import java.util.HashMap;

public class OutputHandler {
	private HashMap<String, Output> outputs;
	private InputHandler inputHandler;
	
	public OutputHandler(String... names) {
		outputs = new HashMap<String, Output>();
		
		for (String name : names) {
			outputs.put(name, new Output(name));
		}
	}
	public void invoke(String outputName) {
		Output output = outputs.get(outputName);
		if (output == null)
			throw new RuntimeException("The output '"+outputName+"' does not exist");
		outputs.get(outputName).invoke();
	}
	/**
	 * Gets an output from a name
	 * @param outputName
	 * @return
	 */
	public Output get(String outputName) {
		return outputs.get(outputName);
	}
	
	/**
	 * Gets the related input handler
	 * This is used in the IO manager when it might want to trigger
	 * something on itself
	 * @return
	 */
	public InputHandler getRelatedInputHandler() {
		return inputHandler;
	}
	
	/**
	 * IOManager sets this
	 * @param inputHandler
	 */
	public void setRelatedInputHandler(InputHandler inputHandler) {
		this.inputHandler = inputHandler;
	}
}
