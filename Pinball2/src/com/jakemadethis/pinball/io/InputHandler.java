package com.jakemadethis.pinball.io;

import java.util.HashMap;

import com.jakemadethis.pinball.EventHandler.EventListener;
import com.jakemadethis.pinball.io.Input.EventArgs;

public class InputHandler {
	private HashMap<String, Input> inputs;
	private EventListener<EventArgs> listener;
	public InputHandler(EventListener<Input.EventArgs> listener, String... names) {
		inputs = new HashMap<String, Input>();
		this.listener = listener;
		
		for (String name : names) {
			inputs.put(name, new Input(this, name));
		}
	}
	
	public Input get(String inputName) {
		return inputs.get(inputName);
	}
	
	public void invokeFromInput(Object sender, EventArgs args) {
		listener.invoke(sender, args);
	}

}
