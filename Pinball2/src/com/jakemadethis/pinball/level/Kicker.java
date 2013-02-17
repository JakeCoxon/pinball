package com.jakemadethis.pinball.level;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.jakemadethis.pinball.BaseModel;
import com.jakemadethis.pinball.LevelException;
import com.jakemadethis.pinball.game.GameModel;

public class Kicker extends Wall {

	private final Vector2 p1;
	private final Vector2 p2;
	private int hits;
	private Vector2 normal;
	
	public Kicker(BaseModel model, float[] path, float restitution) {
		super(model.world, path, restitution);

		if (path.length != 4) 
			throw new LevelException("Kicker can only have 2 points");
		
		p1 = new Vector2(path[0], path[1]);
		p2 = new Vector2(path[2], path[3]);

		normal = p2.cpy().sub(p1);
		normal.set(-normal.y, normal.x);
		normal.nor();
		
		model.add(this);
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
