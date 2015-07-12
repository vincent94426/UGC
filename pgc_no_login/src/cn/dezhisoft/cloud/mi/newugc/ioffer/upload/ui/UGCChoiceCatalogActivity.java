package cn.dezhisoft.cloud.mi.newugc.ioffer.upload.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.TextView;

import cn.dezhisoft.cloud.mi.newugc.R;

import cn.dezhisoft.cloud.mi.newugc.ioffer.upload.view.CatalogAdapter;
import cn.dezhisoft.cloud.mi.newugc.ioffer.upload.view.CatalogListView;
import cn.dezhisoft.cloud.mi.newugc.ugc.model.Category;

/**
 * 
 * @author Rosson Chen
 *
 */
public final class UGCChoiceCatalogActivity extends UGCBaseActivity {
	
	public static final int 	CHOICE_LEVEL_FIRST		= 1 ;
	public static final int 	CHOICE_LEVEL_SENCON		= CHOICE_LEVEL_FIRST + 1 ;
	
	public static final String  KEY_LEVEL				= "_level" ;
	public static final String 	KEY_RESULT				= "_result" ;
	
	int mode ;
	TextView title ;
	CatalogListView list ;
	
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ugc_catalog_layout);
		title		= (TextView) findViewById(R.id.level_content_title);
		list		= (CatalogListView) findViewById(R.id.catalog_list_view);
		
		int level = getIntent().getIntExtra(KEY_LEVEL, CHOICE_LEVEL_FIRST);
		if(level == CHOICE_LEVEL_FIRST){
			mode = CHOICE_LEVEL_FIRST ;
			title.setText(R.string.ugc_label_level_title_first);
			list.loadCatalogList(CategoryList);
			// Ĭ�Ϸ���
			if(rootCategory != null){
				list.getAdapter().setDefaultSelected(rootCategory);
			} else {
				rootCategory = CategoryList.get(0);
			}
		} else {
			mode = CHOICE_LEVEL_SENCON ;
			title.setText(R.string.ugc_label_level_title_second);
			list.loadCatalogList(rootCategory.getSubCategoryList());
		}
		// 
		list.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,int position, long id) {
				//
				final CatalogAdapter adapter = list.getAdapter() ;
				
				adapter.updateSelect(view);
				
				confirmSelected(adapter.getItem(position));
			}
		}) ;
	}
	
	public void buttonOnclick(View v){
		if(v.getId() == R.id.bnt_level_back){
			confirmSelected(list.getAdapter().getDefaultSelected());
		}
	}
	
	private final void confirmSelected(Category category){
		Intent intent = getIntent();
		intent.putExtra(KEY_RESULT, category);
		UGCChoiceCatalogActivity.this.setResult(mode, intent);
        UGCChoiceCatalogActivity.this.finish();
	}
}
