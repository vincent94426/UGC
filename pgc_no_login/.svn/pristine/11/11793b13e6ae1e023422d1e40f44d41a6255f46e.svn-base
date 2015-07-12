package cn.dezhisoft.cloud.mi.newugc.ioffer.ui;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import android.os.Bundle;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;

import cn.dezhisoft.cloud.mi.newugc.R;

import cn.dezhisoft.cloud.mi.newugc.ioffer.service.IofferService;
import cn.dezhisoft.cloud.mi.newugc.ioffer.ui.view.ContentTypeListView;
import cn.dezhisoft.cloud.mi.newugc.ioffer.ui.view.OnListItemClick;
import cn.dezhisoft.cloud.mi.newugc.ioffer.upload.ui.UGCBaseActivity;
import cn.dezhisoft.cloud.mi.newugc.ugc.model.Category;
import cn.dezhisoft.cloud.mi.newugc.ugc.model.PackagePage;
import cn.dezhisoft.cloud.mi.newugc.ugc.model.PackageType;

/**
 * 
 * @author Rosson Chen
 * 
 */
public final class CategoryDetailActivity extends BaseActivity {

	static final int ITEM_HEIGHT = 40;

	ContentTypeListView listView;
	TextView video_filter;
	Category category;
	PopupWindow popup_filter;
	LayoutInflater inflater;
	final List<String> catalogNames = new ArrayList<String>();
	final List<TextView> choiceTextViews = new ArrayList<TextView>();

	TextView allcatalogTextView;
	
	ProgressBar waitProgress ;

