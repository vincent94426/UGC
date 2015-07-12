package cn.dezhisoft.cloud.mi.newugc.ugc.model;

import cn.dezhisoft.cloud.mi.newugc.ioffer.common.Util;
import cn.dezhisoft.cloud.mi.newugc.ugc.core.model.MediaType;

public final class MaterialResouce {

	private MaterialResouceType assetType;
	private String 				assetFileUrl;
	private String 				assetFileSize;
	private String				assetStreamType;
	private String 				assetFormatDesc;
	private String 				assetVideoTrack;
	private String 				assetAudioTrack;
	private MediaType 			assetMediaTrack;
	private String 				assetDuration;

	public MaterialResouceType getAssetType() {
		return assetType;
	}

	public void setAssetType(String assetType) {
		this.assetType = MaterialResouceType.create(assetType);
	}

	public String getAssetFileUrl() {
		return assetFileUrl;
	}

	public void setAssetFileUrl(String assetFileUrl) {
		this.assetFileUrl = assetFileUrl;
	}

	public String getAssetFileSize() {
		return assetFileSize;
	}

	public void setAssetFileSize(String assetFileSize) {
		this.assetFileSize = assetFileSize;
	}

	public String getAssetStreamType() {
		return assetStreamType;
	}

	public void setAssetStreamType(String assetStreamType) {
		this.assetStreamType = assetStreamType;
	}

	public String getAssetFormatDesc() {
		return assetFormatDesc;
	}

	public void setAssetFormatDesc(String assetFormatDesc) {
		this.assetFormatDesc = assetFormatDesc;
	}

	public String getAssetVideoTrack() {
		return assetVideoTrack;
	}

	public void setAssetVideoTrack(String assetVideoTrack) {
		this.assetVideoTrack = assetVideoTrack;
	}

	public String getAssetAudioTrack() {
		return assetAudioTrack;
	}

	public void setAssetAudioTrack(String assetAudioTrack) {
		this.assetAudioTrack = assetAudioTrack;
	}

	public MediaType getAssetMediaTrack() {
		return assetMediaTrack;
	}

	public void setAssetMediaTrack(String assetMediaTrack) {
		this.assetMediaTrack = Util.createMediaTypeByTrack(assetMediaTrack);
	}

	public String getAssetDuration() {
		return assetDuration;
	}

	public void setAssetDuration(String assetDuration) {
		this.assetDuration = assetDuration;
	}

}
