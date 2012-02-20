package com.jakemadethis.pinball.views;

import java.util.Random;

import com.badlogic.gdx.graphics.Color;
import com.jakemadethis.pinball.Entity;
import com.jakemadethis.pinball.GameView;
import com.jakemadethis.pinball.IDrawable;
import com.jakemadethis.pinball.Timer;
import com.jakemadethis.pinball.level.Bumper;

public class BumperDrawable implements IDrawable {

	private GameView view;
	private int hits = 0;
	
	private final Color offColor = new Color( 1f, 1f, 1f, 1f );
	private final Color blendColor = new Color( 50f/255f, 204f/255f, 1f, 0.2f );
	
	private Timer timer = new Timer();
	private Random r;
	private Bumper bumper;
	
	public BumperDrawable(Bumper bumper, GameView view) {
		this.view = view;
		this.bumper = bumper;
		r = new Random();
	}
	
	@Override
	public Entity getEntity() {
		return bumper;
	}
	
	@Override
	public void draw() {
		// To check if the bumper has been hit, compare the number
		// of hits on the model to the one we store on the view
		if (bumper.getHits() > hits) {
			hits ++;
			timer.start(0.2f, true);
		}
		

		float radius = bumper.getRadius();
		float cx = bumper.getX();
		float cy = bumper.getY();
		
		// Draw the base shape
		view.world.setColor(offColor);
		view.world.draw(view.getSprite("bumper"), cx - radius, cy - radius, radius*2, radius*2);
		
		if (timer.running()) {
			// Draw some crazy effect over it

			view.world.setColor(blendColor);

			float a = timer.value(1f, 0f);
			for (int i = 0; i < 20; i+=2) {
				float x = a * (r.nextFloat() * 0.4f - 0.2f);
				float y = a * (r.nextFloat() * 0.4f - 0.2f);


				view.world.draw(view.getSprite("bumper"), cx - radius + x, cy - radius + y, radius*2, radius*2);
			}
		}
	}

}