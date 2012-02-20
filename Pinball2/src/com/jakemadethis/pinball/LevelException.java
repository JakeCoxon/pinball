package com.jakemadethis.pinball;

public class LevelException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4952262026167555193L;
	
	public LevelException(String message) {
		super(message);
	}
	
	public LevelException(String message, Throwable cause) {
		super(message, cause);
	}
	
	/*@Override
	public String toString() {
		return getMessage();
	}*/
	
	

}
