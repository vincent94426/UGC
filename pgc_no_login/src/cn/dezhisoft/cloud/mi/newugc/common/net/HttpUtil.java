package cn.dezhisoft.cloud.mi.newugc.common.net;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;

import android.util.Log;
import cn.dezhisoft.cloud.mi.newugc.common.constants.NpmConstants;
import cn.dezhisoft.cloud.mi.newugc.common.exception.Http401Exception;
import cn.dezhisoft.cloud.mi.newugc.common.exception.Http404Exception;
import cn.dezhisoft.cloud.mi.newugc.common.exception.Http500Exception;
import cn.dezhisoft.cloud.mi.newugc.common.exception.HttpErrorException;
import cn.dezhisoft.cloud.mi.newugc.common.exception.UserCancelException;
import cn.dezhisoft.cloud.mi.newugc.common.global.AppRuntimeContext;
import cn.dezhisoft.cloud.mi.newugc.common.net.httpclient.AndroidHttpClient;
import cn.dezhisoft.cloud.mi.newugc.common.net.httpclient.AndroidHttpClientFactory;
import cn.dezhisoft.cloud.mi.newugc.common.util.DebugUtil;
import cn.dezhisoft.cloud.mi.newugc.common.util.IOUtil;
import cn.dezhisoft.cloud.mi.newugc.common.util.ThreadUtil;

/**
 * 工具类：提供基础网络访问服务
 * 1.提供了异常重试、极端网络场景下异常处理、通用HTTP错误码处理
 * 2.要求传入Context是为了在User-Agent准确反映版本信息
 * 
 * add 增加对Cancel的支持，部分请求接口中加入OnCancelListenner, 如果需要Cancel
 * @author chenhp
 */
public final class HttpUtil {
	private static final int HTTP_REQUEST_INTERVAL_SLEEP_TIME = 100;
	private static final String TAG = "BaseNetWorker";
	private static final int RETRY_TIMES = 2;
	private static final String JSON_CONTENT_TYPE = "application/json";
	private static AndroidHttpClient client = null;
	private HttpUtil(){}
	
	public static String doDelete4Text(String uri) throws IOException, Http401Exception, Http500Exception, Http404Exception, HttpErrorException, UserCancelException  {
		HttpResponse response = executeRequest(buildHttpDelete(uri), null);
		return readResponse(response);
	}
	
	public static HttpResponse doDelete(String uri) throws IOException, Http401Exception, Http500Exception, Http404Exception, HttpErrorException, UserCancelException  {
		return executeRequest(buildHttpDelete(uri), null);
	}

	private static HttpDelete buildHttpDelete(String uri) throws IOException {
		HttpDelete del = new HttpDelete(uri);

		del.setHeader("Accept-Encoding", "gzip");
		
        return del;
    }
	
	public static String doPut4Text(String uri, String requestData) throws IOException, Http401Exception, Http500Exception, Http404Exception, HttpErrorException, UserCancelException  {
		HttpResponse response = executeRequest(buildHttpPut(uri, requestData), null);
		return readResponse(response);
	}
	
	public static HttpResponse doPut(String uri, String requestData) throws IOException, Http401Exception, Http500Exception, Http404Exception, HttpErrorException, UserCancelException  {
		return executeRequest(buildHttpPut(uri, requestData), null);
	}

	private static HttpPut buildHttpPut(String uri, String requestData) throws IOException {
		HttpPut put = new HttpPut(uri);
		if(null != requestData){
	        StringEntity entity =  new StringEntity( requestData, NpmConstants.DEFAULT_ENCODEING) ;
	        entity.setContentEncoding(NpmConstants.DEFAULT_ENCODEING);
	        entity.setContentType(URLEncodedUtils.CONTENT_TYPE + HTTP.CHARSET_PARAM + NpmConstants.DEFAULT_ENCODEING);
	        put.setEntity(entity);
		}
        return put;
    }
	
	public static String doPost4Text(String uri, String requestData) throws IOException, Http401Exception, Http500Exception, Http404Exception, HttpErrorException, UserCancelException  {
		HttpResponse response = doPostResponse(uri, requestData);
		return readResponse(response);
	}
	
