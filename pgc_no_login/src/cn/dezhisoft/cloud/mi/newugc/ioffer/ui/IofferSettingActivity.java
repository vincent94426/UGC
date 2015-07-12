package cn.dezhisoft.cloud.mi.newugc.ioffer.ui;

import java.util.ArrayList;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import cn.dezhisoft.cloud.mi.newugc.R;

import cn.dezhisoft.cloud.mi.newugc.ioffer.common.Config;
import cn.dezhisoft.cloud.mi.newugc.ioffer.common.Util;
import cn.dezhisoft.cloud.mi.newugc.ioffer.config.AppConfig;
import cn.dezhisoft.cloud.mi.newugc.ioffer.config.VideoSize;
import cn.dezhisoft.cloud.mi.newugc.ioffer.upload.ui.UGCSettingPreference;
import com.sobey.sdk.encoder.AbstractEncoder;
import com.sobey.sdk.encoder.AbstractEncoder.VideoQuality;

/**
 * 客户端设置
 * 
 * @author Rosson Chen
 *
 */
public final class IofferSettingActivity extends BaseActivity implements UGCSettingPreference {
	
	public static final String KEY_SYSTEM				= "_system" ;
	
	static final int 	REQUEST_CODE_RECORD_SIZE		= 0x0000f1 ;
	static final int 	REQUEST_CODE_RECORD_QUALITY		= 0x0000f2 ;
	static final int 	REQUEST_CODE_LIVE_SIZE			= 0x0000f3 ;
	static final int 	REQUEST_CODE_LIVE_QUALITY		= 0x0000f4 ;
	
	/** flag*/
	int auto_flag, record_flag,live_flag , record_q,live_q;
	
	RelativeLayout systemLayout ;
	
	/** input*/
	EditText service_address ;
	
	/** buttong 组件*/
	ImageButton lock_pwd,recordBnt ,liveBnt;
	
	/** 视频质量*/
	TextView qual_title,qual_low , qual_middle, qual_high;
	
	TextView recordSize,recordBit,liveSize,liveBit ;
	
	AppConfig config ;
	
	RelativeLayout recordBntLayout;
	LinearLayout recordLayout,recordQualityLayout ;
	
	RelativeLayout liveBntLayout;
	LinearLayout liveLayout,liveQualityLayout ;
	
	SharedPreferences settings ;
	
	ArrayList<VideoSize> liveSizeList = new ArrayList<VideoSize>();
	ArrayList<VideoSize> recordSizeList = new ArrayList<VideoSize>() ;
	
