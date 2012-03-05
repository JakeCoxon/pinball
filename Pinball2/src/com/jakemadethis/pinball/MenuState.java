package com.jakemadethis.pinball;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.BitmapFont.HAlignment;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class MenuState implements IState, InputProcessor {

	private final Pinball pinball;
	private final SpriteBatch spriteBatch;
	private Texture alphabetTexture;
	private final ArrayList<String> levels;
	private int press = -1;
	private final BitmapFont bitmapfont;
	private final TextureManager textureMan;
	private final int width;
	private final int height;
	private final Timer pressTimer = new Timer();
	private final Timer slideTimer = new Timer();
	private String playLevel;

	public MenuState(Pinball pinball) {
		this.pinball = pinball;
		
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
		Gdx.gl20.glClearColor(0.6f, 0.2f, 0.4f, 1f);
		Gdx.gl20.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		spriteBatch.begin();

		float slideX = slideTimer.started() ? Interpolator.easeInOutCosine(slideTimer, 0f, -width) : 0f;
		
		for (int i = 0; i < levels.size(); i++) {
			String levelName = levels.get(i);
			if (press == i) {
				float r = Interpolator.easeOutQuad(pressTimer, 1f, 0.9f);
				bitmapfont.setColor(r, r, r, 1f);
				float y = Interpolator.easeOutQuad(pressTimer, 0f, 5f);
				bitmapfont.drawMultiLine(spriteBatch, levelName, slideX, height-i*bitmapfont.getLineHeight()+y, width, HAlignment.CENTER);

			}
			else {
				bitmapfont.setColor(1f, 1f, 1f, 1f);
				bitmapfont.drawMultiLine(spriteBatch, levelName, slideX, height-i*bitmapfont.getLineHeight(), width, HAlignment.CENTER);
			}

			//font.drawString(spriteBatch, level, 0, i*64f, 64f);
			
		}
		
		spriteBatch.end();
		
		if (slideTimer.finished()) {
			pinball.setGame(playLevel);
		}
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
			pressTimer.start(0.2f);
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
			playGame(l);
			return true;
		}
		return false;
	}



	private void playGame(String level) {
		playLevel = level;
		slideTimer.start(0.5f);
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
