package cn.dezhisoft.cloud.mi.newugc.ioffer.upload.view;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import cn.dezhisoft.cloud.mi.newugc.R;

import cn.dezhisoft.cloud.mi.newugc.ioffer.upload.ui.ITaskStateListener;
import cn.dezhisoft.cloud.mi.newugc.ugc.core.model.Metadata;
import cn.dezhisoft.cloud.mi.newugc.ugc.core.model.Task;
import cn.dezhisoft.cloud.mi.newugc.ugc.core.upload.IFileUpload.UploadMessage;

/**
 * Upload File Adapter
 * 
 * @author Rosson Chen
 *
 */
public final class UploadFileAdapter extends BaseAdapter implements View.OnClickListener{
	
	public static final int 	ADAPTER_STATE_NORMAL 	= 0 ;
	public static final int 	ADAPTER_STATE_EDIT 		= 1 ;
	static final  int 			B_LENGTH				= 1024 * 1024 ;
	static final DecimalFormat FLOAT_FORMAT				= new DecimalFormat("0.00");
	
	private LayoutInflater 				mInflater ;
	private final ArrayList<Task> 		mTaskList = new ArrayList<Task>();
	private final ArrayList<Task> 		mTaskTempList = new ArrayList<Task>();
	private final ArrayList<HolderView> HolderViews = new ArrayList<HolderView>();
	private ITaskStateListener 			mTaskStateListener ;
	private int 						mFlag ;
	private Context						mContext ;
	
	protected UploadFileAdapter(Context context){
		mTaskList.clear() ;
		mTaskTempList.clear() ;
		mInflater 	= LayoutInflater.from(context);
		mFlag		= ADAPTER_STATE_NORMAL ;
		HolderViews.clear() ;
		mContext	= context ;
	}
	
	protected final void setTaskStateChange(ITaskStateListener listener){
		mTaskStateListener = listener ;
	}
	
	@Override
	public int getCount() {
		return mTaskTempList != null ? mTaskTempList.size() : 0;
	}

