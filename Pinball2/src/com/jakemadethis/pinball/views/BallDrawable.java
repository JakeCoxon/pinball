package com.jakemadethis.pinball.views;

import java.util.LinkedList;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import com.jakemadethis.pinball.Entity;
import com.jakemadethis.pinball.GameView;
import com.jakemadethis.pinball.IDrawable;
import com.jakemadethis.pinball.IView;
import com.jakemadethis.pinball.entities.Ball;

public class BallDrawable implements IDrawable {

	LinkedList<Vector2> trail = new LinkedList<Vector2>();
	private GameView view;
	private Color color = new Color(1f, 0.9f, 1f, 0.5f);
	private Ball ball;
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
		/*trail.add(pos.cpy());
		if (trail.size() > 10) trail.remove();
		int i = 0;
		for (Vector2 p : trail) {
			float alpha = i/10f * 1f;
			color[3] = alpha;
			view.world.drawTexture(p.x, p.y, radius*2, radius*2, view.getSprite("ball"), color);
			i++;
		}*/
		view.world.setColor(color);
		view.world.draw(view.getSprite("ball"), pos.x - radius, pos.y - radius, radius*2, radius*2);
		//view.world.drawTexture(pos.x, pos.y, radius*2, radius*2, view.getSprite("ball"), color);
		
		
	}

}
