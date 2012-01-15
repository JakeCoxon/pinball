/*package com.jakemadethis.pinball.entities;

import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.BodyType;
import org.jbox2d.dynamics.FixtureDef;
import org.jbox2d.dynamics.World;

import com.jakemadethis.pinball.Entity;
import com.jakemadethis.pinball.IDrawable;
import com.jakemadethis.pinball.IView;

public class Rect extends Entity {
	
	
	

	protected Body body;
	protected float w;
	protected float h;

	public Rect(World world, float x, float y, boolean fixed) {
		this(world, x, y, 0.3f, 0.3f, fixed);
	}
	public Rect(World world, float x, float y, float w, float h, boolean fixed) {
		
		this.w = w;
		this.h = h;
		
		FixtureDef fd = new FixtureDef();
        PolygonShape sd = new PolygonShape();
        sd.setAsBox(w/2, h/2);
        fd.shape = sd;
        fd.density = 1.0f;
        fd.friction = 0.5f;
        

        BodyDef bd = new BodyDef();
        bd.allowSleep = false;
        bd.position = new Vec2(x, y);
        
        if (fixed)
        	bd.type = BodyType.STATIC;
        else
        	bd.type = BodyType.DYNAMIC;
        body = world.createBody(bd);
        body.createFixture(fd);
	}
	
	@Override
	public IDrawable createDrawable(IView view) {
		return new Drawable(view);
	}
	
	public class Drawable implements IDrawable {
		private IView view;
		public Drawable(IView view) {
			this.view = view;
		}

		@Override
		public void draw() {
			Vec2 p = body.getPosition();
			float angle = body.getAngle();
			view.drawRect(p.x * 100, p.y * 100, w/2 * 100, h/2 * 100, angle, 1f, 1f, 1f);
			
		}
	}
}
*/