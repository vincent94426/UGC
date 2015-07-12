package cn.dezhisoft.cloud.mi.newugc.common.net;

import org.apache.http.client.methods.HttpRequestBase;

public interface HttpCancelListenner {
	boolean isCanceled();
	void cancel();
	void setHttpRequest(HttpRequestBase request);
}