	@Override
	public Task getItem(int position) {
		return mTaskTempList != null ? mTaskTempList.get(position) : null;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		//
		HolderView holderview ;
		if(convertView == null){
			convertView = mInflater.inflate(R.layout.ugc_upload_file_item, null);
			holderview	= new HolderView() ;
			//
			holderview.common_icon		= (ImageView)convertView.findViewById(R.id.upload_item_icon);
			//
			holderview.task_content_layout= (RelativeLayout)convertView.findViewById(R.id.upload_content_layout);
			holderview.taks_state_layout= (RelativeLayout)convertView.findViewById(R.id.upload_state_layout);
			holderview.task_title		= (TextView)convertView.findViewById(R.id.upload_title_textview);
			holderview.task_progress	= (ProgressBar)convertView.findViewById(R.id.upload_progress_bar);
			holderview.task_detail		= (TextView)convertView.findViewById(R.id.upload_progress_textview);
			holderview.task_state_bnt	= (ImageView)convertView.findViewById(R.id.upload_state_icon);
			//
			holderview.history_layout	= (RelativeLayout)convertView.findViewById(R.id.upload_history_content_layout);
			holderview.history_state_layout = (RelativeLayout)convertView.findViewById(R.id.upload_history_right_layout);
			holderview.history_title	= (TextView)convertView.findViewById(R.id.upload_history_title_textview);
			holderview.history_descri	= (TextView)convertView.findViewById(R.id.upload_history_summary_textview);
			holderview.history_comment	= (TextView)convertView.findViewById(R.id.upload_history_comment_textview);
			holderview.history_category	= (ImageView)convertView.findViewById(R.id.upload_category_icon);
			holderview.history_state	= (TextView)convertView.findViewById(R.id.upload_state_text);
			//
			holderview.remove_layout	= (RelativeLayout)convertView.findViewById(R.id.upload_remove_layout);
			holderview.remove_edit		= (ImageView)convertView.findViewById(R.id.upload_remove_icon);
			//
			convertView.setTag(holderview);
			HolderViews.add(holderview);
		} else {
			holderview = (HolderView)convertView.getTag();
		}
		//
		final Task task = getItem(position);
		//
		holderview.task = task ;
		// icon
		updataTaskIcon(holderview,task);
		//
		switch(mFlag){
		case ADAPTER_STATE_NORMAL :
			// 普通模式
			updateHolderView(holderview,task,true);
			break ;
		case ADAPTER_STATE_EDIT :
			// 编辑模式
			updateHolderView(holderview,task,false);
			holderview.taks_state_layout.setVisibility(View.GONE);
			holderview.history_state_layout.setVisibility(View.GONE);
			holderview.remove_layout.setVisibility(View.VISIBLE);
			
			holderview.remove_edit.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					removeTask(task);
				}
			}) ;
			break ;
		}
		//
		return convertView;
	}
	
	private final void updateHolderView(HolderView holderview,final Task task,boolean hasListener){
		
		Metadata meta  	= task.getMetadata() ;
		int state 		= task.getState() ;
		
		switch(state){
		case Task.STATE.NEW :
		case Task.STATE.RUNNING :
			holderview.history_layout.setVisibility(View.GONE);
			holderview.history_state_layout.setVisibility(View.GONE);
			holderview.remove_layout.setVisibility(View.GONE);
			holderview.task_content_layout.setVisibility(View.VISIBLE);
			holderview.taks_state_layout.setVisibility(View.VISIBLE);
			holderview.task_title.setText(meta != null ? meta.getTitle() : "");
			holderview.task_state_bnt.setBackgroundResource(R.drawable.ugc_ic_ioffer_upload_pause_icon);
			holderview.task_progress.setProgress(task.getTaskProgress());
			holderview.task_detail.setText(getUploadDetail(task));
			if(hasListener) {
				holderview.task_state_bnt.setTag(task);
				holderview.task_state_bnt.setOnClickListener(this) ;
			} else {
				holderview.task_state_bnt.setTag(null);
				holderview.task_state_bnt.setOnClickListener(null);
			}
			break ;
		case Task.STATE.STOP :
			holderview.history_layout.setVisibility(View.GONE);
			holderview.history_state_layout.setVisibility(View.GONE);
			holderview.remove_layout.setVisibility(View.GONE);
			holderview.task_content_layout.setVisibility(View.VISIBLE);
			holderview.taks_state_layout.setVisibility(View.VISIBLE);
			holderview.task_title.setText(meta != null ? meta.getTitle() : "");
			holderview.task_state_bnt.setBackgroundResource(R.drawable.ugc_ic_ioffer_upload_resume_icon);
			holderview.task_progress.setProgress(task.getTaskProgress());
			holderview.task_detail.setText(getUploadDetail(task));
			if(hasListener) {
				holderview.task_state_bnt.setTag(task);
				holderview.task_state_bnt.setOnClickListener(this) ;
			} else {
				holderview.task_state_bnt.setTag(null);
				holderview.task_state_bnt.setOnClickListener(null);
			}
			break ;
		case Task.STATE.WAIT :
			holderview.history_layout.setVisibility(View.GONE);
			holderview.history_state_layout.setVisibility(View.GONE);
			holderview.remove_layout.setVisibility(View.GONE);
			holderview.task_content_layout.setVisibility(View.VISIBLE);
			holderview.taks_state_layout.setVisibility(View.VISIBLE);
			holderview.task_title.setText(meta != null ? meta.getTitle() : "");
			holderview.task_state_bnt.setBackgroundResource(R.drawable.ugc_ic_ioffer_upload_wait_icon);
			holderview.task_progress.setProgress(task.getTaskProgress());
			holderview.task_detail.setText(getUploadDetail(task));
			if(hasListener) {
				holderview.task_state_bnt.setTag(task);
				holderview.task_state_bnt.setOnClickListener(this) ;
			} else {
				holderview.task_state_bnt.setTag(null);
				holderview.task_state_bnt.setOnClickListener(null);
			}
			break ;
		case Task.STATE.DONE :
			holderview.remove_layout.setVisibility(View.GONE);
			holderview.taks_state_layout.setVisibility(View.GONE);
			holderview.task_content_layout.setVisibility(View.GONE);
			holderview.history_layout.setVisibility(View.VISIBLE);
			holderview.history_state_layout.setVisibility(View.VISIBLE);
			holderview.history_title.setText(meta != null ? meta.getTitle() : "");
			holderview.history_descri.setText(task.getMetadata().getCatalogName());
			holderview.history_comment.setText("");
			holderview.history_state.setText(R.string.ugc_label_task_complete);
			holderview.task_state_bnt.setBackgroundResource(R.drawable.ugc_ic_ioffer_upload_resume_icon);
			updataTaskIcon(holderview,task);
			updataTaskDoneCategory(holderview,task);
			break ;
		}
	}
	
	@Override
	public void onClick(View v) {
		Object obj = v.getTag() ;
		if(obj instanceof Task){
			updateTaskState((Task)obj);
		}
	}

	private void updataTaskIcon(HolderView holderview,Task task){
		int type	= task.getType() ;
		//
		if(type == Task.TYPE.AUDIO){
			holderview.common_icon.setBackgroundResource(R.drawable.ugc_ic_ioffer_content_category_a_icon);
		} else if(type == Task.TYPE.VIDEO){
			holderview.common_icon.setBackgroundResource(R.drawable.ugc_ic_ioffer_content_category_v_icon);
		}else if(type == Task.TYPE.IMAGE){
			holderview.common_icon.setBackgroundResource(R.drawable.ugc_ic_ioffer_content_category_i_icon);
		}
	}
	
	private void updataTaskDoneCategory(HolderView holderview,Task task){
		int type	= task.getType() ;
		if(type == Task.TYPE.AUDIO){
			holderview.history_category.setBackgroundResource(R.drawable.ugc_ic_ioffer_content_s_audio);
		} else if(type == Task.TYPE.VIDEO){
			holderview.history_category.setBackgroundResource(R.drawable.ugc_ic_ioffer_content_s_video);
		}else if(type == Task.TYPE.IMAGE){
			holderview.history_category.setBackgroundResource(R.drawable.ugc_ic_ioffer_content_s_image);
		}
	}
	
	private String getUploadDetail(Task task){
		String detail 	= null ;
		int total		= task.getTaskLength();
		int prog		= task.getTaskProgress();
		if(total > 0) {
			float p = (prog < 0 ) ? (0.00f) : ((float)prog / 100) ;
			detail  = FLOAT_FORMAT.format((p * total) / B_LENGTH) + "MB / " + FLOAT_FORMAT.format(((float)total / B_LENGTH)) + "MB";
		}
		return detail != null ? detail : task.getDetail() ;
	}
	
	public final void loadUploadTasks(ArrayList<Task> list){
		
		if(list != null){
			
			final ArrayList<Task> 	l = mTaskList ;
			
			for(Task task: list){
				l.add(task) ;
			}
			
			// 通知数据变化
			notifyTaskListChange() ;
		}
	}
	
	public final void changeEditState(){
		mFlag = (++mFlag) % 2 ;
		notifyDataSetInvalidated() ;
	}
	
	public final int getEditState(){
		return mFlag ;
	}
	
	public synchronized final void updateProgress(String uid,int progress,int length){
		int pro = progress >= 100 ? 100 : progress ;
		String text  = "";
		final ArrayList<HolderView> views = HolderViews ;
		for(HolderView holder : views){
			Task task = holder.task ;
			if(task != null && task.getTransferUID().equals(uid)){
				task.setTaskLength(length);
				task.setTaskProgress(pro);
				task.setState(Task.STATE.RUNNING);
				if(pro >= 100){
					text = mContext.getString(R.string.ugc_label_task_verify) ;
				} else {
					text = getUploadDetail(task) /*+ " " + mContext.getString(R.string.label_task_live_speed)*/ + " " + pro + "%";
				}
				//
				holder.task_progress.setProgress(pro);
				holder.task_state_bnt.setBackgroundResource(R.drawable.ugc_ic_ioffer_upload_pause_icon);
				holder.task_detail.setText(text);
				//
				holder.task_progress.invalidate();
				holder.task_detail.invalidate() ;
				holder.task_state_bnt.invalidate();
				break ;
			}
		}
	}
	
	public final void updateCompleteTask(boolean verify,String tuid){
		
		final ArrayList<Task> 	list = mTaskList ;
		
		for(Task task : list){
			if(task.getTransferUID().equals(tuid)){
				task.setTaskProgress(100);
				if(verify)
					task.setState(Task.STATE.DONE);
				else
					task.setState(Task.STATE.STOP);
				break ;
			}
		}
		
		// 通知数据变化
		notifyTaskListChange();
	}
	
	public final void updateTaskState(UploadMessage msg){
		
		String 		tuid 	= msg.mTuid ;
		int	state 			= msg.mState ;
		final ArrayList<HolderView> views = HolderViews ;
		
		for(HolderView holder : views){
			
			Task task = holder.task ;
			
			if(task != null && task.getTransferUID().equals(tuid)){
				
				task.setState(state);
				task.setDetail(msg.mMessage);
				
				if(state == Task.STATE.NEW || state == Task.STATE.RUNNING){
					holder.task_state_bnt.setBackgroundResource(R.drawable.ugc_ic_ioffer_upload_pause_icon);
				} else if(state == Task.STATE.WAIT){
					holder.task_state_bnt.setBackgroundResource(R.drawable.ugc_ic_ioffer_upload_wait_icon);
				} else if(state == Task.STATE.STOP){
					holder.task_state_bnt.setBackgroundResource(R.drawable.ugc_ic_ioffer_upload_resume_icon);
					holder.task_detail.setText(msg.mMessage);
					holder.task_detail.invalidate() ;
				} else if(state == Task.STATE.DONE){
					notifyTaskListChange();
				}
				
				holder.task_state_bnt.invalidate() ;
				break ;
			}
		}
	}
	
	private final void updateTaskState(Task task){
		
		final ITaskStateListener listener = mTaskStateListener ;
		int state	= task.getState() ;
		
		if(state == Task.STATE.NEW 
				|| state == Task.STATE.RUNNING 
				|| state == Task.STATE.WAIT){
			
			if(listener != null) 
				listener.change(task, state, Task.STATE.STOP);
			
			task.setState(Task.STATE.STOP);
			
		} else if(state == Task.STATE.STOP){
			
			if(listener != null)
				listener.change(task, state, Task.STATE.WAIT);
			
			task.setState(Task.STATE.WAIT);
		}
		
		notifyDataSetChanged();
	}
	
	/** 重新排序 */
	private void notifyTaskListChange(){
		
		mTaskTempList.clear() ;
		
		final ArrayList<Task> list = mTaskList ;
		final ArrayList<Task> temp = mTaskTempList ;
		
		// 排序
		Collections.sort(list, new Comparator<Task>() {
			
			@Override
			public int compare(Task t1, Task t2) {
				if(t1.getId() < t2.getId()){
					return 1 ;
				} else {
					return -1 ;
				}
			}
		});
		
		// 正在上传
		for(Task task : list){
			if(task.getState() == Task.STATE.RUNNING){
				temp.add(task);
			}
		}
		// 等待
		for(Task task : list){
			int state = task.getState() ;
			if(!temp.contains(task) && (state == Task.STATE.WAIT || state == Task.STATE.NEW)){
				temp.add(task);
			}
		}
		// 停止
		for(Task task : list){
			if(!temp.contains(task) && (task.getState() == Task.STATE.STOP)){
				temp.add(task);
			}
		}
		// 完成
		for(Task task : list){
			if(!temp.contains(task) && (task.getState() == Task.STATE.DONE)){
				temp.add(task);
			}
		}
		//
		notifyDataSetChanged() ;
	}
	
	private final void removeTask(Task task){
		
		final ArrayList<Task> 	list = mTaskList ;
		final ArrayList<Task> 	temp = mTaskTempList ;
		final ITaskStateListener listener = mTaskStateListener ;
		
		if(listener != null) listener.remove(task);
		
		list.remove(task);
		temp.remove(task);
		
		notifyDataSetChanged();
	}
	
	final class HolderView {
		Task		task ;
		ImageView 	common_icon ;
		RelativeLayout task_content_layout ;
		RelativeLayout taks_state_layout ;
		TextView  	task_title ;
		ProgressBar task_progress ;
		TextView	task_detail ;
		ImageView	task_state_bnt;
		RelativeLayout history_layout ;
		RelativeLayout history_state_layout ;
		TextView  	history_title ;
		TextView  	history_descri ;
		TextView  	history_comment;
		ImageView	history_category;
		TextView  	history_state;
		RelativeLayout remove_layout ;
		ImageView	remove_edit;
	}
}
