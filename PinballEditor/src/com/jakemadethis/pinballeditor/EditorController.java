package com.jakemadethis.pinballeditor;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputProcessor;
import com.jakemadethis.net.Client;
import com.jakemadethis.pinballeditor.net.EditorClient;
import com.jakemadethis.pinballeditor.tools.BumperTool;
import com.jakemadethis.pinballeditor.tools.Tool;
import com.jakemadethis.pinballeditor.tools.WallPathTool;

public class EditorController implements InputProcessor {
	private EditorModel model;
	private EditorView view;
	private Tool tool;
	private Client client;
	private Tool[] tools;

	public EditorController(final EditorModel model, final EditorView view) {
		this.model = model;
		this.view = view;

		model.initGame();
		
		client = new Client("127.0.0.1", 4444, new EditorClient(model));
		
		tools = new Tool[] {
			new WallPathTool(client, view, model),
			new BumperTool(view, model)
		};
		tool = tools[0];
		
		Gdx.input.setInputProcessor(this);
		
		view.setTool(tool);
		
		
	}
	private void setTool(int id) {
		tool = tools[id];
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
		if (keycode == Keys.F1) { setTool(0); return true; }
		if (keycode == Keys.F2) { setTool(1); return true; }
		
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
