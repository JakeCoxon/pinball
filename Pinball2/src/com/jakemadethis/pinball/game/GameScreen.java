package com.jakemadethis.pinball.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Application.ApplicationType;
import com.badlogic.gdx.Input.Buttons;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.joints.MouseJoint;
import com.badlogic.gdx.physics.box2d.joints.MouseJointDef;
import com.jakemadethis.pinball.IFactory;
import com.jakemadethis.pinball.IState;
import com.jakemadethis.pinball.IView;
import com.jakemadethis.pinball.LevelException;
import com.jakemadethis.pinball.Screen;
import com.jakemadethis.pinball.ScreenTransition;
import com.jakemadethis.pinball.Timer;
import com.jakemadethis.pinball.builder.IBuilder;
import com.jakemadethis.pinball.builder.PinballNewFactory;
import com.jakemadethis.pinball.builder.XMLBuilder;

public class GameScreen extends Screen {

	private final GameModel model;
	private final GameView view;

	private final Timer gamePausedTimer = new Timer();
	private final String levelName;
	private BallDragger ballDragger;

  private float gameSpeed = Timer.GAME_SPEED;

	public static IFactory<Screen> getFactory(final String levelName) {
		return new IFactory<Screen>() {
			@Override
			public Screen create() {
				return new GameScreen(levelName);
			}
		};
	}
	
	public GameScreen(String levelName) {
		this.levelName = levelName;
		long s = System.currentTimeMillis();
		model = new GameModel();
		System.out.println("Created GameModel in "+(System.currentTimeMillis()-s)+"ms");
		
		s = System.currentTimeMillis();
		view = new GameView(model, stage);
		System.out.println("Created GameView in "+(System.currentTimeMillis()-s)+"ms");
		
		s = System.currentTimeMillis();
		loadLevel(levelName);
		System.out.println("Loaded level in "+(System.currentTimeMillis()-s)+"ms");
		
		//addActor(view);
		Gdx.input.setCatchBackKey(true);
	}

	private void loadLevel(String levelName) {

		try {
			// Android needs this for SAX
			if (Gdx.app.getType() == ApplicationType.Android)
				System.setProperty("org.xml.sax.driver", "org.xmlpull.v1.sax2.Driver");
			
			FileHandle file = Gdx.files.internal("data/"+levelName+".xml");
			IBuilder b = XMLBuilder.fromStream(file.read());
			
			//PinballFactory factory = new PinballFactory(model);
			PinballNewFactory factory = new PinballNewFactory(model);
			b.create(factory);
		}
		catch (LevelException e) {
			e.printStackTrace();
			return;
		}
		
		ballDragger = new BallDragger(model, view);

		//Log.d("JAKE", "initGame");
		model.initGame();
	}
	
	@Override
	public void drawBackground(SpriteBatch batch, float parentAlpha) {
		batch.begin();
		batch.draw(PinballAssets.background, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		
		
		view.draw(batch, alpha);
		batch.end();
	}
	
	@Override
	public void thinkInternal(float timestep) {
		ballDragger.think(timestep);
		
		float delta = Gdx.graphics.getDeltaTime();
		model.think(delta * gameSpeed, 4);
		view.think(delta * gameSpeed);
		
		if (model.gameOver) {
			if (!gamePausedTimer.started())
				gamePausedTimer.start(0.5f);
		}
		
	}
	
	@Override
	public boolean keyDown(int keycode) {
		if (keycode == Keys.T) {
			loadLevel(levelName);
			return true;
		}
		else if (keycode == Keys.BACK || keycode == Keys.BACKSPACE || keycode == Keys.ESCAPE) {
			Pinball.setScreen(LevelMenu.getFactory(), new ScreenTransition.SlideBackward());
			return true;
		}
		else if (keycode == Keys.SPACE) {
			if (!model.getBall().isActive()) {
				model.getBall().launch();
			}
		}
		else if (keycode == Keys.LEFT) {
			model.engageLeftFlippers(true);
			return true;
		}
		else if (keycode == Keys.RIGHT) {
			model.engageRightFlippers(true);
			return true;
		}
		return false;
	}


	@Override
	public boolean keyUp(int keycode) {
		if (keycode == Keys.LEFT) {
			model.engageLeftFlippers(false);
			return true;
		}
		else if (keycode == Keys.RIGHT) {
			model.engageRightFlippers(false);
			return true;
		}
		return false;
	}


	@Override
	public boolean touchDown(int x, int y, int pointer, int button) {

		if (ballDragger.touchDown(x,y, pointer, button))
			return true;
		
		if (button == 0) {
			if (model.getBall().isActive()) {
				model.engageLeftFlippers(true);
				model.engageRightFlippers(true);
			}
			else {
				model.getBall().launch();
			}
			return true;
		}
		
		
		
		return false;
	}

	@Override
	public boolean touchUp(int x, int y, int pointer, int button) {
		if (ballDragger.touchUp(x, y, pointer, button))
			return true;
		
		model.engageLeftFlippers(false);
		model.engageRightFlippers(false);
		
		return true;
	}
	

}
