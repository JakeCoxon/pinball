package com.jakemadethis.pinball;

import com.badlogic.gdx.assets.AssetLoaderParameters;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.Logger;

public abstract class AdvancedAssetManager  {
	private static AssetManager man = null;
	public static AssetManager getAssetManager() {
		if (man == null) {
			man = new AssetManager();
			Texture.setAssetManager(man);
			man.getLogger().setLevel(Logger.DEBUG);
		}
		return man;
	}
	
	public interface OnLoadedListener {
		void onLoaded();
	}
	protected boolean loaded = false;
	private OnLoadedListener listener;
	
	
	public AdvancedAssetManager() {
		getAssetManager();
		loadAssets();
	}
	
	
	public void setOnLoadedListener(OnLoadedListener l) {
		this.listener = l;
	}
	
	public synchronized boolean update() {
		boolean l = man.update();
		if (l && !loaded) {
			loaded = true;
			loadAssets();
			if (listener != null) 
				listener.onLoaded();
		}
		return l;
	}
	
	protected abstract void loadAssets();
	
	

	protected <T> T load(String fileName, Class<T> type) {
		return load(fileName, type, null);
	}
	protected <T> T load(String fileName, Class<T> type, AssetLoaderParameters<T> param) {
		if (man.isLoaded(fileName))
			return man.get(fileName, type);
		
		man.load(fileName, type, param);
		return null;
	}


	public boolean finished() {
		return man.getProgress() == 1f;
	}
}
