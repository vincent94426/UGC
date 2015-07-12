package cn.dezhisoft.cloud.mi.newugc.ioffer.ui;

import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;

import cn.dezhisoft.cloud.mi.newugc.R;

import cn.dezhisoft.cloud.mi.newugc.ioffer.ui.MediaPlayerProxy.MediaPlayerListener;


/**
 * 
 * @author Rosson Chen
 * 
 */
public final class PlayAudioActivity extends BasePreviewActivity {

	static final String TAG = "MediaPlayerActivity";

	TextView music_name ,current_time,total_time , music_prog;
	ImageButton play_bnt ;
	int flag ;
	MediaPlayerProxy mediaPlayer ;
	SeekBar music_seekbar ;
	int duration ;
	boolean isUpdate ;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ioffer_play_audio_layout);
		music_name 	= (TextView) findViewById(R.id.play_audio_music_name);
		current_time= (TextView) findViewById(R.id.play_audio_current_time);
		total_time	= (TextView) findViewById(R.id.play_audio_total_time);
		music_prog	= (TextView) findViewById(R.id.play_audio_prog);
		play_bnt	= (ImageButton)findViewById(R.id.play_audio_button);
		music_seekbar = (SeekBar)findViewById(R.id.play_audio_progress_bar);
		music_seekbar.setMax(100);
		flag		= 0 ;
		isUpdate	= false ;
		//
		loadAudio();
	}
	
	private void loadAudio(){
		
		int mode 	= getPreviewMode() ;
		String path = getPreviewPath() ;
		String name = getPreviewName() ;
		
		mediaPlayer	= new MediaPlayerProxy();
		// 设置监听器
		mediaPlayer.setListener(new MediaPlayerListener() {
			
			@Override
			public void progress(float progress, int hour,int minute, int second) {
				updateCurrentPosition((int)(progress * 100),minute,second);
			}

			@Override
			public void onBufferingUpdate(int percent) {
				music_prog.setText(percent + "%");
				if(!isUpdate && percent > 10 && !mediaPlayer.isPlaying()){
					updatePlayState();
					isUpdate 	= true ;
				}
			}

			@Override
			public void onOnBufferingCompletion(int duration) {
				calculateDuration(duration);
			}

			@Override
			public void onPrepared(MediaPlayerProxy proxy) {
				// 计算时间
				calculateDuration(proxy.getDuration());
				// 播放
				updatePlayState();
			}
			
			@Override
			public void onError(MediaPlayer mp, int what, int extra) {
				showMessageDialog("无法播放该音频文件!",new OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						finish() ;
					}
				});
			}
		}) ;
		// 设置数据源
		if(mode == PREVIEW_MODE_LOCAL){
			mediaPlayer.setDataSourcePath(path);
			music_prog.setVisibility(View.INVISIBLE);
		} else {
			mediaPlayer.setDataSourceUrl(path);
			duration = 0;
			music_prog.setVisibility(View.VISIBLE);
		}
		//
		music_name.setText(name != null && !name.equals("") ? name : getFileName(path));
		//
		music_seekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
			
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
	
	private final void calculateDuration(int duration){
		
		if(duration <= 0 || duration == Integer.MAX_VALUE ) return ;
		
		this.duration = duration ;
		int min	= (duration / 1000) / 60 ;
		int sec = (duration / 1000) % 60 ;
		total_time.setText((min > 9 ? min : "0" + min) + ":" + (sec > 9 ? sec : "0" + sec));
	}
	
	private final boolean playAudio(){
		return mediaPlayer != null && mediaPlayer.play() ;
	}
	
	private final boolean pauseAudio(){
		return mediaPlayer != null && mediaPlayer.pause() ;
	}
	
	private void updateCurrentPosition(final int prog ,final int min,final int sec){
		mHandler.post(new Runnable() {
			
			@Override
			public void run() {
				current_time.setText((min > 9 ? min : "0" + min) + ":" + (sec > 9 ? sec : "0" + sec));
				music_seekbar.setProgress(prog);
			}
		});
	}

	public void buttonOnclick(View v){
		if(v.getId() == R.id.play_audio_button){
			updatePlayState();
		}
	}
	
	private final void updatePlayState(){
		if(flag == 0){
			if(playAudio()){
				play_bnt.setBackgroundResource(R.drawable.ic_ioffer_pause_button_selector);
				flag 	= 1 ;
			}
		} else {
			if(pauseAudio()){
				play_bnt.setBackgroundResource(R.drawable.ic_ioffer_play_button_selector);
				flag 	= 0 ;
			}
		}
	}
	
	@Override
	public void release() {
		
		if(mediaPlayer != null){
			mediaPlayer.release();
		}
		
		mediaPlayer = null ;
	}
}
