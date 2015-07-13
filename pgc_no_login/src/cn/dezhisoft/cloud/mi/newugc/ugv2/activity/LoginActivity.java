package cn.dezhisoft.cloud.mi.newugc.ugv2.activity;

import com.baidu.mobstat.StatService;

import java.util.ArrayList;
import java.util.List;

import javax.security.auth.PrivateCredentialPermission;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.AlertDialog;
import android.app.Application;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.StrictMode;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuInflater;
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
import cn.dezhisoft.cloud.mi.newugc.ugv2.chat.common.bean.User;
import cn.dezhisoft.cloud.mi.newugc.ugv2.chat.common.tran.bean.TranObject;
import cn.dezhisoft.cloud.mi.newugc.ugv2.chat.common.tran.bean.TranObjectType;
import cn.dezhisoft.cloud.mi.newugc.ugv2.chat.common.util.Constants;
import cn.dezhisoft.cloud.mi.newugc.ugv2.client.Client;
import cn.dezhisoft.cloud.mi.newugc.ugv2.client.ClientOutputThread;
import cn.dezhisoft.cloud.mi.newugc.ugv2.utils.DialogFactory;
import cn.dezhisoft.cloud.mi.newugc.ugv2.utils.SharePreferenceUtil;
import cn.dezhisoft.cloud.mi.newugc.ugv2.utils.UserDB;

/**
 * 
 * @author Rosson Chen
 *
 */
public final class LoginActivity extends MyActivity implements OnClickListener {

	EditText e_userName , e_password;
	Spinner siteSpinner ;
	Button loginBtn, registerBtn;
	
	ArrayList<Site> list ;
	String[] siteNameList ;
	String siteId ;
	String siteName ;
	static String oldWeb ;
	AlertDialog dialog ;
	
	AppConfig config ;
	private MyApplication application;
	
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().detectDiskReads().detectDiskWrites().detectNetwork().penaltyLog().build());
        StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder().detectLeakedSqlLiteObjects().detectLeakedClosableObjects().penaltyLog().penaltyDeath().build());
		setContentView(R.layout.ugv2_login_layout);
		application=(MyApplication)this.getApplicationContext();
		e_userName = (EditText) findViewById(R.id.ugv2_login_username);
		e_password = (EditText) findViewById(R.id.ugv2_login_password);
		siteSpinner = (Spinner) findViewById(R.id.ugv2_login_site);
		loginBtn = (Button) findViewById(R.id.ugv2_login_btn);
		registerBtn = (Button) findViewById(R.id.ugv2_register_btn);
		
		loginBtn.setOnClickListener(this);
		registerBtn.setOnClickListener(this);
		
		list = new ArrayList<Site>();
		list.add(new Site());
		list.add(new Site());
		list.add(new Site());
		siteNameList = new String[] {"测试站点1","测试站点2","测试站点3",};
	}
	
	@Override
	protected void onResume() {// 在onResume方法里面先判断网络是否可用，再启动服务,这样在打开网络连接之后返回当前Activity时，会重新启动服务联网，
		super.onResume();
		if (isNetworkAvailable()) {
			Intent service = new Intent(this, GetMsgService.class);
			startService(service);
		} else {
			toast(this);
		}
	}
	
	public void onClick(View v){
		switch(v.getId()){
		case R.id.ugv2_login_btn:
			login();
			break;
		case R.id.ugv2_register_btn:
			goRegisterActivity();
			break;
		default:break;
		}
	}
	public void goRegisterActivity(){
		Intent intent=new Intent();
		intent.setClass(this, RegisterActivity.class);
		startActivity(intent);
	}
	private Dialog mDialog=null;
	private void showRequestDialog(){
		if(mDialog!=null){
			mDialog.dismiss();
			mDialog=null;
		}
		mDialog=DialogFactory.creatRequestDialog(this, "正在验证账号...");
		mDialog.show();
	}
	private void login(){
		String id=e_userName.getText().toString().trim();
		String password=e_password.getText().toString().trim();
		if(id.length()==0||password.length()==0){
			DialogFactory.ToastDialog(this, "Login", "亲！账号或密码不能为空欧");
		}else{
			showRequestDialog();
			//通过socket验证信息
			if(application.isClientStart()){
				Client client =application.getClient();
				ClientOutputThread out=client.getClientOutputThread();
				TranObject<User> o=new TranObject<User>(TranObjectType.LOGIN);
				User u=new User();
				u.setId(Integer.parseInt(id));
				u.setPassword(password);
				o.setObject(u);
				out.setMsg(o);
			}else{
				if(mDialog.isShowing())
					mDialog.dismiss();
				DialogFactory.ToastDialog(LoginActivity.this, "Login", "亲，服务器暂未开放服务欧");
			}
		}
	}
	
	
	 //依据自己需求处理父类广播接收者收取到的消息
	public void getMessage(TranObject msg) {
		if (msg != null) {
			System.out.println("Login:" + msg);
			switch (msg.getType()) {
			case LOGIN:// LoginActivity只处理登录的消息
				List<User> list = (List<User>) msg.getObject();
				//TODO 此处的list代表这个人的好友列表
				if (list!=null&&list.size() > 0) {
					// 保存用户信息
					SharePreferenceUtil util = new SharePreferenceUtil(
							LoginActivity.this, Constants.SAVE_USER);
					util.setId(e_userName.getText().toString());
					util.setPasswd(e_password.getText().toString());
					util.setEmail(list.get(0).getEmail());
					util.setName(list.get(0).getName());
					util.setImg(list.get(0).getImg());

					UserDB db = new UserDB(LoginActivity.this);
					db.addUser(list);

					Intent i = new Intent(LoginActivity.this,NavigationActivity.class/*FriendListActivity.class*/);
					i.putExtra(Constants.MSGKEY, msg);
					startActivity(i);

					if (mDialog.isShowing())
						mDialog.dismiss();
					finish();
					Toast.makeText(getApplicationContext(), "登录成功", 0).show();
				} else {
					DialogFactory.ToastDialog(LoginActivity.this, "QQ登录",
							"亲！您的帐号或密码错误哦");
					if (mDialog.isShowing())
						mDialog.dismiss();
				}
				break;
			default:
				break;
			}
		}
	}
	
	
	
	/**
	 * 判断手机网络是否可用
	 * 
	 * @param context
	 * @return
	 */
	private boolean isNetworkAvailable() {
		ConnectivityManager mgr = (ConnectivityManager) getApplicationContext()
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo[] info = mgr.getAllNetworkInfo();
		if (info != null) {
			for (int i = 0; i < info.length; i++) {
				if (info[i].getState() == NetworkInfo.State.CONNECTED) {
					return true;
				}
			}
		}
		return false;
	}

	private void toast(Context context) {
		new AlertDialog.Builder(context)
				.setTitle("温馨提示")
				.setMessage("亲！您的网络连接未打开哦")
				.setPositiveButton("前往打开",
						new DialogInterface.OnClickListener() {

							public void onClick(DialogInterface dialog,
									int which) {
								Intent intent = new Intent(
										android.provider.Settings.ACTION_WIRELESS_SETTINGS);
								startActivity(intent);
							}
						}).setNegativeButton("取消", null).create().show();
	}

}
