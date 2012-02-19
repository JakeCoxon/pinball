package com.jakemadethis.pinball.builder;

import java.util.HashMap;
import java.util.LinkedList;

import org.xml.sax.Attributes;

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
	
}
