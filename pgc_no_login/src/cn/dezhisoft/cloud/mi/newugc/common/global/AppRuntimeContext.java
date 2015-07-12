package cn.dezhisoft.cloud.mi.newugc.common.global;

import android.content.Context;

public class AppRuntimeContext {
	private static Context context;

	public static Context getContext() {
		return context;
	}

	public static void setContext(Context context) {
		AppRuntimeContext.context = context;
	}
}
