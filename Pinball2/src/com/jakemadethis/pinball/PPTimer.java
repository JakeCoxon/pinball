package com.jakemadethis.pinball;

/**
 * Ping-pong timer, a timer which goes forwards then back
 * @author Jake
 *
 */
public class PPTimer extends Timer {
	private boolean forward = false;
	
	public void forward(float seconds) {
		forward = true;
		float v = super.value();
		start(seconds);
		setValue(1-v);
	}
	public void backward(float seconds) {
		forward = false;
		float v = super.value();
		start(seconds);
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
