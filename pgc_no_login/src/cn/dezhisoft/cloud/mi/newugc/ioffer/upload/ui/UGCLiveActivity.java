package cn.dezhisoft.cloud.mi.newugc.ioffer.upload.ui;

import java.io.Serializable;
import java.util.UUID;

import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import cn.dezhisoft.cloud.mi.newugc.R;

import cn.dezhisoft.cloud.mi.newugc.ioffer.common.Resolution;
import com.sobey.sdk.encoder.AbstractEncoder.AudioCodec;
import com.sobey.sdk.encoder.AbstractEncoder.EncoderType;
import cn.dezhisoft.cloud.mi.newugc.ugc.core.model.Metadata;
import cn.dezhisoft.cloud.mi.newugc.ugc.core.model.Task;
import cn.dezhisoft.cloud.mi.newugc.ugc.core.upload.ILiveUpload.ILiveUploadCallback;
import cn.dezhisoft.cloud.mi.newugc.ugc.core.upload.IUGCUpload;
import cn.dezhisoft.cloud.mi.newugc.ugc.core.upload.UGCUploadLiveImpl;

/**
 * 
 * @author Rosson Chen
 *
 */
public class UGCLiveActivity extends UGCBaseActivity implements SurfaceHolder.Callback,UGCSettingPreference{
	
	public static final String KEY_META				= "_meta" ;
	static final String TAG							= "UGCLiveActivity"; 
	
	private static final int	STATE_IDLE			= 1 << 0 ;
	private static final int	STATE_CONNECT		= 1 << 1 ;
	
	private static final int MSG_BEGIN_CONNECT		= 0 ;
	private static final int MSG_END_CONNECT		= MSG_BEGIN_CONNECT + 1 ;
	private static final int MSG_CONNECT_SUCCESS	= MSG_END_CONNECT + 1 ;
	private static final int MSG_CONNECT_FAILED		= MSG_CONNECT_SUCCESS + 1 ;
	private static final int MSG_LIVE_TIMER			= MSG_CONNECT_FAILED + 1 ;
	private static final int MSG_LIVE_STATE			= MSG_LIVE_TIMER + 1 ;
	private static final int MSG_LIVE_KS			= MSG_LIVE_STATE + 1 ;
	private static final int MSG_LIVE_EN			= MSG_LIVE_KS + 1 ;
	private static final int MSG_LIVE_SERVER_MSG	= MSG_LIVE_EN + 1 ;
	private static final int MSG_LIVE_ERROR			= MSG_LIVE_SERVER_MSG + 1 ;
	private static final int MSG_TEXT_ERROR			= MSG_LIVE_ERROR + 1 ;
	
	static final int MAX_TEXT_QUEUE = 3 ;
	static String[] TEXT_QUEUE = {"","",""} ;
	
	private Metadata 							mMetadata ;
	private SurfaceHolder 						mSurfaceHolder ;
	private UGCUploadLiveImpl 					mLiveUpload ;
	
	private int mWidth,mHeight,mFps,mBitrate ,mSampleRate;
	
	private TextView mSpeedView ,mStateView,mTimeView;
	TextView captureInfo_1 ;
	TextView captureInfo_2 ;
	TextView captureInfo_3 ;
	
	ProgressDialog connectDialog ;
	Dialog hintDialog;
	
	int mState ;
	int speed ;
	int index ;
	String server_msg ;
	String text ;
	
	boolean changeColor ,updataTimer;
	boolean mHasError ;
	String  mErrorText ;
	int fpsCounter ;
	
	LinearLayout shutterView ;
	ImageButton bntStart ;
	
	Thread mThread ;
	int encoder , audioMime;
	
	SharedPreferences settings ;
	
