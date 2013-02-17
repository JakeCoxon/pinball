package com.jakemadethis.pinball.builder;

import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.jakemadethis.pinball.Attachable;
import com.jakemadethis.pinball.LevelException;

public class FactoryUtil {

	private static Pattern pattern = 
			Pattern.compile("^(\\-?[0-9]+(?:\\.[0-9]+)?),(\\-?[0-9]+(?:\\.[0-9]+)?)(?: (.+))?$");
	/**
	 * Returns the value of the attribute if it exists, throws an exception
	 * otherwise
	 */
	public static String expected(HashMap<String, String> atts, String key) {
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
	public static String optional(HashMap<String, String> atts, String key, String defaultValue) {
		String s = atts.get(key);
		if (s == null) s = defaultValue;
		return s;
	}
	public static float[] toPosition(String str, Attachable parent, float scale) {
		Matcher matcher = pattern.matcher(str);
		if (matcher.find()) {
			float x = toLength(matcher.group(1), scale), y = toLength(matcher.group(2), scale);
			int corner = matcher.group(3) == null ? 
					(Attachable.TOP | Attachable.LEFT) : Attachable.getCornerFromString(matcher.group(3));
					
			return parent.getAbsolutePoint(x, y, corner);
		}
		return null;
	}
	public static float[] toSize(String str, float scale) {
		String[] strs = str.split(",");
		return new float[] { toLength(strs[0], scale), toLength(strs[1], scale) };
	}
	
	public static float[] toFloatList(String str) {
		String[] strs = str.split(",");
		float[] floats = new float[strs.length];
		for (int i = 0; i < strs.length; i++) floats[i] = Float.valueOf(strs[i]);
		return floats;
	}
	public static float toLength(String str, float scale) {
		return Float.valueOf(str) / scale;
	}
	
	
	public static float[] getAbsolutePosition(Object parent, HashMap<String, String> atts) {
		if (parent == null) 
			throw new LevelException("Parent is null");
		if (!(parent instanceof Attachable))
			throw new LevelException("Parent node is not attachable ("+parent.getClass().getSimpleName()+")");
		
		Attachable attachParent = (Attachable) parent;
		
		float[] at = toFloatList(expected(atts, "at"));
		int relativeTo = Attachable.getCornerFromString(optional(atts, "relativeTo", ""));
		
		float[] abs = attachParent.getAbsolutePoint(at[0], at[1], relativeTo);
		
		return abs;
	}
	
	/**
	 * Splits a string into an array of strings by commas
	 * @param str
	 * @return
	 */
	public static String[] toStringList(String str) {
		return str.split(",");
	}

	public static int toInteger(String str) {
		return Integer.valueOf(str);
	}
}
