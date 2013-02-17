package com.jakemadethis.pinballeditor;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;

public class Program {
	public static void main(String[] args) {
		new Program();
	}
	
	public Program() {
		System.setProperty("com.apple.mrj.application.apple.menu.about.name", "Editor");
    
		PinballEditor editor = new PinballEditor();
		LwjglApplication app = 
			new LwjglApplication(editor, "Editor", 400, 500, true);
	}
}
