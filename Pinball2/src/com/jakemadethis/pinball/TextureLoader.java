package com.jakemadethis.pinball;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;

public class TextureLoader extends AssetLoadingTask<Texture> {

	private final FileHandle file;

	public TextureLoader(FileHandle file) {
		this.file = file;
	}
	
	@Override
	public Texture syncLoad() {
		return new Texture(file);
	}

}
