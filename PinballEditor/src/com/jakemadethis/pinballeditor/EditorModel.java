package com.jakemadethis.pinballeditor;

import java.util.LinkedList;

import com.jakemadethis.pinball.BaseModel;
import com.jakemadethis.pinball.Entity;
import com.jakemadethis.pinball.io.SignalHandler;
import com.jakemadethis.pinball.level.Wall;

public class EditorModel extends BaseModel {
	
	public LinkedList<Wall> walls = new LinkedList<Wall>();
	private final SignalHandler outputs = new SignalHandler("onReset");
	
	
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

	}
	
	public <T extends Entity> T add(T entity) {
		entity = super.add(entity);
		if (entity instanceof Wall) {
			Wall wall = (Wall) entity;
			walls.add(wall);
		}
		return entity;
	}
	

}
