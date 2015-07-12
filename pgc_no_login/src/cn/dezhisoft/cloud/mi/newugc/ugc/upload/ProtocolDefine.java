package cn.dezhisoft.cloud.mi.newugc.ugc.upload;

import cn.dezhisoft.cloud.mi.newugc.ugc.core.model.Metadata;

/**
 * 封装协议格式定义
 * 
 * @author Rosson Chen
 *
 */
public final class ProtocolDefine {

	/** 文本编码格式*/
	public static final String ENCODING 					= "UTF-16" ;
	
	/** 协议版本号*/
	public static final byte VERSION						= 1 ;
	
	/** 平台*/
	public static final String PLATFORM 					= "android" ;
	
	/** 通道状态*/
	public static final String CHANNEL_STATE_PASSED			= "passed" ;
	
	/** 流类型 */
	public static final class StreamType {
		
		/** 流类型 */
		public static final String LIVE				= "Live" ;
		
		/** 文件*/
		public static final String FILE				= "File" ;
	}
	
	/** 传输模式*/
	public static final class TransportMode {
		
		/** 传输模式: 私有TCP */
		public static final String PRIVATE_TCP 				= "PrivateTcp" ;
		
		/** FTP */
		public static final String FTP		 				= "Ftp" ;
	}
	
	/** 数据转发网关协议（DataStream） TCP */
	public static final class DataStreamDefine {
		
		/** ws context 数据包*/
		public static final byte DS_ID_WSC 				= 10 ;
		
		/** 数据发送开始*/
		public static final byte DS_ID_DATA_START 		= 11 ;
		
		/** 数据包*/
		public static final byte DS_ID_DATA_PACKAGE 	= 12 ;
		
		/** 数据发送结束*/
		public static final byte DS_ID_DATA_END	 		= 13 ;
		
		/** 控制命令包*/
		public static final byte DS_ID_COMMAND	 		= 14 ;
		
		/** 数据确认包*/
		public static final byte DS_ID_DATA_CONFIRM	 	= 17 ;
	}
	
	/** 视频数据包格式定义 */
	public static final class VideoPackage {
		
		/** 视频块*/
		public static final byte  VP_ID			= 1 ;
		
		/** H263视频编码*/
		public static final byte  VP_H263		= 5 ;
		
		/** H264视频编码*/
		public static final byte  VP_H264		= 6 ;
		
		/** H263 头*/
		public static final byte  VP_ID_H263	= (VP_ID << 4) | VP_H263;
		
		/** H264 头*/
		public static final byte  VP_ID_H264	= (VP_ID << 4) | VP_H264;
		
		/**
		 * 得到帧
		 * 
		 * @param frame
		 * @return
		 */
		public static final byte getVideoFrameRate(float frame){
			return (byte)((int)frame | 0x80) ;
		}
	}
	
	/** 音频数据包格式定义 */
	public static final class AudioPackage {
		
		/** 音频块*/
		public static final byte AP_ID				= 2 ;
		
		/** MP3音频编码*/
		public static final byte AP_AUDIO_MP3		= 4 ;
		
		/** AMR音频编码*/
		public static final byte AP_AUDIO_AMR		= 9 ;
		
		/** AAC音频编码*/
		public static final byte AP_AUDIO_AAC		= 10 ;
		
		/** MP3 头*/
		public static final byte AP_ID_MP3			= (AP_ID << 4) | AP_AUDIO_MP3 ;
		
		/** AMR 头*/
		public static final byte AP_ID_AMR			= (AP_ID << 4) | AP_AUDIO_AMR ;
		
		/** AAC 头*/
		public static final byte AP_ID_AAC			= (AP_ID << 4) | AP_AUDIO_AAC ;
		
		/** 单声道*/
		public static final byte AP_CHANNEL_ONE 	= 1 ;
		
		/** 双声道*/
		public static final byte AP_CHANNEL_TWO 	= 2 ;
		
		/** 采样位宽： 8位*/
		public static final byte AP_BIT_WIDE_8		= 8 ;
		
