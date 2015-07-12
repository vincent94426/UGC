package cn.dezhisoft.cloud.mi.newugc.ugv2.activity;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import cn.dezhisoft.cloud.mi.newugc.R;
import cn.dezhisoft.cloud.mi.newugc.common.util.CommonUtils;
import cn.dezhisoft.cloud.mi.newugc.common.util.SettingUtil;
import cn.dezhisoft.cloud.mi.newugc.ioffer.config.AppConfig;
import cn.dezhisoft.cloud.mi.newugc.ioffer.config.VideoSize;
import cn.dezhisoft.cloud.mi.newugc.ioffer.service.IofferService;
import cn.dezhisoft.cloud.mi.newugc.ugv2.base.activity.BaseActivity;
import cn.dezhisoft.cloud.mi.newugc.ugv2.base.app.AppUtils;
import cn.dezhisoft.cloud.mi.newugc.ugv2.constants.UgV2Constants;

public class SettingActivity extends BaseActivity {
	private static final int[] VIDEO_QUALITY = new int[]{UgV2Constants.VIDEO_QUALITY_LOW, UgV2Constants.VIDEO_QUALITY_MEDIUM, UgV2Constants.VIDEO_QUALITY_HIGH};
	private TextView logoutBtn;
	private TextView tvUsername, tvNickname, tvEmail, tvPhone, tvVersion;
	private EditText serverAddrText;
	private Spinner  spVideoSize, spBitRate;
	private List<VideoSize> recordSizeList = new ArrayList<VideoSize>();
	private VideoSize selectedVideoSize;
	private int selectedVideoQuality;
	
	@Override
	protected View onCreateBody() {
		View body = getLayoutInflater().inflate(R.layout.ugv2_setting_layout, null);
		
		logoutBtn = (TextView) body.findViewById(R.id.ugv2_logout_btn);
		tvUsername = (TextView) body.findViewById(R.id.ugv2_setting_username_view);
		tvNickname = (TextView) body.findViewById(R.id.ugv2_setting_nickname_view);
		tvEmail = (TextView) body.findViewById(R.id.ugv2_setting_email_view);
		tvPhone = (TextView) body.findViewById(R.id.ugv2_setting_phone_view);
        tvVersion = (TextView) body.findViewById(R.id.ugv2_setting_version);
		
		serverAddrText = (EditText) body.findViewById(R.id.ugv2_setting_server_addr_view);
		spVideoSize = (Spinner) body.findViewById(R.id.ugv2_setting_size_spinner);
		spBitRate   = (Spinner) body.findViewById(R.id.ugv2_setting_bitrate_spinner);
		
		tvUsername.setText(IofferService.getNewTipService().getUser().getUsername());
		logoutBtn.setOnClickListener(logoutListener);
	
		int selectedWidth = SettingUtil.readInt(mContext, UgV2Constants.KEY_VIDEO_SELECTED_WIDTH, UgV2Constants.VIDEO_DEFAULT_WIDTH);
		int selectedHeight = SettingUtil.readInt(mContext, UgV2Constants.KEY_VIDEO_SELECTED_HEIGHT, UgV2Constants.VIDEO_DEFAULT_HEIGHT);
		
		// 录制分辨率
		ArrayList<Object> list = queryVideoSize(VideoSize.Type.RECORDER);
		if(null != list && list.size() > 0){
			String[] videoSizeNames = new String[list.size()];
			int index = 0, selectIndex = 0;
			for(Object obj : list){
				VideoSize videoSize = (VideoSize)obj;
				recordSizeList.add(videoSize);
				int width = videoSize.getWidth();
				int height = videoSize.getHeight();
				if(selectedWidth == width && selectedHeight == height){
					selectIndex = index;
				}
				videoSizeNames[index++] = width + "x" + height;
			}
			
			ArrayAdapter<String> videoSizeAdapter = new ArrayAdapter<String>(mContext, R.layout.ugv2_simle_spinner_item_blue, videoSizeNames);
			videoSizeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			spVideoSize.setAdapter(videoSizeAdapter);
			spVideoSize.setOnItemSelectedListener(onVideoSizeSelectedListener);
			spVideoSize.setSelection(selectIndex);
			//spVideoSize.setSelection(position);
		}
		
		setUiTitle(R.string.ugv2_txt_title_setting);
		
		return body;
	}

