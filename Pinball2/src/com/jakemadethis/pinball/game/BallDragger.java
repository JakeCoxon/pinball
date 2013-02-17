package com.jakemadethis.pinball.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.joints.MouseJoint;
import com.badlogic.gdx.physics.box2d.joints.MouseJointDef;

public class BallDragger {

	private boolean dragging;
	final private Vector3 mouse3d = new Vector3();
	final private Vector2 mouse2d = new Vector2();

	private MouseJoint mouseJoint;
	private final GameView view;
	private final GameModel model;
	
	public BallDragger(GameModel model, GameView view) {
		this.model = model;
		this.view = view;
	}
	
	public void think(float timestep) {
		if (dragging) {
			mouse3d.set(Gdx.input.getX(), Gdx.input.getY(), 0);
			view.worldCamera.unproject(mouse3d);
			mouse2d.set(mouse3d.x, mouse3d.y);
			mouseJoint.setTarget(mouse2d);
		}
	}

	public boolean touchDown(int x, int y, int pointer, int button) {
		if (button == 1) {
			if (model.getBall().isActive()) {
		
				dragging = true;
				
				view.setScrolling(false);
				Vector3 mouse = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0);
				view.worldCamera.unproject(mouse);
				//Vector2 mouse2 = new Vector2(mouse.x, mouse.y);
				
	      BodyDef groundBodyDef = new BodyDef();
	      groundBodyDef.position.set(model.getBall().getBody().getPosition());
	      Body groundBody = model.world.createBody(groundBodyDef);
	      
				MouseJointDef mjd = new MouseJointDef();
	      mjd.bodyA               = groundBody;
	      mjd.bodyB               = model.getBall().getBody();
	      mjd.dampingRatio        = 1f;
	      mjd.frequencyHz         = 10;
	      mjd.maxForce                    = (200.0f * model.getBall().getBody().getMass());
	      mjd.collideConnected= true;
	      mjd.target.set(model.getBall().getBody().getPosition());
				mouseJoint = (MouseJoint) model.world.createJoint(mjd);
				
				return true;
			}
		}
		return false;
	}

	public boolean touchUp(int x, int y, int pointer, int button) {
		if (button == 1 && dragging) {
			dragging = false;
			model.world.destroyJoint(mouseJoint);
			view.setScrolling(true);
			return true;
		}
		return false;
	}
}
