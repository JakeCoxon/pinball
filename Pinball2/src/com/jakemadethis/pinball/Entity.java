package com.jakemadethis.pinball;


import com.badlogic.gdx.physics.box2d.Body;
import com.jakemadethis.pinball.entities.Ball;
import com.jakemadethis.pinball.entities.EntityVisitor;
import com.jakemadethis.pinball.io.Connection;
import com.jakemadethis.pinball.io.Input.EventArgs;
import com.jakemadethis.pinball.io.InputHandler;
import com.jakemadethis.pinball.io.OutputHandler;

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
	 * Creates an IDrawable
	 * @param <A>
	 * @param visitor
	 * @param view
	 * @return
	 */
	public abstract <A> IDrawable accept(EntityVisitor<IDrawable, A> visitor, A view);
	
	public static Connection addConnection(Entity entity, String outputName, Entity target, String inputName) {
		return Connection.add(entity.outputs, outputName, target.inputs, inputName);
	}
	
	public void invokeInput(String action) {
		inputs.invoke(this, new EventArgs(action));
	}
}
