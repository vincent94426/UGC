package cn.dezhisoft.cloud.mi.newugc.ioffer.upload.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.widget.BaseAdapter;

/**
 * 
 * @author Rosson Chen
 *
 */
public abstract class BaseIofferAdapter extends BaseAdapter{

	protected Context context ; 
	protected LayoutInflater inflater ;
	
	public BaseIofferAdapter(Context context){
		inflater = LayoutInflater.from(context);
		this.context = context ;
	}
}
