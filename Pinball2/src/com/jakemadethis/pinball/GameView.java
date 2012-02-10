package com.jakemadethis.pinball;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Random;

import org.lwjgl.opengl.GL20;
import org.lwjgl.util.vector.Matrix;
import org.lwjgl.util.vector.Matrix4f;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.VertexAttribute;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.VertexAttributes.Usage;
import com.badlogic.gdx.graphics.g2d.ParticleEmitter;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;
import com.jakemadethis.pinball.EventHandler.EventListener;
import com.jakemadethis.pinball.BaseModel.EntityArgs;
import com.jakemadethis.pinball.drawing.ImmediateModeRenderer20;
import com.jakemadethis.pinball.drawing.Layer;
import com.jakemadethis.pinball.views.DrawableVisitor;



public class GameView extends BaseView {

	
	private OrthographicCamera worldCamera;
	private OrthographicCamera uiCamera;


	public int score = 0;

	public SpriteBatch world;
	public SpriteBatch ui;
	private ParticleEmitter particleEmitter;
	

	protected Texture spritesTexture;
	protected Texture numbersTexture;

	private Random r;
	public GameModel model;
	
	
	public GameView(GameModel model) {
		super(model);

		this.model = model;
		r = new Random();
		
		worldCamera = new OrthographicCamera(width, height);
		worldCamera.setToOrtho(true);

		uiCamera = new OrthographicCamera(width, height);
		uiCamera.setToOrtho(true);
		
		
	
		Gdx.gl20.glClearColor(0.6f, 0.2f, 0.4f, 1f);

		spritesTexture = new Texture(Gdx.files.internal("data/sprites.png"));
		spritesTexture.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		numbersTexture = new Texture(Gdx.files.internal("data/numbers.png"));
		numbersTexture.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		
		
		//ui.getTextureRenderer().setTexture(0, numbersTexture);
		//world.getTextureRenderer().setTexture(0, spritesTexture);
		
		sprites.put("bumper", new TextureRegion(spritesTexture, 0.25f, 0, 0.5f, 0.25f));
		sprites.put("circle", new TextureRegion(spritesTexture, 0, 0, 0.125f, 0.125f));
		sprites.put("ball", new TextureRegion(spritesTexture, 0.25f, 0, 0.5f, 0.25f));
		sprites.put("line", new TextureRegion(spritesTexture, 0.0f, 0.25f, 0.25f, 0.5f));
		

		world = new SpriteBatch();
		ui = new SpriteBatch();
		ui.setProjectionMatrix(uiCamera.combined);
		
		try {
			particleEmitter = new ParticleEmitter( new BufferedReader(new InputStreamReader(Gdx.files.internal("data/particle").read())));
			Sprite sprite = new Sprite(sprites.get("circle"));
			particleEmitter.setSprite(sprite);
			//particleEmitter.start();
		} catch (IOException e) {
			e.printStackTrace();
		}

		//Texture tex = new Texture(new FileHandle("sprites.png"));
		
		//circleTexture = new TextureRegion(tex, 0, 0, 0.25f, 0.25f);
		//textureHandler.add("square", 0.25f, 0.5f, 0, 0.25f);
		
		final DrawableVisitor visitor = new DrawableVisitor();
		
		// Handlers
		model.entityAddedHandler.add(new EventListener<BaseModel.EntityArgs>() {
			@Override public void invoke(Object sender, EntityArgs args) {
				Entity entity = args.getEntity();
				IDrawable drawable = entity.accept(visitor, GameView.this);
				
				// add entity
				if (drawable != null) drawables.add(drawable);
			}
		});

	}
	
	public void think(float timestep) {
		final float margin = 15f;
		
		worldCamera.zoom = model.width / (width - 2*margin);
		float half = (height/2 - margin) * worldCamera.zoom;
		
		float y = model.getBall().getBody().getPosition().y;
		y = Math.min(Math.max(y, half), model.height - half);
		worldCamera.position.set(model.width/2, y, 0f);
		worldCamera.update();
		world.setProjectionMatrix(worldCamera.combined);

		particleEmitter.setPosition(model.getBall().getBody().getPosition().x, model.getBall().getBody().getPosition().y);
		//particleEmitter.getSprite().setSize(1f, 1f);
		
		if (model.getScore() > score) score++;
		
		if (model.awesomeMode) {
			if (particleEmitter.isComplete())
				particleEmitter.start();
		}
		else {
			particleEmitter.allowCompletion();
		}
	}
	
	
	public void render() {

		Gdx.gl20.glClear(GL10.GL_COLOR_BUFFER_BIT);
		Gdx.gl20.glEnable(GL10.GL_BLEND);
		Gdx.gl20.glEnable(GL10.GL_LINE_SMOOTH);
		Gdx.gl20.glLineWidth(1f);

		Gdx.gl20.glDisable(GL10.GL_CULL_FACE);
		Gdx.gl20.glDisable(GL10.GL_DEPTH_TEST);
		//Gdx.gl20.glBlendFunc(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);
		world.setBlendFunction(GL10.GL_SRC_ALPHA, GL10.GL_ONE);

		
		
		world.begin();
		synchronized (model) {
			
			for (IDrawable drawable : drawables) {
				drawable.draw();
			}
		}
		
		
		particleEmitter.draw(world, Gdx.graphics.getDeltaTime());
			
		world.end();

		ui.begin();
		drawUI();
		ui.end();
	}
	
	private void drawUI() {
		StringBuilder b = new StringBuilder().append(score);
		while(b.length() < 5) b.insert(0, '0');
		drawString(ui, b.toString(), 20f, 20f, 20f, new float[] {1f,1f,1f,1f});
		drawString(ui, String.valueOf(model.combo), 20f, 40f, 20f, new float[] {1f,1f,1f,1f});
		
	}
	
	private void drawString(SpriteBatch batch, String str, float x, float y, float size, float[] color) {
		for (int i = 0; i < str.length(); i++) {
			if (Character.isDigit(str.charAt(i)))
				drawNumber(batch, Character.digit(str.charAt(i), 10), x, y, size, size, color);
			x += size;
		}
	}
	private void drawNumber(SpriteBatch batch, int num, float x, float y, float w, float h, float[] color) {
		float u2 = num * 0.0625f;
		float v2 = (num+1) * 0.0625f;

		batch.draw(numbersTexture, x, y, w, h, 0, u2, 1, v2);
	}


}
