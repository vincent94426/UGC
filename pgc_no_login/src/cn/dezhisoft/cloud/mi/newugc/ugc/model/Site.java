package cn.dezhisoft.cloud.mi.newugc.ugc.model;

/**
 * 站点信息
 * 
 * @author Rosson Chen
 *
 */
public class Site {

	/** 站点id */
	private String siteId ;
	
	/** 站点名称 */
	private String siteName ;
	
	/** 站点描述 */
	private String description ;

	public String getSiteId() {
		return siteId;
	}

	public void setSiteId(String siteId) {
		this.siteId = siteId;
	}

	public String getSiteName() {
		return siteName;
	}

	public void setSiteName(String siteName) {
		this.siteName = siteName;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
}
