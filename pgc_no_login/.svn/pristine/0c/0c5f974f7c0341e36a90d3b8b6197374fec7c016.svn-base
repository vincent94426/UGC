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
import cn.dezhisoft.cloud.mi.newugc.ugc.core.model.ErrorMessage;
import cn.dezhisoft.cloud.mi.newugc.ugc.model.PackagePage;
import cn.dezhisoft.cloud.mi.newugc.ugc.model.PackageType;
import cn.dezhisoft.cloud.mi.newugc.ugc.ws.SoapResponse;

/**
 * 
 * @author Rosson Chen
 *
 */
public final class ContentTypeRelatedListView extends BaseLoadListView implements OnItemClickListener {

	static final String TAG = "ContentTypeRelatedListView";

	PackageType masterContent;
	ContentTypeAdapter adapter;
	SoapResponse response;
	ProgressBar progressBar;

	public ContentTypeRelatedListView(Context context) {
		this(context, null);
	}

	public ContentTypeRelatedListView(Context context, AttributeSet attrs) {
		super(context, attrs);
		adapter = new ContentTypeAdapter(context);
		response = new SoapResponse(handler);

		setAdapter(adapter);
		setOnItemClickListener(this);
		uniteListView(this);
		setIdleState();
	}

	public void setProgressBar(ProgressBar bar) {
		progressBar = bar;
	}

	public void setContentType(PackageType contentType) {
		this.masterContent = contentType;
	}

	public final void onReload() {

		if (masterContent == null) {
			Log.e(TAG, "ContentType is null, Please Call setCategory() method");
			return;
		}

		final IofferService service = IofferService.getNewTipService();
		final PackagePage related	= masterContent.getRelatedPackage() ;
		
		if(related.getPackageTypeList().isEmpty()){
			service.queryRelatedContentType(masterContent, Config.FIRST_PAGE, response);
		} else {
			loadContentTypeList(related);
		}
	}

	@Override
	public ContentTypeAdapter getAdapter() {
		return adapter;
	}

	@Override
	protected Handler getMessageHandler() {
		return new Handler() {

			@Override
			public void dispatchMessage(Message msg) {
				switch (msg.what) {
				case SoapResponse.SoapRequestMessage.MSG_START_SOAP:
					if (progressBar != null)
						progressBar.setVisibility(View.VISIBLE);
					break;
				case SoapResponse.SoapRequestMessage.MSG_END_SOAP:
					if (progressBar != null)
						progressBar.setVisibility(View.GONE);
					break;
				case IofferDefine.MSG_QUERY_RELATED_CONTENT_SUCCESS:
					notifyAdapterChange(true);
					break;
				case IofferDefine.MSG_QUERY_RELATED_CONTENT_FAILED:
					notifyAdapterChange(false);
					
					if(msg.obj instanceof ErrorMessage){
						Toast.makeText(context, ((ErrorMessage)msg.obj).getMessage(), Toast.LENGTH_SHORT).show() ;
					}
					
					break;
				}
			}

		};
	}

	private void notifyAdapterChange(boolean change) {

		if (masterContent != null) {
			
			final PackagePage related	= masterContent.getRelatedPackage() ;
			int pageIndex 	= related.getPageIndex() ;
			int total 		= related.getPageCount();

			adapter.showMore(pageIndex < total);

			if (change) {
				loadContentTypeList(related);
			}
		}
		// set idle state
		setIdleState();
	}

	public void loadContentTypeList(PackagePage content) {
		adapter.loadProgramList(content);
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,long id) {

		// view
		HolderView holder = (HolderView) view.getTag();

		if (holder != null
				&& holder.content_type_load.getVisibility() == View.VISIBLE) {
			// load next page
			loadNextPage();
		}
		// callback to view
		else if (listener != null) {

			listener.onClick(parent, view, adapter.getItem(position));
		}
	}

	private void loadNextPage() {

		final IofferService service = IofferService.getNewTipService();
		final PackagePage related	= masterContent.getRelatedPackage() ;

		if (related.getPageIndex() < related.getPageCount()) {
			service.queryRelatedContentType(masterContent, related.getPageIndex() + 1,response);
		}
	}

}
