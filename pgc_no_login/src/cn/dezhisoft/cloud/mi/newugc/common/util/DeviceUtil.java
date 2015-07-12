package cn.dezhisoft.cloud.mi.newugc.common.util;

import java.util.Locale;

import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;

public final class DeviceUtil {
	public static final String TAG = "DeviceUtil";

	private DeviceUtil() {

	}

	public static void logDensity(Activity activity) {
		DisplayMetrics dm = new DisplayMetrics();
		activity.getWindowManager().getDefaultDisplay().getMetrics(dm);
		Log.i(TAG, "密度:" + dm.densityDpi);
	}

	public static void logScreenDip(Activity activity) {
		DisplayMetrics dm = new DisplayMetrics();
		activity.getWindowManager().getDefaultDisplay().getMetrics(dm);
		Log.i(TAG, "宽dip:" + dm.widthPixels);
		Log.i(TAG, "高dip:" + dm.heightPixels);
	}

	public static void logScreen(Activity activity) {
		DisplayMetrics dm = new DisplayMetrics();
		activity.getWindowManager().getDefaultDisplay().getMetrics(dm);
		int widthPixels = (int) (dm.widthPixels * dm.density + 0.5f);
		int heightPixels = (int) (dm.heightPixels * dm.density + 0.5f);
		Log.i(TAG, "宽:" + widthPixels);
		Log.i(TAG, "高:" + heightPixels);
	}

	public static int dip2px(Context context, float dipValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (dipValue * scale + 0.5f);
	}

	public static int px2dip(Context context, float pxValue) {
		final float scale = context.getResources().getDisplayMetrics().scaledDensity;
		return (int) (pxValue / scale + 0.5f);
	}

	public static int px2sp(Context context, float pxValue) {
		final float scale = context.getResources().getDisplayMetrics().scaledDensity;
		return (int) (pxValue / scale + 0.5f);
	}

	public static int sp2px(Context context, float spValue) {
		final float scale = context.getResources().getDisplayMetrics().scaledDensity;
		return (int) (spValue * scale + 0.5f);
	}

	public static void logDip2px(Context context, float dipValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		Log.i(TAG, "dip:" + dipValue + "---px:" + (int) (dipValue * scale + 0.5f));
	}

	public static void logPx2dip(Context context, float pxValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		Log.i(TAG, "scale:" + scale + "---px:" + pxValue + "---dip:" + (int) (pxValue / scale + 0.5f));
	}

	public static int[] getScreenSize(Context context) {
		DisplayMetrics dm = context.getResources().getDisplayMetrics();
		int[] screenSize = new int[2];
		screenSize[0] = dm.widthPixels;// 获取分辨率宽度
		screenSize[1] = dm.heightPixels;// 获取分辨率高度
		return screenSize;
	}

	public static String getLanguage() {
		return getLanguage(Locale.getDefault());
	}

	static String getLanguage(Context context) {
		String lang = "zh-CN";
		try {
			Configuration configuration = context.getResources().getConfiguration();
			lang = configuration.locale.toString().replace('_', '-');
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return lang;
	}

	/**
	 * 获取当前语言
	 * 
	 * @return
	 */
	public static String getLanguage(Locale locale) {
		String language = locale.getLanguage();
		String country = locale.getCountry();
		return TextUtils.isEmpty(country) ? language : language + "-" + country;
	}

	public static int getAndroidSDKVersion() {
		int version = 0;
		try {
			version = Integer.valueOf(android.os.Build.VERSION.SDK);
		} catch (NumberFormatException e) {
			// Do nothing
		}
		return version;
	}

	public static String getDeviceId(final Context context) {
		TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
		return tm.getDeviceId();
	}

}
