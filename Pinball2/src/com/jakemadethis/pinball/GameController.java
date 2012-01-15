package com.jakemadethis.pinball;

import java.io.IOException;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.files.FileHandle;
import com.jakemadethis.net.Client;
import com.jakemadethis.net.Server;
import com.jakemadethis.pinball.EventHandler.EventListener;
import com.jakemadethis.pinball.BaseModel.EntityArgs;
import com.jakemadethis.pinball.entities.Ball;
import com.jakemadethis.pinball.entities.Bumper;
import com.jakemadethis.pinball.entities.Wall;
import com.jakemadethis.pinball.net.Protocol;

public class GameController implements InputProcessor {
	private boolean running = false;
    private int targetFrameRate = 60;
    public static float gameSpeed = 1f;
	
	final private GameModel model;
	final private IView view;
	
	public GameController(final GameModel model, final IView view) {
		
		this.model = model;
		this.view = view;
		
		Gdx.input.setInputProcessor(this);
		

		//FileHandle file = Gdx.files.internal("data/level.xml");
		//Builder.fromStream(model, file.read());
		
		//Log.d("JAKE", "initGame");
		model.initGame();
		
		Thread thread = new Thread(new Runnable() {
			@Override public void run() {
				Server server = new Server(4444, new Protocol(model));
				server.start();
			}
		});
		thread.start();
		
		//new Client("127.0.0.1", 4444, new Protocol(model));
		
	}
	
	public void resetGame() {
		model.reset();
	}
	

	
	public void run() {
		float delta = Gdx.graphics.getDeltaTime();
		model.think(0.01f * gameSpeed, 4);
		view.think(0.01f * gameSpeed);
		view.render();
	}

	@Override
	public boolean keyDown(int keycode) {
		return false;
	}

	@Override
	public boolean keyUp(int keycode) {
		return false;
	}

	@Override
	public boolean keyTyped(char character) {
		return false;
	}

	@Override
	public boolean touchDown(int x, int y, int pointer, int button) {
		if (model.getBall().isActive()) {
			model.engageFlipper(true);
		}
		else {
			model.getBall().launch();
		}
		return false;
	}

	@Override
	public boolean touchUp(int x, int y, int pointer, int button) {
		model.engageFlipper(false);
		return true;
	}

	@Override
	public boolean touchDragged(int x, int y, int pointer) {
		return false;
	}

	@Override
	public boolean touchMoved(int x, int y) {
		return false;
	}

	@Override
	public boolean scrolled(int amount) {
		return false;
	}
	


	/*public boolean onTouchEvent(MotionEvent event) {
		if (event.getAction() == MotionEvent.ACTION_DOWN) {
			float[] pos = new float[2];
			view.getCamera().screenToWorld(pos, event.getX(), event.getY());
			
			if (!model.getBall().isActive())
				model.getBall().launch();
			else
				model.engageFlipper(true);
			//addBall(pos[0]*100 - 15f/2, pos[1]*100 - 15f/2, 15f);
		}
		else if (event.getAction() == MotionEvent.ACTION_UP) {
			model.engageFlipper(false);
		}
		return false;
	}*/
	



}
