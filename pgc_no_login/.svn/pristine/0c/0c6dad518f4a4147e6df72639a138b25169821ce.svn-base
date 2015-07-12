package cn.dezhisoft.cloud.mi.newugc.ugc.ws.publish;


import android.widget.ImageView;

import cn.dezhisoft.cloud.mi.newugc.ioffer.common.Config;
import com.sobey.sdk.cache.IDownloaderCallback;
import com.sobey.sdk.cache.ImageDownloader;
import cn.dezhisoft.cloud.mi.newugc.ugc.model.ImageItem;
import cn.dezhisoft.cloud.mi.newugc.ugc.ws.IDownResource;

/**
 * 
 * @author Rosson Chen
 *
 */
public final class DownResourceImpl implements IDownResource {

	private  ImageDownloader mCacheManager ;
	private final ImageItem mImageItem = new ImageItem() ;
	
	@Override
	public void downBitmap(String url, ImageView imageView, int rid) {
		downBitmap(url, null, imageView, rid, null, false);
	}

	@Override
	public void downBitmap(String url, ImageView imageView, int rid,boolean fadeIn) {
		downBitmap(url, null, imageView, rid, null, fadeIn);
	}

	@Override
	public void downBitmap(String url, Object tag, ImageView imageView,
			int rid, IDownloaderCallback callback, boolean fadeIn) {
		
		checkCacheManager();
		
		final ImageItem item = mImageItem.clone() ;
		item.setHighUrl(url);
		item.setLowUrl(url);
		item.setTag(tag);
		
		mCacheManager.downloadBitmap(item, imageView, rid, callback, fadeIn);
		
	}
	
	@Override
	public void onStart() {
		if(mCacheManager == null){
			mCacheManager.onStart() ;
		}
	}

	@Override
	public void onPause() {
		if(mCacheManager == null){
			mCacheManager.onPause() ;
		}
	}

	@Override
	public void onResume() {
		if(mCacheManager == null){
			mCacheManager.onResume() ;
		}
	}

	@Override
	public void onDestory() {
		if(mCacheManager == null){
			mCacheManager.onDestory() ;
		}
	}

	private void checkCacheManager(){
		if(mCacheManager == null)
			mCacheManager = ImageDownloader.create(Config.getContext(), "ugc-v1.3", 0.20f);
	}
}
