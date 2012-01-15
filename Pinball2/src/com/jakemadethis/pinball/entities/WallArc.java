package com.jakemadethis.pinball.entities;

import java.util.LinkedList;

import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;


import com.jakemadethis.pinball.Entity;
import com.jakemadethis.pinball.GameView;
import com.jakemadethis.pinball.IDrawable;
import com.jakemadethis.pinball.IView;
import com.jakemadethis.pinball.GameModel;

public class WallArc extends WallPath implements IElement {

	private LinkedList<Body> wallBodies;
	private float[][] lineSegments;

	public WallArc(World world, float cx, float cy, float xradius, float yradius, float minangle, float maxangle, int numsegments) {
		super(world, constructPath(cx, cy, xradius, yradius, minangle, maxangle, numsegments), 0);
		wallBodies = new LinkedList<Body>();
		
	}
	
	private static float[] constructPath(float cx, float cy, float xradius, float yradius, float minangle, float maxangle, int numsegments) {
		float diff = maxangle - minangle;
		
		float[] path = new float[(numsegments+1)*2];
		for(int i=0; i<path.length; i+=2) {
			float angle1 = minangle + i/2 * diff / numsegments; 
			float x1 = cx + xradius * (float)Math.cos(angle1);
			float y1 = cy + yradius * (float)Math.sin(angle1);
			
			path[i] = x1;
			path[i+1] = y1;
		}
		return path;
	}

}
