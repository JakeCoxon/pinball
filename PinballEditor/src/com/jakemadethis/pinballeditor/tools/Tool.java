package com.jakemadethis.pinballeditor.tools;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector3;
import com.jakemadethis.net.Client;
import com.jakemadethis.pinball.Entity;
import com.jakemadethis.pinball.IDrawable;
import com.jakemadethis.pinballeditor.EditorModel;
import com.jakemadethis.pinballeditor.EditorView;

public abstract class Tool implements InputProcessor {


	protected EditorView view;
	protected EditorModel model;
	protected SpriteBatch world;
	protected OrthographicCamera worldCamera;
	protected Vector3 worldMouse = new Vector3(0,0,0);
	
	public Tool(EditorView view, EditorModel model) {
		this.view = view;
		this.model = model;
		this.world = view.world;
		this.worldCamera = view.worldCamera;
	}
	
	public abstract void draw();
	
	public void think(float delta) {
		worldMouse.set(Gdx.input.getX(), Gdx.input.getY(), 0);
		view.worldCamera.unproject(worldMouse);
	}

	@Override
	public boolean keyDown(int keycode) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean keyUp(int keycode) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean keyTyped(char character) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean touchDown(int x, int y, int pointer, int button) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean touchUp(int x, int y, int pointer, int button) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean touchDragged(int x, int y, int pointer) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean touchMoved(int x, int y) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean scrolled(int amount) {
		// TODO Auto-generated method stub
		return false;
	}

}
