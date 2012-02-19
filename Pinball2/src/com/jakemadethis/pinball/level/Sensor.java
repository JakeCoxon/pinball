package com.jakemadethis.pinball.level;

import java.util.HashMap;

import com.badlogic.gdx.physics.box2d.Body;
import com.jakemadethis.pinball.BaseModel;
import com.jakemadethis.pinball.Entity;
import com.jakemadethis.pinball.IDrawable;
import com.jakemadethis.pinball.GameModel;
import com.jakemadethis.pinball.builder.BuilderNode;
import com.jakemadethis.pinball.builder.FactoryUtil;
import com.jakemadethis.pinball.io.OutputHandler;

public class Sensor extends Entity {

	public static Sensor fromNode(BaseModel model, BuilderNode node) {
		HashMap<String, String> atts = node.getAttributes();
		float[] pos = FactoryUtil.getAbsolutePosition(node.getParent().getValue(), atts);
		float radius = 			 Float.valueOf( FactoryUtil.expected(atts, "radius") );
		String name = FactoryUtil.optional(atts, "name", "");
		
		Sensor s = model.addSensor(pos[0], pos[1], radius);
		model.setName(name, s);
		return s;
	}
	
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
	public <A, R> R accept(EntityVisitor<R, A> visitor, A arg) {
		return visitor.visit(this, arg);
	}
	
	public float getX() {
		return x;
	}
	public float getY() {
		return y;
	}
	public float getRadius() {
		return radius;
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
