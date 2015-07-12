package cn.dezhisoft.cloud.mi.newugc.ioffer.upload.view;

import java.util.ArrayList;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import cn.dezhisoft.cloud.mi.newugc.R;

import cn.dezhisoft.cloud.mi.newugc.ioffer.ui.BasePreviewActivity;
import cn.dezhisoft.cloud.mi.newugc.ioffer.ui.BitmapPreviewActivity;
import cn.dezhisoft.cloud.mi.newugc.ioffer.ui.PlayAudioActivity;
import cn.dezhisoft.cloud.mi.newugc.ioffer.ui.PlayVideoActivity;
import cn.dezhisoft.cloud.mi.newugc.ioffer.upload.ui.IDownStateListener;
import cn.dezhisoft.cloud.mi.newugc.ugc.core.model.AssetsType;
import cn.dezhisoft.cloud.mi.newugc.ugc.core.model.AssetsType.DownloadState;
import cn.dezhisoft.cloud.mi.newugc.ugc.core.model.MediaType;

/**
 * 文件下载列表
 * 
 * @author Rosson Chen
 *
 */
public final class DownFileListView extends ListView implements android.widget.AdapterView.OnItemClickListener{

	Context context;
	
	DownFileAdapter adapter ;

	public DownFileListView(Context context) {
		this(context, null);
	}

	public DownFileListView(Context context, AttributeSet attrs) {
		super(context, attrs);
		adapter	= new DownFileAdapter(context);
		uniteListView(this);
		this.context = context;
		setAdapter(adapter);
		
		setOnItemClickListener(this);
	}

	private final void uniteListView(ListView list) {
		list.setDividerHeight(2);
		list.setDivider(getResources().getDrawable(R.drawable.ugc_ic_ioffer_divide_line));
		list.setCacheColorHint(Color.TRANSPARENT);
	}
	
	public void loadAssetsTypeList(ArrayList<AssetsType> list){
		adapter.loadUploadTasks(list);
	}
	
	public final void setStateListener(IDownStateListener listener) {
		adapter.setTaskStateChange(listener);
	}
	
	@Override
	public DownFileAdapter getAdapter() {
		return adapter;
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,long id) {
		
		final AssetsType assets = adapter.getItem(position);
		
		// 任务没有完成
		if(assets.getState() != DownloadState.DONE){
			return ;
		}
		
		MediaType type	= assets.getMediaType() ;
		Intent intent 	= new Intent() ;
		
		if(type == MediaType.AUDIO){
			intent.putExtra(BasePreviewActivity.PREVIEW_NAME, "");
			intent.putExtra(PlayAudioActivity.PREVIEW_KEY, PlayAudioActivity.PREVIEW_MODE_LOCAL);
			intent.putExtra(PlayAudioActivity.PREVIEW_PATH, assets.getLocalPath());
			intent.setClass(context, PlayAudioActivity.class);
			context.startActivity(intent);
		} else if(type == MediaType.VIDEO){
			intent.putExtra(BasePreviewActivity.PREVIEW_NAME, "");
			intent.putExtra(PlayVideoActivity.PREVIEW_KEY, PlayVideoActivity.PREVIEW_MODE_LOCAL);
			intent.putExtra(PlayVideoActivity.PREVIEW_PATH, assets.getLocalPath());
			intent.setClass(context, PlayVideoActivity.class);
			context.startActivity(intent);
		} else if(type == MediaType.IMAGE){
			intent.putExtra(BitmapPreviewActivity.PREVIEW_KEY, BitmapPreviewActivity.PREVIEW_MODE_LOCAL);
			intent.putExtra(BitmapPreviewActivity.PREVIEW_PATH, assets.getLocalPath());
			intent.setClass(context, BitmapPreviewActivity.class);
			context.startActivity(intent);
		} else {
			Toast.makeText(context, R.string.ugc_label_task_not_preview, Toast.LENGTH_SHORT).show() ;
		}
	}
}
