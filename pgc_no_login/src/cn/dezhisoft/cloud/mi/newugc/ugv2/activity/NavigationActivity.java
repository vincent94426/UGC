package cn.dezhisoft.cloud.mi.newugc.ugv2.activity;

import java.io.File;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences.Editor;
import android.content.res.Configuration;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.animation.LayoutAnimationController;
import android.widget.Toast;
import cn.dezhisoft.cloud.mi.newugc.R;
import cn.dezhisoft.cloud.mi.newugc.common.util.DebugUtil;
import cn.dezhisoft.cloud.mi.newugc.common.util.OperationEnableTimer;
import cn.dezhisoft.cloud.mi.newugc.common.util.SettingUtil;
import cn.dezhisoft.cloud.mi.newugc.common.util.ThreadUtil;
import cn.dezhisoft.cloud.mi.newugc.ioffer.common.AccessDatabase;
import cn.dezhisoft.cloud.mi.newugc.ioffer.common.Config;
import cn.dezhisoft.cloud.mi.newugc.ioffer.config.AppConfig;
import cn.dezhisoft.cloud.mi.newugc.ioffer.service.IofferService;
import cn.dezhisoft.cloud.mi.newugc.ugc.core.upload.UGCUploadFileService;
import cn.dezhisoft.cloud.mi.newugc.ugc.version.VersionListener;
import cn.dezhisoft.cloud.mi.newugc.ugc.version.VersionManager;
import cn.dezhisoft.cloud.mi.newugc.ugv2.base.activity.BaseActivity;
import cn.dezhisoft.cloud.mi.newugc.ugv2.chat.common.tran.bean.TranObject;
import cn.dezhisoft.cloud.mi.newugc.ugv2.chat.common.util.Constants;
import cn.dezhisoft.cloud.mi.newugc.ugv2.constants.UgV2Constants;
import cn.dezhisoft.cloud.mi.newugc.ugv2.view.metro.view.AutoFillMetroView;
import cn.dezhisoft.cloud.mi.newugc.ugv2.view.metro.view.MetroItemView;
import cn.dezhisoft.cloud.mi.newugc.ugv2.view.metro.view.MetroView;
import cn.dezhisoft.cloud.mi.newugc.ugv2.view.metro.view.MetroView.OrientationType;

public class NavigationActivity extends BaseActivity{
	private static final int TWO_SPAN = 2;
	private static final int ONE_SPAN = 1;
	
	private View mContent;
	private LayoutInflater inflater;
	
	private AutoFillMetroView mMetro;
	
	@Override
	protected View onCreateBody() {
		this.inflater = this.getLayoutInflater();
		mContent = inflater.inflate(R.layout.ugv2_navigation_layout, null);
		initMetroLayout();
		
		setUiTitle(R.string.ugv2_txt_title_navigation);
		
		DebugUtil.traceLog("NavigationActivity oncreate");
		
		return mContent;
	}

	private void initMetroLayout() {
		initViews();
		
		//runAnimation(mMetro, new LayoutAnimationController(Rotate3dAnimation.GetFlyInAnimation()));
	}
	
