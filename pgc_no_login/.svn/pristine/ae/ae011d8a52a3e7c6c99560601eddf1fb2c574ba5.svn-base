package cn.dezhisoft.cloud.mi.newugc.ioffer.ui;

import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import cn.dezhisoft.cloud.mi.newugc.R;

import cn.dezhisoft.cloud.mi.newugc.ioffer.common.Resolution;
import cn.dezhisoft.cloud.mi.newugc.ioffer.ui.MediaPlayerProxy.MediaPlayerListener;

/**
 * 
 * @author Rosson Chen
 * 
 */
public final class PlayVideoActivity extends BasePreviewActivity implements SurfaceHolder.Callback{

	static final String TAG = "PlayVideoActivity";
	
	FrameLayout root ;
	ImageButton bnt_play ;
	ImageView loadin_view ;
	SeekBar seekbar ;
	TextView loading_progress,current_text,duration_text;

	int flag ;
	SurfaceView surfaceView ;
	MediaPlayerProxy mediaPlayer ;
	RelativeLayout play_controller_layout ,play_loading_layout;
	int duration ;
	final DisplayMetrics displayMetrics = new DisplayMetrics();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		root	= (FrameLayout)getLayoutInflater().inflate(R.layout.ioffer_play_video_layout, null);
		setContentView(root);
		
		getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
		
		bnt_play		= (ImageButton)findViewById(R.id.play_video_button);
		seekbar			= (SeekBar)findViewById(R.id.play_video_progress_bar);
		current_text	= (TextView)findViewById(R.id.play_video_current);
		duration_text	= (TextView)findViewById(R.id.play_video_duration);
		loading_progress= (TextView)findViewById(R.id.play_video_loading_progress);
		surfaceView		= (SurfaceView)findViewById(R.id.play_video_surface);
		loadin_view		= (ImageView)findViewById(R.id.play_video_loading_view);
		play_controller_layout 	= (RelativeLayout)findViewById(R.id.play_video_controller_layout);
		play_loading_layout 	= (RelativeLayout)findViewById(R.id.play_video_loading_layout);
		flag			= 0 ;
		surfaceView.getHolder().addCallback(this);
		surfaceView.getHolder().setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
		
