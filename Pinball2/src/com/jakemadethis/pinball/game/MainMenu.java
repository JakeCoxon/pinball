package com.jakemadethis.pinball.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.ClickListener;
import com.jakemadethis.pinball.IFactory;
import com.jakemadethis.pinball.Screen;
import com.jakemadethis.pinball.ScreenTransition;

public class MainMenu extends Screen {
	
	public static IFactory<Screen> getFactory() {
		return new IFactory<Screen>() {
			@Override
			public Screen create() {
				return new MainMenu();
			}
		};
	}
	
	public MainMenu() {
		super();

		makeButton("Play", 0).setClickListener(new ClickListener() {
			@Override public void click(Actor actor, float x, float y) {
				Pinball.setScreen(LevelMenu.getFactory(), new ScreenTransition.SlideForward());
			}
		});
		
		makeButton("Settings", 1).setClickListener(new ClickListener() {
			@Override public void click(Actor actor, float x, float y) {
				Pinball.setScreen(TestScreen.getFactory(), new ScreenTransition.ZoomIn());
				Pinball.LOL = true;
			}
		});
	}
	
	private TextButton makeButton(String text, int num) {

		TextButton textButton = new TextButton(text);
		textButton.width = Gdx.graphics.getWidth();
		textButton.height = 50f;
		textButton.y = Gdx.graphics.getHeight() - (3 - num) * (textButton.height + 10f);
		stage.addActor(textButton);
		return textButton;
	}

	@Override
	public void drawBackground(SpriteBatch batch, float parentAlpha) {
		batch.begin();
		batch.draw(PinballAssets.background, 0, 0, width, height);
		batch.end();
	}

	
}
