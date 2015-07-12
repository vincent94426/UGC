package cn.dezhisoft.cloud.mi.newugc.ioffer.upload.ui;

import java.io.File;
import java.io.IOException;

import android.app.Activity;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.hardware.Camera;
import android.hardware.Camera.Parameters;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import cn.dezhisoft.cloud.mi.newugc.R;

import cn.dezhisoft.cloud.mi.newugc.ioffer.common.Resolution;

/**
 * 
 * @author Rosson Chen
 *
 */
public final class UGCRecordActivity extends Activity implements SurfaceHolder.Callback,UGCSettingPreference{

	public static final int REQUEST_VIDEOPATH_CODE = 0x0f000000;
	public static final String VIDEO_PATH = "_videopath";
	public static final String VIDEO_NAME = "_videoname";
	
	static final String TAG	= "UGCRecordActivity" ;
	
	private static final String ParentPath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/VIDEO/";
	private String fileName 		= "";

	static final int UPDATE_TIME = 1;
	static final int UPDATE_SHOW_DIALOG = UPDATE_TIME + 1;
	
	TextView captureTime;
	boolean mStartTimer;
	Thread 	mThread ;
	ImageButton captureButton;
	
	TextView mConfirm,mDelete ;
	
	private Camera 			mCamera;
	private SurfaceView		mSurfaceView ;
	private SurfaceHolder 	mSurfaceHolder;
	private int 			mWidth ;
	private int 			mHeight ;
	private int 			mBitrate ;
	private MediaRecorder 	mMediaRecorder;
	private boolean 		mPreview ;
	private boolean			mStartRecorder ;
	private boolean 		mRecorderFile ;
	private boolean 		mHasError ;
	private String			mErrorText ;
	
	private RelativeLayout	mSurfaceLayout ;
	private int				mSurfaceLayoutWidth ;
	private int				mSurfaceLayoutHeight ;
	
