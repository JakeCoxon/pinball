package com.jakemadethis.net;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.Reader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.LinkedList;

public class Server implements Runnable {
	private Thread thread;
	private ServerSocket serverSocket;
	private PrintWriter out;
	private Socket clientSocket;
	private LinkedList<String> buffer;
	private int port;
	private boolean connected = false;
	private BufferedReader in;
	private IServerProtocol protocol;
	
	public Server(int port, IServerProtocol protocol) {
		this.port = port;
		this.protocol = protocol;
		thread = new Thread(this);
	}
	
	public synchronized void start() {
		thread.start();
		while (!connected) {
			try { wait(); }
			catch (InterruptedException e) { }
		}
		
		protocol.onConnect(this);
		System.out.println("Thread started");
	}
	
	@Override
	public void run() {
		try {
			setup();
		} catch (IOException ex) {
			
		}

		connected = true;
		System.out.println("NotifyAll");
		synchronized (this) {
			notifyAll();
		}
		
		waitForInput();
		
	}
	private void waitForInput() {

		try {

			System.out.println("Waiting for input");
			String inputLine;
			while ((inputLine = in.readLine()) != null) {
				protocol.processInput(this, inputLine);
	        }
			System.out.println("Finished waiting");
			
			in.close();
			out.close();
			serverSocket.close();
			clientSocket.close();
			connected = false;
			
		} catch (IOException e) {
			//e.printStackTrace();
		}
		System.out.println("Server disconnected");
	}
	
	public synchronized void sendMessage(String message) {
		out.println(message);
	}
	public synchronized void quit() {
		connected = false;
		notifyAll();
	}

	private void setup() throws IOException {
		try {
		    serverSocket = new ServerSocket(port);
		} catch (IOException e) {
		    System.out.println("Could not listen on port: "+port);
		    return;
		}
		
		System.out.println("Listening on port "+port);
		
		try {
		    clientSocket = serverSocket.accept();
		} catch (IOException e) {
		    System.out.println("Accept failed: 4444");
		    return;
		}
		System.out.println("Accepted");
		
		in = new BufferedReader(
				new InputStreamReader(
				clientSocket.getInputStream()));
		out = new PrintWriter(
                clientSocket.getOutputStream(), true);
		
		buffer = new LinkedList<String>();
		//in = new BufferedReader(
		//                  new InputStreamReader(
		//                      clientSocket.getInputStream()));
		connected = true;
	}

	public boolean isConnected() {
		return connected;
	}
}
