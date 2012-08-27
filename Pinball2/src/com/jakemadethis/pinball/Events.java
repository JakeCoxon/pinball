package com.jakemadethis.pinball;

import java.util.ArrayList;
import java.util.HashMap;

public class Events {
	private HashMap<Class<?>, ArrayList<EventListener<?>>> map;
	
	public <T> void add(Class<T> cls, EventListener<T> listener) {
		if (map.get(cls) == null) map.put(cls, new ArrayList<EventListener<?>>());
		map.get(cls).add(listener);
	}
	
	@SuppressWarnings("unchecked")
	public <T> void invoke(Class<T> cls, T args) {
		if (map.get(cls) == null) return;
		for (EventListener<?> l : map.get(cls)) {
			((EventListener<T>)l).invoke(args);
		}
	}
}

interface EventListener<T> {
	public void invoke(T args);
}