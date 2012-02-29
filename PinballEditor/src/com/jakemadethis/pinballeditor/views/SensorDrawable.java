package com.jakemadethis.pinballeditor.views;

import com.jakemadethis.pinball.Entity;
import com.jakemadethis.pinball.IDrawable;
import com.jakemadethis.pinball.level.Sensor;
import com.jakemadethis.pinballeditor.EditorView;

public class SensorDrawable implements IDrawable {

	private final Sensor sensor;
	private final EditorView view;

	public SensorDrawable(Sensor sensor, EditorView view) {
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
		
		view.world.setColor(1f, 1f, 1f, 0.5f);
		view.world.draw(view.getSprite("bumper"), x-r, y-r, r*2, r*2);
		
	}

}
