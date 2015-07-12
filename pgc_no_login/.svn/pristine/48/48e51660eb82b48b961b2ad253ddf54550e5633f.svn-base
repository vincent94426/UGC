package cn.dezhisoft.cloud.mi.newugc.common.cache.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.support.v4.util.LruCache;
import android.util.Log;
import cn.dezhisoft.cloud.mi.newugc.common.cache.ImageCache;
import cn.dezhisoft.cloud.mi.newugc.common.constants.NpmConstants;
import cn.dezhisoft.cloud.mi.newugc.common.util.DebugUtil;

public final class ImageFileSystemCache implements ImageCache {
	private static final String IMAGE_NOMEDIA_DIR = ".nomedia";
	public  static final String IMAGE_CACHE_DIR = NpmConstants.PRIVATE_DATA_PATH +"cache/images";
	private static final int MAX_CACHE_SIZE = 100 * 1024 * 1024;
	private File rootCacheDir = null;
	private BitmapFactory.Options sBitmapOptions;
	private LruCache<String, Long> hardCache = null;

	private static ImageFileSystemCache instance = null;

	private ImageFileSystemCache(Context context) {
		initCacheInstance(context);
	}

	public static synchronized ImageFileSystemCache getInstance(Context context) {
		if (null == instance) {
			instance = new ImageFileSystemCache(context);
		}
		return instance;
	}

	private void initCacheInstance(Context context) {
		String storageRootPath = Environment.getExternalStorageDirectory().getAbsolutePath();
		this.rootCacheDir = new File(storageRootPath + IMAGE_CACHE_DIR);//context.getCacheDir();
		File noMediaDir = new File(storageRootPath + IMAGE_CACHE_DIR + File.separator + IMAGE_NOMEDIA_DIR);
		if(!noMediaDir.exists()) {
			noMediaDir.mkdirs();
		}
		
		
		sBitmapOptions = new BitmapFactory.Options();
		sBitmapOptions.inPurgeable = true;

		hardCache = new LruCache<String, Long>(MAX_CACHE_SIZE) {
			@Override
			public int sizeOf(String key, Long value) {
				return value.intValue();
			}

			@Override
			protected void entryRemoved(boolean evicted, String key,
					Long oldValue, Long newValue) {
				try {
					File file = getFile(key);
					if (null != file){
						file.delete();
					}
				} catch (Exception ex) {
					DebugUtil.traceThrowableLog(ex);
				}
			}
		};
	}

	@Override
	public boolean putBitmap(String key, Bitmap value) {
		return putBitmap(key, value, null);
	}
	
	@Override
	public boolean putBitmap(String key, Bitmap value, String format) {
		try {
			File file = getFile(key);
			if (file != null) {
				return true;
			}
			FileOutputStream fos = getOutputStream(key);
			if (null == fos){
				return false;
			}

			CompressFormat f = CompressFormat.JPEG;
			if(FORMAT_PNG.equals(format)){
				f = CompressFormat.PNG;
			}
			
			boolean isuccess = value.compress(f, 85, fos);
			fos.flush();
			fos.close();
			if (isuccess) {
				synchronized (hardCache) {
					hardCache.put(key, getFile(key).length());
					
					Log.d(TAG, "Put Bitmap to FileSystemCache : " + key + ",size:" + value.getHeight()*value.getRowBytes());
					return true;
				}
			}
		} catch (Exception ex) {
			DebugUtil.traceThrowableLog(ex);
		}
		return false;
	}

	@Override
	public Bitmap getBitmap(String key) {
		InputStream inputStream = null;
		try {
			File bitmapFile = getFile(key);
			if (bitmapFile == null){
				return null;
			}

			inputStream = new FileInputStream(bitmapFile);
			Bitmap bitmap = BitmapFactory.decodeStream(inputStream, null, sBitmapOptions);
			if (bitmap != null) {
				ImageCache memoryCache = ImageMemoryCache.getInstance();
				memoryCache.putBitmap(key, bitmap);

				Log.d(TAG, "Hit Bitmap from FileSystemCache : " + key);
				return bitmap;
			}
		} catch( Exception e ){// modified at 2013-3-1, base on pmd:AvoidCatchingThrowable 
			DebugUtil.traceThrowableLog(e);
		} catch( Error e){
			DebugUtil.traceThrowableLog(e);
		} finally {
			if(null != inputStream){
				try {
					inputStream.close();
				} catch (IOException e) {
					DebugUtil.traceThrowableLog(e);
				}
			}
		}
		return null;
	}

	private File getFile(String fileName) {
		if (rootCacheDir == null){
			return null;
		}
		
		File file = new File(rootCacheDir, fileName);
		if (!file.exists() || !file.isFile()){
			return null;
		}
		return file;
	}

	private FileOutputStream getOutputStream(String key)
			throws IOException {
		if (rootCacheDir == null){
			return null;
		}
		
		String absolutePath = rootCacheDir.getAbsolutePath() + File.separator + key;
		String dirString = absolutePath.substring(0, absolutePath.lastIndexOf(File.separator));
		File dir = new File(dirString);
		if(dir.exists() || dir.mkdirs()){
			File nomediaDir = new File(dirString + File.separator + IMAGE_NOMEDIA_DIR);
			if(!nomediaDir.exists()){
				nomediaDir.mkdirs();
			}
			
			return new FileOutputStream(new File(absolutePath));
		}else {
			return null;
		}
	}
}
