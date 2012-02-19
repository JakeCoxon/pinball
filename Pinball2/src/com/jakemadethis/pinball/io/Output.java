package com.jakemadethis.pinball.io;

import java.util.LinkedList;

/**
 * An output is an event fired from code that will be sent
 * through a connection to an input
 * 
 * Examples of outputs are "onCollide" or "onSensed"
 * @author Jake
 *
 */
public class Output {
	private LinkedList<Connection> connections;
	private String name;

	public Output(String name) {
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
