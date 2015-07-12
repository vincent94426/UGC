package cn.dezhisoft.cloud.mi.newugc.ioffer.upload.ui;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import cn.dezhisoft.cloud.mi.newugc.R;

/**
 * 
 * @author Rosson Chen
 *
 */
public final class UGCSelectAudioActivity extends Activity {
	
	/** audio path */
	public static final String KEY_PATH					= "_path" ;
	/** audio name */
	public static final String KEY_NAME					= "_name" ;
	/** code*/
	public final static int 	AUDIO_RESULTCODE 		= 3;
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ugc_select_audio_layout);
        
        Cursor cursor = getContentResolver().query(   
        		MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,    
        		null, null, null, null);     
        cursor.moveToPosition(-1);
        ArrayList<AudioItem> list = new ArrayList<UGCSelectAudioActivity.AudioItem>();
        while(cursor.moveToNext()){
        	AudioItem item = new AudioItem() ;
        	item.setName(cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.AudioColumns.DISPLAY_NAME))) ;
        	item.setPath(cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.AudioColumns.DATA)));
        	
        	list.add(item);
        }
        
        cursor.close() ;
        cursor = null ;
        
        final AudioAdapter adapter = new AudioAdapter(this) ;
        ListView audioList = (ListView) findViewById(R.id.selectaudio_list);
        audioList.setCacheColorHint(Color.TRANSPARENT);
        audioList.setDividerHeight(1);
        audioList.setAdapter(adapter);
        adapter.loadAudioList(list);
        
        audioList.setOnItemClickListener(new AdapterView.OnItemClickListener(){

			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				AudioItem item = adapter.getItem(position);
		        Intent intent = getIntent();
		        intent.putExtra(KEY_PATH, item.getPath());
		        intent.putExtra(KEY_NAME, item.getName());
		        UGCSelectAudioActivity.this.setResult(AUDIO_RESULTCODE, intent);
		        UGCSelectAudioActivity.this.finish();
			}});
    }
    
    private final class AudioAdapter extends BaseAdapter{
    	
    	ArrayList<AudioItem> list ;
    	LayoutInflater mInflater;

    	public AudioAdapter(Context context){
    		mInflater	= LayoutInflater.from(context);
    	}
    	
		@Override
		public int getCount() {
			return list != null ? list.size() : 0;
		}

		@Override
		public AudioItem getItem(int position) {
			return list != null ? list.get(position) : null;
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			if(convertView == null)
				convertView	= mInflater.inflate(R.layout.ugc_select_audio_item, null);
			TextView name = (TextView)convertView.findViewById(R.id.audio_item_name);
			name.setText(getItem(position).getName());
			return convertView;
		}
		
		protected void loadAudioList(ArrayList<AudioItem> l){
			if(l != null){
				list	= l ;
				notifyDataSetChanged() ;
			}
		}
    }
    
    private final class AudioItem {
    	String name ;
    	String path ;
		public String getName() {
			return name;
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
    }
}
