package com.jakemadethis.pinball.game.views;

import java.util.ArrayList;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import com.jakemadethis.pinball.Entity;
import com.jakemadethis.pinball.IDrawable;
import com.jakemadethis.pinball.MathUtil;
import com.jakemadethis.pinball.game.GameView;
import com.jakemadethis.pinball.level.Wall;

public class WallPathDrawable implements IDrawable {
	
	private final GameView view;
	private final Color colorOn = new Color(1f, 1f, 1f, 0.5f);
	private final Color colorOff = new Color(1f, 1f, 1f, 0.1f);
	private final Wall wallPath;
	private final ArrayList<Vector2> points;
	
	public WallPathDrawable(Wall wallPath, GameView view) {
		this.view = view;
		this.wallPath = wallPath;
		this.points = wallPath.getPoints();
	}
	@Override
	public Entity getEntity() {
		return wallPath;
	}
	
	
	@Override
	public void draw() {
		float a = 0.5f + 0.5f * (float)Math.sin(System.currentTimeMillis() * 0.01);
		if (!view.model.awesomeMode) {
			a = 1;//0.5f + a*0.5f;
		}
		
		colorOn.a = a;
		Color c = wallPath.isActive() ? colorOn : colorOff;
		view.world.setColor(c);
		for (int i = 0; i < points.size() - 1; i ++) {
			if (wallPath.isActive()) {
				view.world.setColor(c.r, c.g, c.b, MathUtil.interp(a, 0.2f, 0.4f));
			}
			view.drawLine(view.world, view.getSprite("glow"), points.get(i).x, points.get(i).y, points.get(i+1).x, points.get(i+1).y, 0.2f);
			
			view.world.setColor(c.r, c.g, c.b, c.a);
			view.drawLine(view.world, points.get(i).x, points.get(i).y, points.get(i+1).x, points.get(i+1).y, 0.035f);
			//view.world.drawLine(paths[i], paths[i+1], paths[i+2], paths[i+3], wallPath.isActive() ? colorOn : colorOff);
			//view.getWorldDelegate().drawLine(DrawMode.NORMAL, seg[0], seg[1], seg[2], seg[3], color);
		}
		//view.getWorldDelegate().drawLine(DrawMode.NORMAL, x0, y0, x1, y1, color );
	}

}
