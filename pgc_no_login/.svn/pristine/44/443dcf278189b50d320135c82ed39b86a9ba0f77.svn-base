package cn.dezhisoft.cloud.mi.newugc.ugc.ws;

import android.util.Log;

import com.sobey.sdk.base.HttpVisitor;

import cn.dezhisoft.cloud.mi.newugc.ioffer.common.Config;
import cn.dezhisoft.cloud.mi.newugc.ugc.core.ws.CWBaseSoapImpl;
import cn.dezhisoft.cloud.mi.newugc.ugc.core.ws.CWWebService;

/**
 * UGC 网络访问和站点管理
 * 
 * @author Rosson Chen
 *
 */
public abstract class UGCWebService {
	/** 网络访问线程管理 */
	protected static final HttpVisitor HttpService	= new HttpVisitor() ;
	/** UGC平台: 站点管理 */
	public final static ISiteProxy SiteProxy = new SiteProxyImpl(HttpService);
	/** UGC平台: 客户端版本管理  */
	public final static IClientVersionProxy VersionProxy = new ClientVersionProxy(HttpService);
	/** 站点id*/
	private static String  SiteId		= "" ;
	/** 客户端服务器地址 */
	private static String  CLIENT_URL	= "" ;

	protected UGCWebService(){
		
	}
	
	/** debug 模式 */
	public static void setDebug(boolean debug){
		AbstractSoapImpl.DEBUG	= debug ;
		
		CWBaseSoapImpl.DEBUG	= debug ;
	}
	
	/** 设置站点id */
	public final static void setSiteId(String siteId) {
		
		if(siteId == null || siteId.equals("")){
			Log.e("UGCWebService", "无效Site id");
			return ;
		}
		
		SiteId	= siteId ;
		
		CWWebService.setSiteId(siteId);
	}
	
	/** 得到站点id */
	public final static String getSiteId() {
		return SiteId;
	}
	
	/**
	 * 初始化 服务器地址
	 * @param host			: 服务器地址
	 */
	public static final void initClientHost(String host) {
		
		if(host == null || host.equals("")){
			Log.e("UGCWebService", "无效Host");
			return ;
		}
		
		//
		CLIENT_URL	= "http://" + host + Config.WS_CLIENT_SUFFIX ;
	}

	public final static String getClientHost() {
		return CLIENT_URL;
	}
	
	/**
	 * 初始登录用户信息.
	 * 
	 * @param uname			: 用户名
	 * @param upwd			: 密码
	 */
	public static final void initClientUser(String uname,String upwd){
		
		if(uname == null || uname.equals("")){
			Log.e("UGCWebService", "无效用户名");
			return ;
		}
		
		if(upwd == null || upwd.equals("")){
			Log.e("UGCWebService", "无效密码");
			return ;
		}
		
		CWWebService.initClientUser(uname, upwd) ;
	}
	
	/**
	 * 设置用户 session
	 * 
	 * @param session
	 */
	public static final void initClientUser(String session){
		if(session == null || session.equals("")){
			Log.e("UGCWebService", "无效session");
			return ;
		}
		
		CWWebService.initClientUser(session);
	}
	
	public static String getUserName() {
		return CWWebService.getUserName();
	}

	public static String getUserPassword() {
		return CWWebService.getUserPassword();
	}

	public static String getSessionUID() {
		return CWWebService.getSessionUID();
	}
}
