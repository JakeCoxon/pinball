package com.jakemadethis.pinball.game;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.BitmapFontLoader;
import com.badlogic.gdx.assets.loaders.BitmapFontLoader.BitmapFontParameter;
import com.badlogic.gdx.assets.loaders.TextureLoader;
import com.badlogic.gdx.assets.loaders.TextureLoader.TextureParameter;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.jakemadethis.pinball.GeneratorManager;
import com.jakemadethis.pinball.PixelGenerator;

public class PinballAssets {
	public static Texture big;
	public static Texture sprites;
	public static Texture ui;
	public static Texture background;
	public static Texture pixel;
	public static BitmapFont regularfont;
	public static BitmapFont scorefont;

	private static AssetManager assets = null;
	private static GeneratorManager generators = null;
	private static boolean loaded = false;

	public static void loadAssets() {
		if (assets == null) assets = new AssetManager();
		
		System.out.println("SETUP LOADASSETS");
		assets.load("data/sprites.png", Texture.class);
		assets.load("data/ui.png", Texture.class);
		BitmapFontParameter a = new BitmapFontLoader.BitmapFontParameter();
		a.flip = true;
		assets.load("data/regular.fnt", BitmapFont.class, a);
		assets.load("data/scorefont.fnt", BitmapFont.class, a);
		
		if (generators == null) generators = new GeneratorManager();
		generators.load("background", new BackgroundRenderer(), Texture.class);
		generators.load("pixel", new PixelGenerator(), Texture.class);
	}
	
	public static boolean update() {
		if (assets == null || assets.getLoadedAssets() == 0) 
			loadAssets();
		if (assets.update() && generators.update()) {
			if (!loaded) {
				onComplete(); loaded = true;
			}
			return true;
		}
		return false;
	}
	
	public static void dispose() {
		loaded = false;
		assets.clear();
		//assets.dispose();
		//generators.dispose();
	}
	
	public static float getProgress() {
		return (assets.getProgress() + generators.getProgress()) / 2f;
	}
	
	public static void onComplete() {
		
		pixel = generators.get("pixel", Texture.class);	
		background = generators.get("background", Texture.class);
		
		sprites = assets.get("data/sprites.png", Texture.class);
		sprites.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		
		ui = assets.get("data/ui.png", Texture.class);
		ui.setFilter(TextureFilter.Linear, TextureFilter.Linear);

		regularfont = assets.get("data/regular.fnt", BitmapFont.class);
		
		scorefont = assets.get("data/scorefont.fnt", BitmapFont.class);	
		scorefont.setFixedWidthGlyphs("0123456789");
	}

	
	
}
