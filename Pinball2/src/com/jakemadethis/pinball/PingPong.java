package com.jakemadethis.pinball;

/**
 * Ping-pong timer, a timer which goes forwards then back
 * @author Jake
 *
 */
public class PingPong extends Timer {
	private boolean forward = false;
	
	@Override
	public void start(float lengthSeconds) {
	  forward(lengthSeconds);
	}
	
	private void oldStart(float seconds, boolean gameSpeed) {
	  this.length = (long) (seconds * 1000000000L);
    if (gameSpeed) length /= GAME_SPEED;
    startTime = System.nanoTime();
	}
	
	
	public void forward(float seconds) {
		forward = true;
		float v = super.value();
		oldStart(seconds, true);
		setValue(1-v);
	}
	
	public void backward(float seconds) {
		forward = false;
		float v = super.value();
		oldStart(seconds, true);
		setValue(1-v);
	}
	
	public boolean isForward() {
		return forward;
	}
	
	@Override
	public float value() {
		if (!started()) return 0;
		if (!forward) return 1 - super.value();
		return super.value();
	}
}
