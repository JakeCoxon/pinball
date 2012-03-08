package com.jakemadethis.pinball.game;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.jakemadethis.pinball.IState;

public class Pinball implements ApplicationListener {

	private IState current = null;
	
	public void setMenu() {
		current = new MenuState(this);
	}
	
	public void setGame(String levelName) {
		long s = System.currentTimeMillis();
		current = new GameState(this, levelName);
		System.out.println("Started GameState in "+(System.currentTimeMillis()-s)+"ms");
	}

	@Override
	public void create() {
		setMenu();
	}
	
	@Override
	public void render() {
		if (current != null)
			current.run();
	}

	@Override
	public void dispose() {}

	@Override
	public void pause() {}

	@Override
	public void resize(int arg0, int arg1) {}

	@Override
	public void resume() {
		Gdx.app.log("JAKE", "Sprite height: "+TextureManager.get().sprites.getHeight());
		Gdx.app.log("JAKE", TextureManager.get().sprites+"");
		Gdx.app.log("JAKE", Texture.getManagedStatus());
	}

}
