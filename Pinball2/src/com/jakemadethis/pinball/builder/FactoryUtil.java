package com.jakemadethis.pinball.builder;

import static com.jakemadethis.pinball.builder.FactoryUtil.expected;
import static com.jakemadethis.pinball.builder.FactoryUtil.optional;
import static com.jakemadethis.pinball.builder.FactoryUtil.toFloatList;

import java.util.HashMap;

import org.xml.sax.Attributes;

import com.jakemadethis.pinball.Attachable;
import com.jakemadethis.pinball.Entity;

public class FactoryUtil {
	
	/**
	 * Returns the value of the attribute if it exists, throws an exception
	 * otherwise
	 */
	public static String expected(HashMap<String, String> atts, String key) {
		String s = atts.get(key);
		if (s == null) throw new RuntimeException(key+" expected");
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
	public static float[] toPosition(String str) {
		String[] strs = str.split(",");
		return new float[] { Float.valueOf(strs[0]), Float.valueOf(strs[1]) };
	}
	public static float[] toFloatList(String str) {
		String[] strs = str.split(",");
		float[] floats = new float[strs.length];
		for (int i = 0; i < strs.length; i++) floats[i] = Float.valueOf(strs[i]);
		return floats;
	}
	public static float realFloat(String str) {
		return Float.intBitsToFloat(Integer.parseInt(str, 16));
	}
	public static float[] toFloatListReal(String str) {
		String[] strs = str.split(",");
		float[] floats = new float[strs.length];
		for (int i = 0; i < strs.length; i++) 
			floats[i] = realFloat(strs[i]);
		return floats;
	}
	
	public static float[] getAbsolutePosition(Object parent, HashMap<String, String> atts) {
		if (parent == null) 
			throw new FactoryException("Parent is null");
		if (!(parent instanceof Attachable))
			throw new FactoryException("Parent node is not attachable ("+parent.getClass().getSimpleName()+")");
		
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
}
