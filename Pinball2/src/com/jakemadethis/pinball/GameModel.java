package com.jakemadethis.pinball;

import java.util.LinkedList;

import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.Fixture;
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
	private final LinkedList<Flipper> flipperLeft = new LinkedList<Flipper>();
	private final LinkedList<Flipper> flipperRight = new LinkedList<Flipper>();
	
	public EventHandler<Object> newBallHandler = new EventHandler<Object>();
	public int combo = 0;
	public int balls = 3;
	
	private final Timer comboTimer = new Timer();
	private final Timer awesomeTimer = new Timer();
	public boolean awesomeMode = false;
	private final OutputHandler outputs = new OutputHandler("onReset");
	public boolean gameOver;
	
	
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
	@Override
	public void initGame() {
		
		//width = 480f;
		//height = 1000f;
		scale = 100f;
		
		ioManager.debugPrint();
		
		
		
		reset();
		
		scale = 1f;
		width /= 100f;
		height /= 100f;
	}
	
	
	@Override
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
		if (balls > 0) {
			outputs.invoke("onReset");
			newBallHandler.invoke(this, null);
		}
	}
	
	@Override
	public synchronized void think(float timestep, int iters) {
		// Don't allow world to be updated while stepping
		
		float dt = timestep / iters;
		for (int i = 0; i < iters; i++) {
			world.step(dt, 5, 5);
		}
		
		for (int i = 0; i < entities.size(); i++) {
			entities.get(i).think(timestep, this);
		}
		
		if (ball.getBody().getPosition().y > height + 1f) {
			ballLost();
		}
		
		if (!comboTimer.running()) combo = 0;
		if (!awesomeTimer.running()) awesomeMode = false;
	}
	


	
	
	
	private void ballLost() {
		balls--;
		reset();
		if (balls == 0) {
			gameOver = true;
		}
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
