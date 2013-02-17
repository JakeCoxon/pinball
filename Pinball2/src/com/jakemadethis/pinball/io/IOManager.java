package com.jakemadethis.pinball.io;

import java.util.ArrayList;
import java.util.HashMap;

import com.jakemadethis.pinball.Entity;

public class IOManager {
		
	
	private HashMap<String, ArrayList<SlotHandler>> inputsMap;
	private HashMap<String, ArrayList<SignalHandler>> outputsMap;
	
	public IOManager() {
		inputsMap = new HashMap<String, ArrayList<SlotHandler>>();
		outputsMap = new HashMap<String, ArrayList<SignalHandler>>();
	}

	/**
	 * Add an  Input&Output Handlers to the IOmanager
	 * @param fullName
	 * @param entity
	 */
	public void add(String fullName, Entity entity) {
		add(fullName, entity.getSlots(), entity.getSignals());
	}
	
	/**
	 * Add an input&output handler to the IOmanager
	 * @param fullName
	 * @param inputHandler
	 * @param outputHandler
	 */
	public void add(String fullName, SlotHandler inputHandler, SignalHandler outputHandler) {
		
		Integer id = null;
		try { id = Pattern.matchID(fullName); }
		catch (NumberFormatException ex) {}
		
		String name = Pattern.matchName(fullName);
		
		if (inputHandler != null) {
			ArrayList<SlotHandler> inputs;
			if (!inputsMap.containsKey(name))
				inputsMap.put(name, inputs = new ArrayList<SlotHandler>());
			else
				inputs = inputsMap.get(name);
			
			set(inputs, id, inputHandler);
		}
		
		if (outputHandler != null) {
			ArrayList<SignalHandler> outputs;
			if (!outputsMap.containsKey(name))
				outputsMap.put(name, outputs = new ArrayList<SignalHandler>());
			else
				outputs = outputsMap.get(name);
			
			set(outputs, id, outputHandler);
			
			outputHandler.setRelatedSlotHandler(inputHandler);
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
	
	public void addEvent(String forName, String eventName, String targetName, String actionString) {
		ArrayList<SignalHandler> collection = getOutput(forName);
		if (collection == null) 
			throw new IOException("Couldn't find any entities matching '"+forName+"'");
		System.out.println("Found "+collection.size()+" OutputHandlers for "+forName);
		
		boolean self = targetName.equals("#self");
		
		for (int i = 0; i < collection.size(); i++) {
			SignalHandler outputHandler = collection.get(i);
			
			if (self) {
				addEvent(outputHandler, eventName, outputHandler.getRelatedSlotHandler(), actionString);
			}
			else {
				ArrayList<SlotHandler> targets = getInputReplaced(targetName, i);
				if (targets == null) {
					throw new IOException("Couldn't find any entities matching '"+targetName+"'");
				}
				for (SlotHandler target : targets)
					addEvent(outputHandler, eventName, target, actionString);
			}
		}
	}
	private static void addEvent(SignalHandler output, String eventName, SlotHandler target, String actionString) {
		System.out.println("Add event "+eventName+" -> "+actionString);
		String[] split = actionString.split(",");
		String action = split[0];
		
		// Copy 1..n-1 to args
		String[] args = new String[split.length-1];
		if (args.length > 0)
			System.arraycopy(split, 1, args, 0, split.length-1);
		
		Connection.add(output, eventName, target, action, args);
	}
	
	
	public HashMap<String, ArrayList<SlotHandler>> getAllInputs() {
		return inputsMap;
	}
	public HashMap<String, ArrayList<SignalHandler>> getAllOutputs() {
		return outputsMap;
	}
	
	/**
	 * Gets inputs matching fullName. Note there may be gaps in the array
	 * @param fullName
	 * @return
	 */
	public ArrayList<SlotHandler> getInput(String fullName) {
		return Pattern.match(inputsMap, fullName);
	}
	
	/**
	 * Gets inputs matching fullName with # replaced with number
	 * @param fullName
	 * @param number
	 * @return ArrayList of InputHandler's - Note there may be gaps in the array
	 */
	public ArrayList<SlotHandler> getInputReplaced(String fullName, int number) {
		return getInput(fullName.replace("#", String.valueOf(number)));
	}
	
	
	
	/**
	 * Gets the whole array of inputs in a group
	 * @param groupName
	 * @return
	 */
	public ArrayList<SlotHandler> getInputGroup(String groupName) {
		return inputsMap.get(groupName);
	}
	
	/**
	 * Gets outputs matching fullName. Note there may be gaps in the array
	 * @param fullName
	 * @return
	 */
	public ArrayList<SignalHandler> getOutput(String fullName) {
		return Pattern.match(outputsMap, fullName);
	}
	
	/**
	 * Gets the whole array of outputs in a group
	 * @param groupName
	 * @return
	 */
	public ArrayList<SignalHandler> getOutputGroup(String groupName) {
		return outputsMap.get(groupName);
	}

	public void debugPrint() {
		System.out.println("Inputs");
		for (String str : inputsMap.keySet()) {
			//System.out.println(" "+str);
			ArrayList<SlotHandler> list = inputsMap.get(str);
			for (int i = 0; i < list.size(); i++) {
				if (list.get(i) != null)
					System.out.println(" "+str+"-"+i);
			}
		}
		System.out.println("Outputs");
		for (String str : outputsMap.keySet()) {
			//System.out.println(" "+str);
			ArrayList<SignalHandler> list = outputsMap.get(str);
			for (int i = 0; i < list.size(); i++) {
				if (list.get(i) != null)
					System.out.println(" "+str+"-"+i);
			}
		}
	}

	public void testAdd() {
		SlotHandler input = new SlotHandler(null);
		SignalHandler output = new SignalHandler();
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
