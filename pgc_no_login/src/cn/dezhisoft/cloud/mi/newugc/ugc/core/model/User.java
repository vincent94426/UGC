package cn.dezhisoft.cloud.mi.newugc.ugc.core.model;

import java.util.ArrayList;

/**
 * UGC System register user
 * 
 * @author Rosson Chen
 * 
 */
public final class User {

	/** id */
	private String id = "-1";

	/** register user name */
	private String name = "";
	
	/** register nick name */
	private String nickName = "";

	/** register user password */
	private String pwd = "";

	/** register user email */
	private String email = "";

	/** register user phone */
	private String phoneNumber = "";
	
	/** UGC System site id */
	private String siteId ;

	/** session id */
	private String sessionUID = "";
	
	/** register user role */
	private String userRole = "";
	
	/** register user icon: base64 code */
	private String iconBase64 ;
	
	/** user last login time */
	private String lastLoginTime ;
	
	/** approved user roles */
	private ArrayList<String> approvedLoginRole = new ArrayList<String>();
	
	/** approved user name upload stream type*/
	private ArrayList<String> approvedStreamType = new ArrayList<String>();
	
	/** approved user name upload catalog type*/
	private ArrayList<String> approvedCatalogType = new ArrayList<String>();
	
	/** register user icon url */
	private String avatarsUrl ;
	
	/** register user score */
	private int score ;
	
	private String customFieldForm;
	
	public String getCustomFieldForm() {
		return customFieldForm;
	}

	public void setCustomFieldForm(String customFieldForm) {
		this.customFieldForm = customFieldForm;
	}

	public String getNickName() {
		return nickName;
	}

	public void setNickName(String nickName) {
		this.nickName = nickName;
	}
	
	public int getScore() {
		return score;
	}

	public void setScore(int score) {
		this.score = score;
	}

	public String getSiteId() {
		return siteId != null ? siteId : "1";
	}

	public void setSiteId(String siteId) {
		this.siteId = siteId;
	}

	public String getAvatarsUrl() {
		return avatarsUrl;
	}

	public void setAvatarsUrl(String avatarsUrl) {
		this.avatarsUrl = avatarsUrl;
	}

	/**
	 * 娣诲姞瑙掕壊
	 * @param role
	 */
	public void addApprovedLoginRole(String role){
		approvedLoginRole.add(role);
	}
	
	public ArrayList<String> getApprovedLoginRole() {
		return approvedLoginRole;
	}

	/**
	 * 娣诲姞鍒嗙被
	 * @param catalog
	 */
	public void addApprovedCatalogType(String catalog){
		approvedCatalogType.add(catalog);
	}
	
	public ArrayList<String> getApprovedCatalogType() {
		return approvedCatalogType;
	}

	public String getLastLoginTime() {
		return lastLoginTime;
	}

	public void setLastLoginTime(String lastLoginTime) {
		this.lastLoginTime = lastLoginTime;
	}

	public ArrayList<String> getApprovedStreamType() {
		return approvedStreamType;
	}

	/**
	 * 娣诲姞娴佺被鍨�
	 * @param type
	 */
	public void addApprovedStreamType(String type){
		approvedStreamType.add(type);
	}
	
	public String getUserRole() {
		return userRole;
	}

	public void setUserRole(String userRole) {
		this.userRole = userRole;
	}

	public void setId(String id) {
		this.id = id;
	}

	public void setUsername(String user) {
		this.name = user;
	}

	public void setPassword(String pwd) {
		this.pwd = pwd;
	}

	public void setSessionUID(String sessionUID) {
		this.sessionUID = sessionUID;
	}

	public String getId() {
		return id;
	}

	public String getUsername() {
		return name;
	}

	public String getPassword() {
		return pwd;
	}

	public String getSessionUID() {
		return sessionUID != null ? sessionUID : "";
	}

	public String getEmail() {
		return email != null ? email : "";
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPhoneNumber() {
		return phoneNumber != null ? phoneNumber : "";
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}
	
	public String getIconBase64() {
		return iconBase64;
	}

	public void setIconBase64(String iconBase64) {
		this.iconBase64 = iconBase64;
	}

	public void clear(){
		id = "-1" ;
		name = pwd = email = phoneNumber = sessionUID = userRole = "" ;
	}
}
