package cn.dezhisoft.cloud.mi.newugc.ioffer.ui.view;

import android.content.Context;
import android.graphics.Color;
import android.os.Handler;
import android.util.AttributeSet;
import android.widget.ListView;
import cn.dezhisoft.cloud.mi.newugc.R;

import com.sobey.android.ui.ActivityManager;

public abstract class BaseListView extends ListView {
	
	/** handler*/
	protected Handler handler ;
	protected Context context ;
	protected OnListItemClick listener ;
	
	public BaseListView(Context context) {
		this(context, null);
	}

	public BaseListView(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.context = context ;
		handler	= getMessageHandler();
	}
	
	public final void setOnListItemClick(OnListItemClick l){
		listener	= l ;
	}
	
	// message handler
	protected Handler getMessageHandler(){
		return new Handler() ;
	}

	protected final void uniteListView(ListView list) {
		list.setDividerHeight(2);
		list.setDivider(getResources().getDrawable(R.drawable.ic_ioffer_divide_line));
		list.setCacheColorHint(Color.TRANSPARENT);
	}

	protected final ActivityManager getActivityManager() {
		return ActivityManager.getActivityManager();
	}
}
