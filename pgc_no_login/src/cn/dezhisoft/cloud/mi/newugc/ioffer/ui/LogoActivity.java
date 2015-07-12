package cn.dezhisoft.cloud.mi.newugc.ioffer.ui;

import java.util.ArrayList;

import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;

import cn.dezhisoft.cloud.mi.newugc.R;

import cn.dezhisoft.cloud.mi.newugc.ioffer.common.Config;
import cn.dezhisoft.cloud.mi.newugc.ioffer.config.AppConfig;
import cn.dezhisoft.cloud.mi.newugc.ioffer.service.IofferDefine;
import cn.dezhisoft.cloud.mi.newugc.ioffer.upload.ui.UGCSettingPreference;
import cn.dezhisoft.cloud.mi.newugc.ugv2.activity.LoginActivity;
import com.sobey.sdk.encoder.AbstractEncoder.EncoderType;
import cn.dezhisoft.cloud.mi.newugc.ugc.version.UpdateInfo;
import cn.dezhisoft.cloud.mi.newugc.ugc.version.VersionManager;
import cn.dezhisoft.cloud.mi.newugc.ugc.ws.SoapResponse;

/**
 * 
 * @author Rosson Chen
 * 
 */
public class LogoActivity extends BaseActivity {

	private final static int LOGO_TIME = 3000;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ioffer_logo_layout);
		//
		Config.initContext(getApplicationContext());
	}
	
	@Override
	protected Handler getMessageHandler() {
		return new Handler() {

			@Override
			public void handleMessage(Message msg) {
				switch (msg.what) {
				case SoapResponse.SoapRequestMessage.MSG_START_SOAP :
					showDialog(DIALOG_ID_REQUEST);
					break ;
				case SoapResponse.SoapRequestMessage.MSG_END_SOAP :
					dismissDialog(DIALOG_ID_REQUEST);
					break ;
				case IofferDefine.MSG_USER_LOGIN_SUCCESS:
					switchHomeActivity();
					break;
				case IofferDefine.MSG_USER_LOGIN_FAILED:
					switchLoginActivity();
					break;
				}
			}
		};
	}
	
	/** 分析编码器类型 */
	private boolean findEncoderType(){
		SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(this);
		int encoder = settings.getInt(UGCSettingPreference.KEY_ENCODER, -1);
		
		return encoder == EncoderType.ENCODER_OMX 
				|| encoder == EncoderType.ENCODER_SOFT 
				|| encoder == EncoderType.ENCODER_STREAM ;
	}

	@Override
	protected void onStart() {
		super.onStart();
		
		// 查找编码器设置
		boolean find = findEncoderType() ;
		
		if(!find){
			Intent intent = new Intent() ;
			intent.setClass(this, LiveCheckActivity.class);
			startActivity(intent);
		} else {
			
			final PackageInfo pack = VersionManager.getVersionName(this) ;
			
			if(pack != null){
				// 查询本地所有版本信息
				ArrayList<Object> temp = mAccessDatabase.queryAllObject(UpdateInfo.class);
				if(temp.isEmpty()){ 
					goToHome(LOGO_TIME);
					return ;
				}
				// 查询新版信息
				ArrayList<Object> list = mAccessDatabase.queryAllObject(UpdateInfo.class, new String[]{"versionStatus","versionName"}, new String[]{String.valueOf(UpdateInfo.Status.STATUS_NEW),pack.versionName});
				//
				if(list.isEmpty()){
					goToHome(LOGO_TIME);
					return ;
				}
				//
				final UpdateInfo info = (UpdateInfo)list.get(0);
				// 拼接字符串
				final StringBuffer des = new StringBuffer() ;
				try{
					float lv = Float.valueOf(info.getLastVersion());
					for(Object obj : temp){
						UpdateInfo old = (UpdateInfo)obj ;
						float f = Float.valueOf(old.getVersionName());
						if(f >= lv){
							des.append(old.getVersionDescription());
							des.append("\n");
						}
					}
				} catch(Exception e){
					e.printStackTrace() ;
					goToHome(LOGO_TIME);
					return ;
				}
				//
				new Builder(this)
				.setTitle(R.string.label_new_version)
				.setMessage(des.toString())
				.setPositiveButton(R.string.warring_hint_bnt_yes,new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						info.setVersionStatus(UpdateInfo.Status.STATUS_READED);
						mAccessDatabase.updateObject(info);
						goToHome(1000);
					}
				})
				.setCancelable(false)
				.create()
				.show();
			} else {
				goToHome(LOGO_TIME);
			}
		}
	}
	
	private void goToHome(int delayMillis){
		
		mHandler.postDelayed(new Runnable() {
			
			@Override
			public void run() {
				
				AppConfig config 	= mAccessDatabase.getConfig() ;
				String siteId 		= config.getSiteId() ;
				
				// 是否有效站点
				if (!checkSiteId(siteId)) {
					switchLoginActivity();
					return ;
				}
				// 是否自动登录
				if(config.getAuto() == Config.FLGA_BUTTON_ON){
					userLogin(config.getName(), config.getPassword());
				} else {
					switchLoginActivity();
				}
			}
		}, delayMillis);
	}

	private final void switchLoginActivity() {
		Intent intent = new Intent();
		intent.setClass(this, LoginActivity.class);
		startActivity(intent);
		finish();
	}
}
