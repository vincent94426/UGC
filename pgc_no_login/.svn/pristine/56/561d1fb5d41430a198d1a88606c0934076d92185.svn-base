package cn.dezhisoft.cloud.mi.newugc.ioffer.upload.ui;

import java.util.ArrayList;
import java.util.concurrent.ArrayBlockingQueue;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.media.ThumbnailUtils;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.provider.MediaStore.Images;
import android.provider.MediaStore.MediaColumns;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import cn.dezhisoft.cloud.mi.newugc.R;

/**
 * ѡ����Ƶ
 * 
 * @author Rosson Chen
 * 
 */
public final class UGCSelectVideoActivity extends Activity {

	/** video path */
	public static final String KEY_PATH = "_path";
	/** video name */
	public static final String KEY_NAME = "_name";
	/** video mime */
	public static final String KEY_MIME = "_mime";
	
	/** code */
	public final static int VIDEO_RESULTCODE = 4;
	
	private final ArrayList<VideoItem> itemList = new ArrayList<UGCSelectVideoActivity.VideoItem>();
	private ArrayBlockingQueue<VideoItem> queue ;
	private ArrayList<ImageView> iconViews = new ArrayList<ImageView>() ;
	private boolean mStart ;
	
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ugc_select_video_layout);

		Cursor cursor = getContentResolver().query(
				MediaStore.Video.Media.EXTERNAL_CONTENT_URI, null, null, null,
				null);

		iconViews.clear() ;
		itemList.clear() ;
		
		while (cursor.moveToNext()) {
			
			VideoItem item = new VideoItem();
			item.setMime(cursor.getString(cursor.getColumnIndexOrThrow(MediaColumns.MIME_TYPE)));
			item.setName(cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Video.VideoColumns.DISPLAY_NAME)));
			item.setPath(cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Video.VideoColumns.DATA)));
			item.setDuration(cursor.getInt(cursor.getColumnIndexOrThrow(MediaStore.Video.VideoColumns.DURATION)));
			item.setHasError(false);
			
			itemList.add(item);
		}

		cursor.close();
		cursor = null;
		queue	= new ArrayBlockingQueue<UGCSelectVideoActivity.VideoItem>(itemList.size()) ;
		
		final VideoAdapter adapter = new VideoAdapter();
		ListView videoList = (ListView) findViewById(R.id.select_video_list);
		videoList.setCacheColorHint(Color.TRANSPARENT);
		videoList.setDividerHeight(1);
		videoList.setAdapter(adapter);

		videoList.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			public void onItemClick(AdapterView<?> parent, View view,int position, long id) {
				final VideoItem item = adapter.getItem(position);
				
				if(item.getDuration() <= 0){
					Toast.makeText(UGCSelectVideoActivity.this, "��Ч��Ƶ,������ѡ��", Toast.LENGTH_LONG).show();
					return ;
				}
				
				Intent intent = getIntent();
				intent.putExtra(KEY_PATH, item.getPath());
				intent.putExtra(KEY_NAME, item.getName());
				intent.putExtra(KEY_MIME, item.getMime());
				UGCSelectVideoActivity.this.setResult(VIDEO_RESULTCODE, intent);
				UGCSelectVideoActivity.this.finish();
			}
		});
		
		mStart	= true ;
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				
				if(queue == null) return ;
				
				VideoItem item = null ;
				
				while(mStart){
					
					item = queue.poll() ;
					
					if(item != null){
						
						Log.d("UGCSelectVideoActivity", "size=" + queue.size());
						Bitmap icon = ThumbnailUtils.createVideoThumbnail(item.getPath(), Images.Thumbnails.MINI_KIND) ;
						if(icon != null){
							item.setIcon(icon);
							handler.obtainMessage(1, item).sendToTarget() ;
						} else {
							item.setHasError(true);
						}
					} else {
						try{
							Thread.sleep(25);
						} catch (Exception e){
							e.printStackTrace() ;
						}
					}
				}
				
				queue.clear() ;
			}
		}).start() ;
		
		adapter.refresh() ;
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		
		mStart	= false ;
		
		if(itemList != null){
			itemList.clear() ;
		}
	}

	private final static String toTime(int time) {
		time /= 1000;
		int minute = time / 60;
		int hour = minute / 60;
		int second = time % 60;
		minute %= 60;
		return String.format("%02d:%02d:%02d", hour, minute, second);
	}
	
	private final Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			switch(msg.what){
			case 1 :
				VideoItem item = (VideoItem)msg.obj ;
				for(ImageView view : iconViews){
					VideoItem target = (VideoItem)view.getTag() ;
					if(item.getPath().equals(target.getPath())){
						view.setImageBitmap(item.getIcon());
						return ;
					}
				}
				break ;
			}
		}
		
	} ;
	
	private final class VideoAdapter extends BaseAdapter {
		
		@Override
		public int getCount() {
			return itemList != null ? itemList.size() : 0;
		}

		@Override
		public VideoItem getItem(int position) {
			return itemList != null ? itemList.get(position) : null;
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			
			final VideoItem item = getItem(position);
			HolderView holder ;
			
			if(convertView == null){
				holder		= new HolderView() ;
				convertView	= getLayoutInflater().inflate(R.layout.ugc_select_video_item, null);
				holder.icon 	= (ImageView)convertView.findViewById(R.id.video_item_icon);
				holder.title 	= (TextView)convertView.findViewById(R.id.video_item_name);
				holder.duration = (TextView)convertView.findViewById(R.id.video_item_duration);
				convertView.setTag(holder);
				
				iconViews.add(holder.icon);
			} else {
				holder = (HolderView) convertView.getTag() ;
			}
			
			holder.icon.setTag(item);
			Bitmap bitmap = item.getIcon() ;
			
			if(bitmap != null){
				holder.icon.setImageBitmap(bitmap);
			} else {
				// ��֡
				if(!item.isHasError()){
					queue.offer(item);
				}
				
				holder.icon.setImageResource(R.drawable.ic_launcher);
			}
			
			holder.title.setText(item.getName());
			holder.duration.setText(toTime(item.getDuration()));
			
			return convertView;
		}
		
		public void refresh(){
			notifyDataSetChanged() ;
		}
		
	}
	
	private class HolderView {
		ImageView icon ;
		TextView title;
		TextView duration ;
	}

	private final class VideoItem {
		String name;
		String path;
		int duration;
		Bitmap icon;
		String mime ;
		boolean hasError;

		public String getName() {
			return name;
		}

		public String getMime() {
			return mime;
		}

		public void setMime(String mime) {
			this.mime = mime;
		}

		public void setName(String name) {
			this.name = name;
		}

		public String getPath() {
			return path;
		}

		public void setPath(String path) {
			this.path = path;
		}

		public int getDuration() {
			return duration;
		}

		public void setDuration(int duration) {
			this.duration = duration;
		}

		public Bitmap getIcon() {
			return icon;
		}

		public boolean isHasError() {
			return hasError;
		}

		public void setHasError(boolean hasError) {
			this.hasError = hasError;
		}

		public void setIcon(Bitmap icon) {
			this.icon = icon;
		}
	}
}
