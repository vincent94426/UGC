package cn.dezhisoft.cloud.mi.newugc.ugc.ws;

import java.util.ArrayList;

import cn.dezhisoft.cloud.mi.newugc.ugc.model.Advert;
import cn.dezhisoft.cloud.mi.newugc.ugc.model.Category;
import cn.dezhisoft.cloud.mi.newugc.ugc.model.Site;


/**
 * 站点管理. 站点分类，广告等
 * 
 * @author Rosson Chen
 * 
 */
public interface ISiteProxy {
	
	// 查询所有站点信息,查询的信息会填充到list中
	public void querySiteInfo(SoapResponse response,ArrayList<Site> list) ;
	
	// 查询指定站点分类
	public void querySiteCategory(String session,String siteId, SoapResponse response,ArrayList<Category> list);
	
	// 查询指定站点广告
	public void querySiteAdverts(String siteId,int number,SoapResponse response,ArrayList<Advert> list);
}
