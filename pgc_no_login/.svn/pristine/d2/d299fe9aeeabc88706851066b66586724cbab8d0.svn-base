package cn.dezhisoft.cloud.mi.newugc.ugc.core.model;

import java.io.Serializable;

import com.sobey.sdk.db.Table;
import com.sobey.sdk.db.TableID;
import com.sobey.sdk.db.TableID.INCREASE_TYPE;
import com.sobey.sdk.db.TableInt;
import com.sobey.sdk.db.TableString;

/**
 * UGC upload source metadata
 * 
 * @author Rosson Chen
 *
 */
@Table(TableName="_metadata")
public class Metadata implements Serializable {

	private static final long serialVersionUID = 6650302322798408855L;
	
	@TableID(increase=INCREASE_TYPE.AUTO)
	@TableInt
	private int id ;
	
	/** task transfer UID, */
	@TableString(length=128)
	private String 		transferUID ;

	@TableString(length=32)
	private String siteId ;
	
	/** user name */ 
	@TableString(length=128)
	private String username ;
	
	/** source title */ 
	@TableString(length=256)
	private String title ;
	
	/** source description */ 
	@TableString(length=256)
	private String description ;
	
	/** source category id */ 
	@TableString(length=64)
	private String catalogId ;
	
	/** source category name */ 
	@TableString(length=64)
	private String catalogName ;
	
	/** who */ 
	@TableString(length=64)
	private String who ;
	
	/** what */ 
	@TableString(length=64)
	private String what ;
	
	/** when */ 
	@TableString(length=64)
	private String time ;
	
	/** where */ 
	@TableString(length=64)
	private String place ;
	
	/** event */ 
	@TableString(length=64)
	private String event ;
	
	/** gps */ 
	@TableString(length=64)
	private String gps ;
	
	public Metadata(){
		siteId = "1" ;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getSiteId() {
		return siteId;
	}

	public void setSiteId(String siteId) {
		this.siteId = siteId;
	}

	public String getTransferUID() {
		return transferUID;
	}

	public void setTransferUID(String transferUID) {
		this.transferUID = transferUID;
	}

	public String getUsername() {
		return username != null ? username : "";
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getCatalogId() {
		return catalogId != null ? catalogId : "";
	}

	public void setCatalogId(String catalogId) {
		this.catalogId = catalogId;
	}

	public String getCatalogName() {
		return catalogName != null ? catalogName : "";
	}

	public void setCatalogName(String catalogName) {
		this.catalogName = catalogName;
	}

	public String getTitle() {
		return title != null ? title : "";
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return description != null ? description : "";
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getWho() {
		return who != null ? who : "" ;
	}

	public void setWho(String who) {
		this.who = who;
	}

	public String getWhat() {
		return what != null ? what : "";
	}

	public void setWhat(String what) {
		this.what = what;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public String getPlace() {
		return place;
	}

	public void setPlace(String place) {
		this.place = place;
	}

	public String getEvent() {
		return event != null ? event : "";
	}

	public void setEvent(String event) {
		this.event = event;
	}

	public String getGps() {
		return gps != null ? gps : "0,0";
	}

	public void setGps(String gps) {
		this.gps = gps;
	}
	
	public void reset(){
		
	}

	@Override
	public boolean equals(Object meta) {
		if(meta instanceof Metadata){
			return ((Metadata)meta).getTransferUID().equals(transferUID);
		} else {
			return false ;
		}
	}
}
