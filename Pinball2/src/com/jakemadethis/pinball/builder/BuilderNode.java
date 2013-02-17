package com.jakemadethis.pinball.builder;

import java.util.HashMap;
import java.util.LinkedList;

import org.xml.sax.Attributes;

import com.jakemadethis.pinball.LevelException;

public class BuilderNode {
	private BuilderNode parent;
	private String type;
	private HashMap<String, String> atts;
	private LinkedList<BuilderNode> childs;
	private Object value = null;

	public BuilderNode(BuilderNode parent, String type, HashMap<String, String> attsMap) {
		this.parent = parent;
		this.type = type;
		this.atts = attsMap;
		this.childs = new LinkedList<BuilderNode>();
	}
	
	public <R> void createChildren(BuilderFactory<R> factory) {
		for (BuilderNode child : childs) {
			//child.create(factory);
			factory.create(child);
		}
	}
	
	public Object getValue() {
		return value;
	}
	
	public HashMap<String, String> getAttributes() {
		return atts;
	}
	
	public BuilderNode getParent() {
		return parent;
	}
	public void setParent(BuilderNode parent) {
		this.parent = parent;
	}
	public String getNodeName() {
		return type;
	}
	public void addChild(BuilderNode node) {
		childs.add(node);
	}
	public LinkedList<BuilderNode> getChilds() {
		return childs;
	}

	public void setValue(Object value) {
		this.value = value;
	}
	
	
	/**
	 * Returns the value of the attribute if it exists, throws an exception
	 * otherwise
	 */
	public String attExpected(String key) {
		String s = atts.get(key);
		if (s == null) throw new LevelException("Attribute '"+key+"' expected");
		return s;
	}
	
	/**
	 * Returns the value of an attribute if it exists or the default value if not
	 * @param atts
	 * @param key
	 * @param defaultValue
	 * @return
	 */
	public String attOptional(String key, String defaultValue) {
		String s = atts.get(key);
		if (s == null) s = defaultValue;
		return s;
	}
	
	
	
	@Override
	public String toString() {
		return "<BuilderNode type="+getNodeName()+">";
	}
	
}
