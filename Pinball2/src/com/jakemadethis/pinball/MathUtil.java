package com.jakemadethis.pinball;

public class MathUtil {
	public static float clamp(float value, float lower, float upper) {
		return Math.min(Math.max(value, lower), upper);
	}
}
