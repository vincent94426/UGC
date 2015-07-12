package cn.dezhisoft.cloud.mi.newugc.ioffer.upload.ui;

import com.sobey.sdk.db.Table;
import com.sobey.sdk.db.TableID;
import com.sobey.sdk.db.TableID.INCREASE_TYPE;
import com.sobey.sdk.db.TableInt;
import com.sobey.sdk.db.TableString;

/**
 * UGC Upload config
 * 
 * @author Rosson Chen
 *
 */
@Table(TableName="_upload_config")
public final class UGCUploadConfig {

	@TableID(increase=INCREASE_TYPE.AUTO)
	@TableInt
	private int id ;
	
	@TableString(length=256)
	private String appName ;
	
	@TableInt
	private int videoWidth ;
	
	@TableInt
	private int videoHeight ;
	
	@TableInt
	private int videoBitrate ;
	
	@TableInt
	private int gpsFlag ;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getAppName() {
		return appName;
	}

	public void setAppName(String appName) {
		this.appName = appName;
	}

	public int getVideoWidth() {
		return videoWidth;
	}

	public void setVideoWidth(int videoWidth) {
		this.videoWidth = videoWidth;
	}

	public int getVideoHeight() {
		return videoHeight;
	}

	public void setVideoHeight(int videoHeight) {
		this.videoHeight = videoHeight;
	}

	public int getVideoBitrate() {
		return videoBitrate;
	}

	public void setVideoBitrate(int videoBitrate) {
		this.videoBitrate = videoBitrate;
	}

	public int getGpsFlag() {
		return gpsFlag;
	}

	public void setGpsFlag(int gpsFlag) {
		this.gpsFlag = gpsFlag;
	}
}
