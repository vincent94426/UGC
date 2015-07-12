package cn.dezhisoft.cloud.mi.newugc.ioffer.ui.view;

import java.util.ArrayList;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import cn.dezhisoft.cloud.mi.newugc.R;

import cn.dezhisoft.cloud.mi.newugc.ugc.model.PackagePage;
import cn.dezhisoft.cloud.mi.newugc.ugc.model.PackageType;
import cn.dezhisoft.cloud.mi.newugc.ugc.ws.IDownResource;
import cn.dezhisoft.cloud.mi.newugc.ugc.ws.publish.UGCContentWebService;

/**
 * 
 * @author Rosson Chen
 * 
 */
public class ContentTypeAdapter extends BaseIofferAdapter {

	PackagePage content;
	final ArrayList<ImageView> contentTypeIcon = new ArrayList<ImageView>();
	ArrayList<PackageType> contentTypes;
	int totalSize;
	String loadText;
	String comments ;

	protected ContentTypeAdapter(Context context) {
		super(context);
		totalSize = 0;
		contentTypeIcon.clear();
		loadText = context.getString(R.string.label_content_load);
		comments = context.getString(R.string.table_label_comments_suffix);
	}
	
	@Override
	public int getCount() {
		if (content == null)
			return 0;
		
		if (content.getPageCount() > 1 && content.getPageIndex() < content.getPageCount()) {
			return totalSize + 1;
		} else {
			return totalSize;
		}
	}

	@Override
	public PackageType getItem(int position) {
		return contentTypes != null && position < contentTypes.size() ? contentTypes.get(position) : null;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		//
		final IDownResource downloader = UGCContentWebService.Downloader;
		// view
		HolderView holderView;
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.ioffer_content_type_item_layout, null);
			holderView = new HolderView();
			holderView.content_type_load = (TextView) convertView.findViewById(R.id.content_type_load);
			holderView.content_type_layout = (RelativeLayout) convertView.findViewById(R.id.content_type_layout);
			holderView.content_type_icon = (ImageView) convertView.findViewById(R.id.content_type_icon);
			holderView.content_type_title = (TextView) convertView.findViewById(R.id.content_type_title);
			holderView.content_type_description = (TextView) convertView.findViewById(R.id.content_type_description);
			holderView.content_type_comment = (TextView) convertView.findViewById(R.id.content_type_comment);
			holderView.content_type_category = (ImageView) convertView.findViewById(R.id.content_type_catalog);
			convertView.setTag(holderView);
			contentTypeIcon.add(holderView.content_type_icon);
		} else {
			holderView = (HolderView) convertView.getTag();
		}
		
		final PackageType item = getItem(position);
		holderView.content_type_icon.setTag(item);
		if (showMore && position >= totalSize) {
			holderView.content_type_layout.setVisibility(View.INVISIBLE);
			holderView.content_type_load.setVisibility(View.VISIBLE);
			holderView.content_type_load.setText(loadText);
			return convertView;
		}
		//
		if(item == null){
			return convertView ;
		}
		
		holderView.content_type_layout.setVisibility(View.VISIBLE);
		holderView.content_type_load.setVisibility(View.INVISIBLE);
		
		final ImageView icon = holderView.content_type_icon ;
		downloader.downBitmap(item.getIconUrl(), icon, getDefaultIcon(item));
		
		holderView.content_type_title.setText(item.getTitle());
		holderView.content_type_description.setText(item.getDescribe());
		holderView.content_type_comment.setText(item.getCommentCount() + comments);
		//
		return convertView;
	}

	private final int getDefaultIcon(PackageType item) {
		return R.drawable.ic_ioffer_content_category_i_icon ;
	}

	protected final void loadProgramList(PackagePage content) {
		
		if (content != null) {
			
			this.content = content;
			contentTypes = content.getPackageTypeList();
			
			totalSize = contentTypes.size();
			//
			if (totalSize == 0) {
				showMore(false) ;
				Toast.makeText(context, "数据为空", Toast.LENGTH_SHORT).show();
			} else {
				// show more
				showMore(content.getPageIndex() < content.getPageCount()) ;
			}
			// data change
			notifyDataSetChanged();
		}
	}

	protected final void clearProgramList() {
		content 		= null;
		contentTypes 	= null;
		totalSize 		= 0;
		contentTypeIcon.clear() ;
		
		notifyDataSetInvalidated() ;
	}

	public final class HolderView {
		TextView content_type_load;
		RelativeLayout content_type_layout;
		ImageView content_type_icon; // icon
		TextView content_type_title; 
		TextView content_type_description; 
		TextView content_type_comment; 
		ImageView content_type_category;
	}
}
