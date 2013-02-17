package com.jakemadethis.pinball.level;

import static com.jakemadethis.pinball.builder.FactoryUtil.getAbsolutePosition;
import static com.jakemadethis.pinball.builder.FactoryUtil.optional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;
import com.jakemadethis.pinball.BaseModel;
import com.jakemadethis.pinball.Entity;
import com.jakemadethis.pinball.EventHandler.EventListener;
import com.jakemadethis.pinball.IElement;
import com.jakemadethis.pinball.builder.BuilderNode;
import com.jakemadethis.pinball.builder.FactoryException;
import com.jakemadethis.pinball.game.GameModel;
import com.jakemadethis.pinball.io.Slot;
import com.jakemadethis.pinball.io.Slot.EventArgs;
import com.jakemadethis.pinball.io.SlotHandler;

public class Wall extends Entity implements IElement, EventListener<Slot.EventArgs> {
	
	public Body body;
	//private float[] path;
	private final ArrayList<Vector2> points;
	private final LinkedList<Body> wallBodies;
	private boolean active;
	
	public Wall(World world, float[] path, float restitution) {
		if ((path.length & 1) == 1) throw new RuntimeException("Incorrect number of floats");
		
		this.active = true;
		//this.path = path;
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
		slots = new SlotHandler(this, "toggle", "disable", "enable");
	}
	
	private Wall(BaseModel model, ArrayList<Vector2> points) {
		this.active = true;
		this.points = points;
		this.wallBodies = new LinkedList<Body>();
		
		for (int i = 0; i < points.size()-1; i ++) {
			Vector2 a = points.get(i);
			Vector2 b = points.get(i+1);
			Body wall = Box2DFactory.createThinWall(model.world, a.x, a.y, b.x, b.y, 0.8f);
			wall.setUserData(this);
			wallBodies.add(wall);
		}

		slots = new SlotHandler(this, "toggle", "disable", "enable");
		
		model.add(this);
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
		if (args.getSlotName().equals("toggle")) {
			if (active) setActive(false);
			else setActive(true);
		}
		else if(args.getSlotName().equals("disable")) {
			setActive(false);
		}
		else if(args.getSlotName().equals("enable")) {
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
	
	public static class Builder {
		
		private static class Point {
			public Vector2 vec;

			public Point(float x, float y) {
				this.vec = new Vector2(x, y);
			}
		}
		private static class Arc extends Point {
			public final float radius;

			public Arc(float x, float y, float radius) {
				super(x, y);
				this.radius = radius;
			}
		}
		
		private static final int SEGMENTS = 8;
		private ArrayList<Vector2> path = null;
		private ArrayList<Point> points;
		
		public Builder(boolean looped) {
			points = new ArrayList<Wall.Builder.Point>();
		}
		
		public void addPoint(float x, float y) {
			System.out.println("Add point");
			points.add(new Point(x, y));
		}
		
		public void addArc(float x, float y, float radius) {
			System.out.println("Add arc");
			points.add(new Arc(x, y, radius));
		}
		
		public Wall constructWall(BaseModel model) {
			path = new ArrayList<Vector2>();
			
			for (int i = 0; i < points.size(); i++) {
				Point point = points.get(i);
				
				// Arc
				if (point instanceof Arc)
					addArc(points.get(i-1).vec, point.vec, points.get(i+1).vec, ((Arc) point).radius);
				
				// Point
				else
					path.add(point.vec);
			}
			
			return new Wall(model, path);
		}
		
		private void addArc(Vector2 p0, Vector2 p1, Vector2 p2, float r) {
			
			Vector2 p10 = p0.cpy().sub(p1).nor();
			Vector2 p12 = p2.cpy().sub(p1).nor();
			
			Vector2 mid = p12.cpy().add(p10).nor();
			
			
			float cross = p10.crs(mid);
			float h = r / Math.abs(cross);

			
			Vector2 c = mid.cpy().mul(h).add(p1);
			
			// Normals to the two vectors
			Vector2 p12normal = new Vector2(-p12.y, p12.x);
			Vector2 p10normal = new Vector2(p10.y, -p10.x);
			
			// Normals are backwards depending on the direction of the point
			if (cross < 0) {
				p12normal.mul(-1f);
				p10normal.mul(-1f);
			}
			Vector2 a = p10normal.cpy().mul(r).add(c);
			Vector2 b = p12normal.cpy().mul(r).add(c);
			
			path.add(a.cpy());
			addArcInternal(c.x, c.y, r, p10normal, p12normal);
			path.add(b.cpy());
			
			
		}
		
		private void addArcInternal(float cx, float cy, float radius, Vector2 startDir, Vector2 endDir) {
			Vector2 normal = startDir.cpy();
			
			float cross = startDir.crs(endDir); // sin theta
			float dot = startDir.dot(endDir);		// cos theta

			double angle =  (Math.acos(dot) / SEGMENTS);
			if (cross < 0) angle = Math.PI*2 - angle;
			
			float sin = (float) Math.sin(angle);
			float cos = (float) Math.cos(angle);
			
			for(int i=0; i < SEGMENTS; i++) {

				float oldx = normal.x;
				float oldy = normal.y;
				normal.x = oldx * cos - oldy * sin;
				normal.y = oldx * sin + oldy * cos;
				normal.nor();

				float x = cx + normal.x * radius;
				float y = cy + normal.y * radius;
				
				path.add(new Vector2(x, y));
				
			}
		}
	}

	
}
