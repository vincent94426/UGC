package cn.dezhisoft.cloud.mi.newugc.ugc.core.model;

import java.io.Serializable;

import com.sobey.sdk.db.Table;
import com.sobey.sdk.db.TableID;
import com.sobey.sdk.db.TableID.INCREASE_TYPE;
import com.sobey.sdk.db.TableInt;
import com.sobey.sdk.db.TableString;

/**
 * 素材的详细信息,如: 预览地址,下载地址,素材组信息
 * 
 * @author Rosson Chen
 *
 */
@Table(TableName="_assets_table")
public class AssetsType implements Serializable {
	
	/** 下载状态 */
	public static final class DownloadState {
		/** 新任务 */
		public static final int NEW			= 1 ;
		/** 等待 */
		public static final int WAIT		= 2 ;
		/** 正在下载 */
		public static final int RUNNING		= 3 ;
		/** 停止 */
		public static final int STOP		= 4 ;
		/** 完成 */
		public static final int DONE		= 5 ;
	}
	
	private static final long serialVersionUID = -7375027832435379203L;

	/** id */
	@TableID(increase=INCREASE_TYPE.AUTO)
	@TableInt
	private int 		id ;

	/** 素材预览地址。服务器根据当前客户端设置自动返回对应的流媒体地址 */
	@TableString(length=512)
	private String previewUrl ;
	
	/** 素材下载地址*/
	@TableString(length=512)
	private String downloadUrl ;
	
	/** 进度 */
	@TableInt
	private int 	progress ;
	
	/** 总大小 */
	@TableInt
	private int 	total ;
	
	/** 标识符*/
	@TableString(length=256)
	private String indentifier ;
	
	/** 文本信息*/
	@TableString(length=64)
	private String detail ;
	
	/** 状态 */
	@TableInt
	private int 	state ;
	
	/** 视频时间长度*/
	private String duration ;
	
	/** 视频时间长度*/
	private String iconUrl ;
	
	/** 素材类型*/
	private MediaType mediaType ;
	
	/** 素材类型Code */
	@TableInt
	private int 	mediaCode ;
	
	/** FTP 服务地址 */
	@TableString(length=64)
	private String 	ftpServer ;
	
	/** FTP 端口 */
	@TableInt
	private int 	ftpPort ;
	
	/** FTP 用户名 */
	@TableString(length=64)
	private String 	ftpUname ;
	
	/** FTP 密码 */
	@TableString(length=64)
	private String 	ftpUpwd ;
	
	/** 本地路径 */
	@TableString(length=64)
	private String 	localPath ;

	public AssetsType(){
		state		= DownloadState.NEW ;
		previewUrl 	= "" ;
		downloadUrl = "" ;
		indentifier = "" ;
		progress	= 0 ;
		detail		= "" ;
		ftpServer	= "" ;
		ftpPort		= 21 ;
		ftpUname	= "" ;
		ftpUpwd		= "" ;
		mediaCode	= 0 ;
		total		= 0 ;
		localPath	= "" ;
	}
	
	public String getPreviewUrl() {
		return previewUrl != null ? previewUrl : "";
	}

	public int getProgress() {
		return progress;
	}

	public int getTotal() {
		return total;
	}

	public void setTotal(int total) {
		this.total = total;
	}

	public String getDetail() {
		return detail;
	}

	public void setDetail(String detail) {
		this.detail = detail;
	}

	public void setProgress(int progress) {
		this.progress = progress;
	}

	public void setPreviewUrl(String previewUrl) {
		this.previewUrl = previewUrl;
	}

	public String getDownloadUrl() {
		return downloadUrl != null ? downloadUrl : "";
	}

	public void setDownloadUrl(String downloadUrl) {
		this.downloadUrl = downloadUrl;
	}

	public String getLocalPath() {
		return localPath;
	}

	public void setLocalPath(String localPath) {
		this.localPath = localPath;
	}

	public String getIndentifier() {
		return indentifier != null ? indentifier : "unkown";
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public void setIndentifier(String indentifier) {
		this.indentifier = indentifier;
	}

	public String getDuration() {
		return duration;
	}

	public int getState() {
		return state;
	}

	public void setState(int state) {
		this.state = state;
	}

	public void setDuration(String duration) {
		this.duration = duration;
	}

	public String getIconUrl() {
		return iconUrl;
	}

	public void setIconUrl(String iconUrl) {
		this.iconUrl = iconUrl;
	}

	public MediaType getMediaType() {
		
		if(mediaType == null){
			setMediaType(MediaType.matchMediaType(mediaCode));
		}
		
		return mediaType ;
	}

	public void setMediaType(MediaType mediaType) {
		this.mediaType = mediaType;
	}

	public int getMediaCode() {
		return mediaType == null ? mediaCode : mediaType.getCode() ;
	}

	public void setMediaCode(int mediaCode) {
		this.mediaCode = mediaCode;
	}

	public String getFtpServer() {
		return ftpServer;
	}

	public void setFtpServer(String ftpServer) {
		this.ftpServer = ftpServer;
	}

	public int getFtpPort() {
		return ftpPort;
	}

	public void setFtpPort(int ftpPort) {
		this.ftpPort = ftpPort;
	}

	public String getFtpUname() {
		return ftpUname;
	}

	public void setFtpUname(String ftpUname) {
		this.ftpUname = ftpUname;
	}

	public String getFtpUpwd() {
		return ftpUpwd;
	}

	public void setFtpUpwd(String ftpUpwd) {
		this.ftpUpwd = ftpUpwd;
	}

	@Override
	public boolean equals(Object o) {
		if(o instanceof AssetsType){
			AssetsType a = (AssetsType)o;
	         return downloadUrl.equals(a.getDownloadUrl());
		} else {
			return false ;
		}
	}
}
