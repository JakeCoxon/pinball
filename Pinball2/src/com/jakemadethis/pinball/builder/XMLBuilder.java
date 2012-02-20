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

import com.jakemadethis.pinball.LevelException;
import com.jakemadethis.pinball.level.Flipper;



/**
 * Builds objects from an XML file using the SAX parser
 * @author Jake
 *
 */
public class XMLBuilder implements ContentHandler, ErrorHandler, IBuilder
{
  

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
			throw new LevelException("Malformed XML file", e);
		} catch (ClassCastException e) {
			throw new LevelException("Malformed XML file", e);
		} catch (IOException e) {
			throw new LevelException("Could not load file", e);
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
