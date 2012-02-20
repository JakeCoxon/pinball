package com.jakemadethis.pinball.io;

import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;

public class PatternTest {
	
	private IOManager manager;
	InputHandler[] handlers = new InputHandler[10];
	
	@Before
	public void setUp() {
		for (int i = 0; i < handlers.length; i++)
			handlers[i] = new InputHandler(null);
	
		manager = new IOManager();
	}
	
	@Test
	public void testGroupPattern() {
		manager.add("test", handlers[0], null);
		manager.add("test", handlers[1], null);
		ArrayList<InputHandler> match = 
			Pattern.match(manager.getAllInputs(), "test");
		
		assertEquals(2, match.size());
	}
	
	@Test
	public void testHashPattern() {
		manager.add("test", handlers[0], null);
		manager.add("test", handlers[1], null);
		ArrayList<InputHandler> match = 
			Pattern.match(manager.getAllInputs(), "test-#");
		
		assertEquals(2, match.size());
	}
	
	@Test
	public void testUnorderedPattern() {

		manager.add("test-1", handlers[1], null);
		manager.add("test-4", handlers[4], null);
		manager.add("test-2", handlers[2], null);
		
		ArrayList<InputHandler> match = 
			Pattern.match(manager.getAllInputs(), "test");
		
		assertEquals(5, match.size());
	}
	

	@Test
	public void testInvalid() {

		manager.add("test", handlers[0], null);
		assertNull(Pattern.match(manager.getAllInputs(), "invalid"));
	}
	
	@Test
	public void testSinglePattern() {
		manager.add("test", handlers[0], null);
		manager.add("test", handlers[1], null);
		ArrayList<InputHandler> match = 
			Pattern.match(manager.getAllInputs(), "test-0");
		
		assertEquals(1, match.size());
	}
}
