package com.jakemadethis.pinball.io;

import java.util.LinkedList;

public class Output {
	private LinkedList<Connection> connections;
	private String name;

	public Output(String name) {
		this.name = name;
		this.connections = new LinkedList<Connection>();
	}
	public void add(Connection connection) {
		connections.add(connection);
	}
	public void invoke() {
		for (Connection connection : connections) {
			connection.invoke();
		}
	}
}
