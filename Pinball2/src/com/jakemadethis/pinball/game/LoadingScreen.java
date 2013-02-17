package com.jakemadethis.pinball.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Matrix4;
import com.jakemadethis.pinball.IFactory;
import com.jakemadethis.pinball.IState;
import com.jakemadethis.pinball.IView;
import com.jakemadethis.pinball.Screen;
import com.jakemadethis.pinball.ScreenTransition;

public class LoadingScreen extends Screen {
	
	private ShapeRenderer shapeRenderer;
	private boolean finished;
	
	public static IFactory<Screen> getFactory() {
		return new IFactory<Screen>() {
			@Override
			public Screen create() {
				return new LoadingScreen();
			}
		};
	}

	public LoadingScreen() {
		shapeRenderer = new ShapeRenderer();
		System.out.println("Finished is false");
		finished = false;
	}


	@Override
	public void drawBackground(SpriteBatch batch, float parentAlpha) {
		
		
		
		//batch.end();

		shapeRenderer.setProjectionMatrix(batch.getProjectionMatrix());
		shapeRenderer.setTransformMatrix(batch.getTransformMatrix());
    shapeRenderer.translate(0, 0, 0);
    
		shapeRenderer.begin(ShapeType.FilledRectangle);
		float progress = PinballAssets.getProgress();
		shapeRenderer.setColor(progress, 0f, 0f, parentAlpha);
		shapeRenderer.filledRect(20f, 300, (width-40f)*progress, 3);
		shapeRenderer.end();
		
		//batch.begin();
	}

	@Override
	public void thinkInternal(float timestep) {
		if (PinballAssets.update() && !finished) {
			System.out.println("Main Menu please");
			Pinball.setScreen(MainMenu.getFactory(), new ScreenTransition.SlideForward());
			finished = true;
		}
	}

}
