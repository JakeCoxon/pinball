package com.jakemadethis.pinball.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ClickListener;
import com.jakemadethis.pinball.IFactory;
import com.jakemadethis.pinball.MathUtil;
import com.jakemadethis.pinball.MyStage;
import com.jakemadethis.pinball.Screen;
import com.jakemadethis.pinball.ScreenTransition;

public class TestScreen extends Screen {
	public static IFactory<Screen> getFactory() {
		return new IFactory<Screen>() {
			@Override
			public Screen create() {
				return new TestScreen();
			}
		};
	}

	public TestScreen() {
		super(Gdx.graphics.getWidth()*0.9f, Gdx.graphics.getHeight());
		
		TextButton textButton = new TextButton("Hello");
		textButton.setClickListener(new ClickListener() {
			@Override public void click(Actor actor, float x, float y) {
				System.out.println("OK");
				Pinball.setScreen(TestScreen.getFactory(), new ScreenTransition.ZoomIn());
				Pinball.LOL = true;
			}
		});
		textButton.width = width;
		textButton.height = 50f;
		textButton.y = 1 * textButton.height;
		
		stage.addActor(textButton);
		
		
	}
	
	
	@Override
	public void drawBackground(SpriteBatch batch, float parentAlpha) {
		batch.begin();
		batch.draw(PinballAssets.background, 0, 0, width, height);
		batch.end();
		
	}
	
	@Override
	public void thinkInternal(float timestep) {
	  transformMatrix.translate(Gdx.graphics.getWidth() - width, 0, 0);
	}
	

	
	@Override
	public boolean keyDown(int keycode) {
		if (keycode == Keys.BACK || keycode == Keys.BACKSPACE || keycode == Keys.ESCAPE) {
			Pinball.popScreen();
			return true;
		}
		return super.keyDown(keycode);
	}

}
