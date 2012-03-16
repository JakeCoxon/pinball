package com.jakemadethis.pinball.builder;

/**
 * Reads the level name from a node tree
 * @author Jake
 *
 */
public class LevelNameFactory implements BuilderFactory<String> {

	@Override
	public String create(BuilderNode node) {
		if (!node.getNodeName().equals("level"))
			throw new IllegalArgumentException("Invalid node");
		
		String name = FactoryUtil.expected(node.getAttributes(), "name");
		return name;
	}

}
