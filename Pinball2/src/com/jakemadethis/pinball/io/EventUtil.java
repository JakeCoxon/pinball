package com.jakemadethis.pinball.io;

public class EventUtil {
	public static float expectedFloat(String[] args, int id) {
		if (args.length < id) {
			throw new IOException("Expected arg "+(id+1)+" args but recieved "+args.length);
		}
		try {
			return Float.valueOf(args[id]);
		}
		catch(NumberFormatException ex) {
			throw new IOException("Expected float at arg #"+id+" but got "+args[id]);
		}
	}
	public static float optionalFloat(String[] args, int id, float defaultValue) {
		try {
			return expectedFloat(args, id);
		} catch(IOException ex) {
			return defaultValue;
		}
	}
}
