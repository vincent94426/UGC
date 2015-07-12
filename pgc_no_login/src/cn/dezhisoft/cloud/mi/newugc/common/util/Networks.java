package cn.dezhisoft.cloud.mi.newugc.common.util;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.telephony.TelephonyManager;

public class Networks {
	public static final int NETWORK_SETTING_CODE = 99;
	public static boolean isConnected(Context context) {
		ConnectivityManager connectivity = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		if (connectivity == null) {
			return false;
		} else {
			NetworkInfo[] info = connectivity.getAllNetworkInfo();
			if (info != null) {
				for (int i = 0; i < info.length; i++) {
					if (info[i].getState() == NetworkInfo.State.CONNECTED) {
						return true;
					}
				}
			}
		}
		return false;
	}
	
	/**
	 * 是否某种类型的连接
	 * @param context
	 * @param type	{@link ConnectivityManager}
	 * @return
	 */
	public static boolean isConnectionType(Context context, int type) {
		ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo active = cm.getActiveNetworkInfo();
		return (active != null && active.getType() == type);
	}
	
	/**
	 * 是否WIFI连接
	 * @param context
	 * @return
	 */
	public static boolean isWIFI(Context context) {
		ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo[] infos = cm.getAllNetworkInfo();
		
		if(null != infos){
			for (int i = 0; i < infos.length; i++) {
				if("WIFI".equals(infos[i].getTypeName()) && infos[i].isConnected()) {
					return true;
				}
			}
		}
		
		return false;
	}
	
	public static boolean is2GNetWork(Context context) {
			ConnectivityManager connectivity = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
			if (connectivity == null) {
				return false;
			} else {
				ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
				NetworkInfo net = cm.getActiveNetworkInfo();
			    if (null != net && net.getState() == NetworkInfo.State.CONNECTED) {
					int type    = net.getType();
					int subtype = net.getSubtype();
							
					return !isConnectionFast(type, subtype);
				}
			}
			return false;
	}
	
	/**
	 * 是否高速网络，判断是2G/3G
	 * @param type
	 * @param subType
	 * @return
	 */
	public static boolean isConnectionFast(int type, int subType){
        if(type==ConnectivityManager.TYPE_WIFI){
            return true;
        }else if(type==ConnectivityManager.TYPE_MOBILE){
            switch(subType){
            case TelephonyManager.NETWORK_TYPE_1xRTT:
                return false; // ~ 50-100 kbps
            case TelephonyManager.NETWORK_TYPE_CDMA:
                return false; // ~ 14-64 kbps
            case TelephonyManager.NETWORK_TYPE_EDGE:
                return false; // ~ 50-100 kbps
            case TelephonyManager.NETWORK_TYPE_EVDO_0:
                return true; // ~ 400-1000 kbps
            case TelephonyManager.NETWORK_TYPE_EVDO_A:
                return true; // ~ 600-1400 kbps
            case TelephonyManager.NETWORK_TYPE_GPRS:
                return false; // ~ 100 kbps
            case TelephonyManager.NETWORK_TYPE_HSDPA:
                return true; // ~ 2-14 Mbps
            case TelephonyManager.NETWORK_TYPE_HSPA:
                return true; // ~ 700-1700 kbps
            case TelephonyManager.NETWORK_TYPE_HSUPA:
                return true; // ~ 1-23 Mbps
            case TelephonyManager.NETWORK_TYPE_UMTS:
                return true; // ~ 400-7000 kbps
//            // NOT AVAILABLE YET IN API LEVEL 7
//            case Connectivity.NETWORK_TYPE_EHRPD:
//                return true; // ~ 1-2 Mbps
//            case Connectivity.NETWORK_TYPE_EVDO_B:
//                return true; // ~ 5 Mbps
//            case Connectivity.NETWORK_TYPE_HSPAP:
//                return true; // ~ 10-20 Mbps
//            case Connectivity.NETWORK_TYPE_IDEN:
//                return false; // ~25 kbps
//            case Connectivity.NETWORK_TYPE_LTE:
//                return true; // ~ 10+ Mbps
            // Unknown
            case TelephonyManager.NETWORK_TYPE_UNKNOWN:
                return false;
            default:
                return false;
            }
        }else{
            return false;
        }
    }

	/**
	 * 获取当前网络连接类型名称
	 * @param context
	 * @return
	 */
	public static String getNetworkName(Context context) {
		ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo info = cm.getActiveNetworkInfo();
		return (info != null ? info.getTypeName() : "NOTHING");
	}
	
	/**
	 * 打开系统网络设置界面
	 * @param context
	 */
    public static void openNetSettingActivity(Context context) {
    	if(context instanceof Activity) {
    		if (android.os.Build.VERSION.SDK_INT >= 14) {
    			Intent intent = new Intent(android.provider.Settings.ACTION_SETTINGS);
    			intent.addCategory("android.intent.category.LAUNCHER");
    			((Activity)context).startActivityForResult(intent, NETWORK_SETTING_CODE);
    		} else {
    			((Activity)context).startActivityForResult(new Intent(android.provider.Settings.ACTION_WIRELESS_SETTINGS), NETWORK_SETTING_CODE);
    		}
    	}
    }   
}