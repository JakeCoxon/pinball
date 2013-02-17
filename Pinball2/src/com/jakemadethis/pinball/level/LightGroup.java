package com.jakemadethis.pinball.level;

import java.util.ArrayList;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.jakemadethis.pinball.BaseModel;
import com.jakemadethis.pinball.Entity;
import com.jakemadethis.pinball.EventHandler.EventListener;
import com.jakemadethis.pinball.game.GameModel;
import com.jakemadethis.pinball.io.Signal;
import com.jakemadethis.pinball.io.SignalHandler;
import com.jakemadethis.pinball.io.Slot;
import com.jakemadethis.pinball.io.SlotHandler;

public class LightGroup extends Entity implements EventListener<Slot.EventArgs> {
	
	private static final float RADIUS = 0.1f;
	private final Light[] lights;
	private final Sensor[] sensors;
	private int total;
	private int current;
	private Color color;
	private final int score;
	private final BaseModel model;

	public LightGroup(BaseModel model, ArrayList<Vector2> vectors, int score) {
		this.model = model;
		this.score = score;
		this.color = Color.RED;
		this.lights = createLights(model, vectors, color);
		this.sensors = createSensors(model, vectors, color);
		this.total = lights.length;

		slots = new SlotHandler(this, "reset", "increment");
		signals = new SignalHandler("onComplete");
		
		for (int i = 0; i < total; i++) {
			sensors[i].addConnection("onSense", lights[i], "disable");
			lights[i].addConnection("onDisable", this, "increment");
		}
		current = 0;
		
		model.add(this);
	}

	private static Sensor[] createSensors(BaseModel model, ArrayList<Vector2> vectors, Color color) {
		Sensor[] sensors = new Sensor[vectors.size()];
		for (int i = 0; i < vectors.size(); i++) {
			sensors[i] = new Sensor(model, vectors.get(i).x, vectors.get(i).y, RADIUS);
		}
		return sensors;
	}

	private static Light[] createLights(BaseModel model, ArrayList<Vector2> vectors, Color color) {
		Light[] lights = new Light[vectors.size()];
		for (int i = 0; i < vectors.size(); i++) {
			lights[i] = new Light(model, vectors.get(i).x, vectors.get(i).y, RADIUS, color);
		}
		return lights;
	}

	@Override
	public void handleCollision(Ball ball, Body body, GameModel model) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void think(float timestep, GameModel model) {
		
	}
	
	@Override
	public <A, R> R accept(EntityVisitor<R, A> visitor, A arg) {
		return visitor.visit(this, arg);
	}

	@Override
	public void invoke(Object sender, Slot.EventArgs args) {
		if ("increment".equals(args.getSlotName())) {
			increment();
		}
	}

	private void increment() {
		current ++;
		if (current >= total) {
			complete();
			current = 0;
		}
	}

	private void complete() {
		model.addScore(score);
		for (Light light : lights) {
			light.enable();
			light.flash(2f);
		}
		invokeSignal("onComplete");
	}


	
}
