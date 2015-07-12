package cn.dezhisoft.cloud.mi.newugc.ugc.upload;

import java.lang.ref.WeakReference;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import android.app.Activity;
import android.util.Log;
import android.view.Surface;
import android.view.SurfaceHolder;

import com.sobey.sdk.encoder.AbstractEncoder;
import com.sobey.sdk.encoder.IEncoderCallback;
import com.sobey.sdk.encoder.PrimevalEncoder;
import cn.dezhisoft.cloud.mi.newugc.ugc.core.model.Metadata;
import cn.dezhisoft.cloud.mi.newugc.ugc.core.upload.IUGCUpload;

/**
 * 上传实现.直播和文件上传实现.包含了调用公共模块
 * 
 * @author Rosson Chen
 *
 */
public final class UGCUploadImpl implements IUGCUpload {
	
	static final String 	TAG						= "UGCUploadImpl" ;
	static final boolean	DEBUG					= false ;

	/** 直播上传 */
	public final static int	UPLOAD_TYPE_LIVE 	= 1 ;
	
	/** 文件上传 */
	public final static int	UPLOAD_TYPE_FILE 	= 2 ;
	
	/** 时间格式 */
	protected static final SimpleDateFormat FORMATTER = new SimpleDateFormat("HH:mm");
	
	/** native context*/
	private long 						mNativeContext ;
	protected IUGCUploadCallback 		mCallback ;
	private AbstractEncoder				mEncoder ;
	private SurfaceHolder				mSurfaceHolder ;
	private int 						mEncoderType ;
	private final com.sobey.sdk.encoder.Metadata	mVideoMeta = new com.sobey.sdk.encoder.Metadata();
	private final com.sobey.sdk.encoder.Metadata	mAudioMeta = new com.sobey.sdk.encoder.Metadata();
	private static boolean 				mLoad = false ;
	
	private UGCUploadImpl(int type){
		
		mEncoder		= null ;
		mEncoderType	= 0 ;
		mVideoMeta.clear() ;
		mAudioMeta.clear() ;
		// 初始化jni
		mNativeContext 	= _native_init(new WeakReference<UGCUploadImpl>(this),type);
		//
		if(mNativeContext == 0)
			throw new RuntimeException(TAG + "_native_init() error.");
	}
	
	/** 创建实例 */
	public static final UGCUploadImpl createUGCUpload(int type){
		return new UGCUploadImpl(type);
	}
	
	/* (non-Javadoc)
	 * @see cn.dezhisoft.cloud.mi.newugc.ugc.upload.UGCUpload#setMetadata(java.util.HashMap, java.lang.String)
	 */
	@Override
	public void setMetadata(Metadata meta, String tid){
		
		if(meta == null){
			Log.e(TAG, "meta is null");
			return ;
		}
		
		if(tid == null || tid.trim().equals("")){
			Log.e(TAG, "tid is null");
			return ;
		}
		
		String metadata = ProtocolDefine.buildProtocolHeader(meta) ;
		String uid 		= ProtocolDefine.getUTF8String(tid);
		
		if(DEBUG){
			Log.d(TAG, "tuid : " + uid) ;
			Log.d(TAG, "metadata : " + metadata) ;
		}
		
		setMetadata(metadata,uid);
	}
	
	/* (non-Javadoc)
	 * @see cn.dezhisoft.cloud.mi.newugc.ugc.upload.UGCUpload#setCallback(cn.dezhisoft.cloud.mi.newugc.ugc.upload.IUploadCallback)
	 */
	@Override
	public void setCallback(IUGCUploadCallback callback){
		mCallback		= callback ;
	}
	
	/* (non-Javadoc)
	 * @see 
	 */
	@Override
	public void setFilePath(String path) {
		
		if(DEBUG)
			Log.d(TAG, "Private TCP path=" + path) ;
		
		_setFilePath(mNativeContext,path);
	}

