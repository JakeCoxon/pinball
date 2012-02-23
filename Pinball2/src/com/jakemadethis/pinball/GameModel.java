package com.jakemadethis.pinball;

import java.util.LinkedList;

import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Contact;


import com.jakemadethis.pinball.io.OutputHandler;
import com.jakemadethis.pinball.level.Ball;
import com.jakemadethis.pinball.level.Flipper;
import com.jakemadethis.pinball.level.Flipper.Type;

/**
 * Model for the game world
 * @author Jake
 *
 */
public class GameModel extends BaseModel {

	
	private Ball ball;
	private LinkedList<Flipper> flipperLeft = new LinkedList<Flipper>();
	private LinkedList<Flipper> flipperRight = new LinkedList<Flipper>();
	public int combo = 0;
	private Timer comboTimer = new Timer();
	private Timer awesomeTimer = new Timer();
	public boolean awesomeMode = false;
	private OutputHandler outputs = new OutputHandler("onReset");
	
	
	public GameModel() {
		ioManager.add("level", null, outputs);
	}
	
	
	
	
	public void engageFlipper(boolean active) {
		for (Flipper f : flipperLeft) 
			f.setFlipperEngaged(active);
		for (Flipper f : flipperRight)
			f.setFlipperEngaged(active);
	}
	
	public Ball getBall() {
		return ball;
	}
	
	@Override
	public synchronized Flipper addFlipper(float cx, float cy, float length,
			Type type) {
		Flipper f = super.addFlipper(cx, cy, length, type);
		if (type == Type.LEFT) flipperLeft.add(f);
		else if (type == Type.RIGHT) flipperRight.add(f);
		return f;
	}
	
	@Override
	public synchronized Ball addBall(float cx, float cy, float radius) {
		if (ball != null) throw new LevelException("Only 1 ball currently allowed");
		Ball b = super.addBall(cx, cy, radius);
		ball = b;
		return b;
	}
	
	/**
	 * Called after view is created
	 */
	public void initGame() {
		
		width = 480f;
		height = 1000f;
		scale = 100f;

		//addBox(100f, 100f, 100f, 100f);
		//ball = addBall(width-15f, height-15f, 15f);
		
		//setName("sensor", addSensor(width-15f, height-260f, 2f));
		
		//addBumper(130f, 110f, 50f);
		//addBumper(300f, 150f, 50f);
		//addBumper(328f, 300f, 64f);
		
		float rest = 0.5f;

		float mid = (15f + width-30f)/2;
		//flipperLeft = addFlipper(mid - 120f, height-80f, 100f, Flipper.Type.LEFT);
		//flipperRight = addFlipper(mid + 120f, height-80f, 100f, Flipper.Type.RIGHT);

		//addWall(width-30f, height-200f, width-30f, height, rest);
		
		//setName("toggleWall", addWall(width-30f, height-200f, width, height-230f, rest));
		
		//addWall(15, height-15, width-15, height - 15, 0);

		
		//addWall(0, 300f, 90f, 370f, rest);
		//addWall(0.0f,544f,89.2f,369.4f, rest);
		
		//addWall(105.5f,915f,0.0f,864f, rest);
		//addWall(448f,864f,360.8f,911.6f, rest);
		
		
		//add(new ParticleEmitter(width/100f/2f, height/100f/2f));
		
		float r = 100;
		//addWallArc(width-r, r, r, r, (float) (-Math.PI/2), 0, 8);
		//addWallArc(r, r, r, r, (float) (-Math.PI), (float) (-Math.PI/2), 8);

		// Top
		//addWall(r, 0, width-r, 0, rest);

		//addWall(0, r, 0, 864f, rest);
		//addWall(width, r, width, height, rest);
		
		//Entity.addConnection(sensor, "onSense", toggleWall, "turnOn");
		//Connection.add(outputs, "onReset", toggleWall.inputs, "turnOff");
		//ioManager.addEvent("sensor", "onSense", "toggleWall", "turnOn");
		//ioManager.addEvent("level", "onReset", "toggleWall", "turnOff");
		
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
		
		super.addScore(score);
		
	}
	

	public synchronized void reset() {
		getBall().reset();
		awesomeMode = false;
		combo = 0;
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
		
		// Fixture A or B could be the ball so find which one
		if (contact.getFixtureA().getBody() == ball.getBody()) {
			b = (Ball) contact.getFixtureA().getBody().getUserData();
			f = contact.getFixtureB();
		} else if (contact.getFixtureB().getBody() == ball.getBody()) {
			b = (Ball) contact.getFixtureB().getBody().getUserData();
			f = contact.getFixtureA();
		}
		
		// If neither is the ball exit
		if (b == null) return;
		
		Entity ent = (Entity)f.getBody().getUserData();
		ent.handleCollision(b, f.getBody(), this);
		
		int score;
		if ((score = ((IElement) ent).getScore()) > 0)
			addScore(score);
	}

	
}
