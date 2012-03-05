package com.jakemadethis.pinball;

import java.util.HashMap;
import java.util.LinkedList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public abstract class BaseView implements IView {
	public EventHandler<Object> initGameHandler = new EventHandler<Object>();

	public BaseModel model;
	protected LinkedList<IDrawable> drawables;

	protected final float width;
	protected final float height;

	private final Texture pixelTexture;

	//protected ShaderProgram flatShader;
	//protected ShaderProgram textureShader;
	protected HashMap<String, TextureRegion> sprites;

	//public Layer ui;
	//public Layer world;
	
	public BaseView(BaseModel model) {
		this.model = model;
		
		drawables = new LinkedList<IDrawable>();
		

		width = Gdx.graphics.getWidth();
		height = Gdx.graphics.getHeight();
		
		Pixmap pixmap = new Pixmap(1, 1, Format.RGB565);
		pixmap.setColor(1f, 1f, 1f, 1f);
		pixmap.drawPixel(0, 0);
		pixelTexture = new Texture(pixmap);
		pixelTexture.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		

		/*flatShader = new ShaderProgram(
				ImmediateModeRenderer20.createVertexShader(false, true, 0),
				ImmediateModeRenderer20.createFragmentShader(false, true, 0)
		);
		if (!flatShader.isCompiled()) throw new RuntimeException(flatShader.getLog());
		textureShader = new ShaderProgram(
				ImmediateModeRenderer20.createVertexShader(false, true, 1),
				ImmediateModeRenderer20.createFragmentShader(false, true, 1)
		);
		if (!textureShader.isCompiled()) throw new RuntimeException(textureShader.getLog());*/

		sprites = new HashMap<String, TextureRegion>();
		
		
		

		/*ui = new Layer(flatShader, flatShader, textureShader);
		world = new Layer(flatShader, flatShader, textureShader);*/
		
		
		

	}
	
	public void addDrawable(IDrawable d) {
		drawables.add(d);
	}
	
	public void drawLine(SpriteBatch batch, float x1, float y1, float x2, float y2, float width) {
		float dy = y2 - y1;
		float dx = x2 - x1;
		float angle = (float) Math.toDegrees(Math.atan2(dy, dx));
		float length = (float) Math.sqrt(dx * dx + dy * dy);
		TextureRegion region = sprites.get("line");
		if (region == null) throw new RuntimeException("Needs a line texture");
		batch.draw(region, x1, y1, 0f, width/2, length, width, 1f, 1f, angle);
		//batch.draw(pixelTexture, x1, y1, 0f, width/2, length, width, 1f, 1f, angle, 0, 0, 1, 1, false, false);
	}
	
	public void drawRect(SpriteBatch batch, float x, float y, float w, float h) {
		batch.draw(pixelTexture, x, y, w, h);
	}

	public TextureRegion getSprite(String name) {
		return sprites.get(name);
	}
}
