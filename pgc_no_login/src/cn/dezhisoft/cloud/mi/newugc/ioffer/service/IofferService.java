package cn.dezhisoft.cloud.mi.newugc.ioffer.service;

import java.util.ArrayList;
import java.util.Stack;

import android.os.Handler;

import cn.dezhisoft.cloud.mi.newugc.ioffer.upload.ui.UGCBaseActivity;
import cn.dezhisoft.cloud.mi.newugc.ugc.core.model.MediaType;
import cn.dezhisoft.cloud.mi.newugc.ugc.core.model.User;
import cn.dezhisoft.cloud.mi.newugc.ugc.core.ws.CWUploadWebService;
import cn.dezhisoft.cloud.mi.newugc.ugc.core.ws.CWWebService;
import cn.dezhisoft.cloud.mi.newugc.ugc.model.Advert;
import cn.dezhisoft.cloud.mi.newugc.ugc.model.Category;
import cn.dezhisoft.cloud.mi.newugc.ugc.model.CommentType;
import cn.dezhisoft.cloud.mi.newugc.ugc.model.OrderBy;
import cn.dezhisoft.cloud.mi.newugc.ugc.model.PackageType;
import cn.dezhisoft.cloud.mi.newugc.ugc.model.Site;
import cn.dezhisoft.cloud.mi.newugc.ugc.model.MaterialStatus;
import cn.dezhisoft.cloud.mi.newugc.ugc.version.VersionInfo;
import cn.dezhisoft.cloud.mi.newugc.ugc.ws.IDownResource;
import cn.dezhisoft.cloud.mi.newugc.ugc.ws.IPublishProxy;
import cn.dezhisoft.cloud.mi.newugc.ugc.ws.SoapResponse;
import cn.dezhisoft.cloud.mi.newugc.ugc.ws.UGCWebService;
import cn.dezhisoft.cloud.mi.newugc.ugc.ws.publish.UGCContentWebService;

/**
 * Ioffer5.0 业务逻辑层
 * 
 * @author Rosson Chen
 *
 */
public final class IofferService implements IofferDefine {
	
	//
	private static IofferService intance ;
	
	/** 搜索分类*/
	public final Category CategorySearch = new Category("搜索",OrderBy.NEWEST);
	/** 热门分类*/
	public final Category CategoryHot 	= new Category("热门",OrderBy.HOTSPOT);
	/** 点击分类*/
	public final Category CategoryClick = new Category("点击率",OrderBy.CLICKRATE);
	/** 视频分类 */
	public final Category CategoryVideo = new Category("视频",OrderBy.NEWEST,MediaType.VIDEO);
	/** 音频分类 */
	public final Category CategoryAudio = new Category("音频",OrderBy.NEWEST,MediaType.AUDIO);
	/** 图片分类 */  
	public final Category CategoryImage = new Category("图文",OrderBy.NEWEST,MediaType.IMAGE);
	/** Catalog 分类栈*/
	private final Stack<Category> CatalogStack = new Stack<Category>();
	
	/** 当前素材*/
	private PackageType contentType ;
	/** 当前相关素材*/
	private PackageType contentTypeRelated ;
	
	private final ArrayList<Site>  SiteList = new ArrayList<Site>();
	private final ArrayList<Advert>  AdvertList  = new ArrayList<Advert>();
	
	private IofferService(){}
	
	public static IofferService getNewTipService(){
		if(intance == null) intance = new IofferService() ;
		return intance ;
	}
	
	public void setSiteId(String siteId){
		//UGCWebService.setDebug(true) ;
		UGCWebService.setSiteId(siteId);
	}
	
	public void setUGCHost(String host){
		CWUploadWebService.initClientHost(host);
		UGCContentWebService.initPublishHost(host);
	}
	
	/** 内容汇聚地址 */
	public String getPublishHost(){
		return UGCContentWebService.getPulishHost() ;
	}
	
	/** 客户端接口地址 */
	public String getClientHost(){
		return CWUploadWebService.getClientHost() ;
	}
	
