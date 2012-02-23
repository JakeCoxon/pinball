package com.jakemadethis.pinball;

import com.badlogic.gdx.ApplicationListener;

public class Pinball extends PinballStateManager implements ApplicationListener {


	@Override
	public void create() {
		setMenu();
	}
	
	@Override
	public void render() {
		runCurrentState();
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
