package com.jakemadethis.pinball.io;

import java.util.ArrayList;
import java.util.HashMap;

import com.jakemadethis.pinball.Entity;

public class IOManager {
		
	
	private HashMap<String, ArrayList<InputHandler>> inputsMap;
	private HashMap<String, ArrayList<OutputHandler>> outputsMap;
	
	public IOManager() {
		inputsMap = new HashMap<String, ArrayList<InputHandler>>();
		outputsMap = new HashMap<String, ArrayList<OutputHandler>>();
	}

	/**
	 * Add an  Input&Output Handlers to the IOmanager
	 * @param fullName
	 * @param entity
	 */
	public void add(String fullName, Entity entity) {
		add(fullName, entity.inputs, entity.outputs);
	}
	
	/**
	 * Add an input&output handler to the IOmanager
	 * @param fullName
	 * @param inputHandler
	 * @param outputHandler
	 */
	public void add(String fullName, InputHandler inputHandler, OutputHandler outputHandler) {
		
		Integer id = null;
		try { id = Pattern.matchID(fullName); }
		catch (NumberFormatException ex) {}
		
		String name = Pattern.matchName(fullName);
		
		if (inputHandler != null) {
			ArrayList<InputHandler> inputs;
			if (!inputsMap.containsKey(name))
				inputsMap.put(name, inputs = new ArrayList<InputHandler>());
			else
				inputs = inputsMap.get(name);
			
			set(inputs, id, inputHandler);
		}
		
		if (outputHandler != null) {
			ArrayList<OutputHandler> outputs;
			if (!outputsMap.containsKey(name))
				outputsMap.put(name, outputs = new ArrayList<OutputHandler>());
			else
				outputs = outputsMap.get(name);		
			
			set(outputs, id, outputHandler);
			
			outputHandler.setRelatedInputHandler(inputHandler);
		}
	}
	
	private <T> void set(ArrayList<T> list, Integer id, T handler) {
		if (id != null) {
			while (id >= list.size()) list.add(null);
			list.set(id, handler);
		}
		else
			list.add(handler);
	}
	
	public void addEvent(String forName, String eventName, String targetName, String action) {
		ArrayList<OutputHandler> collection = getOutput(forName);
		System.out.println("Found "+collection.size()+" OutputHandlers for "+forName);
		boolean self = targetName.equals("#self");
		for (OutputHandler outputHandler : collection) {
			if (self) addEvent(outputHandler, eventName, outputHandler.getRelatedInputHandler(), action);
			else {
				for (InputHandler target : getInput(targetName))
					addEvent(outputHandler, eventName, target, action);
			}
		}
	}
	private static void addEvent(OutputHandler output, String eventName, InputHandler target, String action) {
		System.out.println("Add event "+eventName+" -> "+action);
		Connection.add(output, eventName, target, action);
	}
	
	/**
	 * Gets inputs matching fullName. Note there may be gaps in the array
	 * @param fullName
	 * @return
	 */
	public ArrayList<InputHandler> getInput(String fullName) {
		return Pattern.match(inputsMap, fullName);
	}
	/**
	 * Gets outputs matching fullName. Note there may be gaps in the array
	 * @param fullName
	 * @return
	 */
	public ArrayList<OutputHandler> getOutput(String fullName) {
		return Pattern.match(outputsMap, fullName);
	}

	public void debugPrint() {
		System.out.println("Inputs");
		for (String str : inputsMap.keySet()) {
			//System.out.println(" "+str);
			ArrayList<InputHandler> list = inputsMap.get(str);
			for (int i = 0; i < list.size(); i++) {
				if (list.get(i) != null)
					System.out.println(" "+str+"-"+i);
			}
		}
		System.out.println("Outputs");
		for (String str : outputsMap.keySet()) {
			//System.out.println(" "+str);
			ArrayList<OutputHandler> list = outputsMap.get(str);
			for (int i = 0; i < list.size(); i++) {
				if (list.get(i) != null)
					System.out.println(" "+str+"-"+i);
			}
		}
	}

	public void testAdd() {
		InputHandler input = new InputHandler(null);
		OutputHandler output = new OutputHandler();
		add("test", input, output);
		add("test", input, output);
		add("test", input, output);
		add("test", input, output);
		add("test-0", input, output);
		//add("test-4", input, output);

		add("something-4", input, output);
		add("something", input, output);
		add("something", input, output);
		add("something", input, output);
		add("something", input, output);
		add("something", input, output);

		add("blah-3", input, output);
		add("blah-2", input, output);
		add("blah-1", input, output);
	}
	
}
