package com.jakemadethis.pinball.game.views;

import java.util.LinkedList;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import com.jakemadethis.pinball.Entity;
import com.jakemadethis.pinball.IDrawable;
import com.jakemadethis.pinball.game.GameView;
import com.jakemadethis.pinball.level.Ball;

public class BallDrawable implements IDrawable {

	LinkedList<Vector2> trail = new LinkedList<Vector2>();
	private final GameView view;
	private final Color color = new Color(0.8f, 0.4f, 0.6f, 1f);
	private final Ball ball;
	
	public BallDrawable(Ball ball, GameView view) {
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
		
		
		// Outer ring
		if (view.model.awesomeMode) {

			view.world.setColor(1f, 1f, 1f, 0.5f);
			view.world.draw(view.getSprite("ball"), pos.x - radius*1.5f, pos.y - radius*1.5f, radius*2*1.5f, radius*2*1.5f);
		}
		
		view.world.setColor(color);
		view.world.draw(view.getSprite("ball"), pos.x - radius, pos.y - radius, radius*2, radius*2);
		//view.world.drawTexture(pos.x, pos.y, radius*2, radius*2, view.getSprite("ball"), color);

		
	}

}
