package com.jakemadethis.pinballeditor;

import java.util.ArrayList;
import java.util.LinkedList;

import org.lwjgl.Sys;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.jakemadethis.pinball.BaseModel;
import com.jakemadethis.pinball.BaseView;
import com.jakemadethis.pinball.Entity;
import com.jakemadethis.pinball.GameView;
import com.jakemadethis.pinball.IDrawable;
import com.jakemadethis.pinball.IView;
import com.jakemadethis.pinball.BaseModel.EntityArgs;
import com.jakemadethis.pinball.EventHandler.EventListener;
import com.jakemadethis.pinball.entities.WallPath;
import com.jakemadethis.pinballeditor.tools.Tool;
import com.jakemadethis.pinballeditor.views.DrawableVisitor;

public class EditorView extends BaseView {

	private EditorModel model;
	public OrthographicCamera worldCamera;
	private float gridSize = 0.32f;
	public SpriteBatch world;
	private Tool tool;
	
	
	
	
	public float snap(float num) {
		if (Gdx.input.isKeyPressed(Keys.CONTROL_LEFT)) return num;
		return Math.round(num/gridSize)*gridSize;
	}

	public EditorView(EditorModel model) {
		super(model);
		this.model = model;
		
		worldCamera = new OrthographicCamera(width, height);
		worldCamera.setToOrtho(true);
		worldCamera.position.set(model.width/2, model.height/2, 0f);
		worldCamera.zoom = model.height / (height - 20f);
		
		Gdx.gl20.glClearColor(0f, 0f, 0f, 1f);
		
		world = new SpriteBatch();
		

		Texture spritesTexture = new Texture(Gdx.files.internal("data/sprites.png"));
		spritesTexture.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		
		
		//ui.getTextureRenderer().setTexture(0, numbersTexture);
		//world.getTextureRenderer().setTexture(0, spritesTexture);
		
		sprites.put("bumper", new TextureRegion(spritesTexture, 0.25f, 0, 0.5f, 0.25f));
		sprites.put("circle", new TextureRegion(spritesTexture, 0, 0, 0.125f, 0.125f));
		sprites.put("ball", new TextureRegion(spritesTexture, 0.25f, 0, 0.5f, 0.25f));
		sprites.put("line", new TextureRegion(spritesTexture, 0.0f, 0.25f, 0.25f, 0.5f));

		final DrawableVisitor visitor = new DrawableVisitor();
		
		// Handlers
		model.entityAddedHandler.add(new EventListener<BaseModel.EntityArgs>() {
			@Override public void invoke(Object sender, EntityArgs args) {
				Entity entity = args.getEntity();
				IDrawable drawable = entity.accept(visitor, EditorView.this);
				
				// add entity
				if (drawable != null) drawables.add(drawable);
			}
		});
		model.entityRemovedHandler.add(new EventListener<BaseModel.EntityArgs>() {
			@Override public void invoke(Object sender, EntityArgs args) {
				
				Entity entity = args.getEntity();
				LinkedList<IDrawable> newList = new LinkedList<IDrawable>();
				
				for (IDrawable drawable : drawables) {
					if (entity != drawable.getEntity()) {
						newList.add(drawable);
					}
				}
				drawables = newList;
			}
		});
		
		
	}
	
	@Override
	public void think(float timeStep) {
		
		//worldCamera.position.set(0f, 0f, 0f);
		//worldCamera.position.add(0.1f, 0.1f, 0f);
		int dx = (Gdx.input.isKeyPressed(Keys.RIGHT) ? 1 : 0) - (Gdx.input.isKeyPressed(Keys.LEFT) ? 1 : 0);
		int dy = (Gdx.input.isKeyPressed(Keys.DOWN) ? 1 : 0) - (Gdx.input.isKeyPressed(Keys.UP) ? 1 : 0);
		int dz = (Gdx.input.isKeyPressed(Keys.Z) ? 1 : 0) - (Gdx.input.isKeyPressed(Keys.A) ? 1 : 0);
		
		worldCamera.zoom = Math.max(0.01f, worldCamera.zoom + dz  * 0.001f);
		worldCamera.position.add(dx*0.1f, dy*0.1f, 0f);
		worldCamera.update();
		world.setProjectionMatrix(worldCamera.combined);
	}
	
	
	@Override
	public void render() {

		Gdx.gl20.glClear(GL10.GL_COLOR_BUFFER_BIT);
		Gdx.gl20.glEnable(GL10.GL_BLEND);
		Gdx.gl20.glEnable(GL10.GL_LINE_SMOOTH);
		Gdx.gl20.glLineWidth(1f);

		Gdx.gl20.glDisable(GL10.GL_CULL_FACE);
		Gdx.gl20.glDisable(GL10.GL_DEPTH_TEST);
		Gdx.gl20.glBlendFunc(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);
		

		synchronized (model) {

			world.begin();
			world.setColor(1f, 1f, 1f, 0.1f);
			
			for (int i = 0; i <= model.height/0.32f; i++) {
				drawHairLine(world, 0f, i * gridSize, model.width, i * gridSize, 2f);
			}
			for (int i = 0; i <= model.width/0.32f; i++) {
				drawHairLine(world, i * gridSize, 0, i * gridSize, model.height, 2f);
			}
			
			for (IDrawable drawable : drawables) {
				drawable.draw();
			}
			tool.draw();
			
			
			//world.render(worldCamera.combined);
			
			world.end();
		}
		
		//spriteBatch.setColor(1f, 0f, 0f, 0.5f);
	}
	
	
	public void drawHairLine(SpriteBatch batch, float x1, float y1, float x2, float y2, float width) {
		drawLine(batch, x1, y1, x2, y2, worldCamera.zoom * width);
	}
	
	public SpriteBatch getSpriteBatch() {
		return world;
	}

	public void setTool(Tool tool) {
		this.tool = tool;
	}

	

}
