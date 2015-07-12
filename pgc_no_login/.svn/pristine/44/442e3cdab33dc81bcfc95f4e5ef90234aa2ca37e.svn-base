package cn.dezhisoft.cloud.mi.newugc.common.util;

import java.util.List;
import java.util.Locale;

import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.text.TextUtils;

public final class AppUtil {
	private static final Object APP_NAME = "npm";
	private AppUtil(){}

    public static boolean isTopActivity(Context context) {
        String packageName = context.getPackageName();
        ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> taskInfos = manager.getRunningTasks(1);

        return taskInfos.size() > 0 && packageName.equals(taskInfos.get(0).topActivity.getPackageName());
    }

	public static String getUserAgent(Context mContext) {
		PackageManager pm = mContext.getPackageManager();
		PackageInfo info = null;
		try {
			info = pm.getPackageInfo(mContext.getPackageName(), 0);
		} catch (Exception e) {
		}
		Locale locale = Locale.getDefault();
		
		StringBuffer buff = new StringBuffer();
		buff.append(APP_NAME);
		
		if(info != null) {
			buff.append(info.versionName).append('/').append(info.versionCode);
		}

		buff.append(" (Linux; U; Android ").append(Build.VERSION.RELEASE).append("; ");
		buff.append(locale.getLanguage() == null ? "en" : locale
						.getLanguage().toLowerCase());
		buff.append('-');
		buff.append(locale.getCountry() == null ? "" : locale.getCountry()
						.toLowerCase()).append("; ");
		buff.append(Build.MODEL).append("; Build/").append(Build.ID);
		
		//增加渠道信息
		try {
			ApplicationInfo app = pm.getApplicationInfo(mContext.getPackageName(), PackageManager.GET_META_DATA);
			if(app!=null && app.metaData != null) {
				Bundle bundle = app.metaData;
				String storeId = bundle.getString("lenovo:channel");
				if(!TextUtils.isEmpty(storeId)) {
					buff.append("; Channel/").append(storeId);
				}
			}
		} catch (Exception e1) {
		}
		
		buff.append(") ");
		
		try {
			TelephonyManager tm = (TelephonyManager) mContext.getSystemService(Context.TELEPHONY_SERVICE);
			buff.append(tm.getDeviceId());
		} catch (Exception e) {
		}
		
		/*npm 2.1.14/3900279 (Linux; U; Android 2.3.4; zh-cn; Nexus One; Build/IMobile-djasir-GRJ22; Channel/npm) 354957035452245*/
		
		return buff.toString();
	}
	
    public static String getAppVersionName(Context context) {
        String versionName = "";
        try {
            PackageManager pm = context.getPackageManager();
            PackageInfo pi = pm.getPackageInfo(context.getPackageName(), 0);
            versionName = pi.versionName;
            if (versionName == null || versionName.length() <= 0) {
                return "0.1.0";
            }
        } catch (Exception e) {
            // Log.e(TAG, "Exception", e);
        }
        return versionName;
    }
    
    public static boolean isServerReachable(){
    	return true;
    }
}