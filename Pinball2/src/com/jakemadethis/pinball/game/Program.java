package com.jakemadethis.pinball.game;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;

public class Program {
	public static void main(String[] args) {
		LwjglApplication app = 
			new LwjglApplication(new Pinball(), "Pinball", 300, 400, true);
	}
}