	/* (non-Javadoc)
	 * @see 
	 */
	@Override
	public void setFileInfo(String name, String extName) {
		
		if(DEBUG)
			Log.d(TAG, "Private TCP file metadata : filename=" + name + " ext=" + extName) ;
		
		_setFileInfo(mNativeContext,name,extName);
	}

	/* (non-Javadoc)
	 * @see 
	 */
	@Override
	public boolean isUploadFile() {
		return _isUploadFile(mNativeContext);
	}

	/* (non-Javadoc)
	 * @see cn.dezhisoft.cloud.mi.newugc.ugc.upload.ILiveUpload#setEncoder(int, android.app.Activity)
	 */
	@Override
	public void setEncoder(int encoder,Activity onwer){
		
		mEncoderType		= encoder ;
		String packName 	= "" ;
		String libName 		= "" ;
		String methodName	= "" ;
		
		switch(mEncoderType){
		case AbstractEncoder.EncoderType.ENCODER_STREAM :
			mEncoder	= AbstractEncoder.createEncoder(onwer, mEncoderType);
			mEncoder.setEncodeCallback(StreamCallback);
			break ;
		case AbstractEncoder.EncoderType.ENCODER_SOFT :
			mEncoder	= new PrimevalEncoder();
			mEncoder.setEncodeCallback(SoftCallback);
			break ;
		case AbstractEncoder.EncoderType.ENCODER_OMX :
			
			if(onwer == null)
				throw new RuntimeException("Activity is null") ;
			
			packName = onwer.getPackageName() ;
			int sdk  = android.os.Build.VERSION.SDK_INT;
			
			// 9,10 --->2.3 | 2.3.3
			if (sdk < 11) {
				
				if(!mLoad){
					System.loadLibrary("encoder2.3.x");
					mLoad	= true ;
				}
				
				libName = "/data/data/"+ packName +"/lib/libencoder2.3.x.so";
			} 
			// 14,15--->4.0 | 4.0.3
			else if (sdk > 13 && sdk <= 15){
				
				if(!mLoad){
					System.loadLibrary("encoder4.0.x");
					mLoad	= true ;
				}
				
				libName = "/data/data/"+ packName + "/lib/libencoder4.0.x.so";
			} 
			// android4.1
			else if(sdk == 16){
				
				if(!mLoad){
					System.loadLibrary("encoder4.1.x");
					mLoad	= true ;
				}
				
				libName 	= "/data/data/"+ packName + "/lib/libencoder4.1.x.so";
				methodName	= "_Z16createOMXEncoderv" ;
			}
			break ;
		}
		
		
		// 设置对应OMX库
		_setEncoder(mNativeContext,encoder,libName,methodName);
	}
	
	@Override
	public com.sobey.sdk.encoder.Metadata getVideoMetadata() {
		return (mEncoderType == AbstractEncoder.EncoderType.ENCODER_STREAM) && mEncoder != null ? mEncoder.getVideoMetadata(): mVideoMeta;
	}
	
	@Override
	public com.sobey.sdk.encoder.Metadata getAudioMetadata() {
		return (mEncoderType == AbstractEncoder.EncoderType.ENCODER_STREAM) && mEncoder != null ? mEncoder.getAudioMetadata(): mAudioMeta;
	}

	/* (non-Javadoc)
	 * @see cn.dezhisoft.cloud.mi.newugc.ugc.upload.ILiveUpload#setVideoConfig(int, int, int, int)
	 */
	@Override
	public void setEncoderVideo(int width,int height,int fps,int bitRate){
		
		if(mEncoder != null){
			mEncoder.setVideoSize(width, height);
			mEncoder.setVideoFramerate(fps);
			mEncoder.setVideoBitrate(bitRate) ;
		}
		
		final com.sobey.sdk.encoder.Metadata	meta = mVideoMeta ;
		
		if(mEncoderType	== AbstractEncoder.EncoderType.ENCODER_SOFT 
				|| mEncoderType == AbstractEncoder.EncoderType.ENCODER_OMX){
			meta.putInt(com.sobey.sdk.encoder.Metadata.KEY_MIME, AbstractEncoder.VideoCodec.VIDEO_MIME_AVC);
			meta.putInt(com.sobey.sdk.encoder.Metadata.KEY_VIDEO_WIDTH, width);
			meta.putInt(com.sobey.sdk.encoder.Metadata.KEY_VIDEO_HEIGHT, height);
			meta.putInt(com.sobey.sdk.encoder.Metadata.KEY_VIDEO_FPS, fps);
			meta.putInt(com.sobey.sdk.encoder.Metadata.KEY_VIDEO_BITRATE, bitRate);
		}
		
		// 设置视频参数
		_setVideoConfig(mNativeContext,AbstractEncoder.VideoCodec.VIDEO_MIME_AVC, width, height, fps,bitRate);
	}
	
