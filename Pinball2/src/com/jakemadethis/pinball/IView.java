package com.jakemadethis.pinball;


public interface IView {
	public void render();
	//public void addEntity(Entity entity);
	//public Texture getTexture(String texture);

	public void think(float timeStep);
	
	//public ICamera getCamera();
	//public void think(int targetFrameRate);
	//public boolean canRender();

	
}
