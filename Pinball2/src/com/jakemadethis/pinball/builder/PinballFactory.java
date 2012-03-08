package com.jakemadethis.pinball.builder;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;

import com.jakemadethis.pinball.BaseModel;
import com.jakemadethis.pinball.LevelException;
import com.jakemadethis.pinball.level.Ball;
import com.jakemadethis.pinball.level.Bumper;
import com.jakemadethis.pinball.level.Counter;
import com.jakemadethis.pinball.level.Event;
import com.jakemadethis.pinball.level.Flipper;
import com.jakemadethis.pinball.level.Frame;
import com.jakemadethis.pinball.level.Kicker;
import com.jakemadethis.pinball.level.Level;
import com.jakemadethis.pinball.level.Light;
import com.jakemadethis.pinball.level.Points;
import com.jakemadethis.pinball.level.RoundedWall;
import com.jakemadethis.pinball.level.Sensor;
import com.jakemadethis.pinball.level.Wall;
import com.jakemadethis.pinball.level.WallArc;

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
	
	private final static Class<?>[] types = new Class<?>[] {
		Ball.class, Bumper.class, Counter.class, Event.class,
		Flipper.class, Frame.class, Kicker.class, Level.class,
		Light.class, Points.class, RoundedWall.class, Sensor.class, 
		Wall.class, WallArc.class
	};
	
	
	private static HashMap<String, Method> factory;
	static {
		factory = new HashMap<String, Method>();
		for (int i = 0; i < types.length; i++) {
			Class<?> _class = types[i];
			String name = _class.getSimpleName();
			String lname = Character.toLowerCase(name.charAt(0)) + name.substring(1);
			
			Method method = null;
			try {
				method = _class.getMethod(CONSTRUCT_METHOD, 
						BaseModel.class, BuilderNode.class);
			} catch (SecurityException e) {
				e.printStackTrace();
			} catch (NoSuchMethodException e) {
				System.err.println("Class '"+name+"' doesn't have a construct method '"+CONSTRUCT_METHOD);
			}
			
			factory.put(lname, method);
		}
	}
	
	private final BaseModel model;
	
	public PinballFactory(BaseModel model) {
		this.model = model;
	}
	
	public BaseModel getModel() {
		return model;
	}

	@Override
	public Object create(BuilderNode node) {
		
		String type = node.getNodeName();
		
		Object _return = invokeMethod(type, node, model);
		node.setValue(_return);
		
		for (BuilderNode child : node.getChilds()) {
			create(child);
		}
		
		return _return;
	}

	private Object invokeMethod(String type, BuilderNode node, BaseModel model) {
		Method method = null;
		
		try{
				try {
			
				method = factory.get(type);
				if (method == null) {
					System.err.println("'"+type+"' does not have a valid factory.");
					return null;
				}
				return method.invoke(null, model, node);
				
			} catch (InvocationTargetException e) {
				if (e.getCause() instanceof Exception)
					throw (Exception) e.getCause();
				throw new RuntimeException(e.getCause());
			}
		} catch (Exception e) {
			throw new LevelException("Node '"+node.getNodeName()+"' could not be created", e);
		}
	}

	
}
