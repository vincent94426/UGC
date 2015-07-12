package cn.dezhisoft.cloud.mi.newugc.ioffer.ui;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import cn.dezhisoft.cloud.mi.newugc.R;

import cn.dezhisoft.cloud.mi.newugc.ioffer.common.Config;
import cn.dezhisoft.cloud.mi.newugc.ioffer.service.IofferDefine;
import cn.dezhisoft.cloud.mi.newugc.ioffer.upload.ui.UGCDownListActivity;
import cn.dezhisoft.cloud.mi.newugc.ioffer.upload.ui.UGCUploadListActivity;
import cn.dezhisoft.cloud.mi.newugc.ugv2.activity.LoginActivity;
import cn.dezhisoft.cloud.mi.newugc.ugc.core.down.IFileDownload;
import cn.dezhisoft.cloud.mi.newugc.ugc.core.down.UGCDownFileService;
import cn.dezhisoft.cloud.mi.newugc.ugc.core.upload.IFileUpload;
import cn.dezhisoft.cloud.mi.newugc.ugc.core.upload.UGCUploadFileService;
import cn.dezhisoft.cloud.mi.newugc.ugc.ws.SoapResponse;

/**
 * 
 * @author Rosson Chen
 * 
 */
public class TabUserActivity extends BaseActivity {

	static final String TAG		= "TabUserActivity" ;
	
	ImageView user_icon ;
	
	TextView user_name,user_upload,user_download;
	
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.tab_user_layout);
		
		// by wei.zhou. fix bug
		Config.checkActivityStatus(this);
		
		user_icon		= (ImageView)findViewById(R.id.img_view_user_icon);
		user_name		= (TextView)findViewById(R.id.tv_user_name);
		user_upload		= (TextView)findViewById(R.id.tv_user_upload);
		user_download	= (TextView)findViewById(R.id.tv_user_download);
	}

	@Override
	protected void onResume() {
		super.onResume();
		// UI
		user_name.setText(mIofferService.getUser().getUsername());
		// 下载
		IFileDownload down = UGCDownFileService.getUGCFileDownloadImpl();
		if(down != null)
			user_download.setText(String.valueOf(down.getDownloadSize()));
		// 上传
		IFileUpload upload = UGCUploadFileService.getUGCFileUpload() ;
		if(upload != null)
			user_upload.setText(String.valueOf(upload.getUploadSize()));
	}

	@Override
	protected Handler getMessageHandler() {
		return new Handler(){

			@Override
			public void handleMessage(Message msg) {
				switch(msg.what){
				case SoapResponse.SoapRequestMessage.MSG_START_SOAP :
					showDialog(DIALOG_ID_REQUEST);
					break ;
				case SoapResponse.SoapRequestMessage.MSG_END_SOAP :
					dismissDialog(DIALOG_ID_REQUEST);
					break ;
				case IofferDefine.MSG_USER_LOGOUT_SUCCESS :
					
					mAccessDatabase.updateAutoLoginUser(true,"","");
					
					Intent intent = new Intent(TabUserActivity.this, LoginActivity.class);
		    		startActivity(intent);
		    		getActivityManager().finish();
		    		
		    		mIofferService.reset() ;
		    		
					break ;
				case IofferDefine.MSG_USER_LOGOUT_FAILED :
					showMessageDialog(getString(R.string.label_user_logout_failed));
					break ;
				case IofferDefine.MSG_USER_ENUM_SUCCESS :
					getActivityManager().pushActivity(IofferEditUserActivity.class);
					break ;
				case IofferDefine.MSG_USER_ENUM_FAILED :
					showMessageDialog(getString(R.string.label_user_enum_failed));
					break ;
				}
			}
		};
	}
	
	/**
	 * 
	 * @param v
	 */
	public void buttonOnclick(View v) {
		switch (v.getId()) {
		case R.id.bnt_user_logout:
			mIofferService.logout(new cn.dezhisoft.cloud.mi.newugc.ugc.core.ws.CWSoapResponse(mHandler)) ;
			break;
		case R.id.bnt_user_edituser :
			mIofferService.userEnum(new cn.dezhisoft.cloud.mi.newugc.ugc.core.ws.CWSoapResponse(mHandler));
			break ;
		case R.id.user_upload_layout:
			getActivityManager().pushActivity(UGCUploadListActivity.class);
			break;
		case R.id.user_download_layout:
			getActivityManager().pushActivity(UGCDownListActivity.class);
			break;
		}
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		return false;
	}
}
