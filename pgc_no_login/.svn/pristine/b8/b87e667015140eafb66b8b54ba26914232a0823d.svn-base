package cn.dezhisoft.cloud.mi.newugc.ugv2.view.metro.model;

import android.view.View;
import cn.dezhisoft.cloud.mi.newugc.ugv2.view.metro.view.MetroItemLayout;

public class AutoFillMetroItem {

	protected MetroItemLayout mMetroView = null;

	private int mRowSpan = 1;
	private int mColSpan = 1;

	/**
	 * @param v
	 * @param rowspan
	 * @param colspan
	 */
	public AutoFillMetroItem(MetroItemLayout v, int rowspan, int colspan) {
		mMetroView = v;

		if (rowspan < 1)
			throw new IllegalArgumentException("rowspan < 1");
		mRowSpan = rowspan;

		if (colspan < 1)
			throw new IllegalArgumentException("colspan < 1");
		mColSpan = colspan;
	}

	public View getMetroView() {
		return mMetroView;
	}

	public int getRowSpan() {
		return mRowSpan;
	}

	public int getColSpan() {
		return mColSpan;
	}

	public void setmMetroView(MetroItemLayout mMetroView) {
		this.mMetroView = mMetroView;
	}

	public void setmRowSpan(int mRowSpan) {
		this.mRowSpan = mRowSpan;
	}

	public void setmColSpan(int mColSpan) {
		this.mColSpan = mColSpan;
	}
	
}