	/** 意外停止后恢复数据 */
	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) {
		
		final IofferService mIofferService = IofferService.getNewTipService();
		mIofferService.reset() ;
		
		super.onRestoreInstanceState(savedInstanceState);
	}
	
	/** 意外停止,保存数据 */
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		
		// 保存临时数据
		final AccessDatabase db = Config.getAccessDatabase(this);
		final AppConfig config 	= db.getConfig() ;
		final Editor editor 	= Config.getSharedPreferences(this).edit(); 
		
		editor
		.putString(Config.KEY_HOST, config.getHost())
		.putString(Config.KEY_SITE, config.getSiteId())
		.putString(Config.KEY_SESSION, IofferService.getNewTipService().getUser().getSessionUID())
		.commit() ;
		
		super.onSaveInstanceState(outState);
	}
	
	@Override
	public void onResume() {
		super.onResume();
		topBar.setRightBtnVisibility(View.GONE);
		
		DebugUtil.traceLog("NavigationActivity onResume");
		
		Intent service = new Intent() ;
		service.setClass(getApplicationContext(), UGCUploadFileService.class);
		startService(service);
		
		/*service = new Intent() ;
		service.setClass(getApplicationContext(), UGCDownFileService.class);
		startService(service);*/
		//
		AppConfig config = Config.getAccessDatabase(this).getConfig();
		// 是否需要检查版本
		if(config.getCheckVersion() == Config.FLGA_BUTTON_ON){
			
			VersionManager.checkVersion(config.getHost(), config.getSiteId(),this, new VersionListener() {
				
				@Override
				public void notUpdate() {
					//Toast.makeText(getApplicationContext(), R.string.label_not_update_version, Toast.LENGTH_LONG).show();
				}

				@Override
				public void onError(String message) {
					//Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
				}
			});
		}
	}
	
	
	@Override
	protected void onDestroy() {
		executeExit();

		DebugUtil.traceLog("NavigationActivity onDestroy");
		super.onDestroy();
	}

	private void stopAllService(){
		// 停止上传服务
		Intent service = new Intent();
		service.setClass(getApplicationContext(), UGCUploadFileService.class);
		stopService(service);
		// 停止下载服务
		/*service = new Intent();
		service.setClass(getApplicationContext(), UGCDownFileService.class);
		stopService(service);*/
	}
	
	private boolean executeExit(){
		stopAllService();

		// 清除临时数据
		Config.getSharedPreferences(this).edit().clear();

		IofferService.getNewTipService().reset();

		System.exit(0);
		// finish() ;
		//
		return true;
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		setRowsColumns(newConfig);
		super.onConfigurationChanged(newConfig);
	}

	public void runAnimation(ViewGroup v,LayoutAnimationController localLayoutAnimationController){
		localLayoutAnimationController.setOrder(2);
		localLayoutAnimationController.setDelay(0.3F);
		v.setLayoutAnimation(localLayoutAnimationController);
	}
	
	private void setRowsColumns(Configuration config) {
		
		mMetro = (AutoFillMetroView)mContent.findViewById(R.id.metro_ui);
		if (config.orientation == Configuration.ORIENTATION_LANDSCAPE)
			mMetro.setVisibleItems(2, 3);
		else
			mMetro.setVisibleItems(6, 2);
	}
	
	private void initViews(){
		mMetro = (AutoFillMetroView)mContent.findViewById(R.id.metro_ui);
		
		mMetro.setOrientation(OrientationType.Horizontal);
		setRowsColumns(getResources().getConfiguration());
		//Resources resources = getResources();
		mMetro.addAutoFillMetroItem(new MetroItemView(inflater, TWO_SPAN, ONE_SPAN, null, 
				R.drawable.ugv2_navigation_video, R.string.ugv2_module_text_video, 0, mOnMetroItemTouchListener));
		mMetro.addAutoFillMetroItem(new MetroItemView(inflater, ONE_SPAN, ONE_SPAN, null, 
				R.drawable.ugv2_navigation_image, R.string.ugv2_module_text_image, 0, mOnMetroItemTouchListener));
		mMetro.addAutoFillMetroItem(new MetroItemView(inflater, ONE_SPAN, ONE_SPAN, null, 
				R.drawable.ugv2_navigation_audio, R.string.ugv2_module_text_audio, 0, mOnMetroItemTouchListener));
		
		mMetro.addAutoFillMetroItem(new MetroItemView(inflater, TWO_SPAN, ONE_SPAN, null, 
				R.drawable.ugv2_navigation_talk, R.string.ugv2_module_text_audio, 0, mOnMetroItemTouchListener));
		
		mMetro.addAutoFillMetroItem(new MetroItemView(inflater, TWO_SPAN, ONE_SPAN, null, 
				R.drawable.ugv2_navigation_takephoto, R.string.ugv2_module_text_camera_image, 0, mOnMetroItemTouchListener));
		
		mMetro.addAutoFillMetroItem(new MetroItemView(inflater, TWO_SPAN, ONE_SPAN, null, 
				R.drawable.ugv2_navigation_record_video, R.string.ugv2_module_text_camera_video, 0, mOnMetroItemTouchListener));
		mMetro.addAutoFillMetroItem(new MetroItemView(inflater, ONE_SPAN, ONE_SPAN, null, 
				R.drawable.ugv2_navigation_task, R.string.ugv2_module_text_task, 0, mOnMetroItemTouchListener));
		mMetro.addAutoFillMetroItem(new MetroItemView(inflater, ONE_SPAN, ONE_SPAN, null, 
				R.drawable.ugv2_navigation_setting, R.string.ugv2_module_text_setting, 0, mOnMetroItemTouchListener));
	}
	
	public MetroView getMetroView(){
		return mMetro;
	}
	
	private OnTouchListener mOnMetroItemTouchListener = new OnTouchListener() {
		
		@Override
		public boolean onTouch(View v, MotionEvent event) {
			if(OperationEnableTimer.isEnable()){
				OperationEnableTimer.disableOperation(3000);
				
				switch (event.getAction()) {
				case MotionEvent.ACTION_DOWN:
					if(R.drawable.ugv2_navigation_video == v.getId()){
						chooseVideo();
					}else if(R.drawable.ugv2_navigation_image == v.getId()){
						chooseImage();
					}else if(R.drawable.ugv2_navigation_audio == v.getId()){
						chooseAudio();
					}else if(R.drawable.ugv2_navigation_task == v.getId()){
						Intent intent = new Intent(mContext, TaskActivity.class);
						startActivity(intent);
					}else if(R.drawable.ugv2_navigation_setting == v.getId()){
						Intent intent = new Intent(mContext, SettingActivity.class);
						startActivity(intent);
					}else if(R.drawable.ugv2_navigation_takephoto == v.getId()){
						captureImage();
					}else if(R.drawable.ugv2_navigation_record_video == v.getId()){
						captureVideo();
					}else if(R.drawable.ugv2_navigation_talk == v.getId()){
						TranObject msg = (TranObject) getIntent().getSerializableExtra(Constants.MSGKEY);
						Intent intent = new Intent(NavigationActivity.this, FriendListActivity.class);
						intent.putExtra(Constants.MSGKEY, msg);
						startActivity(intent);
					}
					break;
				default:
					break;
				}
			}
			return true;
		}
	};

	private void chooseImage(){
		String selectTitle = "选择图片";
		String selectContentType = UgV2Constants.CONTENT_TYPE_IMAGE;
		startChooserActivity(selectTitle, selectContentType, UgV2Constants.REQ_CODE_CHOOSER_IMAGE);
	}
	
	private void chooseVideo(){
		String selectTitle = "选择视频";
		String selectContentType = UgV2Constants.CONTENT_TYPE_VIDEO;
		startChooserActivity(selectTitle, selectContentType, UgV2Constants.REQ_CODE_CHOOSER_VIDEO);
	}
	
	private void chooseAudio(){
		String selectTitle = "选择音频";
		String selectContentType = UgV2Constants.CONTENT_TYPE_AUDIO;
		startChooserActivity(selectTitle, selectContentType, UgV2Constants.REQ_CODE_CHOOSER_AUDIO);
	}

	private void startChooserActivity(String selectTitle,
			String selectContentType, int requestCode) {
		Intent intent = new Intent();
		intent.setType(selectContentType);
		intent.setAction(Intent.ACTION_GET_CONTENT);
		startActivityForResult(Intent.createChooser(intent,selectTitle), requestCode);
	}
	
	private void startViewActivity(final String path) {
		if(isVideoFile(path)){
			ThreadUtil.runOnUiThread(new Runnable() {
				
				@Override
				public void run() {
					Intent intent = new Intent(Intent.ACTION_VIEW);
					String type = "video/mp4";
					Uri uri = Uri.fromFile(new File(path));
					intent.setDataAndType(uri, type);
					startActivity(intent);
				}
			}, 1000);
		}
	}
	
	private boolean isVideoFile(String path) {
		boolean isVideo = true;
		if(TextUtils.isEmpty(path)
				|| path.endsWith(".jpg")
				|| path.endsWith(".jpeg")
				|| path.endsWith(".png")
				|| path.endsWith(".gif")
				|| path.endsWith(".bmp")
				|| path.endsWith(".tif")){
			isVideo = false;
		}
		return isVideo;
	}

	String lastImgPath;
	private void captureImage(){
		//String selectTitle = "拍照";
		lastImgPath = Environment.getExternalStorageDirectory().getAbsolutePath() 
				+ UgV2Constants.CONTENT_PATH_PHOTO 
				+ System.currentTimeMillis() + ".jpg";

		File vFile = new File(lastImgPath);
		if(vFile.getParentFile().exists() || vFile.getParentFile().mkdirs()){
			Uri uri = Uri.fromFile(vFile);

			Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
			
			intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
			
			startActivityForResult(intent, UgV2Constants.REQ_CODE_IMAGE_CAPTURE);
			SettingUtil.writeString(getApplicationContext(), UgV2Constants.KEY_LAST_CAPTURE_IMAGE_PATH, lastImgPath);
		}
	}
	
	private void captureVideo(){
		Intent intent = new Intent(NavigationActivity.this, CaptureActivity.class);
			
		startActivityForResult(intent, UgV2Constants.REQ_CODE_VIDEO_CAPTURE);
	}
	/*String lastVideoPath;
	private void captureVideo(){
		//String selectTitle = "拍照";
		lastVideoPath = Environment.getExternalStorageDirectory().getAbsolutePath() 
				+ UgV2Constants.CONTENT_PATH_VIDEO 
				+ System.currentTimeMillis() + ".mp4";

		File vFile = new File(lastVideoPath);
		if(vFile.getParentFile().exists() || vFile.getParentFile().mkdirs()){
			Uri uri = Uri.fromFile(vFile);

			Intent intent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
			
			intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
			
			startActivityForResult(intent, UgV2Constants.REQ_CODE_VIDEO_CAPTURE);
		}
	}*/
	
	@TargetApi(19) @SuppressLint("NewApi") @Override
	protected void onActivityResult(int requestCode, int resultCode,
			Intent dataParam) {
		String resultParam = null;
		if (Activity.RESULT_OK == resultCode) {
			if (requestCode == UgV2Constants.REQ_CODE_CHOOSER_IMAGE
					|| requestCode == UgV2Constants.REQ_CODE_CHOOSER_VIDEO
					|| requestCode == UgV2Constants.REQ_CODE_CHOOSER_AUDIO) {//选择
				Uri chooseUri = dataParam.getData();
				DebugUtil.traceLog("onActivityResult chooseUri:" + chooseUri);
				if (null == chooseUri) {
					return;
				}
//				chooseUri = Uri.parse(Uri.decode(dataParam.getData().toString()));
				if (chooseUri.toString().startsWith("file:///")) {
					onChooseMediaCallback(requestCode, chooseUri.toString());
					return;
				}
				if (Build.VERSION.SDK_INT >= 19 && DocumentsContract.isDocumentUri(NavigationActivity.this, chooseUri)) {
					if (isMediaDocument(chooseUri)) {
			            final String docId = DocumentsContract.getDocumentId(chooseUri);
			            final String[] split = docId.split(":");
			            final String type = split[0];

			            Uri contentUri = null;
			            if ("image".equals(type)) {
			                contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
			            } else if ("video".equals(type)) {
			                contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
			            } else if ("audio".equals(type)) {
			                contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
			            }

			            final String selection = "_id=?";
			            final String[] selectionArgs = new String[] {
			                    split[1]
			            };

			            resultParam = getDataColumn(NavigationActivity.this, contentUri, selection, selectionArgs);
			            DebugUtil.traceLog("onActivityResult resultParam_111:" + resultParam);
			            if(!TextUtils.isEmpty(resultParam)){
							onChooseMediaCallback(requestCode, resultParam);
						}
			        }
				} else {
					Cursor chooseCursor = this.getContentResolver().query(
							chooseUri, new String[] { "_data" }, null, null, null);
					if (null != chooseCursor && chooseCursor.moveToFirst()) {
						resultParam = chooseCursor.getString(0);
						DebugUtil.traceLog("onActivityResult resultParam:" + resultParam);
						if(!TextUtils.isEmpty(resultParam)){
							onChooseMediaCallback(requestCode, resultParam);
						}else {
							Cursor c = this.getContentResolver().query(
									chooseUri, null, null, null, null);
							
							if(null != c && c.moveToFirst()){
								int column = c.getColumnCount();
								for(int i=0;i< column; i++){
									int t = c.getType(i);
									if(t == Cursor.FIELD_TYPE_STRING){
										DebugUtil.traceLog("getContentResolver().query column:" + i + ",value:" + c.getString(i));
									}
								}
							}
						}
					}
				}
			}else if(requestCode == UgV2Constants.REQ_CODE_IMAGE_CAPTURE){//data is null
				/*String imagePath = dataParam.getStringExtra(UgV2Constants.KEY_RESULT_PATH);
				if(!TextUtils.isEmpty(imagePath)){
					onChooseMediaCallback(UgV2Constants.REQ_CODE_IMAGE_CAPTURE, lastImgPath);
				}*/
				lastImgPath = SettingUtil.readString(getApplicationContext(), UgV2Constants.KEY_LAST_CAPTURE_IMAGE_PATH, lastImgPath);
				DebugUtil.traceLog("REQ_CODE_IMAGE_CAPTURE:" + lastImgPath);
				onChooseMediaCallback(requestCode, lastImgPath);
			}else if(requestCode == UgV2Constants.REQ_CODE_VIDEO_CAPTURE){
				String videoPath = dataParam.getStringExtra(UgV2Constants.KEY_RESULT_PATH);
				if(!TextUtils.isEmpty(videoPath)){
					onChooseMediaCallback(requestCode, videoPath);
				}
			}
		}
	}
	
	private boolean isMediaDocument(Uri uri) {
	    return "com.android.providers.media.documents".equals(uri.getAuthority());
	}
	
	public static String getDataColumn(Context context, Uri uri, String selection,
	        String[] selectionArgs) {

	    Cursor cursor = null;
	    final String column = "_data";
	    final String[] projection = {
	            column
	    };

	    try {
	        cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs,
	                null);
	        if (cursor != null && cursor.moveToFirst()) {
	            final int index = cursor.getColumnIndexOrThrow(column);
	            return cursor.getString(index);
	        }
	    } finally {
	        if (cursor != null)
	            cursor.close();
	    }
	    return null;
	}
    
	private void onChooseMediaCallback(int requestCode, String resultParam) {
		//inputTitle.setText(resultParam);
		Intent intent = new Intent(mContext, MainActivity.class);
		intent.putExtra(UgV2Constants.KEY_REQ_CODE, requestCode);
		intent.putExtra(UgV2Constants.KEY_REQ_PATH, resultParam);
		
		startActivity(intent);
	}
	
	private long firstTime = 0;
	@Override
	public void onBackPressed() {
		long secondTime = System.currentTimeMillis();
		if (secondTime - firstTime > 3000) {
			Toast.makeText(getApplicationContext(), "再按一次退出程序",
					Toast.LENGTH_SHORT).show();
			firstTime = secondTime;
		} else {
			super.onBackPressed();
		}
	}
}
