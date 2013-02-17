package com.jakemadethis.pinball;

public abstract class ScreenTransition {
	public static abstract class Reversable extends ScreenTransition {
	  
	  protected PingPong timer = new PingPong();
    public PingPong getTimer() {
	    return timer;
	  }
		public void reverse() {
		  timer.backward(0.5f);
		}
		@Override
		public void start(ScreenStack screenStack, Screen newscreen) {
		  super.start(screenStack, newscreen);
		  timer.start(0.5f);
		}

    
	}
	
	public static class SlideForward extends ScreenTransition {
		
		@Override
		public void thinkInternal(float delta) {
			float w = parentScreen.width;
			
			float x = Interpolator.easeOutQuad(timer, w + 10f, 0f);
			topScreen.transformMatrix.translate(x, 0, 0);
			
			parentScreen.transformMatrix.set(topScreen.transformMatrix);
			parentScreen.transformMatrix.translate(- w - 10f, 0, Interpolator.easeOutQuad(timer, 0, 100f));
			
			parentScreen.alpha = timer.value(1f, 0f);
			
		}
		
	};
	public static class SlideBackward extends ScreenTransition {
    
    @Override
    public void thinkInternal(float delta) {
      float w = parentScreen.width;
      
      float x = Interpolator.easeOutQuad(timer, - w - 10f, 0f);
      topScreen.transformMatrix.translate(x, 0, 0);
      parentScreen.transformMatrix.set(topScreen.transformMatrix);
      parentScreen.transformMatrix.translate(w + 10f, 0, Interpolator.easeOutQuad(timer, 0, 100f));
      
      parentScreen.alpha = timer.value(1f, 0f);
      
    }
    
  };
	
	public static class ZoomIn extends Reversable {
		
		@Override
		public void thinkInternal(float delta) {
			
			//float a = timer.value();
			
			topScreen.transformMatrix.translate(
					Interpolator.easeOutQuad(timer, 50f, 0f), 
					0, 
					Interpolator.easeOutQuad(timer, -20f, 0));
			
			topScreen.alpha *= timer.value(0f, 0.95f);
			parentScreen.transformMatrix.set(topScreen.transformMatrix);
			parentScreen.transformMatrix.translate(-50f, 0, 20f);
      parentScreen.alpha *= timer.value(1f, 0.8f);
			
		}

		
	}

	private ScreenStack stack;
	protected Screen topScreen;
	protected Screen parentScreen;
	protected Timer timer = new Timer();
	
	public void start(ScreenStack screenStack, Screen newscreen) {
		stack = screenStack;
		this.topScreen = newscreen;
		this.parentScreen = newscreen.parentScreen;
		timer.start(0.5f);
	}
	public abstract void thinkInternal(float delta);
	
	public Timer getTimer() {
    return timer;
  }
	
	
	public void debug() {}
}
