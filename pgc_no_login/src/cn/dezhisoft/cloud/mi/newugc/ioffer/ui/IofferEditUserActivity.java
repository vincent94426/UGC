package cn.dezhisoft.cloud.mi.newugc.ioffer.ui;


import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import cn.dezhisoft.cloud.mi.newugc.R;

import com.sobey.android.ui.ActivityManager;
import cn.dezhisoft.cloud.mi.newugc.ioffer.common.Util;
import cn.dezhisoft.cloud.mi.newugc.ioffer.service.IofferDefine;
import cn.dezhisoft.cloud.mi.newugc.ugc.core.model.ErrorMessage;
import cn.dezhisoft.cloud.mi.newugc.ugc.core.model.User;
import cn.dezhisoft.cloud.mi.newugc.ugc.model.ImageItem;
import cn.dezhisoft.cloud.mi.newugc.ugc.ws.SoapResponse;
import cn.dezhisoft.cloud.mi.newugc.ugc.ws.publish.DownResourceCallback;

/**
 * 
 * @author Rosson Chen
 * 
 */
public final class IofferEditUserActivity extends BaseCropBitmapActivity {

	static final String TAG		= "IofferEditUserActivity" ;
	
	ImageView user_icon ;
	
	EditText user_email ,
			user_phone,
			user_old_pwd ,
			user_new_pwd,
			user_confirm_pwd ;
	
	User loginUser ,newUser;
	
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ioffer_edit_user_layout);
		
		loginUser 	= mIofferService.getUser() ;
		newUser		= new User();
		
		loadModifyUser();
		
		TextView title = (TextView) findViewById(R.id.ioffer_title_bar_content);
		title.setText(R.string.label_edit_user_title);
		//
		choice_flag = 0 ;
	}
	
	/**
	 * �û�����Ϣ���
	 */
	private final void loadModifyUser(){
		user_icon	= (ImageView) findViewById(R.id.img_view_edit_user_icon);
		user_email	= (EditText) findViewById(R.id.edit_edit_user_email);
		user_phone	= (EditText) findViewById(R.id.edit_edit_user_phone);
		user_old_pwd= (EditText) findViewById(R.id.edit_edit_user_old_pwd);
		user_new_pwd= (EditText) findViewById(R.id.edit_edit_user_new_password);
		user_confirm_pwd= (EditText) findViewById(R.id.edit_edit_user_confirm);
		
		user_email.setText(loginUser.getEmail());
		user_phone.setText(loginUser.getPhoneNumber());
		
		String url = loginUser.getAvatarsUrl() ;
		if(url != null && !url.equals("")){
			ImageItem item = new ImageItem() ;
			//item.setUrl(url);
			//mIofferService.getDownloader().downloadIcon(item, mHandler);
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
				case IofferDefine.MSG_USER_MODIFY_SUCCESS :
					
					mAccessDatabase.updateAutoLoginUser(false,newUser.getUsername(), newUser.getPassword());
					
					showMessageDialog(getString(R.string.edit_user_success),new DialogInterface.OnClickListener(){

						@Override
						public void onClick(DialogInterface dialog, int which) {
							ActivityManager.getActivityManager().backPrevious() ;
						}
						
					});
					
					break ;
				case IofferDefine.MSG_USER_MODIFY_FAILED :
					
					if(msg.obj instanceof ErrorMessage)
						showMessageDialog(((ErrorMessage)msg.obj).getMessage());
					break ;
				case DownResourceCallback.MSG_DOWNLOAD_BITMAP_SUCCESS :
					
//					String url = loginUser.getAvatarsUrl() ;
//					Bitmap bitmap = mIofferService.getDownloader().getBitmap(url) ;
//					
//					if(bitmap != null){
//						icon_bitmap	= bitmap ;
//						user_icon.setImageBitmap(icon_bitmap);
//					}
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
		case R.id.modify_user_icon :
			showChoiceBitmapDialog();
			break ;
		}
	}
	
	/**
	 * 
	 * @param v
	 */
	public void topBarButtonOnclick(View v){
		switch (v.getId()) {
		case R.id.bnt_ioffer_title_bar_left :
			ActivityManager.getActivityManager().backPrevious() ;
			break;
		case R.id.bnt_ioffer_title_bar_right :
			
			String old_pwd 	= user_old_pwd.getText().toString().trim();
			String newpwd 	= user_new_pwd.getText().toString().trim();
			String cpwd 	= user_confirm_pwd.getText().toString().trim();
			String email 	= user_email.getText().toString().trim();
			String phone 	= user_phone.getText().toString().trim();
			
			if(!old_pwd.equals("")){
				
				if(!old_pwd.equals(loginUser.getPassword())){
					showMessageDialog(getString(R.string.edit_user_old_pwd_wrong));
					return ;
				}
				
				if(!newpwd.equals(cpwd)){
					showMessageDialog(getString(R.string.edit_user_c_pwd_wrong));
					return ;
				}
				
				newUser.setPassword(newpwd);
			}
			else {
				newUser.setPassword(loginUser.getPassword());
			}
			
			// icon
			if(choice_flag == 1 && icon_bitmap != null){
				newUser.setIconBase64(Util.bitmapToBase64(icon_bitmap));
			}
			
			//
			newUser.setUsername(loginUser.getUsername());
			newUser.setUserRole(loginUser.getUserRole());
			newUser.setSessionUID(loginUser.getSessionUID());
			newUser.setEmail(email);
			newUser.setPhoneNumber(phone);
			
			mIofferService.userUpdate(newUser, new cn.dezhisoft.cloud.mi.newugc.ugc.core.ws.CWSoapResponse(mHandler));
			
			break ;
		}
	}
	
	@Override
	protected void updateUserIcon(Intent data) {
		if(data == null) return ;
		Bundle extras = data.getExtras();
		if (extras != null) {
			Bitmap bmp  = extras.getParcelable("data");
			icon_bitmap = resizeImage(bmp, ICON_WIDTH, ICON_WIDTH);
			user_icon.setImageBitmap(icon_bitmap);
		}
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		return false;
	}
}
