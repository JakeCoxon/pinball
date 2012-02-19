package com.jakemadethis.pinball.level;

import java.util.HashMap;

import static com.jakemadethis.pinball.builder.FactoryUtil.*;
import com.jakemadethis.pinball.BaseModel;
import com.jakemadethis.pinball.builder.BuilderNode;

public abstract class Event {

	public static Object fromNode(BaseModel model, BuilderNode node) {
		
		HashMap<String, String> atts = node.getAttributes();
		String _for = expected(atts, "for");
		String when = expected(atts, "when");
		String target = expected(atts, "target");
		String action = expected(atts, "action");
		float wait = Float.valueOf(optional(atts, "wait", "0"));
		
		model.getIoManager().addEvent(_for, when, target, action);
		
		return null;		
	}
	
}
