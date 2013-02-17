package com.jakemadethis.pinball;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.math.Vector3;

public class ScreenCamera extends PerspectiveCamera {
	private float clickPlane;
	private double cameraDistance;

	public ScreenCamera(float fov, float width, float height) {
		super(fov, width, height);
		up.set(0, -1f, 0);
		direction.set(0, 0, 1f);
		
		near = 1f;
		
		cameraDistance = (height)/2f / Math.tan(fov/2f * (Math.PI / 180d));
		
		
		position.set(Gdx.graphics.getWidth() / 2f, Gdx.graphics.getHeight() / 2f, (float) -cameraDistance);
		far = 2000f;
		
		clickPlane = (float)(cameraDistance / (far - near)); // TODO: Check this
		update();
	}
	
	public double getCameraDistance() {
		return cameraDistance;
	}
	
	@Override
	public void unproject(Vector3 vec, float viewportX, float viewportY,
			float viewportWidth, float viewportHeight) {
		vec = vec.cpy(); vec.z = clickPlane;
		super.unproject(vec, viewportX, viewportY, viewportWidth, viewportHeight);
	}
}
