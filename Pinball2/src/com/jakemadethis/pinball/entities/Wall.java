package com.jakemadethis.pinball.entities;

import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;

import com.jakemadethis.pinball.Entity;
import com.jakemadethis.pinball.EventHandler.EventListener;
import com.jakemadethis.pinball.GameView;
import com.jakemadethis.pinball.IDrawable;
import com.jakemadethis.pinball.IView;
import com.jakemadethis.pinball.GameModel;
import com.jakemadethis.pinball.io.Input;
import com.jakemadethis.pinball.io.InputHandler;
import com.jakemadethis.pinball.io.Input.EventArgs;

public class Wall extends WallPath implements IElement, EventListener<Input.EventArgs> {
	
	public Wall(World world, float x0, float y0, float x1, float y1, float restitution) {
		super(world, new float[]{x0, y0, x1, y1}, restitution);
	}
}
