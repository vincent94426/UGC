package cn.dezhisoft.cloud.mi.newugc.ugc.version;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;

import cn.dezhisoft.cloud.mi.newugc.R;

import cn.dezhisoft.cloud.mi.newugc.ioffer.common.AccessDatabase;
import cn.dezhisoft.cloud.mi.newugc.ioffer.common.Config;
import cn.dezhisoft.cloud.mi.newugc.ioffer.service.IofferDefine;
import cn.dezhisoft.cloud.mi.newugc.ioffer.service.IofferService;
import cn.dezhisoft.cloud.mi.newugc.ugc.ws.SoapResponse;

/**
 * 版本管理
 * 
 * @author Rosson Chen
 * 
 */
public final class VersionManager {
	
	static final String TAG = "VersionManager" ;
	
	private static final int MSG_UPDATE 			= 1 ;
	private static final int MSG_DOWN_PROGRESS 		= 2 ;
	private static final int MSG_HIDE_DOWN_PROGRESS = 3 ;
	private static final int TIME_OUT				= 5000 ;
	
	private static final String TEXT_NEW_VERSION		= "版本升级" ;
	private static final String TEXT_BNT_YES			= "立即升级" ;
	private static final String TEXT_BNT_NO				= "暂不升级" ;
	private static final String TEXT_PROGRESS_TITLE		= "正在下载更新" ;
	
	private static VersionListener mListener ;
	private static ProgressDialog mProgressDialog ;
	private static Handler 		 mHandler ;
	private static String 	     mDescription ;
	
	/** 检测版本 */
	public static void checkVersion(String url,String siteId,final Activity activity,VersionListener listener){
		// 网络类型
		int type = getConnectedType(activity);
		if(type != ConnectivityManager.TYPE_WIFI){
			listener.notUpdate() ;
			return ;
		}
		//
		mListener	= listener ;
		// 参数
		if(url == null || url.equals("") || siteId == null || siteId.equals("")){
			mListener.notUpdate() ;
			return ;
		}
		// 客户端版本
		final PackageInfo pack = getVersionName(activity);
		if (pack == null) {
			mListener.notUpdate();
			return;
		}
		final String clientVersion = pack.versionName ;
		//
		mHandler = new Handler() {

			@Override
			public void handleMessage(Message msg) {
				super.handleMessage(msg);
				switch (msg.what) {
				case IofferDefine.MSG_QUERY_VERSION_SUCCESS :
					
					final VersionInfo info = (VersionInfo)msg.obj ;
					
					info.setLastVersion(clientVersion);
					String result 	= info.getStatus() ;
					mDescription	= info.getDescription() ;
					// 有新版本
					if(result.equals(VersionInfo.Result.CANUPGRADE)){
						mHandler.obtainMessage(MSG_UPDATE, info).sendToTarget();
					} 
					// 必须更新
					else if(result.equals(VersionInfo.Result.MUSTUPGRADE)){
						downloadAndInstall(activity,info);
					} 
					// 不需要更新
					else {
						mListener.notUpdate() ;
					}
					break ;
				case IofferDefine.MSG_QUERY_VERSION_FAILED :
					mListener.onError(activity.getString(R.string.label_error_check_version));
					break ;
				case MSG_UPDATE:
					showUpdateDialog(activity,(VersionInfo)msg.obj);
					break;
				case MSG_DOWN_PROGRESS :
					
					if(mProgressDialog == null){
						mProgressDialog	= new ProgressDialog(activity);
						mProgressDialog.setCancelable(false);
						mProgressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
						mProgressDialog.setMessage(mDescription);
						mProgressDialog.setMessage(TEXT_PROGRESS_TITLE);
					}
					
					if(!mProgressDialog.isShowing())
						mProgressDialog.show();
					
					mProgressDialog.setMax(msg.arg1);
					mProgressDialog.setProgress(msg.arg2);
					break ;
				case MSG_HIDE_DOWN_PROGRESS :
					
					if(mProgressDialog != null){
						mProgressDialog.dismiss() ;
					}
					mProgressDialog = null ;
					break ;
				}
			}
		};
		
		// 查询版本
		IofferService.getNewTipService().queryVersionUpdate(url, siteId,pack.versionName,new SoapResponse(mHandler));
		
	}
	
