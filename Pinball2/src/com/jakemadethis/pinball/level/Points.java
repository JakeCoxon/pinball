package com.jakemadethis.pinball.level;

import java.util.HashMap;

import com.jakemadethis.pinball.BaseModel;
import com.jakemadethis.pinball.EventHandler.EventListener;
import com.jakemadethis.pinball.builder.BuilderNode;
import com.jakemadethis.pinball.builder.FactoryUtil;
import com.jakemadethis.pinball.io.Slot.EventArgs;
import com.jakemadethis.pinball.io.SlotHandler;
import com.jakemadethis.pinball.io.SignalHandler;

public class Points implements EventListener<EventArgs> {
	
	private SlotHandler inputHandler;
	private int numPoints;
	private BaseModel model;
	

	public Points(BaseModel model, int numPoints) {
		this.model = model;
		this.numPoints = numPoints;
		this.inputHandler = new SlotHandler(this, "increment");
		
	}


	@Override
	public void invoke(Object sender, EventArgs args) {
		String event = args.getSlotName();
		if (event.equals("increment")) {
			increment();
		}
	}


	private void increment() {
		model.addScore(numPoints);
	}
	
	
}
