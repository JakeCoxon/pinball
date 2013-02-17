package com.jakemadethis.pinball.io;

import org.junit.Test;

import com.jakemadethis.pinball.EventHandler.EventListener;
import com.jakemadethis.pinball.io.Slot.EventArgs;


public class EventsTest {
	class SuccessException extends RuntimeException {
	}

	private EventListener<EventArgs> success;
	private EventListener<EventArgs> successArgs;
	
	public EventsTest() {
		success = new EventListener<Slot.EventArgs>() {
			@Override public void invoke(Object sender, EventArgs args) {
				if (args.getSlotName().equals("test"))
					throw new SuccessException();
			}
		};
		successArgs = new EventListener<Slot.EventArgs>() {
			@Override public void invoke(Object sender, EventArgs args) {
				System.out.println(args.getArgs().length);
				if (args.getSlotName().equals("test") && args.getArgs()[0].equals("arg"))
					throw new SuccessException();
			}
		};
	}
	
	
	@Test(expected=SuccessException.class)
	public void testBasicEvent() {
		
		SlotHandler inputHandler = new SlotHandler(success, "test");
		SignalHandler outputHandler = new SignalHandler("onInvokeTest");
		
		IOManager man = new IOManager();
		man.add("testReceive", inputHandler, null);
		man.add("testInvoke", null, outputHandler);
		
		man.addEvent("testInvoke", "onInvokeTest", "testReceive", "test");

		outputHandler.invoke("onInvokeTest");
		
	}
	
	@Test(expected=SuccessException.class)
	public void testArgsEvent() {
		
		SlotHandler inputHandler = new SlotHandler(successArgs, "test");
		SignalHandler outputHandler = new SignalHandler("onInvokeTest");
		
		IOManager man = new IOManager();
		man.add("testReceive", inputHandler, null);
		man.add("testInvoke", null, outputHandler);
		
		man.addEvent("testInvoke", "onInvokeTest", "testReceive", "test,arg");

		outputHandler.invoke("onInvokeTest");
		
	}
}
