package com.jakemadethis.pinball.level;

import java.util.HashMap;

import com.jakemadethis.pinball.BaseModel;
import com.jakemadethis.pinball.EventHandler.EventListener;
import com.jakemadethis.pinball.builder.BuilderNode;
import com.jakemadethis.pinball.builder.FactoryUtil;
import com.jakemadethis.pinball.io.Slot.EventArgs;
import com.jakemadethis.pinball.io.SlotHandler;
import com.jakemadethis.pinball.io.SignalHandler;

public class Counter implements EventListener<EventArgs> {
	
	
	public static Counter fromNode(BaseModel model, BuilderNode node) {
		HashMap<String, String> atts = node.getAttributes();
		String name = FactoryUtil.optional(atts, "name", "");
		int max = Integer.valueOf(FactoryUtil.expected(atts, "max"));
		
		Counter c = new Counter(max);
		model.setName(name, c.inputHandler, c.outputHandler);
		return c;
	}
	
	private int max;
	private int count;
	private SlotHandler inputHandler;
	private SignalHandler outputHandler;
	

	public Counter(int max) {
		this.max = max;
		this.count = 0;
		this.inputHandler = new SlotHandler(this, "increment", "reset");
		this.outputHandler = new SignalHandler("onMax");
	}


	@Override
	public void invoke(Object sender, EventArgs args) {
		String event = args.getSlotName();
		if (event.equals("increment")) {
			increment();
		}
		else if (event.equals("reset")) {
			reset();
		}
	}


	private void reset() {
		count = 0;
	}


	private void increment() {
		count ++;
		if (count >= max) {
			count = max;
			outputHandler.invoke("onMax");
		}
	}
	
	
}
