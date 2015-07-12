package cn.dezhisoft.cloud.mi.newugc.ugc.core.model;

/**
 * 媒体数据类型 : 视频 | 音频  | 图片 | 文档 | 未知
 * 
 * @author Rosson Chen
 *
 */
public enum MediaType {

	/** 未知素材*/
	UNKOWN("unknown",0) ,
	
	/** 视频*/
	VIDEO("video",1) ,
	
	/** 音频*/
	AUDIO("audio",2) ,
	
	/** 图片*/
	IMAGE("image",3) ,
	
	/** 文档*/
	DOCUMENT("document",4) ,
	
	/** 全部类型*/
	ALL("all",9) ;
	
	/** 数据类型*/
	private String meta ;
	
	/** 数据类型Code*/
	private int code ;
	
	private MediaType (String metadata,int type){
		meta = metadata ;
		code = type ;
	}
	
	public final String getMediaType(){
		return meta ;
	}
	
	public int getCode() {
		return code;
	}

	/**
	 * 匹配媒体数据类型
	 * 
	 * @param type	: 类型
	 * @return
	 */
	public static MediaType matchMediaType(String type){
		if(type == null) return UNKOWN ;
		//
		if(type.equals("video")) return VIDEO ;
		else if(type.equals("audio")) return AUDIO ;
		else if(type.equals("image")) return IMAGE ;
		else if(type.equals("document")) return DOCUMENT ;
		else return ALL ;
	}
	
	/**
	 * 根据code 来匹配媒体类型
	 * 
	 * @param code
	 * @return
	 */
	public static MediaType matchMediaType(int code){
		if(code == 0) return UNKOWN ;
		else if(code == 1) return VIDEO ;
		else if(code == 2) return AUDIO ;
		else if(code == 3) return IMAGE ;
		else if(code == 4) return DOCUMENT ;
		else return ALL ;
	}
}
