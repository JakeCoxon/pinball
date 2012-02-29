package com.jakemadethis.pinballeditor.views;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import com.jakemadethis.pinball.Entity;
import com.jakemadethis.pinball.IDrawable;
import com.jakemadethis.pinball.level.Ball;
import com.jakemadethis.pinballeditor.EditorView;

public class BallDrawable implements IDrawable {

	private final EditorView view;
	private final Color color = new Color(1f, 0.9f, 1f, 0.5f);
	private final Ball ball;
	public BallDrawable(Ball ball, EditorView view) {
		this.ball = ball;
		this.view = view;
	}
	
	@Override
	public Entity getEntity() {
		return ball;
	}
	
	@Override
	public void draw() {
		Vector2 pos = ball.getBody().getPosition();
		float radius = ball.getRadius();
		
		view.world.setColor(color);
		view.world.draw(view.getSprite("ball"), 
				pos.x - radius, pos.y - radius, 
				radius*2, radius*2);
		
		
	}

}
