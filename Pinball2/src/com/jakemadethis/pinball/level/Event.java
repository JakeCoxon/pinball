package com.jakemadethis.pinball.level;

import java.util.HashMap;

import static com.jakemadethis.pinball.builder.FactoryUtil.*;
import com.jakemadethis.pinball.BaseModel;
import com.jakemadethis.pinball.LevelException;
import com.jakemadethis.pinball.builder.BuilderNode;
import com.jakemadethis.pinball.io.IOException;

public abstract class Event {

	public static Object fromNode(BaseModel model, BuilderNode node) {
		
		HashMap<String, String> atts = node.getAttributes();
		String _for = expected(atts, "for");
		String when = expected(atts, "when");
		String target = expected(atts, "target");
		String action = expected(atts, "action");
		float wait = Float.valueOf(optional(atts, "wait", "0"));
		
		try {
			model.getIoManager().addEvent(_for, when, target, action);
		}
		catch(IOException e) {
			throw new LevelException("Event couldn't be created", e);
		}
		
		return null;		
	}
	
}
