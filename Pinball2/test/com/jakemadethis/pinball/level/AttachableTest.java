package com.jakemadethis.pinball.level;

import static org.junit.Assert.*;

import org.junit.Test;

import com.jakemadethis.pinball.Attachable;


public class AttachableTest {
	
	@Test
	public void testRelativeAttachable() {
		assertEquals(Attachable.TOP | Attachable.LEFT, Attachable.getCornerFromString(""));
		
		assertEquals(Attachable.TOP | Attachable.LEFT, Attachable.getCornerFromString("topleft"));
		assertEquals(Attachable.TOP | Attachable.RIGHT, Attachable.getCornerFromString("topright"));
		assertEquals(Attachable.TOP | Attachable.CENTERX, Attachable.getCornerFromString("topcenter"));

		assertEquals(Attachable.CENTERY | Attachable.LEFT, Attachable.getCornerFromString("centerleft"));
		assertEquals(Attachable.CENTERY | Attachable.RIGHT, Attachable.getCornerFromString("centerright"));
		assertEquals(Attachable.CENTERY | Attachable.CENTERX, Attachable.getCornerFromString("centercenter"));

		assertEquals(Attachable.BOTTOM | Attachable.LEFT, Attachable.getCornerFromString("bottomleft"));
		assertEquals(Attachable.BOTTOM | Attachable.RIGHT, Attachable.getCornerFromString("bottomright"));
		assertEquals(Attachable.BOTTOM | Attachable.CENTERX, Attachable.getCornerFromString("bottomcenter"));
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void testBadString() {
		Attachable.getCornerFromString("aijsduiasd");
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void testGoodThenBadString() {
		Attachable.getCornerFromString("topleftbad");
	}
}
