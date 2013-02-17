package com.jakemadethis.pinball.game;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.jakemadethis.pinball.IFactory;
import com.jakemadethis.pinball.IState;
import com.jakemadethis.pinball.MathUtil;
import com.jakemadethis.pinball.ScreenCamera;
import com.jakemadethis.pinball.Screen;
import com.jakemadethis.pinball.ScreenStack;
import com.jakemadethis.pinball.ScreenTransition;
import com.jakemadethis.pinball.ScreenTransition.SlideForward;

public class Pinball implements ApplicationListener {

	private static Pinball inst;
	public static boolean LOL = false;
	
	private ScreenStack screenStack = new ScreenStack();
	
	
	public static void setScreen(IFactory<Screen> factory, ScreenTransition transition) {
		inst.screenStack.setTopScreen(factory, transition);
	}
	
	public static void setTransition(ScreenTransition transition) {
		inst.screenStack.setTransition(transition);
	}
	public static void popScreen() {
		inst.screenStack.popTopScreen();
	}
	
	
	public Pinball() {
		inst = this;
	}


	@Override
	public void create() {
		

		//while(!PinballAssets.update()) {
			//System.out.println("Loading");
		//}
	
		Screen.camera = new ScreenCamera(80f, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		//camera2 = makeCamera();

		setScreen(LoadingScreen.getFactory(), null);
	}
	
	@Override
	public void render() {

		//Gdx.gl20.glClearColor(0.6f, 0.2f, 0.4f, 1f);
		Gdx.gl20.glClearColor(0f, 0f, 0f, 1f);
		Gdx.gl20.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		//oldscreen.think(Gdx.graphics.getDeltaTime());
		Screen top = screenStack.getTop();
		
		//if (top != null && top.getTransition() == null) {
		//	System.out.println("No transi");
		//}

		
		//if (top != null && top.getTransition() != null)
			//top.getTransition().debug();
		if (LOL) {
			
			LOL = false;
		}
		
		
		screenStack.think(Gdx.graphics.getDeltaTime());
		//oldscreen.draw();
		top.draw();
			
		//current.x = MathUtil.timeSine(1f, -10f, 10f);
		//current.y = MathUtil.timeSine(1f, -10f, 10f);
		//stage.act(Gdx.graphics.getDeltaTime());
		
		//stage.draw();
	}

	@Override
	public void dispose() {}

	@Override
	public void pause() {
		PinballAssets.dispose();
	}

	@Override
	public void resize(int arg0, int arg1) {}

	@Override
	public void resume() {
		setScreen(LoadingScreen.getFactory(), null);
	}



}
