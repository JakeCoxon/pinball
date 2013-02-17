package com.jakemadethis.pinball.io;

import java.util.LinkedList;

/**
 * A signal is an event fired from code that will be sent
 * through a connection to a slot
 * 
 * Examples of outputs are "onCollide" or "onSensed"
 * @author Jake
 *
 */
public class Signal {
	private LinkedList<Connection> connections;
	private String name;

	public Signal(String name) {
		this.name = name;
		this.connections = new LinkedList<Connection>();
	}
	
	/**
	 * Adds a connection
	 * @param connection
	 */
	public void add(Connection connection) {
		connections.add(connection);
	}
	
	/**
	 * Invokes all connections
	 */
	public void invoke() {
		for (Connection connection : connections) {
			connection.invoke();
		}
	}
}
