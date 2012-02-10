package com.jakemadethis.pinball;

import java.util.LinkedList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.VertexAttribute;
import com.badlogic.gdx.graphics.VertexAttributes.Usage;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.physics.box2d.Contact;


import com.jakemadethis.pinball.entities.Ball;
import com.jakemadethis.pinball.entities.Bumper;
import com.jakemadethis.pinball.entities.Flipper;
import com.jakemadethis.pinball.entities.IElement;
import com.jakemadethis.pinball.entities.Sensor;
import com.jakemadethis.pinball.entities.Wall;
import com.jakemadethis.pinball.entities.WallArc;
import com.jakemadethis.pinball.entities.WallPath;
import com.jakemadethis.pinball.entities.Flipper.Type;
import com.jakemadethis.pinball.io.Connection;
import com.jakemadethis.pinball.io.IOManager;
import com.jakemadethis.pinball.io.OutputHandler;

public class GameModel extends BaseModel {

	
	private Ball ball;
	private Flipper flipperLeft;
	private Flipper flipperRight;
	private int score = 0;
	public int combo = 0;
	private Timer comboTimer = new Timer();
	private Timer awesomeTimer = new Timer();
	public boolean awesomeMode = false;
	
	
	public GameModel() {
	}
	
	private OutputHandler outputs = new OutputHandler("onReset");
	
	public int getScore() {
		return score;
	}
	
	public void engageFlipper(boolean active) {
		flipperLeft.setFlipperEngaged(active);
		flipperRight.setFlipperEngaged(active);
	}
	
	public Ball getBall() {
		return ball;
	}
	
	/**
	 * Called after view is created
	 */
	public void initGame() {
		
		width = 480f;
		height = 1000f;
		scale = 100f;

		ioManager.add("level", null, outputs);
		//addBox(100f, 100f, 100f, 100f);
		ball = addBall(width-15f, height-15f, 15f);
		
		setName("sensor", addSensor(width-15f, height-260f, 2f));
		
		addBumper(130f, 110f, 50f);
		addBumper(300f, 150f, 50f);
		addBumper(328f, 300f, 64f);
		
		float rest = 0.5f;

		float mid = (15f + width-30f)/2;
		flipperLeft = addFlipper(mid - 120f, height-80f, 100f, Flipper.Type.LEFT);
		flipperRight = addFlipper(mid + 120f, height-80f, 100f, Flipper.Type.RIGHT);

		addWall(width-30f, height-200f, width-30f, height, rest);
		
		setName("toggleWall", addWall(width-30f, height-200f, width, height-230f, rest));
		
		//addWall(15, height-15, width-15, height - 15, 0);

		
		addWall(0, 300f, 90f, 370f, rest);
		
		addWall(105.5f,915f,0.0f,864f, rest);
		addWall(448f,864f,360.8f,911.6f, rest);
		
		addWall(0.0f,544f,89.2f,369.4f, rest);
		
		//add(new ParticleEmitter(width/100f/2f, height/100f/2f));
		
		float r = 100;
		addWallArc(width-r, r, r, r, (float) (-Math.PI/2), 0, 8);
		addWallArc(r, r, r, r, (float) (-Math.PI), (float) (-Math.PI/2), 8);

		addWall(r, 0, width-r, 0, rest);

		addWall(0, r, 0, 864f, rest);
		addWall(width, r, width, height, rest);
		
		//Entity.addConnection(sensor, "onSense", toggleWall, "turnOn");
		//Connection.add(outputs, "onReset", toggleWall.inputs, "turnOff");
		ioManager.addEvent("sensor", "onSense", "toggleWall", "turnOn");
		ioManager.addEvent("level", "onReset", "toggleWall", "turnOff");
		
		//ioManager.testAdd();
		ioManager.debugPrint();
		
		
		
		reset();
		
		scale = 1f;
		width /= 100f;
		height /= 100f;
	}
	
	
	public void addScore(int score) {
		if (comboTimer.running()) {
			comboTimer.start(1f);
			combo ++;
		}
		else {
			comboTimer.start(1f);
			combo = 1;
		}
		
		if (combo >= 5) {
			awesomeMode = true;
			awesomeTimer.start(5f);
		}
		
		if (awesomeMode) score *= 2;
		
		this.score += score;
		
		
	}
	

	public synchronized void reset() {
		getBall().reset();
		outputs.invoke("onReset");
	}
	
	@Override
	public synchronized void think(float timestep, int iters) {
		// Don't allow world to be updated while stepping
		
		float dt = timestep / iters;
		for (int i = 0; i < iters; i++) {
			world.step(dt, 5, 5);
		}
		
		for (Entity ent : entities) 
			ent.think(timestep, this);
		
		if (ball.getBody().getPosition().y > height + 1f) {
			reset();
		}
		
		if (!comboTimer.running()) combo = 0;
		if (!awesomeTimer.running()) awesomeMode = false;
	}
	


	
	
	
	@Override
	public void beginContact(Contact contact) {
		Ball b = null;
		Fixture f = null;
		if (contact.getFixtureA().getBody() == ball.getBody()) {
			b = (Ball) contact.getFixtureA().getBody().getUserData();
			f = contact.getFixtureB();
		} else if (contact.getFixtureB().getBody() == ball.getBody()) {
			b = (Ball) contact.getFixtureB().getBody().getUserData();
			f = contact.getFixtureA();
		}
		
		if (b == null) return;
		
		Entity ent = (Entity)f.getBody().getUserData();
		ent.handleCollision(b, f.getBody(), this);
		
		int score;
		if ((score = ((IElement) ent).getScore()) > 0)
			addScore(score);
	}

	
}
