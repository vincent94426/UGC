package cn.dezhisoft.cloud.mi.newugc.ioffer.upload.ui;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnErrorListener;
import android.media.ThumbnailUtils;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore.Video;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import cn.dezhisoft.cloud.mi.newugc.R;

import cn.dezhisoft.cloud.mi.newugc.ioffer.upload.view.VideoRangeSelectBarView;
import cn.dezhisoft.cloud.mi.newugc.ioffer.upload.view.VideoRangeSelectBarView.NeedleListener;
import com.sobey.sdk.cut.CutMediaFile;
import com.sobey.sdk.cut.CutProgressCallback;

/**
 * 
 * @author Rosson Chen
 *
 */
public final class UGCVideoClipActivity extends UGCBaseActivity implements SurfaceHolder.Callback {
	
	/** �ļ�·�� */ 
	public final static String KEY_FILE 		= "_temp_file" ;
	/** �ļ���� */ 
	public final static String KEY_NAME 		= "_temp_name" ;
	/** �ɹ� */
	public final static int    RESULT_CODE_SUCCEED		= 0x0000fA;
	
	static final String TAG	= "UGCVideoClipActivity" ;
	
	private static final int MSG_CUT_SUCCESS	= 1 ;
	private static final int MSG_CUT_FAILED		= 2 ;
	private static final int MSG_CUT_PROGRESS	= 3 ;
	private static final int MSG_PLAY_PAUSE		= 4 ;
	private static final int MSG_PLAY_UPDATE	= 5 ;
	private static final int MSG_PLAY_RESET		= 6 ;

	SurfaceView mSurfaceView;
	MediaPlayer mMediaPlayer ;
	Bitmap		mThumbBitmap ;
	ImageView	mVideoThumb ;
	boolean		mInit ;
	boolean		mPrepared ;
	String 		mSourcePath ;
	String 		mTargetPath ;
	String 		mTargetName ;
	
	ImageView 	mPlayButton;
	TextView 	mVideoStartTime;
	TextView 	mVideoEndTime;
	int startTime 	= 0;
	int endTime 	= 0;
	int progress 	= 0;
	int videoDuration = 0;

	VideoRangeSelectBarView mVideoBar;

	boolean mStart;
	Thread	mThread ;

	ProgressDialog cutVideoDialog;
	TextView proTextView ;
	TextView bntClip ;
	
