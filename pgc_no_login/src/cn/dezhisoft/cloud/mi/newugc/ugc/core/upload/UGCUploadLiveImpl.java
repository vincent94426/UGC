package cn.dezhisoft.cloud.mi.newugc.ugc.core.upload;

import android.app.Activity;
import android.util.Log;
import android.view.SurfaceHolder;

import cn.dezhisoft.cloud.mi.newugc.ugc.core.config.Text;
import cn.dezhisoft.cloud.mi.newugc.ugc.core.model.ChannelInfo;
import cn.dezhisoft.cloud.mi.newugc.ugc.core.model.ErrorMessage;
import cn.dezhisoft.cloud.mi.newugc.ugc.core.model.Metadata;
import cn.dezhisoft.cloud.mi.newugc.ugc.core.model.Task;
import cn.dezhisoft.cloud.mi.newugc.ugc.core.upload.IUGCUpload.IUGCUploadCallback;
import cn.dezhisoft.cloud.mi.newugc.ugc.core.ws.CWIChannelProxy;
import cn.dezhisoft.cloud.mi.newugc.ugc.core.ws.CWIUserProxy;
import cn.dezhisoft.cloud.mi.newugc.ugc.core.ws.CWSoapResponse;
import cn.dezhisoft.cloud.mi.newugc.ugc.core.ws.CWUploadWebService;
import cn.dezhisoft.cloud.mi.newugc.ugc.upload.ProtocolDefine;
import cn.dezhisoft.cloud.mi.newugc.ugc.upload.UGCUploadImpl;


/**
 * UGC 直播管理
 * 
 * @author Rosson Chen
 *
 */
public final class UGCUploadLiveImpl extends CWSoapResponse implements ILiveUpload , IUGCUploadCallback {

		
	static final String TAG					= "UGCLiveUpload" ;
	
	private final UGCUploadImpl 			mUploadImpl ;
	private ILiveUploadCallback				mCallback ;
	private String							mTempTuid ;
	private String							mTuid ;
	private ChannelInfo						mChannel ;
	private	Metadata						mMetadata ;
	public UGCUploadLiveImpl(){
		mUploadImpl	= UGCUploadImpl.createUGCUpload(UGCUploadImpl.UPLOAD_TYPE_LIVE);
		mUploadImpl.setCallback(this);
		mTempTuid 	= mTuid = "" ;
		mChannel	= null ;
		mCallback	= null ;
		mMetadata	= null ;
	}

	@Override
	public void setEncoder(int encoder, Activity onwer) {
		mUploadImpl.setEncoder(encoder, onwer);
	}

	@Override
	public void setAudioConfig(int mime,int channel,int sampleRate,int bitrate) {
		mUploadImpl.setEncoderAudio(mime,channel,sampleRate,bitrate);
	}

	@Override
	public void setVideoConfig(int width, int height, int fps, int bitRate) {
		mUploadImpl.setEncoderVideo(width, height, fps, bitRate);
	}

	@Override
	public void setPreview(SurfaceHolder holder) {
		mUploadImpl.setEncoderPreview(holder);
	}

	@Override
	public long getCurrentTime() {
		return mUploadImpl.getCurrentTime();
	}

	@Override
	public void setCallback(ILiveUploadCallback callback) {
		mCallback	= callback ;
	}

	@Override
	public void prepare() {
		mUploadImpl.prepare() ;
	}

	@Override
	public boolean start(Metadata meta,String uid) {
		
		if(uid == null || uid.equals("")) return false ;
		
		if(meta == null) return false ;
		
		mTempTuid	= uid ;
		mMetadata	= meta ;
		
		// 请求通道
		CWUploadWebService.ChannelProxy.requestTransferChannel(ProtocolDefine.StreamType.LIVE, uid, this);
		
		return true;
	}
	
