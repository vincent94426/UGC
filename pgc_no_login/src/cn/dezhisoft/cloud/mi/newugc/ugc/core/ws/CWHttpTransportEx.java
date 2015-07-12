package cn.dezhisoft.cloud.mi.newugc.ugc.core.ws;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.ksoap2.transport.ServiceConnection;
import org.ksoap2.transport.ServiceConnectionSE;
import org.xmlpull.v1.XmlPullParserException;

/**
 * The Class HttpTransportEx, which is a hack of HttpTransportSE to separate the request process and the parse process
 * into two phrases, which originally is in one whole phrase. We have 2 reasons to do the hack
 * 1. The requirement to implement IRequest and IParser need to separate it
 * 2. Make the parser more testable, then we can test the parser with local data
 * 
 * How to use this Class: the HttpTransportEx can and only can be created when sending request. When do the parse, 
 * just using getTransport to get the HttpTransportEx to do the parse, but not create a new one.
 */
public final class CWHttpTransportEx extends HttpTransportSE {

	/** 超时时间*/
	private final static int TIME_OUT = 15 * 1000 ;
	
    /**
     * Instantiates a new HttpTransportEx to use the hack function.
     */
    public CWHttpTransportEx() {
        this(null);
    }

    /**
     * Instantiates a new HttpTransportEx.
     *
     * @param url the soap url
     */
    public CWHttpTransportEx(String url) {
        super(url,TIME_OUT);
    }

    @Override
	protected ServiceConnection getServiceConnection() throws IOException {
        return new ServiceConnectionSE(url,TIME_OUT);
	}

	/**
     * Gets the input stream.
     *
     * @param soapAction the soap action
     * @param envelope the SoapEnvelope
     * @return the input stream of the response
     * @throws IOException Signals that an I/O exception has occurred.
     * @throws XmlPullParserException the xml pull parser exception
     */
    public InputStream getInputStream(String soapAction, SoapSerializationEnvelope envelope) throws IOException,
            XmlPullParserException {

        if (soapAction == null) {
            soapAction = "\"\"";
        }
        // 得到参数
        InputStream is = null;
        ServiceConnection connection = null ;
        byte[] requestData = createRequestData(envelope);
        try {
        	// 初始化
	        connection = getServiceConnection();
	        connection.setRequestProperty("User-Agent", "kSOAP/2.0");
	        connection.setRequestProperty("SOAPAction", soapAction);
	        connection.setRequestProperty("Content-Type", "text/xml");
	        connection.setRequestProperty("Connection", "close");
	        connection.setRequestProperty("Content-Length", "" + requestData.length);
	        connection.setRequestMethod("POST");
	        connection.connect();
	        //
	        OutputStream os = connection.openOutputStream();
	        os.write(requestData, 0, requestData.length);
	        os.flush();
	        os.close();
	        requestData = null;
	        connection.connect();
	        //
            is = connection.openInputStream();
        } catch (IOException e) {
            is = connection.getErrorStream();
            if (is == null) {
                connection.disconnect();
                throw(e);
            }
        }
        return is;
    }
    
}
