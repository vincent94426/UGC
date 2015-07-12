package cn.dezhisoft.cloud.mi.newugc.ioffer.config;

import java.io.Serializable;

import com.sobey.sdk.db.Table;
import com.sobey.sdk.db.TableID;
import com.sobey.sdk.db.TableID.INCREASE_TYPE;
import com.sobey.sdk.db.TableInt;
import com.sobey.sdk.db.TableString;
import com.sobey.sdk.encoder.AbstractEncoder;

@Table(TableName="_config")
public final class AppConfig implements Serializable{

	private static final long serialVersionUID = -1165011326324219070L;

	@TableID(increase=INCREASE_TYPE.AUTO)
	@TableInt
	private int id ;

	@TableString(length=256)
	private String host ;
	
	@TableInt
	private int auto ;
	
	@TableString(length=64)
	private String name ;
	
	@TableString(length=64)
	private String password ;
	
	@TableString(length=32)
	private String siteId ;
	
	@TableString(length=128)
	private String siteName ;
	
	@TableInt
	private int checkVersion ;
	
	@TableInt
	private int recordFlag ;
	
	@TableInt
	private int recordWidth ;
	
	@TableInt
	private int recordHeight ;
	
	@TableInt
	private int recordQuality ;
	
	@TableInt
	private int liveFlag ;
	
	@TableInt
	private int liveWidth ;
	
	@TableInt
	private int liveHeight ;
	
	@TableInt
	private int liveQuality ;
	
	public String getSiteName() {
		return siteName;
	}

	public void setSiteName(String siteName) {
		this.siteName = siteName;
	}

	public AppConfig(){
		siteId 	= "-1" ;
		auto	=  0;
		host	= "" ;
		checkVersion = 0 ;
		
		recordFlag 	= liveFlag   	= 0 ;
		recordWidth	= liveWidth		= 176 ;
		recordHeight= liveHeight	= 144 ;
		recordQuality = liveQuality	= AbstractEncoder.VideoQuality.VIDEO_QUALITY_LOW ;
	}

	public int getCheckVersion() {
		return checkVersion;
	}

	public void setCheckVersion(int checkVersion) {
		this.checkVersion = checkVersion;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public int getAuto() {
		return auto;
	}

	public void setAuto(int auto) {
		this.auto = auto;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getSiteId() {
		return siteId;
	}

	public int getRecordFlag() {
		return recordFlag;
	}

	public void setRecordFlag(int recordFlag) {
		this.recordFlag = recordFlag;
	}

	public int getRecordWidth() {
		return recordWidth;
	}

	public void setRecordWidth(int recordWidth) {
		this.recordWidth = recordWidth;
	}

	public int getRecordHeight() {
		return recordHeight;
	}

	public void setRecordHeight(int recordHeight) {
		this.recordHeight = recordHeight;
	}

	public int getRecordQuality() {
		return recordQuality;
	}

	public void setRecordQuality(int recordQuality) {
		this.recordQuality = recordQuality;
	}

	public int getLiveFlag() {
		return liveFlag;
	}

	public void setLiveFlag(int liveFlag) {
		this.liveFlag = liveFlag;
	}

	public int getLiveWidth() {
		return liveWidth;
	}

	public void setLiveWidth(int liveWidth) {
		this.liveWidth = liveWidth;
	}

	public int getLiveHeight() {
		return liveHeight;
	}

	public void setLiveHeight(int liveHeight) {
		this.liveHeight = liveHeight;
	}

	public int getLiveQuality() {
		return liveQuality;
	}

	public void setLiveQuality(int liveQuality) {
		this.liveQuality = liveQuality;
	}

	public void setSiteId(String siteId) {
		this.siteId = siteId;
	}
}
