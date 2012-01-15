package com.jakemadethis.pinball;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL10;

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
		// TODO Auto-generated method stub
		
	}

	@Override
	public void pause() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void render() {
		controller.run();
	}

	@Override
	public void resize(int arg0, int arg1) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void resume() {
		// TODO Auto-generated method stub
		
	}

}
