package cn.dezhisoft.cloud.mi.newugc.ioffer.ui;

import android.app.AlertDialog.Builder;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import cn.dezhisoft.cloud.mi.newugc.R;

import com.sobey.android.ui.ActivityManager;
import com.sobey.android.ui.UIConfig;
import cn.dezhisoft.cloud.mi.newugc.ioffer.common.AccessDatabase;
import cn.dezhisoft.cloud.mi.newugc.ioffer.common.Config;
import cn.dezhisoft.cloud.mi.newugc.ioffer.config.AppConfig;
import cn.dezhisoft.cloud.mi.newugc.ioffer.service.IofferDefine;
import cn.dezhisoft.cloud.mi.newugc.ioffer.service.IofferService;
import cn.dezhisoft.cloud.mi.newugc.ugv2.activity.LoginActivity;
import cn.dezhisoft.cloud.mi.newugc.ugc.core.down.UGCDownFileService;
import cn.dezhisoft.cloud.mi.newugc.ugc.core.upload.UGCUploadFileService;
import cn.dezhisoft.cloud.mi.newugc.ugc.version.VersionListener;
import cn.dezhisoft.cloud.mi.newugc.ugc.version.VersionManager;
import cn.dezhisoft.cloud.mi.newugc.ugc.ws.SoapResponse;

/**
 * 
 * @author Rosson Chen
 *
 */
public final class IofferActivityManager extends ActivityManager {

	private long firstTime = System.currentTimeMillis();
	
	private static final int MENU_CHANGE_USER	= 1 ;
	private static final int MENU_CHECK_VERISON	= MENU_CHANGE_USER + 1 ;
	private static final int MENU_EXT			= MENU_CHECK_VERISON + 1 ;
	
	private ProgressDialog progressDialog ;
	
	@Override
	public UIConfig loadUIConfig() {
		return new IofferUIConfig();
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		//
		Intent service = new Intent() ;
		service.setClass(getApplicationContext(), UGCUploadFileService.class);
		startService(service);
		
		service = new Intent() ;
		service.setClass(getApplicationContext(), UGCDownFileService.class);
		startService(service);
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
					Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
				}
			});
		}
	}
	
	@Override
	protected void onStop() {
		super.onStop();
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
	public boolean onCreateOptionsMenu(Menu menu) { 
	    menu.add(0, MENU_CHANGE_USER, 0, R.string.menu_change_user); 
	    //menu.add(0, MENU_CHECK_VERISON, 0, R.string.menu_check_version); 
	    menu.add(0, MENU_EXT, 0, R.string.menu_exit); 
	    return true; 
	} 

	@Override
	public boolean onOptionsItemSelected(MenuItem item) { 
	    switch (item.getItemId()) { 
	    case MENU_CHANGE_USER: 
	    	final IofferService mIofferService = IofferService.getNewTipService();
	    	mIofferService.logout(new cn.dezhisoft.cloud.mi.newugc.ugc.core.ws.CWSoapResponse(handler)) ;
	        return true; 
	    case MENU_EXT: 
	    	showMessageDialog(getString(R.string.label_exit_application), new OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					executeExit() ;
				}
			});
	    	return true ; 
	    case MENU_CHECK_VERISON :
	    	
	    	return true ;
	    } 
	    	
	    return false; 
	} 
	
	@Override
	protected boolean quitApplication() {
		
		if(System.currentTimeMillis() - firstTime > 1500){
			Toast.makeText(this, R.string.quit_application, Toast.LENGTH_SHORT).show() ;
			firstTime	= System.currentTimeMillis() ;
			return false ;
		}
		
		//
		return executeExit();
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		
		stopAllService();
	}

	private void stopAllService(){
		// 停止上传服务
		Intent service = new Intent();
		service.setClass(getApplicationContext(), UGCUploadFileService.class);
		stopService(service);
		// 停止下载服务
		service = new Intent();
		service.setClass(getApplicationContext(), UGCDownFileService.class);
		stopService(service);
	}
	
	private boolean executeExit(){
		
		stopAllService();

		// 清除临时数据
		Config.getSharedPreferences(this).edit().clear();

		config.onDestroy();

		IofferService.getNewTipService().reset();

		System.exit(0);
		// finish() ;
		//
		return true;
	}
	
	protected final void showMessageDialog(String message){
		new Builder(this).setTitle(R.string.warring_hint_title)
		.setMessage(message)
		.setPositiveButton(R.string.warring_hint_bnt_yes,null)
		.create()
		.show();
	}
	
	protected final void showMessageDialog(String message,OnClickListener listener){
		new Builder(this).setTitle(R.string.warring_hint_title)
		.setMessage(message)
		.setPositiveButton(R.string.warring_hint_bnt_yes,listener)
		.setNegativeButton(R.string.warring_hint_bnt_no,null)
		.create()
		.show();
	}
	
	private final Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case SoapResponse.SoapRequestMessage.MSG_START_SOAP:
				if(progressDialog == null) {
					progressDialog = ProgressDialog.show(IofferActivityManager.this, "", getString(R.string.dialog_request_service), false, false);
				} else {
					progressDialog.show() ;
				}
				break;
			case SoapResponse.SoapRequestMessage.MSG_END_SOAP:
				if(progressDialog != null){
					progressDialog.dismiss() ;
				}
				break;
			case IofferDefine.MSG_USER_LOGOUT_SUCCESS:

				final AccessDatabase accessDatabase = AccessDatabase.getAccessDatabase() ;
				accessDatabase.updateAutoLoginUser(true, "", "");

				Intent intent = new Intent(IofferActivityManager.this,LoginActivity.class);
				startActivity(intent);
				
				IofferActivityManager.this.finish() ;
				break;
			case IofferDefine.MSG_USER_LOGOUT_FAILED:
				showMessageDialog(getString(R.string.label_user_logout_failed));
				break;
			}
		}
	};

}
