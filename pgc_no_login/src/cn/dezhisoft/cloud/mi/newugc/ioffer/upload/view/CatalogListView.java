package cn.dezhisoft.cloud.mi.newugc.ioffer.upload.view;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.widget.ListView;

import cn.dezhisoft.cloud.mi.newugc.R;

import cn.dezhisoft.cloud.mi.newugc.ugc.model.Category;

/**
 * 
 * @author Rosson Chen
 *
 */
public final class CatalogListView extends ListView{
	
	CatalogAdapter adapter ;
	
	int mode ;
	
	public CatalogListView(Context context) {
		this(context, null);
	}
	
	protected final void uniteListView(ListView list) {
		list.setDividerHeight(2);
		list.setDivider(getResources().getDrawable(R.drawable.ugc_ic_ioffer_divide_line));
		list.setCacheColorHint(Color.TRANSPARENT);
	}

	public CatalogListView(Context context, AttributeSet attrs) {
		super(context, attrs);
		adapter = new CatalogAdapter(context);
		setAdapter(adapter);
		uniteListView(this);
	}

	public void loadCatalogList(ArrayList<Category> list){
		adapter.loadCatalogList(list);
	}

	@Override
	public CatalogAdapter getAdapter() {
		return adapter;
	}
}
