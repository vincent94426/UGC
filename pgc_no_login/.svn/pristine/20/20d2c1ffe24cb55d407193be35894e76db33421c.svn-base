package cn.dezhisoft.cloud.mi.newugc.ugv2.base.app;

import java.util.ArrayList;
import java.util.List;

public final class AppUtils {
	private static List<AppStateListener> stateListeners = new ArrayList<AppStateListener>();
	
	public static synchronized void registerAppStateListener(AppStateListener listener){
		if(null != listener && !stateListeners.contains(listener)){
			stateListeners.add(listener);
		}
	}
	
	public static synchronized void unregisterAppStateListener(AppStateListener listener){
		if(null != listener){
			stateListeners.remove(listener);
		}
	}
	
	public static synchronized void notifyAppStateListener(){
		for(AppStateListener listener : stateListeners){
			listener.onExit();
		}
	}
}
