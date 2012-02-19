package com.jakemadethis.pinballeditor.views;

import com.jakemadethis.pinball.Entity;
import com.jakemadethis.pinball.IDrawable;
import com.jakemadethis.pinball.level.Ball;
import com.jakemadethis.pinball.level.Bumper;
import com.jakemadethis.pinball.level.EntityVisitor;
import com.jakemadethis.pinball.level.Flipper;
import com.jakemadethis.pinball.level.Sensor;
import com.jakemadethis.pinball.level.Wall;
import com.jakemadethis.pinballeditor.EditorView;

public class DrawableVisitor implements EntityVisitor<IDrawable, EditorView> {

	@Override
	public IDrawable visit(Entity entity, EditorView arg) {
		return entity.accept(this, arg);
	}
	
	@Override
	public IDrawable visit(Wall wallPath, EditorView view) {
		return new WallPathDrawable(wallPath, view);
	}

	@Override
	public IDrawable visit(Ball ball, EditorView view) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IDrawable visit(Bumper bumper, EditorView view) {
		return new BumperDrawable(bumper, view);
	}

	@Override
	public IDrawable visit(Flipper flipper, EditorView view) {
		return new FlipperDrawable(flipper, view);
	}

	@Override
	public IDrawable visit(Sensor sensor, EditorView view) {
		// TODO Auto-generated method stub
		return null;
	}
	
	

}
