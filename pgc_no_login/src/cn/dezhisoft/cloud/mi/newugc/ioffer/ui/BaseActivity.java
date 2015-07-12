package cn.dezhisoft.cloud.mi.newugc.ioffer.ui;

import android.app.Activity;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import cn.dezhisoft.cloud.mi.newugc.R;

import com.sobey.android.ui.ActivityManager;
import cn.dezhisoft.cloud.mi.newugc.ioffer.common.AccessDatabase;
import cn.dezhisoft.cloud.mi.newugc.ioffer.common.Config;
import cn.dezhisoft.cloud.mi.newugc.ioffer.config.AppConfig;
import cn.dezhisoft.cloud.mi.newugc.ioffer.exception.ExceptionHandler;
import cn.dezhisoft.cloud.mi.newugc.ioffer.service.IofferService;

/**
 * 
 * @author Rosson Chen
 * 
 */
public abstract class BaseActivity extends Activity {
	
	/** 业务逻辑模块 */
	protected static final IofferService mIofferService = IofferService.getNewTipService();
	/** 数据库访问 */
	protected static AccessDatabase mAccessDatabase ;

	protected static final int 	DIALOG_ID_REQUEST	= 0x00fff0 ;
	protected final Context mContext	= this ;
	protected Handler mHandler;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		Thread.setDefaultUncaughtExceptionHandler(new ExceptionHandler(this));
		
		mHandler = getMessageHandler();
		
		if (mAccessDatabase == null) {
			mAccessDatabase = Config.getAccessDatabase(this) ;
		}
	}

	protected Handler getMessageHandler() {
		return new Handler();
	}
	
	protected final boolean checkSiteId(String siteid){
		return siteid != null && !siteid.equals("-1") ;
	}

	protected final void showMessageDialog(String message){
		showMessageDialog(message,null);
	}
	
	protected final void showMessageDialog(String message,OnClickListener listener){
		showMessageDialog(getString(R.string.warring_hint_title),message,listener);
	}
	
	protected final void showMessageDialog(String title,String message,OnClickListener listener){
		new Builder(this)
		.setTitle(title)
		.setMessage(message)
		.setPositiveButton(R.string.warring_hint_bnt_yes,listener)
		.create()
		.show();
	}
	
	protected final ActivityManager getActivityManager(){
		return ActivityManager.getActivityManager();
	}
	
	@Override
	protected Dialog onCreateDialog(int id) {
		switch(id){
		case DIALOG_ID_REQUEST :
			return ProgressDialog.show(this, "", getString(R.string.dialog_request_service), false, false);
		}
		return super.onCreateDialog(id);
	}
	
	protected final void switchHomeActivity(){
		Intent intent = new Intent();
		intent.setClass(mContext, IofferActivityManager.class);
		startActivity(intent);
		finish();
	}
	
	protected final void userLogin(final String uname, final String pwd) {
		
		// 默认地址
		if(!loadHostSetting()){
			Log.e("userLogin","ERROR: Please go to setting") ;
			return ;
		}
		// 登录
		mIofferService.login(uname, pwd, new cn.dezhisoft.cloud.mi.newugc.ugc.core.ws.CWSoapResponse(mHandler));
	}
	
	/** 初始化设置地址 */
	protected final boolean loadHostSetting(){
		
		AppConfig config = mAccessDatabase.getConfig();
		String siteId	 = config.getSiteId() ;
		String webHost = config != null ? config.getHost() : "" ;
		//
		if(webHost.equals("")){
			showMessageDialog(getString(R.string.warring_hint_web_setting));
			return false;
		}
		// debug
		Log.d("loadHostSetting", "host=" + webHost + " siteId=" + siteId) ;
		// 设置站点
		mIofferService.setSiteId(siteId);
		// 设置web地址
		mIofferService.setUGCHost(webHost);
		
		return true;
	}
}
