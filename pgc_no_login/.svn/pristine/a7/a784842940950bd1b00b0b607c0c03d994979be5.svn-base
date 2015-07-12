package cn.dezhisoft.cloud.mi.newugc.ioffer.ui;

import android.os.Bundle;
import android.os.Handler;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ProgressBar;
import android.widget.TextView;

import cn.dezhisoft.cloud.mi.newugc.R;

import cn.dezhisoft.cloud.mi.newugc.ioffer.service.IofferService;
import cn.dezhisoft.cloud.mi.newugc.ioffer.ui.view.ContentTypeRelatedListView;
import cn.dezhisoft.cloud.mi.newugc.ioffer.ui.view.OnListItemClick;
import cn.dezhisoft.cloud.mi.newugc.ugc.model.PackageType;

/**
 * 相关素材列表: 素材的相关素材是根据指定素材的KeyWord来进行搜索
 * 
 * @author Rosson Chen
 *
 */
public final class ContentTypeRelatedActivity extends BaseActivity {

	ContentTypeRelatedListView list ;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.related_content_type_layout) ;
		
		PackageType content = mIofferService.getContentTypeRelated();
		
		TextView title 	= (TextView)findViewById(R.id.ioffer_related_title);
		ProgressBar bar = (ProgressBar) findViewById(R.id.wait_progress_bar);
		list			= (ContentTypeRelatedListView)findViewById(R.id.related_content_type_list);
		
		title.setText(getString(R.string.label_related_title));
		
		list.setContentType(content);
		list.setProgressBar(bar);
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
		// 加载
		list.onReload() ;
	}

	@Override
	protected Handler getMessageHandler() {
		return super.getMessageHandler();
	}

	public void buttonOnclick(View v){
		final int id = v.getId() ;
		if(id == R.id.bnt_related_back){
			getActivityManager().backPrevious() ;
		}
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		return false;
	}
}
