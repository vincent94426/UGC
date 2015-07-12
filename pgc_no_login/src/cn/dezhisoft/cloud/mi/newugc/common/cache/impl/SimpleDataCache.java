package cn.dezhisoft.cloud.mi.newugc.common.cache.impl;

import java.util.Hashtable;
import java.util.Map;

import cn.dezhisoft.cloud.mi.newugc.common.cache.DataCache;

public final class SimpleDataCache implements DataCache {
	private Map<Integer, Object> cacheValMap = new Hashtable<Integer, Object>();
	private Map<Integer, Long> cacheTimeMap = new Hashtable<Integer, Long>();
	
	private static SimpleDataCache dataCache;
	private SimpleDataCache() {
		
	}
	
	public static DataCache getInstance() {
		if(null == dataCache){
			dataCache = new SimpleDataCache();
		}
		return dataCache;
	}
	
	@Override
	public void putData(String key, Object value) {
		int keyCode = key.hashCode();
		cacheValMap.put(keyCode, value);
		cacheTimeMap.put(keyCode, System.currentTimeMillis());
	}

	@Override
	public Object getData(String key) {
		int keyCode = key.hashCode();
		Long savedTime = cacheTimeMap.get(keyCode);
		if(null == savedTime || (System.currentTimeMillis() - savedTime) > 500) {//缓存时间大于500毫秒，失效
			return null;
		}else {
			return cacheValMap.get(keyCode);
		}
	}
}
