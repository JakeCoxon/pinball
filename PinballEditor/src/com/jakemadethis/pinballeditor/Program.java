package com.jakemadethis.pinballeditor;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;

public class Program {
	public static void main(String[] args) {
		new Program();
	}
	
	public Program() {
		LwjglApplication app = 
			new LwjglApplication(new PinballEditor(), "Editor", 400, 500, true);
		
	}
}
