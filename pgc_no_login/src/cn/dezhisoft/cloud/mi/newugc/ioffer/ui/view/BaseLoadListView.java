package cn.dezhisoft.cloud.mi.newugc.ioffer.ui.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;

/**
 * 
 * @author Rosson Chen
 * 
 */
public abstract class BaseLoadListView extends BaseListView implements OnScrollListener{
	
	static final int STATE_IDLE		= 1 << 1 ;
	static final int STATE_LOAD		= 1 << 2 ;
	private int state ;
	
	public BaseLoadListView(Context context) {
		this(context, null);
	}

	public BaseLoadListView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}
	
	@Override
	public final void onScrollStateChanged(AbsListView view, int scrollState) {
		if (scrollState == SCROLL_STATE_IDLE 
				&& view.getLastVisiblePosition() == view.getCount() - 1){ 
			
			if((state & 0xFF) == STATE_IDLE){
				
				state = STATE_LOAD ;
				
				onLoad();
			}
        }  
	}

	@Override
	public final void onScroll(AbsListView view, int firstVisibleItem,int visibleItemCount, int totalItemCount) {
		
	}
	
	protected final void setIdleState(){
		state = STATE_IDLE ;
	}
	
	/** 滑动加载 */
	protected void onLoad(){
		
	}
}
