package com.jakemadethis.pinball.entities;

import com.badlogic.gdx.physics.box2d.Body;
import com.jakemadethis.pinball.Entity;
import com.jakemadethis.pinball.IDrawable;
import com.jakemadethis.pinball.IView;
import com.jakemadethis.pinball.GameModel;
import com.jakemadethis.pinball.io.OutputHandler;

public class Sensor extends Entity {

	private float x;
	private float y;
	private float radius;
	private boolean sense = false;

	public Sensor(float x, float y, float radius) {
		this.x = x;
		this.y = y;
		this.radius = radius;
		
		outputs = new OutputHandler("onSense", "onUnSense");
	}
	
	@Override
	public <A> IDrawable accept(EntityVisitor<IDrawable, A> visitor, A arg) {
		return visitor.visit(this, arg);
	}
	
	

	@Override
	public void handleCollision(Ball ball, Body body, GameModel model) {
	}

	@Override
	public void think(float timestep, GameModel model) {
		if (test(model.getBall())) {
			if(!sense) {
				sense = true;
				outputs.invoke("onSense");
			}
		}
		else {
			if (sense) {
				sense = false;
				outputs.invoke("onUnSense");
			}
		}
	}
	
	private boolean test(Ball ball) {
		float x = ball.getBody().getPosition().x;
		float y = ball.getBody().getPosition().y;
		float r = ball.getRadius();
		if (x + r > this.x - radius && x - r < this.x + radius) {
			if (y + r > this.y - radius && y - r < this.y + radius) {
				return true;
			}
		}
		return false;
	}

}
