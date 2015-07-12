package cn.dezhisoft.cloud.mi.newugc.ioffer.upload.ui;

import java.io.File;
import java.util.UUID;

import android.content.DialogInterface;
import android.content.Intent;
import android.location.Address;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import cn.dezhisoft.cloud.mi.newugc.R;

import cn.dezhisoft.cloud.mi.newugc.ioffer.common.Config;
import cn.dezhisoft.cloud.mi.newugc.ioffer.common.Util;
import cn.dezhisoft.cloud.mi.newugc.ioffer.ui.IofferSettingActivity;
import com.sobey.sdk.db.IDBManager;
import com.sobey.sdk.gps.LocationCallback;
import com.sobey.sdk.gps.LocationFactory;
import cn.dezhisoft.cloud.mi.newugc.ugc.core.model.Metadata;
import cn.dezhisoft.cloud.mi.newugc.ugc.core.model.Task;
import cn.dezhisoft.cloud.mi.newugc.ugc.core.ws.CWUploadWebService;
import cn.dezhisoft.cloud.mi.newugc.ugc.model.Category;
import cn.dezhisoft.cloud.mi.newugc.ugc.ws.UGCWebService;

/**
 * 上传主界面：选择文件，创建任务
 * UGC Upload Component
 * 
 * @author Rosson Chen
 * 
 */
public final class UGCAddUploadActivity extends UGCBaseActivity {
	
	/** 视频 */
	public static final String MIME_MP4 		= "video/mp4";
	public static final String MIME_3GP 		= "video/3gpp";
	
	/** for debug*/
	static final String TAG						= "TabAddActivity" ;
	static final String DEFAULT_TITLE 			= "" ;
	
	static final int REQUEST_CODE_VIDEO_RECORD	= 0x0000ff ;
	static final String TEMP_PATH 				= Environment.getExternalStorageDirectory() + File.separator + "IofferTemp" + File.separator;

	final static int VIDEO_REQUESTCODE 			= 1;
	final static int AUDIO_REQUESTCODE 			= VIDEO_REQUESTCODE + 1;
	final static int AUDIO_RESULTCODE 			= AUDIO_REQUESTCODE + 1;
	final static int IMAGE_REQUESTCODE 			= AUDIO_RESULTCODE + 1;
	final static int IMAGE_CAPTURE 				= IMAGE_REQUESTCODE + 1;
	final static int LEVEL_ONE_REQUESTCODE 		= IMAGE_CAPTURE + 1;
	final static int LEVEL_SENCOND_REQUESTCODE  = LEVEL_ONE_REQUESTCODE + 1;
	final static int VIDEO_CUT_REQUESTCODE 		= LEVEL_SENCOND_REQUESTCODE + 1;
	
	final static int ADD_FLAG_FILE				= 0x00ff00 ;
	final static int ADD_FLAG_LIVE				= ADD_FLAG_FILE + 1 ;
	final static int ADD_FLAG_RECORDER			= ADD_FLAG_LIVE + 1 ;
	
	protected File icon_file; 
	LinearLayout choice_layout ;
	FrameLayout meta_data_layout ;
	Button bnt_file , bnt_live ,bnt_recorder /*,bnt_recorder_start*/;
	RelativeLayout add_file_layout , add_live_layout,add_recorder_layout;
	
	EditText file_title,
			 file_keyword,
			 file_description,
			 file_first_level,
			 file_sencond_level,
			 file_name;
	
	EditText live_title ,
			 live_keyword,
			 live_description,
			 live_location ;
	
	EditText 	recorder_title,
				recorder_keyword,
				recorder_description,
				recorder_location,
				recorder_video_name;
	
	ImageButton live_bnt_location, recorder_bnt_location, setting_bnt;
	
	int live_location_flag ,recorder_location_flag;
	
	/** popup menu*/
	PopupWindow 		popupMenu ;
	String 				filePath,fileName;
	LocationFactory 	gpsFactory;
	String 				parentCatalogId = "",subCatalogId = "";
	int 				catalog ;
	int 				add_flag ;
	Task				task ;
	
