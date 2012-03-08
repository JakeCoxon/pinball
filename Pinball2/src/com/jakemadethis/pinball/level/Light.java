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
import com.jakemadethis.pinball.io.Input;
import com.jakemadethis.pinball.io.Input.EventArgs;
import com.jakemadethis.pinball.io.InputHandler;
import com.jakemadethis.pinball.io.OutputHandler;

public class Light extends Entity implements EventListener<Input.EventArgs> {

	public static Light fromNode(BaseModel model, BuilderNode node) {
		HashMap<String, String> atts = node.getAttributes();
		float[] pos = getAbsolutePosition(node.getParent().getValue(), atts);
		float[] size = toFloatList(expected(atts, "size"));
		float[] col = toFloatList(optional(atts, "color", "1,1,1,1"));
		String name = optional(atts, "name", "");
		
		Color color = new Color(col[0], col[1], col[2], col[3]);
		Light l = model.addLight(pos[0], pos[1], size[0], size[1], color);
		model.setName(name, l);
		return l;
	}
	
	private Color color;
	private float x;
	private float y;
	private float w;
	private float h;
	private boolean enabled;
	private Timer flashTimer = new Timer();;

	public Light(float x, float y, float w, float h, Color color) {
		this.x = x;
		this.y = y;
		this.w = w;
		this.h = h;
		this.color = color;
		this.enabled = true;
		
		inputs = new InputHandler(this, "toggle", "disable", "enable", "flash");
		outputs = new OutputHandler("onEnable", "onDisable");
	}
	
	public float getX() {
		return x;
	}
	public float getY() {
		return y;
	}
	public float getWidth() {
		return w;
	}
	public float getHeight() {
		return h;
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
		String inputName = args.getInputName();
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

	private void flash(float length) {
		flashTimer.start(length, true);
	}

	private void disable() {
		if (enabled && !flashTimer.running()) {
			enabled = false;
			outputs.invoke("onDisable");
		}
	}

	private void enable() {
		if (!enabled && !flashTimer.running()) {
			enabled = true;
			outputs.invoke("onEnable");
		}
	}

	private void toggle() {
		if (enabled) disable();
		else enable();
	}

}