	int keySystem = 0 ;
	String oldWeb ;
	
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ioffer_setting_layout);
		
		settings 	 = PreferenceManager.getDefaultSharedPreferences(this);
		systemLayout = (RelativeLayout)findViewById(R.id.host_system_layout);
		liveSizeList.clear() ;
		recordSizeList.clear() ;
		
		// 设置标题
		TextView title = (TextView) findViewById(R.id.ioffer_title_bar_content);
		title.setText(R.string.label_setting_title);
		// 得到组件
		service_address = (EditText)findViewById(R.id.edit_setting_service_address);
		lock_pwd		= (ImageButton) findViewById(R.id.bnt_setting_lock_password);
		
		config		= mAccessDatabase.getConfig() ;
		auto_flag	= config.getAuto() ;
		record_flag	= config.getRecordFlag() ;
		live_flag	= config.getLiveFlag() ;
		record_q	= config.getRecordQuality() ;
		live_q		= config.getLiveQuality() ;
		oldWeb		= config.getHost() ;
		
		loadRecordSetting();
		
		loadLiveSetting();
		
		service_address.setText(config.getHost() != null ? config.getHost() : "" ) ;
		
		updataFlag(R.id.bnt_setting_lock_password,auto_flag,false);
		updataFlag(R.id.bnt_setting_record,record_flag,false);
		updataFlag(R.id.bnt_setting_live,live_flag,false);
		
		Bundle bundle = getIntent().getExtras() ;
		
		if(bundle != null){
			
			keySystem	= bundle.getInt(KEY_SYSTEM) ;
			
			if(keySystem > 0){
				systemLayout.setVisibility(View.GONE);
			} else {
				systemLayout.setVisibility(View.VISIBLE);
			}
		} else {
			systemLayout.setVisibility(View.VISIBLE);
		}
		
		// 录制分辨率
		ArrayList<Object> list = queryVideoSize(VideoSize.Type.RECORDER);
		for(Object obj : list){
			recordSizeList.add((VideoSize)obj);
		}
		
		// 直播分辨率
		list = queryVideoSize(VideoSize.Type.LIVE);
		for(Object obj : list){
			liveSizeList.add((VideoSize)obj);
		}
	}
	
	@Override
	protected void onResume() {
		super.onResume();
	}

	private ArrayList<Object> queryVideoSize(int type){
		return mAccessDatabase.queryAllObject(VideoSize.class, new String[]{"type"}, new String[]{String.valueOf(type)});
	}
	
	private void updateQuality(int width,int height,TextView view,int quality){
		
		int bitrate = 0 ;
		
		if(quality == VideoQuality.VIDEO_QUALITY_HEIGH){
			bitrate	= Util.calculateBitrate(width,height,VideoQuality.VIDEO_QUALITY_HEIGH) ;
			view.setText(getString(R.string.label_setting_high) + "(" + Util.formatBitrate(bitrate) + ")");
		} else if(quality == VideoQuality.VIDEO_QUALITY_MEDIUM){
			bitrate	= Util.calculateBitrate(width,height,VideoQuality.VIDEO_QUALITY_MEDIUM) ;
			view.setText(getString(R.string.label_setting_middle) + "(" + Util.formatBitrate(bitrate) + ")");
		} else {
			bitrate	= Util.calculateBitrate(width,height,VideoQuality.VIDEO_QUALITY_LOW) ;
			view.setText(getString(R.string.label_setting_low) + "(" + Util.formatBitrate(bitrate) + ")");
		}
	}
	
	private void loadRecordSetting(){
		
		recordBnt		= (ImageButton) findViewById(R.id.bnt_setting_record);
		
		recordLayout	= (LinearLayout)findViewById(R.id.record_video_layout);
		recordBntLayout	= (RelativeLayout)findViewById(R.id.setting_record_layout);
		recordQualityLayout = (LinearLayout)findViewById(R.id.record_quality_layout);
		
		// 分辨率
		final View resolution	= recordLayout.findViewById(R.id.setting_quality_open_resolution);
		final View bitrate	   	= recordLayout.findViewById(R.id.setting_quality_open_rate);
		int width			= config.getRecordWidth() ;
		int height			= config.getRecordHeight() ;
		
		recordSize		= (TextView)resolution.findViewById(R.id.setting_resolution_textview);
		recordBit		= (TextView)bitrate.findViewById(R.id.setting_rate_textview);
		
		recordSize.setText(width + "x" + height) ;
		
		updateQuality(width,height,recordBit,config.getRecordQuality()) ;
		
		// 低,中,高
		final View low = recordQualityLayout.findViewById(R.id.bnt_setting_quality_low);
		final View med = recordQualityLayout.findViewById(R.id.bnt_setting_quality_middle);
		final View hig = recordQualityLayout.findViewById(R.id.bnt_setting_quality_high);
		
		if(record_q == AbstractEncoder.VideoQuality.VIDEO_QUALITY_HEIGH){
			low.setBackgroundResource(R.drawable.ic_ioffer_setting_quality_low_default);
			med.setBackgroundResource(R.drawable.ic_ioffer_setting_quality_middle_default);
			hig.setBackgroundResource(R.drawable.ic_ioffer_setting_quality_high_selected);
		} else if(record_q == AbstractEncoder.VideoQuality.VIDEO_QUALITY_MEDIUM){
			low.setBackgroundResource(R.drawable.ic_ioffer_setting_quality_low_default);
			med.setBackgroundResource(R.drawable.ic_ioffer_setting_quality_middle_selected);
			hig.setBackgroundResource(R.drawable.ic_ioffer_setting_quality_high_default);
		} else {
			low.setBackgroundResource(R.drawable.ic_ioffer_setting_quality_low_selected);
			med.setBackgroundResource(R.drawable.ic_ioffer_setting_quality_middle_default);
			hig.setBackgroundResource(R.drawable.ic_ioffer_setting_quality_high_default);
		}
		
		// 
		final View.OnClickListener listener = new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				final int id = v.getId() ;
				Intent intent ;
				
				switch(id){
				case R.id.bnt_setting_quality_low :
					low.setBackgroundResource(R.drawable.ic_ioffer_setting_quality_low_selected);
					med.setBackgroundResource(R.drawable.ic_ioffer_setting_quality_middle_default);
					hig.setBackgroundResource(R.drawable.ic_ioffer_setting_quality_high_default);
					config.setRecordQuality(AbstractEncoder.VideoQuality.VIDEO_QUALITY_LOW);
					break ;
				case R.id.bnt_setting_quality_middle :
					low.setBackgroundResource(R.drawable.ic_ioffer_setting_quality_low_default);
					med.setBackgroundResource(R.drawable.ic_ioffer_setting_quality_middle_selected);
					hig.setBackgroundResource(R.drawable.ic_ioffer_setting_quality_high_default);
					config.setRecordQuality(AbstractEncoder.VideoQuality.VIDEO_QUALITY_MEDIUM);
					break ;
				case R.id.bnt_setting_quality_high :
					low.setBackgroundResource(R.drawable.ic_ioffer_setting_quality_low_default);
					med.setBackgroundResource(R.drawable.ic_ioffer_setting_quality_middle_default);
					hig.setBackgroundResource(R.drawable.ic_ioffer_setting_quality_high_selected);
					config.setRecordQuality(AbstractEncoder.VideoQuality.VIDEO_QUALITY_HEIGH);
					break ;
				case R.id.setting_quality_open_resolution :
					intent	= new Intent() ;
					intent.putExtra(VideoSizeActivity.KEY_TYPE, VideoSize.Type.RECORDER);
					intent.putExtra(VideoSizeActivity.KEY_WIDTH, config.getRecordWidth());
					intent.putExtra(VideoSizeActivity.KEY_HEIGHT, config.getRecordHeight());
					intent.setClass(mContext, VideoSizeActivity.class);
					startActivityForResult(intent, REQUEST_CODE_RECORD_SIZE);
					break ;
				case R.id.setting_quality_open_rate :
					intent	= new Intent() ;
					intent.setClass(mContext, VideoQualityActivity.class);
					intent.putExtra(VideoQualityActivity.KEY_QUALITY, config.getRecordQuality());
					intent.putExtra(VideoQualityActivity.KEY_WIDTH, config.getRecordWidth());
					intent.putExtra(VideoQualityActivity.KEY_HEIGHT, config.getRecordHeight());
					startActivityForResult(intent, REQUEST_CODE_RECORD_QUALITY);
					break ;
				}
			}
		};
		
		low.setOnClickListener(listener);
		med.setOnClickListener(listener);
		hig.setOnClickListener(listener);
		resolution.setOnClickListener(listener);
		bitrate.setOnClickListener(listener);
	}
	
	private void loadLiveSetting(){
		
		liveBnt			= (ImageButton) findViewById(R.id.bnt_setting_live);
		
		liveLayout		= (LinearLayout)findViewById(R.id.live_video_layout);
		liveBntLayout	= (RelativeLayout)findViewById(R.id.setting_live_layout);
		liveQualityLayout = (LinearLayout)findViewById(R.id.live_quality_layout);
		
		// 分辨率
		final View resolution = liveLayout.findViewById(R.id.setting_quality_open_resolution);
		final View bitrate 	= liveLayout.findViewById(R.id.setting_quality_open_rate);
		int width		= config.getLiveWidth() ;
		int height		= config.getLiveHeight() ;

		liveSize = (TextView) resolution.findViewById(R.id.setting_resolution_textview);
		liveBit = (TextView) bitrate.findViewById(R.id.setting_rate_textview);

		liveSize.setText(config.getLiveWidth() + "x" + config.getLiveHeight());
		
		updateQuality(width,height,liveBit, config.getLiveQuality());

		// 低,中,高
		final View low = liveQualityLayout.findViewById(R.id.bnt_setting_quality_low);
		final View med = liveQualityLayout.findViewById(R.id.bnt_setting_quality_middle);
		final View hig = liveQualityLayout.findViewById(R.id.bnt_setting_quality_high);

		if (live_q == AbstractEncoder.VideoQuality.VIDEO_QUALITY_HEIGH) {
			low.setBackgroundResource(R.drawable.ic_ioffer_setting_quality_low_default);
			med.setBackgroundResource(R.drawable.ic_ioffer_setting_quality_middle_default);
			hig.setBackgroundResource(R.drawable.ic_ioffer_setting_quality_high_selected);
		} else if (live_q == AbstractEncoder.VideoQuality.VIDEO_QUALITY_MEDIUM) {
			low.setBackgroundResource(R.drawable.ic_ioffer_setting_quality_low_default);
			med.setBackgroundResource(R.drawable.ic_ioffer_setting_quality_middle_selected);
			hig.setBackgroundResource(R.drawable.ic_ioffer_setting_quality_high_default);
		} else {
			low.setBackgroundResource(R.drawable.ic_ioffer_setting_quality_low_selected);
			med.setBackgroundResource(R.drawable.ic_ioffer_setting_quality_middle_default);
			hig.setBackgroundResource(R.drawable.ic_ioffer_setting_quality_high_default);
		}

		//
		final View.OnClickListener listener = new OnClickListener() {

			@Override
			public void onClick(View v) {
				final int id = v.getId();
				Intent intent;

				switch (id) {
				case R.id.bnt_setting_quality_low:
					low.setBackgroundResource(R.drawable.ic_ioffer_setting_quality_low_selected);
					med.setBackgroundResource(R.drawable.ic_ioffer_setting_quality_middle_default);
					hig.setBackgroundResource(R.drawable.ic_ioffer_setting_quality_high_default);
					config.setLiveQuality(AbstractEncoder.VideoQuality.VIDEO_QUALITY_LOW);
					break;
				case R.id.bnt_setting_quality_middle:
					low.setBackgroundResource(R.drawable.ic_ioffer_setting_quality_low_default);
					med.setBackgroundResource(R.drawable.ic_ioffer_setting_quality_middle_selected);
					hig.setBackgroundResource(R.drawable.ic_ioffer_setting_quality_high_default);
					config.setLiveQuality(AbstractEncoder.VideoQuality.VIDEO_QUALITY_MEDIUM);
					break;
				case R.id.bnt_setting_quality_high:
					low.setBackgroundResource(R.drawable.ic_ioffer_setting_quality_low_default);
					med.setBackgroundResource(R.drawable.ic_ioffer_setting_quality_middle_default);
					hig.setBackgroundResource(R.drawable.ic_ioffer_setting_quality_high_selected);
					config.setLiveQuality(AbstractEncoder.VideoQuality.VIDEO_QUALITY_HEIGH);
					break;
				case R.id.setting_quality_open_resolution:
					intent = new Intent();
					intent.putExtra(VideoSizeActivity.KEY_TYPE, VideoSize.Type.LIVE);
					intent.putExtra(VideoSizeActivity.KEY_WIDTH, config.getLiveWidth());
					intent.putExtra(VideoSizeActivity.KEY_HEIGHT, config.getLiveHeight());
					intent.setClass(mContext, VideoSizeActivity.class);
					startActivityForResult(intent, REQUEST_CODE_LIVE_SIZE);
					break;
				case R.id.setting_quality_open_rate:
					intent = new Intent();
					intent.setClass(mContext, VideoQualityActivity.class);
					intent.putExtra(VideoQualityActivity.KEY_QUALITY, config.getLiveQuality());
					intent.putExtra(VideoQualityActivity.KEY_WIDTH, config.getLiveWidth());
					intent.putExtra(VideoQualityActivity.KEY_HEIGHT, config.getLiveHeight());
					startActivityForResult(intent, REQUEST_CODE_LIVE_QUALITY);
					break;
				}
			}
		};

		low.setOnClickListener(listener);
		med.setOnClickListener(listener);
		hig.setOnClickListener(listener);
		resolution.setOnClickListener(listener);
		bitrate.setOnClickListener(listener);
	}
	
	/**
	 * top 导航
	 * @param v
	 */
	public void topBarButtonOnclick(View v){
		
		// 隐藏键盘
		Util.hideVirtualKeyPad(mContext, service_address);
		
		final AppConfig app = config ;
		
		switch(v.getId()){
		case R.id.bnt_ioffer_title_bar_left :
			finish() ;
			break ;
		case R.id.bnt_ioffer_title_bar_right :
			
			// 是否更新web地址
			if(keySystem < 1) {
				
				String web = service_address.getText().toString().trim();
				
				if(web.equals("")){
					showMessageDialog(getString(R.string.warring_setting_fill_webserver));
					return ;
				}
				
				// 更新配置
				app.setHost(web);
				
				// 修改了web地址
				if(!web.equals(oldWeb)){
					app.setSiteId("-1");
					app.setSiteName("");
				}
			}
			
			// 更新数据库
			mAccessDatabase.updateObject(app);
			
			int width = 0, height = 0 , quality = 0;
			VideoSize pre = null ;
			int size = 0 ;
			
			// 设置录制
			if(app.getRecordFlag() == Config.FLGA_BUTTON_ON){
				
				width	= app.getRecordWidth() ;
				height	= app.getRecordHeight() ;
				quality = app.getRecordQuality() ;
				
				settings.edit()
				.putInt(KEY_RECORDER_WIDTH, width)
				.putInt(KEY_RECORDER_HEIGHT, height)
				.putInt(KEY_RECORDER_BITRATE, Util.calculateBitrate(width, height, quality))
				.commit() ;
				
			} else {
				
				if(recordSizeList != null && recordSizeList.size() > 0){
					
					size 	= recordSizeList.size() ;
					
					// 高质量
					if(app.getRecordQuality() == AbstractEncoder.VideoQuality.VIDEO_QUALITY_HEIGH){
						
						pre		= recordSizeList.get(size - 1) ;
						quality = AbstractEncoder.VideoQuality.VIDEO_QUALITY_HEIGH ;
						
						width	= pre.getWidth() ;
						height	= pre.getHeight() ;
						
						settings.edit()
						.putInt(KEY_RECORDER_WIDTH, width)
						.putInt(KEY_RECORDER_HEIGHT, height)
						.putInt(KEY_RECORDER_BITRATE, Util.calculateBitrate(width, height,quality))
						.commit() ;
					} 
					// 中质量
					else if(app.getRecordQuality() == AbstractEncoder.VideoQuality.VIDEO_QUALITY_MEDIUM){
						
						pre		= recordSizeList.get(size / 2) ;
						quality = AbstractEncoder.VideoQuality.VIDEO_QUALITY_MEDIUM ;
						
						width	= pre.getWidth() ;
						height	= pre.getHeight() ;
						
						settings.edit()
						.putInt(KEY_RECORDER_WIDTH, width)
						.putInt(KEY_RECORDER_HEIGHT, height)
						.putInt(KEY_RECORDER_BITRATE, Util.calculateBitrate(width, height, quality))
						.commit() ;
					} 
					// 低质量
					else {
						
						pre		= recordSizeList.get(0) ;
						quality = AbstractEncoder.VideoQuality.VIDEO_QUALITY_LOW ;
						
						width	= pre.getWidth() ;
						height	= pre.getHeight() ;
						
						settings.edit()
						.putInt(KEY_RECORDER_WIDTH, width)
						.putInt(KEY_RECORDER_HEIGHT, height)
						.putInt(KEY_RECORDER_BITRATE, Util.calculateBitrate(width, height, quality))
						.commit() ;
					}
					
				} else {
					Toast.makeText(getApplicationContext(), "Camera Support List is null", Toast.LENGTH_SHORT).show() ;
				}
			}
			
			// 直播设置
			if(app.getLiveFlag() == Config.FLGA_BUTTON_ON){
				
				width	= app.getLiveWidth() ;
				height	= app.getLiveHeight() ;
				quality = app.getLiveQuality() ;
				
				settings.edit()
				.putInt(KEY_ENCODER_WIDTH, width)
				.putInt(KEY_ENCODER_HEIGHT, height)
				.putInt(KEY_ENCODER_BITRATE, Util.calculateBitrate(width, height, quality))
				.commit() ;
				
			} else {
				
				if(liveSizeList != null && liveSizeList.size() > 0){
					
					size 	= liveSizeList.size() ;
					
					// 高质量
					if(app.getLiveQuality() == AbstractEncoder.VideoQuality.VIDEO_QUALITY_HEIGH){
						
						quality = AbstractEncoder.VideoQuality.VIDEO_QUALITY_HEIGH ;
						pre		= liveSizeList.get(size - 1) ;
						
						width	= pre.getWidth() ;
						height	= pre.getHeight() ;
						
						settings.edit()
						.putInt(KEY_ENCODER_WIDTH, width)
						.putInt(KEY_ENCODER_HEIGHT, height)
						.putInt(KEY_ENCODER_BITRATE, Util.calculateBitrate(width, height, quality))
						.commit() ;
					} 
					// 中质量
					else if(app.getLiveQuality() == AbstractEncoder.VideoQuality.VIDEO_QUALITY_MEDIUM){
						
						quality = AbstractEncoder.VideoQuality.VIDEO_QUALITY_MEDIUM ;
						pre		= liveSizeList.get(size / 2) ;
						
						width	= pre.getWidth() ;
						height	= pre.getHeight() ;
						
						settings.edit()
						.putInt(KEY_ENCODER_WIDTH, width)
						.putInt(KEY_ENCODER_HEIGHT, height)
						.putInt(KEY_ENCODER_BITRATE, Util.calculateBitrate(width, height, quality))
						.commit() ;
					} 
					// 低质量
					else {
						
						quality = AbstractEncoder.VideoQuality.VIDEO_QUALITY_LOW ;
						pre		= liveSizeList.get(0) ;
						
						width	= pre.getWidth() ;
						height	= pre.getHeight() ;
						
						settings.edit()
						.putInt(KEY_ENCODER_WIDTH, width)
						.putInt(KEY_ENCODER_HEIGHT, height)
						.putInt(KEY_ENCODER_BITRATE, Util.calculateBitrate(width, height, quality))
						.commit() ;
					}
					
				} else {
					Toast.makeText(getApplicationContext(), "Camera Support List is null", Toast.LENGTH_SHORT).show() ;
				}
			}
			
			finish() ;
			break ;
		}
	}
	
	/**
	 * button 事件
	 * @param v
	 */
	public void buttonOnclick(View v){
		
		final int id = v.getId() ;
		switch(id){
		case R.id.bnt_setting_lock_password :
			auto_flag = (++auto_flag) % 2 ;
			updataFlag(id,auto_flag,true);
			break ;
		case R.id.bnt_setting_record :
			record_flag = (++record_flag) % 2 ;
			updataFlag(id,record_flag,true);
			break ;
		case R.id.bnt_setting_live :
			live_flag = (++live_flag) % 2 ;
			updataFlag(id,live_flag,true);
			break ;
		}
	}
	
	private void updataFlag(int id,int flag, boolean update){
		// 关闭
		if(flag == Config.FLGA_BUTTON_OFF){
			// 自动登录
			if(id == R.id.bnt_setting_lock_password){
				lock_pwd.setBackgroundResource(R.drawable.ic_ioffer_bnt_off);
				
				if(update) config.setAuto(Config.FLGA_BUTTON_OFF);
			} 
			// 录制
			else if(id == R.id.bnt_setting_record){
				recordBnt.setBackgroundResource(R.drawable.ic_ioffer_bnt_off);
				
				recordBntLayout.setBackgroundResource(R.drawable.ic_ioffer_setting_auto_bg);
				recordLayout.setVisibility(View.GONE);
				recordQualityLayout.setVisibility(View.VISIBLE);
				
				if(update) config.setRecordFlag(Config.FLGA_BUTTON_OFF);
			} 
			// 直播
			else if(id == R.id.bnt_setting_live){
				liveBnt.setBackgroundResource(R.drawable.ic_ioffer_bnt_off);
				
				liveBntLayout.setBackgroundResource(R.drawable.ic_ioffer_setting_auto_bg);
				liveLayout.setVisibility(View.GONE);
				liveQualityLayout.setVisibility(View.VISIBLE);
				
				if(update) config.setLiveFlag(Config.FLGA_BUTTON_OFF);
			}
		}
		// 打开
		else if(flag == Config.FLGA_BUTTON_ON){
			// 自动登录
			if(id == R.id.bnt_setting_lock_password){
				lock_pwd.setBackgroundResource(R.drawable.ic_ioffer_bnt_on);
				
				if(update) config.setAuto(Config.FLGA_BUTTON_ON);
			} 
			// 录制
			else if(id == R.id.bnt_setting_record){
				recordBnt.setBackgroundResource(R.drawable.ic_ioffer_bnt_on);
				
				recordBntLayout.setBackgroundResource(R.drawable.ic_ioffer_setting_custom_start_bg);
				recordLayout.setVisibility(View.VISIBLE);
				recordQualityLayout.setVisibility(View.GONE);
				
				if(update) config.setRecordFlag(Config.FLGA_BUTTON_ON);
			} 
			// 直播
			else if(id == R.id.bnt_setting_live){
				liveBnt.setBackgroundResource(R.drawable.ic_ioffer_bnt_on);
				
				liveBntLayout.setBackgroundResource(R.drawable.ic_ioffer_setting_custom_start_bg);
				liveLayout.setVisibility(View.VISIBLE);
				liveQualityLayout.setVisibility(View.GONE);
				
				if(update) config.setLiveFlag(Config.FLGA_BUTTON_ON);
			}
		}
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		
		int width 	= 0 ;
		int height 	= 0 ;
		int quality = 0 ;
		
		// 视频分辨率
		if(resultCode == VideoSizeActivity.REQUEST_VIDEOSIZE_CODE){
			width 	= data.getExtras().getInt(VideoSizeActivity.KEY_WIDTH);
			height 	= data.getExtras().getInt(VideoSizeActivity.KEY_HEIGHT);
		}
		
		// 视频质量,低,中,高
		if (resultCode == VideoQualityActivity.REQUEST_VIDEORATE_CODE) {
			quality = data.getExtras().getInt(VideoQualityActivity.KEY_QUALITY);
		}
		
		switch(requestCode){
		case REQUEST_CODE_RECORD_SIZE :
			
			if(width != 0 && height != 0){
				recordSize.setText(width + "x" + height);
				config.setRecordWidth(width);
				config.setRecordHeight(height);
				
				updateQuality(width,height,recordBit,config.getRecordQuality()) ;
			}
			break ;
		case REQUEST_CODE_RECORD_QUALITY :
			
			// 视频质量,低,中,高
			if(quality != 0){
				
				width  	= config.getRecordWidth() ;
				height 	= config.getRecordHeight() ;
				config.setRecordQuality(quality);
				
				updateQuality(width,height,recordBit,quality) ;
			}
			break ;
		case REQUEST_CODE_LIVE_SIZE :
			
			if(width != 0 && height != 0){
				liveSize.setText(width + "x" + height);
				config.setLiveWidth(width);
				config.setLiveHeight(height);
				
				updateQuality(width,height,liveBit,config.getLiveQuality()) ;
			}
			
			break ;
		case REQUEST_CODE_LIVE_QUALITY :
			
			if(quality != 0){

				width 	= config.getLiveWidth();
				height 	= config.getLiveHeight();
				config.setLiveQuality(quality);

				updateQuality(width, height, liveBit, quality);
			}
			break ;
		}
	}
}
