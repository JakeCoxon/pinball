package com.jakemadethis.pinball.io;

import java.util.HashMap;

import com.jakemadethis.pinball.EventHandler.EventListener;
import com.jakemadethis.pinball.io.Slot.EventArgs;

public class SlotHandler {
	private HashMap<String, Slot> slots;
	private EventListener<EventArgs> listener;
	public SlotHandler(EventListener<Slot.EventArgs> listener, String... names) {
		slots = new HashMap<String, Slot>();
		this.listener = listener;
		
		for (String name : names) {
			slots.put(name, new Slot(this, name));
		}
	}
	
	public Slot get(String inputName) {
		return slots.get(inputName);
	}
	
	public void invokeFromSignal(Object sender, EventArgs args) {
		listener.invoke(sender, args);
	}

}
