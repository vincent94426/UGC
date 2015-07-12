package cn.dezhisoft.cloud.mi.newugc.ioffer.upload.ui;

import com.sobey.sdk.encoder.AbstractEncoder.AudioCodec;
import com.sobey.sdk.encoder.AbstractEncoder.EncoderType;

/**
 * UGC live and Recorder file setting
 * 
 * @author Rosson Chen
 *
 */
public interface UGCSettingPreference {
	
	//
	public static final int    DEFAULT_WIDTH			= 176 ;
	public static final int    DEFAULT_HEIGHT			= 144 ;
	public static final int    DEFAULT_BITRATE			= 300 * 1024 ;
	public static final int    DEFAULT_ENCODER			= EncoderType.ENCODER_STREAM  ;
	public static final int    DEFAULT_AUDIO_MIME		= AudioCodec.AUDIO_MIME_AMR  ;
	
	// 1->OMX 2->Soft 3-Stream
	public static final String KEY_ENCODER 				= "ugc_use_encoder" ;
	
	// live {AMR=1,AAC=2}
	public static final String KEY_ENCODER_AUDIO_MIME	= "ugc_encoder_audio_mime" ;
	public static final String KEY_ENCODER_WIDTH 		= "ugc_encoder_width" ;
	public static final String KEY_ENCODER_HEIGHT 		= "ugc_encoder_height" ;
	public static final String KEY_ENCODER_FPS			= "ugc_encoder_fps" ;
	public static final String KEY_ENCODER_BITRATE 		= "ugc_encoder_bitrate" ;
	
	// recorder
	public static final String KEY_RECORDER_WIDTH 		= "ugc_recorder_width" ;
	public static final String KEY_RECORDER_HEIGHT 		= "ugc_recorder_height" ;
	public static final String KEY_RECORDER_BITRATE		= "ugc_recorder_bitrate" ;	
}
