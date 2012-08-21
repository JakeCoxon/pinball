package com.jakemadethis.pinball.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.ui.Align;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;

public class GameOverScreen extends Group {
	private final Label scoreLabel;
	private final static float MARGIN = 20f;
	private final GameView gameView;
	public InputProcessor prevInputProcessor;

	public GameOverScreen(GameView gameView, float width, float height) {
		this.gameView = gameView;
		this.width = width;
		this.height = height;
		
		LabelStyle style = new LabelStyle(PinballAssets.regularfont, Color.WHITE);
		Label label = new Label("Game Over", style);
		label.width = width;
		label.setAlignment(Align.CENTER);
		label.y = 100f;
		
		scoreLabel = new Label("", style);
		scoreLabel.width = width;
		scoreLabel.y = 200;
		scoreLabel.setAlignment(Align.CENTER);
		
		addActor(label);
		addActor(scoreLabel);
	}
	
	@Override
	public boolean touchDown(float x, float y, int pointer) {
		this.visible = false;
		Gdx.input.setInputProcessor(prevInputProcessor);
		return super.touchDown(x, y, pointer);
	}
	
	public void setScore(int score) {
		scoreLabel.setText(String.valueOf(score));
	}
	
	@Override
	public void draw(SpriteBatch batch, float parentAlpha) {
		batch.setColor(0f, 0f, 0f, 0.8f);
		batch.draw(PinballAssets.pixel, MARGIN, MARGIN, width-2*MARGIN, height-2*MARGIN);
		super.draw(batch, parentAlpha);
	}
}
