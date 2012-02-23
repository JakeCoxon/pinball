package com.jakemadethis.pinball.io;

import org.junit.Test;

import com.jakemadethis.pinball.EventHandler.EventListener;
import com.jakemadethis.pinball.io.Input.EventArgs;


public class EventsTest {
	class SuccessException extends RuntimeException {
	}

	private EventListener<EventArgs> success;
	private EventListener<EventArgs> successArgs;
	
	public EventsTest() {
		success = new EventListener<Input.EventArgs>() {
			@Override public void invoke(Object sender, EventArgs args) {
				if (args.getInputName().equals("test"))
					throw new SuccessException();
			}
		};
		successArgs = new EventListener<Input.EventArgs>() {
			@Override public void invoke(Object sender, EventArgs args) {
				System.out.println(args.getArgs().length);
				if (args.getInputName().equals("test") && args.getArgs()[0].equals("arg"))
					throw new SuccessException();
			}
		};
	}
	
	
	@Test(expected=SuccessException.class)
	public void testBasicEvent() {
		
		InputHandler inputHandler = new InputHandler(success, "test");
		OutputHandler outputHandler = new OutputHandler("onInvokeTest");
		
		IOManager man = new IOManager();
		man.add("testReceive", inputHandler, null);
		man.add("testInvoke", null, outputHandler);
		
		man.addEvent("testInvoke", "onInvokeTest", "testReceive", "test");

		outputHandler.invoke("onInvokeTest");
		
	}
	
	@Test(expected=SuccessException.class)
	public void testArgsEvent() {
		
		InputHandler inputHandler = new InputHandler(successArgs, "test");
		OutputHandler outputHandler = new OutputHandler("onInvokeTest");
		
		IOManager man = new IOManager();
		man.add("testReceive", inputHandler, null);
		man.add("testInvoke", null, outputHandler);
		
		man.addEvent("testInvoke", "onInvokeTest", "testReceive", "test,arg");

		outputHandler.invoke("onInvokeTest");
		
	}
}
