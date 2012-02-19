package com.jakemadethis.pinball;

/**
 * Something that can be drawn
 * @author Jake
 *
 */
public interface IDrawable {

	public Entity getEntity(); // TODO: don't want this
	public abstract void draw();

}