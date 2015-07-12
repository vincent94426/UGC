package cn.dezhisoft.cloud.mi.newugc.ugv2.activity;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.hardware.Camera;
import android.hardware.Camera.Size;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import cn.dezhisoft.cloud.mi.newugc.R;
import cn.dezhisoft.cloud.mi.newugc.common.util.DebugUtil;
import cn.dezhisoft.cloud.mi.newugc.common.util.SettingUtil;
import cn.dezhisoft.cloud.mi.newugc.ioffer.config.VideoSize;
import cn.dezhisoft.cloud.mi.newugc.ugv2.base.app.Ugv2Application;
import cn.dezhisoft.cloud.mi.newugc.ugv2.constants.UgV2Constants;

public class LaunchActivity extends Activity {
	public static final String AUTO_CREATED_SHORTCUT = "auto_created_shorcut";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ugv2_launch_layout);
		
		DebugUtil.setUncaughtExceptionHandler();
		
		new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                boolean createdShortCut = SettingUtil.readBoolean(LaunchActivity.this.getApplicationContext(), AUTO_CREATED_SHORTCUT, false);
                if( !createdShortCut ){
                    //add shortcut to desktop
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                          	addShortcut();
                          	SettingUtil.readBoolean(LaunchActivity.this.getApplicationContext(), AUTO_CREATED_SHORTCUT, true);
                          	checkAndSaveVideoSize();
                        }
                    }).run();
                }
                
                startMainUI();
                finish();
            }
        }, 1000);
	}
	
	private void startMainUI(){
        Intent mainIntent = new Intent(this, LoginActivity.class);
        startActivity(mainIntent);
	}
	
	/** 加载摄像头支持的分辨率 */
	private List<Size> loadCameraSupportSize(){
		
		List<Size> list = null ;
		try{
			Camera camera = Camera.open() ;
			list = camera.getParameters().getSupportedVideoSizes();
			if (list == null) {
				list = camera.getParameters().getSupportedPreviewSizes();
			}
			if(list.size() > 0){
				// 排序
				Collections.sort(list, new Comparator<Camera.Size>() {
					
					@Override
					public int compare(Camera.Size size1, Camera.Size size2) {
						if(size2.width < size1.width 
								|| (size2.width == size1.width && size2.height < size1.height))
							return 1;
						else if(size2.width == size1.width)
							return 0 ;
						else 
							return -1 ;
					}
				});
			}
			camera.release() ;
			camera = null ;
		} catch(Exception e){
			e.printStackTrace() ;
		}
		
		return list ;
	}
	
	private void checkAndSaveVideoSize() {
		boolean checkflag = SettingUtil.readBoolean(getApplicationContext(), UgV2Constants.KEY_CHECK_FLAG, false);
		if(checkflag){
			return ;
		}
		
		List<Size> cameraSizeList = loadCameraSupportSize();
		if (cameraSizeList == null){
			return;
		}
		// 录制
		for (Size size : cameraSizeList) {
			VideoSize vs = new VideoSize();
			vs.setWidth(size.width);
			vs.setHeight(size.height);
			vs.setType(VideoSize.Type.RECORDER);
			Ugv2Application.getAccessDatabase().saveObject(vs);
		}
		SettingUtil.writeBoolean(getApplicationContext(), UgV2Constants.KEY_CHECK_FLAG, true);
	}

	protected boolean addShortcut() {
    	try {
	        if(!isShortCutExists()) {
	            Intent shortcut = new Intent();
	
	            shortcut.putExtra(Intent.EXTRA_SHORTCUT_NAME, getString(R.string.ugv2_app_name));
	            shortcut.putExtra("duplicate", false); //不允许重复创建
	            shortcut.putExtra(Intent.EXTRA_SHORTCUT_INTENT, getIntentShortcut());
	            shortcut.putExtra(Intent.EXTRA_SHORTCUT_ICON_RESOURCE, Intent.ShortcutIconResource.fromContext(this, R.drawable.ic_launcher));
	            shortcut.setAction("com.android.launcher.action.INSTALL_SHORTCUT");
	            sendOrderedBroadcast(shortcut,null);
	            return true;
	        }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    protected Intent getIntentShortcut() {
        Intent i = new Intent(Intent.ACTION_MAIN);
        i.setComponent(getComponentName());
        i.addCategory(Intent.CATEGORY_LAUNCHER);
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        i.addFlags(Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);

        return i;
    }

    protected boolean isShortCutExists() {
        Cursor c = null;
        try {
            ContentResolver cr = this.getContentResolver();
            String AUTHORITY;
            if(Build.VERSION.SDK_INT > 8) {
            	AUTHORITY = "com.android.launcher2.settings";
            } else {
            	AUTHORITY = "com.android.launcher.settings";
            }
            Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/favorites?notify=true");
            c = cr.query(CONTENT_URI, null, "title = '" + getString(R.string.ugv2_app_name) + "'",null, null);
            return (c != null && c.getCount() > 0);
        } finally {
            if(c != null) {
                c.close();
            }
        }
    }

}
