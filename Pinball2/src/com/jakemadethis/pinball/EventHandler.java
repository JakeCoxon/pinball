package com.jakemadethis.pinball;

import java.util.LinkedList;

/**
 * Handler to adding listeners
 * @author Jake
 *
 * @param <T> The class used to passing data
 */
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
	
	/**
	 * Invokes all event listeners
	 * @param sender The object that fired this invoke
	 * @param arg The data to pass to the listeners
	 */
	public void invoke(Object sender, T arg) {
		for (EventListener<T> listener : list) {
			listener.invoke(sender, arg);
		}
	}
	
	/**
	 * Adds an event listener
	 * @param listener
	 */
	public void add(EventListener<T> listener) {
		list.add(listener);
	}
}
