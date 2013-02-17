package com.jakemadethis.pinball.level;

import java.util.HashMap;
import java.util.Random;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;
import com.jakemadethis.pinball.BaseModel;
import com.jakemadethis.pinball.Entity;
import com.jakemadethis.pinball.builder.BuilderNode;
import com.jakemadethis.pinball.builder.FactoryUtil;
import com.jakemadethis.pinball.game.GameModel;

public class Ball extends Entity {
	
	public static final float BALL_RADIUS = 15f;
	
	private final Body body;
	private final float radius;
	private final Random random;
	
	public Ball(BaseModel model, float x, float y, float radius) {
		this.radius = radius;
		body = Box2DFactory.createCircle(model.world, x, y, radius, false);
		body.setUserData(this);
		
		body.setActive(false);
		random = new Random();
		
		model.add(this);
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
	
	public void setPos(Vector2 pos) {
		body.setTransform(pos, 0f);
	}
	public Vector2 getPos() {
		return body.getPosition();
	}
	
	public void reset() {
		//body.setTransform(initial, 0f);
		body.setLinearVelocity(new Vector2(0,0));
		body.setActive(false);
	}

	@Override
	public void handleCollision(Ball ball, Body body, GameModel model) {
		// TODO Auto-generated method stub
		
	}
	
	
}
