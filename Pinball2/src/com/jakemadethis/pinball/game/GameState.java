package com.jakemadethis.pinball.game;

import com.badlogic.gdx.Gdx;
import com.jakemadethis.pinball.IState;

public class GameState implements IState {

	private final GameModel model;
	private final GameView view;
	private final GameController controller;
	
	public GameState(Pinball pinball, String levelName) {
		long s = System.currentTimeMillis();
		model = new GameModel();
		System.out.println("Created GameModel in "+(System.currentTimeMillis()-s)+"ms");
		
		s = System.currentTimeMillis();
		view = new GameView(model);
		System.out.println("Created GameView in "+(System.currentTimeMillis()-s)+"ms");
		
		s = System.currentTimeMillis();
		controller = new GameController(pinball, model, view, levelName);
		System.out.println("Created GameController in "+(System.currentTimeMillis()-s)+"ms");
		
		Gdx.input.setCatchBackKey(true);
	}
	
	@Override
	public void run() {
		controller.run();
	}

}
