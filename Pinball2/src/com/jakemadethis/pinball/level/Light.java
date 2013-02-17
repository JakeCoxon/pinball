package com.jakemadethis.pinball.level;

import static com.jakemadethis.pinball.builder.FactoryUtil.*;
import java.util.HashMap;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.physics.box2d.Body;
import com.jakemadethis.pinball.BaseModel;
import com.jakemadethis.pinball.Entity;
import com.jakemadethis.pinball.EventHandler.EventListener;
import com.jakemadethis.pinball.Timer;
import com.jakemadethis.pinball.builder.BuilderNode;
import com.jakemadethis.pinball.builder.FactoryUtil;
import com.jakemadethis.pinball.game.GameModel;
import com.jakemadethis.pinball.io.EventUtil;
import com.jakemadethis.pinball.io.Slot;
import com.jakemadethis.pinball.io.Slot.EventArgs;
import com.jakemadethis.pinball.io.SlotHandler;
import com.jakemadethis.pinball.io.SignalHandler;

public class Light extends Entity implements EventListener<Slot.EventArgs> {
	
	private Color color;
	private float x;
	private float y;
	private boolean enabled;
	private Timer flashTimer = new Timer();
	private float radius;;

	public Light(BaseModel model, float x, float y, float radius, Color color) {
		this.x = x;
		this.y = y;
		this.radius = radius;
		this.color = color;
		this.enabled = true;
		
		slots = new SlotHandler(this, "toggle", "disable", "enable", "flash");
		signals = new SignalHandler("onEnable", "onDisable");
		
		model.add(this);
	}
	
	public float getX() {
		return x;
	}
	public float getY() {
		return y;
	}
	public float getRadius() {
		return radius;
	}
	public boolean isEnabled() {
		return enabled;
	}
	public Color getColor() {
		return color;
	}
	
	@Override
	public void handleCollision(Ball ball, Body body, GameModel model) {
		
	}

	@Override
	public void think(float timestep, GameModel model) {
		
	}

	@Override
	public <A, R> R accept(EntityVisitor<R, A> visitor, A arg) {
		return visitor.visit(this, arg);
	}

	@Override
	public void invoke(Object sender, EventArgs args) {
		String inputName = args.getSlotName();
		if (inputName.equals("toggle")) {
			toggle();
		}
		else if (inputName.equals("enable")) {
			enable();
		}
		else if (inputName.equals("disable")) {
			disable();
		}
		else if (inputName.equals("flash")) {
			float length = EventUtil.optionalFloat(args.getArgs(), 0, 2f);
			flash(length);
		}
	}
	
	/**
	 * Gets whether this light is flashing
	 * @return
	 */
	public boolean isFlashing() {
		return flashTimer.running();
	}
	
	public Timer getFlashTimer() {
		return flashTimer;
	}

	public void flash(float length) {
		flashTimer.start(length, true);
	}

	public void disable() {
		if (enabled && !flashTimer.running()) {
			enabled = false;
			signals.invoke("onDisable");
		}
	}

	public void enable() {
		if (!enabled && !flashTimer.running()) {
			enabled = true;
			signals.invoke("onEnable");
		}
	}

	public void toggle() {
		if (enabled) disable();
		else enable();
	}

}
