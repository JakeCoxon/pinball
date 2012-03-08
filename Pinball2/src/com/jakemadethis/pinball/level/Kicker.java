package com.jakemadethis.pinball.level;

import static com.jakemadethis.pinball.builder.FactoryUtil.getAbsolutePosition;
import static com.jakemadethis.pinball.builder.FactoryUtil.optional;

import java.util.ArrayList;
import java.util.HashMap;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;
import com.jakemadethis.pinball.BaseModel;
import com.jakemadethis.pinball.LevelException;
import com.jakemadethis.pinball.builder.BuilderNode;
import com.jakemadethis.pinball.builder.FactoryException;
import com.jakemadethis.pinball.game.GameModel;

public class Kicker extends Wall {
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
		
		if (pathList.size() != 4) 
			throw new LevelException("Kicker can only have 2 points");
		
		// Clear childs list so the factory doesn't build them
		node.getChilds().clear();
		
		// Put it into a float array
		float[] path = new float[pathList.size()];
		for (int i = 0; i < path.length; i++) 
			path[i] = pathList.get(i);

		
		Kicker entity = model.addKicker(path, restitution);
		model.setName(name, entity);
	
		return entity;
	}

	private final Vector2 p1;
	private final Vector2 p2;
	private int hits;
	private Vector2 normal;
	
	public Kicker(World world, float[] path, float restitution) {
		super(world, path, restitution);

		if (path.length != 4) 
			throw new LevelException("Kicker can only have 2 points");
		
		p1 = new Vector2(path[0], path[1]);
		p2 = new Vector2(path[2], path[3]);

		normal = p2.cpy().sub(p1);
		normal.set(-normal.y, normal.x);
		normal.nor();
	}
	
	@Override
	public int getScore() {
		return 10;
	}
	
	@Override
	public <A, R> R accept(EntityVisitor<R, A> visitor, A arg) {
		return visitor.visit(this, arg);
	}
	
	@Override
	public void handleCollision(Ball ball, Body body, GameModel model) {
		hits++;
		Vector2 ballpos = ball.getBody().getPosition();
		normal = p2.cpy().sub(p1);
		if (ballpos.crs(normal) < 0)
			normal.set(normal.y, -normal.x);
		else
			normal.set(-normal.y, normal.x);
		normal.nor();
		normal.y -= 0.2f;
		normal.nor();
		
		
		Vector2 mul = normal.cpy().mul(15f);
		ball.getBody().setLinearVelocity(0, 0);
		ball.getBody().setLinearVelocity(mul);
	}
	
	public int getHits() {
		return hits;
	}
	
	/**
	 * Gets the normal in the direction the ball last hit the kicker
	 * @return
	 */
	public Vector2 getNormal() {
		return normal;
	}
}
