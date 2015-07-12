package cn.dezhisoft.cloud.mi.newugc.ugc.core.ws;

import java.io.InputStream;

import org.json.JSONException;
import org.json.JSONObject;
import org.ksoap2.serialization.SoapObject;

import android.text.TextUtils;
import android.util.Log;

import com.sobey.sdk.base.HttpVisitor;
import com.sobey.sdk.base.IResponse;
import com.sobey.sdk.base.Request;
import cn.dezhisoft.cloud.mi.newugc.ugc.core.model.ErrorMessage;
import cn.dezhisoft.cloud.mi.newugc.ugc.core.model.User;
import cn.dezhisoft.cloud.mi.newugc.ugv2.constants.UgV2Constants;

/**
 * 用户管理模块的实现
 *  
 * @author Rosson Chen
 *
 */
public final class CWUserProxyImpl extends CWBaseSoapImpl implements CWIUserProxy {
	
	/** for debug*/
	static final String 	TAG		= "UserProxyImpl" ;
	
	/** login user */
	private final User mLoginUser = new User() ;
	
	public CWUserProxyImpl(HttpVisitor service){
		super(service);
		mLoginUser.clear() ;
		mLoginUser.setSiteId("test");
		mLoginUser.setUsername("jiating");
		mLoginUser.setPassword("000000");
		mLoginUser.setSessionUID("002c3215701447431a04ab74bd4e9d3b");
		mLoginUser.setCustomFieldForm("{'columncode':'co2c94929a467f57e801467f57e8f60000,co2c94929a467f57e801467f6174850001,co2c902c5c38d6eced0138d70070cf000c','columnname':'晚间新闻,测试栏目,高清制作'}");
	}
	
	@Override
	public void userRegister(User user,CWSoapResponse response) {
		
		if(response == null) return ;
		
		final HttpVisitor service = httpService ;
		
		service.sendSoapRequest(requestRegisterUser(CWWebService.getSiteId(),user), response, responseRegister());
	}

	@Override
	public void userEnum(final CWSoapResponse response) {
		
		httpService.sendSoapRequest(requestEnumUser(mLoginUser), new CWSoapResponse(response) {
			
			@Override
			public Object dispatchResponse(Object value) {
				if(value instanceof User){
					User user = (User)value ;
					mLoginUser.setSiteId(user.getSiteId());
					mLoginUser.setScore(user.getScore());
					mLoginUser.setLastLoginTime(user.getLastLoginTime());
					mLoginUser.setEmail(user.getEmail());
					mLoginUser.setPhoneNumber(user.getPhoneNumber());
					//login_user.setIconBase64(user.getIconBase64());
					mLoginUser.setAvatarsUrl(user.getAvatarsUrl());
					mLoginUser.setUserRole(user.getUserRole());
					// 回调处理
					response.dispatchResponse(mLoginUser) ;
				} else {
					response.dispatchResponse(value) ;
				}
				return null;
			}
		}, responseEnumUser());
	}

	/**
	 * 注册新用户请求
	 * 
	 * @param newuser
	 * @return
	 */
	private final Request requestRegisterUser(final String siteID,final User newuser) {
		// 返回请求对象
		return new Request(Request.RESULT_TYPE_OBJECT) {
			@Override
			public InputStream getResquestInputStream() throws Exception {
				SoapObject request = new SoapObject(NAMESPACE, "CreateUser");
				request.addProperty("SessionUID", "0");
				request.addProperty("SiteID", siteID);
				request.addProperty("Username", newuser.getUsername());
				// 角色
				if(newuser.getApprovedLoginRole().size() > 0){
					SoapObject loginRole = new SoapObject("", "");
					for(String role : newuser.getApprovedLoginRole()){
						loginRole.addProperty("UserRole", role);
					}
					request.addProperty("ApprovedLoginRole", loginRole);
				}
				// 流类型
				if(newuser.getApprovedStreamType().size() > 0) {
					SoapObject streamType = new SoapObject("", "");
					for(String type : newuser.getApprovedStreamType()){
						streamType.addProperty("StreamType", type);
					}
					request.addProperty("ApprovedStreamType", streamType);
				}
				// 分类类型
				if(newuser.getApprovedCatalogType().size() > 0) {
					SoapObject catalotType = new SoapObject("", "");
					for(String catalog : newuser.getApprovedCatalogType()){
						catalotType.addProperty("CatalogID", catalog);
					}
					request.addProperty("ApprovedCatalogID", catalotType);
				}
				//
				request.addProperty("Password", newuser.getPassword());
				request.addProperty("Email", newuser.getEmail());
				request.addProperty("PhoneNumber", newuser.getPhoneNumber());
				request.addProperty("Avatars", newuser.getIconBase64());
				//
				return sendUploadSoapRequest(NAMESPACE + "/" + "CreateUser",request);
			}
		};
	}

