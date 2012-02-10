package com.jakemadethis.pinball.entities;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.physics.box2d.joints.RevoluteJoint;
import com.badlogic.gdx.physics.box2d.joints.RevoluteJointDef;

import com.jakemadethis.pinball.Entity;
import com.jakemadethis.pinball.GameView;
import com.jakemadethis.pinball.IDrawable;
import com.jakemadethis.pinball.IView;
import com.jakemadethis.pinball.GameModel;

public class Flipper extends Entity implements IElement {

	public enum Type {
		LEFT, RIGHT;
	}
	private Body flipperBody;
	private RevoluteJointDef jointDef;
	private Body anchorBody;
	private RevoluteJoint joint;
	private float minangle;
	private float maxangle;
	private float upspeed = 40f;
	private float downspeed = 20f;
	private float flipperLength;
	private boolean engaged;
	private Type type;

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
    	jointDef.maxMotorTorque = 100f;
    	
    	joint = (RevoluteJoint)world.createJoint(jointDef);
    	setEffectiveMotorSpeed(downspeed);
	}
	
	@Override
	public <A> IDrawable accept(EntityVisitor<IDrawable, A> visitor, A arg) {
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
		// TODO Auto-generated method stub
		
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
