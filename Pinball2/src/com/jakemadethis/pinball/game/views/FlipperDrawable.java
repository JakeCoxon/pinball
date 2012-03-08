package com.jakemadethis.pinball.game.views;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.joints.RevoluteJoint;
import com.jakemadethis.pinball.Entity;
import com.jakemadethis.pinball.IDrawable;
import com.jakemadethis.pinball.MathUtil;
import com.jakemadethis.pinball.game.GameView;
import com.jakemadethis.pinball.level.Flipper;

public class FlipperDrawable implements IDrawable {
	private GameView view;
	private Color color = new Color( 1f, 1f, 1f, 1f );
	private Flipper flipper;
	private Body anchorBody;
	private RevoluteJoint joint;
	
	public FlipperDrawable(Flipper flipper, GameView view) {
		this.flipper = flipper;
		this.view = view;
		this.anchorBody = flipper.getAnchorBody();
		this.joint = flipper.getJoint();
	}
	
	@Override
	public Entity getEntity() {
		return flipper;
	}
	
	@Override
	public void draw() {
		Vector2 position = anchorBody.getPosition();
		float angle = joint.getJointAngle();
		// HACK: angle can briefly get out of range, always draw between min and max
		
		// clamp
		angle = MathUtil.clamp(angle, flipper.getLowerAngle(), flipper.getUpperAngle());
		
		float x1 = position.x + 0.05f * (float)Math.sin(angle);
		float y1 = position.y - 0.05f * (float)Math.cos(angle);
		float x2 = position.x + 0.05f * (float)Math.sin(angle) + flipper.getLength() * (float)Math.cos(angle);
		float y2 = position.y - 0.05f * (float)Math.cos(angle) + flipper.getLength() * (float)Math.sin(angle);

		view.world.setColor(color);
		view.drawLine(view.world, x1, y1, x2, y2, 0.02f);
		view.world.draw(view.getSprite("circle"), position.x-0.05f, position.y-0.05f, 0.1f, 0.1f);
		//view.world.drawLine(x1, y1, x2, y2, color);
		//view.world.drawTexture(position.x, position.y, 0.1f, 0.1f, view.getSprite("circle"), color);
		//view.getWorldDelegate().drawLine(DrawMode.NORMAL, x1, y1, x2, y2, color );
		//view.getWorldDelegate().drawCircle(DrawMode.NORMAL, position.x, position.y, 0.05f, 0.05f, color);

	}

}
