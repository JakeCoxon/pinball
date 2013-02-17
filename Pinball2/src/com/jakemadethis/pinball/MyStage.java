package com.jakemadethis.pinball;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Stage;

public class MyStage extends Stage {
	private Vector2 coords = new Vector2();

	public MyStage(float width, float height, boolean stretch) {
		super(width, height, stretch);
	}
	
	public MyStage(float width, float height, boolean stretch, SpriteBatch batch) {
		super(width, height, stretch, batch);
	}

	@Override
	public void draw() {
		draw(1f);
	}
	
	public void draw(float parentAlpha) {
		if (!root.visible) return;
		batch.begin();
		root.draw(batch, parentAlpha);
		batch.end();
	}
	
	
	
}
