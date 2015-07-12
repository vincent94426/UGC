package cn.dezhisoft.cloud.mi.newugc.ugv2.view.metro.view;

import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;

public class MetroItemLayout extends FrameLayout {
	
	public static final String TAG = "MetroItemLayout";
	private OnMetroItemTouchListener mOnMetroItemTouchListener;
	
	public MetroItemLayout(Context context) {
		super(context);
		clearFocus();
	}

	public MetroItemLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
		clearFocus();
	}
	
	

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		final int action = event.getAction();
		switch (action) {
		case MotionEvent.ACTION_DOWN:
			Log.v(TAG, "MetroItemLayout action down.");
			break;
		case MotionEvent.ACTION_MOVE:
			Log.v(TAG, "MetroItemLayout action move.");
			return false;
		case MotionEvent.ACTION_UP:
			clearFocus();
			Log.v(TAG, "MetroItemLayout action up.");
			break;
		}
		clearFocus();
		return true;
	}

	@Override
	public boolean onInterceptTouchEvent(MotionEvent ev) {
		final int action = ev.getAction();
		switch (action) {
		case MotionEvent.ACTION_DOWN:
			Log.v(TAG, "MetroItemLayout onInterceptTouchEvent action down.");
			break;
		case MotionEvent.ACTION_MOVE:
			Log.v(TAG, "MetroItemLayout onInterceptTouchEvent action move.");
			break;
		case MotionEvent.ACTION_UP:
			Log.v(TAG, "MetroItemLayout onInterceptTouchEvent action up.");
			break;
		}
		return false;
	}
	
	
	public void setOnMetroItemTouchListener(OnMetroItemTouchListener mOnMetroItemTouchListener){
		this.mOnMetroItemTouchListener = mOnMetroItemTouchListener;
	}
	
	public interface OnMetroItemTouchListener{
		public void onItemTouch(View v);
	}
	
	
}
