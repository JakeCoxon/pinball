package com.jakemadethis.pinballeditor;

import java.util.LinkedList;

import com.jakemadethis.pinball.BaseModel;
import com.jakemadethis.pinball.Entity;
import com.jakemadethis.pinball.io.OutputHandler;
import com.jakemadethis.pinball.level.Wall;

public class EditorModel extends BaseModel {
	
	public LinkedList<Wall> walls = new LinkedList<Wall>();
	private final OutputHandler outputs = new OutputHandler("onReset");
	
	
	public EditorModel() {
		super();


		
	}
	@Override
	public void clear() {
		super.clear();
		ioManager.add("level", null, outputs);
	}
	
	@Override
	public void remove(Entity entity) {
		super.remove(entity);
		walls.remove(entity);
	}
	
	@Override
	public void initGame() {

		scale = 1f;
		width /= 100f;
		height /= 100f;
	}
	
	@Override
	public synchronized Wall addWall(float x0, float y0, float x1, float y1,
			float restitution) {
		Wall wall = super.addWall(x0, y0, x1, y1, restitution);
		walls.add(wall);
		return wall;
	}
	
	@Override
	public synchronized Wall addWallPath(float[] path, float restitution) {
		Wall wall = super.addWallPath(path, restitution);
		walls.add(wall);
		return wall;
	}

}
