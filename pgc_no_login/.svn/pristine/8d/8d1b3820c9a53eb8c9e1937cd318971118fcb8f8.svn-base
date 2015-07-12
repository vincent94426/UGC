package cn.dezhisoft.cloud.mi.newugc.ioffer.ui.view;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.widget.BaseAdapter;

import cn.dezhisoft.cloud.mi.newugc.ugc.ws.publish.DownResourceCallback;

/**
 * 
 * @author Rosson Chen
 *
 */
public abstract class BaseIofferAdapter extends BaseAdapter{

	protected Context context ; 
	protected LayoutInflater inflater ;
	protected boolean showMore ;
	
	public BaseIofferAdapter(Context context){
		inflater 		= LayoutInflater.from(context);
		this.context 	= context ;
		showMore		= false ;
	}
	
	protected final void showMore(boolean hasMore){
		showMore	= hasMore ;
	}
	
	protected void updateIcon(String url){
		
	}
	
	/** */
	protected final Handler handler = new Handler(){

		@Override
		public void dispatchMessage(Message msg) {
			if(msg.what == DownResourceCallback.MSG_DOWNLOAD_BITMAP_SUCCESS){
				updateIcon(msg.obj != null ? msg.obj.toString() : "") ;
			}
		}
	} ;
}
