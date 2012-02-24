package com.jakemadethis.pinball;

public class GameState implements IState {

	private GameModel model;
	private GameView view;
	private GameController controller;
	
	public GameState(PinballStateManager stateManager, String levelName) {
		long s = System.currentTimeMillis();
		model = new GameModel();
		System.out.println("Created GameModel in "+(System.currentTimeMillis()-s)+"ms");
		
		s = System.currentTimeMillis();
		view = new GameView(model);
		System.out.println("Created GameView in "+(System.currentTimeMillis()-s)+"ms");
		
		s = System.currentTimeMillis();
		controller = new GameController(stateManager, model, view, levelName);
		System.out.println("Created GameController in "+(System.currentTimeMillis()-s)+"ms");
	}
	
	@Override
	public void run() {
		controller.run();
	}

}
