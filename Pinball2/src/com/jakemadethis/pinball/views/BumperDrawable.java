package com.jakemadethis.pinball.views;

import java.util.Random;

import com.badlogic.gdx.graphics.Color;
import com.jakemadethis.pinball.Entity;
import com.jakemadethis.pinball.GameView;
import com.jakemadethis.pinball.IDrawable;
import com.jakemadethis.pinball.IView;
import com.jakemadethis.pinball.Timer;
import com.jakemadethis.pinball.entities.Bumper;

public class BumperDrawable implements IDrawable {

	private GameView view;
	private int hits = 0;
	private final Color onColor = new Color( 1f, 0f, 0f, 1f );
	private final Color offColor = new Color( 1f, 1f, 1f, 1f );
	private final Color blendColor = new Color( 50f/255f, 204f/255f, 1f, 0.2f );
	private Color color = new Color(1f,1f,1f,1f);
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
		if (bumper.getHits() > hits) {
			hits ++;
			timer.start(0.2f, true);
		}
		

		float radius = bumper.getRadius();
		float cx = bumper.getX();
		float cy = bumper.getY();
		
		if (timer.running()) {
			/*color.r = timer.value(onColor.r, offColor.r);
			color.g = timer.value(onColor.g, offColor.g);
			color.b = timer.value(onColor.b, offColor.b);
			color.a = timer.value(onColor.a, offColor.a);*/
			//view.world.drawTexture(cx, cy, radius*2, 
			//		radius*2, view.getSprite("bumper"), color);
			//view.getWorldDelegate().drawCircle(DrawMode.NORMAL, cx, cy, radius, radius, color);
			

			view.world.setColor(offColor);
			view.world.draw(view.getSprite("bumper"), cx - radius, cy - radius, radius*2, radius*2);
			
			/*if (coords == null) {
				coords = new float[20];
				for (int i =0; i < 20; i+=2) {
					float x = r.nextFloat() * 0.2f - 0.1f;
					float y = r.nextFloat() * 0.2f - 0.1f;
					coords[i] = x;
					coords[i+1] = y;
				}
			}*/
			view.world.setColor(blendColor);
			//view.world.setBlendFunction(srcFunc, dstFunc);
			float a = timer.value(1f, 0f);
			for (int i = 0; i < 20; i+=2) {
				float x = a * (r.nextFloat() * 0.4f - 0.2f);
				float y = a * (r.nextFloat() * 0.4f - 0.2f);


				view.world.draw(view.getSprite("bumper"), cx - radius + x, cy - radius + y, radius*2, radius*2);
				//view.world.drawTexture(cx+x, cy+y, radius*2, radius*2, view.getSprite("bumper"), blendColor);
			}
		}
		else {
			view.world.setColor(offColor);
			view.world.draw(view.getSprite("bumper"), cx - radius, cy - radius, radius*2, radius*2);
		}
			//view.world.drawTexture(cx, cy, radius*2, radius*2, view.getSprite("bumper"), offColor);
		//	view.getWorldDelegate().drawCircle(DrawMode.NORMAL, cx, cy, radius, radius, offColor);
	}

}
