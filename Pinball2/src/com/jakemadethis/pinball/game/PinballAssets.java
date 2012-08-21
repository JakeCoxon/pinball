package com.jakemadethis.pinball.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.utils.Logger;
import com.jakemadethis.pinball.AssetLoader;
import com.jakemadethis.pinball.BitmapFontLoader;
import com.jakemadethis.pinball.PixelGenerator;
import com.jakemadethis.pinball.TextureLoader;

public class PinballAssets extends AssetLoader {
	public static Texture big;
	public static Texture sprites;
	public static Texture ui;
	public static Texture background;
	public static Texture pixel;
	public static BitmapFont regularfont;
	public static BitmapFont scorefont;
	
	private static PinballAssets inst;
	
	public static PinballAssets get() {
		if (inst == null) inst = new PinballAssets();
		return inst;
	}

	
	public PinballAssets() {
		log.setLevel(Logger.DEBUG);
		log.debug("JasdsadsdasdsadAKE");
	}
	
	@Override
	public void loadAssets() {
		int width = Gdx.graphics.getWidth(), height = Gdx.graphics.getHeight();
		
		//for (int i = 0; i < 1; i++)
			//load("big"+i, Texture.class, new TextureLoader(Gdx.files.internal("data/big"+i+".png")));
		
		load("pixel", Texture.class, new PixelGenerator());
		load("sprites", Texture.class, new TextureLoader("data/sprites.png"));
		load("ui", Texture.class, new TextureLoader("data/ui.png"));
		load("background", Texture.class, new BackgroundRenderer(width, height, width/2, height*2/3, width*1.5f, (width > 400 ? 6 : 4)));
		load("regularfont", BitmapFont.class, new BitmapFontLoader("data/regular.fnt", true));
		load("scorefont", BitmapFont.class, new BitmapFontLoader("data/scorefont.fnt", true));
	}
	
	@Override
	public void onComplete() {
		//big = get("big0", Texture.class);
		
		pixel = get("pixel", Texture.class);
		sprites = get("sprites", Texture.class);
			sprites.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		ui = get("ui", Texture.class);
			ui.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		background = get("background", Texture.class);

		regularfont = get("regularfont", BitmapFont.class);
		scorefont = get("scorefont", BitmapFont.class);	
			scorefont.setFixedWidthGlyphs("0123456789");
	}

	
	
}