	/** 站点id */
	public String getSiteId(){
		return UGCWebService.getSiteId() ;
	}
	
	// 下载管理
	public IDownResource getDownloader(){
		return UGCContentWebService.Downloader ;
	}
	
	// 查询站点
	public void querySiteInfo(Handler handler){
		
		SiteList.clear() ;
		
		UGCWebService.SiteProxy.querySiteInfo(new SoapResponse(handler){

			@Override
			public Object dispatchResponse(Object value) {
				if(value instanceof ArrayList<?>){
					sendMessage(MSG_QUERY_SITE_SUCCESS) ;
				} else {
					sendMessage(MSG_QUERY_SITE_FAILED) ;
				}
				return null ;
			}
			
		},SiteList);
	}
	
	// 用户注册
	public void userRegister(User user, cn.dezhisoft.cloud.mi.newugc.ugc.core.ws.CWSoapResponse response) {
		
		CWWebService.UserProxy.userRegister(user, new cn.dezhisoft.cloud.mi.newugc.ugc.core.ws.CWSoapResponse(response){

			@Override
			public Object dispatchResponse(Object value) {
				if(value instanceof User){
					sendMessage(MSG_USER_REGISTER_SUCCESS) ;
				} else {
					sendMessage(MSG_USER_REGISTER_FAILED,value) ;
				}
				return null ;
			}
			
		});
	}

	// 用户登录
	public void login(String uname,String upwd,cn.dezhisoft.cloud.mi.newugc.ugc.core.ws.CWSoapResponse response){
		
		CWWebService.UserProxy.login(uname, upwd, new cn.dezhisoft.cloud.mi.newugc.ugc.core.ws.CWSoapResponse(response){

			@Override
			public Object dispatchResponse(Object value) {
				if(value instanceof User){
					sendMessage(MSG_USER_LOGIN_SUCCESS) ;
				} else {
					sendMessage(MSG_USER_LOGIN_FAILED,value) ;
				}
				return null ;
			}
			
		});
	}
	
	// 查询用户详细信息
	public void userEnum(cn.dezhisoft.cloud.mi.newugc.ugc.core.ws.CWSoapResponse response){
		
		CWWebService.UserProxy.userEnum(new cn.dezhisoft.cloud.mi.newugc.ugc.core.ws.CWSoapResponse(response){

			@Override
			public Object dispatchResponse(Object value) {
				
				if(value instanceof User){
					sendMessage(MSG_USER_ENUM_SUCCESS);
				} else {
					sendMessage(MSG_USER_ENUM_FAILED);
				}
				return null ;
			}
			
		});
	}
	
	// 更新用户信息
	public void userUpdate(User user, cn.dezhisoft.cloud.mi.newugc.ugc.core.ws.CWSoapResponse response) {
		
		CWWebService.UserProxy.userUpdate(user,new cn.dezhisoft.cloud.mi.newugc.ugc.core.ws.CWSoapResponse(response){

			@Override
			public Object dispatchResponse(Object value) {
				
				if(value instanceof User){
					sendMessage(MSG_USER_MODIFY_SUCCESS);
				} else {
					sendMessage(MSG_USER_MODIFY_FAILED);
				}
				
				return null ;
			}
			
		}) ;
	}
	
	// 用户注销
	public void logout(cn.dezhisoft.cloud.mi.newugc.ugc.core.ws.CWSoapResponse response){
		
		CWWebService.UserProxy.logout(new cn.dezhisoft.cloud.mi.newugc.ugc.core.ws.CWSoapResponse(response){

			@Override
			public Object dispatchResponse(Object value) {
				
				sendMessage(MSG_USER_LOGOUT_SUCCESS);
				
				getUser().clear() ;
				
				return null ;
			}
			
		}) ;
	}
	
	public User getUser(){
		return CWWebService.UserProxy.getUser() ;
	}
	
