package com.jakemadethis.pinball;

import java.util.HashMap;
import java.util.LinkedList;

import com.badlogic.gdx.utils.Logger;

public class AssetLoader {
	static class Task<T> {
		public final String name;
		public final AssetLoadingTask<T> loader;
		public final Class<?> type;
		public Task(String name, Class<?> type, AssetLoadingTask<T> loader) {
			this.name = name;
			this.type = type;
			this.loader = loader;
		}
	}

	public interface OnLoadedListener {
		void onLoaded();
	}
	
	
	private final LinkedList<Task<?>> queue = new LinkedList<Task<?>>();
	private final HashMap<Class<?>, HashMap<String, Object>> assets = new HashMap<Class<?>, HashMap<String,Object>>();
	
	protected final Logger log;
	private boolean loaded = false;
	private OnLoadedListener listener;
	
	public AssetLoader() {
		log = new Logger(AssetLoader.class.getSimpleName());
		queue.offer(null);
		loadAssets();
	}
	public void setOnLoadedListener(OnLoadedListener listener) {
		this.loaded = false;
		this.listener = listener;
	}
	
	protected void loadAssets() {
	}
	protected void onComplete() {
	}
	
	public boolean isCompleted() {
		return loaded;
	}
	
	public <T> T load(String name, Class<?> type, AssetLoadingTask<T> task) {
		if (isLoaded(name, type)) return get(name, type);
		queue.offer(new Task<T>(name, type, task));
		return null;
	}

	@SuppressWarnings("unchecked")
	protected <T> T get(String name, Class<?> type) {
		return (T) assets.get(type).get(name);
	}

	private boolean isLoaded(String name, Class<?> type) {
		if (!assets.containsKey(type)) return false;
		if (!assets.get(type).containsKey(name)) return false;
		return true;
	}
	
	/**
	 * Updates the loader
	 * @return true if everything is loaded
	 */
	public boolean update() {
		if (queue.size() != 0) {
			nextTask();
			if (queue.size() != 0) return false;
		}
		if (!loaded) {
			loaded = true;
			onComplete();
			listener.onLoaded();
		}
		return true;
	}

	

	private void nextTask() {
		Task<?> task = queue.poll();
		if (task == null) return;
		Object result = task.loader.syncLoad();
		if (!assets.containsKey(task.type))
			assets.put(task.type, new HashMap<String, Object>());
		assets.get(task.type).put(task.name, result);
		
		System.out.println("Loaded "+task.name);
	}
}
