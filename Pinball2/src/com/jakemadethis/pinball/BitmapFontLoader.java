package com.jakemadethis.pinball;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.g2d.BitmapFont;

public class BitmapFontLoader extends AssetLoadingTask<BitmapFont> {

	private final FileHandle file;
	private final boolean flipped;

	public BitmapFontLoader(FileHandle file, boolean flipped) {
		this.file = file;
		this.flipped = flipped;
	}
	
	public BitmapFontLoader(String file, boolean flipped) {
		this(Gdx.files.internal(file), flipped);
	}

	@Override
	public BitmapFont syncLoad() {
		return new BitmapFont(file, flipped);
	}

}
