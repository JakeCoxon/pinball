package com.jakemadethis.pinball;

import java.util.LinkedList;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.badlogic.gdx.physics.box2d.World;
import com.jakemadethis.pinball.entities.Ball;
import com.jakemadethis.pinball.entities.Bumper;
import com.jakemadethis.pinball.entities.Flipper;
import com.jakemadethis.pinball.entities.Sensor;
import com.jakemadethis.pinball.entities.Wall;
import com.jakemadethis.pinball.entities.WallArc;
import com.jakemadethis.pinball.entities.WallPath;
import com.jakemadethis.pinball.io.IOManager;

public class BaseModel implements ContactListener {
	public static class EntityArgs {
		final private Entity entity;
		public EntityArgs(Entity entity) { this.entity = entity; }
		public Entity getEntity() { return entity; }
	}
	

	protected IOManager ioManager;
	public World world;
	protected float scale = 1f;
	public EventHandler<EntityArgs> entityAddedHandler = new EventHandler<EntityArgs>();
	public EventHandler<EntityArgs> entityRemovedHandler = new EventHandler<EntityArgs>();
	public LinkedList<Entity> entities;
	public float width, height;
	
	public BaseModel() {
		entities = new LinkedList<Entity>();
		world = new World(new Vector2(0, 8f), true);
		world.setContactListener(this);
		ioManager = new IOManager();
	}

	public void initGame() {
	}

	public synchronized void think(float timestep, int iters) {
	}
	public void setName(String name, Entity entity) {
		if (name.length() == 0) return;
		ioManager.add(name, entity);
	}
	
	private <T extends Entity> T add(String name, T entity) {
		add(entity);
		ioManager.add(name, entity);
		return entity;
	}
	private <T extends Entity> T add(T entity) {
		entities.add(entity);
		entityAddedHandler.invoke(this, new EntityArgs(entity));
		return entity;
	}
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
	public synchronized WallPath addWallPath(float[] path, float restitution) {
		for (int i=0; i < path.length; i++) path[i] = path[i]/scale;
		return add(new WallPath(world, path, restitution));
	}
	public synchronized WallArc addWallArc(float cx, float cy, float xradius, float yradius, float minangle, float maxangle, int numSegments) {
		return add(new WallArc(world, cx/scale, cy/scale, xradius/scale, yradius/scale, minangle, maxangle, numSegments));
	}

	@Override
	public void beginContact(Contact contact) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void endContact(Contact contact) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void preSolve(Contact contact, Manifold oldManifold) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void postSolve(Contact contact, ContactImpulse impulse) {
		// TODO Auto-generated method stub
		
	}
	
}
