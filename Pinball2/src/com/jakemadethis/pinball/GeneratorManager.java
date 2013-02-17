package com.jakemadethis.pinball;

import java.util.LinkedList;

import com.badlogic.gdx.utils.ObjectMap;

public class GeneratorManager {
	public GeneratorManager() {
		
	}
	
	private LinkedList<String> names = new LinkedList<String>();
	private LinkedList<Class<?>> types = new LinkedList<Class<?>>();
	private LinkedList<AssetGenerator<?>> queue = new LinkedList<AssetGenerator<?>>();
	
	
	private ObjectMap<String, Class<?>> classes = new ObjectMap<String, Class<?>>();
	private ObjectMap<Class<?>, ObjectMap<String, Object>> assets = new ObjectMap<Class<?>, ObjectMap<String,Object>>();
	

	public <T> void load(String name, AssetGenerator<T> generator, Class<T> type) {
		names.add(name);
		types.add(type);
		queue.add(generator);
		classes.put(name, type);
	}
	
	private void next() {
		AssetGenerator<?> poll = queue.poll();
		String name = names.poll();
		Class<?> type = types.poll();
		ObjectMap<String, Object> hsh = assets.get(type);
		if (hsh == null) {
			hsh = new ObjectMap<String, Object>();
			assets.put(type, hsh);
		}
		hsh.put(name, poll.generate());
	}
	
	@SuppressWarnings("unchecked")
	public <T> T get(String name, Class<T> type) {
		return (T) assets.get(type).get(name);
	}
	
	/**
	 * Load one thing
	 * @return Whether it has finished loading all generators
	 */
	public boolean update() {
		if (queue.size() > 0) next();
		return queue.size() == 0;
	}

	public float getProgress() {
		return 1; // TODO: Calculate this
	}

	public void dispose() {
		// TODO Auto-generated method stub
		
	}
	
}
