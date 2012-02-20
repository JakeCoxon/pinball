package com.jakemadethis.pinball.level;

import java.util.HashMap;

import com.jakemadethis.pinball.BaseModel;
import com.jakemadethis.pinball.EventHandler.EventListener;
import com.jakemadethis.pinball.builder.BuilderNode;
import com.jakemadethis.pinball.builder.FactoryUtil;
import com.jakemadethis.pinball.io.Input.EventArgs;
import com.jakemadethis.pinball.io.InputHandler;
import com.jakemadethis.pinball.io.OutputHandler;

public class Points implements EventListener<EventArgs> {
	
	
	public static Points fromNode(BaseModel model, BuilderNode node) {
		HashMap<String, String> atts = node.getAttributes();
		String name = FactoryUtil.optional(atts, "name", "");
		int number = Integer.valueOf(FactoryUtil.expected(atts, "number"));
		
		Points c = new Points(model, number);
		model.setName(name, c.inputHandler, null);
		
		return c;
	}
	
	private InputHandler inputHandler;
	private int numPoints;
	private BaseModel model;
	

	public Points(BaseModel model, int numPoints) {
		this.model = model;
		this.numPoints = numPoints;
		this.inputHandler = new InputHandler(this, "increment");
	}


	@Override
	public void invoke(Object sender, EventArgs args) {
		String event = args.getInputName();
		if (event.equals("increment")) {
			increment();
		}
	}


	private void increment() {
		model.addScore(numPoints);
	}
	
	
}
