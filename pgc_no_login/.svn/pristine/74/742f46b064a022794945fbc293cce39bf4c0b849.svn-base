package cn.dezhisoft.cloud.mi.newugc.ioffer.ui;

import java.util.ArrayList;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import cn.dezhisoft.cloud.mi.newugc.R;

import cn.dezhisoft.cloud.mi.newugc.ioffer.config.VideoSize;

/**
 * 分辨率设置
 * 
 * @author Rosson Chen
 * 
 */
public final class VideoSizeActivity extends BaseActivity {

	static final String TAG = "VideoSizeActivity";
	static final int MAX_HEIGHT	= 720 ;
	/** 返回代码*/
	protected static final int REQUEST_VIDEOSIZE_CODE	= 0x00ff0001 ;
	/** 选择的宽度*/
	public static final String KEY_WIDTH			= "_w" ;
	/** 选择的高度*/
	public static final String KEY_HEIGHT			= "_h" ;
	/** 视频分辨率类型 */
	public static final String KEY_TYPE				= "_type" ;
	
	/** 上传任务列表 */
	ListView video_size_list;
	final ArrayList<View> ViewList = new ArrayList<View>();
	final ArrayList<VideoSize> list = new ArrayList<VideoSize>() ;
	
	int mWidth , mHeight ;
	int selected_id ;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ioffer_video_size_layout);
		// list
		video_size_list	= (ListView) findViewById(R.id.video_size_list_view);
		ViewList.clear() ;
		list.clear() ;
		
		int type = getIntent().getIntExtra(KEY_TYPE, VideoSize.Type.RECORDER) ;
		ArrayList<Object> temp = null ;
		
		if(type == VideoSize.Type.RECORDER){
			
			temp = queryVideoSize(VideoSize.Type.RECORDER);
			
			for(Object obj : temp){
				list.add((VideoSize)obj);
			}
		
		} else if(type == VideoSize.Type.LIVE){
			
			temp = queryVideoSize(VideoSize.Type.LIVE);
			
			for(Object obj : temp){
				list.add((VideoSize)obj);
			}
		}
		
		// 默认选择
		int width  = getIntent().getIntExtra(KEY_WIDTH, 0) ;
		int height = getIntent().getIntExtra(KEY_HEIGHT, 0) ;
		
		if(width == 0 || height == 0){
			selected_id	= 0 ;
		} else {
			for(int index = 0 , max = list.size() ; index < max ; index++){
				VideoSize vs = list.get(index);
				if(width == vs.getWidth() && height == vs.getHeight()){
					selected_id	= index ;
					break ;
				}
			}
		}
		
		// 设置adapter
		video_size_list.setAdapter(SizeAdapter);
		video_size_list.setDividerHeight(2);
		video_size_list.setCacheColorHint(Color.TRANSPARENT);
		video_size_list.setDivider(getResources().getDrawable(R.drawable.ic_ioffer_divide_line));
		video_size_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,int position, long id) {
				selected_id			= position ;
				
				final VideoSize vp  = list.get(position);
				mWidth 				= vp.getWidth() ;
				mHeight				= vp.getHeight() ;
				
				SizeAdapter.notifyDataSetInvalidated();
				
				confirmSelected();
			}
		}) ;
	}
	
	private ArrayList<Object> queryVideoSize(int type){
		return mAccessDatabase.queryAllObject(VideoSize.class, new String[]{"type"}, new String[]{String.valueOf(type)});
	}
	
	public void buttonOnclick(View v) {
		switch (v.getId()) {
		case R.id.bnt_video_size_back :
			confirmSelected();
			break ;
		}
	}
	
	private void confirmSelected(){
		Intent intent = getIntent();
		intent.putExtra(KEY_WIDTH, mWidth);
		intent.putExtra(KEY_HEIGHT, mHeight);
		setResult(REQUEST_VIDEOSIZE_CODE, intent);
		finish();
	}
	
	/**
	 * 
	 */
	final BaseAdapter SizeAdapter = new BaseAdapter() {
		
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// 是否为null
			if(convertView == null) {
				convertView = getLayoutInflater().inflate(R.layout.ioffer_video_size_item, null);
			}
			// 设置id
			final VideoSize vp = list.get(position);
			TextView  size = (TextView) convertView.findViewById(R.id.video_size_text);
			TextView  mark = (TextView) convertView.findViewById(R.id.video_size_mark) ;
			size.setText(vp.getWidth() + "x" + vp.getHeight());
			// 设置背景
			if(position == 0)
				convertView.setBackgroundResource(R.drawable.ic_ioffer_setting_item_start_gb);
			else if(position == (getCount() -1))
				convertView.setBackgroundResource(R.drawable.ic_ioffer_setting_item_end_gb);
			else 
				convertView.setBackgroundResource(R.drawable.ic_ioffer_setting_item_middle_gb);
			// 是否选中
			if(position == selected_id){
				mark.setBackgroundResource(R.drawable.ic_ioffer_level_selected);
				mWidth 	= vp.getWidth() ;
				mHeight	= vp.getHeight() ;
			} else {
				mark.setBackgroundResource(R.drawable.ic_ioffer_level_default);
			}
			
			if(!ViewList.contains(convertView))
				ViewList.add(convertView);
			// 返回
			return convertView;
		}
		
		@Override
		public long getItemId(int position) {
			return position;
		}
		
		@Override
		public VideoSize getItem(int position) {
			return list != null ? list.get(position) : null ;
		}
		
		@Override
		public int getCount() {
			return list != null ? list.size() : 0;
		}
	};
}
