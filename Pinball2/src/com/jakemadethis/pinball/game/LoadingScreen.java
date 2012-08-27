package com.jakemadethis.pinball.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.jakemadethis.pinball.IState;
import com.jakemadethis.pinball.IView;

public class LoadingScreen implements IState, IView {
	
	private SpriteBatch spriteBatch;
	private ShapeRenderer shapeRenderer;

	public LoadingScreen() {
		spriteBatch = new SpriteBatch();
		shapeRenderer = new ShapeRenderer();
	}
	
	@Override
	public void run() {
		if (PinballAssets.update()) {
			Pinball.setMenu();
			return;
		}
		Gdx.gl20.glClearColor(0f, 0f, 0f, 1f);
		Gdx.gl20.glClear(GL20.GL_COLOR_BUFFER_BIT);

		shapeRenderer.begin(ShapeType.FilledRectangle);
		float progress = PinballAssets.getProgress();
		shapeRenderer.setColor(progress, 0f, 0f, 1f);
		shapeRenderer.filledRect(0, 0, Gdx.graphics.getWidth()*progress, Gdx.graphics.getHeight());
		shapeRenderer.end();
		
		spriteBatch.begin();
		//spriteBatch.draw(loading, width/2-32, height/2-32, 32, 32, 64, 64, 1, 1, 0, 0, 0, 64, 64, false, true);
		spriteBatch.end();
		
	}


	@Override
	public void render() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void think(float timeStep) {
		// TODO Auto-generated method stub
		
	}

}
