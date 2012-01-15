package com.jakemadethis.pinball.net;

import java.util.ArrayList;

import com.badlogic.gdx.math.Vector2;
import com.jakemadethis.net.Client;
import com.jakemadethis.net.IClientProtocol;
import com.jakemadethis.net.IServerProtocol;
import com.jakemadethis.net.Server;
import com.jakemadethis.pinball.Entity;
import com.jakemadethis.pinball.GameModel;
import com.jakemadethis.pinball.entities.Sensor;
import com.jakemadethis.pinball.entities.Wall;
import com.jakemadethis.pinball.entities.WallArc;
import com.jakemadethis.pinball.entities.WallPath;

public class Protocol implements IServerProtocol {

	private GameModel model;
	private String input;
	public Protocol(GameModel model) {
		this.model = model;
	}
	@Override
	public void processInput(Server server, String in) {
		this.input = in;
		if (eat("movewall ")) {
			
			String[] strs = input.split(",");
			int entId = Integer.valueOf(strs[0]);
			Entity entity = model.entities.get(entId);
			int pointId = Integer.valueOf(strs[1]);
			float x = Float.valueOf(strs[2]);
			float y = Float.valueOf(strs[3]);
			
			if (entity instanceof WallPath) {
				WallPath path = (WallPath) entity;
				path.movePoint(pointId, x, y);
			}
			
		}
		
		else if (eat("finalwall ")) {
			
			String[] strs = input.split(",");
			int entId = Integer.valueOf(strs[0]);
			Entity entity = model.entities.get(entId);
			
			if (entity instanceof WallPath) {
				WallPath path = (WallPath) entity;
				path.reconstruct();
			}
			
		}
		
		else if (eat("addwallpoint ")) {
			
			String[] strs = input.split(",");
			int entId = Integer.valueOf(strs[0]);
			Entity entity = model.entities.get(entId);
			int pointId = Integer.valueOf(strs[1]);
			float x = Float.valueOf(strs[2]);
			float y = Float.valueOf(strs[3]);
			
			if (entity instanceof WallPath) {
				WallPath path = (WallPath) entity;
				path.addPoint(pointId, new Vector2(x, y));
			}
			
		}
		
		else if (eat("addwall")) {
			String[] strs = input.split(",");
			float x = Float.valueOf(strs[2]);
			float y = Float.valueOf(strs[3]);
			
			WallPath p = model.addWall(x, y, x+1, y+1, 1f);
			sendWallPath(server, model.entities.size()-1, p);
			
		}
		/**
		 * public static void sendFinalWall(Client client, int entityId) {
		client.sendMessage("finalwall "+entityId);
	}
	public static void sendAddWallPoint(Client client, int entityId, int pathId, float x, float y) {
		client.sendMessage("addwallpoint "+entityId+","+pathId+","+x+","+y);
	}
	public static void sendAddWall(Client client, int entityId, float x, float y) {
		client.sendMessage("addwall "+entityId+","+x+","+x+","+y);
	}
		 */
	}
	
	private boolean eat(String str) {
		if (input.startsWith(str)) {
			input = input.substring(str.length());
			return true;
		}
		return false;
	}
	
	
	@Override
	public void onConnect(Server server) {
		server.sendMessage("Server greets you");
		
		int id = 0;
		for (Entity entity : model.entities) {
			if (entity instanceof WallPath && !(entity instanceof WallArc)) {
				WallPath path = (WallPath) entity;
				sendWallPath(server, id, path);
			}
			id++;
		}
	}
	
	private static void sendWallPath(Server server, int id, WallPath path) {
		ArrayList<Vector2> points = path.getPoints();
		StringBuilder sb = new StringBuilder("wall ");
		
		sb.append(id).append(',');
		for (int i = 0; i < points.size(); i++) {
			sb.append(points.get(i).x)
				.append(',')
				.append(points.get(i).y);
			if (i < points.size()-1) {
				sb.append(',');
			}
		}
		System.out.println("Send "+sb.toString());
		server.sendMessage(sb.toString());
	}
	
}