		/** 采样位宽： 16位*/
		public static final byte AP_BIT_WIDE_16		= 16 ;
		
		/** 采样位宽： 24位*/
		public static final byte AP_BIT_WIDE_24		= 24 ;
		
		/** 采样位宽： 32位*/
		public static final byte AP_BIT_WIDE_32		= 32 ;
		
		/** 采样率： 8000 Hz*/
		public static final byte AP_SAMPLE_RATE_1	= 1 ;
		
		/** 采样率： 16000 Hz*/
		public static final byte AP_SAMPLE_RATE_2	= 2 ;
		
		/** 采样率： 32000 Hz*/
		public static final byte AP_SAMPLE_RATE_3	= 3 ;
		
		/** 采样率： 44100 Hz*/
		public static final byte SAMPLE_RATE_4		= 4 ;
		
		/** 采样率： 48000 Hz*/
		public static final byte SAMPLE_RATE_5		= 5 ;
		
		/** 采样率： 96000 Hz*/
		public static final byte SAMPLE_RATE_6		= 6 ;
	}
	
	/***/
	private ProtocolDefine(){}
	
	/**
	 * 创建媒体描述文件头
	 * 
	 * @param info
	 * @return
	 */
	public static final String buildProtocolHeader(Metadata metaData,int id,String type,String fileName,String extName,String tid){
		if(metaData == null)
			throw new RuntimeException("ERROR: MediaInfo is null !"); 
		// 构建xml文件
		StringBuilder xml = new StringBuilder();
		xml.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
		xml.append("<signal version=\"" + VERSION + "\" device=\""+ PLATFORM + "\""+ " uuid=\""+ tid +"\">") ;
		// 构建metadata节点
		String meta = buildProtocolHeader(metaData);
		xml.append(meta);
		// 媒体数据
		xml.append("<streams>") ;
		xml.append("<stream id=\""+ id +"\" type=\"" + type + "\">") ;
		if(type != null && type.equals(StreamType.FILE)){
			xml.append("<file>") ;
			xml.append("<fileName>" + fileName +"</fileName>") ;
			xml.append("<extName>" + extName +"</extName>") ;
			xml.append("</file>") ;
		}
		xml.append("</stream>") ;
		xml.append("</streams>") ;
		// 结束标记
		xml.append("</signal>") ;
		//
		return xml.toString() ;
	}
	
	/**
	 * 构建元数据xml
	 * 
	 * @param info
	 * @return
	 */
	public static final String buildProtocolHeader(Metadata meta){
		if(meta == null)
			throw new RuntimeException("ERROR: MediaInfo is null !"); 
		// 构建xml文件
		StringBuilder xml = new StringBuilder();
		xml.append("<metadata>");
		xml.append("<siteid>" + meta.getSiteId() + "</siteid>") ;
		xml.append("<username>" + meta.getUsername() + "</username>") ;
		xml.append("<title>" + meta.getTitle() + "</title>") ;
		xml.append("<catalogid>" + meta.getCatalogId() + "</catalogid>") ;
		xml.append("<description>" + meta.getDescription() + "</description>") ;
		xml.append("<who>" + meta.getWho() +"</who>") ;
		xml.append("<what>" + meta.getWhat() +"</what>") ;
		xml.append("<when>" + meta.getTime()+"</when>") ;
		xml.append("<where>" + meta.getPlace() +"</where>") ;
		xml.append("<why>" + "" +"</why>") ;
		xml.append("<gps>" + meta.getGps() +"</gps>") ;
		xml.append("</metadata>");
		try{
			return getUTF8String(xml.toString()) ;
		}catch(Exception e){
			e.printStackTrace();
			return xml.toString() ;
		}
	}
	
	/**
	 * 得到统一的UTF8字符串
	 * @param input
	 * @return
	 */
	public static final String getUTF8String(String input){
		try{
			return new String(input.getBytes("UTF-8"),"UTF-8") ;
		}catch(Exception e){
			e.printStackTrace();
			return input;
		}
	}
}
