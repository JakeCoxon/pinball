package com.jakemadethis.pinballeditor;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;

import javax.tools.Tool;

import com.badlogic.gdx.Application.ApplicationType;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.files.FileHandle;
import com.jakemadethis.net.Client;
import com.jakemadethis.pinball.LevelException;
import com.jakemadethis.pinball.builder.IBuilder;
import com.jakemadethis.pinball.builder.PinballFactory;
import com.jakemadethis.pinball.builder.XMLBuilder;
import com.jakemadethis.pinballeditor.FileMonitor.FileChangedListener;

public class EditorController implements InputProcessor {
	private final EditorModel model;
	private final EditorView view;
	private Tool tool;
	private Client client;
	private Tool[] tools;
	private boolean firstLoad;

	public EditorController(final EditorModel model, final EditorView view) {
		this.model = model;
		this.view = view;
		this.firstLoad = true;
		

		System.setOut(new PrintStream(new OutputStream() {
			@Override public void write(int arg0) throws IOException {
				
			}
		}));
		
		load();
		
		FileMonitor monitor = new FileMonitor(500);
		monitor.setFileChangedListener(new FileChangedListener() {
			@Override public void fileChanged(File file) {
				load();
			}
		});

		
		monitor.addFile(new File("data/level.xml"));
		
	}
	
	public void load() {

		model.clear();
		model.scale = 100f;
		

		try {
			// Android needs this for SAX
			if (Gdx.app.getType() == ApplicationType.Android)
				System.setProperty("org.xml.sax.driver", "org.xmlpull.v1.sax2.Driver");
			
			FileHandle file = Gdx.files.internal("data/level.xml");
			IBuilder b = XMLBuilder.fromStream(file.read());
			
			PinballFactory factory = new PinballFactory(model);
			b.create(factory);
		}
		catch (LevelException e) {
			e.printStackTrace();
		}

		//Log.d("JAKE", "initGame");
		model.initGame();
		
		if (this.firstLoad) {
			view.resetCamera();
			this.firstLoad = false;
		}
		

		
		//client = new Client("127.0.0.1", 4444, new EditorClient(model));
		
		Gdx.input.setInputProcessor(this);
		
		
		
	}
	
	
	public void run() {
		model.think(0.01f, 4);
		view.think(0.01f);
		view.render();
	}

	@Override
	public boolean keyDown(int keycode) {
		if (keycode == Keys.F1) { load(); }
		
		return false;
	}

	@Override
	public boolean keyUp(int keycode) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean keyTyped(char character) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean touchDown(int x, int y, int pointer, int button) {
		return false;
	}

	@Override
	public boolean touchUp(int x, int y, int pointer, int button) {
		return false;
	}

	@Override
	public boolean touchDragged(int x, int y, int pointer) {
		return false;
	}

	@Override
	public boolean touchMoved(int x, int y) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean scrolled(int amount) {
		// TODO Auto-generated method stub
		return false;
	}
}
