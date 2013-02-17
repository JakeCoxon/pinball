package com.jakemadethis.pinball;

import java.util.LinkedList;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.badlogic.gdx.physics.box2d.World;
import com.jakemadethis.pinball.io.IOManager;
import com.jakemadethis.pinball.io.SlotHandler;
import com.jakemadethis.pinball.io.SignalHandler;
import com.jakemadethis.pinball.level.Ball;
import com.jakemadethis.pinball.level.Bumper;
import com.jakemadethis.pinball.level.Flipper;
import com.jakemadethis.pinball.level.Kicker;
import com.jakemadethis.pinball.level.Light;
import com.jakemadethis.pinball.level.Sensor;
import com.jakemadethis.pinball.level.Wall;

/**
 * The base that holds the Box2d world, entities, physics listeners,
 * io manager
 * Also has helper methods for adding entities
 * @author Jake
 *
 */
public class BaseModel implements ContactListener {
	
	public static class EntityArgs {
		final private Entity entity;
		public EntityArgs(Entity entity) { this.entity = entity; }
		public Entity getEntity() { return entity; }
	}
	

	protected IOManager ioManager;
	public World world;
	
	private int score = 0;
	
	public EventHandler<EntityArgs> entityAddedHandler = new EventHandler<EntityArgs>();
	public EventHandler<EntityArgs> entityRemovedHandler = new EventHandler<EntityArgs>();
	public LinkedList<Entity> entities;
	
	public float width, height;
	
	public BaseModel() {
		entities = new LinkedList<Entity>();
		clear();
		
	}
	
	

	public void clear() {
		if (entities != null) {
			while(!entities.isEmpty()) {
				remove(entities.getLast());
			}
		}
		world = new World(new Vector2(0, 5f), true);
		world.setContactListener(this);
		ioManager = new IOManager();
	}
	public void initGame() {
	}
	
	public IOManager getIoManager() {
		return ioManager;
	}
	
	public int getScore() {
		return score;
	}
	public void addScore(int num) {
		score += num;
	}

	/**
	 * Set the size of the model
	 * @param width
	 * @param height
	 */
	public void setSize(float width, float height) {
		this.width = width;
		this.height = height;
	}

	/**
	 * Run every update frame
	 * @param timestep
	 * @param iters
	 */
	public synchronized void think(float timestep, int iters) {
	}
	
	/**
	 * Sets the name of the entity in the io manager
	 * @param name
	 * @param entity
	 */
	public void setName(String name, Entity entity) {
		if (name.length() == 0) return;
		ioManager.add(name, entity);
	}
	
	/**
	 * Sets the name of these handlers in the io manager
	 * @param name
	 * @param inputHandler
	 * @param outputHandler
	 */
	public void setName(String name, SlotHandler inputHandler, SignalHandler outputHandler) {
		if (name.length() == 0) return;
		ioManager.add(name, inputHandler, outputHandler);
	}
	
	protected <T extends Entity> T add(String name, T entity) {
		add(entity);
		ioManager.add(name, entity);
		return entity;
	}
	
	/**
	 * Adds an entity to the game and invokes the entityAdded handler
	 * @param <T>
	 * @param entity
	 * @return
	 */
	public <T extends Entity> T add(T entity) {
		entities.add(entity);
		entityAddedHandler.invoke(this, new EntityArgs(entity));
		return entity;
	}
	
	/**
	 * Removes an entity to the game and invokes the entityRemoved handler
	 * @param <T>
	 * @param entity
	 * @return
	 */
	public void remove(Entity entity) {
		entities.remove(entity);
		entityRemovedHandler.invoke(this, new EntityArgs(entity));
	}
	

	@Override
	public void beginContact(Contact contact) {
		
	}

	@Override
	public void endContact(Contact contact) {
		
	}

	@Override
	public void preSolve(Contact contact, Manifold oldManifold) {
		
	}

	@Override
	public void postSolve(Contact contact, ContactImpulse impulse) {
		
	}


	
}
