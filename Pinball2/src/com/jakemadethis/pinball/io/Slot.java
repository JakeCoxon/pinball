package com.jakemadethis.pinball.io;

import java.util.LinkedList;

/**
 * A slot is something that is invoked as a result 
 * of an signal firing an event
 * 
 * For example, "toggle" or "turnOff" are inputs
 * 
 * These events are passed to an input handler which 
 * calls the appropriate function
 * 
 * @author Jake
 *
 */
public class Slot {
	
	public static class EventArgs {
		private String slotName;
		private String[] args;

		public EventArgs(String slotName, String... args) {
			this.slotName = slotName;
			this.args = args;
		}
		public String getSlotName() {
			return slotName;
		}
		public String[] getArgs() {
			return args;
		}
	}
	private LinkedList<Connection> connections;
	private String name;
	private SlotHandler handler;

	public Slot(SlotHandler handler, String name) {
		this.name = name;
		this.handler = handler;
		this.connections = new LinkedList<Connection>();
	}
	public String getName() {
		return name;
	}
	public void add(Connection connection) {
		connections.add(connection);
	}
	public void invokeFromConnection(Connection connection) {
		handler.invokeFromSignal(this, new EventArgs(connection.getInput().getName(), connection.getArgs()));
	}
	
}
