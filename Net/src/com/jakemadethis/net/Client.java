package com.jakemadethis.net;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

public class Client implements Runnable {

	private Thread thread;
	private IClientProtocol protocol;
	private Socket socket;
	private BufferedReader in;
	private String host;
	private int port;
	private PrintWriter out;
	public Client(String host, int port, IClientProtocol protocol) {
		this.protocol = protocol;
		this.host = host;
		this.port = port;
		thread = new Thread(this);
		thread.start();
	}
	
	@Override
	public void run() {
		try {
			socket = new Socket(host, port);

            out = new PrintWriter(socket.getOutputStream(), true);
			in = new BufferedReader(new InputStreamReader(
		                            socket.getInputStream()));
			
			
		} catch (UnknownHostException e) {
			//e.printStackTrace();
			return;
		} catch (IOException e) {
			//e.printStackTrace();
			return;
		}

		System.out.println("Connected");
		BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in));
        String fromServer;

        try {
			while ((fromServer = in.readLine()) != null) {
			    protocol.processInput(this, fromServer);
			}
		} catch (IOException e) {
			//e.printStackTrace();
		}

        try {
        	out.close();
			in.close();
	        stdIn.close();
	        socket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public synchronized void sendMessage(String string) {
		if (out == null) return;
		out.println(string);
	}
}
