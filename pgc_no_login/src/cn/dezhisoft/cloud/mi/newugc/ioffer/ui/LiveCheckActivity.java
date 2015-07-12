package cn.dezhisoft.cloud.mi.newugc.ioffer.ui;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.hardware.Camera;
import android.hardware.Camera.Size;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import cn.dezhisoft.cloud.mi.newugc.R;

import cn.dezhisoft.cloud.mi.newugc.ioffer.config.VideoSize;
import cn.dezhisoft.cloud.mi.newugc.ioffer.upload.ui.UGCSettingPreference;
import com.sobey.sdk.encoder.AbstractEncoder;
import com.sobey.sdk.encoder.AbstractEncoder.AudioCodec;
import com.sobey.sdk.encoder.AbstractEncoder.EncoderType;
import com.sobey.sdk.encoder.AbstractEncoder.VideoCodec;
import com.sobey.sdk.encoder.IEncoderCallback;

/**
 * 编码器检测
 * 
 * @author Rosson Chen
 *
 */
public final class LiveCheckActivity extends BaseActivity implements SurfaceHolder.Callback, UGCSettingPreference {

	static final String TAG	= "LiveCheckActivity" ;
	private static final int MSG_START_CHECK		= 1 ;
	private static final int MSG_END_CHECK			= MSG_START_CHECK + 1 ;
	static final int FREQ_1G						= 1000000 ;
	static final int MAX_WIDTH_1CPU					= 480;
	static final int MAX_HEIGHT_1CPU				= 320;
	static final int MAX_WIDH_NCPU					= 640;
	static final int MAX_HEIGHT_NCPU				= 480;
	
