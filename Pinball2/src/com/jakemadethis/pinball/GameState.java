package com.jakemadethis.pinball;

public class GameState implements IState {

	private GameModel model;
	private GameView view;
	private GameController controller;
	
	public GameState(PinballStateManager stateManager, String levelName) {
		model = new GameModel();
		view = new GameView(model);
		controller = new GameController(stateManager, model, view, levelName);
	}
	
	@Override
	public void run() {
		controller.run();
	}

}
