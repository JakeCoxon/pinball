package com.jakemadethis.pinball.level;


import java.util.HashMap;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;
import com.jakemadethis.pinball.BaseModel;
import com.jakemadethis.pinball.Entity;
import com.jakemadethis.pinball.EventHandler.EventListener;
import com.jakemadethis.pinball.IElement;
import com.jakemadethis.pinball.builder.BuilderNode;
import com.jakemadethis.pinball.builder.FactoryUtil;
import com.jakemadethis.pinball.game.GameModel;
import com.jakemadethis.pinball.io.Input;
import com.jakemadethis.pinball.io.Input.EventArgs;
import com.jakemadethis.pinball.io.InputHandler;


public class Bumper extends Entity implements IElement, EventListener<Input.EventArgs> {
	
	
	/**
	 * Creates a bumper from attributes at,radius[,name]
	 * @param model
	 * @param atts
	 * @return
	 */
	public static Bumper fromNode(BaseModel model, BuilderNode node) {
		HashMap<String, String> atts = node.getAttributes();
		
		float[] at = 	FactoryUtil.toPosition( FactoryUtil.expected(atts, "at") );
		float radius = 			 Float.valueOf( FactoryUtil.expected(atts, "radius") );
		String name = 						    FactoryUtil.optional(atts, "name", "");
	
		Bumper entity = model.addBumper(at[0], at[1], radius);
		model.setName(name, entity);
		
		return entity;
	}
	
	
	public Body body;
	private float cx;
	private float cy;
	private float radius;
	private int hits = 0;
	private final float kick = 0.5f;
	
	private static String INPUT_TOGGLE = "toggle";

	
	public Bumper(World world, float cx, float cy, float radius) {
		body = Box2DFactory.createCircle(world, cx, cy, radius, true);
		body.setUserData(this);
		this.cx = cx;
		this.cy = cy;
		this.radius = radius;
		
		
		inputs = new InputHandler(this, INPUT_TOGGLE);
	}
	
	public void setX(float x) {
		this.cx = x;
	}
	public void setY(float y) {
		this.cy = y;
	}
	public void setRadius(float radius) {
		this.radius = radius;
	}
	public float getX() { return cx; }
	public float getY() { return cy; }
	public float getRadius() { return radius; }
	
	@Override
	public <A, R> R accept(EntityVisitor<R, A> visitor, A arg) {
		return visitor.visit(this, arg);
	}
	
	@Override
	public void invoke(Object sender, EventArgs args) {
		if (args.getInputName().equals(INPUT_TOGGLE)) {
			System.out.println("Bumpers toggle function invoked");
		}
	}

	@Override
	public int getScore() {
		return 10;
	}
	
	@Override
	public void think(float timeStep, GameModel model) {
	}


	@Override
	public void handleCollision(Ball ball, Body body, GameModel model) {
		hits ++;
		Vector2 impulse = impulseForBall(ball.getBody());
		ball.getBody().setLinearVelocity(0, 0);
		if (impulse != null)
			ball.getBody().applyLinearImpulse(impulse, ball.getBody().getWorldCenter());
	}
	
	private Vector2 impulseForBall(Body ball) {
		if (this.kick <= 0.01f) return null;
		Vector2 ballpos = ball.getWorldCenter();
		Vector2 thisPos = body.getPosition();
		Vector2 impulse = ballpos.cpy().sub(thisPos).nor().mul(kick);
		return impulse;
	}

	public int getHits() {
		return hits;
	}

}
