package com.jakemadethis.pinballeditor;

import com.badlogic.gdx.ApplicationListener;

public class PinballEditor implements ApplicationListener {

	private EditorModel model;
	private EditorView view;
	private EditorController controller;
	
	@Override
	public void create() {
		model = new EditorModel();
		view = new EditorView(model);
		controller = new EditorController(model, view);
	}

	@Override
	public void resize(int width, int height) {
	}

	@Override
	public void render() {
		controller.run();
	}

	@Override
	public void pause() {
		// TODO Auto-generated method stub

	}

	@Override
	public void resume() {
		// TODO Auto-generated method stub

	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub

	}

}
