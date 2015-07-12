package cn.dezhisoft.cloud.mi.newugc.ugc.core.ws;

import com.sobey.sdk.base.Request;

/**
 * SOAP 抽象请求, 子类必须实现克隆方法
 * 
 * @author Rosson Chen
 * 
 */
public abstract class CWBaseSoapRequet extends Request implements Cloneable {

	public CWBaseSoapRequet() {
		super(Request.RESULT_TYPE_OBJECT);
	}

	/** 返回实现类对象 */
	public abstract CWBaseSoapRequet cloneRequest();

}
