package cn.dezhisoft.cloud.mi.newugc.ioffer.ui;

import android.media.MediaPlayer;
import android.media.MediaPlayer.OnVideoSizeChangedListener;
import android.util.Log;
import android.view.SurfaceHolder;

/**
 * 
 * @author Rosson Chen
 * 
 */
public final class MediaPlayerProxy implements Runnable,MediaPlayer.OnBufferingUpdateListener,
				MediaPlayer.OnCompletionListener,
				MediaPlayer.OnErrorListener,
				MediaPlayer.OnPreparedListener {

	private static final String 	TAG				= "MediaPlayerProxy" ;
	
	private static final int STATE_UNKOWN 			= 0;
	private static final int STATE_LOADED 			= 1 << 1;
	private static final int STATE_PREPARING		= 1 << 2;
	private static final int STATE_PREPARED 		= 1 << 3;
	private static final int STATE_PLAY 			= 1 << 4;
	private static final int STATE_PAUSE 			= 1 << 5;
	private static final int STATE_STOP 			= 1 << 6;

	MediaPlayer mediaPlayer;
	int flag ;
	int duration ;
	int current_position ;
	MediaPlayerListener listener ;
	int percent ;

	public void setDataSourcePath(String path) {
		setDataSourceImpl(path);
	}

	public void setDataSourceUrl(String url) {
		setDataSourceImpl(url);
	}

	private void setDataSourceImpl(String path) {
		try {
			
			setNewState(STATE_LOADED);
			
			mediaPlayer = new MediaPlayer();
			mediaPlayer.setDataSource(path);
			mediaPlayer.setOnCompletionListener(this);
			mediaPlayer.setOnErrorListener(this);
			mediaPlayer.setOnPreparedListener(this);
		} catch (Exception e) {
			e.printStackTrace();
			if(listener != null)
				listener.onError(null, 0, 0);
			release();
		}
	}
	
	/** 准备,立即返回 */
	public void prepareAsync(){
		prepareImpl(true) ;
	}
	
	/** 准备,等待完成 */
	public void prepare(){
		prepareImpl(false) ;
	}
	
	public void prepareImpl(boolean sync){
		
		if(mediaPlayer == null) return ;
		
		try{
			if (sync){
				mediaPlayer.setOnBufferingUpdateListener(this) ;
				mediaPlayer.prepareAsync();
				duration	= -1 ;
				percent		= 0 ;
			} else {
				mediaPlayer.prepare();
				duration 	= mediaPlayer.getDuration() ;
				percent		= 100 ;
			}
			
			setNewState(STATE_PREPARING);
			
			current_position = 0 ;
			
			// 启动线程
			new Thread(this).start();
		} catch(Exception e){
			e.printStackTrace() ;
		}
	}
	
	public void setListener(MediaPlayerListener l){
		listener = l ;
	}
	
	public void setSurfaceHolder(SurfaceHolder holder){
		if(mediaPlayer != null) 
			mediaPlayer.setDisplay(holder);
	}
	
	public void seekTo(int msec){
		if(mediaPlayer != null) 
			mediaPlayer.seekTo(msec);
	}
	
	public boolean isPlaying(){
		return flag == STATE_PLAY ;
	}
	
	public boolean isBufferingCompletion(){
		return percent >= 100 ;
	}

	private final void setNewState(int state){
		flag = state ;
	}
	
	@Override
	public void run() {
		int dur ,hour,minute,second ;
		float progress ;
		while(flag > 0){
			switch(flag){
			case STATE_PLAY :
				current_position = mediaPlayer.getCurrentPosition();
				dur				 = current_position / 1000 ;
				progress		 = (float)current_position / duration ;
				hour			 = dur / 3600 ;
				minute			 = dur / 60 ;
				second			 = dur % 60 ;
				if(listener != null)listener.progress(progress, hour,minute, second);
				break ;
			case STATE_STOP :
				
				break ;
			}
			try{
				Thread.sleep(200);
			} catch(Exception e){
				e.printStackTrace() ;
			}
		}
	}
	
	public int getVideoWidth(){
		return mediaPlayer != null ? mediaPlayer.getVideoWidth() : 0 ;
	}
	
	public int getVideoHeight(){
		return mediaPlayer != null ? mediaPlayer.getVideoHeight() : 0 ;
	}

	public final int getDuration(){
		return duration ;
	}
	
	public final int getCurrentPosition(){
		return mediaPlayer != null ? mediaPlayer.getCurrentPosition() : 0 ;
	}
	
	public final boolean play() {
		if(flag == STATE_PREPARED || flag == STATE_PAUSE){
			
			mediaPlayer.start();
			
			setNewState(STATE_PLAY) ;
			
			return true ;
		}
		return false ;
	}

	public final boolean pause() {
		if(flag == STATE_PLAY){
			
			mediaPlayer.pause() ;
			
			setNewState(STATE_PAUSE) ;
			
			return true ;
		}
		return false ;
	}

	public final boolean stop() {
		if(flag == STATE_PLAY || flag == STATE_PAUSE){
			
			mediaPlayer.stop();
			
			setNewState(STATE_STOP) ;
			
			return true ;
		}
		return false ;
	}

	public final void release() {
		
		if(mediaPlayer != null){
			
			if(flag == STATE_PLAY)
				stop();
			
			mediaPlayer.release() ;
		}
		
		setNewState(STATE_UNKOWN);
		
		mediaPlayer = null ;
	}
	
	public static interface MediaPlayerListener {
		
		public void onPrepared(MediaPlayerProxy proxy);
		
		public void progress(float progress,int hour ,int minute,int second);
		
		public void onBufferingUpdate(int percent);
		
		public void onOnBufferingCompletion(int duration);
		
		public void onError(MediaPlayer mp, int what, int extra) ;
	}

	@Override
	public void onCompletion(MediaPlayer mp) {
		Log.d(TAG, ">>>>>>>>>>>>>>>onCompletion");
	}

	@Override
	public void onBufferingUpdate(MediaPlayer mp, int percent) {
		this.percent = percent ;
		if(listener != null){
			
			Log.d(TAG, "Buffer update >> " + percent);
			
			listener.onBufferingUpdate(percent);
			
			if(percent > 0 && flag == STATE_LOADED)
				setNewState(STATE_PREPARED);
			
			if(percent >= 100){
				duration = mp.getDuration();
				mediaPlayer.setOnBufferingUpdateListener(null);
				listener.onOnBufferingCompletion(duration);
			}
		}
	}

	@Override
	public boolean onError(MediaPlayer mp, int what, int extra) {
		
		if(listener != null) listener.onError(mp, what, extra);
		
		release();
		
		return true ;
	}

	@Override
	public void onPrepared(MediaPlayer mp) {
		
		setNewState(STATE_PREPARED);
		
		if(listener != null) listener.onPrepared(this);
	}
	
	public void setOnVideoSizeChangedListener(OnVideoSizeChangedListener listener){
		if(mediaPlayer != null) mediaPlayer.setOnVideoSizeChangedListener(listener);
	}
}
