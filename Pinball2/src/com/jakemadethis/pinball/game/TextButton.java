package com.jakemadethis.pinball.game;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.jakemadethis.pinball.PingPong;

public class TextButton extends com.badlogic.gdx.scenes.scene2d.ui.TextButton {

	private final PingPong timer = new PingPong();
	private final static float TIME_DOWN = 0.2f;
	private final static float TIME_UP = 0.2f;
	
	public static TextButtonStyle createStyle() {
		return new TextButtonStyle(null, null, null, 
				1f, 1f, 0f, 0f, 
				PinballAssets.regularfont, 
				Color.BLACK, Color.BLACK, Color.BLACK);
	}
	
	public TextButton(String text) {
		super(text, createStyle());
		this.height = 50f;
	}
	
	@Override
	public boolean touchDown(float x, float y, int pointer) {
		if (super.touchDown(x, y, pointer)) {
			timer.forward(TIME_DOWN);
			return true;
		}
		return false;
	}
	
	@Override
	public void touchUp(float x, float y, int pointer) {
		super.touchUp(x, y, pointer);
		timer.backward(TIME_UP);
	}
	
	
	@Override
	public void draw(SpriteBatch batch, float parentAlpha) {
		float r = timer.value(1f, 0.5f);
		batch.setColor(1f, r, 1f, parentAlpha);
		batch.draw(PinballAssets.pixel, x, y, width, height);
		super.draw(batch, parentAlpha);
	}

}
