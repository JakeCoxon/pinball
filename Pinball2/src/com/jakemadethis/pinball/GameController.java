package com.jakemadethis.pinball;

import com.badlogic.gdx.Application.ApplicationType;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.joints.MouseJoint;
import com.badlogic.gdx.physics.box2d.joints.MouseJointDef;
import com.jakemadethis.pinball.builder.IBuilder;
import com.jakemadethis.pinball.builder.PinballFactory;
import com.jakemadethis.pinball.builder.XMLBuilder;

public class GameController implements InputProcessor {
  public static float gameSpeed = 1f;
	
	final private GameModel model;
	final private GameView view;
	private final Pinball pinball;
	private final Timer gamePausedTimer = new Timer();
	private final String levelName;

	private boolean dragging;

	private MouseJoint mouseJoint;
	
	public GameController(Pinball pinball, final GameModel model, final GameView view, String levelName) {
		
		this.pinball = pinball;
		this.model = model;
		this.view = view;
		
		Gdx.input.setInputProcessor(this);
		
		this.levelName = levelName;
		loadLevel();
		


		/*Thread thread = new Thread(new Runnable() {
			@Override public void run() {
				Server server = new Server(4444, new Protocol(model));
				server.start();
			}
		});
		thread.start();*/
		
		//new Client("127.0.0.1", 4444, new Protocol(model));
		
	}

	private void loadLevel() {
		model.scale = 100f;

		try {
			// Android needs this for SAX
			if (Gdx.app.getType() == ApplicationType.Android)
				System.setProperty("org.xml.sax.driver", "org.xmlpull.v1.sax2.Driver");
			
			FileHandle file = Gdx.files.internal("data/"+levelName+".xml");
			IBuilder b = XMLBuilder.fromStream(file.read());
			
			PinballFactory factory = new PinballFactory(model);
			b.create(factory);
		}
		catch (LevelException e) {
			e.printStackTrace();
		}

		//Log.d("JAKE", "initGame");
		model.initGame();
	}
	
	public void resetGame() {
		model.reset();
	}
	

	
	public void run() {
		
		if (dragging) {
			Vector3 mouse = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0);
			view.worldCamera.unproject(mouse);
			Vector2 mouse2 = new Vector2(mouse.x, mouse.y);
			mouseJoint.setTarget(mouse2);
		}
		
		float delta = Gdx.graphics.getDeltaTime();
		model.think(delta * gameSpeed, 4);
		view.think(delta * gameSpeed);
		view.render();
		
		if (model.gameOver) {
			if (!gamePausedTimer.started())
				gamePausedTimer.start(0.5f);
		}
	}

	@Override
	public boolean keyDown(int keycode) {
		if (keycode == Keys.T) {
			loadLevel();
			return true;
		}
		else if (keycode == Keys.BACK || keycode == Keys.BACKSPACE) {
			pinball.setMenu();
		}
		return false;
	}


	@Override
	public boolean keyUp(int keycode) {
		return false;
	}

	@Override
	public boolean keyTyped(char character) {
		return false;
	}

	@Override
	public boolean touchDown(int x, int y, int pointer, int button) {
		if (model.gameOver) {
			if(gamePausedTimer.finished())
				pinball.setMenu();
			return true; 
		}
		
		if (button == 0) {
			if (model.getBall().isActive()) {
				model.engageFlipper(true);
			}
			else {
				model.getBall().launch();
			}
		}
		else if (button == 1) {
			if (model.getBall().isActive()) {
				dragging = true;
				
				view.setScrolling(false);
				Vector3 mouse = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0);
				view.worldCamera.unproject(mouse);
				Vector2 mouse2 = new Vector2(mouse.x, mouse.y);
				
        BodyDef groundBodyDef = new BodyDef();
        groundBodyDef.position.set(model.getBall().getBody().getPosition());
        Body groundBody = model.world.createBody(groundBodyDef);
        
				MouseJointDef mjd = new MouseJointDef();
        mjd.bodyA               = groundBody;
        mjd.bodyB               = model.getBall().getBody();
        mjd.dampingRatio        = 1f;
        mjd.frequencyHz         = 10;
        mjd.maxForce                    = (200.0f * model.getBall().getBody().getMass());
        mjd.collideConnected= true;
        mjd.target.set(model.getBall().getBody().getPosition());
				mouseJoint = (MouseJoint) model.world.createJoint(mjd);
			}
		}
		return false;
	}

	@Override
	public boolean touchUp(int x, int y, int pointer, int button) {
		model.engageFlipper(false);
		if (dragging) {
			dragging = false;
			model.world.destroyJoint(mouseJoint);
			view.setScrolling(true);
		}
		return true;
	}

	@Override
	public boolean touchDragged(int x, int y, int pointer) {
		return false;
	}

	@Override
	public boolean touchMoved(int x, int y) {
		return false;
	}

	@Override
	public boolean scrolled(int amount) {
		return false;
	}
	


	/*public boolean onTouchEvent(MotionEvent event) {
		if (event.getAction() == MotionEvent.ACTION_DOWN) {
			float[] pos = new float[2];
			view.getCamera().screenToWorld(pos, event.getX(), event.getY());
			
			if (!model.getBall().isActive())
				model.getBall().launch();
			else
				model.engageFlipper(true);
			//addBall(pos[0]*100 - 15f/2, pos[1]*100 - 15f/2, 15f);
		}
		else if (event.getAction() == MotionEvent.ACTION_UP) {
			model.engageFlipper(false);
		}
		return false;
	}*/
	



}
