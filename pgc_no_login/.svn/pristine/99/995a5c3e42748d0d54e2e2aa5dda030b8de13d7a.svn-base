package cn.dezhisoft.cloud.mi.newugc.ugc.model;

import java.util.ArrayList;

public final class PackageType implements Cloneable {

	private String packageGuid ;
	private String catalogId;
	private String title;
	private String createTime;
	private String deviceType;
	private MaterialStatus status;
	private int commentCount;
	private double commentRating;
	private int clickCount;
	private int downloadCount;
	private String iconUrl;
	private String describe ;
	private String userName ;
	
	private final ArrayList<Material> Materials = new ArrayList<Material>() ;
	
	private final PackagePage RelatedPackage = new PackagePage() ;
	
	private final Comment comment = new Comment() ;
	
	public PackageType(){
		clear() ;
	}
	
	public Comment getComment() {
		return comment;
	}

	public String getPackageGuid() {
		return packageGuid;
	}

	public String getDescribe() {
		return describe;
	}

	public void setDescribe(String describe) {
		this.describe = describe;
	}

	public void setPackageGuid(String packageGuid) {
		this.packageGuid = packageGuid;
	}

	public String getCatalogId() {
		return catalogId;
	}

	public void setCatalogId(String catalogId) {
		this.catalogId = catalogId;
	}

	public String getTitle() {
		return title;
	}

	public PackagePage getRelatedPackage() {
		return RelatedPackage;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
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

	public int getCommentCount() {
		return commentCount;
	}

	public void setCommentCount(int commentCount) {
		this.commentCount = commentCount;
	}

	public double getCommentRating() {
		return commentRating;
	}

	public void setCommentRating(double commentRating) {
		this.commentRating = commentRating;
	}

	public int getClickCount() {
		return clickCount;
	}

	public void setClickCount(int clickCount) {
		this.clickCount = clickCount;
	}

	public ArrayList<Material> getMaterials() {
		return Materials;
	}

	public int getDownloadCount() {
		return downloadCount;
	}

	public void setDownloadCount(int downloadCount) {
		this.downloadCount = downloadCount;
	}

	public String getIconUrl() {
		return iconUrl;
	}

	public void setIconUrl(String iconUrl) {
		this.iconUrl = iconUrl;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	private final void clear(){
		packageGuid = catalogId = title = deviceType = "" ; 
		status = null ;
		commentCount = 0 ;
		commentRating = 0.0 ;
		clickCount = 0 ;
		downloadCount = 0 ;
		iconUrl = "" ;
		describe = "" ;
		userName = "" ;
		RelatedPackage.clear() ;
		Materials.clear() ;
	}
	
	public final PackageType cloneContentType(){
		try{
			PackageType content = (PackageType)super.clone() ;
			if(content != null) content.clear() ;
			return content != null ? content : new PackageType() ;
		}catch(CloneNotSupportedException e){
			return null ;
		}
	}
}
