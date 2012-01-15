package com.jakemadethis.pinball;

import java.util.LinkedList;

public class EventHandler<T> {
	public static abstract interface EventListener<T> {
		public void invoke(Object sender, T args);
	}
	public static abstract class ActionListener implements EventListener<Object> {
	}
	
	private LinkedList<EventListener<T>> list;
	
	public EventHandler() {
		list = new LinkedList<EventHandler.EventListener<T>>();
	}
	public void invoke(Object sender, T arg) {
		for (EventListener<T> event : list) {
			event.invoke(sender, arg);
		}
	}
	
	public void add(EventListener<T> event) {
		list.add(event);
	}
}
