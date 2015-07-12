package cn.dezhisoft.cloud.mi.newugc.ugc.model;

/**
 * 素材状态 : 转码,发布
 * @author Rosson Chen
 *
 */
public enum MaterialStatus {

	WAITING, AUDITING, PUBLISH ;
	
	public static MaterialStatus createStatus(int code){
		if(code == 0){
			return WAITING ;
		} else if(code == 1){
			return AUDITING ;
		} else if(code == 2){
			return PUBLISH ;
		} else {
			return null ;
		}
	}
}