	/* (non-Javadoc)
	 * @see cn.dezhisoft.cloud.mi.newugc.ugc.upload.ILiveUpload#setPreview(android.view.SurfaceHolder)
	 */
	@Override
	public void setEncoderPreview(SurfaceHolder holder){
		// 检测
		if(holder == null)
			throw new RuntimeException(TAG + "setPreview() is null.");
		
		mSurfaceHolder	= holder ;
		
		// 设置预览
		if(mEncoder != null){
			mEncoder.setPreviewDisplay(holder);
		} else {
			_setDisplay(mNativeContext,holder.getSurface());
		}
	}
	
	/* (non-Javadoc)
	 * @see cn.dezhisoft.cloud.mi.newugc.ugc.upload.ILiveUpload#setAudioConfig(int)
	 */
	@Override
	public void setEncoderAudio(int mime,int channel,int sampleRate,int bitrate){
		
		// 检测音频类型
		if(!(mime == AbstractEncoder.AudioCodec.AUDIO_MIME_AMR || 
				mime == AbstractEncoder.AudioCodec.AUDIO_MIME_AAC))
			throw new RuntimeException(TAG + "Please chose audio mime: AUDIO_MIME_AMR | AUDIO_MIME_AAC");
		
		final com.sobey.sdk.encoder.Metadata	meta = mAudioMeta ;
		
		// 设置编码器音频
		if(mEncoder != null){
			mEncoder.setAudioMime(mime);
			mEncoder.setAudioChannel(channel);
			mEncoder.setAudioSampleRate(sampleRate);
			mEncoder.setAudioBitrate(bitrate);
		}
		
		if(mEncoderType	== AbstractEncoder.EncoderType.ENCODER_SOFT){
			meta.putInt(com.sobey.sdk.encoder.Metadata.KEY_MIME, AbstractEncoder.AudioCodec.AUDIO_MIME_AMR);
			meta.putInt(com.sobey.sdk.encoder.Metadata.KEY_AUDIO_CHANNEL, channel);
			meta.putInt(com.sobey.sdk.encoder.Metadata.KEY_AUDIO_SMAPLRATE, sampleRate);
			meta.putInt(com.sobey.sdk.encoder.Metadata.KEY_AUDIO_BITRATE, bitrate);
		} else if(mEncoderType == AbstractEncoder.EncoderType.ENCODER_OMX){
			meta.putInt(com.sobey.sdk.encoder.Metadata.KEY_MIME, AbstractEncoder.AudioCodec.AUDIO_MIME_AAC);
			meta.putInt(com.sobey.sdk.encoder.Metadata.KEY_AUDIO_CHANNEL, channel);
			meta.putInt(com.sobey.sdk.encoder.Metadata.KEY_AUDIO_SMAPLRATE, sampleRate);
			meta.putInt(com.sobey.sdk.encoder.Metadata.KEY_AUDIO_BITRATE, bitrate);
		}
		// 设置音频参数
		_setAudioConfig(mNativeContext,mime, 1, mime == AbstractEncoder.AudioCodec.AUDIO_MIME_AMR ? 8000 : sampleRate,bitrate);
	}
	
	@Override
	public void setTransferVideo(int mime, int width, int height, int fps,int bitrate) {
		_setTransferVideo(mNativeContext, mime, width, height, fps, bitrate);
	}

