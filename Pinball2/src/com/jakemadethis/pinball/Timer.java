package com.jakemadethis.pinball;


/**
 * A timer class that interpolates between two values over time
 * @author Jake
 *
 */
public class Timer {
	
	public final static float GAME_SPEED = 1;//0.1f;
	
	protected long length = 0;
	protected long startTime = 0;

	private float a;

	public Timer() {
		
	}
	
	/**
	 * Starts the timer
	 * @param lengthSeconds Length in seconds the timer should run for
	 * @param gameSpeed Whether the timer should take in to account the game speed
	 */
	public void start(float lengthSeconds, boolean gameSpeed) {
		this.length = (long) (lengthSeconds * 1000000000L);
		if (gameSpeed) length /= GAME_SPEED;
		startTime = System.nanoTime();
	}
	
	/**
	 * Starts the timer and taking in to account the game speed
	 * @param lengthSeconds Length in seconds the timer should run for
	 */
	public void start(float lengthSeconds) {
		start(lengthSeconds, true);
	}
	
	/**
	 * @return the value of the timer between 0 and 1
	 */
	public float value() {
		return a = Math.min((float)(System.nanoTime() - startTime) / length, 1);
	}
	
	/**
	 * Overrides the value by shifting the start time
	 * @param value
	 */
	public void setValue(float value) {
		startTime = System.nanoTime() - (long) (value * length);
	}
	
	/**
	 * @return The number of seconds since the timer started
	 */
	public float getTimeElapsed() {
		return (float)(System.nanoTime() - startTime) / 1000000000L;
	}
	
	/**
	 * @param min
	 * @param max
	 * @return the interpolated value between min and max of the timer
	 */
	public float value(float min, float max) {
		return min + value() * (max - min);
	}
	
	/**
	 * @return true if the timer has started and has not finished
	 */
	public boolean running() {
		return System.nanoTime() <= startTime + length;
	}
	
	/**
	 * @return true if the timer has run and finished
	 */
	public boolean finished() {
		return !running() && started();
	}
	
	/**
	 * @return the length of the timer
	 */
	public long getLength() {
		return length;
	}

	/**
	 * @return true if the timer has been started
	 */
	public boolean started() {
		return length > 0;
	}
	
	/**
	 * Resets the timer so started() returns true
	 */
	public void reset() {
		length = 0;
	}
}
