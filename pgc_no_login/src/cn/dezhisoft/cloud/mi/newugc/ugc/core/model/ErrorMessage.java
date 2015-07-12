package cn.dezhisoft.cloud.mi.newugc.ugc.core.model;

/**
 * UGC error message
 * 
 * @author Rosson Chen
 * 
 */
public class ErrorMessage {

	/** message code */
	private String code ;
	
	/** message */
	private String message ;
	
	public ErrorMessage(){
		code = message = "" ;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}
}
