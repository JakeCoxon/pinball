package com.jakemadethis.pinball;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.WidgetGroup;

public abstract class Screen implements InputProcessor {
	
	public static Camera camera;
	
	protected final MyStage stage;
	protected Matrix4 transformMatrix = new Matrix4().idt();
	protected Matrix4 cameraCombined = new Matrix4();
	public float alpha = 1f;
	protected float width;
	protected float height;
	
	protected ScreenTransition transition = null;
	protected Screen parentScreen = null;

	public Screen(MyStage stage) {
		this.stage = stage;
		stage.setCamera(camera);
		this.width = stage.width();
		this.height = stage.height();
	}
	public Screen(float width, float height) {
		this(new MyStage(width, height, true));
	}
	public Screen() {
    this(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
  }
	
	public Stage getStage() {
		return stage;
	}
	
	
	public Screen getParentScreen() {
		return parentScreen;
	}
	public void setParentScreen(Screen screen) {
		parentScreen = screen;
	}
	
	public Matrix4 getTransformMatrix() {
		return transformMatrix;
	}
	public void setTransformMatrix(Matrix4 transformMatrix) {
		this.transformMatrix = transformMatrix;
	}
	
	public ScreenTransition getTransition() {
		return transition;
	}
	public void setTransition(ScreenTransition transition) {
		this.transition = transition;
	}
	
	public void draw() {
		if (parentScreen != null) {
			parentScreen.draw();
		}
		
		cameraCombined.set(camera.combined).mul(transformMatrix);
		stage.getSpriteBatch().setProjectionMatrix(cameraCombined);
		
		drawBackground(stage.getSpriteBatch(), alpha);
		stage.draw(alpha);
		
		
	}
	
	public void drawBackground(SpriteBatch spriteBatch, float parentAlpha) {
		
	}
	
	public void think(float timestep) {
		
		/*if (parentScreen != null)
			parentScreen.transformMatrix.set(transformMatrix);
		
		if (transition != null)
			transition.think(timestep);
		
		thinkInternal(timestep);
		
		if (parentScreen != null)
			parentScreen.think(timestep);*/
		
	}
	public void thinkInternal(float timestep) {
		
	}
	
	
	
	@Override
	public boolean keyDown(int keycode) {
		// TODO Auto-generated method stub
		return false;
	}
	@Override
	public boolean keyUp(int keycode) {
		// TODO Auto-generated method stub
		return false;
	}
	@Override
	public boolean keyTyped(char character) {
		// TODO Auto-generated method stub
		return false;
	}
	@Override
	public boolean touchDown(int x, int y, int pointer, int button) {
		// TODO Auto-generated method stub
		return false;
	}
	@Override
	public boolean touchUp(int x, int y, int pointer, int button) {
		// TODO Auto-generated method stub
		return false;
	}
	@Override
	public boolean touchDragged(int x, int y, int pointer) {
		// TODO Auto-generated method stub
		return false;
	}
	@Override
	public boolean touchMoved(int x, int y) {
		// TODO Auto-generated method stub
		return false;
	}
	
	@Override
	public boolean scrolled(int amount) {
		// TODO Auto-generated method stub
		return false;
	}
	
	public void detatchParent() {
		parentScreen = null;
		transition = null;
	}

	
}
