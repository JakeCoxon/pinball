package com.jakemadethis.pinball;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Random;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.BitmapFont.HAlignment;
import com.badlogic.gdx.graphics.g2d.ParticleEmitter;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.jakemadethis.pinball.BaseModel.EntityArgs;
import com.jakemadethis.pinball.EventHandler.EventListener;
import com.jakemadethis.pinball.views.DrawableVisitor;

public class GameView extends BaseView {

	
	private final OrthographicCamera worldCamera;
	private final OrthographicCamera uiCamera;


	public int score = 0;

	public SpriteBatch world;
	public SpriteBatch ui;
	private ParticleEmitter particleEmitter;

	private final Random r;
	public GameModel model;
	private final BitmapFont regularFont;
	private final BitmapFont scoreFont;
	StringBuilder stringBuilder = new StringBuilder();
	
	private final Timer gameOverTimer = new Timer();
	private final Timer shakeTimer = new Timer();
	private float shakeness = 1f;
	
	
	public GameView(GameModel model) {
		super(model);

		this.model = model;
		r = new Random();
		
		worldCamera = new OrthographicCamera(width, height);
		worldCamera.setToOrtho(true);

		uiCamera = new OrthographicCamera(width, height);
		uiCamera.setToOrtho(true);
		
		
	
		Gdx.gl20.glClearColor(0.6f, 0.2f, 0.4f, 1f);

		TextureManager textureMan = TextureManager.get();
		
		//scorefont = new Font(textureMan.scorefont, true);
		//scorefont.setLetterSpacing(-8);

		//regularFont = new Font(textureMan.regularfont, true);
		
		regularFont = new BitmapFont(Gdx.files.internal("data/regular.fnt"), true);
		scoreFont = new BitmapFont(Gdx.files.internal("data/scorefont.fnt"), true);
		scoreFont.setFixedWidthGlyphs("0123456789");
		
		//ui.getTextureRenderer().setTexture(0, numbersTexture);
		//world.getTextureRenderer().setTexture(0, spritesTexture);
		
		sprites.put("bumper", new TextureRegion(textureMan.sprites, 0.25f, 0, 0.5f, 0.25f));
		sprites.put("circle", new TextureRegion(textureMan.sprites, 0, 0, 0.125f, 0.125f));
		sprites.put("ball", new TextureRegion(textureMan.sprites, 0.25f, 0, 0.5f, 0.25f));
		sprites.put("line", new TextureRegion(textureMan.sprites, 0.0f, 0.25f, 0.25f, 0.5f));
		sprites.put("rect", new TextureRegion(textureMan.sprites, 0.5f, 0f, 0.75f, 0.25f));
		

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
	
	public void shake() {
		if (shakeTimer.running()) shakeness += 0.05f;
		else shakeness = 0.05f;
		shakeTimer.start(0.5f);
	}
	
	@Override
	public void think(float timestep) {
		final float margin = 15f;
		
		worldCamera.zoom = model.width / (width - 2*margin);
		float half = (height/2 - margin) * worldCamera.zoom;
		
		float y = model.getBall().getBody().getPosition().y;
		y = Math.min(Math.max(y, half), model.height - half);
		
		float shakex = 0;
		float shakey = 0; 
		if (shakeTimer.running()) {
			float t = shakeTimer.value(shakeness, 0f);
			shakex = r.nextFloat() * t;
			shakey = r.nextFloat() * t;
		}
		worldCamera.position.set(model.width/2 + shakex, y + shakey, 0f);
		worldCamera.update();
		world.setProjectionMatrix(worldCamera.combined);

		particleEmitter.setPosition(model.getBall().getBody().getPosition().x, model.getBall().getBody().getPosition().y);
		//particleEmitter.getSprite().setSize(1f, 1f);
		
		if (model.getScore() > score) score++;
		
		if (model.awesomeMode) {
			if (particleEmitter.isComplete())
				particleEmitter.start();
		} else {
			particleEmitter.allowCompletion();
		}
	}
	
	
	@Override
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
			
			for (int i = 0; i < drawables.size(); i++) {
				drawables.get(i).draw();
			}
		}
		
		
		particleEmitter.draw(world, Gdx.graphics.getDeltaTime());
			
		world.end();

		drawUI();
	}
	
	private void drawUI() {
		ui.begin();
		stringBuilder.setLength(0);
		stringBuilder.append(score);
		while(stringBuilder.length() < 5) stringBuilder.insert(0, '0');
		String scoreText = String.valueOf(model.combo);

		drawTextShadow(ui, scoreFont, stringBuilder.toString(), 20f, 20f);
		drawTextShadow(ui, scoreFont, String.valueOf(model.balls), 0, 20f, width-20f, HAlignment.RIGHT);
		drawTextShadow(ui, scoreFont, scoreText, 20f, 45f);
		
		if (model.gameOver && gameOverTimer.getLength() == 0) {
			gameOverTimer.start(0.5f);
		}
		if (gameOverTimer.getLength() > 0) {
			float x = Interpolator.easeOutQuad(gameOverTimer, -width, 0);
			
			ui.setColor(0f, 0f, 0f, 0.7f);
			ui.draw(getSprite("rect"), x, 0, width, height);
			ui.setColor(1f, 1f, 1f, 1f);
			drawTextShadow(ui, regularFont, "Game", x, 100f, width, HAlignment.CENTER);
			drawTextShadow(ui, regularFont, "Over", x, 164f, width, HAlignment.CENTER);
			drawTextShadow(ui, regularFont, String.valueOf(model.getScore()), x, 300f, width, HAlignment.CENTER);
		}

		ui.end();
	}

	private void drawTextShadow(SpriteBatch batch, BitmapFont font, String text, float x, float y) {
		drawTextShadow(batch, font, text, x, y, width, HAlignment.LEFT);
	}
	private void drawTextShadow(SpriteBatch batch, BitmapFont font, String text, float x, float y, float alignWidth, HAlignment align) {

		font.setColor(0f, 0f, 0f, 0.5f);
		font.drawMultiLine(batch, text, x-1, y-1, alignWidth, align);
		
		font.setColor(1f, 1f, 1f, 1f);
		font.drawMultiLine(batch, text, x, y, alignWidth, align);
	}

	
	
	


}
