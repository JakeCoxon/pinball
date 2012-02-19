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
}