	/** 网络类型 */
	private static int getConnectedType(Context context) {
		if (context != null) {
			ConnectivityManager mConnectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
			NetworkInfo mNetworkInfo = mConnectivityManager.getActiveNetworkInfo();
			if (mNetworkInfo != null && mNetworkInfo.isAvailable()) {
				return mNetworkInfo.getType();
			}
		}
		return -1;
	}

	/** 本地包信息 */
	public static PackageInfo getVersionName(Context context){
		try{
			PackageManager packageManager = context.getPackageManager();
			return packageManager.getPackageInfo(context.getPackageName(), 0);
		}catch(Exception e){
			e.printStackTrace() ;
			return null;
		}
	}
	
	/** 下载并安装 */
	private static void downloadAndInstall(final Activity activity,final VersionInfo info){
		
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				
				try{
					
					File file = downloadApkFile(info.getUrl());
					
					mHandler.obtainMessage(MSG_HIDE_DOWN_PROGRESS).sendToTarget() ;
					
					Thread.sleep(1500);
					
					if(file != null){
						installApk(activity,file,info);
					} else {
						mListener.notUpdate() ;
					}
				}catch(Exception e){
					e.printStackTrace() ;
				}
			}
		}).start() ;
	}

	/** 下载文件 */
	private static File downloadApkFile(String path){

		try{
			
			if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
	
				URL url = new URL(path);
				HttpURLConnection conn = (HttpURLConnection) url.openConnection();
				conn.setConnectTimeout(TIME_OUT);
				// apk length
				final int length = conn.getContentLength() ;
				
				InputStream is = conn.getInputStream();
				File file = new File(Environment.getExternalStorageDirectory(),"iUGC" + System.currentTimeMillis() + ".apk");
				FileOutputStream fos = new FileOutputStream(file);
				BufferedInputStream bis = new BufferedInputStream(is);
				
				byte[] buffer = new byte[1024];
				int len;
				int total = 0;
				while ((len = bis.read(buffer)) != -1) {
					fos.write(buffer, 0, len);
					total += len;
					// progress
					mHandler.obtainMessage(MSG_DOWN_PROGRESS, length, total).sendToTarget();
				}
				
				fos.close();
				bis.close();
				is.close();
				return file;
			} else {
				return null;
			}
		}catch(Exception e){
			e.printStackTrace() ;
			return null ;
		}
	}
	
	/** 新版本提示对话框 */
	private static void showUpdateDialog(final Activity activity,final VersionInfo info) {
		AlertDialog.Builder builer = new Builder(activity);
		builer.setTitle(TEXT_NEW_VERSION);
		builer.setMessage(info.getDescription());
		builer.setCancelable(false);
		builer.setPositiveButton(TEXT_BNT_YES, new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				downloadAndInstall(activity, info);
			}
		});
		builer.setNegativeButton(TEXT_BNT_NO, new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				mListener.notUpdate() ;
			}
		}).create()
		.show() ;
	}
	
	/** 安装新版本 */
	private static void installApk(Activity context,File file,VersionInfo info) {
		AccessDatabase db = Config.getAccessDatabase(context);
		UpdateInfo update = new UpdateInfo() ;
		update.setVersionName(info.getMajorVersion());
		update.setVersionDescription(info.getDescription());
		update.setLastVersion(info.getLastVersion());
		update.setVersionStatus(UpdateInfo.Status.STATUS_NEW);
		db.saveObject(update);
		
		Intent intent = new Intent();
		intent.setAction(Intent.ACTION_VIEW);
		intent.setDataAndType(Uri.fromFile(file),"application/vnd.android.package-archive");
		context.startActivity(intent);
		context.finish() ;
	}
	
}
