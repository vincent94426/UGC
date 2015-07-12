package cn.dezhisoft.cloud.mi.newugc.common.cache;

import android.content.Context;
import android.graphics.Bitmap;
import cn.dezhisoft.cloud.mi.newugc.common.cache.impl.ImageFileSystemCache;
import cn.dezhisoft.cloud.mi.newugc.common.cache.impl.ImageMemoryCache;

/**
 * 图片Cache代理类，包装了对多类型Cache的读取
 * @author chenhp1
 */
public final class ImageCacheProxy {
	private static ImageCache memoryCache = null;
	private static ImageCache fileSystemCache = null;
	private ImageCacheProxy(){}
	
	private static void initCache(Context context) {
		memoryCache = ImageMemoryCache.getInstance();
		fileSystemCache = ImageFileSystemCache.getInstance(context);
	}
	
	public static boolean putBitmap(Context context, String key, Bitmap value) {
		return putBitmap(context, key, value, null);
	}
	
	public static boolean putBitmap(Context context, String key, Bitmap value, String format) {
		if(null == fileSystemCache || null == memoryCache){
			initCache(context);
		}
		
		boolean cachedA = memoryCache.putBitmap(key, value);
		boolean cachedB = fileSystemCache.putBitmap(key, value, format);
		
		return cachedA && cachedB;
	}
	
	public static Bitmap getBitmap(Context context, String key) {
		if(null == fileSystemCache || null == memoryCache){
			initCache(context);
		}
		
		Bitmap val = memoryCache.getBitmap(key);
		if(null != val){
			return val;
		}
		
		val = fileSystemCache.getBitmap(key);
		
		return val;
	}
}
