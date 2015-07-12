package cn.dezhisoft.cloud.mi.newugc.ugc.version;

import java.io.Serializable;

import com.sobey.sdk.db.Table;
import com.sobey.sdk.db.TableID;
import com.sobey.sdk.db.TableInt;
import com.sobey.sdk.db.TableID.INCREASE_TYPE;
import com.sobey.sdk.db.TableString;

@Table(TableName="_update")
public class UpdateInfo implements Serializable{
	
	public static final class Status {
		public static final int STATUS_NEW 			= 1 ;
		public static final int STATUS_READED		= 2 ;
	}
	
	/** id */
	@TableID(increase=INCREASE_TYPE.AUTO)
	@TableInt
	private int 		id ;
	
	@TableString(length=32)
	private String 		versionName ;
	
	@TableString(length=256)
	private String 		versionDescription ;
	
	@TableInt
	private int 		versionStatus ;
	
	@TableString(length=32)
	private String 		lastVersion ;
	
	public UpdateInfo(){
		setVersionStatus(Status.STATUS_NEW);
		setLastVersion("1.0");
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getVersionName() {
		return versionName;
	}

	public void setVersionName(String versionName) {
		this.versionName = versionName;
	}

	public String getVersionDescription() {
		return versionDescription;
	}

	public void setVersionDescription(String versionDescription) {
		this.versionDescription = versionDescription;
	}

	public int getVersionStatus() {
		return versionStatus;
	}

	public String getLastVersion() {
		return lastVersion;
	}

	public void setLastVersion(String lastVersion) {
		this.lastVersion = lastVersion;
	}

	public void setVersionStatus(int versionStatus) {
		this.versionStatus = versionStatus;
	}
	
}
