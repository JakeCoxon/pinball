package com.jakemadethis.pinball;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Stack;

import org.xml.sax.Attributes;
import org.xml.sax.ContentHandler;
import org.xml.sax.ErrorHandler;
import org.xml.sax.InputSource;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.XMLReaderFactory;

import com.jakemadethis.pinball.entities.IElement;
import com.jakemadethis.pinball.entities.Wall;
import com.jakemadethis.pinball.entities.Flipper;




public class Builder implements ContentHandler, ErrorHandler
{

	private static String expected(Attributes atts, String key) {
		String s = atts.getValue(key);
		if (s == null) throw new RuntimeException(key+" expected");
		return s;
	}
	private static String optional(Attributes atts, String key, String def) {
		String s = atts.getValue(key);
		if (s == null) s = def;
		return s;
	}
	private static float[] toPosition(String str) {
		String[] strs = str.split(",");
		return new float[] { Float.valueOf(strs[0]), Float.valueOf(strs[1]) };
	}
	private static float[] toFloatList(String str) {
		String[] strs = str.split(",");
		float[] floats = new float[strs.length];
		for (int i = 0; i < strs.length; i++) floats[i] = Float.valueOf(strs[i]);
		return floats;
	}
	private static float realFloat(String str) {
		return Float.intBitsToFloat(Integer.parseInt(str, 16));
	}
	private static float[] toFloatListReal(String str) {
		String[] strs = str.split(",");
		float[] floats = new float[strs.length];
		for (int i = 0; i < strs.length; i++) 
			floats[i] = realFloat(strs[i]);
		return floats;
	}
	private static String[] toStringList(String str) {
		return str.split(",");
	}
	

	
	private interface IBuilder {
		public void create(GameModel model);
	}
	
	@SuppressWarnings("unused")
	private static class LevelBuilder implements IBuilder {
		private LinkedList<IBuilder> builders;
		public LevelBuilder(String className, Object parent, Attributes atts) {
			builders = new LinkedList<Builder.IBuilder>();
		}
		
		public void add(IBuilder builder) {
			builders.add(builder);
		}
		
		@Override
		public void create(GameModel model) {
			for (IBuilder builder : builders) {
				builder.create(model);
			}
		}
		
	}
	
	@SuppressWarnings("unused")
	private static class WallBuilder implements IBuilder {
		private float[] path;
		private Float restitution;
		private String name;

		public WallBuilder(String className, Object parent, Attributes atts) {

			String pathVal;
			if ((pathVal = atts.getValue("realPath")) != null) {
				path = toFloatListReal(pathVal);
			}
			else if ((pathVal = atts.getValue("path")) != null) {
				path = toFloatList(pathVal);
			}
			else 
				throw new RuntimeException("path expected");
			//from = toPosition(expected(atts, "from"));
			//to = toPosition(expected(atts, "to"));
			restitution = Float.valueOf(optional(atts, "restitution", "0.5"));
			name = optional(atts, "name", "");
			
			((LevelBuilder)parent).add(this);
		}

		@Override
		public void create(GameModel model) {
			Entity entity = model.addWallPath(path, restitution);
			model.setName(name, entity);
		}
	}
	
	@SuppressWarnings("unused")
	private static class BumperBuilder implements IBuilder {
		private float[] at;
		private float radius;
		private String name;

		public BumperBuilder(String className, Object parent, Attributes atts) {
			at = toPosition(expected(atts, "at"));
			radius = Float.valueOf(expected(atts, "radius"));
			name = optional(atts, "name", "");

			((LevelBuilder)parent).add(this);
		}

		@Override
		public void create(GameModel model) {
			Entity entity = model.addBumper(at[0], at[1], radius);
			model.setName(name, entity);
		}
	}
	
	@SuppressWarnings("unused")
	private static class FlipperBuilder implements IBuilder {
		private Flipper.Type type;
		private Float length;
		private float[] at;
		private String name;

		public FlipperBuilder(String className, Object parent, Attributes atts) {
			at = toPosition(expected(atts, "at"));
			length = Float.valueOf(expected(atts, "length"));
			name = optional(atts, "name", "");
			
			if (className.equals("flipperLeft"))
				type = Flipper.Type.LEFT;
			else if (className.equals("flipperRight"))
				type = Flipper.Type.RIGHT;
		}

