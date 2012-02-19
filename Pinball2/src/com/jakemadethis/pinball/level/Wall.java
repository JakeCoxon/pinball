package com.jakemadethis.pinball.level;

import static com.jakemadethis.pinball.builder.FactoryUtil.getAbsolutePosition;
import static com.jakemadethis.pinball.builder.FactoryUtil.optional;
import static com.jakemadethis.pinball.builder.FactoryUtil.toFloatList;
import static com.jakemadethis.pinball.builder.FactoryUtil.toFloatListReal;

import java.io.ObjectInputStream.GetField;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;

import com.jakemadethis.pinball.Entity;
import com.jakemadethis.pinball.EventHandler.EventListener;
import com.jakemadethis.pinball.BaseModel;
import com.jakemadethis.pinball.IDrawable;
import com.jakemadethis.pinball.GameModel;
import com.jakemadethis.pinball.IElement;
import com.jakemadethis.pinball.builder.BuilderNode;
import com.jakemadethis.pinball.builder.FactoryException;
import com.jakemadethis.pinball.builder.PinballFactory;
import com.jakemadethis.pinball.io.Input;
import com.jakemadethis.pinball.io.InputHandler;
import com.jakemadethis.pinball.io.Input.EventArgs;
import com.jakemadethis.pinball.level.Ball;

public class Wall extends Entity implements IElement, EventListener<Input.EventArgs> {
	
	public static Wall fromNode(BaseModel model, BuilderNode node) {
		HashMap<String, String> atts = node.getAttributes();
		

		float restitution = Float.valueOf( optional(atts, "restitution", "0.5") );
		String name =                      optional(atts, "name", "");
		
		
		ArrayList<Float> pathList = new ArrayList<Float>();
		
		// Make all <point> tags
		for (BuilderNode child : node.getChilds()) {
			if (!child.getNodeName().equals("point")) 
				throw new FactoryException("Invalid "+child.getNodeName()+" here");
			
			float[] pos = getAbsolutePosition(node.getParent().getValue(), child.getAttributes());
			pathList.add(pos[0]);
			pathList.add(pos[1]);
		}
		
		// Clear childs list so the factory doesn't build them
		node.getChilds().clear();
		
		// Put it into a float array
		float[] path = new float[pathList.size()];
		for (int i = 0; i < path.length; i++) 
			path[i] = pathList.get(i);

		
		Wall entity = model.addWallPath(path, restitution);
		model.setName(name, entity);
	
		return entity;
	}
	
	public Body body;
	private float restitution;
	//private float[] path;
	private ArrayList<Vector2> points;
	private LinkedList<Body> wallBodies;
	private boolean active;
	
	public Wall(World world, float[] path, float restitution) {
		if ((path.length & 1) == 1) throw new RuntimeException("Incorrect number of floats");
		
		this.active = true;
		//this.path = path;
		this.restitution = restitution;
		wallBodies = new LinkedList<Body>();
		
		points = new ArrayList<Vector2>(path.length / 2);
		for (int i = 0; i < path.length/2; i ++) {
			points.add(new Vector2(path[i*2], path[i*2+1]));
		}
		
		for (int i = 0; i < path.length - 2; i += 2) {
			Body wall = Box2DFactory.createThinWall(world, path[i], path[i+1], path[i+2], path[i+3], restitution);
			wall.setUserData(this);
			wallBodies.add(wall);
		}
		inputs = new InputHandler(this, "toggle", "disable", "enable");
	}

	public Wall(World world, float x0, float y0, float x1, float y1,
			float restitution) {
		this(world, new float[] { x0, y0, x1, y1 }, restitution);
	}

	@Override
	public <A, R> R accept(EntityVisitor<R, A> visitor, A arg) {
		return visitor.visit(this, arg);
	}
	
	public void movePoint(int pointId, float x, float y) {
		points.get(pointId).set(x, y);
	}
	public Vector2 getPoint(int pointId) {
		return points.get(pointId);
	}
	public int addPoint(int pointId, Vector2 newPoint) {
		points.add(pointId, newPoint);
		return pointId;
	}
	
	public void setActive(boolean bool) {
		for (Body body : wallBodies) {
			body.setActive(bool);
		}
		active = bool;
	}
	
	@Override
	public void invoke(Object sender, EventArgs args) {
		if (args.getInputName().equals("toggle")) {
			if (active) setActive(false);
			else setActive(true);
		}
		else if(args.getInputName().equals("disable")) {
			setActive(false);
		}
		else if(args.getInputName().equals("enable")) {
			setActive(true);
		}
	}
	
	@Override
	public String toString() {
		/*StringBuilder sb = new StringBuilder();
		sb.append(x0).append(',').append(y0).append(',')
			.append(x1).append(',').append(y1).append(',')
			.append(restitution);
		return sb.toString();*/
		return "";
	}
	
	@Override
	public int getScore() {
		return 0;
	}
	
	@Override
	public void think(float timeStep, GameModel model) {	
	}
	
	

	@Override
	public void handleCollision(Ball ball, Body body, GameModel model) {
		// TODO Auto-generated method stub
		
	}

	public ArrayList<Vector2> getPoints() {
		return points;
	}

	public boolean isActive() {
		return active;
	}

	public void reconstruct() {
		// TODO Auto-generated method stub
		
	}

	
}
