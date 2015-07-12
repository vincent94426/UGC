package cn.dezhisoft.cloud.mi.newugc.ugc.ws.publish;

import android.os.Handler;

import com.sobey.sdk.base.DownloadCallback;
import cn.dezhisoft.cloud.mi.newugc.ugc.model.ImageItem;

/**
 * 资源下载回调
 * 
 * @author Rosson Chen
 *
 */
public class DownResourceCallback extends DownloadCallback {
	
	// 图片下载成功消息
	public static final int MSG_DOWNLOAD_BITMAP_SUCCESS = 0x000fffff01 ;
	
	static final String TAG		= "DownResourceCallback" ;
	static long TIMECODE		= System.currentTimeMillis() ;
	
	private ImageItem item ;
	private int what ;
	private Handler handler;
	
	public DownResourceCallback(ImageItem item){
		this(item,MSG_DOWNLOAD_BITMAP_SUCCESS,null);
	}
	
	public DownResourceCallback(ImageItem item,Handler handler){
		this(item,MSG_DOWNLOAD_BITMAP_SUCCESS,handler);
	}
	
	public DownResourceCallback(ImageItem item,int msg,Handler handler){
		this.item 	= item ;
		this.what 	= msg ;
		this.handler= handler ;
	}

	public final ImageItem getItem() {
		return item;
	}

	@Override
	public synchronized void onFinish(String url, Object value) {
		
		if(System.currentTimeMillis() - TIMECODE < 20 ){
			try{
				Thread.sleep(20) ;
			}catch(Exception e){
				e.printStackTrace() ;
			}
		}
		
		if(handler != null)
			handler.obtainMessage(what, item).sendToTarget() ;
		
		TIMECODE	= System.currentTimeMillis() ;
	}
}
