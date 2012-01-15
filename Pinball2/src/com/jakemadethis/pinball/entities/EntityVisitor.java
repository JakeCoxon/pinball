package com.jakemadethis.pinball.entities;

import com.jakemadethis.pinball.IDrawable;

public interface EntityVisitor <T, A> {
	T visit(WallPath wallPath, A arg);
	T visit(Ball ball, A arg);
	T visit(Bumper bumper, A arg);
	T visit(Flipper flipper, A arg);
	T visit(Sensor sensor, A arg);
}