	RelativeLayout	mSurfaceLayout ;
	SurfaceView		mSurfaceView ;
	private int		mSurfaceLayoutWidth ;
	private int		mSurfaceLayoutHeight ;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ugc_upload_live_layout);
		
		mSurfaceLayout		= (RelativeLayout)findViewById(R.id.live_video_layout);
		mSurfaceView	  	= (SurfaceView)findViewById(R.id.live_video_sureface) ;
		mSurfaceView.getHolder().addCallback(this);
		mSurfaceView.getHolder().setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
		
		settings  	= PreferenceManager.getDefaultSharedPreferences(this);
		mHasError 	= false ;
		
		// 初始化上传元数据
		if(!initUGCMetadata()){
			mErrorText	= getString(R.string.ugc_label_live_error_metadata) ;
			mHasError 	= true ;
			Log.e(TAG, mErrorText) ;
		}
		
		// 初始化编码器
		if(!mHasError && !initEncoderParameter()){
			mHasError	= true ;
			showHintDialog(R.string.ugc_label_live_error_encoder);
		}
		
		//
		mState		= STATE_IDLE ;
		changeColor	= false ;
		//
		loadView();
	}
	
	/** 初始化编码器参数 */
	private boolean initEncoderParameter(){
		
		final Editor editor = settings.edit() ;
		audioMime	= settings.getInt(KEY_ENCODER_AUDIO_MIME, -1);
		encoder 	= settings.getInt(KEY_ENCODER, -1);
		
		// encoder type
		if(!(encoder == EncoderType.ENCODER_OMX 
				|| encoder == EncoderType.ENCODER_SOFT 
				|| encoder == EncoderType.ENCODER_STREAM)) {
			Log.e(TAG, "ERROR: Please set encoder type") ;
			return false ;
		}
		
		// audio mime
		if(!(audioMime == AudioCodec.AUDIO_MIME_AAC || audioMime == AudioCodec.AUDIO_MIME_AMR)){
			audioMime	= DEFAULT_AUDIO_MIME ;
			editor.putInt(KEY_ENCODER_AUDIO_MIME, DEFAULT_AUDIO_MIME).commit() ;
			Log.w(TAG, "Set Default Audio mime=" + audioMime) ;
		}
		
		// video size
		mWidth			= settings.getInt(KEY_ENCODER_WIDTH, -1);
		mHeight			= settings.getInt(KEY_ENCODER_HEIGHT, -1);
		mBitrate		= settings.getInt(KEY_ENCODER_BITRATE, 300 * 1024);
		mFps			= settings.getInt(KEY_ENCODER_FPS, 30);
		
		if(mWidth < 0 || mHeight < 0) {
			mWidth		= DEFAULT_WIDTH ;
			mHeight		= DEFAULT_HEIGHT ;
			mBitrate	= DEFAULT_BITRATE ;
			editor.putInt(KEY_ENCODER_WIDTH, mWidth)
			.putInt(KEY_ENCODER_HEIGHT, mHeight)
			.putInt(KEY_ENCODER_BITRATE, mBitrate).commit();
			Log.w(TAG, "Set Default View Size : " + " mWidth=" + mWidth + " mHeight=" + mHeight) ;
		}
		
		if(encoder == EncoderType.ENCODER_OMX){
			audioMime	= AudioCodec.AUDIO_MIME_AAC ;
			mSampleRate	= 44100 ;
		} else if(encoder == EncoderType.ENCODER_SOFT){
			mFps		= 15 ;
			audioMime	= AudioCodec.AUDIO_MIME_AMR ;
			mSampleRate	= 8000 ;
		} else if(encoder == EncoderType.ENCODER_STREAM){
			if(audioMime == AudioCodec.AUDIO_MIME_AAC){
				mSampleRate	= 44100 ;
			} else {
				mSampleRate	= 8000 ;
			}
		}
		
		return true ;
	}
	
	private boolean initUGCMetadata(){
		Serializable s = getIntent().getExtras().getSerializable(KEY_META);
		if(s instanceof Metadata){
			mMetadata	= (Metadata)s ;
			return true ;
		}
		return false ;
	}
	
	private final void loadView(){
		mSpeedView	= (TextView) findViewById(R.id.live_speed_textview);
		mStateView	= (TextView) findViewById(R.id.live_state_textview);
		bntStart	= (ImageButton)findViewById(R.id.bnt_live_start);
		shutterView = (LinearLayout)findViewById(R.id.shutter_view);
		mTimeView	= (TextView)findViewById(R.id.live_time_textview);
		captureInfo_1 = (TextView) findViewById(R.id.live_info_textview_1);
		captureInfo_2 = (TextView) findViewById(R.id.live_info_textview_2);
		captureInfo_3 = (TextView) findViewById(R.id.live_info_textview_3);
		
		hintDialog = new Builder(this)
			.setTitle(R.string.ugc_warring_hint_title)
			.setMessage(R.string.ugc_label_task_live_ks)
			.setNegativeButton(R.string.ugc_warring_hint_bnt_yes, null)
			.create();
		//
		reset();
	}
	
	private final void reset(){
		speed			= 0 ;
		mState			= STATE_IDLE ;
		index 			= 0;
		TEXT_QUEUE 		= new String[]{"","",""};
		server_msg		= "" ;
		text			= "" ;
		changeColor		= false ;
		
		captureInfo_1.setText("");
		captureInfo_2.setText("");
		captureInfo_3.setText("");
		mSpeedView.setText("0 " + getString(R.string.ugc_label_task_live_speed));
		mTimeView.setText("00:00:00");
		mStateView.setText(R.string.ugc_label_task_disconnect) ;
		
		updateShutter(false);
	}
	
	private final void updateShutter(boolean start){
		if(start){
			shutterView.setBackgroundResource(R.drawable.ugc_ic_btn_shutter_press);
			bntStart.setImageResource(R.drawable.ugc_ic_btn_video_record_stop);
		} else {
			shutterView.setBackgroundResource(R.drawable.ugc_ic_btn_shutter_normal);
			bntStart.setColorFilter(0) ;
			bntStart.setImageResource(R.drawable.ugc_ic_btn_video_record);
		}
	}

	public void buttonOnclick(View v){
		
		if(v.getId() == R.id.bnt_live_start){
			
			if(changeColor) return ;
			
			if(mState == STATE_IDLE) {
				
				final String tuid = UUID.randomUUID().toString() ;
				
				if(mLiveUpload.start(mMetadata, tuid)){
					mState	= STATE_CONNECT ;
					mHandler.sendEmptyMessage(MSG_BEGIN_CONNECT);
				}
			} else {
				stop() ;
			}
		}
	}

	@Override
	protected void onStart() {
		super.onStart();
	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		
		mSurfaceLayoutWidth	= mSurfaceLayout.getWidth() ;
		mSurfaceLayoutHeight= mSurfaceLayout.getHeight() ;
		
		mSurfaceHolder		= holder ;
		mSurfaceHolder.setKeepScreenOn(true);
		// 宽高比
		fixedSurfaceLayout(mSurfaceHolder);
		
		// 启动编码器
		bootEncoder() ;
	}
	
	private void fixedSurfaceLayout(SurfaceHolder holder){
		
		// Surface View 的比例计算
		Resolution res = Resolution.calculateDisplayResolution(mSurfaceLayoutWidth, 
				mSurfaceLayoutHeight,
				mWidth, mHeight);
		
		holder.setFixedSize(res.getWidth(), res.getHeight());
		
		// 更新View宽度和高度
		LayoutParams lp = mSurfaceView.getLayoutParams();
		lp.width 	= res.getWidth() ;
		lp.height 	= res.getHeight() ;
		mSurfaceView.setLayoutParams(lp);
	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,int height) {
		
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		mSurfaceHolder		= holder ;
		
		release();
	}
	
	private void bootEncoder(){
		
		if(!mHasError){
			if(mLiveUpload == null){
				mLiveUpload	= new UGCUploadLiveImpl() ;
				mLiveUpload.setCallback(callback);
				mLiveUpload.setEncoder(encoder, this);
				mLiveUpload.setPreview(mSurfaceHolder);
				mLiveUpload.setVideoConfig(mWidth, mHeight, mFps, mBitrate);
				mLiveUpload.setAudioConfig(audioMime,1,mSampleRate,96 * 1024);
				mLiveUpload.prepare() ;
			}
		} else {
			new Builder(this)
			.setTitle(R.string.ugc_warring_hint_title)
			.setMessage(mErrorText)
			.setNegativeButton(R.string.ugc_warring_hint_bnt_yes, new OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					finish();
				}
			})
			.create().show();
		}
	}
	
	private final void stop(){
		
		updataTimer		= false ;
		try{
			if(mThread != null)
				mThread.join() ;
			mThread = null ;
		}catch(Exception e){
			e.printStackTrace() ;
		}
		
		if(mLiveUpload != null){
			mLiveUpload.stop() ;
		}
		
		//
		reset() ;
	}
	
	private final void release(){
		
		stop() ;
		
		if(mLiveUpload != null){
			mLiveUpload.stop() ;
			mLiveUpload.release() ;
			mLiveUpload	= null ;
		}
	}
	
	private final void updateTimer(){
		
		updataTimer = true ;
		
		mThread = new Thread(new Runnable() {
			
			@Override
			public void run() {
				Message msg = null ;
				long timer ;
				int hour ;
				int minute ;
				int second ;
				String time ;
				long enter_timer = System.currentTimeMillis() ;
				
				while(updataTimer){
					try{
						Thread.sleep(1000);
						//
						timer = (System.currentTimeMillis() - enter_timer) / 1000 ;
						hour = (int)(timer / 3600);
						minute = (int)((timer % 3600) / 60);
						second = (int)(timer % 60) ;
						time = (hour > 9 ? hour : "0" + hour) + ":" + (minute > 9 ? minute : "0" + minute) + ":" + (second > 9 ? second : "0" + second) ;
						//
						msg = mHandler.obtainMessage();
						msg.what = MSG_LIVE_TIMER ;
						msg.obj = time ;
						mHandler.sendMessage(msg);
					}catch(Exception e){
						e.printStackTrace() ;
					}
				}
			}
		}) ;
		
		mThread.setDaemon(true);
		mThread.setName("#UpdataTimer");
		mThread.start() ;
	}
	
	private final Handler mHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			switch(msg.what){
			case MSG_BEGIN_CONNECT:
				if(connectDialog == null)
					connectDialog	= ProgressDialog.show(UGCLiveActivity.this, "",  getString(R.string.ugc_label_task_connecting), false, false);
				else
					connectDialog.show() ;
				break ;
			case MSG_END_CONNECT:
				if(connectDialog != null)
					connectDialog.dismiss() ;
				break ;
			case MSG_CONNECT_SUCCESS :
				updateShutter(true);
				break ;
			case MSG_CONNECT_FAILED :
				updateShutter(false);
				showHintDialog(msg.obj != null ? msg.obj.toString() :  getString(R.string.ugc_label_task_network_error)) ;
				break ;
			case MSG_LIVE_TIMER :
				if(updataTimer){
					mTimeView.setText(msg.obj.toString()) ;
					mSpeedView.setText(speed +  getString(R.string.ugc_label_task_live_speed));
				}
				break ;
			case MSG_LIVE_STATE:
				
				String sn = msg.obj != null ? msg.obj.toString() : "" ;
				
				if(sn.equals("")) return ;
				
				if(sn.toLowerCase().equals("onair")){
					changeColor = true ;
				} else if(sn.toLowerCase().equals("ready")){
					changeColor = true ;
				} else {
					changeColor = false ;
				}
				
				if(changeColor){
					bntStart.setColorFilter(0xF8000000) ;
				}else{
					bntStart.setColorFilter(0) ;
				}
				//
				mStateView.setText(sn) ;
				break ;
			case MSG_LIVE_SERVER_MSG :
				
				if(text == null || server_msg == null) return ;
				
				// info
				if(index < MAX_TEXT_QUEUE){
					TEXT_QUEUE[index++] = server_msg ;
				}else {
					TEXT_QUEUE[0] = TEXT_QUEUE[1] ;
					TEXT_QUEUE[1] = TEXT_QUEUE[2] ;
					TEXT_QUEUE[2] = server_msg ;
				}
				//
				captureInfo_1.setText(TEXT_QUEUE[0]);
				captureInfo_2.setText(TEXT_QUEUE[1]);
				captureInfo_3.setText(TEXT_QUEUE[2]);
				
				showHintDialog(text);
				break ;
			case MSG_LIVE_KS :
				if(hintDialog != null && !hintDialog.isShowing())
					hintDialog.show() ;
				stop() ;
				break ;
			case MSG_LIVE_EN :
				
				break ;
			case MSG_LIVE_ERROR :
				showHintDialog(R.string.ugc_label_task_network_error);
				break ;
			case MSG_TEXT_ERROR :
				showHintDialog(msg.obj != null ? msg.obj.toString() : "");
				break ;
			}
		}
	} ;
	
	ILiveUploadCallback callback = new ILiveUploadCallback() {
		
		
		@Override
		public void command(String tuid,int command, int code, String t1,String t2) {
			
			if(command == IUGCUpload.Command.COMMAND_TYPE_COMMAND){
				switch(code){
				case IUGCUpload.CodeServer.CODE_KS :
					mHandler.sendEmptyMessage(MSG_LIVE_KS);
					break ;
				case IUGCUpload.CodeServer.CODE_SN :
					mHandler.obtainMessage(MSG_LIVE_STATE, t1).sendToTarget() ;
					break ;
				case IUGCUpload.CodeServer.CODE_EN ://
					break ;
				case IUGCUpload.CodeServer.CODE_FC :
					server_msg		= t1 ;
					text			= t2 ;
					mHandler.sendEmptyMessage(MSG_LIVE_SERVER_MSG);
					break;
				}
			} else if(command == IUGCUpload.Command.COMMAND_TYPE_ERROR){
				switch(code){
				case IUGCUpload.CodeError.CODE_ERROR_SEND_ERROR :
				case IUGCUpload.CodeError.CODE_ERROR_RECV_ERROR :
					mHandler.sendEmptyMessage(MSG_LIVE_ERROR);
					break ;
				}
			}
		}

		@Override
		public void liveStatistics(String tuid,long send, long lost, int bitRate) {
			speed	= bitRate >> 10 ;
		}

		@Override
		public void notifyChange(String tuid, int state, String message) {
			if(state == Task.STATE.RUNNING){
				mHandler.sendEmptyMessage(MSG_END_CONNECT);
				mHandler.sendEmptyMessage(MSG_CONNECT_SUCCESS);
				updateTimer();
			} else if(state == Task.STATE.STOP){
				mState		= STATE_IDLE ;
				mHandler.sendEmptyMessage(MSG_END_CONNECT);
				mHandler.obtainMessage(MSG_CONNECT_FAILED, message).sendToTarget();
			}
		}
	};
}
