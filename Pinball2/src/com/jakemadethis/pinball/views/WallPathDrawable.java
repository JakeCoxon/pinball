package com.jakemadethis.pinball.views;

import java.util.ArrayList;
import java.util.LinkedList;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import com.jakemadethis.pinball.Entity;
import com.jakemadethis.pinball.GameView;
import com.jakemadethis.pinball.IDrawable;
import com.jakemadethis.pinball.IView;
import com.jakemadethis.pinball.entities.WallPath;

public class WallPathDrawable implements IDrawable {
	
	private GameView view;
	private final Color colorOn = new Color(1f, 1f, 1f, 0.5f);
	private final Color colorOff = new Color(1f, 1f, 1f, 0.1f);
	private WallPath wallPath;
	private ArrayList<Vector2> points;
	
	public WallPathDrawable(WallPath wallPath, GameView view) {
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
			a = 0.5f + a*0.5f;
		}
		
		colorOn.a = a;
		view.world.setColor(wallPath.isActive() ? colorOn : colorOff);
		for (int i = 0; i < points.size() - 1; i ++) {
			view.drawLine(view.world, points.get(i).x, points.get(i).y, points.get(i+1).x, points.get(i+1).y, 0.05f);
			//view.world.drawLine(paths[i], paths[i+1], paths[i+2], paths[i+3], wallPath.isActive() ? colorOn : colorOff);
			//view.getWorldDelegate().drawLine(DrawMode.NORMAL, seg[0], seg[1], seg[2], seg[3], color);
		}
		//view.getWorldDelegate().drawLine(DrawMode.NORMAL, x0, y0, x1, y1, color );
	}

}
