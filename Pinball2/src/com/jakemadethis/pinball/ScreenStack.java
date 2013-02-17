package com.jakemadethis.pinball;

import java.util.HashMap;
import java.util.LinkedList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.jakemadethis.pinball.ScreenTransition.Reversable;

public class ScreenStack {
	
	private Screen top = null;
	private boolean toPop = false;
	
	public void setTopScreen(IFactory<Screen> factory, ScreenTransition transition) {
		//if (top != null && top.getTransition() != null && !top.getTransition().isFinished()) return;

		Screen newscreen = factory.create();
		newscreen.setParentScreen(top);
		top = newscreen;

		setTransition(transition);
		//top.setTransition(transition);
		
		//if (transition != null) transition.start(this, top);
		printState();
		
		setInput(top);
		
	}
	
	private void setInput(Screen screen) {
		Gdx.input.setInputProcessor(new InputMultiplexer(screen.getStage(), screen));
	}

	public void popTopScreen() {
		if (top.transition instanceof ScreenTransition.Reversable) {
			((ScreenTransition.Reversable)top.transition).reverse();
			setInput(top.parentScreen);
			printState();
		}
	}
	
	public void setTransition(ScreenTransition transition) {
		//if (top != null && top.getTransition() != null && !top.getTransition().isFinished()) return;
		top.setTransition(transition);
		if (transition != null) {
		  transition.start(this, top);
		}
	}
	
	public void think(float timestep) {

    top.transformMatrix.idt();
    top.alpha = 1f;
    
	  Screen current = top;
	  while (current != null) {
  	  // Parent screen transform should be this screen's base transform
	    // (The transform before the transition has messed with it)
  	  if (current.parentScreen != null) {
  	    current.parentScreen.transformMatrix.set(current.transformMatrix);
  	    current.parentScreen.alpha = current.alpha;
  	  }
      
      if (current.transition != null) {
        if (current.transition.getTimer().finished()) {
          onFinish(current);
          //printState();
        }

        if (current.transition != null)
          current.transition.thinkInternal(timestep);
      }
      
      current.thinkInternal(timestep);
      
      current = current.parentScreen;
      
	  }
	}
	
	public void printState() {
	  System.out.println("Screen stack");

    Screen current = top;
    while (current != null) {
      System.out.print("  ");
      System.out.println(current);
      current = current.parentScreen;
    }
	}
	
	
	public Screen getTop() {
		return top;
	}

	private void onFinish(Screen screen) {
	  if (screen.transition == null) return;
	  if (screen.transition instanceof ScreenTransition.Reversable) {
	    Reversable r = (ScreenTransition.Reversable)screen.transition;
	    if(!r.getTimer().isForward())
	      top = top.parentScreen;
	  }
	  else {
			top.detatchParent();
		}
	}
	
}
