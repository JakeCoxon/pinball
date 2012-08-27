package com.jakemadethis.pinball;

public class MathUtil {
	/**
	 * @param value The value to clamp
	 * @param lower The lower bound
	 * @param upper The upper bound
	 * @return A value clamped between an upper and lower bound
	 */
	public static float clamp(float value, float lower, float upper) {
		return Math.min(Math.max(value, lower), upper);
	}
	
	public static float interp(float t, float min, float max) {
		return min*(1-t)+max*t;
	}
	
	public static float timeSine(float scale, float min, float max) {
		double s = Math.sin(System.currentTimeMillis() / 1000d * (2 * Math.PI) * scale);
		s = (s*0.5d)+0.5d;
		return interp((float)s, min, max);
	}
}
