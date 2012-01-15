package com.jakemadethis.pinballeditor;

import com.jakemadethis.net.IServerProtocol;
import com.jakemadethis.net.Server;

public class EditorServer implements Runnable {
	
	private Server server;
	private IServerProtocol protocol;
	
	public EditorServer(IServerProtocol protocol) {
		this.protocol = protocol;
		new Thread(this).start();
	}
	

	@Override
	public void run() {
		System.out.println("Waiting for connection");
		server = new Server(4444, protocol);
		server.start();
		System.out.println("Connected");
	}
	
	public void sendMessage(String string) {
		server.sendMessage(string);
	}
}
