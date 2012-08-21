package com.jakemadethis.pinball.game;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Random;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.TextureData;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.BitmapFont.HAlignment;
import com.badlogic.gdx.graphics.g2d.ParticleEmitter;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.jakemadethis.pinball.AssetLoader.OnLoadedListener;
import com.jakemadethis.pinball.BaseModel;
import com.jakemadethis.pinball.BaseModel.EntityArgs;
import com.jakemadethis.pinball.BaseView;
import com.jakemadethis.pinball.Entity;
import com.jakemadethis.pinball.EventHandler;
import com.jakemadethis.pinball.EventHandler.EventListener;
import com.jakemadethis.pinball.IDrawable;
import com.jakemadethis.pinball.Interpolator;
import com.jakemadethis.pinball.Timer;
import com.jakemadethis.pinball.game.views.DrawableVisitor;

public class GameView extends BaseView implements OnLoadedListener {

	
	public final OrthographicCamera worldCamera;
	public final OrthographicCamera uiCamera;


	public int score = 0;

	public SpriteBatch world;
	public SpriteBatch ui;
	private ParticleEmitter awesomeEffect;

	private final Random r;
	public GameModel model;
	StringBuilder stringBuilder = new StringBuilder();
	
	private final Timer gameOverTimer = new Timer();
	private final Timer shakeTimer = new Timer();
	private final Timer slideInTimer = new Timer();
	private float shakeness = 1f;
	private ParticleEmitter spawnEffect;
	private boolean firstBall;
	private boolean scrolling = true;
	private float scrollY;
	private Stage stage;
	private boolean instantScroll;
	public Texture spritesTexture;
	
	private GameOverScreen gameOverScreen;
	private FrameBuffer fgFrameBuffer;
	
