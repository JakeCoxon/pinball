package com.jakemadethis.pinball.game;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;

public class TextureManager {
	private static TextureManager man = null;
	public static TextureManager get() {
		if (man == null) man = new TextureManager();
		return man;
	}
	
	public Texture regularfont, scorefont, sprites;
	private final AssetManager ass;
	private final static String[] files = {
		"data/sprites.png", "data/scorefont.png",
		"data/regularfont.png"
	};
	
	public TextureManager() {
		ass = new AssetManager();
		Texture.setAssetManager(ass);

		for (String file : files) {
			ass.load(file, Texture.class);
		}
	}
	
	public void generate() {
		sprites = ass.get("data/sprites.png", Texture.class);
		sprites.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		
		scorefont = ass.get("data/scorefont.png", Texture.class);
		scorefont.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		
		regularfont = ass.get("data/regularfont.png", Texture.class);
		regularfont.setFilter(TextureFilter.Linear, TextureFilter.Linear);
	}
	
	public AssetManager getAssMan() {
		return ass;
	}
	public boolean isLoaded() {
		for (String file : files) {
			if (!ass.isLoaded(file)) 
				return false;
		}
		return true;
	}
}
