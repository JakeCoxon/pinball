package com.jakemadethis.pinball;

public class Timer {
	private long length = 0;
	private long startTime = 0;
	public Timer() {
		
	}
	public void start(float seconds, boolean gameSpeed) {
		this.length = (long) (seconds * 1000000000L);
		if (gameSpeed) length /= GameController.gameSpeed;
		startTime = System.nanoTime();
	}
	public void start(float seconds) {
		start(seconds, false);
	}
	
	public float value() {
		return Math.min((float)(System.nanoTime() - startTime) / length, 1);
	}
	
	public float value(float min, float max) {
		return min + value() * (max - min);
	}
	
	public boolean running() {
		return System.nanoTime() <= startTime + length;
	}
}
