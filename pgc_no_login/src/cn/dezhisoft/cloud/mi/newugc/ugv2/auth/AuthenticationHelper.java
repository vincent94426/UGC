package cn.dezhisoft.cloud.mi.newugc.ugv2.auth;

import android.app.Activity;
import android.text.TextUtils;

import com.higgses.sobeysdk.api.SoBeyApi;
import com.higgses.sobeysdk.api.inf.SdkCallback;
import cn.dezhisoft.cloud.mi.newugc.ugv2.constants.UgV2Constants;

/**
 * 登录验证相关辅助类
 * 1.调用SDK登录
 * 2.SDK出问题则调用自己的登录
 * @author Rosson Chen
 * 2013-6-30
 */
public final class AuthenticationHelper {
	private Activity activity;
	public AuthenticationHelper(Activity activity){
		this.activity = activity;
	}
	
	public void validateLogin(final AuthenticationCallback callback) {
		try{
			SoBeyApi.getAccessToken(activity, UgV2Constants.NPM_ANDROID_APPID, UgV2Constants.NPM_ANDROID_APPKEY, new SdkCallback() {
				
				@Override
				public void onCallback(Boolean bSdkAvailable, Boolean loginStatus, String accessToken, String msg) {
					if(bSdkAvailable.booleanValue()){
						if(loginStatus.booleanValue() && !TextUtils.isEmpty(accessToken)){
							//登录成功
							initAppRuntime(callback);
						}else {
							//登录错误
							if(isNpmServerAvailabe()){//检测NPM服务可用，调用自己的登录界面
								mockNpmLogin(callback);
							}else{
								callback.onAuth(false);
							}
						}
					}else {
						//平台不存在或者不可用，调用自己的登录界面
						mockNpmLogin(callback);
					}
				}
	
				private boolean isNpmServerAvailabe() {
					return true;
				}
			});
		}catch (Exception e) {
			mockNpmLogin(callback);
		}
	}

	private void initAppRuntime(final AuthenticationCallback callback) {
		callback.onAuth(true);
	}
	
	/**
	 * 不做模拟登录了，直接回调通知错误
	 * @param callback
	 */
	private void mockNpmLogin(final AuthenticationCallback callback) {
		/*InitTask.getInstance().execute(InitTaskName.LOGIN, new AbstractTaskListener() {
			
			@Override
			public void onFinish(Bundle bundle) {
				int result = bundle.getInt(Task.KEY_RESULT);
				if (result == Task.RESULT_OK) {
					callback.onAuth(true);
				}else {
					callback.onAuth(false);
				}
			}
		}, "00002398", "");*/
		callback.onAuth(false);
	}
	
	public interface AuthenticationCallback{
		void onAuth(boolean bLogin);
	}
}