	private OnItemSelectedListener onVideoSizeSelectedListener = new OnItemSelectedListener() {

		@Override
		public void onItemSelected(AdapterView<?> arg0, View arg1,
				int position, long arg3) {
			selectedVideoSize = recordSizeList.get(position);
			
			int selectedIndex = getSelectedBitrateIndex();
			
			int width = selectedVideoSize.getWidth();
			int height = selectedVideoSize.getHeight();
			int lowBitrate = CommonUtils.calculateBitrate(width, height, UgV2Constants.VIDEO_QUALITY_LOW);
			int midBitrate = CommonUtils.calculateBitrate(width, height, UgV2Constants.VIDEO_QUALITY_MEDIUM);
			int highBitrate = CommonUtils.calculateBitrate(width, height, UgV2Constants.VIDEO_QUALITY_HIGH);
			
			String highBitText = getString(R.string.label_setting_high) + "(" + CommonUtils.formatBitrate(highBitrate) + ")";
			String middleBitText = getString(R.string.label_setting_middle) + "(" + CommonUtils.formatBitrate(midBitrate) + ")";
			String lowBitText = getString(R.string.label_setting_low) + "(" + CommonUtils.formatBitrate(lowBitrate) + ")";
			
			String[] bitRateTextArray = new String[]{lowBitText, middleBitText, highBitText};
			ArrayAdapter<String> bitRateAdapter = new ArrayAdapter<String>(mContext, R.layout.ugv2_simle_spinner_item_blue, bitRateTextArray);
			bitRateAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			spBitRate.setAdapter(bitRateAdapter);
			spBitRate.setOnItemSelectedListener(onBitRateSelectedListener);
			spBitRate.setSelection(selectedIndex);
		}

		private int getSelectedBitrateIndex() {
			int selectedQuality = SettingUtil.readInt(mContext, UgV2Constants.KEY_VIDEO_SELECTED_QUALITY, UgV2Constants.VIDEO_QUALITY_LOW);
			int selectedIndex = 0;
			if(selectedQuality == UgV2Constants.VIDEO_QUALITY_MEDIUM){
				selectedIndex = 1;
			}else if(selectedQuality == UgV2Constants.VIDEO_QUALITY_HIGH){
				selectedIndex = 2;
			}
			return selectedIndex;
		}

		@Override
		public void onNothingSelected(AdapterView<?> arg0) {
			
		}
	};

	private OnItemSelectedListener onBitRateSelectedListener = new OnItemSelectedListener() {

		@Override
		public void onItemSelected(AdapterView<?> arg0, View arg1,
				int position, long arg3) {
			selectedVideoQuality = VIDEO_QUALITY[position];
			
			SettingUtil.writeInt(mContext, UgV2Constants.KEY_VIDEO_SELECTED_WIDTH, selectedVideoSize.getWidth());
			SettingUtil.writeInt(mContext, UgV2Constants.KEY_VIDEO_SELECTED_HEIGHT, selectedVideoSize.getHeight());
			SettingUtil.writeInt(mContext, UgV2Constants.KEY_VIDEO_SELECTED_QUALITY, selectedVideoQuality);
		}

		@Override
		public void onNothingSelected(AdapterView<?> arg0) {
			
		}
	};
	
	private ArrayList<Object> queryVideoSize(int type){
		return mAccessDatabase.queryAllObject(VideoSize.class, new String[]{"type"}, new String[]{String.valueOf(type)});
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
	}

	@Override
	protected void onPause() {
		//saveServerAddr();
		
		super.onPause();
	}

	private void saveServerAddr() {
		AppConfig config = mAccessDatabase.getConfig();
		String oldhost = config.getHost();
		
		String newhost = serverAddrText.getText().toString();
		if(TextUtils.isEmpty(newhost) || newhost.equals(oldhost)){
			return ;
		}
		
		config.setHost(newhost);
		config.setSiteId("-1");
		config.setSiteName("");
		
		mAccessDatabase.updateObject(config);
	}

	@Override
	protected void onResume() {
		super.onResume();
		topBar.setRightBtnVisibility(View.GONE);
		
		/*AppConfig config = mAccessDatabase.getConfig();
		String host = null == config ? "" : config.getHost();
		
		serverAddrText.setText(host);
		serverAddrText.clearFocus();
		InputMethodManager imm = (InputMethodManager)this.getSystemService(INPUT_METHOD_SERVICE);
		imm.hideSoftInputFromWindow(serverAddrText.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);*/

        //显示版本号
        try {
            PackageInfo packageInfo = mContext.getPackageManager().getPackageInfo(mContext.getPackageName(), 0);
            tvVersion.setText(packageInfo.versionName);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

	}

	OnClickListener logoutListener = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			AppUtils.notifyAppStateListener();
			
			Intent intent = new Intent(mContext, LoginActivity.class);
			startActivity(intent);
		}
	};
}
