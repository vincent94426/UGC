package cn.dezhisoft.cloud.mi.newugc.ioffer.upload.ui;

import java.util.ArrayList;

import android.app.Activity;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import cn.dezhisoft.cloud.mi.newugc.R;

import com.sobey.sdk.db.DatabaseManager;
import com.sobey.sdk.db.IDBManager;
import cn.dezhisoft.cloud.mi.newugc.ugc.model.Category;
import cn.dezhisoft.cloud.mi.newugc.ugc.core.upload.IFileUpload;
import cn.dezhisoft.cloud.mi.newugc.ugc.core.ws.CWWebService;
import cn.dezhisoft.cloud.mi.newugc.ugc.ws.SoapResponse;
import cn.dezhisoft.cloud.mi.newugc.ugc.ws.UGCWebService;

/**
 * Base Activity
 * 
 * @author Rosson Chen
 *
 */
public class UGCBaseActivity extends Activity {
	
	protected static final String AppName			= "ugc_upload" ;
	protected static final int FLGA_BUTTON_OFF		= 0 ;
	protected static final int FLGA_BUTTON_ON		= 1 ;
	protected static final ArrayList<Category> CategoryList = new ArrayList<Category>();
	protected static UGCUploadConfig 	AppConfig ;
	protected static IDBManager AppDatabase ;
	protected static Category 	rootCategory ;
	protected static Category 	subCategory ;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		initConfig();
	}
	
	private final void initConfig(){
		
		if(!(AppDatabase == null || AppConfig == null)) return ;
		
		if(AppDatabase == null){
			DatabaseManager.initDBManager(this,1,null);
			AppDatabase		= DatabaseManager.getDatabaseManager() ;
		}
		
		if(AppConfig == null){
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
	}
	
	protected final void showHintDialog(int rid){
		showHintDialog(getString(rid));
	}
	
	protected final void showHintDialog(String message){
		new Builder(this).setTitle(R.string.ugc_warring_hint_title)
		.setMessage(message)
		.setPositiveButton(R.string.ugc_warring_hint_bnt_yes,null)
		.create()
		.show();
	}
	
	protected final void showListenerDialog(String message,OnClickListener l){
		new Builder(this).setTitle(R.string.ugc_warring_hint_title)
		.setMessage(message)
		.setPositiveButton(R.string.ugc_warring_hint_bnt_yes,l)
		.setNeutralButton(R.string.ugc_warring_hint_bnt_no, l)
		.create()
		.show();
	}
	
	protected final void showErrorListenerDialog(String message,OnClickListener l){
		new Builder(this).setTitle(R.string.ugc_warring_hint_title)
		.setMessage(message)
		.setPositiveButton(R.string.ugc_warring_hint_bnt_yes,l)
		.setCancelable(false)
		.create()
		.show();
	}
	
	protected final void sendQueryBroadcast(){
		Intent intent = new Intent() ;
		intent.setAction(IFileUpload.ACTION_QUERY_NEW_TASK);
		sendBroadcast(intent);
	}
	
	public static final void clearSiteCategory(){
		CategoryList.clear() ;
	}
	
	public static ArrayList<Category> getSiteCategory(){
		return CategoryList ;
	}
	
	public static final void querySiteCategory(){
		
		if(CategoryList.size() > 0) return ;
		
		String session = CWWebService.UserProxy.getUser().getSessionUID() ;
		String siteId  = UGCWebService.getSiteId() ;
		
		UGCWebService.SiteProxy.querySiteCategory(session,siteId,new SoapResponse(),CategoryList);
	}
}
