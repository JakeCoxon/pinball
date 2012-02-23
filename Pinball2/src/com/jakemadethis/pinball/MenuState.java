package com.jakemadethis.pinball;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class MenuState implements IState, InputProcessor {

	private PinballStateManager stateManager;
	private SpriteBatch spriteBatch;
	private Texture alphabetTexture;
	private Font font;
	private ArrayList<String> levels;
	private int press = -1;

	public MenuState(PinballStateManager stateManager) {
		this.stateManager = stateManager;
		
		Gdx.input.setInputProcessor(this);
		
		spriteBatch = new SpriteBatch();
		OrthographicCamera c = 
			new OrthographicCamera(
					Gdx.graphics.getWidth(),
					Gdx.graphics.getHeight());
		c.setToOrtho(true);
		spriteBatch.setProjectionMatrix(c.combined);
		
		alphabetTexture = new Texture(Gdx.files.internal("data/alphabet.png"));
		alphabetTexture.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		
		font = new Font(alphabetTexture);
		
		levels = new ArrayList<String>();
		
		FileHandle internal = Gdx.files.internal("data");
		FileHandle[] list = internal.list();
		for (FileHandle f : list) {
			if (f.extension().equals("xml")) {
				levels.add(f.nameWithoutExtension());
			}
		}
		
	}
	
	

	@Override
	public void run() {
		Gdx.gl20.glClearColor(0f, 0f, 0f, 1f);
		Gdx.gl20.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		spriteBatch.begin();
		for (int i = 0; i < levels.size(); i++) {
			String level = levels.get(i);
			if (press == i) spriteBatch.setColor(0.9f, 0f, 0f, 1f);
			else spriteBatch.setColor(0.9f, 0.9f, 0.9f, 1f);
			font.drawString(spriteBatch, level, 0, i*64f, 64f);
			
		}
		spriteBatch.end();
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
		int i = y/64;
		if (i < levels.size()) {
			String l = levels.get(i);
			press = i;
			return true;
		}
		return false;
	}



	@Override
	public boolean touchUp(int x, int y, int pointer, int button) {
		int i = y/64;
		press = -1;
		if (i < levels.size()) {
			String l = levels.get(i);
			stateManager.setGame(l);
			return true;
		}
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
