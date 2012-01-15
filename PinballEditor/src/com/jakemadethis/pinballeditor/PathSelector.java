package com.jakemadethis.pinballeditor;

import com.badlogic.gdx.math.Vector2;
import com.jakemadethis.pinball.entities.WallPath;

public class PathSelector {
	private WallPath path;
	private int id;

	public PathSelector(WallPath path, int id) {
		this.path = path;
		this.id = id;
	}
	
	public WallPath getPath() { return path; }
	public int getId() { return id; }
	public Vector2 getPosition() { return path.getPoint(id); }
	
	public boolean equals(WallPath path, Vector2 vec) {
		return this.path == path && this.path.getPoint(id).equals(vec);
	}
	public void set(WallPath path, int id) {
		this.path = path;
		this.id = id;
	}
	public void set(PathSelector sel) {
		this.path = sel.getPath();
		this.id = sel.getId();
	}
	public PathSelector clone() {
		return new PathSelector(path, id);
	}
	public boolean isNull() {
		return this.path == null;
	}
	public void clear() {
		this.path = null;
		this.id = -1;
	}

	public boolean valid() {
		return path != null;
	}
}
