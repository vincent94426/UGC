package cn.dezhisoft.cloud.mi.newugc.ugc.core.model;

/**
 * UGC System gateway channel 
 * 
 * @author Rosson Chen
 * 
 */
public final class ChannelInfo {
	
	/** upload mode */
	public static final class Mode {
		
		/** upload by TCP */
		public final static int TCP		= 1 ;
		
		/** upload by FTP */
		public final static int FTP		= 2 ;
	}
	
	/** transfer mode */
	private int mode ;

	/** channel state: pass | */
	private String status ;
	
	/** channel transfer UID */
	private String transferUID ;

	/** message */
	private String rejectMessage ;
	
	/** FTP connect*/
	private final ConnectFTP ftp = new ConnectFTP() ;
	
	/** private TCP connect*/
	private final ConnectTCP tcp = new ConnectTCP() ;
	
	public ChannelInfo(){
		transferUID	=  "" ;
		mode		= Mode.TCP ;
	}

	public String getTransferUID() {
		return transferUID;
	}

	public int getMode() {
		return mode;
	}

	public void setMode(int mode) {
		this.mode = mode;
	}

	public void setTransferUID(String transferUID) {
		this.transferUID = transferUID;
	}

	public String getStatus() {
		return status == null ? "response status is null" : status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getRejectMessage() {
		return rejectMessage;
	}

	public void setRejectMessage(String rejectMessage) {
		this.rejectMessage = rejectMessage;
	}

	public ConnectFTP getFtp() {
		return ftp;
	}

	public ConnectTCP getTcp() {
		return tcp;
	}
}