	public GameView(GameModel model) {
		super(model);

		this.model = model;
		r = new Random();
		PinballAssets.get().setOnLoadedListener(this);
		
		worldCamera = new OrthographicCamera(width, height);
		worldCamera.setToOrtho(true);

		uiCamera = new OrthographicCamera(width, height);
		uiCamera.setToOrtho(true);

		
	
		//assets = PinballAssetManager.get();
		//assets.loadGameAssets();
	}
	@Override
	public void onLoaded() {
		
		//scorefont = new Font(textureMan.scorefont, true);
		//scorefont.setLetterSpacing(-8);

		//regularFont = new Font(textureMan.regularfont, true);

		
		//ui.getTextureRenderer().setTexture(0, numbersTexture);
		//world.getTextureRenderer().setTexture(0, spritesTexture);
		
		
		sprites.put("bumper", new TextureRegion(PinballAssets.sprites, 0.25f, 0, 0.5f, 0.25f));
		sprites.put("circle", new TextureRegion(PinballAssets.sprites, 0, 0, 0.125f, 0.125f));
		sprites.put("ball", new TextureRegion(PinballAssets.sprites, 0.25f, 0, 0.5f, 0.25f));
		sprites.put("line", new TextureRegion(PinballAssets.sprites, 0.0f, 0.25f, 0.25f, 0.5f));
		sprites.put("glow", new TextureRegion(PinballAssets.sprites, 0.0f, 0.5f, 0.25f, 0.75f));
		sprites.put("rect", new TextureRegion(PinballAssets.sprites, 0.5f, 0f, 0.75f, 0.25f));
		sprites.put("donut", new TextureRegion(PinballAssets.sprites, 0.25f, 0.25f, 0.5f, 0.5f));
		sprites.put("gradient", new TextureRegion(PinballAssets.sprites, 0.25f, 0.5f, 0.5f, 0.75f));
		

		world = new SpriteBatch();
		ui = new SpriteBatch();
		ui.setProjectionMatrix(uiCamera.combined);

		stage = new Stage(width, height, false, ui);
		((OrthographicCamera)stage.getCamera()).setToOrtho(true);
		
		gameOverScreen = new GameOverScreen(this, width, height);
		gameOverScreen.visible = false;
		stage.addActor(gameOverScreen);
		
		
		
		try {
			awesomeEffect = new ParticleEmitter( new BufferedReader(new InputStreamReader(Gdx.files.internal("data/particle").read())));
			Sprite sprite = new Sprite(sprites.get("circle"));
			awesomeEffect.setSprite(sprite);
			//particleEmitter.start();
		} catch (IOException e) {
			e.printStackTrace();
		}
		try {
			spawnEffect = new ParticleEmitter( new BufferedReader(new InputStreamReader(Gdx.files.internal("data/spawneffect").read())));
			Sprite sprite = new Sprite(sprites.get("donut"));
			spawnEffect.setSprite(sprite);
			//particleEmitter.start();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		slideInTimer.start(0.5f);
		firstBall = true;
		instantScroll = true;

		// Render background
		{
			//bgRenderer = new BackgroundRenderer((int)width, (int)height);
			//bgRenderer.generate(width/2, height*2/3, width*1.5f, (width > 400 ? 6 : 4));
		}
		
		final DrawableVisitor visitor = new DrawableVisitor();
		
		// Create drawables for all entities
		for (Entity ent : model.entities) {
			IDrawable drawable = ent.accept(visitor, GameView.this);
			
			// add entity
			if (drawable != null) drawables.add(drawable);
		}
		
		// Handlers
		model.entityAddedHandler.add(new EventListener<BaseModel.EntityArgs>() {
			@Override public void invoke(Object sender, EntityArgs args) {
				Entity entity = args.getEntity();
				IDrawable drawable = entity.accept(visitor, GameView.this);
				
				// add entity
				if (drawable != null) drawables.add(drawable);
			}
		});
		
		model.newBallHandler.add(new EventHandler.ActionListener() {
			@Override public void invoke(Object sender, Object args) {
				// First ball is handled by the slideInTimer
				if (!firstBall)  {
					newBallEffect();
				}
			}
		});
		
		model.gameOverHandler.add(new EventHandler.ActionListener() {
			@Override public void invoke(Object sender, Object args) {
				gameOver();
			}
		});

	}
	
	private void gameOver() {
		gameOverScreen.visible = true;
		gameOverScreen.prevInputProcessor = Gdx.input.getInputProcessor();
		gameOverScreen.setScore(model.getScore());
		Gdx.input.setInputProcessor(stage);
	}

	protected void newBallEffect() {
		spawnEffect.setPosition(model.getBall().getInitialPos().x, model.getBall().getInitialPos().y);
		spawnEffect.start();
	}

	public void shake() {
		if (shakeTimer.running()) shakeness += 0.05f;
		else shakeness = 0.05f;
		shakeTimer.start(0.5f);
		Gdx.input.vibrate(50);  
	}
	
	@Override
	public void think(float timestep) {
		
		if (!PinballAssets.get().update()) return;
		
		final float margin = 15f;
		
		worldCamera.zoom = model.width / (width - 2*margin);
		float half = (height/2 - margin) * worldCamera.zoom;
		
		if (scrolling) {
			float target = model.getBall().getBody().getPosition().y;
			target = Math.min(Math.max(target, half), model.height - half);
			if (instantScroll) {
				scrollY = target;
				instantScroll = false;
			}
			else 
				scrollY += (target - scrollY) / 4;
		}
		
		float shakex = 0;
		float shakey = 0; 
		if (shakeTimer.running()) {
			float t = shakeTimer.value(shakeness, 0f);
			shakex = r.nextFloat() * t;
			shakey = r.nextFloat() * t;
		}
		
		if (slideInTimer.getTimeElapsed() > 0.7f && firstBall) {
			newBallEffect();
			firstBall = false;
		}
		
		float slideX = slideInTimer.finished() ? 0 : Interpolator.easeOutQuad(slideInTimer, model.width, 0);
		
		worldCamera.position.set(model.width/2 + shakex - slideX, scrollY + shakey, 0f);
		worldCamera.update();
		world.setProjectionMatrix(worldCamera.combined);

		awesomeEffect.setPosition(model.getBall().getBody().getPosition().x, model.getBall().getBody().getPosition().y);
		//particleEmitter.getSprite().setSize(1f, 1f);
		
		if (model.getScore() > score) {
			
			if (model.getScore() - score > 200) score += 4;
			else score ++;
		}
		
		if (model.awesomeMode) {
			if (awesomeEffect.isComplete())
				awesomeEffect.start();
		} else {
			awesomeEffect.allowCompletion();
		}
	}
	
	
	@Override
	public void render() {

		if (!PinballAssets.get().isCompleted()) {

			Gdx.gl20.glClearColor(0f, 0f, 0f, 1f);
			Gdx.gl20.glClear(GL20.GL_COLOR_BUFFER_BIT);
			
			return;
		}
		Gdx.gl20.glClearColor(0f, 0f, 0f, 1f);
		Gdx.gl20.glClear(GL10.GL_COLOR_BUFFER_BIT);
		Gdx.gl20.glEnable(GL10.GL_BLEND);
		Gdx.gl20.glEnable(GL10.GL_LINE_SMOOTH);
		Gdx.gl20.glLineWidth(1f);

		Gdx.gl20.glDisable(GL10.GL_CULL_FACE);
		Gdx.gl20.glDisable(GL10.GL_DEPTH_TEST);
		

		ui.begin();
		ui.setColor(1f, 1f, 1f, 1f);
		ui.draw(PinballAssets.background, 0, 0, width, height);
		ui.end();

		if (fgFrameBuffer == null)
			fgFrameBuffer = new FrameBuffer(Format.RGBA8888, (int)width, (int)height, false);
		fgFrameBuffer.begin();
		Gdx.gl20.glClearColor(0f, 0f, 0f, 0f);
		Gdx.gl20.glClear(GL10.GL_COLOR_BUFFER_BIT);
		world.begin();
		
		setBlendMode(world, BlendMode.Additive);

		
		
		synchronized (model) {
			
			for (int i = 0; i < drawables.size(); i++) {
				drawables.get(i).draw();
			}
		}
		
		
		awesomeEffect.draw(world, Gdx.graphics.getDeltaTime());
		spawnEffect.draw(world, Gdx.graphics.getDeltaTime());
		
			
		world.end();
		fgFrameBuffer.end();
		

		TextureData t = fgFrameBuffer.getColorBufferTexture().getTextureData();
		Pixmap pix = t.consumePixmap();
		//a: for (int i = 0; i < t.getWidth(); i++) {
			//for (int j = 0; j < t.getHeight(); j++) {
		//System.out.println(pix.getWidth()+" "+pix.getHeight());
		//int i = 50, j = 50;
			//	int p = pix.getPixel(i, j);
				/*int a = p & 0xFF;
				int b = (p >> 8) & 0xFF;
				int g = (p >> 16) & 0xFF;
				int r = (p >> 24) & 0xFF;
				//if ((a & 0xFF) == 0) {
					System.out.println(i+" "+j+" "+Integer.toHexString(p)+" "+r+" "+g+" "+b+" "+a);*/
					//break a;
				//}
			//}
		//}
		
		ui.begin();
		ui.setBlendFunction(GL10.GL_ONE, GL10.GL_ONE);
		ui.draw(fgFrameBuffer.getColorBufferTexture(), 0, 0, width, height);
		ui.end();
		

		drawUI();
	}
	
	private void drawUI() {
		ui.begin();
		setBlendMode(ui, BlendMode.Normal);
		stringBuilder.setLength(0);
		stringBuilder.append(score);
		while(stringBuilder.length() < 5) stringBuilder.insert(0, '0');
		String scoreText = String.valueOf(model.combo);


		float textAlpha = slideInTimer.finished() ? 1f : Interpolator.easeOutQuad(slideInTimer, 0f, 1f);
		
		drawTextShadow(ui, PinballAssets.scorefont, stringBuilder.toString(), 20f, 20f, textAlpha);
		drawTextShadow(ui, PinballAssets.scorefont, String.valueOf(model.balls), 0, 20f, width-20f, HAlignment.RIGHT, textAlpha);
		drawTextShadow(ui, PinballAssets.scorefont, scoreText, 20f, 45f, textAlpha);
		
		/*if (model.gameOver && gameOverTimer.getLength() == 0) {
			gameOverTimer.start(0.5f);
		}
		if (gameOverTimer.getLength() > 0) {
			float x = Interpolator.easeOutQuad(gameOverTimer, -width/2, 0);
			float alpha = Interpolator.easeOutQuad(gameOverTimer, 0, 0.7f);
			
			ui.setColor(0f, 0f, 0f, alpha);
			drawRect(ui, x, 0, width, height);
			ui.setColor(1f, 1f, 1f, 1f);
			drawTextShadow(ui, regularFont, "Game", x, 100f, width, HAlignment.CENTER, 1f);
			drawTextShadow(ui, regularFont, "Over", x, 164f, width, HAlignment.CENTER, 1f);
			drawTextShadow(ui, regularFont, String.valueOf(model.getScore()), x, 300f, width, HAlignment.CENTER, 1f);
		}*/
		
		ui.end();
		
		stage.draw();
	}

	private void drawTextShadow(SpriteBatch batch, BitmapFont font, String text, float x, float y, float alpha) {
		drawTextShadow(batch, font, text, x, y, width, HAlignment.LEFT, alpha);
	}
	private void drawTextShadow(SpriteBatch batch, BitmapFont font, String text, float x, float y, float alignWidth, HAlignment align, float alpha) {

		font.setColor(0f, 0f, 0f, alpha * 0.5f);
		font.drawMultiLine(batch, text, x-1, y-1, alignWidth, align);
		
		font.setColor(1f, 1f, 1f, alpha);
		font.drawMultiLine(batch, text, x, y, alignWidth, align);
	}

	public void setScrolling(boolean b) {
		this.scrolling = b;
	}

	

	
	
	


}
