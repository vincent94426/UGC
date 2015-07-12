package cn.dezhisoft.cloud.mi.newugc.ioffer.service;

public interface IofferDefine {

	public static final int MSG_USER_LOGIN_SUCCESS			= 0x0ff00000 ;
	public static final int MSG_USER_LOGIN_FAILED			= MSG_USER_LOGIN_SUCCESS + 1 ;
	public static final int MSG_USER_LOGOUT_SUCCESS			= MSG_USER_LOGIN_FAILED + 1 ;
	public static final int MSG_USER_LOGOUT_FAILED			= MSG_USER_LOGOUT_SUCCESS + 1 ;
	public static final int MSG_USER_ENUM_SUCCESS			= MSG_USER_LOGOUT_FAILED + 1 ;
	public static final int MSG_USER_ENUM_FAILED			= MSG_USER_ENUM_SUCCESS + 1 ;
	public static final int	MSG_USER_MODIFY_SUCCESS			= MSG_USER_ENUM_FAILED + 1;
	public static final int	MSG_USER_MODIFY_FAILED			= MSG_USER_MODIFY_SUCCESS + 1;
	public static final int MSG_USER_REGISTER_SUCCESS		= MSG_USER_MODIFY_FAILED + 1;	
	public static final int MSG_USER_REGISTER_FAILED		= MSG_USER_REGISTER_SUCCESS + 1;	
	
	//////////////////////////////////////////////////////////
	public static final int MSG_QUERY_SITE_SUCCESS			= MSG_USER_REGISTER_FAILED + 1 ;
	public static final int MSG_QUERY_SITE_FAILED			= MSG_QUERY_SITE_SUCCESS + 1 ;
	public static final int MSG_QUERY_CATEGORY_SUCCESS		= MSG_QUERY_SITE_FAILED + 1 ;
	public static final int MSG_QUERY_CATEGORY_FAILED		= MSG_QUERY_CATEGORY_SUCCESS + 1 ;
	public static final int MSG_QUERY_CONTENT_DETAIL_SUCCESS= MSG_QUERY_CATEGORY_FAILED + 1 ;
	public static final int MSG_QUERY_CONTENT_DETAIL_FAILED = MSG_QUERY_CONTENT_DETAIL_SUCCESS + 1 ;
	public static final int MSG_QUERY_CONTENT_ASSET_SUCCESS = MSG_QUERY_CONTENT_DETAIL_FAILED + 1 ;
	public static final int MSG_QUERY_CONTENT_ASSET_FAILED 	= MSG_QUERY_CONTENT_ASSET_SUCCESS + 1 ;
	public static final int MSG_QUERY_CONTENT_COMMENT_SUCCESS	= MSG_QUERY_CONTENT_ASSET_FAILED + 1 ;
	public static final int MSG_QUERY_CONTENT_COMMENT_FAILED 	= MSG_QUERY_CONTENT_COMMENT_SUCCESS + 1 ;
	public static final int MSG_QUERY_RELATED_CONTENT_SUCCESS = MSG_QUERY_CONTENT_COMMENT_FAILED + 1 ;
	public static final int MSG_QUERY_RELATED_CONTENT_FAILED  = MSG_QUERY_RELATED_CONTENT_SUCCESS + 1 ;
	public static final int MSG_ADD_CONTENT_COMMENT_SUCCESS   	= MSG_QUERY_RELATED_CONTENT_FAILED + 1 ;
	public static final int MSG_ADD_CONTENT_COMMENT_FAILED   	= MSG_ADD_CONTENT_COMMENT_SUCCESS + 1 ;
	public static final int MSG_SEARCH_CONTENT_SUCCESS   	= MSG_ADD_CONTENT_COMMENT_FAILED + 1 ;
	public static final int MSG_SEARCH_CONTENT_FAILED   	= MSG_SEARCH_CONTENT_SUCCESS + 1 ;
	
	//////////////////////////////////////////////////////////
	public static final int MSG_DOWNLOAD_BEGIN			 	= MSG_SEARCH_CONTENT_FAILED + 1 ;
	public static final int MSG_DOWNLOAD_END			 	= MSG_DOWNLOAD_BEGIN + 1 ;
	public static final int MSG_DOWNLOAD_FAILED			 	= MSG_DOWNLOAD_END + 1 ;
	
	public static final int MSG_QUERY_VERSION_SUCCESS		= MSG_DOWNLOAD_FAILED + 1 ;
	public static final int MSG_QUERY_VERSION_FAILED		= MSG_QUERY_VERSION_SUCCESS + 1 ;
}
