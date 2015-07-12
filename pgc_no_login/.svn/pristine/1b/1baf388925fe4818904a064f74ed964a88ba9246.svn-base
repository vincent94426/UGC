package cn.dezhisoft.cloud.mi.newugc.ugc.ws;


import android.widget.ImageView;

import com.sobey.sdk.cache.IDownloaderCallback;

/**
 * 资源下载, 图片, APK, 文档等
 * 
 * @author Rosson Chen
 *
 */
public interface IDownResource {
	
	public void downBitmap(String url,ImageView imageView,int rid);
	
	public void downBitmap(String url,ImageView imageView,int rid,boolean fadeIn);
	
	public void downBitmap(String url,Object tag,ImageView imageView,int rid,IDownloaderCallback callback,boolean fadeIn) ;
	
	public void onStart() ;
	
	public void onPause() ;
	
	public void onResume() ;
	
	public void onDestory() ;
}
