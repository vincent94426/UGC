package cn.dezhisoft.cloud.mi.newugc.ugc.core.ws;

/**
 * 通道管理接口定义: 
 * 
 *  1. 申请传输通道 
 *  2. 传输通道心跳 
 *  3. 释放通道
 * 
 * @author Rosson Chen
 * 
 */
public interface CWIChannelProxy {
	
	// 申请通道
	public void requestTransferChannel(String stream,String uid,CWSoapResponse response);
	
	// 启动通道心跳
	public void startTransferHeartbeat(String tuid);
	
	// 是否通道
	public void releaseTransferChannel(String tuid);
}
