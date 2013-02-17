package com.jakemadethis.pinball.game;

import java.util.ArrayList;
import java.util.LinkedList;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.jakemadethis.pinball.BaseModel;
import com.jakemadethis.pinball.Entity;
import com.jakemadethis.pinball.EventHandler;
import com.jakemadethis.pinball.IElement;
import com.jakemadethis.pinball.LevelException;
import com.jakemadethis.pinball.Timer;
import com.jakemadethis.pinball.io.SignalHandler;
import com.jakemadethis.pinball.level.Ball;
import com.jakemadethis.pinball.level.Flipper;
import com.jakemadethis.pinball.level.Flipper.Type;
import com.jakemadethis.pinball.level.Gem;

/**
 * Model for the game world
 * @author Jake
 *
 */
public class GameModel extends BaseModel {

	
	private static final float BALL_RADIUS = 0.15f;
	private Ball ball;
	private final LinkedList<Flipper> flipperLeft = new LinkedList<Flipper>();
	private final LinkedList<Flipper> flipperRight = new LinkedList<Flipper>();

	public EventHandler<Object> newBallHandler = new EventHandler<Object>();
	public EventHandler<Object> gameOverHandler = new EventHandler<Object>();
	public int combo = 0;
	public int balls = 3;
	
	private final Timer comboTimer = new Timer();
	private final Timer awesomeTimer = new Timer();
	public boolean awesomeMode = false;
	private final SignalHandler outputs = new SignalHandler("onReset");
	public boolean gameOver;
	private Vector2 initialBallPos;
	
	
	public GameModel() {
		ioManager.add("level", null, outputs);
		
		
		new Gem(this, 2f, 4f);
	}
	
	
	
	
	public void engageLeftFlippers(boolean active) {		
		for (Flipper f : flipperLeft) 
			f.setFlipperEngaged(active);
	}
	public void engageRightFlippers(boolean active) {
		for (Flipper f : flipperRight)
			f.setFlipperEngaged(active);
	}
	
	public Ball getBall() {
		return ball;
	}
	
	public void addBallPos(float x, float y) {
		initialBallPos = new Vector2(x, y);
	}
	
	
	public <T extends Entity> T add(T entity) {
		entity = super.add(entity);
		if (entity instanceof Ball) {
			ball = (Ball) entity;
		} else if (entity instanceof Flipper) {
			Flipper flipper = (Flipper) entity;
			if (flipper.getType() == Type.LEFT) flipperLeft.add(flipper);
			else if (flipper.getType() == Type.RIGHT) flipperRight.add(flipper);
		}
		return entity;
	}
	
	/**
	 * Called after view is created
	 */
	@Override
	public void initGame() {
		
		//width = 480f;
		//height = 1000f;
		//scale = 100f;
		
		ioManager.debugPrint();
		
		
		
		reset();
		
		//scale = 1f;
		//width /= 100f;
		//height /= 100f;
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
		if(ball == null) 
			ball = add(new Ball(this, initialBallPos.x, initialBallPos.y, BALL_RADIUS));
		
		ball.reset();
		ball.setPos(initialBallPos);
		
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
			gameOverHandler.invoke(this, null);
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
