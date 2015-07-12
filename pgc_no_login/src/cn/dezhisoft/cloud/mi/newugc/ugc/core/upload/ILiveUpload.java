package cn.dezhisoft.cloud.mi.newugc.ugc.core.upload;

import android.app.Activity;
import android.view.SurfaceHolder;

import cn.dezhisoft.cloud.mi.newugc.ugc.core.model.Metadata;

/**
 * 直播上传接口
 * 
 * @author Rosson Chen
 * 
 */
public interface ILiveUpload {
	
	/** 直播上传回调 */
	public static interface ILiveUploadCallback {
		
		public void notifyChange(String tuid,int state,String message);
		
		public void command(String tuid,int type,int code ,String t1,String t2);
		
		public void liveStatistics(String tuid,long send, long lost, int bitRate);
	}
	
	public void setCallback(ILiveUploadCallback callback) ;

	public void setEncoder(int encoder, Activity onwer);

	public void setAudioConfig(int mime,int channel,int sampleRate,int bitrate);

	public void setVideoConfig(int width, int height, int fps,int bitRate);

	public void setPreview(SurfaceHolder holder);

	public long getCurrentTime();
	
	public void prepare();
	
	public boolean start(Metadata meta,String uid) ;
	
	public void stop();
	
	public void release() ;
}