package com.jakemadethis.pinball;

/**
 * A timer class that interpolates between two values over time
 * @author Jake
 *
 */
public class Timer {
	private long length = 0;
	private long startTime = 0;
	public Timer() {
		
	}
	
	/**
	 * Starts the timer
	 * @param lengthSeconds Length in seconds the timer should run for
	 * @param gameSpeed Whether the timer should take in to account the game speed
	 */
	public void start(float lengthSeconds, boolean gameSpeed) {
		this.length = (long) (lengthSeconds * 1000000000L);
		if (gameSpeed) length /= GameController.gameSpeed;
		startTime = System.nanoTime();
	}
	
	/**
	 * Starts the timer without taking in to account the game speed
	 * @param lengthSeconds Length in seconds the timer should run for
	 */
	public void start(float lengthSeconds) {
		start(lengthSeconds, false);
	}
	
	/**
	 * @return the value of the timer between 0 and 1
	 */
	public float value() {
		return Math.min((float)(System.nanoTime() - startTime) / length, 1);
	}
	
	/**
	 * @param min
	 * @param max
	 * @return  the interpolated value between min and max of the timer
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
}
