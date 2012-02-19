package com.jakemadethis.pinball.io;

import java.util.ArrayList;
import java.util.HashMap;

public class Pattern {

	public static Integer matchID(String fullName) {
		int pos;
		if ((pos = fullName.indexOf('-')) == -1) return null;
		String id = fullName.substring(pos+1);
		return Integer.valueOf(id);
	}
	
	public static String matchName(String fullName) {
		int pos;
		if ((pos = fullName.indexOf('-')) == -1) return fullName;
		return fullName.substring(0, pos);
	}
	public static <T> ArrayList<T> match(HashMap<String, ArrayList<T>> map, String fullName) {
		
		String namePart;
		String varPart;
		int dash;
		
		// Pattern looks like 'entity'
		if ((dash = fullName.indexOf('-')) == -1) 
			return map.get(fullName);
		
		namePart = fullName.substring(0, dash);
		varPart = fullName.substring(dash+1);
		
		ArrayList<T> collection = map.get(namePart);
		if (collection == null) return null;
		
		// Pattern looks like 'entity-#'
		if (varPart.length() == 1 && varPart.charAt(0) == '#')
			return collection;
		
		// Pattern looks like 'entity-3'
		int id;
		try {
			id = Integer.valueOf(varPart);
			T object;
			if ((object = collection.get(id)) == null) return null;
			
			ArrayList<T> newList = new ArrayList<T>();
			newList.add(object);
			return newList;
		} catch (NumberFormatException ex) {
			throw new RuntimeException(ex);
		}
	}
}
