package cn.dezhisoft.cloud.mi.newugc.ugc.ws;

import android.os.Handler;

import com.sobey.sdk.base.IRequestListener;
import com.sobey.sdk.base.IResponse;

/**
 * SOAP 请求监听器
 * 1. 开始请求
 * 2. 结束请求 (dispatchResponse 会被调用,覆盖本方法)
 * 
 * @author Rosson Chen
 * 
 */
public class SoapResponse implements IRequestListener ,Cloneable {

	/** SOAP 请求消息 */
	public static final class SoapRequestMessage {
		
		/** SOAP 请求开始 */
		public static final int MSG_START_SOAP	= 0x0f000000 ;
		/** SOAP 请求结束 */
		public static final int MSG_END_SOAP	= MSG_START_SOAP + 1 ;
	}
	
	/** SOAP 请求模式(前台模式(会发生request消息)|后台模式) */
	public static final class SoapRequestMode {
		
		/** 前台模式 */
		public static final int MODE_FOREGROUND	= 1 ;
		/** 后台模式 */
		public static final int MODE_BACKGROUND	= MODE_FOREGROUND + 1 ;
	}
	
	/** 请求模式*/
	private int mode ;
	/** handler */
	private Handler handler ;
	
	public SoapResponse(){
		this(SoapRequestMode.MODE_BACKGROUND,null);
	}
	
	public SoapResponse(Handler handler){
		this(SoapRequestMode.MODE_FOREGROUND,handler);
	}
	
	public SoapResponse(SoapResponse response){
		this(response.mode,response.handler);
	}
	
	public SoapResponse(int mode, Handler handler){
		this.mode 	  = mode ;
		this.handler  = handler ;
	}
	
	public final void init(SoapResponse response){
		init(response.mode,response.handler);
	}
	
	public final void init(int mode, Handler handler){
		this.mode 	  = mode ;
		this.handler  = handler ;
	}
	
	public final boolean sendMessage(int what){
		return sendMessage(what,null) ;
	}
	
	public final boolean sendMessage(int what,Object obj){
		if(handler != null) {
			handler.obtainMessage(what, obj).sendToTarget() ;
			return true ;
		} else {
			return false ;
		}
	}
	
	@Override
	public int startRequest() {
		
		if(mode == SoapRequestMode.MODE_FOREGROUND){
			sendMessage(SoapRequestMessage.MSG_START_SOAP) ;
		}
		
		return INTERCEPTOR_SUCCEED;
	}
	
	@Override
	public void endRequest(int code, Object value) {
		
		if(mode == SoapRequestMode.MODE_FOREGROUND){
			sendMessage(SoapRequestMessage.MSG_END_SOAP) ;
		}
		
		if(code == IResponse.RESPONSE_CODE_FAILED) {
			requestProcess(value);
		}
	}
	
	@Override
	public final Object requestProcess(Object value) {
		return dispatchResponse(value) ;
	}
	
	/** SOAP 响应 .子类覆盖此方法*/
	public Object dispatchResponse(Object value){
		return value ;
	}
}
