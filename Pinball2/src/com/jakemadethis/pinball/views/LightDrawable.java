package com.jakemadethis.pinball.views;

import com.badlogic.gdx.graphics.Color;
import com.jakemadethis.pinball.Entity;
import com.jakemadethis.pinball.GameView;
import com.jakemadethis.pinball.IDrawable;
import com.jakemadethis.pinball.level.Light;

public class LightDrawable implements IDrawable {

	private Light light;
	private GameView view;
	private Color offColor = new Color(1f, 1f, 1f, 0.1f);

	public LightDrawable(Light light, GameView view) {
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
		Color color = offColor;
		if (light.isFlashing()) {
			if (((int)(light.getFlashTimer().getTimeElapsed()*4) & 1) == 1) {
				color = light.getColor();
			}
		}
		else if (light.isEnabled()) {
			color = light.getColor();
		}
		
		view.world.setColor(color);
		view.world.draw(view.getSprite("bumper"), x-w/2, y-h/2, w, h);
	}

}
