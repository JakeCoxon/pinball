package com.jakemadethis.pinball;

import static java.lang.Math.*;
public class Interpolator {
	public static float interp(float t, float min, float max) {
		return min*(1-t)+max*t;
	}
	
	public static float easeInOutCosine(Timer timer, float min, float max) {
		float t = (float) ((1-cos(timer.value()*PI))/2);
		return interp(t, min, max);
	}
	
	public static float easeOutQuad(Timer timer, float min, float max){
		float value = timer.value();
		max -= min;
		return -max * value * (value - 2) + min;
	}
}
