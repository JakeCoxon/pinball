package com.jakemadethis.pinball;


import java.util.HashMap;

import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;
import com.jakemadethis.pinball.game.GameModel;
import com.jakemadethis.pinball.io.Connection;
import com.jakemadethis.pinball.io.Slot;
import com.jakemadethis.pinball.io.Slot.EventArgs;
import com.jakemadethis.pinball.io.SlotHandler;
import com.jakemadethis.pinball.io.Signal;
import com.jakemadethis.pinball.io.SignalHandler;
import com.jakemadethis.pinball.level.Ball;
import com.jakemadethis.pinball.level.EntityVisitor;
import com.jakemadethis.pinball.level.Level;

/**
 * Entity
 * Has an optional IDrawable
 * Holds inputs and outputs
 * @author Jake
 *
 */
public abstract class Entity {

	public int ID;
	protected SlotHandler slots;
	protected SignalHandler signals;
	
	public void setID(int id) {
		this.ID = id;
	}
	
	public Slot getSlot(String name) {
		return slots.get(name);
	}
	
	public Signal getSignal(String name) {
		return signals.get(name);
	}
	
	public SlotHandler getSlots() {
		return slots;
	}
	
	public SignalHandler getSignals() {
		return signals;
	}
	
	public Connection addConnection(String signalName, Entity target, String slotName) {
		return addConnection(this, signalName, target, slotName);
	}
	
	/**
	 * Called when a ball hits this entity
	 * @param ball
	 * @param body
	 * @param model
	 */
	public abstract void handleCollision(Ball ball, Body body, GameModel model);
	
	/**
	 * Called every think frame
	 * @param timestep
	 * @param model
	 */
	public abstract void think(float timestep, GameModel model);
	
	/**
	 * Accepts the entity visitor
	 * @param <A> the argument passed to the visitor
	 * @param <R> the return value from the visitor
	 * @param visitor
	 * @param view
	 * @return
	 */
	public abstract <A, R> R accept(EntityVisitor<R, A> visitor, A arg);
	
	/**
	 * Adds a connection between two entities
	 * @param entity
	 * @param signalName
	 * @param target
	 * @param slotName
	 * @return
	 */
	public static Connection addConnection(Entity entity, String signalName, Entity target, String slotName) {
		return Connection.add(entity.signals, signalName, target.slots, slotName);
	}
	
	/**
	 * Invokes an input on an entity
	 * @param action
	 */
	protected void invokeSignal(String action) {
		slots.invokeFromSignal(this, new EventArgs(action));
	}
}
