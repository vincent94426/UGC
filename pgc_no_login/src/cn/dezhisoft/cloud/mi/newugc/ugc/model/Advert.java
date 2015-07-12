package cn.dezhisoft.cloud.mi.newugc.ugc.model;

/**
 * 广告信息
 * @author Rosson Chen
 *
 */
public class Advert {

	/** 广告提示信息*/
	public String tips ;
	
	/** 连接地址*/
	public String linkUrl ;

	public String getTips() {
		return tips;
	}

	public void setTips(String tips) {
		this.tips = tips;
	}

	public String getLinkUrl() {
		return linkUrl != null && !linkUrl.equals("") ? linkUrl : "http://www.sobey.com/";
	}

	public void setLinkUrl(String linkUrl) {
		this.linkUrl = linkUrl;
	}
}
