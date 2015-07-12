package cn.dezhisoft.cloud.mi.newugc.ugc.model;

import java.util.ArrayList;

import cn.dezhisoft.cloud.mi.newugc.ioffer.common.Util;
import cn.dezhisoft.cloud.mi.newugc.ugc.core.model.MediaType;

/**
 * 素材
 * 
 * @author Rosson Chen
 * 
 */
public class Material {

	private String packageGuid;
	private String MaterialGuid;
	private String title;
	private MediaType mediaType;
	private String duration;
	private String createTime;
	private String userName;
	private String deviceType;
	private MaterialStatus status;
	private int clickCount;
	private int downloadCount;
	private String iconUrl ;
	
	private final ArrayList<MaterialResouce> resouces = new ArrayList<MaterialResouce>() ;
	
	public Material(){
		clear() ;
	}
	
	public ArrayList<MaterialResouce> getResouces() {
		return resouces;
	}

	public void addMaterialResouce(MaterialResouce asset){
		resouces.add(asset);
	}
	
	public String getPackageGuid() {
		return packageGuid;
	}

	public String getIconUrl() {
		return iconUrl;
	}

	public void setIconUrl(String iconUrl) {
		this.iconUrl = iconUrl;
	}

	public void setPackageGuid(String packageGuid) {
		this.packageGuid = packageGuid;
	}

	public String getMaterialGuid() {
		return MaterialGuid;
	}

	public void setMaterialGuid(String materialGuid) {
		MaterialGuid = materialGuid;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public MediaType getMediaType() {
		return mediaType;
	}

	public void setMediaType(String mediaType) {
		this.mediaType = Util.createMediaType(mediaType);
	}

	public String getDuration() {
		return duration;
	}

	public void setDuration(String duration) {
		this.duration = duration;
	}

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getDeviceType() {
		return deviceType;
	}

	public void setDeviceType(String deviceType) {
		this.deviceType = deviceType;
	}

	public MaterialStatus getStatus() {
		return status;
	}

	public void setStatus(int code) {
		this.status = MaterialStatus.createStatus(code);
	}

	public int getClickCount() {
		return clickCount;
	}

	public void setClickCount(int clickCount) {
		this.clickCount = clickCount;
	}

	public int getDownloadCount() {
		return downloadCount;
	}

	public void setDownloadCount(int downloadCount) {
		this.downloadCount = downloadCount;
	}
	
	public void clear(){
		resouces.clear() ;
	}
}
