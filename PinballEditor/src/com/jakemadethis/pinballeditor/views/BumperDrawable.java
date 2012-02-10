package com.jakemadethis.pinballeditor.views;

import java.util.Random;

import com.badlogic.gdx.graphics.Color;
import com.jakemadethis.pinball.Entity;
import com.jakemadethis.pinball.GameView;
import com.jakemadethis.pinball.IDrawable;
import com.jakemadethis.pinball.IView;
import com.jakemadethis.pinball.Timer;
import com.jakemadethis.pinball.entities.Bumper;
import com.jakemadethis.pinballeditor.EditorView;

public class BumperDrawable implements IDrawable {

	private EditorView view;
	private final Color color = new Color( 1f, 1f, 1f, 0.7f );
	private Bumper bumper;
	
	public BumperDrawable(Bumper bumper, EditorView view) {
		this.view = view;
		this.bumper = bumper;
	}
	@Override
	public Entity getEntity() {
		return bumper;
	}
	
	@Override
	public void draw() {
		float radius = bumper.getRadius();
		float cx = bumper.getX();
		float cy = bumper.getY();
		
		view.world.setColor(color);
		view.world.draw(view.getSprite("bumper"), cx - radius, cy - radius, radius*2, radius*2);
			
	}

}
