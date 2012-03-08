package com.jakemadethis.pinball.game.views;

import com.jakemadethis.pinball.Entity;
import com.jakemadethis.pinball.IDrawable;
import com.jakemadethis.pinball.game.GameView;
import com.jakemadethis.pinball.level.Sensor;

public class SensorDrawable implements IDrawable {

	private Sensor sensor;
	private GameView view;

	public SensorDrawable(Sensor sensor, GameView view) {
		this.sensor = sensor;
		this.view = view;
	}

	@Override
	public Entity getEntity() {
		return sensor;
	}

	@Override
	public void draw() {
		float x = sensor.getX(), y = sensor.getY();
		float r = sensor.getRadius();
		
		view.world.setColor(1f, 1f, 1f, 0.1f);
		view.world.draw(view.getSprite("bumper"), x-r, y-r, r*2, r*2);
		
	}

}
