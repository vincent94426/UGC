package cn.dezhisoft.cloud.mi.newugc.ugc.ws;

import java.io.InputStream;

import org.ksoap2.serialization.SoapObject;

import android.util.Log;

import com.sobey.sdk.base.HttpVisitor;
import com.sobey.sdk.base.IResponse;
import com.sobey.sdk.base.Request;

import cn.dezhisoft.cloud.mi.newugc.ioffer.common.Config;
import cn.dezhisoft.cloud.mi.newugc.ugc.version.VersionInfo;

/**
 * 版本管理实现
 * 
 * @author Rosson Chen
 *
 */
public final class ClientVersionProxy extends AbstractSoapImpl implements IClientVersionProxy {

	static final String TAG = "ClientVersionProxy" ;
	
	protected ClientVersionProxy(HttpVisitor service){
		super(service);
	}
	
	@Override
	public void queryVersionUpdate(String url ,String siteId, String versionName,SoapResponse response) {
		
		if(response == null || url == null || url.equals("")){
			Log.e(TAG, "ERROR: queryVersionUpdate() response is null");
			return ; 
		}
		//
		String _url = "http://" + url + Config.WS_CLIENT_SUFFIX ;
		
		httpService.sendSoapRequest(requestVersionInfo(_url,siteId,versionName),response, responseVersionInfo());
	}
	
	private final Request requestVersionInfo(final String url ,final String siteid,final String versionName){
		return new Request(Request.RESULT_TYPE_OBJECT) {
			
			@Override
			public InputStream getResquestInputStream() throws Exception {
				
				final SoapObject request = new SoapObject(NAMESPACE, "CheckVersion");
				request.addProperty("SiteID", siteid);
				request.addProperty("ClientDevice", "Android");
				request.addProperty("VersionNumber", versionName);
				
				final String action = NAMESPACE + "/" + "CheckVersion" ;
				
				return sendSoapRequest(url,action,request);
			}
		};
	}
	
	private final IResponse responseVersionInfo(){
		return new IResponse() {
			
			@Override
			public Object wrapperObject(InputStream input) throws Exception {
				// debug
				debugResponse(input) ;
				// 解析
				final AbstractXmlParser xmlParser = new AbstractXmlParser(input) {

					VersionInfo info = new VersionInfo();
					boolean update = false ;
					
					@Override
					protected void starTag(String tag) throws Exception {
						if(tag.equals("UpgradeVersion")){
							update	= true ;
							info.setMajorVersion(parser.nextText());
						} else if(tag.equals("UpgradeDescription")){
							info.setDescription(parser.nextText());
						} else if(tag.equals("UpgradeDescription")){
							info.setDescription(parser.nextText());
						} else if(tag.equals("UpgradeUrl")){
							info.setUrl(parser.nextText());
						} else if(tag.equals("Status")){
							info.setStatus(parser.nextText());
						}
					}

					@Override
					protected Object getParserObject() {
						return update ? info : null ;
					}

					@Override
					protected void endTag(String tag) throws Exception {
						
					}
					
				} ;
				//
				return xmlParser.parserSoapXml() ;
			}
		};
	}

}
