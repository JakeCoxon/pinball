package com.jakemadethis.pinball;

/**
 * A class which entities can be attached to upon creation
 * @author Jake
 *
 */
public abstract class Attachable {

	public final static int LEFT = 1;
	public final static int RIGHT = 2;
	public final static int TOP = 4;
	public final static int BOTTOM = 8;
	public final static int CENTERX = 16;
	public final static int CENTERY = 32;
	
	/**
	 * Gets a corner from string
	 * @param string [top|bottom|center][left|right|center] 
	 * @return
	 */
	public static int getCornerFromString(String string) {
		if (string == null || string.length() == 0) return Attachable.LEFT | Attachable.TOP;
		int x = Attachable.LEFT;
		int y = Attachable.TOP;
		
		if (string.startsWith("top")) {
			string = string.substring("top".length());
			y = Attachable.TOP;
		} else if (string.startsWith("bottom")) {
			string = string.substring("bottom".length());
			y = Attachable.BOTTOM;
		} else if (string.startsWith("center")) {
			string = string.substring("center".length());
			y = Attachable.CENTERY;
		} else {
			throw new IllegalArgumentException("Invalid relative point");
		}
		
		if (string.startsWith("left")) {
			string = string.substring("left".length());
			x = Attachable.LEFT;
		} else if (string.startsWith("right")) {
			string = string.substring("right".length());
			x = Attachable.RIGHT;
		} else if (string.startsWith("center")) {
			string = string.substring("center".length());
			x = Attachable.CENTERX;
		} else {
			throw new IllegalArgumentException("Invalid relative point");
		}
		
		if (string.length() != 0)
			throw new IllegalArgumentException("Invalid relative point");
		
		return x | y;
	}
	
	float x, y, width, height;
	
	public Attachable(float x, float y, float width, float height) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
	}
	
	public Attachable(Attachable other) {
		this.x = other.getLeft();
		this.y = other.getTop();
		this.width = other.getWidth();
		this.height = other.getHeight();
	}

	/**
	 * Get absolute point from a relative position and a corner
	 * @param x the relative x position
	 * @param y the relative y position
	 * @param relativeTo the corner enum see {@link Attachable}
	 * @return A float array with the absolute x, y coordinates
	 */
	public float[] getAbsolutePoint(float x, float y, int relativeTo) {
		if ((relativeTo & LEFT) == LEFT)            x += getLeft();
		else if ((relativeTo & CENTERX) == CENTERX) x += getCenterX();
		else if ((relativeTo & RIGHT) == RIGHT)     x += getRight();
		
		if ((relativeTo & TOP) == TOP)              y += getTop();
		else if ((relativeTo & CENTERY) == CENTERY) y += getCenterY();
		else if ((relativeTo & BOTTOM) == BOTTOM)   y += getBottom();
		
		return new float[] {x, y};
	}
	
	public float getLeft() {
		return x;
	}
	public float getCenterX() {
		return x + width/2;
	}
	public float getCenterY() {
		return y + height/2;
	}
	public float getTop() {
		return y;
	}
	public float getBottom() {
		return y + height;
	}
	public float getRight() {
		return x + width;
	}
	public float getWidth() {
		return width;
	}
	public float getHeight() {
		return height;
	}
}
