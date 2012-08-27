package com.jakemadethis.pinball;


import java.util.HashMap;

import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;
import com.jakemadethis.pinball.game.GameModel;
import com.jakemadethis.pinball.io.Connection;
import com.jakemadethis.pinball.io.Input.EventArgs;
import com.jakemadethis.pinball.io.InputHandler;
import com.jakemadethis.pinball.io.OutputHandler;
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
	public InputHandler inputs;
	public OutputHandler outputs;
	
	public void setID(int id) {
		this.ID = id;
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
	 * @param outputName
	 * @param target
	 * @param inputName
	 * @return
	 */
	public static Connection addConnection(Entity entity, String outputName, Entity target, String inputName) {
		return Connection.add(entity.outputs, outputName, target.inputs, inputName);
	}
	
	/**
	 * Invokes an input on an entity
	 * @param action
	 */
	public void invokeInput(String action) {
		inputs.invokeFromInput(this, new EventArgs(action));
	}
}
