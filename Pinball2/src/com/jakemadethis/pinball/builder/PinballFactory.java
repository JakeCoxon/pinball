package com.jakemadethis.pinball.builder;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;

import org.xml.sax.Attributes;

import com.badlogic.gdx.physics.box2d.World;
import com.jakemadethis.pinball.BaseModel;
import com.jakemadethis.pinball.Entity;
import com.jakemadethis.pinball.GameModel;
import com.jakemadethis.pinball.Pinball;
import com.jakemadethis.pinball.builder.BuilderFactory;
import com.jakemadethis.pinball.builder.BuilderNode;
import com.jakemadethis.pinball.level.Flipper;
import com.jakemadethis.pinball.level.Level;

/**
 * Implementation of BuilderFactory
 * Creates entities from builder nodes.
 * This is done by reflection, first finding the entity in 
 * com.jakemadethis.pinball.level.<entityName> and then calling the static 
 * fromNode(BaseModel, HashMap) method on that entity
 * 
 * Downside of reflection is no static errors for methods/classes 
 * that don't exist
 * @author Jake
 *
 */
public class PinballFactory implements BuilderFactory<Object> {

	private final static String CONSTRUCT_METHOD = "fromNode";
	private static final String CLASS_PATTERN = "%s.level.%s";
	private BaseModel model;
	
	public PinballFactory(BaseModel model) {
		this.model = model;
	}
	
	public BaseModel getModel() {
		return model;
	}

	@Override
	public Object create(BuilderNode node) {
		
		// Upper case the first letter
		String type = Character.toUpperCase(node.getNodeName().charAt(0)) + node.getNodeName().substring(1);
		
		Object _return = invokeMethod(type, node, model);
		node.setValue(_return);
		
		for (BuilderNode child : node.getChilds()) {
			create(child);
		}
		
		return _return;
	}

	private Object invokeMethod(String type, BuilderNode node, BaseModel model) {
		Method method = null;
		try {
			// Find the base package of Pinball
			// This should be com.jakemadethis.pinball
			// but I don't want to hard code it
			String basePackage = Pinball.class.getPackage().getName();
			
			String className = String.format(CLASS_PATTERN, basePackage, type);
			Class<?> c = Class.forName(className);
			method = c.getMethod(CONSTRUCT_METHOD, BaseModel.class, BuilderNode.class);
			
			return method.invoke(null, model, node);
			
		} catch (NoSuchMethodException e) {
			System.err.println("Pinball Factory error");
			e.printStackTrace();	
		} catch (IllegalArgumentException e) {
			System.err.println("Pinball Factory error");
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			System.err.println("Pinball Factory error");
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			System.err.println("Pinball Factory error");
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			System.err.println("Pinball Factory error");
			e.printStackTrace();
		}
		return null;
	}

	
}
