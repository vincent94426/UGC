package cn.dezhisoft.cloud.mi.newugc.ugc.core.model;

import java.io.Serializable;
import java.util.ArrayList;

import com.sobey.sdk.db.Table;
import com.sobey.sdk.db.TableID;
import com.sobey.sdk.db.TableID.INCREASE_TYPE;
import com.sobey.sdk.db.TableInt;
import com.sobey.sdk.db.TableString;

/**
 * UGC System upload Class Task define
 * 
 * @author Rosson Chen
 *
 */
@Table(TableName="_task")
public class Task implements Serializable {
	
	/** */
	private static final long serialVersionUID = -5074179189969434391L;
	
	/** UGC category*/
	public static final class CATEGORY {
		/** Upload */
		public static final int UPLOAD			= 0 ;
		/** Download */
		public static final int DOWNLOAD		= 1 ;
	}
	
	/** UGC upload type*/
	public static final class TYPE {
		/** Live */
		public static final int LIVE			= 2 ;
		/** File */
		public static final int FILE			= 3 ;
		/** Image */
		public static final int IMAGE			= 4 ;
		/** Audio */
		public static final int AUDIO			= 5 ;
		/** Video */
		public static final int VIDEO			= 6 ;
	}
	
	/** UGC upload state*/
	public static final class STATE {
		/** new task */	
		public static final int NEW				= 7 ;
		/** run: up | down */
		public static final int RUNNING			= 8 ;
		/** wait for run*/
		public static final int WAIT			= 9 ;
		/** stop */
		public static final int STOP			= 10 ;
		/** finish */
		public static final int DONE			= 11 ;
	}
	
	/** id */
	@TableID(increase=INCREASE_TYPE.AUTO)
	@TableInt
	private int 		id ;
	
	/** task category */
	@TableInt
	private int 		category ;
	
	/** task type */
	@TableInt
	private int 		type ;
	
	/** task state */
	@TableInt
	private int 		state ;
	
	/** task transfer UID */
	@TableString(length=128)
	private String 		transferUID ;
	
	/** task progress */
	@TableInt
	private int 		taskProgress ;
	
	/** task length */
	@TableInt
	private int 		taskLength ;
	
	/** task start point */
	@TableInt
	private int 		taskStartPoint ;
	
	/** task end point */
	@TableInt
	private int 		taskEndPoint ;
	
	/** task name */
	@TableString(length=128)
	private String 		taskName ;
	
	/** task path */
	@TableString(length=256)
	private String 		taskPath ;
	
	/** task metadata*/
	private Metadata	metadata ;
	
	/** upload task user */
	@TableString(length=128)
	private String  	author ;
	
	/** task state text */
	@TableString(length=256)
	private String 		detail ;
	
	/** more than one file */
	private final ArrayList<String> MultiTask = new ArrayList<String>() ;
	
	public Task(){
		MultiTask.clear() ;
	}
	
	public int getCategory() {
		return category;
	}

	public void setCategory(int category) {
		this.category = category;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public int getState() {
		return state;
	}

	public void setState(int state) {
		this.state = state;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getDetail() {
		return detail != null ? detail : "等待";
	}

	public void setDetail(String detail) {
		this.detail = detail;
	}

	public String getAuthor() {
		return author != null ? author : "unkown";
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public int getTaskProgress() {
		return taskProgress;
	}

	public void setTaskProgress(int taskProgress) {
		this.taskProgress = taskProgress;
	}

	public int getTaskStartPoint() {
		return taskStartPoint;
	}

	public void setTaskStartPoint(int taskStartPoint) {
		this.taskStartPoint = taskStartPoint;
	}

	public int getTaskEndPoint() {
		return taskEndPoint;
	}

	public void setTaskEndPoint(int taskEndPoint) {
		this.taskEndPoint = taskEndPoint;
	}

	public String getTransferUID() {
		return transferUID;
	}

	public int getTaskLength() {
		return taskLength;
	}

	public void setTaskLength(int taskLength) {
		this.taskLength = taskLength;
	}

	public void setTransferUID(String transferUID) {
		this.transferUID = transferUID;
	}

	public String getTaskName() {
		return taskName;
	}

	public void setTaskName(String taskName) {
		this.taskName = taskName;
	}

	public String getTaskPath() {
		return taskPath;
	}

	public void setTaskPath(String taskPath) {
		this.taskPath = taskPath;
	}
	
	public Metadata getMetadata() {
		return metadata;
	}

	public void setMetadata(Metadata metadata) {
		this.metadata = metadata;
	}

	public String getTaskExtensionName(){
		if(taskPath == null) return "" ;
		int ext = taskPath.lastIndexOf(".");
		if(ext <= 0) return "" ;
		String extName = taskPath.substring(ext);
		return extName;
	}

	/** file list */
	public ArrayList<String> getRelateFileList() {
		return MultiTask;
	}
	
	/** add file */
	public void addSubTask(String file){
		MultiTask.add(file);
	}
	
	@Override
	public boolean equals(Object o) {
		if(o instanceof Task){
			Task task = (Task)o;
	         return transferUID.equals(task.getTransferUID());
		} else {
			return false ;
		}
	}

}
