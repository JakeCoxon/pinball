package com.jakemadethis.pinball.views;

import com.jakemadethis.pinball.Entity;
import com.jakemadethis.pinball.GameView;
import com.jakemadethis.pinball.IDrawable;
import com.jakemadethis.pinball.level.Ball;
import com.jakemadethis.pinball.level.Bumper;
import com.jakemadethis.pinball.level.EntityVisitor;
import com.jakemadethis.pinball.level.Flipper;
import com.jakemadethis.pinball.level.Light;
import com.jakemadethis.pinball.level.Sensor;
import com.jakemadethis.pinball.level.Wall;

public class DrawableVisitor implements EntityVisitor<IDrawable, GameView> {

	@Override
	public IDrawable visit(Entity entity, GameView arg) {
		return entity.accept(this, arg);
	}
	
	@Override
	public IDrawable visit(Wall wallPath, GameView view) {
		return new WallPathDrawable(wallPath, view);
	}

	@Override
	public IDrawable visit(Ball ball, GameView view) {
		return new BallDrawable(ball, view);
	}

	@Override
	public IDrawable visit(Bumper bumper, GameView view) {
		return new BumperDrawable(bumper, view);
	}

	@Override
	public IDrawable visit(Flipper flipper, GameView view) {
		return new FlipperDrawable(flipper, view);
	}

	@Override
	public IDrawable visit(Sensor sensor, GameView view) {
		return new SensorDrawable(sensor, view);
	}
	
	@Override
	public IDrawable visit(Light light, GameView view) {
		return new LightDrawable(light, view);
	}
	
	

}