	@Override
	public Object dispatchResponse(Object value) {
		
		// UGC web service
		final CWIChannelProxy channelProxy = CWUploadWebService.ChannelProxy;
		final CWIUserProxy userProxy 	= CWUploadWebService.UserProxy;
		final UGCUploadImpl upload 	= mUploadImpl; 
		final Metadata meta			= mMetadata;
		final String uid			= mTempTuid ;
		com.sobey.sdk.encoder.Metadata encVideo ;
		com.sobey.sdk.encoder.Metadata encAudio ;
		
		if(value instanceof ChannelInfo){
			
			boolean connect 	= false ;
			ChannelInfo channel	= (ChannelInfo)value ;
			
			if(meta == null){
				Log.i(TAG, "Live Metadata is null");
				return null ;
			}
			
			String ip		= CWUploadWebService.parserHostIpAddress(channel.getTcp().getHost());
			int port		= channel.getTcp().getPort() ;
			
			if(ip != null){
				// 连接服务器
				// 视频传输参数
				encVideo  = upload.getVideoMetadata() ;
				upload.setTransferVideo(encVideo.findInt(com.sobey.sdk.encoder.Metadata.KEY_MIME), 
						encVideo.findInt(com.sobey.sdk.encoder.Metadata.KEY_VIDEO_WIDTH), 
						encVideo.findInt(com.sobey.sdk.encoder.Metadata.KEY_VIDEO_HEIGHT), 
						encVideo.findInt(com.sobey.sdk.encoder.Metadata.KEY_VIDEO_FPS), 
						encVideo.findInt(com.sobey.sdk.encoder.Metadata.KEY_VIDEO_BITRATE));
				// 音频传输参数
				encAudio  = upload.getAudioMetadata() ;
				upload.setTransferAudio(encAudio.findInt(com.sobey.sdk.encoder.Metadata.KEY_MIME),
						encAudio.findInt(com.sobey.sdk.encoder.Metadata.KEY_AUDIO_CHANNEL), 
						encAudio.findInt(com.sobey.sdk.encoder.Metadata.KEY_AUDIO_SMAPLRATE), 
						encAudio.findInt(com.sobey.sdk.encoder.Metadata.KEY_AUDIO_BITRATE));
				// 元数据
				meta.setUsername(userProxy.getUser().getUsername());
				upload.setMetadata(meta, uid);
				
				connect 	= upload.connect(ip, port);
				// callback
				Log.w(TAG, "open connect :" + (connect ? "success" : "failed!"));
				notifyChange(uid, connect ? Task.STATE.RUNNING : Task.STATE.STOP, connect ? "" : Text.LIVE_CONNECT_ERROR);
				//
				if(!connect) {
					upload.stop() ;
				} else {
					mTuid	= uid ;
					// 在传输通道上执行心跳
					channelProxy.startTransferHeartbeat(channel.getTransferUID());
					// 开始
					upload.start(false) ;
				}
			} else {
				notifyChange(uid, Task.STATE.STOP, channel.getRejectMessage());
			}
			// 通道信息
			mChannel	= channel ;
		} else if(value instanceof ErrorMessage){
			// error
			notifyChange(uid, Task.STATE.STOP, ((ErrorMessage)value).getMessage());
		} else {
			notifyChange(uid, Task.STATE.STOP, Text.APPLY_CHANNEL_FAILED);
		}
		return null;
	}
	
	private void notifyChange(String uid,int state,String message){
		if(mCallback != null) mCallback.notifyChange(uid, state, message);
	}
	
	public void stop() {
		// 释放通道
		if(mChannel != null)
			CWUploadWebService.ChannelProxy.releaseTransferChannel(mChannel.getTransferUID());
		// 停止
		mUploadImpl.stop() ;
	}
	
	@Override
	public void progress(long total, int sendsize,int id) {
		
	}
	
	@Override
	public void liveStatistics(long send, long lost, int bitRate) {
		mCallback.liveStatistics(mTuid,send, lost, bitRate);
	}
	
	@Override
	public void callback(int type, int code, String t1, String t2) {
		mCallback.command(mTuid, type, code, t1,t2);
	}
	
	@Override
	public void release() {
		
		stop();
		
		mUploadImpl.release() ;
	}
}
