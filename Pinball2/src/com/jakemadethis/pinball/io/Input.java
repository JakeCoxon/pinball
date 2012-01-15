package com.jakemadethis.pinball.io;

import java.util.LinkedList;

import com.jakemadethis.pinball.EventHandler;
import com.jakemadethis.pinball.EventHandler.EventListener;

public class Input {
	
	public static class EventArgs {
		private String inputName;

		public EventArgs(String inputName) {
			this.inputName = inputName;
		}
		public String getInputName() {
			return inputName;
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
	public void invoke(Connection connection) {
		handler.invoke(this, new EventArgs(connection.getInput().getName()));
	}
	
}
