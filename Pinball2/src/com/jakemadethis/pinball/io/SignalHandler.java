package com.jakemadethis.pinball.io;

import java.util.HashMap;

public class SignalHandler {
	private HashMap<String, Signal> signals;
	private SlotHandler slotHandler;
	
	public SignalHandler(String... names) {
		signals = new HashMap<String, Signal>();
		
		for (String name : names) {
			signals.put(name, new Signal(name));
		}
	}
	public void invoke(String signalName) {
		Signal output = signals.get(signalName);
		if (output == null)
			throw new RuntimeException("The output '"+signalName+"' does not exist");
		signals.get(signalName).invoke();
	}
	
	/**
	 * Gets a signal from a name
	 * @param signalName
	 * @return
	 */
	public Signal get(String signalName) {
		return signals.get(signalName);
	}
	
	/**
	 * Gets the related input handler
	 * This is used in the IO manager when it might want to trigger
	 * something on itself
	 * @return
	 */
	public SlotHandler getRelatedSlotHandler() {
		return slotHandler;
	}
	
	/**
	 * IOManager sets this
	 * @param slotHandler
	 */
	public void setRelatedSlotHandler(SlotHandler slotHandler) {
		this.slotHandler = slotHandler;
	}
}
