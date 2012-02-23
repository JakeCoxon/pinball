package com.jakemadethis.pinball;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;

public class TextureManager {
	private static TextureManager man = null;
	public static TextureManager get() {
		if (man == null) man = new TextureManager();
		return man;
	}
	
	public Texture regularfont, scorefont, sprites;
	
	public TextureManager() {
		sprites = new Texture(Gdx.files.internal("data/sprites.png"));
		sprites.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		
		scorefont = new Texture(Gdx.files.internal("data/scorefont.png"));
		scorefont.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		
		regularfont = new Texture(Gdx.files.internal("data/regularfont.png"));
		regularfont.setFilter(TextureFilter.Linear, TextureFilter.Linear);
	}
}
