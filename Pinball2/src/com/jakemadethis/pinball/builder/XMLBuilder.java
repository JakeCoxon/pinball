package com.jakemadethis.pinball.builder;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Constructor;
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

import com.jakemadethis.pinball.level.Flipper;



/**
 * Builds objects from an XML file using the SAX parser
 * @author Jake
 *
 */
public class XMLBuilder implements ContentHandler, ErrorHandler, IBuilder
{


	

	/*
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
	}*/
   

	private Stack<BuilderNode> stack;
	private BuilderNode topNode;
    
	
    
	/*
	 * Constructs an XMLBuilder instance
	 */
    private XMLBuilder(InputSource source) {
    	
    	stack = new Stack<BuilderNode>();
    	
    	XMLReader xr;
			try {
				//reader = new FileReader(file);
				xr = XMLReaderFactory.createXMLReader();
				xr.setContentHandler(this);
				xr.setErrorHandler(this);
				
				xr.parse(source);
				
				
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
    
    /* (non-Javadoc)
     * @see com.jakemadethis.pinball.builder.IBuilder#createAll(com.jakemadethis.pinball.builder.BuilderFactory, A)
     */
    @Override
    public <R> R create(BuilderFactory<R> factory) {
    	return factory.create(topNode);
    }
    
    
    /**
     * Creates a new Builder from a filename
     * @param fileName
     * @return
     */
    public static XMLBuilder fromFile(String fileName) {
    	try {
			return new XMLBuilder(new InputSource(new FileReader(fileName)));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		return null;
    }
    
    /**
     * Creates a new XMLBuilder from an input stream
     * @param stream
     * @return
     */
    public static XMLBuilder fromStream(InputStream stream) {
    	return new XMLBuilder(new InputSource(stream));
    }

    

	@Override
	public void startElement(String uri, String localName, String qName,
			Attributes atts) throws SAXException {
		
		// Parents are stored in a stack
		BuilderNode parent = null;
		if (stack.size() > 0) parent = stack.peek();
		
		// Construct a hashmap from the Attributes instance
		// 1. The Attributes instance is reused so I can't use this
		// 2. This means I can switch from SAX easily if I need to
		HashMap<String, String> attsMap = new HashMap<String, String>();
		for (int i = 0; i < atts.getLength(); i++) {
			attsMap.put(atts.getLocalName(i), atts.getValue(i));
		}
		
		// Create node
		BuilderNode node = new BuilderNode(parent, localName, attsMap);
		
		// Add to parent
		if (parent != null)
			parent.addChild(node);
		else 
			topNode = node;
		
		// Add to stack
		stack.push(node);
		
	}


	@Override
	public void endElement(String uri, String localName, String qName)
			throws SAXException {
		
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
