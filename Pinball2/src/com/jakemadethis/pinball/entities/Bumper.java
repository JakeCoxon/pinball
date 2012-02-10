package com.jakemadethis.pinball.entities;


import java.util.Random;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;
import com.jakemadethis.pinball.Entity;
import com.jakemadethis.pinball.EventHandler;
import com.jakemadethis.pinball.GameView;
import com.jakemadethis.pinball.IDrawable;
import com.jakemadethis.pinball.IView;
import com.jakemadethis.pinball.GameModel;
import com.jakemadethis.pinball.Timer;
import com.jakemadethis.pinball.io.Input;
import com.jakemadethis.pinball.io.InputHandler;
import com.jakemadethis.pinball.io.Input.EventArgs;
import com.jakemadethis.pinball.EventHandler.EventListener;


public class Bumper extends Entity implements IElement, EventListener<Input.EventArgs> {
	
	public Body body;
	private float cx;
	private float cy;
	private float radius;
	private int hits = 0;
	private float kick = 1f;
	
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
	public <A> IDrawable accept(EntityVisitor<IDrawable, A> visitor, A arg) {
		return visitor.visit(this, arg);
	}
	
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
		ball.getBody().applyLinearImpulse(impulse, ball.getBody().getWorldCenter());
	}
	
	private Vector2 impulseForBall(Body ball) {
		if (this.kick <= 0.01f) return null;
		// compute unit vector from center of peg to ball, and scale by kick value to get impulse
		Vector2 ballpos = ball.getWorldCenter();
		Vector2 thisPos = body.getPosition();
		float ix = ballpos.x - thisPos.x;
		float iy = ballpos.y - thisPos.y;
		float mag = (float)Math.sqrt(ix*ix + iy*iy);
		float scale = this.kick  / mag;
		return new Vector2(ix*scale, iy*scale);
	}

	public int getHits() {
		return hits;
	}

}
