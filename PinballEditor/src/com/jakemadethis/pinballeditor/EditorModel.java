package com.jakemadethis.pinballeditor;

import java.util.LinkedList;

import com.jakemadethis.pinball.BaseModel;
import com.jakemadethis.pinball.Entity;
import com.jakemadethis.pinball.entities.Flipper;
import com.jakemadethis.pinball.entities.Wall;
import com.jakemadethis.pinball.entities.WallPath;

public class EditorModel extends BaseModel {
	
	LinkedList<WallPath> walls = new LinkedList<WallPath>();
	
	public EditorModel() {
		super();
		

		width = 480f / 100f;
		height = 1000f / 100f;
		
	}
	
	@Override
	public void remove(Entity entity) {
		super.remove(entity);
		walls.remove(entity);
	}
	
	@Override
	public void initGame() {

		width = 480f;
		height = 1000f;

		/*//addBox(100f, 100f, 100f, 100f);
		addBall(width-15f, height-15f, 15f);
		
		setName("sensor", addSensor(width-15f, height-260f, 2f));
		
		addBumper(130f, 110f, 50f);
		addBumper(300f, 150f, 50f);
		addBumper(328f, 300f, 64f);
		
		float rest = 0.5f;

		float mid = (15f + width-30f)/2;
		addFlipper(mid - 120f, height-80f, 100f, Flipper.Type.LEFT);
		addFlipper(mid + 120f, height-80f, 100f, Flipper.Type.RIGHT);

		addWall(width-30f, height-200f, width-30f, height, rest);
		
		setName("toggleWall", addWall(width-30f, height-200f, width, height-230f, rest));
		
		addWall(0, 0, width, 0, rest);
		//addWall(15, height-15, width-15, height - 15, 0);

		addWall(0, 0, 0, height, rest);
		addWall(width, 0, width, height, rest);
		
		addWall(0, 300f, 90f, 370f, 1f);
		
		
		float r = 100;
		addWallArc(width-r, r, r, r, (float) (-Math.PI/2), 0, 8);
		addWallArc(r, r, r, r, (float) (-Math.PI), (float) (-Math.PI/2), 8);
		
		
		*/
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
	public synchronized WallPath addWallPath(float[] path, float restitution) {
		WallPath wall = super.addWallPath(path, restitution);
		walls.add(wall);
		return wall;
	}

}
