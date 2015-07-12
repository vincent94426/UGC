package cn.dezhisoft.cloud.mi.newugc.ugc.core.ws;

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

import cn.dezhisoft.cloud.mi.newugc.common.util.DebugUtil;
import cn.dezhisoft.cloud.mi.newugc.ioffer.common.Config;

import com.sobey.sdk.base.HttpVisitor;

/**
 * SOAP 抽象基类
 * 
 * @author Rosson Chen
 *
 */
public abstract class CWBaseSoapImpl {

	/** name space*/
	public static final String NAMESPACE = Config.WS_NAMESPACE ;
	/** HTTP service */
	protected HttpVisitor httpService ;
	/** debug*/
	public static boolean DEBUG	= false ;
	
	public CWBaseSoapImpl(HttpVisitor service){
		httpService = service ;
	}
	
	protected final InputStream sendUploadSoapRequest(String soapAction,SoapObject request) throws Exception {
		SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
		envelope.setOutputSoapObject(request);
		envelope.dotNet = true;
		CWHttpTransportEx ht = new CWHttpTransportEx(CWUploadWebService.getClientHost());
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
