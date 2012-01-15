package com.jakemadethis.pinballeditor;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.jakemadethis.net.Client;
import com.jakemadethis.pinball.GameModel;
import com.jakemadethis.pinball.IView;
import com.jakemadethis.pinball.entities.WallPath;
import com.jakemadethis.pinballeditor.net.EditorClient;

public class EditorController implements InputProcessor {
	private EditorModel model;
	private EditorView view;
	private Tool tool;
	private Client client;

	public EditorController(final EditorModel model, final EditorView view) {
		this.model = model;
		this.view = view;

		model.initGame();
		
		client = new Client("127.0.0.1", 4444, new EditorClient(model));
		
		tool = new WallPathTool(client, view, model);
		Gdx.input.setInputProcessor(this);
		
		view.setTool(tool);
		
		
	}
	
	
	public void run() {
		tool.think(0.01f);
		model.think(0.01f, 4);
		view.think(0.01f);
		view.render();
	}

	@Override
	public boolean keyDown(int keycode) {
		return tool.keyDown(keycode);
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
		return tool.touchDown(x, y, pointer, button);
	}

	@Override
	public boolean touchUp(int x, int y, int pointer, int button) {
		return tool.touchUp(x, y, pointer, button);
	}

	@Override
	public boolean touchDragged(int x, int y, int pointer) {
		return tool.touchDragged(x, y, pointer);
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
