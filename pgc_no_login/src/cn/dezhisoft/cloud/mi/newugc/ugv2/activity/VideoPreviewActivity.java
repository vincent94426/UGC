package cn.dezhisoft.cloud.mi.newugc.ugv2.activity;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.MediaController;
import android.widget.VideoView;
import cn.dezhisoft.cloud.mi.newugc.R;
import cn.dezhisoft.cloud.mi.newugc.ugv2.base.activity.BaseActivity;
import cn.dezhisoft.cloud.mi.newugc.ugv2.constants.UgV2Constants;

public class VideoPreviewActivity extends BaseActivity {
	private String path = "";

	private VideoView mVideoView;

	private static int i = 0;

	private Dialog dialog;
	
	private View mContent;
	
	private LayoutInflater inflater;

	public void onCreate(Bundle icicle) {

		super.onCreate(icicle);

	}

	@Override
	protected void onResume() {

		super.onResume();

		mVideoView.seekTo(i);

		mVideoView.start();

	}

	@Override
	protected void onStop() {

		super.onStop();

		mVideoView.pause();

		i = mVideoView.getCurrentPosition();

	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {

		if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {

			startActivity(new Intent(VideoPreviewActivity.this, MainActivity.class));
			
			finish();

			return true;

		}

		return false;

	}

	@Override
	protected View onCreateBody() {
		this.inflater = this.getLayoutInflater();
		
		mContent = inflater.inflate(R.layout.activity_video_preview, null);
		
//		requestWindowFeature(Window.FEATURE_NO_TITLE);
//
//		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
		
//		getWindow().setFormat(PixelFormat.TRANSLUCENT);

		Bundle bundle = this.getIntent().getExtras();
		
//		setContentView(R.layout.activity_video_preview);

		path = bundle.getString(UgV2Constants.KEY_REQ_PATH);

		dialog = ProgressDialog.show(this, "视频加载中...", "请您稍候");

		mVideoView = (VideoView) mContent.findViewById(R.id.surface_view);

		mVideoView.setVideoPath(path);

		MediaController controller = new MediaController(this);

		mVideoView.setMediaController(controller);

		mVideoView.requestFocus();

		mVideoView.setOnPreparedListener(new OnPreparedListener() {

			// @Override

			public void onPrepared(MediaPlayer mp) {

				mVideoView.setBackgroundColor(Color.argb(0, 0, 255, 0));

				dialog.dismiss();

			}

		});

		mVideoView.setOnCompletionListener(new OnCompletionListener() {

			// @Override

			public void onCompletion(MediaPlayer mp) {
				
				startActivity(new Intent(VideoPreviewActivity.this, MainActivity.class));

				finish();

			}

		});

		controller.setOnTouchListener(new View.OnTouchListener() {

			public boolean onTouch(View v, MotionEvent event) {

				return true;

			}

		});
		
		setUiTitle(R.string.video_preview_page_title);
		
		setRightBtnVisibility(View.GONE);
		
		return mContent;
	}
	
	@Override
	protected void onBackBtnClick() {
		
		startActivity(new Intent(VideoPreviewActivity.this, MainActivity.class));
		
		finish();
	}
	
	@Override
	protected void onLeftBtnClick(View v) {

		startActivity(new Intent(VideoPreviewActivity.this, MainActivity.class));
		
		finish();
	}

}
