package cn.dezhisoft.cloud.mi.newugc.ugv2.model;

import cn.dezhisoft.cloud.mi.newugc.ugv2.common.User;

public class QqClientUser {

	
	public boolean checkUser(User u){
		return new QqClientConServer().sendLoginInfoToServer(u);
	}
}
