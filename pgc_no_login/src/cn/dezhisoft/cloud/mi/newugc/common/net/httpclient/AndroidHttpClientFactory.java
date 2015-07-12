package cn.dezhisoft.cloud.mi.newugc.common.net.httpclient;

import java.io.IOException;
import java.net.ConnectException;
import java.net.SocketException;
import java.net.UnknownHostException;

import org.apache.http.NoHttpResponseException;
import org.apache.http.impl.client.DefaultHttpRequestRetryHandler;
import org.apache.http.protocol.HttpContext;

import android.content.Context;
import android.util.Log;

import cn.dezhisoft.cloud.mi.newugc.common.exception.UserCancelException;
import cn.dezhisoft.cloud.mi.newugc.common.util.AppUtil;


public class AndroidHttpClientFactory {
	private static AndroidHttpClient client = null;
	//private static final String MOCK_AGENT = "(Linux; U; Android ; en-us;)";
	
	public static AndroidHttpClient getAndroidHttpClient(Context context) {
		if(null == client)
			initHttpClient(context);
		return client;
	}
	
	public static void destroyAndroidHttpClient() {
		try{
			if(null != client)
				client.close();
			client = null;
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	private static void initHttpClient(Context context) {
		String ua = AppUtil.getUserAgent(context);
		
		Log.d("UserAgent", ua);
		
		client = AndroidHttpClient.newInstance(ua, context);
		
		AndroidHttpClient ahc = (AndroidHttpClient) client;
		ahc.setHttpRequestRetryHandler(new DefaultHttpRequestRetryHandler() {

			@Override
			public boolean retryRequest(IOException exception,
					int executionCount, HttpContext context) {
				if (exception == null) {
		            throw new IllegalArgumentException("Exception parameter may not be null");
		        }
		        if (context == null) {
		            throw new IllegalArgumentException("HTTP context may not be null");
		        }
		        //如果用户取消则不重试
		        if(exception instanceof UserCancelException) {
		        	return false;
		        }
		        if (executionCount > this.getRetryCount()) {
		            // Do not retry if over max retry count
		            return false;
		        }
		        if(exception instanceof NoHttpResponseException) {
					return true;
				}
		        //增加Unknown Host
		        if(exception instanceof UnknownHostException) {
		        	return true;
		        }
		        if (exception instanceof ConnectException) {
		            // Connection refused
		            return false;
		        }
		        if(exception instanceof SocketException) {
		        	// other socket exception retry 
		        	return true;
		        }
		        exception.printStackTrace();
		        
				return super.retryRequest(exception, executionCount, context);
			}
		});
	}
}