	public void queryPackage(Category category,int pageIndex,SoapResponse response){
		
		final IPublishProxy publish = UGCContentWebService.PublishProxy ; 
		final String session 	= CWWebService.UserProxy.getUser().getSessionUID() ;
		final String siteID 	= CWWebService.UserProxy.getUser().getSiteId() ;
		
		publish.queryPackage(siteID,session,"", "", MaterialStatus.PUBLISH,pageIndex,category, new SoapResponse(response){

			@Override
			public Object dispatchResponse(Object value) {
				if(value instanceof Category){
					sendMessage(MSG_QUERY_CATEGORY_SUCCESS) ;
				} else{
					sendMessage(MSG_QUERY_CATEGORY_FAILED,value) ;
				}
				return null ;
			}
			
		});
	}
	
	public void queryMaterial(PackageType contentType,SoapResponse response){
		
		final IPublishProxy publish = UGCContentWebService.PublishProxy ; 
		final String session 	= CWWebService.UserProxy.getUser().getSessionUID() ;
		final String siteID 	= CWWebService.UserProxy.getUser().getSiteId() ;
		
		publish.queryMaterial(siteID,session,"",MediaType.ALL,MaterialStatus.PUBLISH,0,contentType, new SoapResponse(response){

			@Override
			public Object dispatchResponse(Object value) {
				if(value instanceof PackageType){
					sendMessage(MSG_QUERY_CONTENT_DETAIL_SUCCESS) ;
				} else{
					sendMessage(MSG_QUERY_CONTENT_DETAIL_FAILED,value) ;
				}
				return null ;
			}
			
		});
	}
	
	public void queryComments(PackageType contentType,int pageNumber,SoapResponse response){
		
		final IPublishProxy publish = UGCContentWebService.PublishProxy ; 
		final String session 	= CWWebService.UserProxy.getUser().getSessionUID() ;
		
		publish.queryComments(session,contentType, pageNumber, new SoapResponse(response){

			@Override
			public Object dispatchResponse(Object value) {
				if(value instanceof PackageType){
					sendMessage(MSG_QUERY_CONTENT_COMMENT_SUCCESS) ;
				} else{
					sendMessage(MSG_QUERY_CONTENT_COMMENT_FAILED,value) ;
				}
				return null ;
			}
			
		});
	}
	
	public void queryRelatedContentType(PackageType contentType,int pageIndex,SoapResponse response){
		
		final IPublishProxy publish = UGCContentWebService.PublishProxy ; 
		final String session = CWWebService.UserProxy.getUser().getSessionUID() ;
		
		publish.queryRelatedPackageType(contentType,session, pageIndex, new SoapResponse(response){

			@Override
			public Object dispatchResponse(Object value) {
				
				if(value instanceof PackageType){
					sendMessage(MSG_QUERY_RELATED_CONTENT_SUCCESS) ;
				} else{
					sendMessage(MSG_QUERY_RELATED_CONTENT_FAILED,value) ;
				}
				
				return value ;
			}
			
		});
	}
	
	public void addCommentType(String packageGuid,CommentType comment,SoapResponse response){
		
		final IPublishProxy publish = UGCContentWebService.PublishProxy ; 
		final String session = CWWebService.UserProxy.getUser().getSessionUID() ;
		
		publish.addComment(session, packageGuid,comment, new SoapResponse(response){

			@Override
			public Object dispatchResponse(Object value) {
				
				if(value instanceof Boolean){
					if((Boolean)value){
						sendMessage(MSG_ADD_CONTENT_COMMENT_SUCCESS) ;
					}
				} else {
					sendMessage(MSG_ADD_CONTENT_COMMENT_FAILED,value) ;
				}
				
				return value ;
			}
			
		});
	}
	
	/** 下载统计 */
	public void addDownloadStatistics(String packageGuid,String materialGuid, SoapResponse response){
		
		final IPublishProxy publish = UGCContentWebService.PublishProxy ; 
		
		publish.addDownloadStatistics(packageGuid, materialGuid, new SoapResponse(response){

			@Override
			public Object dispatchResponse(Object value) {
				
				if(value instanceof Boolean){
					
				} else {
					
				}
				
				return value ;
			}
			
		});
		
	}
	
