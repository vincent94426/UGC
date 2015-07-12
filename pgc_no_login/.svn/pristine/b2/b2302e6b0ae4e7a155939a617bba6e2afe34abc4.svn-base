package cn.dezhisoft.cloud.mi.newugc.ioffer.common;

import cn.dezhisoft.cloud.mi.newugc.ioffer.config.AppConfig;
import cn.dezhisoft.cloud.mi.newugc.ioffer.service.IofferService;
import cn.dezhisoft.cloud.mi.newugc.ugc.core.config.BaseConfig;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * 基本常量
 * 
 * @author Rosson Chen
 *
 */
public final class Config extends BaseConfig {
	
	/** 首页*/
	public static final int FIRST_PAGE = 0 ;
	/** 关闭 */
	public static final int FLGA_BUTTON_OFF		= 0 ;
	/** 打开 */
	public static final int FLGA_BUTTON_ON		= 1 ;
	
	/** 默认地址/clientService?wsdl */
	// 云平台: http://ugcm.sob-newstips.cn:9418
    // 深圳现场地址:119.145.39.58:8098/tpp/ws
    // 外网地址:113.142.30.248/tpp/ws
    // 内网地址:172.16.174.111
	public static final String DEFAULT_HOST	= "113.142.30.248/tpp/ws";//"172.18.2.51:8080/tpp/ws";//"113.142.30.93:8080/ugc";//"192.168.1.24:9418/ugc";//"ugcm.sob-newstips.cn:9418/ugc" ;
	// 默认站点
	public static final String DEFAULT_SITE	= "275";//"275" ;
	
	/** name space*/
	public static final String WS_NAMESPACE 	     = "http://www.sobey.com/ugc" ;
	/** 客户端管理后缀*/
	public static final String WS_CLIENT_SUFFIX	 = "/clientService" ;
	/** 发布接口后缀*/
	public static final String WS_PUBLISH_SUFFIX   = "/contentService" ;
	
	
	private static final String SP_NAME			= "_com_sobey_iugc" ;
	public static final String KEY_HOST 		= "_host" ;
	public static final String KEY_SITE 		= "_site" ;
	public static final String KEY_SESSION	 	= "_session" ;
	
	private static Context mContext ;
	
	public static void initContext(Context context){
		mContext = context ;
	}
	
	public static Context getContext(){
		return mContext ;
	}
	
	/** 临时数据 */
	public static final SharedPreferences getSharedPreferences(Context context){
		return context.getSharedPreferences(SP_NAME, Context.MODE_PRIVATE); 
	}
	
	/** 数据库访问 */
	public static final AccessDatabase getAccessDatabase(Context context){
		AccessDatabase accessDatabase = AccessDatabase.getAccessDatabase() ;
		if(accessDatabase == null){
			AccessDatabase.initAccessDatabase(context);
			return AccessDatabase.getAccessDatabase() ;
		} else {
			return accessDatabase ;
		}
	}
	
	/** 检测Activity状态, Android4.0 以后增加了停止活动选项; 当应用退出到后台时,活动被关闭 */
	public static final void checkActivityStatus(Context context){
		//
		final IofferService mIofferService = IofferService.getNewTipService();
		// 检测临时数据
		final AccessDatabase db = Config.getAccessDatabase(context);
		final AppConfig config = db.getConfig() ;
		String host = config.getHost() ;
		String site = config.getSiteId() ;
		String usession = IofferService.getNewTipService().getUser().getSessionUID() ;
		
		// 业务层的服务器地址
		String phost = mIofferService.getPublishHost() ;
		String chost = mIofferService.getClientHost() ;
		String csite = mIofferService.getSiteId() ;
		final SharedPreferences settings = Config.getSharedPreferences(context); 
		String session 	= settings.getString(Config.KEY_SESSION, "");
		
		// 重新设置地址
		if((phost == null || phost.trim().equals("")) 
				|| (chost == null || chost.trim().equals(""))){
			mIofferService.setUGCHost(host);
		}
		
		// 重新设置站点
		if(csite == null || csite.trim().equals("")){
			mIofferService.setSiteId(site);
		}
		
		// 重新设置session
		if (usession == null || usession.trim().equals("")) {
			// 清除临时数据
			mIofferService.reset() ;
			// 设置session
			mIofferService.getUser().setSessionUID(session);
		}
	}
}
