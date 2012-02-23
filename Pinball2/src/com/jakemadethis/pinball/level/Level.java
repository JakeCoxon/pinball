package com.jakemadethis.pinball.level;

import static com.jakemadethis.pinball.builder.FactoryUtil.*;

import java.util.HashMap;

import com.jakemadethis.pinball.Attachable;
import com.jakemadethis.pinball.BaseModel;
import com.jakemadethis.pinball.builder.BuilderNode;

public class Level extends Attachable {

	public static Level fromNode(BaseModel model, BuilderNode node) {
		HashMap<String, String> atts = node.getAttributes();
		float[] size = toFloatList(expected(atts, "size"));
		String label =             expected(atts, "name");
		
		model.setSize(size[0], size[1]);
		return new Level(label, size[0], size[1]);
	}

	private String name;
	
	public Level(String name, float width, float height) {
		super(0, 0, width, height);
		this.name = name;
	}
	
	
	public String getLabel() {
		return name;
	}
	
	
}
