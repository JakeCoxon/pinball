package com.jakemadethis.pinballeditor.views;

import com.badlogic.gdx.graphics.Color;
import com.jakemadethis.pinball.Entity;
import com.jakemadethis.pinball.IDrawable;
import com.jakemadethis.pinball.level.Light;
import com.jakemadethis.pinballeditor.EditorView;

public class LightDrawable implements IDrawable {

	private final Light light;
	private final EditorView view;
	private final Color color = new Color(1f, 0f, 0f, 0.9f);

	public LightDrawable(Light light, EditorView view) {
		this.light = light;
		this.view = view;
	}

	@Override
	public Entity getEntity() {
		return light;
	}

	@Override
	public void draw() {
		float x = light.getX(), y = light.getY();
		float w = light.getWidth(), h = light.getHeight();
		view.world.setColor(color);
		view.world.draw(view.getSprite("bumper"), x-w/2, y-h/2, w, h);
	}

}
