package com.jakemadethis.pinball.io;

import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;

public class IOManagerTest {
	
	IOManager manager;
	SlotHandler[] handlers = new SlotHandler[10];
	
	@Before
	public void setUp() {
		manager = new IOManager();
		for (int i = 0; i < handlers.length; i++)
			handlers[i] = new SlotHandler(null);
	}
	
	@Test
	public void testNumbered() {

		manager.add("test", handlers[0], null);
		assertEquals(1, manager.getInputGroup("test").size());

		manager.add("test", handlers[1], null);
		ArrayList<SlotHandler> inputGroup = manager.getInputGroup("test");
		assertEquals(handlers[0], inputGroup.get(0));
		assertEquals(handlers[1], inputGroup.get(1));
		assertEquals(2, inputGroup.size());
		
		//manager.debugPrint();
		
	}
	
	
	@Test
	public void testUnordered() {
		manager.add("test-1", handlers[1], null);
		manager.add("test-4", handlers[4], null);
		manager.add("test-2", handlers[2], null);
		
		// size should be 5 but [0] and [3] will be null
		
		ArrayList<SlotHandler> inputGroup = manager.getInputGroup("test");

		assertEquals(5, inputGroup.size());

		assertEquals(null, inputGroup.get(0));
		assertEquals(handlers[1], inputGroup.get(1));
		assertEquals(handlers[2], inputGroup.get(2));
		assertEquals(null, inputGroup.get(3));
		assertEquals(handlers[4], inputGroup.get(4));
	}
	
	@Test
	public void testAddEvent() {
		manager.add("test-1", handlers[1], null);
		manager.add("test-4", handlers[4], null);
		manager.add("test-2", handlers[2], null);
		
		//manager.addEvent("test", eventName, targetName, action)
	}
}
