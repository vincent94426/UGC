package cn.dezhisoft.cloud.mi.newugc.ioffer.ui;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import cn.dezhisoft.cloud.mi.newugc.R;

import cn.dezhisoft.cloud.mi.newugc.ioffer.common.Config;
import cn.dezhisoft.cloud.mi.newugc.ioffer.service.IofferService;
import cn.dezhisoft.cloud.mi.newugc.ioffer.ui.view.ContentTypeListView;
import cn.dezhisoft.cloud.mi.newugc.ioffer.ui.view.OnListItemClick;
import cn.dezhisoft.cloud.mi.newugc.ioffer.upload.ui.UGCBaseActivity;
import cn.dezhisoft.cloud.mi.newugc.ugc.model.PackagePage;
import cn.dezhisoft.cloud.mi.newugc.ugc.model.PackageType;

/**
 * 
 * @author Rosson Chen
 *
 */
public final class TabContentActivity extends BaseActivity {
	
	final static String 	TAG			= "TabContentActivity" ;
	Button bnt_hot, bnt_rank,bnt_category;
	FrameLayout parent_content ;
	RelativeLayout hot_content ,category_content ;
	ContentTypeListView hotCategoryList,clickCategoryList;
	ImageButton bnt_reload ;
	ProgressBar waitProgress ;
	int flag_button ;
	
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.tab_content_layout);
		
		// by wei.zhou. fix bug
		Config.checkActivityStatus(this);
		
		bnt_hot		= (Button) findViewById(R.id.bnt_content_hot);
		bnt_rank	= (Button) findViewById(R.id.bnt_content_rank);
		bnt_category= (Button) findViewById(R.id.bnt_content_category);
		parent_content = (FrameLayout) findViewById(R.id.content_list_layout);
		bnt_reload	= (ImageButton)findViewById(R.id.bnt_content_reload);
		waitProgress = (ProgressBar) findViewById(R.id.wait_progress_bar);
		
		loadSubContentLayout();
		
		UGCBaseActivity.querySiteCategory() ;
	}

	private final void loadSubContentLayout(){
		hot_content		= (RelativeLayout)getLayoutInflater().inflate(R.layout.conent_hot_layout, null);
		hotCategoryList	= (ContentTypeListView) hot_content.findViewById(R.id.content_host_list);
		clickCategoryList= (ContentTypeListView) getLayoutInflater().inflate(R.layout.conent_rank_layout, null);
		category_content=  (RelativeLayout)getLayoutInflater().inflate(R.layout.conent_category_layout, null);
		
		hotCategoryList.setCategory(mIofferService.CategoryHot);
		clickCategoryList.setCategory(mIofferService.CategoryClick);
		hotCategoryList.setProgressBar(waitProgress);
		clickCategoryList.setProgressBar(waitProgress);
		
		hotCategoryList.setOnListItemClick(onClick);
		clickCategoryList.setOnListItemClick(onClick);
		
		hotCategoryList.clear() ;
		clickCategoryList.clear() ;
		
		updateButtonClick(R.id.bnt_content_hot);
	}
	
	public final void buttonOnclick(View v) {
		
		switch (v.getId()) {
		case R.id.bnt_content_hot:
		case R.id.bnt_content_rank:
		case R.id.bnt_content_category:
			updateButtonClick(v.getId());
			break;
		case R.id.content_category_video_layout :
			mIofferService.CategoryVideo.getPackageTypes().clear() ;
			mIofferService.setCurrentCategory(mIofferService.CategoryVideo);
			getActivityManager().pushActivity(CategoryDetailActivity.class);
			break ;
		case R.id.content_category_audio_layout :
			mIofferService.CategoryAudio.getPackageTypes().clear() ;
			mIofferService.setCurrentCategory(mIofferService.CategoryAudio);
			getActivityManager().pushActivity(CategoryDetailActivity.class);
			break ;
		case R.id.content_category_img_layout :
			mIofferService.CategoryImage.getPackageTypes().clear() ;
			mIofferService.setCurrentCategory(mIofferService.CategoryImage);
			getActivityManager().pushActivity(CategoryDetailActivity.class);
			break ;
		case R.id.bnt_content_reload :
			if(flag_button == R.id.bnt_content_hot){
				hotCategoryList.onReload() ;
			} else if(flag_button == R.id.bnt_content_rank){
				clickCategoryList.onReload() ;
			}
			break ;
		case R.id.bnt_content_search :
			mIofferService.CategorySearch.clear() ;
			getActivityManager().pushActivity(SearchActivity.class);
			break ;
		}
	}
	
	private final void updateButtonClick(int id){
		
		PackagePage content = null ;
		
		switch(id){
		case R.id.bnt_content_hot:
			bnt_hot.setBackgroundResource(R.drawable.ic_ioffer_content_hot_selected);
			bnt_rank.setBackgroundResource(R.drawable.ic_ioffer_content_rank_default);
			bnt_category.setBackgroundResource(R.drawable.ic_ioffer_content_category_default);
			
			parent_content.removeAllViews() ;
			parent_content.addView(hot_content);
			
			mIofferService.setCurrentCategory(mIofferService.CategoryHot);
			content	= mIofferService.CategoryHot.getPackageTypes() ;
			// 更新UI
			if(content.getPackageTypeList().isEmpty()){
				hotCategoryList.onReload() ;
			} else {
				hotCategoryList.loadContentTypeList(content);
			}
			
			// button id
			flag_button = R.id.bnt_content_hot ;
			bnt_reload.setVisibility(View.VISIBLE);
			break;
		case R.id.bnt_content_rank:
			bnt_hot.setBackgroundResource(R.drawable.ic_ioffer_content_hot_default);
			bnt_rank.setBackgroundResource(R.drawable.ic_ioffer_content_rank_selected);
			bnt_category.setBackgroundResource(R.drawable.ic_ioffer_content_category_default);
			
			parent_content.removeAllViews() ;
			parent_content.addView(clickCategoryList);
			
			mIofferService.setCurrentCategory(mIofferService.CategoryClick);
			content	= mIofferService.CategoryClick.getPackageTypes() ;
			
			// 更新UI
			if(content.getPackageTypeList().isEmpty()){
				clickCategoryList.onReload() ;
			} else {
				clickCategoryList.loadContentTypeList(content);
			}
			
			// button id
			flag_button = R.id.bnt_content_rank ;
			bnt_reload.setVisibility(View.VISIBLE);
			break;
		case R.id.bnt_content_category:
			bnt_hot.setBackgroundResource(R.drawable.ic_ioffer_content_hot_default);
			bnt_rank.setBackgroundResource(R.drawable.ic_ioffer_content_rank_default);
			bnt_category.setBackgroundResource(R.drawable.ic_ioffer_content_category_selected);
			
			parent_content.removeAllViews() ;
			parent_content.addView(category_content);
			// button id
			flag_button = R.id.bnt_content_category ;
			bnt_reload.setVisibility(View.INVISIBLE);
			break;
		}
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		
		hotCategoryList.clear() ;
		clickCategoryList.clear() ;
		mIofferService.CategoryHot.clear() ;
		mIofferService.CategoryClick.clear() ;
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		return false;
	}
	
	private final OnListItemClick onClick = new OnListItemClick() {
		
		@Override
		public void onClick(AdapterView<?> parent, View view, Object item) {
			
			if(item == null) return ;
			
			final IofferService service = IofferService.getNewTipService() ;
			
			PackageType contentType = (PackageType)item ;
			service.setCurrentPackageType(contentType) ;
			// 界面跳转
			getActivityManager().pushActivity(ContentTypeDetailActivity.class);
		}
	}; 
}
