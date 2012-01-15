package com.jakemadethis.net;

public interface IServerProtocol {
	void processInput(Server server, String input);
	void onConnect(Server server);
}
