package cn.dezhisoft.cloud.mi.newugc.ugv2.view.adapter;

import java.util.List;
import java.util.Map;

import android.content.Context;
import android.database.DataSetObserver;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SimpleAdapter;

public class CategorySpinnerAdapter extends SimpleAdapter {

	public CategorySpinnerAdapter(Context context,
			List<? extends Map<String, ?>> data, int resource, String[] from,
			int[] to) {
		super(context, data, resource, from, to);
	}

}
