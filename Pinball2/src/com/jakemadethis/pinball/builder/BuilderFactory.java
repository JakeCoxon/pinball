package com.jakemadethis.pinball.builder;

/**
 * 
 * @author Jake
 *
 * @param <R> The return type
 * @param <A> The argument type
 */
public interface BuilderFactory<R> {
	R create(BuilderNode node);
}
