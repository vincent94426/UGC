package cn.dezhisoft.cloud.mi.newugc.ioffer.ui;


import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import cn.dezhisoft.cloud.mi.newugc.R;

import cn.dezhisoft.cloud.mi.newugc.ioffer.config.AppConfig;
import cn.dezhisoft.cloud.mi.newugc.ioffer.service.IofferDefine;
import com.sobey.sdk.util.Util;
import cn.dezhisoft.cloud.mi.newugc.ugc.core.model.ErrorMessage;
import cn.dezhisoft.cloud.mi.newugc.ugc.core.model.User;
import cn.dezhisoft.cloud.mi.newugc.ugc.ws.SoapResponse;

/**
 * 注册界面
 * @author Rosson Chen
 *
 */
public final class IofferRegisterActivity extends BaseCropBitmapActivity {
	
	/** UI组件*/
	EditText userName,nickName,password,confirm,email,phone ;
	
	/** icon*/
	ImageView userIcon ;
	
	/** */
	User user ;
	
	/** 客户端配置*/
	AppConfig config ;
	
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ioffer_register_layout);
		
		config = mAccessDatabase.getConfig() ;
		// top bar
		TextView title = (TextView) findViewById(R.id.ioffer_title_bar_content);
		title.setText(R.string.label_register_title);
		// 输入UI组件
		userName 	= (EditText) findViewById(R.id.edit_register_username);
		nickName 	= (EditText) findViewById(R.id.edit_register_nickname);
		password 	= (EditText) findViewById(R.id.edit_register_password);
		confirm 	= (EditText) findViewById(R.id.edit_register_confirm);
		email 		= (EditText) findViewById(R.id.edit_register_email);
		phone 		= (EditText) findViewById(R.id.edit_register_phone);
		userIcon	= (ImageView) findViewById(R.id.register_user_icon_view);
		choice_flag = 0 ;
		
		// 查询配置
		if(!loadHostSetting()){
			showMessageDialog(getString(R.string.label_register_web_error));
		}
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
				case IofferDefine.MSG_USER_REGISTER_SUCCESS :
					new Builder(mContext)
					.setTitle(getString(R.string.register_success))
					.setPositiveButton(R.string.warring_hint_bnt_yes,new DialogInterface.OnClickListener() {
						
						@Override
						public void onClick(DialogInterface dialog, int which) {
							finish() ;
						}
					})
					.setCancelable(false)
					.create()
					.show();
					break ;
				case IofferDefine.MSG_USER_REGISTER_FAILED :
					if(msg.obj instanceof ErrorMessage)
						showMessageDialog(((ErrorMessage)msg.obj).getMessage());
					else
						showMessageDialog("Sorry register failed");
					break ;
				}
			}
		};
	}

	/**
	 * top 导航
	 * @param v
	 */
	public void topBarButtonOnclick(View v){
		switch(v.getId()){
		case R.id.bnt_ioffer_title_bar_left :
			finish() ;
			break ;
		case R.id.bnt_ioffer_title_bar_right :
			String name 	= userName.getText().toString().trim();
			String nickname = nickName.getText().toString().trim();
			String pwd		= password.getText().toString().trim();
			String cpwd		= confirm.getText().toString().trim();
			String em		= email.getText().toString().trim();
			String ph		= phone.getText().toString().trim();
			// 创建用户
			user = new User() ;
			// 用户名
			if(name.length() < 4 || name.length() > 20){
				showMessageDialog(getString(R.string.register_fill_username));
				return ;
			}
			
			if(nickname.length() < 4 || nickname.length() > 20){
				showMessageDialog(getString(R.string.register_fill_nickname));
				return ;
			}
			
			boolean result = cn.dezhisoft.cloud.mi.newugc.ioffer.common.Util.checkText(name);
			if (!result) {
				showMessageDialog(getString(R.string.warnning_login_input_illegal));
				return;
			}
			
			// 密码
			if(pwd.length() < 6 || pwd.length() > 20){
				showMessageDialog(getString(R.string.register_fill_password));
				return ;
			}
			// 验证密码
			if(!pwd.equals(cpwd)){
				showMessageDialog(getString(R.string.register_confirm_wrong));
				return;
			}
			//
			if(!em.equals("") && !Util.validateEmail(em)){
				showMessageDialog(getString(R.string.register_email_wrong));
				return;
			}
			// 头像
			if(choice_flag == 1 && icon_bitmap != null){
				user.setIconBase64(cn.dezhisoft.cloud.mi.newugc.ioffer.common.Util.bitmapToBase64(icon_bitmap));
			}
			//
			user.setSiteId("1");
			user.setSessionUID("0");
			user.setUsername(name);
			user.setNickName(nickname);
			user.setPassword(pwd);
			user.setEmail(em);
			user.setPhoneNumber(ph);
			user.addApprovedLoginRole("client");
			user.addApprovedStreamType("live");
			//user.addApprovedStreamType("file");
			//user.addApprovedCatalogType("");
			//
			mIofferService.userRegister(user,new cn.dezhisoft.cloud.mi.newugc.ugc.core.ws.CWSoapResponse(mHandler));
			break ;
		}
	}
	
	/**
	 * button 事件
	 * @param v
	 */
	public void buttonOnclick(View v){
		switch(v.getId()){
		case R.id.register_user_icon :
			showChoiceBitmapDialog();
			break ;
		}
	}
	
	/**
	 * 更新
	 */
	@Override
	protected void updateUserIcon(Intent data) {
		if(data == null) return ;
		Bundle extras = data.getExtras();
		if (extras != null) {
			Bitmap bmp  = extras.getParcelable("data");
			icon_bitmap = resizeImage(bmp, ICON_WIDTH, ICON_WIDTH);
			userIcon.setImageBitmap(icon_bitmap);
		}
	}
}
