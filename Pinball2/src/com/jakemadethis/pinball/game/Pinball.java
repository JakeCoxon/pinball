package com.jakemadethis.pinball.game;

import com.badlogic.gdx.ApplicationListener;
import com.jakemadethis.pinball.IState;

public class Pinball implements ApplicationListener {

	private IState current = null;
	
	public void setMenu() {
		current = new MainMenu(this);
	}
	
	public void setGame(String levelName) {
		long s = System.currentTimeMillis();
		current = new GameState(this, levelName);
		System.out.println("Started GameState in "+(System.currentTimeMillis()-s)+"ms");
	}

	@Override
	public void create() {
		PinballAssets.get();
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
	public void resume() {}

}
