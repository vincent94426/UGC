package cn.dezhisoft.cloud.mi.newugc.ugc.model;

public enum MaterialResouceType {
	
	RAW,		/** 原始文件 */
	PREVIEW ,	/** 预览地址 */
	UNKOWN ;	/** 未知 */
	
	public static MaterialResouceType create(String type){
		
		if(type == null) return UNKOWN ;
		
		if(type.equals("raw")){
			return RAW ;
		} else if(type.equals("preview")){
			return PREVIEW ;
		} else {
			return UNKOWN ;
		}
	}
}
