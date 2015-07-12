package cn.dezhisoft.cloud.mi.newugc.ugc.version;

import java.io.Serializable;

/**
 * 版本信息
 * 
 * @author Rosson Chen
 *
 */
public final class VersionInfo implements Serializable {
	
	/** 是否需要更新 */
	public static class Result {
		public static final String COMPATIBLE 	= "compatible" ;
		public static final String CANUPGRADE 	= "canupgrade" ;
		public static final String MUSTUPGRADE 	= "mustupgrade" ;
	}

	/** 主版本号 */
	private String majorVersion;

	/** 子版本号 */
	private String accessoryVersion;
	
	/** 上一版本号 */
	private String lastVersion;

	/** 描述 */
	private String description;

	/** 地址 */
	private String url;
	
	/** 是否必须更新 */
	private String status ;
	
	public VersionInfo(){
		setStatus("");
		setLastVersion("1.0");
	}

	public String getLastVersion() {
		return lastVersion;
	}

	public void setLastVersion(String lastVersion) {
		this.lastVersion = lastVersion;
	}

	public String getMajorVersion() {
		return majorVersion;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public void setMajorVersion(String majorVersion) {
		this.majorVersion = majorVersion;
	}

	public String getAccessoryVersion() {
		return accessoryVersion;
	}

	public void setAccessoryVersion(String accessoryVersion) {
		this.accessoryVersion = accessoryVersion;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

}