	@Override
	public void setTransferAudio(int mime, int channel, int samplerate,int bitrate) {
		_setTransferAudio(mNativeContext, mime, channel, samplerate, bitrate);
	}

	/* (non-Javadoc)
	 * @see cn.dezhisoft.cloud.mi.newugc.ugc.upload.UGCUpload#prepare()
	 */
	@Override
	public void prepare() {
		
		// 启动编码器
		if(mEncoder != null){
			mEncoder.prepare() ;
			mEncoder.start() ;
		}
		//
		if(mSurfaceHolder != null)
			mSurfaceHolder.setKeepScreenOn(true);
		
		// 对于直播而言,启动编码器
		if(!_prepare(mNativeContext)){
			Log.e(TAG, "prepare failed.");
		}
	}
	
	/**
	 * 设置任务信息
	 */
	private void setMetadata(String meta,String tid) {
		_setTaskInfo(mNativeContext,meta,ProtocolDefine.PLATFORM,tid);
	}

	/* (non-Javadoc)
	 * @see cn.dezhisoft.cloud.mi.newugc.ugc.upload.ILiveUpload#setFTPServerInfo()
	 */
	@Override
	public void setFTPServerInfo(String ip, int port, String uname,String upwd) {
		_setFTPInfo(mNativeContext, ip, port, uname, upwd);
	}
	
	@Override
	public void setFTPDirectory(String dir) {
		_setFTPDir(mNativeContext, dir);
	}

	/* (non-Javadoc)
	 * @see cn.dezhisoft.cloud.mi.newugc.ugc.upload.ILiveUpload#startFTPUpload()
	 */
	@Override
	public boolean startFTPUpload(ArrayList<String> files,boolean resume) {
		
		if(files == null || files.isEmpty()) return false;
		
		final int size = files.size() ;
		// mnt/sdcard/dddx/ddd.mp3
		final String[] fs = new String[size * 3];
		
		for(int i = 0, n = 0 ; i < size ; i++, n+=3){
			String path = files.get(i) ;
			int pos		= path.lastIndexOf(".") ;
			
			fs[n] 	= path.substring(path.lastIndexOf("/") + 1, pos);// name
			fs[n+1] = path.substring(pos);// ext name
			fs[n+2] = path ;// path
		}
		
		return _startUpload(mNativeContext, fs, resume);
	}

	/* (non-Javadoc)
	 * @see cn.dezhisoft.cloud.mi.newugc.ugc.upload.ILiveUpload#getCurrentTime()
	 */
	@Override
	public long getCurrentTime(){
		return _getCurrentTime(mNativeContext);
	}
	
	/* (non-Javadoc)
	 * @see cn.dezhisoft.cloud.mi.newugc.ugc.upload.UGCUpload#connect(java.lang.String, int)
	 */
	@Override
	public boolean connect(String ip, int port) {
		
		if(DEBUG)
			Log.d(TAG, "ip=" + ip + " port=" + port) ;
		
		return _connect(mNativeContext,ip,port);
	}

	/* (non-Javadoc)
	 * @see cn.dezhisoft.cloud.mi.newugc.ugc.upload.UGCUpload#start(boolean resume)
	 */
	@Override
	public void start(boolean resume) {
		if(!_start(mNativeContext,resume)){
			Log.e(TAG, "Start failed.");
		}
	}

	/* (non-Javadoc)
	 * @see cn.dezhisoft.cloud.mi.newugc.ugc.upload.UGCUpload#stop()
	 */
	@Override
	public void stop() {
		_stop(mNativeContext);
	}
	

