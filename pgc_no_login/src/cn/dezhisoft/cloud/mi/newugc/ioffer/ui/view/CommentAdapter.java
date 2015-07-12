package cn.dezhisoft.cloud.mi.newugc.ioffer.ui.view;

import java.util.ArrayList;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import cn.dezhisoft.cloud.mi.newugc.R;

import cn.dezhisoft.cloud.mi.newugc.ugc.model.CommentType;

/**
 * 
 * @author Eric
 * 
 */
public class CommentAdapter extends BaseIofferAdapter {

	private ArrayList<CommentType> data;

	public CommentAdapter(Context context) {
		super(context);
	}

	@Override
	public int getCount() {
		return data != null ? data.size() : 0;
	}

	@Override
	public CommentType getItem(int position) {
		return data != null ? data.get(position) : null;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder = null;

		if (convertView == null) {
			viewHolder = new ViewHolder();
			convertView = inflater.inflate(R.layout.ioffer_comment_list_item_layout, null);
			viewHolder.headline = (TextView) convertView.findViewById(R.id.ioffer_comment_item_headline);
			viewHolder.rating = (RatingBar) convertView.findViewById(R.id.ioffer_comment_item_rating);
			viewHolder.appraiser_and_date = (TextView) convertView.findViewById(R.id.ioffer_comment_appraiser_and_date);
			viewHolder.message = (TextView) convertView.findViewById(R.id.ioffer_comment_message);

			convertView.setTag(viewHolder);

		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}

		CommentType comment = getItem(position);
		viewHolder.headline.setText((position + 1) + "." + comment.getCommentTitle());
		viewHolder.rating.setRating(comment.getCommentRating());
		viewHolder.appraiser_and_date.setText(comment.getCommentUser() + " " + "时间:" + " " + formatDate(comment.getCommentDate()));
		viewHolder.message.setText(comment.getCommentContent());

		return convertView;
	}

	public void loadCommentsList(ArrayList<CommentType> list) {
		if (list != null) {
			data = list;
			notifyDataSetChanged();
		}
	}

	public final class ViewHolder {
		public TextView headline;
		public RatingBar rating;
		public TextView appraiser_and_date;
		public TextView message;
	}
	
	public String formatDate(String time) {
		String date = "";
		
		char d[] = time.toCharArray();
		
		String year = String.valueOf(d, 0, 4);
		String month = String.valueOf(d, 5, 2);
		String day = String.valueOf(d, 8, 2);
		
		date = year + "-" + month + "-" + day;
		
		 return date;
	}
	

	public static String getVersionName(Context context) {
		String versionName = "";
		try {
			PackageManager pm = context.getPackageManager();
			PackageInfo pi = pm.getPackageInfo(context.getPackageName(), 0);
			versionName = pi.versionName;
			if (versionName == null || versionName.length() <= 0) {
				return "";
			}
		} catch (Exception e) {
			Log.e("VersionInfo", "Exception", e);
		}
		return versionName;
	}
	
	protected final void clearCommentList(){
		data = null;
		notifyDataSetChanged();
	}
}
