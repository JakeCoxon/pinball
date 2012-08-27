package com.jakemadethis.pinball.level;

import com.badlogic.gdx.physics.box2d.Body;
import com.jakemadethis.pinball.Entity;
import com.jakemadethis.pinball.game.GameModel;

public class Gem extends Entity {
	
	private final float x;
	private final float y;
	private float radius = 0.1f;

	public Gem(float x, float y) {
		this.x = x;
		this.y = y;
	}
	
	public float getX() {
		return x;
	}
	public float getY() {
		return y;
	}
	
	@Override
	public void handleCollision(Ball ball, Body body, GameModel model) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void think(float timestep, GameModel model) {
		if (test(model.getBall())) {
			model.addScore(100);
			model.remove(this);
		}
	}
	
	@Override
	public <A, R> R accept(EntityVisitor<R, A> visitor, A arg) {
		return visitor.visit(this, arg);
	}
	
	private boolean test(Ball ball) {
		float x = ball.getBody().getPosition().x;
		float y = ball.getBody().getPosition().y;
		float r = ball.getRadius();
		if (x + r > this.x - radius  && x - r < this.x + radius) {
			if (y + r > this.y - radius && y - r < this.y + radius) {
				return true;
			}
		}
		return false;
	}
	
}
