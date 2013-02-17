package com.jakemadethis.pinball.builder;

import static org.junit.Assert.*;

import java.io.ByteArrayInputStream;
import java.io.UnsupportedEncodingException;

import org.junit.Test;

public class TestLevelLoading {
	
	@Test
	public void test() throws UnsupportedEncodingException {
		String str = 
		"<level name=\"Basic level\" size=\"480,1000\">"+
		  "<wall>"+
		    "<point at=\"0,0 bottomleft\" />"+
		    "<arc at=\"0,0 topleft\" radius=\"100\" />"+
		    "<arc at=\"0,0 topright\" radius=\"100\" />"+
		    "<point at=\"0,0 bottomright\" />"+
		  "</wall>"+
		"</level>";
		
		XMLBuilder builder = XMLBuilder.fromStream(new ByteArrayInputStream(str.getBytes("UTF-16")));
		builder.create(new BuilderFactory<String>() {

			@Override
			public String create(BuilderNode node) {
				System.out.println(node.toString());
				for (BuilderNode ch : node.getChilds()) {
					create(ch);
				}
				return null;
			}
		});
	}
	
}