	SharedPreferences settings ;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ugc_record_layout) ;
		
		settings 	= PreferenceManager.getDefaultSharedPreferences(this);
		mHasError	= false ;
		
		// load recorder
		if(!loadRecorderParameter()){
			mHasError	= true ;
			mErrorText	= getString(R.string.ugc_label_add_recorder_error);
		}
		
		// load UI
		initLayout();
		
		mPreview = mStartRecorder = false ;
		mRecorderFile	= false ;
		
		fileName = "VEDIO_" + System.currentTimeMillis() + ".mp4";
	}
	
	private final void initLayout() {
		
		mSurfaceLayout	= (RelativeLayout)findViewById(R.id.ugc_record_surface_layout) ;
		
		captureTime		= (TextView)findViewById(R.id.recorder_time_textview);
		captureButton	=  (ImageButton) findViewById(R.id.bnt_recorder_start);
		mConfirm		= (TextView) findViewById(R.id.bnt_recorder_confirm) ;
		mDelete			= (TextView) findViewById(R.id.bnt_recorder_delete) ;
		
		mSurfaceView	= (SurfaceView) findViewById(R.id.recorder_video_sureface);
		mSurfaceView.getHolder().addCallback(this);
		mSurfaceView.getHolder().setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
		
		mConfirm.setVisibility(View.INVISIBLE);
		mDelete.setVisibility(View.INVISIBLE);
	}
	
	private final boolean loadRecorderParameter(){
		
		mWidth	= settings.getInt(KEY_RECORDER_WIDTH, -1);
		mHeight	= settings.getInt(KEY_RECORDER_HEIGHT, -1);
		mBitrate= settings.getInt(KEY_RECORDER_BITRATE, -1);
		
		//
		if(mWidth <= 0 || mHeight <= 0 || mBitrate <= 0){
			mWidth		= DEFAULT_WIDTH ;
			mHeight		= DEFAULT_HEIGHT ;
			mBitrate	= DEFAULT_BITRATE ;
			Editor edit = settings.edit() ;
			edit.putInt(KEY_RECORDER_WIDTH, mWidth)
			.putInt(KEY_RECORDER_HEIGHT, mHeight)
			.putInt(KEY_RECORDER_BITRATE, mBitrate).commit();
			Log.w(TAG, "Set Recorder Default Parameter : width=" + mWidth + " height=" + mHeight + " bitrate" + mBitrate) ;
		}
		
		return true ;
	}

	private final void createUpdateTimer() {
		
		mStartTimer = true ;
		
		mThread = new Thread(new Runnable() {

			@Override
			public void run() {
				Message msg = null;
				long enter_timer = System.currentTimeMillis() ;
				long timer;
				int hour;
				int minute;
				int second;
				String time;
				while (mStartTimer) {
					try {
						Thread.sleep(500);
						timer = (System.currentTimeMillis() - enter_timer) / 1000;
						hour = (int) (timer / 3600);
						minute = (int) ((timer % 3600) / 60);
						second = (int) (timer % 60);
						time = (hour > 9 ? hour : "0" + hour) + ":"
								+ (minute > 9 ? minute : "0" + minute) + ":"
								+ (second > 9 ? second : "0" + second);
						msg = mHandler.obtainMessage();
						msg.what = UPDATE_TIME;
						msg.obj = time;
						mHandler.sendMessage(msg);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
				
				mHandler.obtainMessage(UPDATE_TIME, "00:00:00").sendToTarget() ;
			}
		});
		mThread.setName("#Timer Thread");
		mThread.start() ;
	}

	public void buttonOnclick(View view) {
		final int id = view.getId() ;
		
		if(id == R.id.bnt_recorder_start){
			if(!mStartRecorder) {
				startRecord();
			} else {
				mConfirm.setVisibility(View.VISIBLE);
				mDelete.setVisibility(View.VISIBLE);
				stopRecord(mStartRecorder,true);
			}
		} else if(id == R.id.bnt_recorder_confirm){
			setRecorderResult();
		} else if(id == R.id.bnt_recorder_delete){
			mRecorderFile = false ;
			File file = new File(ParentPath + fileName);
			if(file.exists()) file.delete() ;
			
			mConfirm.setVisibility(View.INVISIBLE);
			mDelete.setVisibility(View.INVISIBLE);
		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			setRecorderResult();
			return false ;
		} else {
			return super.onKeyDown(keyCode, event);
		}
	}
	
	private void setRecorderResult(){
		Intent intent = getIntent();
		intent.putExtra(VIDEO_PATH, mRecorderFile ? (ParentPath + fileName) : "");
		intent.putExtra(VIDEO_NAME, mRecorderFile ? fileName : "");
		setResult(REQUEST_VIDEOPATH_CODE, intent);
		finish() ;
	}

	/**
	 * UI handler
	 * 
	 */
	private final Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case UPDATE_TIME:
				captureTime.setText(msg.obj.toString());
				break;
			}
		}
	};

	private void startRecord() {
		try {

			stopCamera(mPreview);
			
			File parent = new File(ParentPath);
			if (!parent.isDirectory()) {
				parent.mkdirs();
			}
			
			File file = new File(ParentPath + fileName);
			if(file.exists()) file.delete() ;
			file.createNewFile();
			//
			mMediaRecorder = new MediaRecorder();
			mMediaRecorder.setVideoSource(MediaRecorder.VideoSource.CAMERA);
			mMediaRecorder.setAudioSource(MediaRecorder.AudioSource.CAMCORDER);
			mMediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
			mMediaRecorder.setVideoEncoder(MediaRecorder.VideoEncoder.H264);
			mMediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
			mMediaRecorder.setVideoSize(mWidth,mHeight);
			mMediaRecorder.setVideoEncodingBitRate(mBitrate);
			mMediaRecorder.setPreviewDisplay(mSurfaceHolder.getSurface());
			mMediaRecorder.setOutputFile(file.getAbsolutePath());
			mMediaRecorder.prepare();
			mMediaRecorder.start();
			mStartRecorder = true ;
			mRecorderFile  = true ;
			captureButton.setImageResource(R.drawable.ugc_ic_btn_video_record_stop);
			createUpdateTimer();
		} catch (IOException e) {
			e.printStackTrace();
			stopRecord(false,false) ;
		}
	}

	private void stopRecord(boolean stop,boolean preview) {
		
		mStartTimer = false ;
		
		if (mMediaRecorder != null) {
			if(stop)
				mMediaRecorder.stop();
			mMediaRecorder.reset();
			mMediaRecorder.release();
			mMediaRecorder = null;
		}
		
		mStartRecorder	= false ;
		
		try{
			if(mThread != null)
				mThread.join() ;
			mThread	= null ;
		}catch(Exception e){
			e.printStackTrace() ;
		}
		
		captureButton.setImageResource(R.drawable.ugc_ic_btn_video_record);
	}
	
	private void startCameraPreview(){
		try {
			mCamera 	= Camera.open();
			mCamera.setPreviewDisplay(mSurfaceHolder);
			Parameters params 	= mCamera.getParameters();
			params.setPreviewSize(mWidth, mHeight);
			mCamera.setParameters(params);
			mCamera.startPreview();
			mPreview	= true ;
		} catch (Exception e) {
			e.printStackTrace();
			stopCamera(false);
		}
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
	
	private void stopCamera(boolean preview){
		if (mCamera != null) {
			if(preview) 
				mCamera.stopPreview();
			mCamera.release();
			mCamera = null;
		}
	}
	
	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		
		mSurfaceLayoutWidth	 = mSurfaceLayout.getWidth() ;
		mSurfaceLayoutHeight = mSurfaceLayout.getHeight() ;
		
		mSurfaceHolder	= holder ;
		
		// 修改宽高比
		fixedSurfaceLayout(mSurfaceHolder);
		
		if(!mHasError) {
			startCameraPreview();
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

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,int height) {
		
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		stopCamera(false);
		stopRecord(mStartRecorder,false);
	}
}
