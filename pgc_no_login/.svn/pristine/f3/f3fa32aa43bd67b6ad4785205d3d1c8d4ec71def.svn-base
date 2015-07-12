package cn.dezhisoft.cloud.mi.newugc.ioffer.upload.ui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.SimpleAdapter;

import cn.dezhisoft.cloud.mi.newugc.R;

import com.sobey.sdk.base.BitmapUtils;

/**
 * 
 * @author Rosson Chen
 *
 */
public final class UGCSelectImageActivity extends Activity {
	
	/** video path */
	public static final String KEY_PATH 	= "_path";
	/** video name */
	public static final String KEY_NAME 	= "_name";
	/** code */
	public final static int 	IMAGE_RESULTCODE = 6;
	
	private final ArrayList<HashMap<String, String>> itemList = new ArrayList<HashMap<String, String>>();
	
	private static final String ITEM_NAME 	= "_name" ;
	private static final String ITEM_PATH 	= "_path" ;
	private static final String ITEM_ERROR 	= "_error" ;

	GridView gridView  ;
	private final HashMap<String, Bitmap> IconList = new HashMap<String, Bitmap>() ;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ugc_select_image_layout);
		
		gridView = (GridView)findViewById(R.id.select_image_grap);
		itemList.clear() ;
		IconList.clear() ;
		
		Cursor cursor = getContentResolver().query(
				MediaStore.Images.Media.EXTERNAL_CONTENT_URI, null, null, null,
				null);
		cursor.moveToPosition(-1);
		
		final int nameColumn = cursor.getColumnIndex(MediaStore.Images.Media.DISPLAY_NAME) ;
		final int pathColumn = cursor.getColumnIndex(MediaStore.Images.Media.DATA) ;
		
		String name ;
		String path ;
		
		while (cursor.moveToNext()) {
			
			HashMap<String, String> map = new HashMap<String, String>() ;
			name 	= cursor.getString(nameColumn);
			path 	= cursor.getString(pathColumn);
			
			map.put(ITEM_NAME, name);
			map.put(ITEM_PATH, path);
			map.put(ITEM_ERROR, "false");

			itemList.add(map);
		}
		cursor.close();
		cursor = null;
		
		String[] from 	= {ITEM_PATH};
		int[] to 		= {R.id.imageView};
		ListAdapter adapter = new GridAdapter(this, itemList, R.layout.item_layout, from,to);
		gridView.setAdapter(adapter);
		gridView.setOnItemClickListener(listener);
		
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		
		for(int id = 0 ,size = itemList.size(); id < size ; id++){
			HashMap<String, String> map = itemList.get(id);
			String path = map.get(ITEM_PATH);
			Bitmap icon = IconList.get(path);
			if(icon != null){
				icon.recycle() ;
			}
		}
		
		itemList.clear() ;
		IconList.clear() ;
		System.gc() ;
	}

	OnItemClickListener listener = new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,long id) {
			
			final HashMap<String, String> map = itemList.get(position) ;
			
			Intent intent = getIntent();
			intent.putExtra(KEY_PATH, map.get(ITEM_PATH));
			intent.putExtra(KEY_NAME, map.get(ITEM_NAME));
			UGCSelectImageActivity.this.setResult(IMAGE_RESULTCODE, intent);
			UGCSelectImageActivity.this.finish();
		}
	};

	final class GridAdapter extends SimpleAdapter {

		public GridAdapter(Context context,
				List<? extends Map<String, ?>> data, int resource,
				String[] from, int[] to) {
			super(context, data, resource, from, to);
		}

		// set the imageView using the path of image
		public void setViewImage(ImageView v, String value) {
			
			if(value == null) return ;
			
			Bitmap icon = IconList.get(value);
			
			if(icon == null){
				try {
					Bitmap bitmap 	= BitmapUtils.createBitmap(value, 64, 64);
					icon 			= Bitmap.createScaledBitmap(bitmap, 64, 64, true);
					//bitmap.recycle() ;
					IconList.put(value, icon);
				} catch (Exception e) {
					e.printStackTrace() ;
				}
			}
			
			//
			v.setImageBitmap(icon);
		}
	}

}
