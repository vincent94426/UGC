package cn.dezhisoft.cloud.mi.newugc.ugc.ws.publish;

import android.util.Log;

import cn.dezhisoft.cloud.mi.newugc.ioffer.common.Config;
import cn.dezhisoft.cloud.mi.newugc.ugc.core.ws.CWUploadWebService;
import cn.dezhisoft.cloud.mi.newugc.ugc.ws.IDownResource;
import cn.dezhisoft.cloud.mi.newugc.ugc.ws.IPublishProxy;
import cn.dezhisoft.cloud.mi.newugc.ugc.ws.UGCWebService;

/**
 * UGC 内容发布管理服务代理. 本类实现UGC服务器端发布内容交互 
 * 1. 广告查询 
 * 2. 分类查询 (热门|排行|类别) 
 * 3. 评论查询 4. 添加评论
 * 
 * @author Rosson Chen
 * 
 */
public final class UGCContentWebService extends UGCWebService {
	
	static final String TAG	= "UGCContentWebService" ;

	/** 发布服务器地址 */
	private static String PublishURL 				= "" ;
	/** 发布管理 */
	public static final IPublishProxy PublishProxy 	= new PublishProxyImpl(HttpService);
	/** 资源下载管理  */
	public static final IDownResource Downloader	= new DownResourceImpl() ;
	
	/**
	 * 初始化内容管理服务器
	 * 
	 * @param host		: 服务器地址
	 */
	public final static void initPublishHost(String host){
		
		if(host == null || host.equals("")){
			Log.e(TAG, "无效Host");
			return ;
		}
		
		//
		PublishURL	= "http://" + host + Config.WS_PUBLISH_SUFFIX ;
	}
	
	public static final String getPulishHost(){
		return PublishURL ;
	}
}
