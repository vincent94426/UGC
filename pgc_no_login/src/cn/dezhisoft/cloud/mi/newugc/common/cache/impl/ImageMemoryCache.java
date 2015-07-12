package cn.dezhisoft.cloud.mi.newugc.common.cache.impl;

import java.lang.ref.SoftReference;
import java.util.LinkedHashMap;
import java.util.Map;

import android.graphics.Bitmap;
import android.support.v4.util.LruCache;
import android.util.Log;
import cn.dezhisoft.cloud.mi.newugc.common.cache.ImageCache;

public final class ImageMemoryCache implements ImageCache{
	private static final int MAX_HARD_CACHE_SIZE = 500 * 1024;
	private static final int MAX_SOFT_CACHE_SIZE = 30;
	private LruCache<String, Bitmap> hardCache = null;
	private Map<String, SoftReference<Bitmap>> softCache = null;
	
	private static ImageMemoryCache instance = null;
	private ImageMemoryCache() {
		initCacheInstance();
	}
	
	public static synchronized ImageMemoryCache getInstance(){
		if(null == instance) {
			instance = new ImageMemoryCache();
		}
		return instance;
	}

	private void initCacheInstance() {
		hardCache = new LruCache<String, Bitmap>(MAX_HARD_CACHE_SIZE){
			@Override
			public int sizeOf(String key, Bitmap value){
				return value.getRowBytes() * value.getHeight();
			}
			@Override
			protected void entryRemoved(boolean evicted, String key, Bitmap oldValue, Bitmap newValue){
				softCache.put(key, new SoftReference<Bitmap>(oldValue));
			}
		};
		
		softCache = new LinkedHashMap<String, SoftReference<Bitmap>>(MAX_SOFT_CACHE_SIZE, 0.75f, true){
			/**
			 * 
			 */
			private static final long serialVersionUID = -5317816177504546466L;

			@Override
			protected boolean removeEldestEntry(Map.Entry<String, SoftReference<Bitmap>> eldest) {
				return size() > MAX_SOFT_CACHE_SIZE;
			}

		};
	}
	
	@Override
	public Bitmap getBitmap(String key) {
		if(null == key){
			return null;
		}

		{ //synchronized (hardCache) 
			final Bitmap val = hardCache.get(key);
			
			if(null != val) {
				Log.d(TAG, "Hit Bitmap from First MemoryCache : " + key);
				
				return val;
			}
		}
		
		{//synchronized (softCache) 
			SoftReference<Bitmap> softReference = softCache.get(key);
			if(null != softReference){
				final Bitmap val = softReference.get();
				
				if(null != val) {
					Log.d(TAG, "Hit Bitmap from Second MemoryCache : " + key);					
				
					return val;
				}else {
					softCache.remove(key);
				}
			}
		}
		return null;
	}

	@Override
	public boolean putBitmap(String key, Bitmap value) {
		if(null != value){
			synchronized (hardCache) {
				hardCache.put(key, value);
				
				Log.d(TAG, "Put Bitmap to MemoryCache : " + key + ",size:" + value.getHeight()*value.getRowBytes());
				return true;
			}
		}
		return false;
	}

	@Override
	public boolean putBitmap(String key, Bitmap value, String format) {
		return putBitmap(key, value);
	}
}
