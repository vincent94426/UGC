package cn.dezhisoft.cloud.mi.newugc.ugv2.activity;

import com.baidu.mobstat.StatService;

import java.util.ArrayList;

import android.app.ActivityManager;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import cn.dezhisoft.cloud.mi.newugc.R;
import cn.dezhisoft.cloud.mi.newugc.common.util.DebugUtil;
import cn.dezhisoft.cloud.mi.newugc.common.util.OperationEnableTimer;
import cn.dezhisoft.cloud.mi.newugc.ioffer.common.Config;
import cn.dezhisoft.cloud.mi.newugc.ioffer.common.Util;
import cn.dezhisoft.cloud.mi.newugc.ioffer.config.AppConfig;
import cn.dezhisoft.cloud.mi.newugc.ioffer.service.IofferDefine;
import cn.dezhisoft.cloud.mi.newugc.ugc.core.model.ErrorMessage;
import cn.dezhisoft.cloud.mi.newugc.ugc.core.ws.CWUploadWebService;
import cn.dezhisoft.cloud.mi.newugc.ugc.model.Site;
import cn.dezhisoft.cloud.mi.newugc.ugc.ws.SoapResponse;
import cn.dezhisoft.cloud.mi.newugc.ugc.ws.UGCWebService;
import cn.dezhisoft.cloud.mi.newugc.ugv2.base.activity.AbstractBuzzActivity;

/**
 * 
 * @author Rosson Chen
 *
 */
public final class LoginActivity extends AbstractBuzzActivity {

	EditText userName , password;
	Spinner siteSpinner ;
	String uname,upwd ;
	Button loginBtn, settingBtn;
	
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
		setContentView(R.layout.ugv2_login_layout);
		//
		userName = (EditText) findViewById(R.id.ugv2_login_username);
		password = (EditText) findViewById(R.id.ugv2_login_password);
		siteSpinner = (Spinner) findViewById(R.id.ugv2_login_site);
		loginBtn = (Button) findViewById(R.id.ugv2_login_btn);
		settingBtn = (Button) findViewById(R.id.ugv2_login_setting);
		
		oldWeb	= null ;
		loginBtn.setOnClickListener(onclickListener);
		settingBtn.setOnClickListener(onclickListener);
		
		list = new ArrayList<Site>();
		list.add(new Site());
		list.add(new Site());
		list.add(new Site());
		siteNameList = new String[] {"测试站点1","测试站点2","测试站点3",};
		
		UGCWebService.setDebug(true);
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

	protected void switchHomeActivity() {
		Intent homeIntent = new Intent(this.getApplicationContext(), NavigationActivity.class);
		startActivity(homeIntent);
		finish();
	}

	@Override
	protected void onResume() {
		super.onResume();
		
		setDefaultSite();
		Log.i("baidu", "baidu 统计 resume");
		StatService.onResume(this);
	}
	@Override
	protected void onPause() {
	    // TODO Auto-generated method stub
	    super.onPause();
	    Log.i("baidu", "baidu 统计 pause");
	    StatService.onPause(this);
	}

	private void setDefaultSite() {
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
		//siteTextview.setText(siteName);
		initSitesList();
	}
	
	private OnClickListener onclickListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.ugv2_login_btn:

				if(OperationEnableTimer.isEnable()){
					/*OperationEnableTimer.disableOperation(1000);
					
					
					Util.hideVirtualKeyPad(mContext, userName);
	
					//
					uname = userName.getText().toString().trim();
					upwd = password.getText().toString().trim();
					String siteId = config.getSiteId();
	
					//密码可以为空
					if (("".equals(uname))) {
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
						return;
					}*/
	
					userLogin(uname, upwd);

				}
				break;
			case R.id.ugv2_login_setting:
				showDialog(DLG_ID_SETTING);
				break;
			}
		}
	};

	private void initSitesList() {
		Util.hideVirtualKeyPad(mContext, userName);
		
		/*String webHost = config.getHost();
		
		if(siteNameList == null || (oldWeb != null && !oldWeb.equals(webHost))){
			
			if(webHost.equals("")){
				showMessageDialog(getString(R.string.warring_hint_web_setting));
				return;
			}
			
			CWUploadWebService.initClientHost(webHost);
			
			mIofferService.querySiteInfo(mHandler) ;
			
		} else {
			showSiteListDialog() ;
		}*/
		showSiteListDialog() ;
	}

	private void showSiteListDialog() {
		
		if(siteNameList == null || siteNameList.length == 0){ 
			return ;
		}
		
		ArrayAdapter<String> siteAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, siteNameList);
		siteAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		siteSpinner.setAdapter(siteAdapter);
		int selectIndex = 0;
		for (int i = 0; i < list.size(); i++) {
			if (siteId.equals(list.get(i).getSiteId())){
				selectIndex = i;
				break;
			}
		}
		siteSpinner.setSelection(selectIndex);
		siteSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				Site site = list.get(arg2);
				config.setSiteId(site.getSiteId());
				config.setSiteName(site.getSiteName());
				mAccessDatabase.updateObject(config);
				
				siteId 	= site.getSiteId() ;
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				
			}
		});
	}
	
	private static final int DLG_ID_SETTING = 101;
	@Override
	protected Dialog onCreateDialog(int id) {
		switch (id) {
		case DLG_ID_SETTING:
			Builder dialogBuilder = new AlertDialog.Builder(this);
			dialogBuilder.setTitle("设置服务地址");
			View layout = getLayoutInflater().inflate(R.layout.ugv2_setting_dialog_layout, null);
			layout.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
			final EditText onAddEditText = (EditText) layout.findViewById(R.id.editText);
			final Button confirmButton = (Button) layout.findViewById(R.id.confirmButton);
			confirmButton.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					String oldhost = config.getHost();

					String newhost = onAddEditText.getText().toString();
					if (TextUtils.isEmpty(newhost) || newhost.equals(oldhost)) {
						dismissDialog(DLG_ID_SETTING);
						return;
					}

					config.setHost(newhost);
					config.setSiteId("-1");
					config.setSiteName("");

					mAccessDatabase.updateObject(config);
					dismissDialog(DLG_ID_SETTING);
					initSitesList();
				}
			});
			dialogBuilder.setView(layout);
			return dialogBuilder.create();
		}
		return super.onCreateDialog(id);
	}

	@Override
	protected void onPrepareDialog(int id, Dialog dialog, Bundle args) {
		super.onPrepareDialog(id, dialog, args);
		switch (id) {
		case DLG_ID_SETTING:
			final EditText editText = (EditText) dialog.findViewById(R.id.editText);
			editText.setText(config.getHost());
			break;
		default:
			break;
		}
		super.onPrepareDialog(id, dialog);
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
			killBackgroudProcess(mContext);
			super.onBackPressed();
		}
	}
	
	public static void killBackgroudProcess(Context context) {
		try {
			Log.d("ApplicationUtil", "KillBackgroudProcess");
			String packageName = context.getPackageName();
			ActivityManager am = (ActivityManager) context
					.getSystemService(Context.ACTIVITY_SERVICE);
			am.killBackgroundProcesses(packageName);

			System.exit(0);
		} catch (Exception e) {
			DebugUtil.traceThrowableLog(e);
		}
	}
}