	/**
	 * 注册响应分析
	 * 
	 * @return
	 */
	private final IResponse responseRegister(){
		return new IResponse() {
			@Override
			public Object wrapperObject(InputStream input) throws Exception {
				// debug
				debugResponse(input);
				// xml 解析器
				final CWBaseXmlParser xmlParser = new CWBaseXmlParser(input) {
					User user = null ;
					@Override
					public void starTag(String tag) throws Exception {
						if (tag.equals("CreateUserResponse")) {
							user = new User();
						}
					}
					
					@Override
					public Object getParserObject() {
						return user;
					}
				};
				// 解析
				return xmlParser.parserSoapXml() ;
			}
		};
	}
	
	private final Request requestEnumUser(final User user) {
		// 返回请求对象
		return new Request(Request.RESULT_TYPE_OBJECT) {
			@Override
			public InputStream getResquestInputStream() throws Exception {
				SoapObject request = new SoapObject(NAMESPACE, "EnumUser");
				request.addProperty("SessionID", user.getSessionUID());
				request.addProperty("SiteID", user.getSiteId());
				request.addProperty("Username", user.getUsername());
				request.addProperty("UserRole", "");
				request.addProperty("Email", "");
				request.addProperty("PhoneNumber", "");
				//
				return sendUploadSoapRequest(NAMESPACE + "/" + "EnumUser",request);
			}
		};
	}
	
	private final IResponse responseEnumUser(){
		return new IResponse() {
			
			@Override
			public Object wrapperObject(InputStream input) throws Exception {
				debugResponse(input);
				// xml 解析器
				CWBaseXmlParser xmlParser = new CWBaseXmlParser(input) {
					/** 用户*/
					User user = new User();
					@Override
					public void starTag(String tag) throws Exception {
						if(tag.equals("SiteID")){
							user.setSiteId(parser.nextText());
						}else if(tag.equals("Username")){
							user.setUsername(parser.nextText());
						}else if(tag.equals("UserRole")){
							user.addApprovedLoginRole(parser.nextText());
						}else if(tag.equals("StreamType")){
							user.addApprovedStreamType(parser.nextText());
						}else if(tag.equals("CatalogID")){
							user.addApprovedCatalogType(parser.nextText());
						}else if(tag.equals("Password")){
							user.setPassword(parser.nextText());
						}else if(tag.equals("Email")){
							user.setEmail(parser.nextText());
						}else if(tag.equals("PhoneNumber")){
							user.setPhoneNumber(parser.nextText());
						}else if(tag.equals("AvatarsUrl")){// 头像
							user.setAvatarsUrl(parser.nextText());
						}else if(tag.equals("Score")){// 积分
							user.setScore(Integer.valueOf(parser.nextText()));
						}
					}
					
					@Override
					public Object getParserObject() {
						return user ;
					}
				};
				// 解析xml文件
				return xmlParser.parserSoapXml() ;
			}
		};
	}
	
	