		root.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				int flag = play_controller_layout.getVisibility() ;
				if(flag == View.VISIBLE){
					play_controller_layout.setVisibility(View.INVISIBLE);
				} else {
					play_controller_layout.setVisibility(View.VISIBLE);
				}
			}
		}) ;
	}

	public void buttonOnclick(View v){
		if(v.getId() == R.id.play_video_button){
			updatePlayState();
		}
	}
	
	private void updatePlayState(){
		if(flag == 0){
			if(playVideo()){
				bnt_play.setBackgroundResource(R.drawable.ic_ioffer_pause_button_selector);
				flag 	= 1 ;
			}
		} else {
			if(pauseVideo()){
				bnt_play.setBackgroundResource(R.drawable.ic_ioffer_play_button_selector);
				flag 	= 0 ;
			}
		}
	}

	@Override
	protected void onResume() {
		super.onResume();
		Animation animation = AnimationUtils.loadAnimation(this, R.anim.loading_anim) ;
		animation.setInterpolator(new LinearInterpolator());
		loadin_view.startAnimation(animation);
	}
	
	private final boolean playVideo(){
		return mediaPlayer != null && mediaPlayer.play() ;
	}
	
	private final boolean pauseVideo(){
		return mediaPlayer != null && mediaPlayer.pause() ;
	}
	
	private final void calculateDuration(int duration){
		if(duration <= 0 || duration == Integer.MAX_VALUE) 
			return ;
		this.duration = duration ;
		int dur	= duration / 1000 ;
		int hour= dur / 3600 ;
		int min	= dur / 60 ;
		int sec = dur % 60 ;
		duration_text.setText((hour > 9 ? hour : "0" + hour) + ":" + (min > 9 ? min : "0" + min) + ":" + (sec > 9 ? sec : "0" + sec));
	}
	
	@Override
	public void release() {
		
	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		loadVideoSource(holder);
	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,int height) {
		
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		
		if(mediaPlayer != null)
			mediaPlayer.release();
		
		mediaPlayer = null ;
	}
	
	private void loadVideoSource(final SurfaceHolder holder) {
		
		int mode 	= getPreviewMode() ;
		String path = getPreviewPath() ;
		//String name = getPreviewName() ;
		//
		mediaPlayer	= new MediaPlayerProxy();
		// 监听器
		mediaPlayer.setListener(new MediaPlayerListener() {

			@Override
			public void progress(float progress, int hour, int minute,int second) {
				updateCurrentPosition((int) (progress * 100), hour, minute,second);
			}

			@Override
			public void onOnBufferingCompletion(int duration) {
				calculateDuration(duration);
			}

			@Override
			public void onBufferingUpdate(int percent) {
				if (percent > 10 && !mediaPlayer.isPlaying()) {
					updatePlayState();
					play_loading_layout.setVisibility(View.INVISIBLE);
					loading_progress.setVisibility(View.INVISIBLE);
				} else {
					loading_progress.setText(percent + "%");
				}
			}

			@Override
			public void onPrepared(MediaPlayerProxy proxy) {
				calculateDuration(proxy.getDuration());

				// Surface View 的比例计算
				Resolution res = Resolution.calculateDisplayResolution(
						displayMetrics.widthPixels,
						displayMetrics.heightPixels, proxy.getVideoWidth(),
						proxy.getVideoHeight());

				holder.setFixedSize(res.getWidth(), res.getHeight());
				// 更新View宽度和高度
				LayoutParams lp = surfaceView.getLayoutParams();
				lp.width = res.getWidth();
				lp.height = res.getHeight();
				surfaceView.setLayoutParams(lp);
				// 准备完成后开始播放
				updatePlayState();
			}

			@Override
			public void onError(MediaPlayer mp, int what, int extra) {
				showMessageDialog("无法播放该视频!", new OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						finish();
					}
				});
			}
		});
		// 设置数据源
		if(mode == PREVIEW_MODE_LOCAL){
			mediaPlayer.setDataSourcePath(path);
			calculateDuration(mediaPlayer.getDuration());
			loadin_view.setVisibility(View.INVISIBLE);
			loading_progress.setVisibility(View.INVISIBLE);
			play_loading_layout.setVisibility(View.INVISIBLE);
		} else {
			mediaPlayer.setDataSourceUrl(path);
			duration 	= 0;
			loading_progress.setVisibility(View.VISIBLE);
		}
		//
		holder.setKeepScreenOn(true);
		// set display
		mediaPlayer.setSurfaceHolder(holder);
		// 初始化进度条
		seekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
			
			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {
				
			}
			
			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {
				
			}
			
			@Override
			public void onProgressChanged(SeekBar seekBar, int progress,boolean fromUser) {
				if(fromUser){
					if(mediaPlayer.isBufferingCompletion()){
						float pro 	= (float)progress / 100 ;
						int sec 	= (int)(pro * mediaPlayer.getDuration());
						mediaPlayer.seekTo(sec) ;
					} else {
						seekBar.setProgress(0);
					}
				}
			}
		}) ;
		
		// 准备
		if(mode == PREVIEW_MODE_LOCAL){
			mediaPlayer.prepare() ;
		} else {
			mediaPlayer.prepareAsync() ;
		}
	}
	
	private void updateCurrentPosition(final int prog ,final int hour,final int min,final int sec){
		mHandler.post(new Runnable() {
			
			@Override
			public void run() {
				current_text.setText((hour > 9 ? hour : "0" + hour) + ":" + (min > 9 ? min : "0" + min) + ":" + (sec > 9 ? sec : "0" + sec));
				seekbar.setProgress(prog);
			}
		});
	}
}