	/* (non-Javadoc)
	 * @see cn.dezhisoft.cloud.mi.newugc.ugc.upload.UGCUpload#release()
	 */
	@Override
	public void release() {
		
		stop();
		
		if(mSurfaceHolder != null)
			mSurfaceHolder.setKeepScreenOn(false);
		
		mVideoMeta.clear() ;
		mAudioMeta.clear() ;
		
		if(mEncoder != null){
			mEncoder.stop() ;
			mEncoder.release() ;
			mEncoder	= null ;
		}
		
		if(mNativeContext != 0)
			_release(mNativeContext);
		
		mNativeContext	= 0 ;
	}
	
	
	/**AMR音频数据到来*/
	private static final void amrFrameCallback(Object me ,byte[] amrData,int size){
		
	}
	
	
	/** 文件进度 */
	private static final void progressCallback(Object me ,long total,int sendSize,int id) {
		UGCUploadImpl upload = (UGCUploadImpl) ((WeakReference) me).get();
		if(upload == null) 
			return ;
		//
		if(upload.mCallback != null) 
			upload.mCallback.progress(total, sendSize,id);
	}
	
	/** 直播统计 */
	private static final void liveStatistics(Object me,long send, long lost, int bitRate){
		UGCUploadImpl upload = (UGCUploadImpl) ((WeakReference) me).get();
		if(upload == null || upload.mCallback == null) 
			return ;
		
		upload.mCallback.liveStatistics(send, lost, bitRate);
	}
	
	/** 消息回调 */
	private static final void callback(Object me,int command,int code,byte[] value){
		UGCUploadImpl upload = (UGCUploadImpl) ((WeakReference) me).get();
		if(upload == null || upload.mCallback == null) 
			return ;
		
		String text = "" ;
		String t1	= "" ;
		String t2   = "" ;
		
		switch(command){
		case Command.COMMAND_TYPE_COMMAND :
			// 消息文本
			text = buildUTF16String(value) ;
			
			if(text == null) break ;
			//
			if(code == CodeServer.CODE_FC){//前场指导
				//{557780}{dddasfddddddd}
				int time = (int) upload.getCurrentTime() ;
				//
				if(time <= 0) break ;
				// 
				int start = text.indexOf("{", 0);
				int end   = text.indexOf("}", 0);
				if(start >= 0 && end > start){
					// 解析服务器时间戳
					int frame_t = Integer.parseInt(text.substring(start + 1 , end));
					// 解析消息文本
					t1 	= text.substring(end + 2, text.length() - 1) ;
					// 时间差
					int currentTime = (int)((time - frame_t) / 1000 )  ;
					String time_t	= FORMATTER.format(new Date()) + " : " ;
					t2 				= String.valueOf(currentTime) + " seconds before" + "\n" + t1;
					// 组合消息
					t1				= time_t + t1 ;
					t2				= time_t + t2 ;
					// 回调
					upload.mCallback.callback(Command.COMMAND_TYPE_COMMAND, code, t1, t2);
				}
			} else if(code == CodeServer.CODE_SN){
				// 分析
				t1 = text.substring(1, text.length() - 1);
				//
				upload.mCallback.callback(Command.COMMAND_TYPE_COMMAND, code, t1, t2);
			} else {
				upload.mCallback.callback(Command.COMMAND_TYPE_COMMAND, code, text,"");
			}
			break ;
		case Command.COMMAND_TYPE_VERFIY :
			// 消息文本
			text = buildUTF8String(value) ;
			// 回调
			upload.mCallback.callback(Command.COMMAND_TYPE_VERFIY, code, text,"");
			break ;
		case Command.COMMAND_TYPE_ERROR :
			upload.mCallback.callback(Command.COMMAND_TYPE_ERROR, code, buildUTF16String(value),"");
			break ;
		}
	}
	
	/**
	 * 创建字符串
	 * @param value
	 * @return
	 */
	private final static String buildUTF16String(byte[] value){
		if(value == null || value != null && value.length == 0)
			return null ;
		// c++
		if(value.length % 2 == 0){
			byte[] temp = new byte[value.length];
			int size = value.length - 1 ;
			for(int i = 0 ; i < size; i+=2){
				temp[i] = value[i+1];
				temp[i+1] = value[i];
			}
			try{
				return new String(temp,"UTF-16");
			}catch(Exception e){
				e.printStackTrace() ;
				return null;
			}
		} else {
			return null ;
		}
	}
	
