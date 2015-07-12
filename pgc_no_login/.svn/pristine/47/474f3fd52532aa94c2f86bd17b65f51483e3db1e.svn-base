package cn.dezhisoft.cloud.mi.newugc.ugc.core.down;

import java.util.ArrayList;

import cn.dezhisoft.cloud.mi.newugc.ugc.core.model.AssetsType;
import cn.dezhisoft.cloud.mi.newugc.ugc.core.model.AssetsType.DownloadState;

/**
 * FTP 下载定义. 实现队列任务下载
 * 
 * @author Rosson Chen
 *
 */
public interface IFileDownload {
	
	/** 文件下载消息 */
	public static final class DownloadMessage {

		public String 		mUrl;
		public int 			mState;
		public int			mProgress;
		public int 			mTotalSize;
		public String 		mMessage;
		public AssetsType	assets ;
		
		public DownloadMessage(){
			mState  	= DownloadState.STOP ;
			mProgress 	= 0;
			mTotalSize  = 0;
			mMessage 	= "";
			assets		= null ;
		}
	}

	/** 下载查询,通过广播消息来通知下载服务来查询新任务 */
	public static final String ACTION_QUERY_NEW_TASK_DOWN 	= "cn.dezhisoft.cloud.mi.newugc.ugc.down.query" ;
	
	/** 下载回调接口 */
	public static interface IDownloadCallback {
		
		/** 进度回调*/
		public void progress(AssetsType asset,int id,int percent,long total) ;
		
		/** 状态回调*/
		public void stateCallback(AssetsType asset,String msg);
	}
	
	/** 设置回调 */
	public void setCallback(IDownloadCallback callback) ;
	
	/** 从数据库中查询所有下载 */
	public ArrayList<AssetsType> getHistoryList() ;
	
	/** 下载队列大小 */
	public int getDownloadSize();
	
	/** 任务是否存在 */
	public boolean checkAssetsType(AssetsType type);
	
	/** 从历史记录中删除任务 */
	public void removeAssetsType(AssetsType asset);
	
	/** 重新下载 */
	public boolean resumeAssetsType(AssetsType asset);
	
	/** 停止当前任务 */
	public void stopDownload(); 
}
