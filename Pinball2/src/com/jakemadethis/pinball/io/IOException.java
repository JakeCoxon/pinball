package com.jakemadethis.pinball.io;

public class IOException extends RuntimeException {
	/**
	 * 
	 */
	private static final long serialVersionUID = -7629846426187630598L;

	public IOException(String message) {
		super(message);
	}
	public IOException(String message, Throwable cause) {
		super(message, cause);
	}
}
