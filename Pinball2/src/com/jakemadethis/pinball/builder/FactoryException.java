package com.jakemadethis.pinball.builder;

public class FactoryException extends RuntimeException {
	/**
	 * 
	 */
	private static final long serialVersionUID = 5217181105165276150L;
	public FactoryException(String message) {
		super(message);
	}
	public FactoryException() {
		super();
	}
}
