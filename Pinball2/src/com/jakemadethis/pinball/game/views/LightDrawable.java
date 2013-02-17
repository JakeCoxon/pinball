package com.jakemadethis.pinball.game.views;

import com.badlogic.gdx.graphics.Color;
import com.jakemadethis.pinball.Entity;
import com.jakemadethis.pinball.IDrawable;
import com.jakemadethis.pinball.MathUtil;
import com.jakemadethis.pinball.game.GameView;
import com.jakemadethis.pinball.level.Light;

public class LightDrawable implements IDrawable {

	private final Light light;
	private final GameView view;
	private final Color offColor = new Color(1f, 1f, 1f, 0.1f);

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
		float r = light.getRadius();
		
		boolean on = light.isEnabled();
		if (light.isFlashing()) {
			on = ((int)(light.getFlashTimer().getTimeElapsed()*4) & 1) == 1;
		}

		Color color = null;
		if (on) {
			color = light.getColor();
			r *= 1.1f;
			color.a = 2f;
			view.world.setColor(1f, 0f, 0f, MathUtil.timeSine(1f, 0.5f, 0.6f));
			float rs = MathUtil.timeSine(1f, 0.4f, 0.41f);
			view.world.draw(view.getSprite("gradient"), x-rs, y-rs, 2*rs, 2*rs);
		}
		else {
			color = offColor;
		}

		
		
		view.world.setColor(color);
		view.world.draw(view.getSprite("bumper"), x-r/2, y-r/2, r, r);
	}

}
