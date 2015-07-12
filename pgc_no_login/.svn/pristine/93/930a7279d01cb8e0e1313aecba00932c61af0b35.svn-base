package cn.dezhisoft.cloud.mi.newugc.ioffer.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import cn.dezhisoft.cloud.mi.newugc.R;

import cn.dezhisoft.cloud.mi.newugc.ioffer.common.Util;
import com.sobey.sdk.encoder.AbstractEncoder;
import com.sobey.sdk.encoder.AbstractEncoder.VideoQuality;

/**
 * 视频质量设置: 高,中,低
 * 
 * @author Rosson Chen
 * 
 */
public final class VideoQualityActivity extends BaseActivity {

	static final String TAG = "VideoRateActivity";
	/** 返回代码 */
	public static final int REQUEST_VIDEORATE_CODE = 0x00ff00f1;
	/** 选择视频质量 */
	public static final String KEY_QUALITY 	= "_quality";
	public static final String KEY_WIDTH	= "_width";
	public static final String KEY_HEIGHT	= "_height" ;
	
	/** ui组件*/
	TextView mark_low , mark_middle, mark_high ;
	/** 默认为低质量*/
	int width = 176, height = 144 ;
	int quality ;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ioffer_video_rate_layout);
		
		mark_low	= (TextView) findViewById(R.id.bnt_setting_rate_low);
		mark_middle	= (TextView) findViewById(R.id.bnt_setting_rate_middle);
		mark_high	= (TextView) findViewById(R.id.bnt_setting_rate_high);
		
		TextView low 	= (TextView) findViewById(R.id.video_rate_text_low);
		TextView middle	= (TextView) findViewById(R.id.video_rate_text_middle);
		TextView high	= (TextView) findViewById(R.id.video_rate_text_high);
		
		// 默认
		final Bundle bundle = getIntent().getExtras() ;
		quality			= bundle.getInt(KEY_QUALITY);
		width 			= bundle.getInt(KEY_WIDTH);
		height 			= bundle.getInt(KEY_HEIGHT);
		
		int bitrate = Util.calculateBitrate(width, height, VideoQuality.VIDEO_QUALITY_LOW) ;
		low.setText(getString(R.string.label_setting_low) + " (" + Util.formatBitrate(bitrate) + ")") ;
		bitrate = Util.calculateBitrate(width, height, VideoQuality.VIDEO_QUALITY_MEDIUM);
		middle.setText(getString(R.string.label_setting_middle) + " (" + Util.formatBitrate(bitrate) + ")") ;
		bitrate = Util.calculateBitrate(width, height, VideoQuality.VIDEO_QUALITY_HEIGH) ;
		high.setText(getString(R.string.label_setting_high) + " (" + Util.formatBitrate(bitrate) + ")") ;
		
		if(quality == VideoQuality.VIDEO_QUALITY_LOW){
			updateQualityView(R.id.setting_rate_low);
		} else if(quality == VideoQuality.VIDEO_QUALITY_MEDIUM){
			updateQualityView(R.id.setting_rate_middle);
		} else if(quality == VideoQuality.VIDEO_QUALITY_HEIGH){
			updateQualityView(R.id.setting_rate_high);
		}
	}

	/**
	 * 事件处理
	 * 
	 * @param v
	 */
	public void buttonOnclick(View v) {
		final int id = v.getId() ;
		switch (id) {
		case R.id.bnt_setting_rate_back:
			confirmSelected();
			break;
		case R.id.setting_rate_low:
		case R.id.setting_rate_middle:
		case R.id.setting_rate_high:
			// 更新UI
			updateQualityView(id);
			// 退出
			confirmSelected();
			break;
		}
	}
	
	private void updateQualityView(int id){
		switch (id) {
		case R.id.setting_rate_low:
			quality = AbstractEncoder.VideoQuality.VIDEO_QUALITY_LOW ;
			mark_low.setBackgroundResource(R.drawable.ic_ioffer_level_selected);
			mark_middle.setBackgroundResource(R.drawable.ic_ioffer_level_default);
			mark_high.setBackgroundResource(R.drawable.ic_ioffer_level_default);
			break;
		case R.id.setting_rate_middle:
			quality = AbstractEncoder.VideoQuality.VIDEO_QUALITY_MEDIUM ;
			mark_low.setBackgroundResource(R.drawable.ic_ioffer_level_default);
			mark_middle.setBackgroundResource(R.drawable.ic_ioffer_level_selected);
			mark_high.setBackgroundResource(R.drawable.ic_ioffer_level_default);
			break;
		case R.id.setting_rate_high:
			quality = AbstractEncoder.VideoQuality.VIDEO_QUALITY_HEIGH ;
			mark_low.setBackgroundResource(R.drawable.ic_ioffer_level_default);
			mark_middle.setBackgroundResource(R.drawable.ic_ioffer_level_default);
			mark_high.setBackgroundResource(R.drawable.ic_ioffer_level_selected);
			break;
		}
	}
	
	private void confirmSelected(){
		Intent intent = getIntent();
		intent.putExtra(KEY_QUALITY, quality);
		this.setResult(REQUEST_VIDEORATE_CODE, intent);
		this.finish();
	}
}
