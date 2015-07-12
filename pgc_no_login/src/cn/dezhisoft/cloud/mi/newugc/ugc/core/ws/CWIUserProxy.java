package cn.dezhisoft.cloud.mi.newugc.ugc.core.ws;

import cn.dezhisoft.cloud.mi.newugc.ugc.core.model.User;

/**
 * 用户管理模块接口定义:
 * 
 * 1. 创建用户
 * 2. 删除用户
 * 3. 修改用户
 * 4. 枚举用户(查询用户详细信息)
 * 5. 登录
 * 6. 注销
 * 7. 得到当前用户
 * 
 * @author Rosson Chen
 *
 */
public interface CWIUserProxy {
	
	// 用户注册
	public void userRegister(User user,CWSoapResponse response);
	
	// 枚举用户详细信息
	public void userEnum(CWSoapResponse response);
	
	// 更新用户信息
	public void userUpdate(User user,CWSoapResponse response) ;
	
	// 用户登录
	public void login(String uname,String upwd,CWSoapResponse response) ;
	
	// 得到登录用户
	public User getUser() ;
	
	// 注销
	public void logout(CWSoapResponse response);
	
	// 重置
	public void reset();
}
