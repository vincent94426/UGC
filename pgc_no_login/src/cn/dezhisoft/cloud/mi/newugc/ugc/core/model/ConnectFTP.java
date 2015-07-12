package cn.dezhisoft.cloud.mi.newugc.ugc.core.model;

/**
 * FTP 连接
 * 
 * @author Rosson Chen
 *
 */
public final class ConnectFTP {

	/** FTP server */
	private String 	ftpServer ;
	
	/** FTP port */
	private int 	ftpPort ;
	
	/** FTP user name */
	private String 	ftpUname ;
	
	/** FTP user password */
	private String 	ftpUpwd ;
	
	protected ConnectFTP(){
		ftpServer	= "" ;
		ftpPort		= 21 ;
		ftpUname	= "" ;
		ftpUpwd		= "" ;
	}

	public String getFtpServer() {
		return ftpServer;
	}

	public void setFtpServer(String ftpServer) {
		this.ftpServer = ftpServer;
	}

	public int getFtpPort() {
		return ftpPort;
	}

	public void setFtpPort(int ftpPort) {
		this.ftpPort = ftpPort;
	}

	public String getFtpUname() {
		return ftpUname;
	}

	public void setFtpUname(String ftpUname) {
		this.ftpUname = ftpUname;
	}

	public String getFtpUpwd() {
		return ftpUpwd;
	}

	public void setFtpUpwd(String ftpUpwd) {
		this.ftpUpwd = ftpUpwd;
	}
}
