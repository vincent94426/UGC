package cn.dezhisoft.cloud.mi.newugc.ugc.core.upload;

import java.util.ArrayList;

import cn.dezhisoft.cloud.mi.newugc.ugc.core.model.Metadata;

import android.app.Activity;
import android.view.SurfaceHolder;

/**
 * UGC 上传功能抽象.调用公共模块
 * 
 * @author Rosson Chen
 *
 */
public interface IUGCUpload {
	
	/** 传输模式  */
	public static final class TransferMode {
		
		/** private tcp */
		public static final int PRIVATE_TCP	= 0X01;
	
		/** ftp */
		public static final int FTP			= 0X02;
	}
	
	/** 消息类型 */
	public static final class Command {
		
		/** 命令类型 */
		public static final int COMMAND_TYPE_COMMAND 	= 0X01;
		/** 错误类型 */
		public static final int COMMAND_TYPE_ERROR 		= 0X02;
		/** 验证类型 */
		public static final int COMMAND_TYPE_VERFIY 	= 0X03;
	}

	/** 服务器消息code */
	public static final class CodeServer {
		
		/** fc */
		public static final int CODE_FC 				= 0;
		/** ks */
		public static final int CODE_KS 				= 1;
		/** sn */
		public static final int CODE_SN 				= 2;
		/** en */
		public static final int CODE_EN 				= 3;
	}

	/** 验证消息 */
	public static final class CodeVerify {
		
		/** 验证失败 */
		public static final int CODE_VERIFY_FAILED 		= 0;
		/** 验证成功 */
		public static final int CODE_VERIFY_SUCCESS 	= 1;
		/** 验证超时 */
		public static final int CODE_VERIFY_TIMEOUT 	= 2;
		/** 等待验证 */
		public static final int CODE_VERIFY_WAIT 		= 3;
	}

	/** 错误消息 */
	public static final class CodeError {
		
		/** 发送错误 */
		public static final int CODE_ERROR_SEND_ERROR 	= 0;
		/** 接收错误 */
		public static final int CODE_ERROR_RECV_ERROR 	= 1;
		/** 打开文件失败 */
		public static final int CODE_ERROR_OPEN_FILE 	= 2;
		/** 超时错误 */
		public static final int CODE_ERROR_TIMEOUT 		= 3;
	}
	
	/** FTP 错误消息 */
	public static final class CodeFTP {
		
		/** 成功*/
		public static final int  CODE_OK         		 = 0x00000000 ;
		/** 连接失败 */
		public static final int CODE_ERROR_CONNECT    	 = 0x00ff0001 ;
		/** 用户名和密码错误 */
		public static final int CODE_ERROR_LOGIN         = 0x00ff0002 ;
		/** 注销失败 */
		public static final int CODE_ERROR_LOGOUT        = 0x00ff0003 ;
		/** 创建远程目录失败 */
		public static final int CODE_ERROR_REMOTE_MKDIR	 = 0x00ff0004 ;
		/** 远程目录切换失败 */
		public static final int CODE_ERROR_REMOTE_CHANGE = 0x00ff0005 ;
		/** 打开本文件失败 */
		public static final int CODE_ERROR_LOCAL_RFILE   = 0x00ff0006 ;
		/** 切换本地目录失败 */
		public static final int CODE_ERROR_LOCAL_CFILE   = 0x00ff0007 ;
		/** 创建本地目录失败 */
		public static final int CODE_ERROR_LOCAL_MKDIR   = 0x00ff0008 ;
		/** 发送数据失败 */
		public static final int CODE_ERROR_SEND_DATA     = 0x00ff0009 ;
		/** 读数据 错误 */
		public static final int CODE_ERROR_READ_DATA     = 0x00ff000A ;
		/** socket 错误 */
		public static final int CODE_ERROR_SOKET         = 0x00ff000B ;
		/** 远程目录错误 */
		public static final int  CODE_REMOTE_MKFILE      = 0x00ff000C ;
		/** 数据端口错误 */
		public static final int CODE_DATAPORT_ERROR      = 0x00ff000D ;
	}
	
	/** UGC 上传回调 */
	public static interface IUGCUploadCallback {

		public void callback(int type, int code, String t1, String t2);

		public void progress(long total, int sendsize,int id);

		public void liveStatistics(long send, long lost, int bitRate);
	}

	// 上传控制
	public void setCallback(IUGCUploadCallback callback);
	
	public boolean connect(String ip, int port);

	public void setMetadata(Metadata meta,String tid);

	public void prepare();

	public void start(boolean resume);

	public void stop();

	public void release();
	
	// 私有协议文件上传
	public void setFilePath(String path) ;

	public void setFileInfo(String name, String extName);

	public boolean isUploadFile();
	
	// FTP 文件上传
	public void setFTPServerInfo(String ip,int port,String uname,String upwd);
	
	public void setFTPDirectory(String dir);
	
	public boolean startFTPUpload(ArrayList<String> files,boolean resume);
	
	// live upload
	// 视频传输参数
	public void setTransferVideo(int mime,int width,int height,int fps,int bitrate) ;
	// 音频传输参数
	public void setTransferAudio(int mime,int channel,int samplerate,int bitrate) ;
	// 当前传输帧时间戳
	public long getCurrentTime();
	
	// 编码器
	public void setEncoder(int encoder, Activity onwer);
	// 编码音频配置
	public void setEncoderAudio(int mime,int channel,int sampleRate,int bitrate);
	// 编码视频配置
	public void setEncoderVideo(int width, int height, int fps,int bitRate);
	// 编码预览
	public void setEncoderPreview(SurfaceHolder holder);
	// 编码参数集合
	public com.sobey.sdk.encoder.Metadata getVideoMetadata() ;
	public com.sobey.sdk.encoder.Metadata getAudioMetadata() ;
	
}