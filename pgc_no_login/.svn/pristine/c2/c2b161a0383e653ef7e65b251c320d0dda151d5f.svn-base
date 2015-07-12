package cn.dezhisoft.cloud.mi.newugc.common.util;

import com.higgses.sobeysdk.api.SoBeyApi;

/**
 * SDKC采集自定义事件
 * @author Rosson Chen
 * 2013-6-30
 */
public final class ReaperUtil {
	private ReaperUtil(){}
	public static void trackEvent(String status, String event, String eventTime){
		SoBeyApi.sendEvent(status, event, eventTime);
	}
	
	public static void trackEvent(String status, String event){
		SoBeyApi.sendEvent(status, event, "0");
	}
}
