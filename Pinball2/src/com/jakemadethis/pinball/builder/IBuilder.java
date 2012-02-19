package com.jakemadethis.pinball.builder;

/**
 * An interface for constructing objects using a BuilderFactory
 * @author Jake
 *
 */
public interface IBuilder {

	/**
	 * Creates the whole tree of nodes
	 * @param <R> Return type of the factory
	 * @param <A> Argument type of the factory
	 * @param factory The factory instance to build with
	 * @param arg The value of the argument to pass to the factory
	 * @return The constructed value of the topNode
	 */
	public abstract <R> R create(BuilderFactory<R> factory);

}