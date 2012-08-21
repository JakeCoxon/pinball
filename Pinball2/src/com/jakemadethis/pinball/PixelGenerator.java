package com.jakemadethis.pinball;

import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.Texture;

public class PixelGenerator extends AssetLoadingTask<Texture> {

	@Override
	public Texture syncLoad() {
		Pixmap pixmap = new Pixmap(1, 1, Format.RGB565);
		pixmap.setColor(1f, 1f, 1f, 1f);
		pixmap.drawPixel(0, 0);
		return new Texture(pixmap);
	}

}
