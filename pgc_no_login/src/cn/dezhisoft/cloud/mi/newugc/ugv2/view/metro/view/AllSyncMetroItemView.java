package cn.dezhisoft.cloud.mi.newugc.ugv2.view.metro.view;

import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View.OnTouchListener;
import cn.dezhisoft.cloud.mi.newugc.R;

public class AllSyncMetroItemView extends MetroItemView {

	public AllSyncMetroItemView(LayoutInflater inflater, int rowspan,
			int colspan, Drawable backgroundDrawable, int iconDrawable, int textTitle,
			int textAnimation, OnTouchListener Listener) {
		super(inflater, rowspan, colspan, backgroundDrawable, iconDrawable,
				textTitle, textAnimation, Listener);
	}
	

	protected int getLayoutId() {
		return R.layout.lesync_metro_allsync_item_view;
	}

}
