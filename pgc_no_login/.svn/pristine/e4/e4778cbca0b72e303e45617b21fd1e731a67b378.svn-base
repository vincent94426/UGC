package cn.dezhisoft.cloud.mi.newugc.ugc.ws;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;

import com.sobey.sdk.base.HttpVisitor;

import cn.dezhisoft.cloud.mi.newugc.common.util.DebugUtil;
import cn.dezhisoft.cloud.mi.newugc.ioffer.common.Config;
import cn.dezhisoft.cloud.mi.newugc.ugc.ws.publish.UGCContentWebService;
import cn.dezhisoft.cloud.mi.newugc.ugc.core.ws.CWHttpTransportEx;
import cn.dezhisoft.cloud.mi.newugc.ugc.core.ws.CWUploadWebService;

/**
 * SOAP 抽象基类
 * 
 * @author Rosson Chen
 *
 */
public abstract class AbstractSoapImpl {

	/** name space*/
	protected static final String NAMESPACE = Config.WS_NAMESPACE ;
	/** HTTP service */
	protected HttpVisitor httpService ;
	/** debug*/
	protected static boolean DEBUG	= !false ;
	
	protected AbstractSoapImpl(HttpVisitor service){
		httpService = service ;
	}
	
	protected final InputStream sendUpSoapRequest(String soapAction,SoapObject request) throws Exception {
		SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
		envelope.setOutputSoapObject(request);
		envelope.dotNet = true;
		CWHttpTransportEx ht = new CWHttpTransportEx(CWUploadWebService.getClientHost());
		return ht.getInputStream(soapAction, envelope);
	}
	
	protected final InputStream sendContentSoapRequest(String soapAction,SoapObject request) throws Exception {
		SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
		envelope.setOutputSoapObject(request);
		envelope.dotNet = true;
		CWHttpTransportEx ht = new CWHttpTransportEx(UGCContentWebService.getPulishHost());
		return ht.getInputStream(soapAction, envelope);
	}
	
	protected final InputStream sendSoapRequest(String actionUrl,String soapAction,SoapObject request) throws Exception {
		SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
		envelope.setOutputSoapObject(request);
		envelope.dotNet = true;
		CWHttpTransportEx ht = new CWHttpTransportEx(actionUrl);
		return ht.getInputStream(soapAction, envelope);
	}
	
	protected static final String getLocalIpAddress() {
		try {
			for(Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces();en.hasMoreElements();){
				NetworkInterface ni = en.nextElement();	
				for(Enumeration<InetAddress> ea = ni.getInetAddresses();ea.hasMoreElements();){
					InetAddress ia = ea.nextElement();
					if(!ia.isLoopbackAddress()){
						return ia.getHostAddress().toString();
					}
				}
			}
		} catch (SocketException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	protected final void debugResponse(InputStream input){
		if(!DebugUtil.isDEBUG) return ;
		try{
			BufferedReader reader = new BufferedReader(new InputStreamReader(input));
			String str = reader.readLine() ;
			while(str != null){
				System.out.println(str);
				//DebugUtil.traceLog(str);
				str = reader.readLine() ;
			}
			input.reset();
		}catch(Exception e){
			e.printStackTrace() ;
		}
	}
}