	boolean iscancel;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.catalog_content_type_layout);
		iscancel = false;
		listView = (ContentTypeListView) findViewById(R.id.catalog_program_list);
		TextView title 	= (TextView) findViewById(R.id.ioffer_catalog_title);
		video_filter 	= (TextView) findViewById(R.id.bnt_video_filter);
		category 		= mIofferService.getCurrentCategory() ;
		inflater 		= getLayoutInflater();
		waitProgress 	= (ProgressBar) findViewById(R.id.wait_progress_bar);
		
		catalogNames.clear();
		choiceTextViews.clear();
		
		video_filter.setVisibility(View.VISIBLE);
		loadPopupFilter();
		
		listView.setProgressBar(waitProgress);
		listView.setCategory(category);
		
		if (category.getPackageTypes().getPackageTypeList().size() == 0) {
			listView.onReload() ;
		} else {
			listView.loadContentTypeList(category.getPackageTypes());
		}
		
		listView.setOnListItemClick(new OnListItemClick() {
			
			@Override
			public void onClick(AdapterView<?> parent, View view, Object item) {
				
				final IofferService service = IofferService.getNewTipService() ;
				
				PackageType contentType = (PackageType)item ;
				service.setCurrentPackageType(contentType) ;
				// 界面跳转
				getActivityManager().pushActivity(ContentTypeDetailActivity.class);
			}
		}) ;
		
		title.setText(category.getCatalogName());
	}

	private void loadPopupFilter() {
		View root = inflater.inflate(R.layout.ioffer_popup_video_fliter, null);
		root.findViewById(R.id.bnt_filter_cancel).setOnClickListener(popup_filter_listener);
		root.findViewById(R.id.bnt_filter_confirm).setOnClickListener(popup_filter_listener);
		root.findViewById(R.id.filter_layout_22).setOnClickListener(popup_filter_listener);
		allcatalogTextView = (TextView) root.findViewById(R.id.video_catalog_item_choice1);
		//
		LinearLayout content = (LinearLayout) root.findViewById(R.id.filter_layout_catalog_content);
		ArrayList<Category> catalogs = UGCBaseActivity.getSiteCategory() ;
		//
		for (Category cata : catalogs) {
		
			View v = inflater.inflate(R.layout.ioffer_popup_video_parent_item,null);
			((TextView) v.findViewById(R.id.video_catalog_parent_title)).setText(cata.getCatalogName());
			content.addView(v, createLayoutParams(ITEM_HEIGHT));
			//
			content.addView(createLine(), createLayoutParams(2));
			//
			for (Category sub : cata.getSubCategoryList()) {
				View s = inflater.inflate(R.layout.ioffer_popup_video_sub_item,null);
				((TextView) s.findViewById(R.id.video_catalog_sub_title)).setText(sub.getCatalogName());
				choiceTextViews.add((TextView) s.findViewById(R.id.video_catalog_item_choice));
				s.setTag(sub);
				s.setOnClickListener(popup_filter_listener);
				content.addView(s, createLayoutParams(ITEM_HEIGHT));
				//
				content.addView(createLine(), createLayoutParams(2));
			}
		}
		
		popup_filter = new PopupWindow(root, getWindowManager()
				.getDefaultDisplay().getWidth(), getWindowManager()
				.getDefaultDisplay().getHeight());
	}
	
	private final View createLine() {
		View line = new View(this);
		line.setBackgroundResource(R.drawable.ic_ioffer_video_filter_line);
		return line;
	}

	private final LayoutParams createLayoutParams(int height) {
		return new LayoutParams(LayoutParams.FILL_PARENT, height);
	}

	public final void buttonOnclick(View v) {
		switch (v.getId()) {
		case R.id.bnt_catalog_back:
			mIofferService.previousCategory() ;
			getActivityManager().backPrevious();
			break;
		case R.id.bnt_video_filter:
			if (popup_filter != null && !popup_filter.isShowing()) {
				popup_filter.setAnimationStyle(R.anim.fllter_from_bottom_in);
				popup_filter.setFocusable(true);
				popup_filter.update();
				popup_filter.showAtLocation(getWindow().getDecorView(),Gravity.FILL, 0, 0);
			}
			break;
		}
	}

	private final void dismissPopupMenu() {
		if (popup_filter != null && popup_filter.isShowing()) {
			popup_filter.setAnimationStyle(R.anim.fliter_from_bottom_out);
			popup_filter.dismiss();
		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		return false;
	}

	final View.OnClickListener popup_filter_listener = new View.OnClickListener() {

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.bnt_filter_cancel:
				dismissPopupMenu();
				catalogNames.clear();
				allcatalogTextView.setVisibility(View.INVISIBLE);
				for(TextView tv : choiceTextViews) 
					tv.setVisibility(View.INVISIBLE);
				iscancel = true;
				break;
			case R.id.bnt_filter_confirm:
				
				dismissPopupMenu();
				
				PackagePage contentNew = new PackagePage();
				
				getContentByFilter(contentNew);
				
				listView.loadContentTypeList(contentNew);
				catalogNames.clear();
				allcatalogTextView.setVisibility(View.INVISIBLE);
				
				for(TextView tv : choiceTextViews) 
					tv.setVisibility(View.INVISIBLE);
				
				break;
			case R.id.ioffer_video_filter_item_layout:
				
				if (v.getTag() instanceof Category) {
					saveSelectCataNames(v);
				}
				break;
			case R.id.filter_layout_22:
				onButtonPopAllSort();
				break;
			}
		}
	};
	
	private void  onButtonPopAllSort(){
		if (allcatalogTextView.getVisibility() == View.VISIBLE) {
			allcatalogTextView.setVisibility(View.INVISIBLE);
			catalogNames.clear();
			for (TextView tv : choiceTextViews) {
				tv.setVisibility(View.INVISIBLE);
			}

		} else {
			allcatalogTextView.setVisibility(View.VISIBLE);
			catalogNames.clear();
			
			ArrayList<Category> catalogs = UGCBaseActivity.getSiteCategory() ;
			for (Category cata : catalogs) {
				for (Category sub : cata.getSubCategoryList()) {
					catalogNames.add(sub.getCatalogName());
				}
			}
			
			for (TextView tv : choiceTextViews) {
				tv.setVisibility(View.VISIBLE);
			}
		}
	}
	
	private void getContentByFilter(PackagePage contentNew) {
		
		ArrayList<PackageType> historyList = category.getPackageTypes().getPackageTypeList() ;
		ArrayList<PackageType> tempList    = contentNew.getPackageTypeList() ;
		
//		for (PackageType contentType : historyList) {
//			if (catalogNames.contains(contentType.getItemCatalog())) {
//				tempList.add(contentType);
//			}
//		}
	}

	private void saveSelectCataNames(View v) {
		Category catalog = (Category) v.getTag();
		TextView textView = (TextView) v.findViewById(R.id.video_catalog_item_choice);
		if (textView.getVisibility() == View.VISIBLE) {
			textView.setVisibility(View.INVISIBLE);
			Iterator<String> it = catalogNames.iterator();
			while (it.hasNext()) {
				String ctaName = it.next();
				if(ctaName.equals(catalog.getCatalogName())) {
				  catalogNames.remove(ctaName);
				  break;
				}
			}
		} else {
			textView.setVisibility(View.VISIBLE);
			String catalogName = catalog.getCatalogName();
			if(!catalogNames.contains(catalogName))
			catalogNames.add(catalogName);
		}
	}

}
