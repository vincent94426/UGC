package cn.dezhisoft.cloud.mi.newugc.ugc.ws;

import com.sobey.sdk.base.Request;

/**
 * SOAP 抽象请求, 子类必须实现克隆方法
 * 
 * @author Rosson Chen
 * 
 */
public abstract class AbstractSoapRequet extends Request implements Cloneable {
	
	protected String siteId ;
	protected String sessionUID ;

	public AbstractSoapRequet() {
		super(Request.RESULT_TYPE_OBJECT);
	}

	/** 返回实现类对象 */
	public abstract AbstractSoapRequet cloneRequest();

}
