package com.jakemadethis.pinball.game;

import java.util.ArrayList;

import com.badlogic.gdx.Application.ApplicationType;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ClickListener;
import com.badlogic.gdx.utils.Logger;
import com.jakemadethis.pinball.AssetLoader.OnLoadedListener;
import com.jakemadethis.pinball.IState;
import com.jakemadethis.pinball.Interpolator;
import com.jakemadethis.pinball.LevelException;
import com.jakemadethis.pinball.Timer;
import com.jakemadethis.pinball.builder.LevelNameFactory;
import com.jakemadethis.pinball.builder.XMLBuilder;

public class MainMenu implements IState, ClickListener, OnLoadedListener {

	private final Pinball pinball;
	private final SpriteBatch spriteBatch;
	private final ArrayList<String> levels;
	private final int press = -1;
	private final int width;
	private final int height;
	private final Timer pressTimer = new Timer();
	private final Timer slideTimer = new Timer();
	private String playLevel;
	private final Stage stage;
	private final Group group;
	private Texture big;
	private final Texture loading;
	


	public MainMenu(Pinball pinball) {
		this.pinball = pinball;
		
		PinballAssets.get().setOnLoadedListener(this);
		//assets.setOnLoadedListener(this);
		
		spriteBatch = new SpriteBatch();
		width = Gdx.graphics.getWidth();
		height = Gdx.graphics.getHeight();
		
		OrthographicCamera c = 
			new OrthographicCamera(
					Gdx.graphics.getWidth(),
					Gdx.graphics.getHeight());
		c.setToOrtho(true);
		c.update();
		
		spriteBatch.setProjectionMatrix(c.combined);
		
		loading = new Texture("data/loading.png");
		loading.setFilter(TextureFilter.Linear, TextureFilter.Linear);

		levels = new ArrayList<String>();

		
		stage = new Stage(width, height, false);
		((OrthographicCamera)stage.getCamera()).setToOrtho(true);

		

		group = new Group();
		group.width = width;
		group.height = height;
		stage.addActor(group);
	

		//label.action(MoveTo.$(0f, 0f, 2f));
		
		Gdx.input.setInputProcessor(stage);
		
		
		Gdx.input.setCatchBackKey(false);
	}
	
	@Override
	public void onLoaded() {
		
		Gdx.app.setLogLevel(Logger.DEBUG);
		Gdx.app.debug("JAKE", "asdasdsad "+big);	


		
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
					playGame(file.nameWithoutExtension());
				}
			});
			textButton.width = width;
			textButton.height = 50f;
			textButton.y = id * textButton.height;
			group.addActor(textButton);
			
		}
		catch (LevelException e) {
			e.printStackTrace();
		}
	}



	@Override
	public void run() {
		if (!PinballAssets.get().update()) {

			Gdx.gl20.glClearColor(0f, 0f, 0f, 1f);
			Gdx.gl20.glClear(GL20.GL_COLOR_BUFFER_BIT);
			
			spriteBatch.begin();
			
			spriteBatch.draw(loading, width/2-32, height/2-32, 32, 32, 64, 64, 1, 1, 0, 0, 0, 64, 64, false, true);
			spriteBatch.end();
			
			return;
		}
		
		
		Gdx.gl20.glClearColor(0.6f, 0.2f, 0.4f, 1f);
		Gdx.gl20.glClear(GL20.GL_COLOR_BUFFER_BIT);
	

		float slideX = slideTimer.started() ? Interpolator.easeInOutCosine(slideTimer, 0f, -width) : 0f;
		group.x = slideX;
		
		/*spriteBatch.begin();

		
		for (int i = 0; i < levels.size(); i++) {
			String levelName = levelNames.get(i);
			if (press == i) {
				float r = Interpolator.easeOutQuad(pressTimer, 1f, 0.9f);
				bitmapfont.setColor(r, r, r, 1f);
				float y = Interpolator.easeOutQuad(pressTimer, 0f, 5f);
				bitmapfont.drawMultiLine(spriteBatch, levelName, slideX, i*bitmapfont.getLineHeight()+y, width, HAlignment.CENTER);

			}
			else {
				bitmapfont.setColor(1f, 1f, 1f, 1f);
				bitmapfont.drawMultiLine(spriteBatch, levelName, slideX, i*bitmapfont.getLineHeight(), width, HAlignment.CENTER);
			}

			//font.drawString(spriteBatch, level, 0, i*64f, 64f);
			
		}
		float progress = assets.getProgress();

		bitmapfont.setColor(1f, 1f, 1f, 1f);
		bitmapfont.draw(spriteBatch, "Loading "+progress, 0, height-40f);
		spriteBatch.end();*/
		
		stage.act(Gdx.graphics.getDeltaTime());
		stage.draw();
		
		
		
		if (slideTimer.finished()) {
			pinball.setGame(playLevel);
		}
		
		
		
	}



	private void playGame(String level) {
		playLevel = level;
		slideTimer.start(0.5f);
	}



	@Override
	public void click(Actor actor, float x, float y) {
	}

}
