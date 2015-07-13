package cn.dezhisoft.cloud.mi.newugc.ugv2.client;

import cn.dezhisoft.cloud.mi.newugc.ugv2.chat.common.tran.bean.TranObject;


/**
 * 消息监听接口
 * 
 * @author way
 * 
 */
public interface MessageListener {
	public void Message(TranObject msg);
}
