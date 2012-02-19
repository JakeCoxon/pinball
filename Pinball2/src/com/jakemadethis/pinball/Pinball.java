package com.jakemadethis.pinball;

import com.badlogic.gdx.ApplicationListener;

public class Pinball implements ApplicationListener {

	private GameModel model;
	private GameView view;
	private GameController controller;

	@Override
	public void create() {
		model = new GameModel();
		view = new GameView(model);
		controller = new GameController(model, view);
	}

	@Override
	public void dispose() {
		
	}

	@Override
	public void pause() {
		
	}

	@Override
	public void render() {
		controller.run();
	}

	@Override
	public void resize(int arg0, int arg1) {
		
	}

	@Override
	public void resume() {
		
	}

}
