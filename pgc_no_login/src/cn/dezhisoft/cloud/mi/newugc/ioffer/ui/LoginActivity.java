package cn.dezhisoft.cloud.mi.newugc.ioffer.ui;

import java.util.ArrayList;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Html;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import cn.dezhisoft.cloud.mi.newugc.R;

import cn.dezhisoft.cloud.mi.newugc.ioffer.common.Config;
import cn.dezhisoft.cloud.mi.newugc.ioffer.common.Util;
import cn.dezhisoft.cloud.mi.newugc.ioffer.config.AppConfig;
import cn.dezhisoft.cloud.mi.newugc.ioffer.service.IofferDefine;
import cn.dezhisoft.cloud.mi.newugc.ugc.core.model.ErrorMessage;
import cn.dezhisoft.cloud.mi.newugc.ugc.core.ws.CWUploadWebService;
import cn.dezhisoft.cloud.mi.newugc.ugc.model.Site;
import cn.dezhisoft.cloud.mi.newugc.ugc.ws.SoapResponse;

/**
 * 
 * @author Rosson Chen
 *
 */
public final class LoginActivity extends BaseActivity {

	EditText userName , password;
	TextView register_user;
	TextView siteTextview ;
	String uname,upwd ;
	
	ArrayList<Site> list ;
	String[] siteNameList ;
	String siteId ;
	String siteName ;
	static String oldWeb ;
	AlertDialog dialog ;
	
	AppConfig config ;
	
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ioffer_login_layout);
		//
		userName = (EditText) findViewById(R.id.edit_login_username);
		password = (EditText) findViewById(R.id.edit_login_password);
		register_user = (TextView) findViewById(R.id.bnt_login_register);
		siteTextview = (TextView) findViewById(R.id.edit_login_site);
		oldWeb	= null ;
		//
		register_user.setText(Html.fromHtml("<u>" + getString(R.string.label_login_create_account) + "</u>"));
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
				case IofferDefine.MSG_USER_LOGIN_SUCCESS :
					// auto login
					mAccessDatabase.updateAutoLoginUser(true,uname,upwd);
					// open check version
					mAccessDatabase.updateCheckVersion(Config.FLGA_BUTTON_ON);
					// go home activity
					switchHomeActivity() ;
					break ;
				case IofferDefine.MSG_USER_LOGIN_FAILED :
					String text = "" ;
					if(msg.obj instanceof ErrorMessage){
						text = ((ErrorMessage)msg.obj).getMessage();
					} else {
						text = msg.obj != null ? msg.obj.toString() : "Login Error." ;
					}
					showMessageDialog(text);
					break ;
				case IofferDefine.MSG_QUERY_SITE_SUCCESS :
					
					list = mIofferService.getSiteList() ;
					int size = list.size() ;
					
					if(size == 0){
						showMessageDialog(getString(R.string.label_query_site_failed));
						return ;
					}
					
					oldWeb	= config.getHost() ;
					
					siteNameList = new String[size];
					for(int i = 0 ; i < size ; i++){
						siteNameList[i] = list.get(i).getSiteName() ;
					}
					
					showSiteListDialog() ;
					break ;
				case IofferDefine.MSG_QUERY_SITE_FAILED :
					showMessageDialog(getString(R.string.label_query_site_failed));
					break ;
				}
			}
			
		};
	}

	@Override
	protected void onResume() {
		super.onResume();
		
		config		= mAccessDatabase.getConfig() ;
		siteId 		= config.getSiteId() ;
		siteName	= config.getSiteName() ;
		
		// 第一次
		if(oldWeb == null)
			oldWeb = config.getHost() ;
		
		if(config.getAuto() == 1){
			userName.setText(config.getName());
			password.setText(config.getPassword());
		} else {
			userName.setText("");
			password.setText("");
		}
		// 设置站点名称
		siteTextview.setText(siteName);
	}

	public void buttonOnclick(View v){
		switch(v.getId()){
		case R.id.bnt_login_login :
			
			Util.hideVirtualKeyPad(mContext, userName);
			
			//
			uname 	= userName.getText().toString().trim();
			upwd	= password.getText().toString().trim();
			String siteId = config.getSiteId() ;
			
			//
			if(("".equals(uname) || "".equals(upwd))){
				showMessageDialog(getString(R.string.warnning_login_input));
				return;
			}
			
			String c = Util.checkInput(uname);
			
			if (c != null) {
				showMessageDialog(getString(R.string.warnning_login_input_illegal));
				return;
			}
			
			if (!checkSiteId(siteId)) {
				showMessageDialog(getString(R.string.warnning_login_site_invaild));
				return ;
			}
			
			userLogin(uname, upwd);
			break ;
		case R.id.bnt_login_setting :
			Intent set = new Intent() ;
			set.putExtra(IofferSettingActivity.KEY_SYSTEM, 0);
			set.setClass(this, IofferSettingActivity.class);
			startActivity(set);
			break ;
		case R.id.bnt_login_register :
			Intent reg = new Intent() ;
			reg.setClass(this, IofferRegisterActivity.class);
			startActivity(reg);
			break ;
		case R.id.edit_login_site :
			
			Util.hideVirtualKeyPad(mContext, userName);
			
			String webHost = config.getHost();
			
			if(siteNameList == null || (oldWeb != null && !oldWeb.equals(webHost))){
				
				if(webHost.equals("")){
					showMessageDialog(getString(R.string.warring_hint_web_setting));
					return;
				}
				
				CWUploadWebService.initClientHost(webHost);
				
				mIofferService.querySiteInfo(mHandler) ;
				
			} else {
				showSiteListDialog() ;
			}
			break ;
		}
	}
	
	private void showSiteListDialog() {
		
		if(siteNameList == null || siteNameList.length == 0) 
			return ;
		//
		if(dialog == null)
			dialog = new AlertDialog.Builder(this)
			.setTitle(R.string.label_site_list)
			.setSingleChoiceItems(siteNameList, -1,
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						
						Site site = list.get(which);
						config.setSiteId(site.getSiteId());
						config.setSiteName(site.getSiteName()) ;
						mAccessDatabase.updateObject(config) ;
						
						siteId 	= site.getSiteId() ;
						siteTextview.setText(site.getSiteName());
						
						dialog.dismiss() ;
					}
				})
			.setNeutralButton(R.string.warring_hint_bnt_no, null)
			.create() ;
		//
		dialog.show() ;
	}
}
