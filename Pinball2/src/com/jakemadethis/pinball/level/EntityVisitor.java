package com.jakemadethis.pinball.level;

import com.jakemadethis.pinball.Entity;

/**
 * Interface for specifying actions per entity
 * @author Jake
 *
 * @param <R> Return type
 * @param <A> Argument type
 */
public interface EntityVisitor <R, A> {
	R visit(Entity entity, A arg);
	R visit(Wall wallPath, A arg);
	R visit(Ball ball, A arg);
	R visit(Bumper bumper, A arg);
	R visit(Flipper flipper, A arg);
	R visit(Sensor sensor, A arg);
	R visit(Light light, A arg);
	R visit(Kicker kicker, A arg);
}