		@Override
		public void create(GameModel model) {
			Entity entity = model.addFlipper(at[0], at[1], length, type);
			model.setName(name, entity);
		}
	}
	
	@SuppressWarnings("unused")
	private static class EventBuilder implements IBuilder {
		private String _for;
		private String when;
		private String target;
		private String action;
		private float wait;

		// <event for="bumpers*" when="collision" target="#self" action="disable" wait="1" />
		public EventBuilder(String className, Object parent, Attributes atts) {
			_for = expected(atts, "for");
			when = expected(atts, "when");
			target = expected(atts, "target");
			action = expected(atts, "action");
			wait = Float.valueOf(optional(atts, "wait", "0"));
		}

		@Override
		public void create(GameModel model) {
			
		}
	}
   

	private Stack<Object> stack;
	private HashMap<String, Class<?>> types;
	private IBuilder topBuilder;
    
    private IBuilder createBuilder(String className, Object parent, Attributes atts) {
    	System.out.println("Creating "+className);
    	try {
    		Class<?> cla = types.get(className);
    		if (cla == null) throw new RuntimeException("No such class "+className);
    		Constructor<?> co = cla.getConstructor(String.class, Object.class, Attributes.class);
    		IBuilder a = (IBuilder) co.newInstance(className, parent, atts);
    		return a;
    	} catch (Exception ex) {
    		throw new RuntimeException("Malformed XML", ex);
    	}
    }
	
    
	/**
	 * Creates a world from an XML file
	 * @param file The filename
	 */
    private Builder(GameModel model, InputSource source) {
    	types = new HashMap<String, Class<?>>();
    	types.put("level", LevelBuilder.class);
    	types.put("wall", WallBuilder.class);
    	types.put("bumper", BumperBuilder.class);
    	types.put("flipperLeft", FlipperBuilder.class);
    	types.put("flipperRight", FlipperBuilder.class);
    	
    	
    	stack = new Stack<Object>();
    	
    	
    	XMLReader xr;
		try {
			//reader = new FileReader(file);
			xr = XMLReaderFactory.createXMLReader();
	    	xr.setContentHandler(this);
	    	xr.setErrorHandler(this);
	    	
	    	xr.parse(source);
	    	
	    	topBuilder.create(model);
	    	
		} catch (SAXException e) {
			System.out.println("Malformed XML file");
			e.printStackTrace();
		} catch (ClassCastException ex) {
			System.out.println("Malformed XML file");
			ex.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
    	
    }
    
    
    
    public static void fromFile(GameModel model, String fileName) {
    	try {
			new Builder(model, new InputSource(new FileReader(fileName)));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
    }
    public static void fromStream(GameModel model, InputStream stream) {
    	new Builder(model, new InputSource(stream));
    }

    

	@Override
	public void startElement(String uri, String localName, String qName,
			Attributes atts) throws SAXException {
		
		
		Object parent = null;
		if (stack.size() > 0) parent = stack.peek();
		IBuilder obj = createBuilder(localName, parent, atts);

		if (parent == null) { // top
			topBuilder = obj;
		}
		stack.push(obj);
		
	}


	@Override
	public void endElement(String uri, String localName, String qName)
			throws SAXException {
		
		if (localName.equals("level")) return;
		
		stack.pop();
	}
	
	@Override
	public void error(SAXParseException arg0) throws SAXException { }

	@Override
	public void fatalError(SAXParseException arg0) throws SAXException { }

	@Override
	public void warning(SAXParseException arg0) throws SAXException { }

	@Override
	public void characters(char[] arg0, int arg1, int arg2) throws SAXException { }

	@Override
	public void endDocument() throws SAXException { }

	@Override
	public void endPrefixMapping(String arg0) throws SAXException { }

	@Override
	public void ignorableWhitespace(char[] arg0, int arg1, int arg2)
			throws SAXException { }

	@Override
	public void processingInstruction(String arg0, String arg1)
			throws SAXException { }

	@Override
	public void setDocumentLocator(Locator arg0) { }

	@Override
	public void skippedEntity(String arg0) throws SAXException { }

	@Override
	public void startDocument() throws SAXException { }
	
	@Override
	public void startPrefixMapping(String arg0, String arg1)
			throws SAXException { }
}
