package cn.dezhisoft.cloud.mi.newugc.ioffer.upload.view;

import java.util.ArrayList;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import cn.dezhisoft.cloud.mi.newugc.R;

import cn.dezhisoft.cloud.mi.newugc.ugc.model.Category;

/**
 * 
 * @author Rosson Chen
 * 
 */
public final class CatalogAdapter extends BaseIofferAdapter {

	ArrayList<Category> list;
	
	final ArrayList<View> BntList = new ArrayList<View>();
	private int selected ;

	public CatalogAdapter(Context context) {
		super(context);
		selected = 0 ;
	}

	@Override
	public int getCount() {
		return list != null ? list.size() : 0;
	}

	@Override
	public Category getItem(int position) {
		return list != null ? list.get(position) : null;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View item = null ;
		// view
		if(convertView == null){
			convertView = inflater.inflate(R.layout.ugc_catalog_item_layout, null);
			BntList.add(convertView);
		}
		//
		item = convertView.findViewById(R.id.catalog_item_layout);
		if(position == 0) {
			item.setBackgroundResource(R.drawable.ugc_ic_ioffer_level_start_bg);
		} else if(position == getCount() - 1){
			item.setBackgroundResource(R.drawable.ugc_ic_ioffer_level_end_bg);
		} else {
			item.setBackgroundResource(R.drawable.ugc_ic_ioffer_level_middle);
		}
		//
		TextView name 	= (TextView)convertView.findViewById(R.id.catalog_name);
		TextView check	= (TextView)convertView.findViewById(R.id.bnt_catalog_select) ;
		name.setText(getItem(position).getCatalogName());
		// 默认选择
		if(selected == position){
			check.setBackgroundResource(R.drawable.ugc_ic_ioffer_level_selected);
		} else {
			check.setBackgroundResource(R.drawable.ugc_ic_ioffer_level_default);
		}
		//
		return convertView;
	}
	
	public void updateSelect(View v){
		final ArrayList<View> list = BntList ;
		for(View item : list){
			TextView select = (TextView)item.findViewById(R.id.bnt_catalog_select) ;
			if(item.equals(v)){
				select.setBackgroundResource(R.drawable.ugc_ic_ioffer_level_selected);
			} else {
				select.setBackgroundResource(R.drawable.ugc_ic_ioffer_level_default);
			}
		}
	}
	
	public void setDefaultSelected(Category category){
		if(list != null){
			for(int index = 0 , size = list.size() ; index < size ; index++){
				Category cate = list.get(index);
				if(cate.getCatalogId().equals(category.getCatalogId())){
					selected	= index ;
					break ;
				}
			}
			
			notifyDataSetInvalidated() ;
		}
	}
	
	public Category getDefaultSelected(){
		return list != null ? list.get(selected) : null ;
	}

	public void loadCatalogList(ArrayList<Category> clist) {
		if (clist != null && clist.size() > 0) {
			list = clist;
			notifyDataSetChanged();
		}
	}
}
