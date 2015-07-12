package cn.dezhisoft.cloud.mi.newugc.ioffer.ui.view;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ProgressBar;
import android.widget.Toast;

import cn.dezhisoft.cloud.mi.newugc.ioffer.common.Config;
import cn.dezhisoft.cloud.mi.newugc.ioffer.service.IofferDefine;
import cn.dezhisoft.cloud.mi.newugc.ioffer.service.IofferService;
import cn.dezhisoft.cloud.mi.newugc.ioffer.ui.view.ContentTypeAdapter.HolderView;
import cn.dezhisoft.cloud.mi.newugc.ugc.model.Category;
import cn.dezhisoft.cloud.mi.newugc.ugc.model.PackagePage;
import cn.dezhisoft.cloud.mi.newugc.ugc.model.PackageType;
import cn.dezhisoft.cloud.mi.newugc.ugc.ws.SoapResponse;

/**
 * 
 * @author Rosson Chen
 *
 */
public final class ContentTypeListView extends BaseLoadListView implements OnItemClickListener {
	
	static final String TAG	= "ContentTypeListView" ;
	
	Category category ;
	ContentTypeAdapter adapter ;
	SoapResponse response ;
	PackageType contentType ;
	ProgressBar progressBar ;
	
	public ContentTypeListView(Context context) {
		this(context, null);
	}
	
	public ContentTypeListView(Context context, AttributeSet attrs) {
		super(context, attrs);
		adapter  = new ContentTypeAdapter(context);
		response = new SoapResponse(handler) ;
		
		setAdapter(adapter);
		setOnItemClickListener(this);
		setOnScrollListener(this);
		uniteListView(this);
		setIdleState();
	}
	
	public void setProgressBar(ProgressBar bar){
		progressBar	= bar ;
	}
	
	public void setCategory(Category category){
		this.category = category ;
	}
	
	public final void onReload(){
		
		if(category == null){
			Log.e(TAG, "category is null, Please Call setCategory() method");
			return ;
		}
		
		clear() ;
		
		final IofferService service = IofferService.getNewTipService() ;
		final String keyWord		= service.CategorySearch.getKeyWord() ;
		
		if(category == service.CategorySearch){
			service.searchContentType(Config.FIRST_PAGE, keyWord, response);
		} else {
			service.queryPackage(category, Config.FIRST_PAGE,response);
		}
	}
	
	public void clear(){

		if(category != null){
			category.getPackageTypes().clear() ;
		}
		
		adapter.clearProgramList();
	}
	
	@Override
	public ContentTypeAdapter getAdapter() {
		return adapter ;
	}

	@Override
	protected Handler getMessageHandler() {
		return new Handler(){

			@Override
			public void dispatchMessage(Message msg) {
				switch(msg.what){
				case SoapResponse.SoapRequestMessage.MSG_START_SOAP :
					if(progressBar != null) progressBar.setVisibility(View.VISIBLE);
					break ;
				case SoapResponse.SoapRequestMessage.MSG_END_SOAP :
					if(progressBar != null) progressBar.setVisibility(View.GONE);
					break ;
				case IofferDefine.MSG_QUERY_CATEGORY_SUCCESS :
				case IofferDefine.MSG_SEARCH_CONTENT_SUCCESS :
				case IofferDefine.MSG_QUERY_RELATED_CONTENT_SUCCESS :
					notifyAdapterChange(true);
					break;
				case IofferDefine.MSG_SEARCH_CONTENT_FAILED :
				case IofferDefine.MSG_QUERY_CATEGORY_FAILED :
				case IofferDefine.MSG_QUERY_RELATED_CONTENT_FAILED :
					notifyAdapterChange(false);
					Toast.makeText(context, msg.obj != null ? msg.obj.toString() : "", Toast.LENGTH_SHORT).show() ;
					break ;
				}
			}
			
		};
	}

	private void notifyAdapterChange(boolean change){
		
		if(category != null){
		
			int pageIndex 	= category.getPackageTypes().getPageIndex() ;
			int total 		= category.getPackageTypes().getPageCount() ;
			
			adapter.showMore(pageIndex < total);
			
			if(change) {
				loadContentTypeList(category.getPackageTypes());
			}
		}
		// set idle state
		setIdleState();
	}
	
	public void loadContentTypeList(PackagePage content){
		adapter.loadProgramList(content);
	}
	
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,long id) {
		
		// view
		HolderView holder = (HolderView)view.getTag() ;
		
		if(holder != null && holder.content_type_load.getVisibility() == View.VISIBLE){
			// load next page
			loadNextPage();
		} 
		// callback to view
		else if(listener != null){
			
			listener.onClick(parent, view, adapter.getItem(position)) ;
		}
	}
	
	private void loadNextPage(){
		
		final IofferService service = IofferService.getNewTipService() ;
		final String keyWord		= service.CategorySearch.getKeyWord() ;
		final PackagePage contents 		= category.getPackageTypes() ;
		
		if(contents.getPageIndex() < contents.getPageCount()){
			if(category == service.CategorySearch){
				service.searchContentType(contents.getPageIndex() + 1, keyWord, response);
			} else {
				service.queryPackage(category, contents.getPageIndex() + 1,response);
			}
		}
	}
}
