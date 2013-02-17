package com.jakemadethis.pinball.level;

import java.util.LinkedList;

import com.jakemadethis.pinball.Attachable;
import com.jakemadethis.pinball.BaseModel;
import com.jakemadethis.pinball.builder.BuilderNode;
import com.jakemadethis.pinball.builder.FactoryException;
import com.jakemadethis.pinball.builder.FactoryUtil;

public class Frame extends Attachable {

	
	public static Frame fromNode(BaseModel model, BuilderNode node) {
		float[] topLeft = null;
		float[] bottomRight = null;
		
		LinkedList<BuilderNode> newChildren = null;
		
		// Loop through <frame>'s children
		for (BuilderNode child : node.getChilds()) {
			
			// <topLeft>
			if (child.getNodeName().equals("topLeft")) {
				if (topLeft != null) 
					throw new FactoryException("Not allowed multiple topLeft tags");
				
				topLeft = FactoryUtil.getAbsolutePosition(node.getParent().getValue(), child.getAttributes());
			}
			
			// <bottomRight>
			else if (child.getNodeName().equals("bottomRight")) {
				if (bottomRight != null) 
					throw new FactoryException("Not allowed multiple bottomRight tags");
				
				bottomRight = FactoryUtil.getAbsolutePosition(node.getParent().getValue(), child.getAttributes());
			}
			
			// <children>
			else if (child.getNodeName().equals("children")) {
				
				newChildren = child.getChilds();
				
			}
			
			// Other
			else {
				throw new FactoryException("Unexpected "+child.getNodeName()+" tag");
			}
		}
		
		// Clear children so factory doesn't try to create them
		node.getChilds().clear();
		
		if (newChildren != null) {
			// Move children from <children> to <frame>
			// This is a bit of a hack but it is to make the factory
			// build the entities with <frame> as the parent
			for (BuilderNode child : newChildren) {
				child.setParent(node);
				node.getChilds().add(child);
			}
		}
		
		
		if (topLeft == null) throw new FactoryException("Expected topLeft tag");
		if (bottomRight == null) throw new FactoryException("Expected bottomRight tag");
		
		return new Frame(topLeft[0], topLeft[1], 
				bottomRight[0] - topLeft[0], bottomRight[1] - topLeft[1]);
	}
	
	public Frame(Attachable rel_to) {
		super(rel_to);
	}
	
	public Frame(float x, float y, float width, float height) {
		super(x, y, width, height);
	}

}
