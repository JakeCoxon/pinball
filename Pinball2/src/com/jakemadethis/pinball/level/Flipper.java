package com.jakemadethis.pinball.level;

import java.util.HashMap;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.physics.box2d.joints.RevoluteJoint;
import com.badlogic.gdx.physics.box2d.joints.RevoluteJointDef;
import com.jakemadethis.pinball.BaseModel;
import com.jakemadethis.pinball.Entity;
import com.jakemadethis.pinball.GameModel;
import com.jakemadethis.pinball.IElement;
import com.jakemadethis.pinball.builder.BuilderNode;
import com.jakemadethis.pinball.builder.FactoryUtil;

public class Flipper extends Entity implements IElement {

	/**
	 * Creates flipper from attributes: at,length,type[,name]
	 * @param model
	 * @param atts
	 * @return
	 */
	public static Flipper fromNode(BaseModel model, BuilderNode node) {

		HashMap<String, String> atts = node.getAttributes();
		
		
		float[] at = FactoryUtil.getAbsolutePosition(node.getParent().getValue(), atts);
		float length =        Float.valueOf( FactoryUtil.expected(atts, "length") );
		String stype = 						 FactoryUtil.expected(atts, "type");
		String name = 						 FactoryUtil.optional(atts, "name", "");
		
		Flipper.Type type = Flipper.Type.LEFT;
		if (stype.equals("right"))
			type = Flipper.Type.RIGHT;
	
		Flipper entity = model.addFlipper(at[0], at[1], length, type);
		model.setName(name, entity);
		
		return entity;
	}
	
	public enum Type {
		LEFT, RIGHT;
	}
	private final Body flipperBody;
	private final RevoluteJointDef jointDef;
	private final Body anchorBody;
	private final RevoluteJoint joint;
	private final float minangle;
	private final float maxangle;
	private final float upspeed = 20f;
	private final float downspeed = 20f;
	private final float flipperLength;
	private boolean engaged;
	private final Type type;

	public Flipper(World world, float cx, float cy, float length, Type type) {
		
		this.type = type;
    	anchorBody = Box2DFactory.createCircle(world, cx, cy, 0.005f, true);
    	anchorBody.setUserData(this);
    	
    	minangle = (float) (-Math.PI/8);
    	maxangle = (float) (Math.PI/8);
    	
    	if (type == Type.RIGHT) length = -length;
    	this.flipperLength = length;
    	
    	
		float ext = 0;//(length > 0) ? -0.05f : +0.05f;
		
		flipperBody = Box2DFactory.createWall(world, cx+ext, cy-0.012f, cx+length, cy+0.012f, 0f);
		flipperBody.setUserData(this);
  	flipperBody.setType(BodyType.DynamicBody);
  	flipperBody.setBullet(true);
  	for(Fixture f : flipperBody.getFixtureList())
  		f.setDensity(5.0f);
  	
  	jointDef = new RevoluteJointDef();
  	jointDef.initialize(anchorBody, flipperBody, new Vector2(cx, cy));
  	jointDef.enableLimit = true;
  	jointDef.enableMotor = true;
  	// counterclockwise rotations are positive, so flip angles for flippers extending left
  	jointDef.lowerAngle = (type == Type.LEFT) ? this.minangle : -this.maxangle;
  	jointDef.upperAngle = (type == Type.LEFT) ? this.maxangle : -this.minangle;
  	jointDef.maxMotorTorque = 50f;
  	
  	joint = (RevoluteJoint)world.createJoint(jointDef);
  	setEffectiveMotorSpeed(downspeed);
	}
	
	@Override
	public <A, R> R accept(EntityVisitor<R, A> visitor, A arg) {
		return visitor.visit(this, arg);
	}
	
	public float getLength() { return flipperLength; }
	public float getLowerAngle() { return jointDef.lowerAngle; }
	public float getUpperAngle() { return jointDef.upperAngle; }
	public RevoluteJoint getJoint() { return joint; }
	public Body getAnchorBody() { return anchorBody; }
	
	@Override
	public int getScore() {
		return 0;
	}
	
	@Override
	public void think(float timeStep, GameModel model) {
	}
	
	
	/** Returns the motor speed of the Box2D joint, normalized to be positive when the flipper is moving up. */
	float getEffectiveMotorSpeed() {
		float speed = joint.getJointSpeed();
		if (type == Type.RIGHT) speed = -speed;
		return speed;
	}
	
	/** Sets the motor speed of the Box2D joint, positive values move the flipper up. */
	void setEffectiveMotorSpeed(float speed) {
		if (type == Type.RIGHT) speed = -speed;
		joint.setMotorSpeed(speed);
	}

	@Override
	public void handleCollision(Ball ball, Body body, GameModel model) {

	}
	
	public boolean isFlipperEngaged() {
		return engaged;
	}
	
	public void setFlipperEngaged(boolean active) {
		// only adjust speed if state is changing, so we don't accelerate flipper that's been slowed down in tick()
		if (active != isFlipperEngaged()) {
			this.engaged = active;
			float speed = (active) ? -upspeed : downspeed;
			setEffectiveMotorSpeed(speed);
		}
	}
	
	public Type getType() {
		return type;
	}
	
	

}
/*abstract class FlipperLeft {
	public static Flipper fromNode(BaseModel model, HashMap<String, String> atts) {
		atts.put("type", "left");
		return Flipper.fromNode(model, atts);
	}
}
abstract class FlipperRight {
	public static Flipper fromNode(BaseModel model, HashMap<String, String> atts) {
		atts.put("type", "right");
		return Flipper.fromNode(model, atts);
	}
}*/