package com.jakemadethis.pinball.game;

import com.badlogic.gdx.ApplicationListener;
import com.jakemadethis.pinball.IState;

public class Pinball implements ApplicationListener {

	private static IState current = null;
	
	public static void setMenu() {
		current = new MainMenu();
	}
	
	public static void setGame(String levelName) {
		long s = System.currentTimeMillis();
		current = new GameState(levelName);
		System.out.println("Started GameState in "+(System.currentTimeMillis()-s)+"ms");
	}
	
	public static void setLoading() {
		current = new LoadingScreen();
	}

	@Override
	public void create() {
		setLoading();
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
		setLoading();
	}

}