	@Override
	public void userUpdate(final User user,final CWSoapResponse response) {
		
		if(response == null) return ;
		
		final HttpVisitor service = httpService ;
		
		service.sendSoapRequest(requesUpdateUser(mLoginUser.getSiteId(),user), new CWSoapResponse(response){

			@Override
			public Object dispatchResponse(Object value) {
				if(value instanceof User){
					mLoginUser.setPassword(user.getPassword());
					mLoginUser.setEmail(user.getEmail());
					mLoginUser.setPhoneNumber(user.getPhoneNumber());
					mLoginUser.setAvatarsUrl(user.getAvatarsUrl());
					mLoginUser.setIconBase64(user.getIconBase64());
					response.dispatchResponse(mLoginUser);
				} else {
					response.dispatchResponse(value);
				}
				return null;
			}
			
		}, responseUpdateUser());
	}
	
	public Request requesUpdateUser(final String siteID,final User user) {
		return new Request(com.sobey.sdk.base.Request.RESULT_TYPE_OBJECT) {
			
			@Override
			public InputStream getResquestInputStream() throws Exception {
				try {
					SoapObject request = new SoapObject(NAMESPACE, "ModifyUser");
					request.addProperty("SessionID", user.getSessionUID());
					request.addProperty("SiteID", siteID);
					request.addProperty("Username", user.getUsername());
					// 角色
					SoapObject loginRole = new SoapObject("", "");
					loginRole.addProperty("UserRole", "client");
					request.addProperty("ApprovedLoginRole", loginRole);
					//
					request.addProperty("Password", user.getPassword());
					request.addProperty("Email", user.getEmail());
					request.addProperty("PhoneNumber", user.getPhoneNumber());
					// 是否修改头像
					if(user.getIconBase64() != null)
						request.addProperty("Aavatars", user.getIconBase64());
					//
					return sendUploadSoapRequest(NAMESPACE + "/" + "ModifyUser",request);
				} catch (Exception e) {
					e.printStackTrace();
				}
				return null;
			}
		};
	}
	
	public IResponse responseUpdateUser() {
		return new IResponse() {
			
			@Override
			public Object wrapperObject(InputStream input) throws Exception {
				// debug
				debugResponse(input);
				// xml 解析器
				final CWBaseXmlParser xmlParser = new CWBaseXmlParser(input) {
					
					boolean update = false ;
					
					@Override
					public void starTag(String tag) throws Exception {
						
						if(tag.equals("ModifyUserResponse")){
							update	= true ;
						}
					}
					
					@Override
					public Object getParserObject() {
						return update ? new User() : "ERROR: responseUpdateUser()";
					}
				};
				// 解析
				return xmlParser.parserSoapXml();
			}
		};
	}
	
	/** user login*/
	public void login(CWSoapResponse response) {
		login(CWUploadWebService.getUserName(),CWUploadWebService.getUserPassword(),response);
	}
	
	/** user login by user name and password */
	public void login(final String uname,final String upwd,final CWSoapResponse response){
		
		//
		/*if(response == null){
			Log.e(TAG, "ERROR: response is null");
			return ;
		}
		
		if(uname == null || upwd == null){
			ErrorMessage error = new ErrorMessage() ;
			error.setMessage("user name or password is null");
			response.dispatchResponse(error);
			return ;
		}*/
		
		final String siteId 	  = CWWebService.getSiteId() ;
		final HttpVisitor service = httpService ;
		final String session	  = mLoginUser.getSessionUID() ;
		// 是否登录
		if(session.equals("")){
			// 登录
			service.sendSoapRequest(requestLogin(siteId,uname,upwd), 
					new CWSoapResponse(response) {
						
						@Override
						public final Object dispatchResponse(Object value) {
							if(value instanceof User){
								User user = (User)value ;
								mLoginUser.setSiteId(siteId);
								mLoginUser.setUsername(uname);
								mLoginUser.setPassword(upwd);
								mLoginUser.setSessionUID(user.getSessionUID());
								mLoginUser.setCustomFieldForm(user.getCustomFieldForm());
								
								if (!TextUtils.isEmpty(user.getCustomFieldForm())) {
									JSONObject jsonCustom;
									try {
										jsonCustom = new JSONObject(user.getCustomFieldForm());
										String userCode = jsonCustom.optString(UgV2Constants.KEY_USER_CODE);
							            if (!TextUtils.isEmpty(userCode)) {
							            	mLoginUser.setId(userCode);
							            }
									} catch (JSONException e) {
										e.printStackTrace();
									}
						        }
								//
								response.dispatchResponse(value);
							} else {
								// 失败
								ErrorMessage error ;
								if(value instanceof ErrorMessage){
									error = (ErrorMessage)value ;
								} else {
									error = new ErrorMessage() ;
									error.setMessage("登录失败!");
								}
								response.dispatchResponse(error);
							}
							return null;
						}
					} , responseLogin());
		} else {
			// 已经登录
			response.dispatchResponse(mLoginUser);
		}
	}

