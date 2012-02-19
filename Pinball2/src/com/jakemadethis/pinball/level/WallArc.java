package com.jakemadethis.pinball.level;

import static com.jakemadethis.pinball.builder.FactoryUtil.*;

import java.util.HashMap;
import java.util.LinkedList;

import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;
import com.jakemadethis.pinball.Attachable;
import com.jakemadethis.pinball.BaseModel;
import com.jakemadethis.pinball.builder.BuilderNode;

public class WallArc extends Wall {

	private LinkedList<Body> wallBodies;
	private float[][] lineSegments;

	public static WallArc fromNode(BaseModel model, BuilderNode node) {
		HashMap<String, String> atts = node.getAttributes();
		
		float[] position = getAbsolutePosition(node.getParent().getValue(), node.getAttributes());
		float[] size = toFloatList(expected(atts, "size"));
		float[] angles = toFloatList(expected(atts, "angles"));
		int numSegments = Integer.valueOf(optional(atts, "numSegments", "8"));

		angles[0] = (float) Math.toRadians(angles[0]);
		angles[1] = (float) Math.toRadians(angles[1]);
		
		return model.addWallArc(position[0], position[1], 
				size[0], size[1], angles[0], angles[1], 
				numSegments);
	}
	
	public WallArc(World world, float cx, float cy, float xradius, float yradius, float minangle, float maxangle, int numsegments) {
		super(world, constructPath(cx, cy, xradius, yradius, minangle, maxangle, numsegments), 0);
		wallBodies = new LinkedList<Body>();
		
	}
	
	private static float[] constructPath(float cx, float cy, float xradius, float yradius, float minangle, float maxangle, int numsegments) {
		float diff = maxangle - minangle;
		
		float[] path = new float[(numsegments+1)*2];
		for(int i=0; i < path.length; i+=2) {
			float angle1 = minangle + i/2 * diff / numsegments; 
			float x1 = cx + xradius * (float)Math.cos(angle1);
			float y1 = cy + yradius * (float)Math.sin(angle1);
			
			path[i] = x1;
			path[i+1] = y1;
		}
		return path;
	}

}