	/** utf-8 */
	private final static String buildUTF8String(byte[] value){
		if(value == null || value != null && value.length == 0)
			return null ;
		// c++
		try{
			return new String(value,"UTF-8");
		}catch(Exception e){
			e.printStackTrace() ;
			return null;
		}
	}
	
	/** native init */
	private native long _native_init(Object weak_this,int type) ;
	
	/** live encoder type */
	private native void _setEncoder(long handle,int encoder,String libName,String methodName) ;
	
	/** task info */
	private native void _setTaskInfo(long handle,String meta,String device,String tuid);
	
	private native boolean _setTransferVideo(long handle,int mime,int width,int height,int fps,int bitrate) ;
	
	private native boolean _setTransferAudio(long handle,int mime,int channel,int samplerate,int bitrate) ;
	
	private native boolean _connect(long handle,String ip,int port);
	
	private native void _setVideoConfig(long handle,int mime,int width,int height,int fps,int bitRate);

	private native void _setDisplay(long handle,Surface surface);
	
	private native void _setAudioConfig(long handle,int mime,int channel,int sampleRate,int bitrate);
	
	/** for stream encoder video*/
	private native void _onVideo(long handle,byte[] frame,int length,long time,boolean iFrame,int fps) ;
	
	/** for stream encoder audio*/
	private native void _onAudio(long handle,byte[] frame,int length,long time) ;
	
	/** for soft encoder audio*/
	private native void _onPCMData(long handle,byte[] data,int length);
	
	/** for soft encoder video*/
	private native void _onYUVData(long handle,byte[] data,int length);
	
	/** file path */
	private native void _setFilePath(long handle,String path) ;
	
	/** transfer mode */
	private native void _setFTPInfo(long handle,String ip,int port,String uname,String upwd);
	
	/** upload files*/
	private native boolean _setFTPDir(long handle,String dir);
	
	/** upload files*/
	private native boolean _startUpload(long handle,String[] files,boolean resume);
	
	/** file name & file ext name */
	private native void _setFileInfo(long handle,String name,String extName);
	
	/** upload file*/
	private native boolean _isUploadFile(long handle);
	
	/** current time*/
	private native long _getCurrentTime(long handle);
	
	private native boolean _prepare(long handle);
	
	private native boolean _start(long handle,boolean resume);
	
	/** 停止发送*/
	private native void _stop(long handle);
	
	/** 释放所有资源*/
	private native void _release(long handle) ;
	
	
	/**设置回传设备的gmid*/
	private native void setDeviceUni(long handle,String dumid);
	
	/**更新元数据(gps)*/
	private native void _updateMetaData(long handle,String szMeta);
	
	
	
	
	private final IEncoderCallback StreamCallback = new IEncoderCallback() {
		
		@Override
		public final void onVideo(byte[] data, int length, long time, boolean iframe, int fps) {
			_onVideo(mNativeContext,data, length, time, iframe, fps);
		}
		
		@Override
		public final void onAudio(byte[] data, int length, long time) {
			_onAudio(mNativeContext,data, length, time);
		}
	};
	
	private final IEncoderCallback SoftCallback = new IEncoderCallback() {
		
		@Override
		public final void onVideo(byte[] data, int length, long time, boolean iframe, int fps) {
			_onYUVData(mNativeContext,data, length);
		}
		
		@Override
		public final void onAudio(byte[] data, int length, long time) {
			_onPCMData(mNativeContext,data, length);
		}
	};
	
	
	
	
	static {
		
		int sdk = android.os.Build.VERSION.SDK_INT;
		
		if (sdk < 9) {
			throw new RuntimeException("Not support This Platform, sdk=" + sdk);
		}
		
		Log.d(TAG, "Android Device SDK version=" + sdk);

		if (sdk >= 9) {
			// common network lib
			System.loadLibrary("ugc_common_network");
			// util lib
			System.loadLibrary("native_util");
			// soft encoder lib
			System.loadLibrary("soft_encoder");
			// native lib
			System.loadLibrary("native_ugc_upload");
		}
	}
	
}
