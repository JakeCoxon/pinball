package com.jakemadethis.pinball.level;

import java.util.HashMap;
import java.util.Random;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;
import com.jakemadethis.pinball.BaseModel;
import com.jakemadethis.pinball.Entity;
import com.jakemadethis.pinball.IDrawable;
import com.jakemadethis.pinball.builder.BuilderNode;
import com.jakemadethis.pinball.builder.FactoryUtil;
import com.jakemadethis.pinball.game.GameModel;

public class Ball extends Entity {
	
	public static final float BALL_RADIUS = 15f;
	
	public static Ball fromNode(BaseModel model, BuilderNode node) {
		HashMap<String, String> atts = node.getAttributes();
		float[] pos = FactoryUtil.getAbsolutePosition(node.getParent().getValue(), atts);
		return model.addBall(pos[0], pos[1], BALL_RADIUS);
	}
	
	private Body body;
	private float radius;
	private Vector2 initial;
	private Random random;
	
	public Ball(World world, float x, float y, float radius) {
		this.radius = radius;
		initial = new Vector2(x, y);
		body = Box2DFactory.createCircle(world, x, y, radius, false);
		body.setUserData(this);
		
		body.setActive(false);
		random = new Random();
	}
	
	@Override
	public <A, R> R accept(EntityVisitor<R, A> visitor, A arg) {
		return visitor.visit(this, arg);
	}

	@Override
	public void think(float timestep, GameModel model) {
	}

	public float getRadius() {
		return radius;
	}
	

	public Body getBody() {
		return body;
	}
	public boolean isActive() {
		return body.isActive();
	}

	public void launch() {
		body.setActive(true);

		float s = random.nextFloat() * 1f - 0.5f;
		body.applyLinearImpulse(new Vector2(0, -2f-s), body.getWorldCenter());
	}
	
	public void reset() {
		body.setTransform(initial, 0f);
		body.setLinearVelocity(new Vector2(0,0));
		body.setActive(false);
	}

	@Override
	public void handleCollision(Ball ball, Body body, GameModel model) {
		// TODO Auto-generated method stub
		
	}
	
	
}
