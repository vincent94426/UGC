package cn.dezhisoft.cloud.mi.newugc.ugv2.tools;

import java.util.HashMap;

import cn.dezhisoft.cloud.mi.newugc.ugv2.activity.Chat;

public class ManageChat {

	private static HashMap hm=new HashMap<String,Chat>();
	
	public static void addChat(String loginIdAndFriendId,Chat qqChat){
		hm.put(loginIdAndFriendId, qqChat);
	}
	
	public static Chat getChat(String loginIdAndFriendId){
		return (Chat)hm.get(loginIdAndFriendId);
	}
}
