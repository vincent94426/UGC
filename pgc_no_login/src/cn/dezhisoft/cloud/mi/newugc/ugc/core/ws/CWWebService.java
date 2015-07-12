package cn.dezhisoft.cloud.mi.newugc.ugc.core.ws;

import android.util.Log;

import cn.dezhisoft.cloud.mi.newugc.ioffer.common.Config;

import com.sobey.sdk.base.HttpVisitor;

/**
 * UGC 网络访问和站点管理
 * 
 * @author Rosson Chen
 *
 */
public abstract class CWWebService {
	
	/** 网络访问线程管理 */
	protected static final HttpVisitor HttpService	= new HttpVisitor() ;
	/** UGC平台: 用户管理  */
	public final static CWIUserProxy UserProxy = new CWUserProxyImpl(HttpService);
	/** 站点id*/
	private static String  SiteId		= "" ;
	/** 客户端服务器地址 */
	private static String  CLIENT_URL	= "" ;

	/** 用户信息 */
	private static String userName 			= "" ;
	private static String userPassword 		= "" ;
	private static String sessionUID		= "" ;
	
	protected CWWebService(){
		
	}
	
	/** debug 模式 */
	public static void setDebug(boolean debug){
		CWBaseSoapImpl.DEBUG	= debug ;
	}
	
	/** 设置站点id */
	public final static void setSiteId(String siteId) {
		
		if(siteId == null || siteId.equals("")){
			Log.e("UGCWebService", "无效Site id");
			return ;
		}
		
		SiteId	= siteId ;
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
		
		userName		= uname ;
		userPassword	= upwd ;
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
		
		sessionUID		= session ;
		UserProxy.getUser().setSessionUID(sessionUID);
	}
	
	public static String getUserName() {
		return userName;
	}

	public static String getUserPassword() {
		return userPassword;
	}

	public static String getSessionUID() {
		return sessionUID;
	}
}
