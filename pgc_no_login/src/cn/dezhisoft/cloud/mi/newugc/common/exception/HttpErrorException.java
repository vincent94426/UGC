package cn.dezhisoft.cloud.mi.newugc.common.exception;

public class HttpErrorException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public HttpErrorException(){
		
	}
	
	public HttpErrorException(String message){
		super(message);
	}
}
