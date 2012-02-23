package com.jakemadethis.pinball;

public class PinballStateManager {
	private IState current = null;
	
	public PinballStateManager() {
	}
	
	public void setState(IState state) {
		current = state;
	}
	public IState getCurrentState() {
		return current;
	}
	public void runCurrentState() {
		if (current != null)
			current.run();
	}
	
	//
	
	public void setMenu() {
		current = new MenuState(this);
	}
	
	public void setGame(String l) {
		current = new GameState(this, l);
	}
	
}
