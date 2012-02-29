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
import com.jakemadethis.pinball.io.InputHandler;
import com.jakemadethis.pinball.io.OutputHandler;
import com.jakemadethis.pinball.level.Ball;
import com.jakemadethis.pinball.level.Bumper;
import com.jakemadethis.pinball.level.Flipper;
import com.jakemadethis.pinball.level.Light;
import com.jakemadethis.pinball.level.Sensor;
import com.jakemadethis.pinball.level.Wall;
import com.jakemadethis.pinball.level.WallArc;

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
	
	// Scale is used when creating entities
	public float scale = 1f;
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
		world = new World(new Vector2(0, 8f), true);
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
	public void setName(String name, InputHandler inputHandler, OutputHandler outputHandler) {
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
	
	public synchronized Sensor addSensor(float x, float y, float r) {
		return add(new Sensor(x / scale, y / scale, r / scale));
	}

	public synchronized Ball addBall(float cx, float cy, float radius) {
		return add(new Ball(world, cx / scale, cy / scale, radius / scale));
	}
	
	public synchronized Bumper addBumper(float cx, float cy, float radius) {
		return add(new Bumper(world, cx / scale, cy / scale, radius / scale));
	}
	
	public synchronized Flipper addFlipper(float cx, float cy, float length, Flipper.Type type) {
		return add(new Flipper(world, cx / scale, cy / scale, length / scale, type));
	}
	
	public synchronized Wall addWall(float x0, float y0, float x1, float y1, float restitution) {
		return add(new Wall(world, x0 / scale, y0 / scale, x1 / scale, y1 / scale, restitution));
	}
	public synchronized Wall addWallPath(float[] path, float restitution) {
		for (int i=0; i < path.length; i++) path[i] = path[i]/scale;
		return add(new Wall(world, path, restitution));
	}
	public synchronized WallArc addWallArc(float cx, float cy, float xradius, float yradius, float minangle, float maxangle, int numSegments) {
		return add(new WallArc(world, cx/scale, cy/scale, xradius/scale, yradius/scale, minangle, maxangle, numSegments));
	}
	public Light addLight(float x, float y, float w, float h, Color color) {
		return add(new Light(x/scale, y/scale, w/scale, h/scale, color));
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