	long firstTime = System.currentTimeMillis();
	
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ugc_tab_add_layout);
		
		// by wei.zhou. fix bug
		Config.checkActivityStatus(this);
		
		// gps
		gpsFactory		 = LocationFactory.createLocationFactory(this);
		// title
		TextView title 	 = (TextView) findViewById(R.id.ioffer_add_title);
		title.setText(R.string.ugc_label_add_title);
		meta_data_layout = (FrameLayout) findViewById(R.id.add_content_layout);
		choice_layout	 = (LinearLayout)findViewById(R.id.add_bnt_choice_layout);
		bnt_file		 = (Button) findViewById(R.id.bnt_add_file);
		bnt_live		 = (Button) findViewById(R.id.bnt_add_live);
		bnt_recorder	 = (Button) findViewById(R.id.bnt_add_recorder);
		setting_bnt		 = (ImageButton)findViewById(R.id.bnt_upload_setting);
		
		loadViews();
		
		configTab();
		
		loadPopupMenu();
		
		showAddFileLayout();
		
		querySiteCategory() ;
	}
	
	private void loadViews(){
		// add file
		add_file_layout = (RelativeLayout) getLayoutInflater().inflate(R.layout.ugc_tab_add_file_layout, null);
		file_title		= (EditText) add_file_layout.findViewById(R.id.edit_add_file_title);
		file_keyword	= (EditText) add_file_layout.findViewById(R.id.edit_add_file_keyword);
		file_description= (EditText) add_file_layout.findViewById(R.id.edit_add_file_description);
		file_first_level= (EditText) add_file_layout.findViewById(R.id.edit_add_file_first_level);
		file_sencond_level= (EditText) add_file_layout.findViewById(R.id.edit_add_file_second_level);
		file_name		= (EditText) add_file_layout.findViewById(R.id.edit_add_file_path);
		file_title.setText(DEFAULT_TITLE);
		// add live
		add_live_layout = (RelativeLayout) getLayoutInflater().inflate(R.layout.ugc_tab_add_live_layout, null);
		live_title		= (EditText) add_live_layout.findViewById(R.id.edit_add_live_title);
		live_keyword	= (EditText) add_live_layout.findViewById(R.id.edit_add_live_description);
		live_description= (EditText) add_live_layout.findViewById(R.id.edit_add_live_from);
		live_location	= (EditText) add_live_layout.findViewById(R.id.edit_add_live_location);
		live_bnt_location= (ImageButton)add_live_layout.findViewById(R.id.bnt_live_location);
		// button
		live_title.setText(DEFAULT_TITLE);
		live_bnt_location.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				live_location_flag = (++live_location_flag) % 2 ;
				updateGpsFlag(ADD_FLAG_LIVE,live_location_flag,true);
			}
		}) ;
		// add recorder
		add_recorder_layout 	= (RelativeLayout) getLayoutInflater().inflate(R.layout.ugc_tab_add_recorder_layout, null);
		recorder_title			= (EditText) add_recorder_layout.findViewById(R.id.edit_add_recorder_title);
		recorder_keyword		= (EditText) add_recorder_layout.findViewById(R.id.edit_add_recorder_keyword);
		recorder_description	= (EditText) add_recorder_layout.findViewById(R.id.edit_add_recorder_description);
		recorder_location		= (EditText) add_recorder_layout.findViewById(R.id.edit_add_recorder_location);
		recorder_video_name		= (EditText) add_recorder_layout.findViewById(R.id.edit_add_recorder_video_path);
		recorder_bnt_location	= (ImageButton)add_recorder_layout.findViewById(R.id.bnt_recorder_location);
		//
		recorder_title.setText(DEFAULT_TITLE);
		recorder_bnt_location.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				recorder_location_flag = (++recorder_location_flag) % 2 ;
				updateGpsFlag(ADD_FLAG_RECORDER,recorder_location_flag,true);
			}
		}) ;
	}
	
	private final void configTab() {
		bnt_file.setVisibility(View.VISIBLE);
		bnt_recorder.setVisibility(View.VISIBLE);
		bnt_live.setVisibility(View.VISIBLE);
		choice_layout.setWeightSum(2);
		choice_layout.requestLayout();
	}
	
	private final void updateGpsFlag(final int add,int flag,boolean save){
		
		final UGCUploadConfig 	config	= AppConfig ;
		final IDBManager db		= AppDatabase ;
		final LocationFactory gps = gpsFactory ;
		
		switch(flag){
		case FLGA_BUTTON_ON :
			if(add == ADD_FLAG_LIVE)
				live_bnt_location.setBackgroundResource(R.drawable.ugc_ic_ioffer_bnt_on);
			else if(add == ADD_FLAG_RECORDER)
				recorder_bnt_location.setBackgroundResource(R.drawable.ugc_ic_ioffer_bnt_on);
			//
			if(save){
				config.setGpsFlag(FLGA_BUTTON_ON);
				db.updateObject(config);
			}
			//
			gpsFactory.startLocation(new LocationCallback() {
				
				@Override
				public void callback(Location location, Address address, String addText) {
					Log.d(TAG, "Latitude=" + location.getLatitude() + " Longitude=" + location.getLongitude());
					if(add == ADD_FLAG_LIVE)
						live_location.setText(addText);
					else if(add == ADD_FLAG_RECORDER)
						recorder_location.setText(addText);
				}
			}) ;
			break ;
		case FLGA_BUTTON_OFF :
			if(add == ADD_FLAG_LIVE)
				live_bnt_location.setBackgroundResource(R.drawable.ugc_ic_ioffer_bnt_off);
			else if(add == ADD_FLAG_RECORDER)
				recorder_bnt_location.setBackgroundResource(R.drawable.ugc_ic_ioffer_bnt_off);
			//
			if(save){
				config.setGpsFlag(FLGA_BUTTON_OFF);
				db.updateObject(config);
			}
			//
			gps.removeAllListener();
			//
			if(add == ADD_FLAG_LIVE)
				live_location.setText("");
			else if(add == ADD_FLAG_RECORDER)
				recorder_location.setText("");
			break ;
		}
	}
	
	private final void loadPopupMenu(){
		View content = getLayoutInflater().inflate(R.layout.ugc_popup_menu, null);
		content.findViewById(R.id.bnt_popup_video).setOnClickListener(popup_menu_listenr);
		content.findViewById(R.id.bnt_popup_audio).setOnClickListener(popup_menu_listenr);
		content.findViewById(R.id.bnt_popup_image).setOnClickListener(popup_menu_listenr);
		content.findViewById(R.id.bnt_popup_camera).setOnClickListener(popup_menu_listenr);
		content.findViewById(R.id.bnt_popup_cancel).setOnClickListener(popup_menu_listenr);
		popupMenu = new PopupWindow(content,getWindowManager().getDefaultDisplay().getWidth(),getWindowManager().getDefaultDisplay().getHeight(),true);
	}
	
	private final void showAddFileLayout(){
		bnt_file.setBackgroundResource(R.drawable.ugc_ic_ioffer_setting_quality_low_selected);
		bnt_recorder.setBackgroundResource(R.drawable.ugc_ic_ioffer_setting_quality_middle_default);
		bnt_live.setBackgroundResource(R.drawable.ugc_ic_ioffer_setting_quality_high_default);
		meta_data_layout.removeAllViews();
		meta_data_layout.addView(add_file_layout);
		file_title.clearFocus();
		setting_bnt.setVisibility(View.VISIBLE);
		add_flag 	= ADD_FLAG_FILE ;
	}
	
	private final void showAddRecorderLayout() {
		bnt_file.setBackgroundResource(R.drawable.ugc_ic_ioffer_setting_quality_low_default);
		bnt_recorder.setBackgroundResource(R.drawable.ugc_ic_ioffer_setting_quality_middle_selected);
		bnt_live.setBackgroundResource(R.drawable.ugc_ic_ioffer_setting_quality_high_default);
		meta_data_layout.removeAllViews();
		meta_data_layout.addView(add_recorder_layout);
		recorder_title.clearFocus();
		setting_bnt.setVisibility(View.VISIBLE);
		updateGpsFlag(ADD_FLAG_RECORDER,AppConfig.getGpsFlag(),false);
		add_flag 	= ADD_FLAG_RECORDER ;
	}
	
	private final void showAddLiveLayout(){
		bnt_file.setBackgroundResource(R.drawable.ugc_ic_ioffer_setting_quality_low_default);
		bnt_recorder.setBackgroundResource(R.drawable.ugc_ic_ioffer_setting_quality_middle_default);
		bnt_live.setBackgroundResource(R.drawable.ugc_ic_ioffer_setting_quality_high_selected);
		meta_data_layout.removeAllViews();
		meta_data_layout.addView(add_live_layout);
		live_title.clearFocus();
		setting_bnt.setVisibility(View.VISIBLE);
		updateGpsFlag(ADD_FLAG_LIVE,AppConfig.getGpsFlag(),false);
		add_flag 	= ADD_FLAG_LIVE ;
	}
	
	public void buttonOnclick(View v) {
		
		final IDBManager db	= AppDatabase ;
		final int id = v.getId() ;
		
		if(id == R.id.bnt_add_file){
			showAddFileLayout();
		} else if(id == R.id.bnt_add_live){
			showAddLiveLayout();
		} else if(id == R.id.bnt_add_recorder){
			showAddRecorderLayout();
		} 
		// 开始上传
		else if(id == R.id.bnt_ugc_add_upload_start){
			if(add_flag == ADD_FLAG_FILE){
				
				if(buildFileMetedata()){
					
					db.saveObject(task);
					db.saveObject(task.getMetadata());
					sendQueryBroadcast();
					Toast.makeText(this, R.string.ugc_label_upload_add_succeed, Toast.LENGTH_SHORT).show();
					
					filePath = "" ;
					fileName = "" ;
					file_name.setText("");
				}
				
			} else if(add_flag == ADD_FLAG_LIVE){
				
				Metadata metadata = buildLiveMetedata();
				
				
				if(metadata != null){
					Intent live = new Intent() ;
					live.putExtra(UGCLiveActivity.KEY_META, metadata);
					live.setClass(this, UGCLiveActivity.class);
					startActivity(live);
				}
			} else if(add_flag == ADD_FLAG_RECORDER){
				
				if(buildRecorderMetedata()) {
					db.saveObject(task);
					db.saveObject(task.getMetadata());
					sendQueryBroadcast();
					Toast.makeText(this, R.string.ugc_label_upload_add_succeed, Toast.LENGTH_SHORT).show();
				
					filePath = "" ;
					fileName = "" ;
					recorder_video_name.setText("");
				}
			}
		}
		// 一级分类
		else if(id == R.id.edit_add_file_first_level){
			
			// 是否重新查询分类
			if(CategoryList == null || CategoryList.size() == 0){
				showListenerDialog(getString(R.string.ugc_label_level_catalog_empty),catalogDialogListener);
				return ;
			}
			
			Intent level_1 = new Intent() ;
			level_1.setClass(this, UGCChoiceCatalogActivity.class);
			level_1.putExtra(UGCChoiceCatalogActivity.KEY_LEVEL, UGCChoiceCatalogActivity.CHOICE_LEVEL_FIRST);
			startActivityForResult(level_1,LEVEL_ONE_REQUESTCODE);
		} 
		// 二级分类
		else if(id == R.id.edit_add_file_second_level){
			
			if(rootCategory == null){
				showHintDialog(R.string.ugc_label_level_first_hint);
				return ;
			}
			
			if(rootCategory.getSubCategoryList().size() == 0){
				showHintDialog(R.string.ugc_label_level_first_empty);
				return ;
			}
			
			Intent level_2 = new Intent() ;
			level_2.setClass(this, UGCChoiceCatalogActivity.class);
			level_2.putExtra(UGCChoiceCatalogActivity.KEY_LEVEL, UGCChoiceCatalogActivity.CHOICE_LEVEL_SENCON);
			startActivityForResult(level_2,LEVEL_SENCOND_REQUESTCODE);
		} else if(id == R.id.edit_add_file_path || id == R.id.bnt_add_file_path){
			final PopupWindow popup = popupMenu ;
			if(popup != null && !popup.isShowing()){
				popup.setAnimationStyle(R.anim.from_bottom_in);
				popup.setFocusable(true);  
				popup.update(); 
				popup.showAtLocation(getWindow().getDecorView(),Gravity.FILL,0,0);
			}
		} else if(id == R.id.edit_add_recorder_video_path || id == R.id.bnt_add_recorder_path){
			Intent path = new Intent() ;
			path.setClass(this, UGCRecordActivity.class);
			startActivityForResult(path,REQUEST_CODE_VIDEO_RECORD);
		} else if(id == R.id.bnt_upload_setting ){
			Intent set = new Intent() ;
			set.putExtra(IofferSettingActivity.KEY_SYSTEM, 1);
			set.setClass(this, IofferSettingActivity.class);
			startActivity(set);
		}
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		//
		if(gpsFactory != null){ 
			gpsFactory.removeAllListener() ;
		}
	}

	private Metadata buildLiveMetedata(){
		
		String title 	= live_title.getText().toString().trim();
		String keyword	= live_keyword.getText().toString().trim();
		String descri 	= live_description.getText().toString().trim();
		String location	= live_location.getText().toString().trim();
		
		if (title.equals("")) {
			showHintDialog(R.string.ugc_warring_title);
			return null ;
		}
		
		String result = Util.checkInput(title);
		if(result != null){
			showHintDialog(getString(R.string.warnning_login_input_illegal));
			return null ;
		}
		
		//
		Metadata metadata = new Metadata();
		metadata.setSiteId(UGCWebService.getSiteId());
		metadata.setTitle(title);
		metadata.setPlace(location);
		metadata.setGps(gpsFactory != null ? gpsFactory.getLatitude() + "," + gpsFactory.getLongitude(): "0,0");
		metadata.setWhat(keyword);
		metadata.setDescription(descri);
		
		return metadata ;
	}
	
	private boolean buildFileMetedata(){
		
		task			= null ;
		
		String title 	= file_title.getText().toString().trim();
		String keyword	= file_keyword.getText().toString().trim();
		String descri 	= file_description.getText().toString().trim();
		String name 	= file_name.getText().toString().trim();
		String catalogId= null ;
		
		// title is null
		if (title.equals("")) {
			showHintDialog(getString(R.string.ugc_warring_title));
			return false;
		}
		
		String result = Util.checkInput(title);
		if(result != null){
			showHintDialog(getString(R.string.warnning_login_input_illegal));
			return false;
		}
		
		//
		if(name.equals("")){
			showHintDialog(getString(R.string.ugc_warring_path));
			return false;
		}
		
		// 检测子分类
		if(subCatalogId != null && !subCatalogId.equals("")){
			catalogId	= subCatalogId ;
		}
		
		// 如果没有选择二级分类,就设置一级分类
		if(catalogId == null && (parentCatalogId != null && !parentCatalogId.equals(""))){
			catalogId	= parentCatalogId ;
		}
		
		// 判断分类id
		if(catalogId == null){
			showHintDialog(getString(R.string.ugc_warring_category));
			return false ;
		}
		
		String tuid	= UUID.randomUUID().toString() ;
		task 	= createUploadTask(catalog, fileName, filePath);
		task.setTransferUID(tuid);
		Metadata metadata = new Metadata();
		metadata.setSiteId(UGCWebService.getSiteId());
		metadata.setTransferUID(tuid);
		metadata.setTitle(title); // title
		metadata.setDescription(descri); // description
		metadata.setWhat(keyword);// keyword
		metadata.setGps("0,0");
		metadata.setWho("");
		metadata.setPlace("");
		metadata.setCatalogId(catalogId);// 分类id
		metadata.setTransferUID(tuid);
		task.setMetadata(metadata);
		
		return true ;
	}
	
	private boolean buildRecorderMetedata(){
		
		task			= null ;
		
		String title 	= recorder_title.getText().toString().trim();
		String keyword	= recorder_keyword.getText().toString().trim();
		String descri 	= recorder_description.getText().toString().trim();
		String place 	= recorder_location.getText().toString().trim();
		String vfname	= recorder_video_name.getText().toString().trim();
		
		if (title.equals("")) {
			showHintDialog(getString(R.string.ugc_warring_title));
			return false;
		} 
		
		String result = Util.checkInput(title);
		if(result != null){
			showHintDialog(getString(R.string.warnning_login_input_illegal));
			return false;
		}
		
		if(vfname.equals("")){
			showHintDialog(getString(R.string.ugc_warring_recorder_path));
			return false;
		}
		
		//
		String tuid	= UUID.randomUUID().toString() ;
		task = createUploadTask(catalog, fileName, filePath);
		task.setTransferUID(tuid);
		Metadata metadata = new Metadata();
		metadata.setSiteId(UGCWebService.getSiteId());
		metadata.setTitle(title);// title
		metadata.setPlace(place);// where
		metadata.setDescription(descri);// description
		metadata.setWhat(keyword); // keyword
		metadata.setGps(gpsFactory != null ? gpsFactory.getLatitude() + "," + gpsFactory.getLongitude(): "0,0");
		metadata.setWho(vfname);
		metadata.setTransferUID(tuid);
		task.setMetadata(metadata);
		
		return true ;
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		
		switch(requestCode){
		case IMAGE_REQUESTCODE :
			if (resultCode == UGCSelectImageActivity.IMAGE_RESULTCODE) {
				
				filePath = data.getStringExtra(UGCSelectImageActivity.KEY_PATH);
				fileName = data.getStringExtra(UGCSelectImageActivity.KEY_NAME);
				catalog  = requestCode ;
				file_name.setText(fileName);
			}
//			if(data == null){
//				return ;
//			}
//			if (resultCode == RESULT_OK) {
//				Cursor cursor = null;
//				try {
//					Uri uri = data.getData();
//					cursor = getContentResolver().query(uri, null, null, null, null);
//					
//					if (cursor != null && cursor.getCount() > 0) {
//						cursor.moveToFirst();
//						
//						filePath 	= cursor.getString(cursor.getColumnIndexOrThrow(MediaColumns.DATA));
//						fileName 	= cursor.getString(cursor.getColumnIndexOrThrow(MediaColumns.DISPLAY_NAME));
//						
//						catalog  = requestCode ;
//						
//						// 视频裁剪
//						/*if(mime.equals(MIME_MP4)){
//							
//							Intent intent = new Intent() ;
//							intent.setClass(getApplicationContext(), UGCVideoClipActivity.class);
//							intent.putExtra(UGCVideoClipActivity.KEY_FILE, filePath);
//							startActivityForResult(intent, VIDEO_CUT_REQUESTCODE);
//						} else {
//							
//							if(mime.equals(MIME_3GP)){
//								Toast.makeText(this, "不支持3GP文件的裁剪!", Toast.LENGTH_LONG).show();
//							}
//							
//						}*/
//						//
//						if(add_flag == ADD_FLAG_FILE)
//							file_name.setText(fileName);     
//						else if(add_flag == ADD_FLAG_RECORDER)
//							recorder_video_name.setText(fileName);
//					}
//				} finally {
//					if (cursor != null)
//						cursor.close();
//					cursor = null;
//				}
//			}
			break ;
		case VIDEO_REQUESTCODE :
			if (resultCode == UGCSelectVideoActivity.VIDEO_RESULTCODE) {
				
				catalog  = requestCode ;
				
				String mime = data.getStringExtra(UGCSelectVideoActivity.KEY_MIME);
				if(mime.equals(MIME_MP4)){
					String path = data.getStringExtra(UGCSelectVideoActivity.KEY_PATH);
					Intent intent = new Intent() ;
					intent.setClass(getApplicationContext(), UGCVideoClipActivity.class);
					intent.putExtra(UGCVideoClipActivity.KEY_FILE, path);
					startActivityForResult(intent, VIDEO_CUT_REQUESTCODE);
				} else {
					
					if(mime.equals(MIME_3GP)){
						Toast.makeText(this, "不支持3GP文件的裁剪!", Toast.LENGTH_LONG).show();
					}
					
					filePath = data.getStringExtra(UGCSelectVideoActivity.KEY_PATH);
					fileName = data.getStringExtra(UGCSelectVideoActivity.KEY_NAME);
					file_name.setText(fileName);
				}
			}
			break ;
		case AUDIO_REQUESTCODE :
			if (resultCode == AUDIO_RESULTCODE) {
				
				filePath = data.getStringExtra(UGCSelectAudioActivity.KEY_PATH);
				fileName = data.getStringExtra(UGCSelectAudioActivity.KEY_NAME);
				catalog  = requestCode ;
				file_name.setText(fileName);
			}
			break ;
		case LEVEL_ONE_REQUESTCODE :
			
			if(data != null){
				Category root	= (Category)data.getSerializableExtra(UGCChoiceCatalogActivity.KEY_RESULT);
				
				if(root != null){
					
					if(add_flag == ADD_FLAG_FILE){
						file_first_level.setText(root.getCatalogName());
					}
					parentCatalogId = root.getCatalogId() ;
					// 清空
					if(rootCategory != null && !rootCategory.getCatalogId().equals(root.getCatalogId())){
						file_sencond_level.setText("");
					}
					//
					rootCategory = root ;
				}
			}
			
			break ;
		case LEVEL_SENCOND_REQUESTCODE :
			
			if(data != null){
				Category sub = (Category)data.getSerializableExtra(UGCChoiceCatalogActivity.KEY_RESULT);
				
				if(sub != null){
					file_sencond_level.setText(sub.getCatalogName());
					subCatalogId = sub.getCatalogId() ;
				}
				
				subCategory	= sub ;
			}
			break ;
		case IMAGE_CAPTURE :
			if(icon_file != null){
				filePath = icon_file.getAbsolutePath() ;
				fileName = icon_file.getName();
				catalog  = requestCode ;
				file_name.setText(fileName);
			}
			break ;
		case REQUEST_CODE_VIDEO_RECORD :
			// 录制文件不需要打点
			if(data != null){
				filePath = data.getStringExtra(UGCRecordActivity.VIDEO_PATH);
				fileName = data.getStringExtra(UGCRecordActivity.VIDEO_NAME);
				
				recorder_video_name.setText(fileName);
				catalog  = requestCode ;
			} else {
				recorder_video_name.setText("");
			}
			break ;
		case VIDEO_CUT_REQUESTCODE :
			
			if(data != null){
				
				filePath = data.getStringExtra(UGCVideoClipActivity.KEY_FILE);
				fileName = data.getStringExtra(UGCVideoClipActivity.KEY_NAME);
				
				file_name.setText(fileName);
				catalog  = requestCode ;
			}
			break ;
		}
	}
		
	private final void dismissPopupMenu(){
		if(popupMenu != null && popupMenu.isShowing()){
			popupMenu.setAnimationStyle(R.anim.from_bottom_out);
			popupMenu.dismiss() ;
		}
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		return false ;
	}
	
	protected Task createUploadTask(int category ,String name,String path){
		
		int pos 		= name.lastIndexOf('.');
		String realName = "" ;
		
		if(pos > 0){
			realName	= name.substring(0, pos);
		} else {
			realName	= name ;
		}
		
		Task task = new Task() ;
		task.setTaskName(realName);
		task.setTaskPath(path);
		task.setCategory(Task.CATEGORY.UPLOAD);
		task.setState(Task.STATE.NEW);
		task.setAuthor(CWUploadWebService.UserProxy.getUser().getUsername());
		if(category == VIDEO_REQUESTCODE 
				|| category == REQUEST_CODE_VIDEO_RECORD
				|| category == VIDEO_CUT_REQUESTCODE){
			task.setType(Task.TYPE.VIDEO);
		} else if(category == IMAGE_REQUESTCODE || category == IMAGE_CAPTURE){
			task.setType(Task.TYPE.IMAGE);
		} else {
			task.setType(Task.TYPE.AUDIO);
		}
		return task ;
	}
	
	final View.OnClickListener popup_menu_listenr = new View.OnClickListener() {
		
		@Override
		public void onClick(View v) {
			final int id = v.getId() ;
			
			// 视频
			if(id == R.id.bnt_popup_video){
				Intent audio_intent = new Intent(UGCAddUploadActivity.this,UGCSelectVideoActivity.class);
				startActivityForResult(audio_intent,VIDEO_REQUESTCODE);
			} 
			// 音频
			else if(id == R.id.bnt_popup_audio){
				Intent audio_intent = new Intent(UGCAddUploadActivity.this,UGCSelectAudioActivity.class);
				startActivityForResult(audio_intent,AUDIO_REQUESTCODE);
			}
			// 图片
			else if(id == R.id.bnt_popup_image){
				Intent audio_intent = new Intent(UGCAddUploadActivity.this,UGCSelectImageActivity.class);
				startActivityForResult(audio_intent,IMAGE_REQUESTCODE);
			} 
			// 照相
			else if(id == R.id.bnt_popup_camera){
				Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
				File IconPath = new File(TEMP_PATH);
				if (!IconPath.exists()) {
					IconPath.mkdirs();
				}
				icon_file = new File(IconPath, "ioffer_temp.jpg");
				intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(icon_file));
				startActivityForResult(intent, IMAGE_CAPTURE);
			} 
			//
			else if(id == R.id.bnt_popup_cancel){
				
			}
			dismissPopupMenu();
		}
	};	
	
	// 
	final DialogInterface.OnClickListener catalogDialogListener = new DialogInterface.OnClickListener() {
		
		@Override
		public void onClick(DialogInterface dialog, int which) {
			if(which == DialogInterface.BUTTON_POSITIVE){
				querySiteCategory() ;
			}
		}
	} ;
}
