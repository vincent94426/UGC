package cn.dezhisoft.cloud.mi.newugc.ugc.share;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;

public final class ShareUtil {

	/**
	 * 查询支持分享的APP
	 * 
	 * @param context
	 * @return
	 */
	public static ArrayList<ShareApp> queryShareResolveInfo(Context context) {
		
		final ArrayList<ShareApp> list = new ArrayList<ShareApp>() ;
		
		if(context == null){
			return list ;
		}
		
		final PackageManager pm  = context.getPackageManager() ;
		List<ResolveInfo> appList = getShareTargets(context);
		
		if (appList.size() > 0) {
			for (int i = 0; i < appList.size(); i++) {
				ShareApp app 			= new ShareApp() ;
				ResolveInfo info 		= (ResolveInfo) appList.get(i);
				ApplicationInfo apinfo 	= info.activityInfo.applicationInfo;
				app.setResolveInfo(info);
				app.setName(apinfo.loadLabel(pm).toString()) ;
				
				list.add(app);
			}
		}
		
		return list ;
	}

	/** 查询ResolveInfo */
	private static List<ResolveInfo> getShareTargets(Context context) {
		Intent intent = new Intent(Intent.ACTION_SEND, null);
		intent.addCategory(Intent.CATEGORY_DEFAULT);
		intent.setType("text/plain");
		PackageManager pm = context.getPackageManager();
		return pm.queryIntentActivities(intent,PackageManager.COMPONENT_ENABLED_STATE_DEFAULT);
	}
	
	/** 启动分析 */
	public static void sendShare(Context context,ShareApp app,String title, String description,String preview,String srteamPath) {
		try {
			Intent shareIntent = new Intent(Intent.ACTION_SEND);
			shareIntent.setComponent(new ComponentName(
							app.getResolveInfo().activityInfo.packageName,
							app.getResolveInfo().activityInfo.name));
			shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION); 
			
			StringBuilder content = new StringBuilder() ;
			content.append("[");
			content.append(title);
			content.append("]");
			content.append(description);
			content.append(",");
			content.append("\n");
			content.append(preview);// 预览
			content.append("\n");
			content.append("分享自iUGC:" + "http://ugcm.sob-newstips.cn/client/iUGC.apk");
			
			Uri uri = null ;
			if(srteamPath != null){
				shareIntent.setType("image/*");   
				uri	= Uri.fromFile(new File(srteamPath));
				shareIntent.putExtra(Intent.EXTRA_STREAM, uri);
				//当用户选择短信时使用sms_body取得文字  
		        shareIntent.putExtra("sms_body", content.toString());  
			} else {
				shareIntent.setType("text/plain");   
			}
			//
			shareIntent.putExtra(Intent.EXTRA_TEXT, content.toString());
			shareIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			context.startActivity(shareIntent);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