	ProgressDialog checkDialog;
	int fpsCounter ;
	int encoder ;
	SurfaceHolder mSurfaceHolder ;
	SharedPreferences settings ;
	boolean mStart ;
	List<Size> cameraSizeList ;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.live_check_layout);
		settings  	= PreferenceManager.getDefaultSharedPreferences(this);
		mStart		= false ;
		
		SurfaceView view = (SurfaceView)findViewById(R.id.surface_view);
		view.getHolder().addCallback(this);
		view.getHolder().setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
		
		cameraSizeList = loadCameraSupportSize();
	}
	
	/** 加载摄像头支持的分辨率 */
	private List<Size> loadCameraSupportSize(){
		
		List<Size> list = null ;
		try{
			Camera camera = Camera.open() ;
			list = camera.getParameters().getSupportedPreviewSizes() ;
			
			if(list.size() > 0){
				// 排序
				Collections.sort(list, new Comparator<Camera.Size>() {
					
					@Override
					public int compare(Camera.Size size1, Camera.Size size2) {
						if(size2.width < size1.width 
								|| (size2.width == size1.width && size2.height < size1.height))
							return 1;
						else if(size2.width == size1.width)
							return 0 ;
						else 
							return -1 ;
					}
				});
			}
			
			camera.release() ;
			camera = null ;
		} catch(Exception e){
			e.printStackTrace() ;
		}
		
		return list ;
	}
	
	private void saveVideoSize(){
		
		if(cameraSizeList == null) return ;
		
		// 录制
		for(Size size : cameraSizeList){
			VideoSize vs = new VideoSize() ;
			vs.setWidth(size.width);
			vs.setHeight(size.height);
			vs.setType(VideoSize.Type.RECORDER);
			mAccessDatabase.saveObject(vs);
		}
		
		// 直播
		if(encoder == EncoderType.ENCODER_SOFT){
			
			int cpu		= getCurCpuNumber() ;
			int freq 	= getCurCpuFreq();
			
			Log.w(TAG, "CPU: " + cpu + " FREQ: " + freq);
			
			ArrayList<VideoSize> list = new ArrayList<VideoSize>() ;
			
			// 单核
			if(cpu == 1){
				// <= 1G
				if(freq <= FREQ_1G){
					for(Size size : cameraSizeList){
						if(size.height <= MAX_HEIGHT_1CPU && size.width <= MAX_WIDTH_1CPU){
							VideoSize vs = new VideoSize() ;
							vs.setWidth(size.width);
							vs.setHeight(size.height);
							vs.setType(VideoSize.Type.LIVE);
							list.add(vs) ;
						}
					}
				} 
				// > 1000000
				else {
					for(Size size : cameraSizeList){
						if(size.height <= MAX_HEIGHT_NCPU && size.width <= MAX_WIDH_NCPU){
							VideoSize vs = new VideoSize() ;
							vs.setWidth(size.width);
							vs.setHeight(size.height);
							vs.setType(VideoSize.Type.LIVE);
							list.add(vs) ;
						}
					}
				}
			} 
			// 多核
			else if(cpu > 1){
				for(Size size : cameraSizeList){
					if(size.height <= MAX_HEIGHT_NCPU && size.width <= MAX_WIDH_NCPU){
						VideoSize vs = new VideoSize() ;
						vs.setWidth(size.width);
						vs.setHeight(size.height);
						vs.setType(VideoSize.Type.LIVE);
						list.add(vs) ;
					}
				}
			}
			
			// 保存直播分辨率
			if(list.size() > 0){
				for(VideoSize size : list){
					mAccessDatabase.saveObject(size);
				}
			} else {
				Size size = cameraSizeList.get(0);
				VideoSize vs = new VideoSize() ;
				vs.setWidth(size.width);
				vs.setHeight(size.height);
				vs.setType(VideoSize.Type.LIVE);
				mAccessDatabase.saveObject(vs);
			}
		} 
		// 流分析编码
		else {
			for(Size size : cameraSizeList){
				VideoSize vs = new VideoSize() ;
				vs.setWidth(size.width);
				vs.setHeight(size.height);
				vs.setType(VideoSize.Type.LIVE);
				mAccessDatabase.saveObject(vs);
			}
		}
	}
	
	public int getCurCpuFreq() {
		FileReader fr = null;
		try {
			fr = new FileReader("/sys/devices/system/cpu/cpu0/cpufreq/scaling_cur_freq");
			BufferedReader br = new BufferedReader(fr);
			String text = br.readLine();
			String result = text.trim();
			return Integer.parseInt(result);
		} catch (Exception e) {
			e.printStackTrace() ;
		} finally {
			if (fr != null) {
				try {
					fr.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return 0;
	}

	public int getCurCpuNumber() {
		return Runtime.getRuntime().availableProcessors();
	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		mSurfaceHolder	= holder ;
	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,int height) {
		mSurfaceHolder	= holder ;
		
		if(!mStart) autoChoiceEncoderType();
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		mSurfaceHolder	= holder ;
	}
	
	/** 自动选择编码器 */
	private void autoChoiceEncoderType(){
		
		Log.d(TAG, "autoChoiceEncoderType") ;
		
		// 1. 首先判断StreamEncoder 是否支持
		// 2. 其次选择软件编码
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				
				mStart				= true ;
				mHandler.obtainMessage(MSG_START_CHECK).sendToTarget() ;
				final Editor editor = settings.edit() ;
				final int checkTime = 10 * 1000 ;
				fpsCounter			= 0 ;
				
				AbstractEncoder enc = AbstractEncoder.createEncoder(LiveCheckActivity.this, EncoderType.ENCODER_STREAM);
				enc.setVideoMime(VideoCodec.VIDEO_MIME_AVC);
				enc.setVideoSize(176, 144);
				enc.setVideoBitrate(300*1024);
				enc.setVideoFramerate(30) ;
				enc.setPreviewDisplay(mSurfaceHolder);
				enc.setAudioMime(AudioCodec.AUDIO_MIME_AMR);
				enc.setAudioChannel(1);
				enc.setAudioSampleRate(8000);
				enc.setShowDebug(false);
				enc.setEncodeCallback(new IEncoderCallback() {
					
					@Override
					public void onVideo(byte[] frame, int length, long timecode,boolean iFrame, int fps) {
						fpsCounter++ ;
					}
					
					@Override
					public void onAudio(byte[] frame, int length, long timecode) {
						
					}
				}) ;
				enc.prepare() ;
				enc.start() ;
				
				long enter = System.currentTimeMillis() ;
				
				while(System.currentTimeMillis() - enter < checkTime){
					try{
						Thread.sleep(50);
					}catch(Exception e){
						e.printStackTrace() ;
					}
				}
				
				int fps = enc.getVideoMetadata().findInt(com.sobey.sdk.encoder.Metadata.KEY_VIDEO_FPS);
				// 设置编码器
				if(fpsCounter > fps * 3){
					encoder	= EncoderType.ENCODER_STREAM ;
					editor.putInt(KEY_ENCODER, EncoderType.ENCODER_STREAM).commit();
				} else {
					encoder	= EncoderType.ENCODER_SOFT ;
					editor.putInt(KEY_ENCODER, EncoderType.ENCODER_SOFT).commit();
				}
				// release
				enc.stop() ;
				enc.release() ;
				mHandler.obtainMessage(MSG_END_CHECK).sendToTarget() ;
				mStart	= false ;
			}
		}).start() ;
	}

	private final Handler mHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			switch(msg.what){
			case MSG_START_CHECK :
				checkDialog	= ProgressDialog.show(LiveCheckActivity.this, "",  getString(R.string.ugc_label_check_live_encoder), false, false);
				break ;
			case MSG_END_CHECK :
				// 检测完成
				if(checkDialog != null)
					checkDialog.cancel() ;
				// 是否软件编码器
				if(encoder == EncoderType.ENCODER_SOFT){
					showMessageDialog(getString(R.string.ugc_label_live_soft));
				}
				// 保存支持的预览视频大小
				saveVideoSize() ;
				// 退出
				finish() ;
				break ;
			}
		}
	} ;
}
