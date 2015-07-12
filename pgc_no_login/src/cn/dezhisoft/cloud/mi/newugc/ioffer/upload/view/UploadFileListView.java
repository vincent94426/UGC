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
import cn.dezhisoft.cloud.mi.newugc.ioffer.upload.ui.ITaskStateListener;
import cn.dezhisoft.cloud.mi.newugc.ugc.core.model.Task;

/**
 * 
 * @author Rosson Chen
 *
 */
public final class UploadFileListView extends ListView implements android.widget.AdapterView.OnItemClickListener{

	UploadFileAdapter adapter;

	Context context;

	public UploadFileListView(Context context) {
		this(context, null);
	}

	public UploadFileListView(Context context, AttributeSet attrs) {
		super(context, attrs);
		adapter = new UploadFileAdapter(context);
		uniteListView(this);
		setAdapter(adapter);
		this.context = context;
		
		setOnItemClickListener(this);
	}

	private final void uniteListView(ListView list) {
		list.setDividerHeight(2);
		list.setDivider(getResources().getDrawable(R.drawable.ugc_ic_ioffer_divide_line));
		list.setCacheColorHint(Color.TRANSPARENT);
	}

	public final void setStateListener(ITaskStateListener listener) {
		adapter.setTaskStateChange(listener);
	}

	public void loadUploadList(ArrayList<Task> list) {
		if (adapter != null)
			adapter.loadUploadTasks(list);
	}

	@Override
	public UploadFileAdapter getAdapter() {
		return adapter;
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,long id) {
		
		final Task task = adapter.getItem(position);
		// 任务是否完成
		if(task.getState() != Task.STATE.DONE) {
			return ;
		}
		
		int type		= task.getType() ;
		Intent intent 	= new Intent() ;
		//
		if(type == Task.TYPE.AUDIO){
			intent.putExtra(BasePreviewActivity.PREVIEW_NAME, "");
			intent.putExtra(PlayAudioActivity.PREVIEW_KEY, PlayAudioActivity.PREVIEW_MODE_LOCAL);
			intent.putExtra(PlayAudioActivity.PREVIEW_PATH, task.getTaskPath());
			intent.setClass(context, PlayAudioActivity.class);
			context.startActivity(intent);
		} else if(type == Task.TYPE.VIDEO){
			intent.putExtra(BasePreviewActivity.PREVIEW_NAME, "");
			intent.putExtra(PlayVideoActivity.PREVIEW_KEY, PlayVideoActivity.PREVIEW_MODE_LOCAL);
			intent.putExtra(PlayVideoActivity.PREVIEW_PATH, task.getTaskPath());
			intent.setClass(context, PlayVideoActivity.class);
			context.startActivity(intent);
		} else if(type == Task.TYPE.IMAGE){
			intent.putExtra(BitmapPreviewActivity.PREVIEW_KEY, BitmapPreviewActivity.PREVIEW_MODE_LOCAL);
			intent.putExtra(BitmapPreviewActivity.PREVIEW_PATH, task.getTaskPath());
			intent.setClass(context, BitmapPreviewActivity.class);
			context.startActivity(intent);
		} else {
			Toast.makeText(context, R.string.ugc_label_task_not_preview, Toast.LENGTH_SHORT).show() ;
		}
	}
}