	@Override
	public final User getUser() {
		return mLoginUser;
	}

	@Override
	public void logout(final CWSoapResponse response) {
		
		//
		if(response == null){
			Log.e(TAG, "ERROR: response is null");
			return ;
		}
		
		final HttpVisitor service = httpService ;
		
		service.sendSoapRequest(requestLogout(mLoginUser.getSessionUID()), response, responseLogout());
	}
	
	private final Request requestLogin(final String siteID ,final String uname,final String pwd) {
		// 返回请求
		return new Request(Request.RESULT_TYPE_OBJECT) {
			
			@Override
			public final InputStream getResquestInputStream() throws Exception {
				SoapObject request = new SoapObject(NAMESPACE, "Login");
				request.addProperty("SiteID", siteID);
				request.addProperty("Username", uname);
				request.addProperty("Password", pwd);
				request.addProperty("LoginRole", "client");
				request.addProperty("Identification", getLocalIpAddress());
				//
				return sendUploadSoapRequest(NAMESPACE + "/" + "Login",request);
			}
		};
	}
	
	private final IResponse responseLogin(){
		return new IResponse() {
			
			@Override
			public Object wrapperObject(InputStream input) throws Exception {
				//
				debugResponse(input);
				// xml 解析
				final CWBaseXmlParser xmlParser = new CWBaseXmlParser(input) {

					/** 用户 */
					User user = null;
					@Override
					public void starTag(String tag) throws Exception {
						if (tag.equals("SessionUID")) {
							user = new User();
							user.setSessionUID(parser.nextText());
						} else if (tag.equals("ApprovedStreamType")) {
							user.addApprovedStreamType(parser.nextText());
						} else if (tag.equals("LastLoginTime")) {
							user.setLastLoginTime(parser.nextText());
						} else if (tag.equals("CustomFieldForm")){
							user.setCustomFieldForm(parser.nextText());
						}
					}

					@Override
					public Object getParserObject() {
						return user;
					}

				};
				// 解析xml
				return xmlParser.parserSoapXml() ;
			}
		};
	}

	private final Request requestLogout(final String sid) {
		return new Request(Request.RESULT_TYPE_OBJECT) {
			
			@Override
			public final InputStream getResquestInputStream() throws Exception {
				SoapObject request = new SoapObject(NAMESPACE, "Logout");
				request.addProperty("SessionUID", sid);
				//
				return sendUploadSoapRequest(NAMESPACE + "/" + "Logout",request);
			}
		};
	}

	private final IResponse responseLogout() {
		return new IResponse() {
			
			@Override
			public final Object wrapperObject(InputStream input) throws Exception {
				// xml 解析
				final CWBaseXmlParser xmlParser = new CWBaseXmlParser(input) {

					@Override
					public void starTag(String tag) throws Exception {
						Log.d(TAG, "解析注销");
					}

					@Override
					public Object getParserObject() {
						return new User();
					}
					
				};
				// 解析
				return xmlParser.parserSoapXml();
			}
		};
	}

	@Override
	public void reset() {
		
	}
}
