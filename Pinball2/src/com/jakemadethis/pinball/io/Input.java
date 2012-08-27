package com.jakemadethis.pinball.io;

import java.util.LinkedList;

/**
 * An input is something that is invoked as a result 
 * of an output firing an event
 * 
 * For example, "toggle" or "turnOff" are inputs
 * 
 * These events are passed to an input handler which 
 * calls the appropriate function
 * 
 * @author Jake
 *
 */
public class Input {
	
	public static class EventArgs {
		private String inputName;
		private String[] args;

		public EventArgs(String inputName, String... args) {
			this.inputName = inputName;
			this.args = args;
		}
		public String getInputName() {
			return inputName;
		}
		public String[] getArgs() {
			return args;
		}
	}
	private LinkedList<Connection> connections;
	private String name;
	private InputHandler handler;

	public Input(InputHandler handler, String name) {
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
		handler.invokeFromInput(this, new EventArgs(connection.getInput().getName(), connection.getArgs()));
	}
	
}
