package cn.dezhisoft.cloud.mi.newugc.ugc.ws;

import com.sobey.sdk.base.IResponse;

/**
 * SOAP 抽象响应, 子类必须实现克隆方法
 * 
 * @author Rosson Chen
 * 
 */
public abstract class AbstractSoapResponse implements IResponse, Cloneable {

	/** 返回实现类对象 */
	public abstract AbstractSoapResponse cloneResponse();

}
