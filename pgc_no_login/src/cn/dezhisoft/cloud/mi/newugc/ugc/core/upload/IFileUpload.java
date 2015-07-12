package cn.dezhisoft.cloud.mi.newugc.ugc.core.upload;

import java.util.ArrayList;

import cn.dezhisoft.cloud.mi.newugc.ugc.core.model.Task;

/**
 * 文件上传接口
 * 
 * @author Rosson Chen
 *
 */
public interface IFileUpload {
	
	/** 广播消息: 用于通知上传服务查询新任务 */
	public static final String ACTION_QUERY_NEW_TASK 			= "cn.dezhisoft.cloud.mi.newugc.ugc.upload.query" ;
	/** 广播消息: 用于通知有上传任务完成 */
	public static final String ACTION_QUERY_UPLOAD_COMPLETE 	= "cn.dezhisoft.cloud.mi.newugc.ugc.upload.complete" ;
	
	/** 文件上传消息 */
	public static final class UploadMessage {

		public String 		mTuid;
		public int 			mTaskId ;// 多任务的时候
		public int 			mState;
		public int			mProgress;
		public int 			mTotalSize;
		public String 		mMessage;
		
		public UploadMessage(){
			mTuid 		= "";
			mState  	= Task.STATE.STOP;
			mProgress 	= 0;
			mTotalSize  = 0;
			mMessage 	= "";
			mTaskId		= 0 ;
		}
	}
	
	/** 文件上传回调 */
	public static interface IFileUploadCallback {
		
		public void progress(String tui,long total,int parent,int id /* 多任务的时候,任务id*/) ;
		
		public void command(String tuid,int type,int code ,String t1);
		
		public void notifyChange(UploadMessage message);
	}
	
	/** 设置回调 */
	public void setCallback(IFileUploadCallback callback) ;
	
	/** 从数据库中查询所有任务 */
	public ArrayList<Task> getHistoryList() ;
	
	/** 得到上传队列大小 */
	public int getUploadSize();
	
	/** 删除任务 */
	public void removeTask(Task task);
	
	/** 从新开始上传 */
	public boolean resumeTask(Task task);
	
	/** 停止当前任务 */
	public void stopCurrentTask();
}