	public static String doPost4Text(String uri, List<BasicNameValuePair> requestData) throws IOException, Http401Exception, Http500Exception, Http404Exception, HttpErrorException, UserCancelException {
        HttpResponse response = doPostResponse(uri, requestData);
        return readResponse(response);
    }

    public static String doPost4Text(String uri, MultipartEntity multipartEntity) throws IOException, Http401Exception, Http500Exception, Http404Exception, HttpErrorException, UserCancelException
    {
    	HttpResponse response = executeRequest(buildHttpPost(uri, multipartEntity), null);
        return readResponse(response);
    }

    public static HttpResponse doPostResponse(String uri, String requestData, HttpCancelListenner listenner) throws IOException, Http401Exception, Http500Exception, Http404Exception, HttpErrorException, UserCancelException {
    	return executeRequest(buildHttpPost(uri, requestData), listenner);
    }
    
    public static HttpResponse doPostResponse(String uri, String requestData) throws IOException, Http401Exception, Http500Exception, Http404Exception, HttpErrorException, UserCancelException {
        return executeRequest(buildHttpPost(uri, requestData), null);
    }
    
    private static HttpResponse doPostResponse(String uri, List<BasicNameValuePair> requestData) throws IOException, Http401Exception, Http500Exception, Http404Exception, HttpErrorException, UserCancelException {
        return executeRequest(buildHttpPost(uri, requestData), null);
    }
    
    public static HttpResponse doPostResponse(String uri, MultipartEntity multipartEntity, HttpCancelListenner listenner) throws IOException, Http401Exception, Http500Exception, Http404Exception, HttpErrorException, UserCancelException {
    	return executeRequest(buildHttpPost(uri, multipartEntity), listenner);
    }
    
    public static HttpResponse doPostResponse(String uri, MultipartEntity multipartEntity) throws IOException, Http401Exception, Http500Exception, Http404Exception, HttpErrorException, UserCancelException {
    	return executeRequest(buildHttpPost(uri, multipartEntity), null);
    }
    
    private static HttpPost buildHttpPost(String uri,MultipartEntity multipartEntity) throws IOException  {
    	HttpPost post = new HttpPost(uri);
    	post.setEntity(multipartEntity);
    	return post;
	}

	private static HttpPost buildHttpPost(String uri, String requestData) throws IOException {
        HttpPost post = new HttpPost(uri);
        StringEntity entity =  new StringEntity( requestData, NpmConstants.DEFAULT_ENCODEING) ;
        entity.setContentEncoding(NpmConstants.DEFAULT_ENCODEING);
        entity.setContentType(JSON_CONTENT_TYPE + HTTP.CHARSET_PARAM + NpmConstants.DEFAULT_ENCODEING);
        post.setEntity(entity);
        return post;
    }
    
    private static HttpPost buildHttpPost(String uri, List<BasicNameValuePair> requestData) throws IOException {
        HttpPost post = new HttpPost(uri);
        UrlEncodedFormEntity entity = new UrlEncodedFormEntity(requestData);
        entity.setContentEncoding(NpmConstants.DEFAULT_ENCODEING);
        entity.setContentType(URLEncodedUtils.CONTENT_TYPE + HTTP.CHARSET_PARAM + NpmConstants.DEFAULT_ENCODEING);
        post.setEntity(entity);
        return post;
    }
	
	public static String doGet4Text(String uri) throws UserCancelException, Http401Exception, Http500Exception, Http404Exception, HttpErrorException, IOException  {
		HttpResponse response = executeRequest(createHttpGet(uri), null);
		return readResponse(response);
	}
	
	public static HttpResponse doGetResponse(String uri) throws UserCancelException, Http401Exception, Http500Exception, Http404Exception, HttpErrorException, IOException  {
		return executeRequest(createHttpGet(uri), null);
	}

	private static HttpGet createHttpGet(String url) {
		HttpGet get = new HttpGet(url);
		
		get.setHeader("Accept-Encoding", "gzip");
		
		return get;
	}

