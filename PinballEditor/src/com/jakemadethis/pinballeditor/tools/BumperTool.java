package com.jakemadethis.pinballeditor.tools;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import com.jakemadethis.pinball.Entity;
import com.jakemadethis.pinball.MathUtil;
import com.jakemadethis.pinball.entities.Bumper;
import com.jakemadethis.pinballeditor.EditorModel;
import com.jakemadethis.pinballeditor.EditorView;

public class BumperTool extends Tool {

	private Bumper selectedBumper;
	private Vector2 offset;
	private final Color color = new Color(0.5f, 0f, 0f, 0.2f);
	private final Color selColor = new Color(1f, 0f, 0f, 0.2f);
	private Bumper hoverBumper;

	public BumperTool(EditorView view, EditorModel model) {
		super(view, model);
	}
	
	private Bumper getBumperFromMouse() {
		Bumper bumper;
		for (Entity ent : model.entities) {
			if (ent instanceof Bumper && (bumper = (Bumper) ent) != null) {
				if (worldMouse.x > bumper.getX() - bumper.getRadius() &&
						worldMouse.x < bumper.getX() + bumper.getRadius() &&
						worldMouse.y > bumper.getY() - bumper.getRadius() &&
						worldMouse.y < bumper.getY() + bumper.getRadius()){
					return bumper;
				}
			}
		}
		return null;
	}

	@Override
	public boolean touchDown(int x, int y, int pointer, int button) {
		if ((selectedBumper = getBumperFromMouse()) != null) {
			offset = new Vector2(worldMouse.x - selectedBumper.getX(), worldMouse.y - selectedBumper.getY());
			return true;
		}
		return false;
	}
	
	@Override
	public boolean touchDragged(int x, int y, int pointer) {
		if (selectedBumper == null) return false;
		selectedBumper.setX(view.snap(worldMouse.x - offset.x));
		selectedBumper.setY(view.snap(worldMouse.y - offset.y));
		return true;
	}

	
	@Override
	public void think(float delta) {
		super.think(delta);
		
		hoverBumper = getBumperFromMouse();
		
		if (selectedBumper != null) {
			int d = (Gdx.input.isKeyPressed(Keys.W) ? 1 : 0) - (Gdx.input.isKeyPressed(Keys.Q) ? 1 : 0);
			float r = MathUtil.clamp(selectedBumper.getRadius() + d * 0.01f, 0.1f, 2);
			selectedBumper.setRadius(r);
		}
	}
	
	@Override
	public void draw() {

		view.world.setColor(color);

		Bumper bumper;
		for (Entity ent : model.entities) {
			if (ent instanceof Bumper && (bumper = (Bumper) ent) != null) {
				float cx = bumper.getX();
				float cy = bumper.getY();
				float radius = 0.2f; //bumper.getRadius() - 5;
				view.world.draw(view.getSprite("bumper"), cx - radius, cy - radius, radius*2, radius*2);
			}
		}

		view.world.setColor(selColor);
		if (hoverBumper != null) {
			
			float cx = hoverBumper.getX();
			float cy = hoverBumper.getY();
			float radius = 0.2f; //selectedBumper.getRadius();
			view.world.draw(view.getSprite("bumper"), cx - radius, cy - radius, radius*2, radius*2);
		}
		
		if (selectedBumper != null && selectedBumper != hoverBumper) {
			
			float cx = selectedBumper.getX();
			float cy = selectedBumper.getY();
			float radius = 0.2f; //selectedBumper.getRadius();
			view.world.draw(view.getSprite("bumper"), cx - radius, cy - radius, radius*2, radius*2);
		}
	}

}
