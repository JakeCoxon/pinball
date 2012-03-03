package com.jakemadethis.pinballeditor.views;

import java.util.ArrayList;
import java.util.Random;

import com.badlogic.gdx.math.Vector2;
import com.jakemadethis.pinball.Entity;
import com.jakemadethis.pinball.IDrawable;
import com.jakemadethis.pinball.level.Wall;
import com.jakemadethis.pinballeditor.EditorView;

public class WallPathDrawable implements IDrawable {
	
	private final EditorView view;
	private final Wall wallPath;
	
	public WallPathDrawable(Wall wallPath, EditorView view) {
		this.view = view;
		this.wallPath = wallPath;
	}
	@Override
	public Entity getEntity() {
		return wallPath;
	}
	
	
	@Override
	public void draw() {

		ArrayList<Vector2> points = wallPath.getPoints();
		
		view.world.setColor(1f, 1f, 1f, 1f);
		Random r = new Random();
		r.setSeed(1000);
		for (int i = 0; i < points.size() - 1; i ++) {
			view.world.setColor(r.nextFloat(),r.nextFloat(), r.nextFloat(), 1f);
			view.drawHairLine(view.world, points.get(i).x, points.get(i).y, points.get(i+1).x, points.get(i+1).y, 2f);
			//view.world.drawLine(paths[i], paths[i+1], paths[i+2], paths[i+3], wallPath.isActive() ? colorOn : colorOff);
			//view.getWorldDelegate().drawLine(DrawMode.NORMAL, seg[0], seg[1], seg[2], seg[3], color);
		}
		//view.getWorldDelegate().drawLine(DrawMode.NORMAL, x0, y0, x1, y1, color );
	}

}
