package com.jakemadethis.pinball;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class MenuState implements IState, InputProcessor {

	private final PinballStateManager stateManager;
	private final SpriteBatch spriteBatch;
	private Texture alphabetTexture;
	private final ArrayList<String> levels;
	private int press = -1;
	private final BitmapFont bitmapfont;
	private final TextureManager textureMan;
	private final int width;
	private final int height;

	public MenuState(PinballStateManager stateManager) {
		this.stateManager = stateManager;
		
		Gdx.input.setInputProcessor(this);
		
		spriteBatch = new SpriteBatch();
		width = Gdx.graphics.getWidth();
		height = Gdx.graphics.getHeight();
		
		OrthographicCamera c = 
			new OrthographicCamera(
					Gdx.graphics.getWidth(),
					Gdx.graphics.getHeight());
		c.setToOrtho(false);
		c.update();
		
		spriteBatch.setProjectionMatrix(c.combined);
		
		textureMan = TextureManager.get();
				
		bitmapfont = new BitmapFont(Gdx.files.internal("data/regular.fnt"), false);
		
		levels = new ArrayList<String>();
		
		FileHandle internal = Gdx.files.internal("data");
		FileHandle[] list = internal.list();
		for (FileHandle f : list) {
			if (f.extension().equals("xml")) {
				levels.add(f.nameWithoutExtension());
			}
		}
		
		Gdx.input.setCatchBackKey(false);
		
	}
	
	

	@Override
	public void run() {
		Gdx.gl20.glClearColor(0f, 0f, 0f, 1f);
		Gdx.gl20.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		spriteBatch.begin();
		for (int i = 0; i < levels.size(); i++) {
			String levelName = levels.get(i);
			if (press == i) bitmapfont.setColor(0.9f, 0f, 0f, 1f);
			else bitmapfont.setColor(0.9f, 0.9f, 0.9f, 1f);

			bitmapfont.draw(spriteBatch, levelName, 0, height-i*bitmapfont.getLineHeight());
			//font.drawString(spriteBatch, level, 0, i*64f, 64f);
			
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
		int i = (int) (y / bitmapfont.getLineHeight());
		if (i < levels.size()) {
			String l = levels.get(i);
			press = i;
			return true;
		}
		return false;
	}



	@Override
	public boolean touchUp(int x, int y, int pointer, int button) {
		int i = (int) (y / bitmapfont.getLineHeight());
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