	/** 点击统计 */
	public void addClickStatistics(String packageGuid,String materialGuid, SoapResponse response){
		
		final IPublishProxy publish = UGCContentWebService.PublishProxy ; 
		
		publish.addClickStatistics(packageGuid, materialGuid, new SoapResponse(response){

			@Override
			public Object dispatchResponse(Object value) {
				
				if(value instanceof Boolean){
					
				} else {
					
				}
				
				return value ;
			}
			
		});
		
	}
	
	public void searchContentType(int pageIndex,String key,SoapResponse response){
		
		final IPublishProxy publish = UGCContentWebService.PublishProxy ; 
		final String session 	= CWWebService.UserProxy.getUser().getSessionUID() ;
		final String siteID 	= CWWebService.UserProxy.getUser().getSiteId() ;
		
		publish.searchPackage(siteID,session, key,CategorySearch, pageIndex, new SoapResponse(response){

			@Override
			public Object dispatchResponse(Object value) {
				if(value instanceof Category){
					sendMessage(MSG_SEARCH_CONTENT_SUCCESS) ;
				} else {
					sendMessage(MSG_SEARCH_CONTENT_FAILED,value) ;
				}
				return null ;
			}
			
		}) ;
	}
	
	/** 查询版本更新 */
	public void queryVersionUpdate(String url,String siteId,String versionNumber, SoapResponse response) {
		
		UGCWebService.VersionProxy.queryVersionUpdate(url,siteId, versionNumber,
				new SoapResponse(response) {
			
					@Override
					public Object dispatchResponse(Object value) {
						if (value instanceof VersionInfo) {
							sendMessage(MSG_QUERY_VERSION_SUCCESS,value);
						} else {
							sendMessage(MSG_QUERY_VERSION_FAILED,value);
						}
						return null;
					}
				});
	}

	public void setCurrentCategory(Category category) {
		if(category != null && getCurrentCategory() != category){
			CatalogStack.push(category);
		}
	}
	
	public final Category getCurrentCategory() {
		return CatalogStack.size() > 0 ? CatalogStack.peek() : null;
	}

	public final void previousCategory() {
		if(CatalogStack.size() > 0)
			CatalogStack.pop() ;
	}
	
	public void setCurrentPackageType(PackageType content){
		contentType	= content ;
	}
	
	public PackageType getContentTypeRelated() {
		return contentTypeRelated;
	}

	public void setContentTypeRelated(PackageType contentTypeRelated) {
		this.contentTypeRelated = contentTypeRelated;
	}

	public PackageType getCurrentPackageType() {
		return contentType;
	}
	
	public ArrayList<Site> getSiteList() {
		
		ArrayList<Site> temp = new ArrayList<Site>() ;
		
		for(Site site : SiteList){
			String name = site.getSiteName().toLowerCase() ;
			if(!name.equals("system")){
				temp.add(site);
			}
		}
		return temp;
	}
	
	public ArrayList<Advert> getAdverts() {
		return AdvertList;
	}
	
	/** 清楚所有数据 */
	public void reset(){
		getUser().clear() ;
		CatalogStack.clear() ;
		SiteList.clear() ;
		AdvertList.clear() ;
		CategorySearch.clear() ;
		CategorySearch.getPackageTypes().clear() ;
		CategoryHot.clear() ;
		CategoryHot.getPackageTypes().clear() ;
		CategoryClick.clear() ;
		CategoryClick.getPackageTypes().clear() ;
		CategoryVideo.clear() ;
		CategoryVideo.getPackageTypes().clear() ;
		CategoryAudio.clear() ;
		CategoryAudio.getPackageTypes().clear() ;
		CategoryImage.clear() ;
		CategoryImage.getPackageTypes().clear() ;
		
		UGCBaseActivity.clearSiteCategory() ;
	}
}
