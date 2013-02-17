package com.jakemadethis.pinball.game;

import java.util.ArrayList;

import com.badlogic.gdx.Application.ApplicationType;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ClickListener;
import com.badlogic.gdx.utils.Logger;
import com.jakemadethis.pinball.IFactory;
import com.jakemadethis.pinball.IState;
import com.jakemadethis.pinball.IView;
import com.jakemadethis.pinball.Interpolator;
import com.jakemadethis.pinball.LevelException;
import com.jakemadethis.pinball.Screen;
import com.jakemadethis.pinball.ScreenTransition;
import com.jakemadethis.pinball.Timer;
import com.jakemadethis.pinball.builder.LevelNameFactory;
import com.jakemadethis.pinball.builder.XMLBuilder;

public class LevelMenu extends Screen {

	private final ArrayList<String> levels;
	
	public static IFactory<Screen> getFactory() {
		return new IFactory<Screen>() {
			@Override
			public Screen create() {
				return new LevelMenu();
			}
		};
	}


	public LevelMenu() {

		levels = new ArrayList<String>();
		
		// Android needs this for SAX
		if (Gdx.app.getType() == ApplicationType.Android)
			System.setProperty("org.xml.sax.driver", "org.xmlpull.v1.sax2.Driver");
		
		FileHandle internal = Gdx.files.internal("data");
		FileHandle[] list = internal.list();
		for (FileHandle f : list) {
			if (f.extension().equals("xml")) {
				addLevel(f);
			}
		}
	}
	
	
	

	private void addLevel(final FileHandle file) {
		try {
			LevelNameFactory factory = new LevelNameFactory();
			String levelName = XMLBuilder.fromStream(file.read()).create(factory);
			
			levels.add(file.nameWithoutExtension());
			
			int id = levels.size()-1;
			TextButton textButton = new TextButton(levelName);
			textButton.setClickListener(new ClickListener() {
				@Override public void click(Actor actor, float x, float y) {
					Pinball.setScreen(GameScreen.getFactory(file.nameWithoutExtension()), new ScreenTransition.SlideForward());
				}
			});
			textButton.width = Gdx.graphics.getWidth();
			textButton.height = 50f;
			textButton.y = (1 + id) * (textButton.height + 10f);
			stage.addActor(textButton);
			
		}
		catch (LevelException e) {
			e.printStackTrace();
		}
	}


	@Override
	public void drawBackground(SpriteBatch batch, float parentAlpha) {
		batch.begin();
		batch.draw(PinballAssets.background, 0, 0, width, height);
		batch.end();
	}
	

	
	@Override
	public boolean keyDown(int keycode) {
		if (keycode == Keys.BACK || keycode == Keys.BACKSPACE || keycode == Keys.ESCAPE) {
			Pinball.setScreen(MainMenu.getFactory(), new ScreenTransition.SlideBackward());
			return true;
		}
		return super.keyDown(keycode);
	}


}
