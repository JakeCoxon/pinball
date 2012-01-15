package com.jakemadethis.pinballeditor.views;

import com.jakemadethis.pinball.IDrawable;
import com.jakemadethis.pinball.entities.Ball;
import com.jakemadethis.pinball.entities.Bumper;
import com.jakemadethis.pinball.entities.EntityVisitor;
import com.jakemadethis.pinball.entities.Flipper;
import com.jakemadethis.pinball.entities.Sensor;
import com.jakemadethis.pinball.entities.WallPath;
import com.jakemadethis.pinballeditor.EditorView;

public class DrawableVisitor implements EntityVisitor<IDrawable, EditorView> {

	@Override
	public IDrawable visit(WallPath wallPath, EditorView view) {
		return new WallPathDrawable(wallPath, view);
	}

	@Override
	public IDrawable visit(Ball ball, EditorView view) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IDrawable visit(Bumper bumper, EditorView view) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IDrawable visit(Flipper flipper, EditorView view) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IDrawable visit(Sensor sensor, EditorView view) {
		// TODO Auto-generated method stub
		return null;
	}
	
	

}
