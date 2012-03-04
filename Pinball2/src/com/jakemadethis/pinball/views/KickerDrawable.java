package com.jakemadethis.pinball.views;

import java.util.ArrayList;
import java.util.Random;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import com.jakemadethis.pinball.Entity;
import com.jakemadethis.pinball.GameView;
import com.jakemadethis.pinball.IDrawable;
import com.jakemadethis.pinball.Timer;
import com.jakemadethis.pinball.level.Kicker;

public class KickerDrawable implements IDrawable {

	private final Kicker kicker;
	private final GameView view;
	private final Color colorOff = new Color(1f, 1f, 1f, 1f);
	private final Color colorOn = new Color(1f, 0.2f, 1f, 1f);
	private final Color color = new Color();
	private int hits = 0;
	private final Timer timer = new Timer();
	private final Random r;

	public KickerDrawable(Kicker kicker, GameView view) {
		this.kicker = kicker;
		this.view = view;
		r = new Random();
	}
	
	@Override
	public Entity getEntity() {
		return kicker;
	}

	@Override
	public void draw() {
		ArrayList<Vector2> points = kicker.getPoints();
		Vector2 p0 = points.get(0);
		Vector2 p1 = points.get(1);
		
		if (kicker.getHits() > hits) {
			hit();
		}
		
		if (timer.running()) {
			color.r = timer.value(colorOn.r, colorOff.r);
			color.g = timer.value(colorOn.g, colorOff.g);
			color.b = timer.value(colorOn.b, colorOff.b);
			color.a = 2f;
			float w = timer.value(0.15f, 0.05f);
			view.world.setColor(color);
			view.drawLine(view.world, p0.x, p0.y, p1.x, p1.y, w);

			float dd = timer.value(0.1f, 0f);
			float a = r.nextFloat();
			float dx = kicker.getNormal().x * a * dd;
			float dy = kicker.getNormal().y * a * dd;
			view.drawLine(view.world, p0.x + dx, p0.y + dy, p1.x + dx, p1.y + dy, 0.05f);
		} else {
			view.world.setColor(colorOff);
			view.drawLine(view.world, p0.x, p0.y, p1.x, p1.y, 0.05f);
		}
	}

	private void hit() {
		timer.start(1f, true);
		hits ++;
		view.shake();
	}
	
}