	CutMediaFile mClipFile ;
	int percent ;
	String clipProgress ;
	boolean showError ;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ugc_video_clip_layout);
		//
		loadView();
		//
		mPrepared	= false ;
		showError	= false ;
		startTime	= 0 ;
		endTime		= 0 ;
		clipProgress = getString(R.string.ugc_label_video_cliping_progress);
		Bundle bundle = getIntent().getExtras() ;
		
		if(bundle != null){
			
			mSourcePath = bundle.getString(KEY_FILE);
			
			if(mSourcePath != null && !mSourcePath.equals("")){

				int end 		= mSourcePath.lastIndexOf('.');
				String  suffix 	= mSourcePath.substring(end);
				mTargetPath		= mSourcePath.substring(0, end) + "_cut" + suffix;
				mTargetName		= mTargetPath.substring(mTargetPath.lastIndexOf('/') + 1,mSourcePath.lastIndexOf('.'));
				
				mThumbBitmap 	= ThumbnailUtils.createVideoThumbnail(mSourcePath,Video.Thumbnails.MINI_KIND);
				mVideoThumb.setImageBitmap(mThumbBitmap);
				mInit		= true ;
			} else {
				mInit		= false ;
			}
		} else {
			mInit		= false ;
		}
	}
	
	private void loadView() {
		
		mVideoThumb 	= (ImageView) findViewById(R.id.ioffer_video_thumb);
		mSurfaceView	= (SurfaceView) findViewById(R.id.ioffer_video_clip_surface_view);
		mPlayButton 	= (ImageView) findViewById(R.id.ioffer_video_range_select_play);
		mVideoStartTime = (TextView) findViewById(R.id.ioffer_video_start_time);
		mVideoEndTime 	= (TextView) findViewById(R.id.ioffer_video_end_time);
		mVideoBar 		= (VideoRangeSelectBarView) findViewById(R.id.ioffer_video_range_select_bar);
		bntClip			= (TextView)findViewById(R.id.ioffer_video_range_select_complete);
		//
		mSurfaceView.getHolder().addCallback(this);
		mSurfaceView.getHolder().setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
		
		mVideoBar.setProgressListener(new VideoRangeSelectBarView.ProgressListener() {

			@Override
			public void progressUpdate(int pos) {
				
				if(mMediaPlayer != null) {
					
					if(pos <= mVideoBar.getEndTime()){
						mMediaPlayer.seekTo(pos);
					}
					
					if(mMediaPlayer.isPlaying()) {
						handler.obtainMessage(MSG_PLAY_PAUSE).sendToTarget() ;
					}
				}
			}
		});

		mVideoBar.setNeedleListener(new NeedleListener() {
			@Override
			public void rangeUpdate(int start, int end) {
				// start time
				startTime = start;
				String hmsStart = toTime(start);
				mVideoStartTime.setText(getString(R.string.ugc_label_video_range_select_start)+ hmsStart);
				// end time
				endTime = end;
				String hmsEnd = toTime(end);
				mVideoEndTime.setText(getString(R.string.ugc_label_video_range_select_end)+ hmsEnd);

				updateClipButton();
			}
		});
		
		//progress 	= mVideoBar.getProgress();
		mVideoStartTime.setText(getString(R.string.ugc_label_video_range_select_start) + "00:00:00");
		mVideoEndTime.setText(getString(R.string.ugc_label_video_range_select_end) + "00:00:00");
	}

	public void topBarOnClick(View view) {
		
		final int id = view.getId() ;
		
		if(id == R.id.ioffer_video_range_select_cancel){
			this.finish();
		} else if(id == R.id.ioffer_video_range_select_complete){
			if(!mInit){
				return ;
			}
			
			// ��ͣ
			if (mMediaPlayer != null && mMediaPlayer.isPlaying()){
				mMediaPlayer.pause() ;
			}
			
			final int start = mVideoBar.getStartTime() / 1000 ;
			final int end 	= mVideoBar.getEndTime() / 1000 ;
			
			// û��ѡ��ü���ʱ��
			if(start == 0 && end == (videoDuration / 1000)){
				mTargetPath		= mSourcePath ;
				mTargetName		= mSourcePath.substring(mSourcePath.lastIndexOf('/') + 1,mSourcePath.lastIndexOf('.'));
				onCutCompleted();
				return ;
			}

			View content = getLayoutInflater().inflate(R.layout.ugc_cut_progress_layout, null);
			proTextView = (TextView) content.findViewById(R.id.progress_textview);
			proTextView.setText(clipProgress + "0%");

			cutVideoDialog = ProgressDialog.show(this, "",getString(R.string.ugc_label_video_cliping), true, false);
			cutVideoDialog.setContentView(content);
			
			new Thread(new Runnable() {
				@Override
				public void run() {

					percent = 0;
					mClipFile = new CutMediaFile();
					mClipFile.cutInital();
					mClipFile.setCutProgressCallback(ClipCallback);

					Log.w(TAG, "Clip Video from : " + start + " to: " + end);

					int code = mClipFile.cutVideo(mSourcePath, mTargetPath,start, end);

					if (code != 0)
						handler.obtainMessage(MSG_CUT_FAILED).sendToTarget();
				}
			}).start();
		}
	}

	public void buttonOnClick(View view) {
		
		final int id = view.getId() ;

		if(id == R.id.ioffer_video_range_selected_playview){
			
			if(!mPrepared){
				Toast.makeText(this, "", Toast.LENGTH_SHORT).show() ;
				return ;
			}
			
			if(mMediaPlayer.isPlaying()){
				// ��ͣ
				handler.obtainMessage(MSG_PLAY_PAUSE).sendToTarget() ;
			} else {
				// ��ʼ
				if(!mStart){
					mPlayButton.setVisibility(View.GONE);
					mVideoThumb.setVisibility(View.GONE);
					mMediaPlayer.start() ;
					endTime 	= videoDuration;
					mStart 		= true;
					
					mThread	 = new Thread(thread);
					mThread.start() ;
				} else {
					// ����
					final int position 	= mMediaPlayer.getCurrentPosition();
					final int end		= mVideoBar.getEndTime();
					// �����
					if(position < end){
						mMediaPlayer.start() ;
						mPlayButton.setVisibility(View.GONE);
					}
				}
			}
		}
	}

	public void timeOnClick(View view) {
		
		final int id = view.getId() ;

		if(id == R.id.ioffer_video_start_time_increase){
			
			if(Math.abs(endTime - startTime) > 1000){
				startTime += 1000;
				mVideoBar.setStartNeedle(startTime);
				mVideoStartTime.setText(getString(R.string.ugc_label_video_range_select_start)+ toTime(startTime));
			}
		} else if(id == R.id.ioffer_video_start_time_reduce){
			
			if(startTime - 1000 >= 0){
				startTime -= 1000;
				mVideoBar.setStartNeedle(startTime);
				mVideoStartTime.setText(getString(R.string.ugc_label_video_range_select_start)+ toTime(startTime));
			}
		} else if(id == R.id.ioffer_video_end_time_increase){
			
			if(endTime + 1000 <= videoDuration){
				endTime += 1000;
				mVideoBar.setEndNeedle(endTime);
				mVideoEndTime.setText(getString(R.string.ugc_label_video_range_select_end)+ toTime(endTime));
			}
			
		} else if(id == R.id.ioffer_video_end_time_reduce){
			
			if(Math.abs(endTime - startTime) > 1000){
				endTime -= 1000;
				mVideoBar.setEndNeedle(endTime);
				mVideoEndTime.setText(getString(R.string.ugc_label_video_range_select_end)+ toTime(endTime));
			}
		}
		
		updateClipButton();
	}
	
	private void updateClipButton(){
		
		final int clipTime	= (mVideoBar.getEndTime() - mVideoBar.getStartTime()) / 1000 ;
		final int duration	= videoDuration / 1000 ;
		
		// û��ѡ��ü���ʱ��
		if(clipTime != duration){
			bntClip.setText(R.string.ugc_label_video_clip_bnt_clip);
		} else {
			bntClip.setText(R.string.ugc_label_video_clip_bnt_ok);
		}
	}
	
	final Thread thread = new Thread(new Runnable() {

		@Override
		public void run() {
			
			final MediaPlayer player = mMediaPlayer ;
			
			while (mStart) {
				
				if(player != null && player.isPlaying()){
					
					videoDuration 	= player.getDuration();
					int current		= player.getCurrentPosition() ;
					final int end	= mVideoBar.getEndTime() ;
					
					// ����UI
					handler.obtainMessage(MSG_PLAY_UPDATE,current,0).sendToTarget() ;
					
					// �Ƿ���ͣ
					if(current >= end){
						handler.obtainMessage(MSG_PLAY_RESET).sendToTarget() ;
					}
				}
				
				try {
					Thread.sleep(50);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	});
	
	// ����ʱ��
	public final static String toTime(int time) {
		time /= 1000;
		int minute = time / 60;
		int hour = minute / 60;
		int second = time % 60;
		minute %= 60;
		return String.format("%02d:%02d:%02d", hour, minute, second);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		
		if(mThumbBitmap != null)
			mThumbBitmap.recycle() ;
		mThumbBitmap = null ;
		
		if(mClipFile != null){
			mClipFile.cutRelease() ;
			mClipFile	= null ;
		}
	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		
	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,int height) {

		if(mInit){
			try{
				
				mMediaPlayer	= new MediaPlayer() ;
				mMediaPlayer.setDataSource(mSourcePath);
				mMediaPlayer.setDisplay(holder) ;
				mMediaPlayer.setOnCompletionListener(new OnCompletionListener() {
					
					@Override
					public void onCompletion(MediaPlayer mp) {
						mVideoThumb.setImageBitmap(mThumbBitmap);
						mVideoThumb.setVisibility(View.VISIBLE);
						mPlayButton.setVisibility(View.VISIBLE);
						mStart 		= false;
						
						mVideoBar.updatePlayPosition(100);
						mVideoBar.setCurrentTime(toTime(mMediaPlayer.getDuration()));
					}
				}) ;
				
				mMediaPlayer.setOnErrorListener(new OnErrorListener() {
					
					@Override
					public boolean onError(MediaPlayer mp, int what, int extra) {
						mStart	 = false ;
						showErrorDialog("��Ƶ����: what=" + what + " extra=" + extra);
						return false;
					}
				}) ;
				
				mMediaPlayer.prepare() ;
				
				// init
				videoDuration 	= mMediaPlayer.getDuration();
				startTime		= 0 ;
				endTime			= videoDuration ;
				mVideoBar.setDuration(videoDuration);
				mVideoEndTime.setText(getString(R.string.ugc_label_video_range_select_end) + toTime(endTime));
				mPrepared	= true ;
			} catch(Exception e){
				e.printStackTrace() ;
				showErrorDialog("�޷����Ÿ���Ƶ");
			}
		} else {
			showHintDialog(R.string.ugc_label_video_init_error);
		}
	}
	
	private void showErrorDialog(String message){
		
		if(showError) return ;
		
		showErrorListenerDialog(message,new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				UGCVideoClipActivity.this.finish() ;
			}
		});
		
		showError	= true ;
	}
	
	private void stop(){
		
		mStart	 = false ;
		
		try{
			
			if(mThread != null)
				mThread.join() ;
			mThread	= null ;
			
			if(mMediaPlayer != null) {
				mMediaPlayer.stop() ;
				mMediaPlayer.release() ;
				mMediaPlayer	= null ;
			}
		}catch(Exception e){
			e.printStackTrace() ;
		}
	}
	
	/** �ü���� */
	private void onCutCompleted(){
		if(mTargetPath != null && !mTargetPath.equals("")) {
			Intent data = getIntent();
			data.putExtra(KEY_FILE, mTargetPath);
			data.putExtra(KEY_NAME, mTargetName);
			this.setResult(RESULT_CODE_SUCCEED, data);
			this.finish() ;
		}
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		stop();
	}
	
	private final CutProgressCallback ClipCallback = new CutProgressCallback() {
		
		@Override
		public void cutProgress(int progress, int total) {
			
			float f 	= (float)progress / total ;
			int current	= (int)(f * 100) ;
			
			if(progress == total){
				handler.obtainMessage(MSG_CUT_SUCCESS).sendToTarget() ;
			} else {
				if(percent != current){
					handler.obtainMessage(MSG_CUT_PROGRESS, current, 0).sendToTarget();
					percent	= current ;
				}
			}
		}
	};
	
	private final Handler handler = new Handler(){

		@Override
		public void handleMessage(Message msg) {
			switch(msg.what){
			case MSG_CUT_SUCCESS :
				cutVideoDialog.dismiss() ;
				onCutCompleted();
				break ;
			case MSG_CUT_FAILED :
				cutVideoDialog.dismiss() ;
				showHintDialog(R.string.ugc_label_video_clip_failed);
				break ;
			case MSG_CUT_PROGRESS :
				proTextView.setText(clipProgress + msg.arg1 + "%");
				break ;
			case MSG_PLAY_PAUSE :
				if(mMediaPlayer != null) mMediaPlayer.pause() ;
				mPlayButton.setVisibility(View.VISIBLE);
				break ;
			case MSG_PLAY_UPDATE :
				
				if (mMediaPlayer != null) {
					if (mVideoBar != null && mMediaPlayer.isPlaying()) {
						mVideoBar.updatePlayPosition(msg.arg1);
						mVideoBar.setCurrentTime(toTime(mMediaPlayer.getCurrentPosition()));
					}
				}
				break ;
			case MSG_PLAY_RESET :
				
				if (mMediaPlayer != null && mVideoBar != null) {
					startTime	= mVideoBar.getStartTime() ;
					mMediaPlayer.seekTo(startTime);
					mMediaPlayer.pause() ;
					mPlayButton.setVisibility(View.VISIBLE);
				}
				break ;
			}
		}
		
	} ;
}
