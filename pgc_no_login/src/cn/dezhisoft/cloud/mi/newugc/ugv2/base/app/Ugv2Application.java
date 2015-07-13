package cn.dezhisoft.cloud.mi.newugc.ugv2.base.app;

import java.util.ArrayList;

import cn.dezhisoft.cloud.mi.newugc.ioffer.common.AccessDatabase;
import cn.dezhisoft.cloud.mi.newugc.ioffer.common.Config;
import cn.dezhisoft.cloud.mi.newugc.ioffer.upload.ui.UGCUploadConfig;

import com.sobey.sdk.db.DatabaseManager;
import com.sobey.sdk.db.IDBManager;

import android.app.Application;
import android.util.Log;

public class Ugv2Application extends Application{
	private static final String AppName			= "ugc_upload" ;
	private static final int FLGA_BUTTON_OFF		= 0 ;
	//private static final int FLGA_BUTTON_ON		= 1 ;
	
	private static IDBManager AppDatabase ;
	private static UGCUploadConfig 	AppConfig ;
	/** 数据库访问 */
	protected static AccessDatabase mAccessDatabase ;
	
	@Override
	public void onCreate() {
		super.onCreate();
		
		initConfig();
		System.out.println("MOTHERFUCKER");
		System.out.println(AppDatabase.toString());
		System.out.println(AppConfig.toString());
		System.out.println(mAccessDatabase.toString());
	}
	
	private final void initConfig(){
		
		if(!(AppDatabase == null || AppConfig == null || mAccessDatabase == null)) return ;
		
		if(AppDatabase == null){
			Log.i("GOD","appDatabase is null");
			DatabaseManager.initDBManager(this,1,null);
			AppDatabase		= DatabaseManager.getDatabaseManager() ;
		}
		
		if(AppConfig == null){
			Log.i("GOD","AppConfig is null");
			ArrayList<Object> list = AppDatabase.queryAllObject(UGCUploadConfig.class);
			if(list != null && list.size() > 0){
				AppConfig	= (UGCUploadConfig)list.get(0);
			} else {
				
				AppConfig	= new UGCUploadConfig() ;
				AppConfig.setAppName(AppName);
				AppConfig.setGpsFlag(FLGA_BUTTON_OFF);
				AppConfig.setVideoWidth(176);
				AppConfig.setVideoHeight(144);
				AppConfig.setVideoBitrate(300*1024);
				
				if(AppDatabase.saveObject(AppConfig) > 0){
					list 		= AppDatabase.queryAllObject(UGCUploadConfig.class);
					AppConfig 	= (UGCUploadConfig)list.get(0);
				} else {
					Log.e("BaseActivity", "Save defaule app config error.");
				}
			}
		}
		
		if (mAccessDatabase == null) {
			mAccessDatabase = Config.getAccessDatabase(getApplicationContext()) ;
		}
	}

	public static IDBManager getAppDatabase() {
		return AppDatabase;
	}

	public static UGCUploadConfig getAppConfig() {
		return AppConfig;
	}

	public static AccessDatabase getAccessDatabase() {
		return mAccessDatabase;
	}
}
