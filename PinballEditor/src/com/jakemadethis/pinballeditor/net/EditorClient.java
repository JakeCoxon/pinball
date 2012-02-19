package com.jakemadethis.pinballeditor.net;

import com.jakemadethis.net.Client;
import com.jakemadethis.net.IClientProtocol;
import com.jakemadethis.pinball.level.Wall;
import com.jakemadethis.pinballeditor.EditorModel;

public class EditorClient implements IClientProtocol {

	private EditorModel model;
	private String input;

	public EditorClient(EditorModel model) {
		this.model = model;
	}

	@Override
	public void processInput(Client client, String in) {
		this.input = in;
		if (eat("wall ")) {
			System.out.println("recieving wall");
			System.out.println(input);
			String[] strs = input.split(",");
			float[] floats = new float[strs.length-1];
			int id = Integer.valueOf(strs[0]);
			for (int i = 1; i < strs.length; i++) {
				floats[i-1] = Float.valueOf(strs[i]) * 100f;
			}
			Wall path = model.addWallPath(floats, 1f);
			path.setID(id);
			System.out.println(id);
		}
		//System.out.println("Recieved "+input);
	}
	
	public static void sendMoveWall(Client client, int entityId, int pathId, float x, float y) {
		client.sendMessage("movewall "+entityId+","+pathId+","+x+","+y);
	}
	public static void sendFinalWall(Client client, int entityId) {
		client.sendMessage("finalwall "+entityId);
	}
	public static void sendAddWallPoint(Client client, int entityId, int pathId, float x, float y) {
		client.sendMessage("addwallpoint "+entityId+","+pathId+","+x+","+y);
	}
	public static void sendAddWall(Client client, float x, float y) {
		client.sendMessage("addwall "+x+","+y);
	}
	
	private boolean eat(String str) {
		if (input.startsWith(str)) {
			input = input.substring(str.length());
			return true;
		}
		return false;
	}
	

}
