package cn.dezhisoft.cloud.mi.newugc.ioffer.ui;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ProgressBar;

import cn.dezhisoft.cloud.mi.newugc.R;

import cn.dezhisoft.cloud.mi.newugc.ioffer.common.Util;
import cn.dezhisoft.cloud.mi.newugc.ioffer.service.IofferService;
import cn.dezhisoft.cloud.mi.newugc.ioffer.ui.view.ContentTypeListView;
import cn.dezhisoft.cloud.mi.newugc.ioffer.ui.view.OnListItemClick;
import cn.dezhisoft.cloud.mi.newugc.ugc.model.Category;
import cn.dezhisoft.cloud.mi.newugc.ugc.model.PackageType;

/**
 * Search
 * 
 * @author Rosson Chen
 * 
 */
public final class SearchActivity extends BaseActivity {

	ContentTypeListView list ;
	ProgressBar loadingBar ;
	EditText input ;
	PackageType contentType ;
	
	final Category search = mIofferService.CategorySearch ;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ioffer_search_layout);
		
		loadingBar	= (ProgressBar)findViewById(R.id.wait_progress_bar) ;
		list		= (ContentTypeListView)findViewById(R.id.search_content_list);
		input		= (EditText)findViewById(R.id.edit_search_input);
		
		list.setCategory(mIofferService.CategorySearch);
		list.setProgressBar(loadingBar);
		list.setOnListItemClick(new OnListItemClick() {
			
			@Override
			public void onClick(AdapterView<?> parent, View view, Object item) {
				
				final IofferService service = IofferService.getNewTipService() ;
				
				PackageType contentType = (PackageType)item ;
				service.setCurrentPackageType(contentType) ;
				// 界面跳转
				getActivityManager().pushActivity(ContentTypeDetailActivity.class);
			}
		}) ;
		
		// 加载数据
		input.setText(search.getKeyWord());
		list.loadContentTypeList(search.getPackageTypes());
	}
	
	public void buttonOnclick(View v){
		int id = v.getId() ;
		switch(id){
		case R.id.bnt_search_back :
			getActivityManager().backPrevious() ;
			break ;
		case R.id.bnt_search_go :
			
			Util.hideVirtualKeyPad(mContext, input);
			
			String key	= input.getEditableText().toString().trim() ;
			
			if(key.equals("")){
				showMessageDialog(getString(R.string.label_search_warring));
				return ;
			}
			// 设置搜索关键字
			search.setKeyWord(key);
			// 加载数据
			list.onReload() ;
			break ;
		}
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		return false;
	}

}
