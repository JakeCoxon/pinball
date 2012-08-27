package com.jakemadethis.pinball;

import com.badlogic.gdx.graphics.OrthographicCamera;


public interface IView {
	public void render();
	public void think(float timeStep);
}