	public static HttpResponse executeRequest(HttpRequestBase request, HttpCancelListenner listenner) throws IOException, UserCancelException, Http401Exception, Http500Exception, Http404Exception, HttpErrorException {
		checkInitHttpClient();
		
		for(int i=0; i<RETRY_TIMES;i++){
			try{
				if(listenner != null){
					listenner.setHttpRequest(request);
				}
				
				if(listenner != null && listenner.isCanceled()) {
					throw new UserCancelException();
				}
				
				HttpResponse response = client.execute(request);
				return checkAndReturnResponse(response);
			}catch(IOException e){
				DebugUtil.traceThrowableLog(e);
				
				if(i == RETRY_TIMES -1){
					throw e;
				}else{
					ThreadUtil.sleepForMilliseconds(HTTP_REQUEST_INTERVAL_SLEEP_TIME);
					continue;
				}
			}
		}
		throw new IOException("Http post failed after retry." );
	}

	/**
	 * 判断response code 如果200直接返回，
	 * 非200，清空stream(1.查看内容，2.防止流未关闭导致httpclient下次请求无法使用) 然后抛出异常
	 * @param response
	 * @return
	 * @throws IOException
	 * @throws Http401Exception
	 * @throws Http500Exception
	 * @throws Http404Exception 
	 * @throws HttpErrorException 
	 */
	private static HttpResponse checkAndReturnResponse(HttpResponse response)
			throws IOException, Http401Exception, Http500Exception, Http404Exception, HttpErrorException
			 {
		StatusLine statusLine = response.getStatusLine();
		if (statusLine.getStatusCode() == HttpStatus.SC_OK) {
			return response;
		} else if(statusLine.getStatusCode() == HttpStatus.SC_UNAUTHORIZED || statusLine.getStatusCode() == HttpStatus.SC_FORBIDDEN){
			Log.e(TAG, "Http failed, unauthorized:" + HttpStatus.SC_UNAUTHORIZED);
			readAndPrintResponse(response);
			
			throw new Http401Exception();
		}else if(statusLine.getStatusCode() >= HttpStatus.SC_INTERNAL_SERVER_ERROR){
			Log.e(TAG, "Http failed, server error code:" + statusLine.getStatusCode());
			readAndPrintResponse(response);
			
			throw new Http500Exception();
		}else if(statusLine.getStatusCode() == HttpStatus.SC_NOT_FOUND){
			Log.e(TAG, "Http failed, server error code:" + HttpStatus.SC_NOT_FOUND);
			readAndPrintResponse(response);
			
			throw new Http404Exception();
		}else {
			readAndPrintResponse(response);
			
			throw new HttpErrorException("Http failed, unknown error code:" + statusLine.getStatusCode());
		}
	}

	private static void readAndPrintResponse(HttpResponse response)
			throws IOException {
		InputStream in = null;
		try{
			in = AndroidHttpClient.getUngzippedContent(response.getEntity());
			if(null != in){
				Log.d(TAG, IOUtil.read(in));
			}
		}finally{
			IOUtil.close( in );
		}
	}
	
	private static String readResponse(HttpResponse response) throws IOException, HttpErrorException {
		StatusLine statusLine = response.getStatusLine();
		InputStream inputStream = null;
		try{
			if (statusLine.getStatusCode() == HttpStatus.SC_OK) {
					inputStream = AndroidHttpClient.getUngzippedContent(response.getEntity());
					return IOUtil.read( inputStream );
			}else {
				readAndPrintResponse(response);
				throw new HttpErrorException("service errror, response code:" + statusLine.getStatusCode());
			}
		}finally{
			IOUtil.close( inputStream );
		}
	}
	

	private static void checkInitHttpClient (){
		synchronized (HttpUtil.class) {
			if(null == client) {
				client = AndroidHttpClientFactory.getAndroidHttpClient(AppRuntimeContext.getContext());
			}
		}
	}
	
	public static void releaseHttpResource(){
		synchronized (HttpUtil.class) {
			AndroidHttpClientFactory.destroyAndroidHttpClient();
			client = null;
		}
	}
	
}