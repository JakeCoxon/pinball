package com.jakemadethis.pinball.views;

import com.jakemadethis.pinball.GameView;
import com.jakemadethis.pinball.IDrawable;
import com.jakemadethis.pinball.entities.Ball;
import com.jakemadethis.pinball.entities.Bumper;
import com.jakemadethis.pinball.entities.EntityVisitor;
import com.jakemadethis.pinball.entities.Flipper;
import com.jakemadethis.pinball.entities.Sensor;
import com.jakemadethis.pinball.entities.WallPath;

public class DrawableVisitor implements EntityVisitor<IDrawable, GameView> {

	@Override
	public IDrawable visit(WallPath wallPath, GameView view) {
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
		// TODO Auto-generated method stub
		return null;
	}
	
	

}
