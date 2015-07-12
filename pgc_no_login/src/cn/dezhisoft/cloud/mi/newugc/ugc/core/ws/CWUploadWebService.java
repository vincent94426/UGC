package cn.dezhisoft.cloud.mi.newugc.ugc.core.ws;

import java.net.Inet6Address;
import java.net.InetAddress;


/**
 * UGC 平台上传管理服务代理.
 * 本类提供了与UGC平台上传web服务器的交互功能,如下:
 * 1. 登录
 * 2. 注销
 * 3. 站点查询
 * 4. 上传直播
 * 5. 上传文件 (TCP || FTP)
 * 
 * @author Rosson Chen
 *
 */
public final class CWUploadWebService extends CWWebService {
	
	protected static final String TAG		= "UGCUploadWebService" ;
	
	/** UGC 通道管理 */
	public final static CWIChannelProxy ChannelProxy	= new CWChannelProxyImpl(HttpService);
	
	private CWUploadWebService(){
		
	}
	
	/**
	 * 解析服务器Host地址
	 * 
	 * @param hostName		: 主机地址
	 * @return
	 */
	public static final String parserHostIpAddress(String hostName){
		try{
			InetAddress ipaddress = Inet6Address.getByName(hostName);
			String sip 			  = ipaddress.getHostAddress() ;
			if(sip != null && !sip.equals(""))
				return sip ;
		} catch(Exception e){
			e.printStackTrace() ;
		}
		return null ;
	}
}
