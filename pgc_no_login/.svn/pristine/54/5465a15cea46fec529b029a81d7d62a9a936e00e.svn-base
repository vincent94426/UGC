package cn.dezhisoft.cloud.mi.newugc.ioffer.common;

import java.util.ArrayList;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import cn.dezhisoft.cloud.mi.newugc.ioffer.config.AppConfig;

import com.sobey.sdk.db.DatabaseManager;
import com.sobey.sdk.db.IDBManager;
import com.sobey.sdk.db.SQLiteUpdateListener;

/**
 * 数据库统一访问接口
 * 
 * @author Rosson Chen
 *
 */
public final class AccessDatabase {
	
	private static AccessDatabase _instance ;
	/** Sobey Database Interface*/
	private static IDBManager AppDatabase ;
	
	private AccessDatabase(Context context){
		
		if(context == null)
			throw new RuntimeException("AccessDatabase Context is null");
		// 初始数据
		DatabaseManager.initDBManager(context,1,new SQLiteUpdateListener() {
			
			@Override
			public void updateDatabase(SQLiteDatabase db,IDBManager manager,int oldVersion, int newVersion) {
				// 保留以前配置数据
				//ArrayList<Object> list = manager.queryAllObject(db, AppConfig.class);
				// 删除表
				//manager.deleteTable(db, AppConfig.class);
				// 保存数据
				//manager.saveObjectList(db,list);
			}
		});
		
		AppDatabase = DatabaseManager.getDatabaseManager();
	}
	
	public static void initAccessDatabase(Context context){
		if(_instance == null) 
			_instance = new AccessDatabase(context);
	}
	
	public static AccessDatabase getAccessDatabase(){
		return _instance ;
	}
	
	/** 保存对象  */
	public int saveObject(Object obj){
		return AppDatabase.saveObject(obj);
	}
	
	/** 保存或更新对象  */
	public int saveOrUpdataObject(Object obj){
		return AppDatabase.saveOrUpdataObject(obj) ;
	}
	
	/** 更新对象 */
	public int updateObject(Object obj){
		return AppDatabase.updateObject(obj) ;
	}
	
	/** 查询所有对象  */
	public ArrayList<Object> queryAllObject(Class<?> objClass){
		return AppDatabase.queryAllObject(objClass) ;
	}
	
	/** 按条件查询对象  */
	public ArrayList<Object> queryAllObject(Class<?> objClass,String[] columns,String[] selectionArgs){
		return AppDatabase.queryAllObject(objClass,columns,selectionArgs) ;
	}
	
	/** 得到应用程序配置文件 */
	public final AppConfig getConfig() {
		
		ArrayList<Object> list = AppDatabase.queryAllObject(AppConfig.class);
		if (list != null && list.size() > 0) {
			return (AppConfig) list.get(0);
		} else {
			
			AppConfig config = new AppConfig();
			config.setAuto(Config.FLGA_BUTTON_ON);
			config.setHost(Config.DEFAULT_HOST);
			config.setSiteId(Config.DEFAULT_SITE);
			config.setSiteName("iUGC");
			config.setPassword("");
			
			AppDatabase.saveObject(config) ;
			
			return config;
		}
	}
	
	/** 更新自动登录设置 */
	public final void updateAutoLoginUser(boolean auto,String uname,String password){
		
		final AppConfig config = getConfig() ;
		
		if(config.getAuto() == Config.FLGA_BUTTON_ON){
			config.setName(uname);
			config.setPassword(password);
			
			if(!auto) config.setAuto(Config.FLGA_BUTTON_OFF) ;
			
			AppDatabase.updateObject(config);
		}
	}
	
	/** 是否检查版本 */
	public final void updateCheckVersion(int flag){
		final AppConfig config = getConfig() ;
		if(config.getCheckVersion() != flag){
			config.setCheckVersion(flag);
			AppDatabase.updateObject(config);
		}
	}
}
