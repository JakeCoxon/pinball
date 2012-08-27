package com.jakemadethis.pinball.game.views;

import com.jakemadethis.pinball.Entity;
import com.jakemadethis.pinball.IDrawable;
import com.jakemadethis.pinball.game.GameView;
import com.jakemadethis.pinball.level.Gem;

public class GemDrawable implements IDrawable {
	
	private final Gem gem;
	private final GameView view;

	public GemDrawable(Gem gem, GameView view) {
		this.gem = gem;
		this.view = view;
	}

	@Override
	public Entity getEntity() {
		return gem;
	}
	
	@Override
	public void draw() {
		view.world.setColor(1f, 1f, 1f, 0.4f);
		view.drawRect(view.world, gem.getX(), gem.getY(), 0.2f, 0.2f);
	}
	
}
