/*package com.jakemadethis.pinball;

import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.World;

import com.jakemadethis.pinball.Rect.Drawable;
import com.jakemadethis.pinball.TextureHandler.Texture;

import android.util.Log;

public class TexturedRect extends Rect {
	
	

	private String textureStr;

	public TexturedRect(World world, float x, float y, float w, float h, String texture, boolean fixed) {
		super(world, x, y, w, h, fixed);
		this.textureStr = texture;
	}

	@Override
	public IDrawable createDrawable(IView view) {
		return new Drawable(view);
	}
	
	public class Drawable implements IDrawable {
		private IView view;
		private Texture texture;
		public Drawable(IView view) {
			this.view = view;
			this.texture = view.getTexture(textureStr);
		}
		
		@Override
		public void draw() {
			Vec2 p = body.getPosition();
			float angle = body.getAngle();
			if (p.y*100 < 10) Log.d("JAKE", "x: "+p.x*100+" y: "+p.y*100);
			view.drawTexturedRect(p.x * 100, p.y * 100, w/2 * 100, h/2 * 100, angle, 1f, 1f, 1f, texture);
			
		}
	}
}*/
