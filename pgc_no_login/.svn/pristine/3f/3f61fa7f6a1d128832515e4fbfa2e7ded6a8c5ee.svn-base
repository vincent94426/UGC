package cn.dezhisoft.cloud.mi.newugc.ioffer.exception;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.Thread.UncaughtExceptionHandler;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

/**
 * 异常处理
 * 
 * @author Rosson Chen
 *
 */
public final class ExceptionHandler implements UncaughtExceptionHandler {
	
	static final String TAG	= "iUGCCrashHandler" ;
	private Context context;

	public ExceptionHandler(Context context) {
		this.context = context;
	}

	@Override
	public void uncaughtException(Thread thread, Throwable ex) {
		
		Log.e(TAG, "ERROR: " + context.getPackageName());
		
		StringWriter sw = new StringWriter();
		ex.printStackTrace(new PrintWriter(sw));
		String error = sw.toString() ;
		
		Log.e(TAG, error);
		
		Intent intent = new Intent(context, ExceptionReportActivity.class);  
		intent.putExtra(ExceptionReportActivity.exceptionMsg, error);  
        context.startActivity(intent);  
        
		android.os.Process.killProcess(android.os.Process.myPid());
		System.exit(10);
	}
}
