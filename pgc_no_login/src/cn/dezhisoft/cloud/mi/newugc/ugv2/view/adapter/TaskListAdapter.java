package cn.dezhisoft.cloud.mi.newugc.ugv2.view.adapter;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.view.DragEvent;
import android.view.GestureDetector.OnGestureListener;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import cn.dezhisoft.cloud.mi.newugc.R;
import cn.dezhisoft.cloud.mi.newugc.common.cache.ImageCacheProxy;
import cn.dezhisoft.cloud.mi.newugc.common.util.ChecksumUtil;
import cn.dezhisoft.cloud.mi.newugc.common.util.TaskThumnailUtil;
import cn.dezhisoft.cloud.mi.newugc.ioffer.upload.ui.ITaskStateListener;
import cn.dezhisoft.cloud.mi.newugc.ugc.core.model.Metadata;
import cn.dezhisoft.cloud.mi.newugc.ugc.core.model.Task;
import cn.dezhisoft.cloud.mi.newugc.ugc.core.upload.IFileUpload.UploadMessage;

/**
 * Upload Task List Adapter
 * 
 * @author Rosson Chen
 */
public final class TaskListAdapter extends BaseAdapter implements View.OnClickListener{
	public static final int 	ADAPTER_STATE_NORMAL 	= 0 ;
	public static final int 	ADAPTER_STATE_EDIT 		= 1 ;
	static final  int 			B_LENGTH				= 1024 * 1024 ;
	static final DecimalFormat FLOAT_FORMAT				= new DecimalFormat("0.00");
	
	private LayoutInflater 				mInflater ;
	private final ArrayList<Task> 		mTaskList = new ArrayList<Task>();
	private final ArrayList<Task> 		mTaskTempList = new ArrayList<Task>();
	private final ArrayList<TaskViewHolder> HolderViews = new ArrayList<TaskViewHolder>();
	private ITaskStateListener 			mTaskStateListener ;
	private int 						mFlag ;
	private Context						mContext ;
	
	public TaskListAdapter(Context context){
		mTaskList.clear() ;
		mTaskTempList.clear() ;
		mInflater 	= LayoutInflater.from(context);
		mFlag		= ADAPTER_STATE_NORMAL ;
		HolderViews.clear() ;
		mContext	= context ;
	}
	
	public void setTaskStateListener(ITaskStateListener listener){
		mTaskStateListener = listener ;
	}
	
	private int currentDelPosition = -1;
	
	public void setCurrentDelPosition(int currentDelPosition) {
		this.currentDelPosition = currentDelPosition;
	}

	public int getCurrentDelPosition() {
		return currentDelPosition;
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
	
	/*private TaskViewHolder currentOpenHolder;
	private void switchStatusAndOperation(TaskViewHolder holder) {
		if(View.GONE == holder.tvTaskDelete.getVisibility()){
			holder.rlStatusLayout.setVisibility(View.GONE);
			holder.tvTaskDelete.setVisibility(View.VISIBLE);
			
			Animation rightInAnimation = AnimationUtils.loadAnimation(mContext, R.anim.from_right_in);
			holder.tvTaskDelete.startAnimation(rightInAnimation);
			
			currentOpenHolder = holder;
		}else {
			holder.tvTaskDelete.setVisibility(View.GONE);

			Animation rightOutAnimation = AnimationUtils.loadAnimation(mContext, R.anim.from_right_out);
			holder.tvTaskDelete.startAnimation(rightOutAnimation);
			
			holder.rlStatusLayout.setVisibility(View.VISIBLE);
			
			currentOpenHolder = null;
		}
	}*/
	
	private void showDelOperation(TaskViewHolder holder){
		if(View.GONE == holder.tvTaskDelete.getVisibility()){
			holder.rlStatusLayout.setVisibility(View.GONE);
			holder.tvTaskDelete.setVisibility(View.VISIBLE);
			
			Animation rightInAnimation = AnimationUtils.loadAnimation(mContext, R.anim.from_right_in);
			holder.tvTaskDelete.startAnimation(rightInAnimation);
		}
	}
	
	private void hideDelOperation(TaskViewHolder holder){
		if(View.VISIBLE == holder.tvTaskDelete.getVisibility()){
			holder.tvTaskDelete.setVisibility(View.GONE);

			Animation rightOutAnimation = AnimationUtils.loadAnimation(mContext, R.anim.from_right_out);
			holder.tvTaskDelete.startAnimation(rightOutAnimation);
			
			holder.rlStatusLayout.setVisibility(View.VISIBLE);
		}
	}
	
	OnClickListener onDeleteClickListener = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			currentDelPosition = -1;
			onTaskDeleteAction((TaskViewHolder)v.getTag());
		}
	};
	
	/*OnTouchListener onTaskTouchListener = new OnTouchListener() {
		float downX, upX;
		View downView;
		@Override
		public boolean onTouch(View v, MotionEvent event) {
			switch (event.getAction()) {
			case MotionEvent.ACTION_UP:
				if(v.getId() == R.id.ugv2_task_delete_view){//删除按钮
					onTaskDeleteAction((TaskViewHolder)v.getTag());
				}else {//其他区域
					upX = event.getX();
					if(Math.abs(upX - downX) > 5 && null != downView && downView == v){//同一个元素上拖动
						switchStatusAndOperation((TaskViewHolder)v.getTag());
					}
				}
				break;
			case MotionEvent.ACTION_DOWN:
				downX = event.getX();
				downView = v;
				if(null != currentOpenHolder){
					switchStatusAndOperation(currentOpenHolder);
					downView = null;
				}
				break;
			default:
				break;
			}
			return true;
		}
	};*/
	

	protected void onTaskDeleteAction(TaskViewHolder tag) {
		removeTask(tag.task);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		//
		TaskViewHolder holderview ;
		if(convertView == null){
			convertView = mInflater.inflate(R.layout.ugv2_task_item_layout, null);
			holderview	= new TaskViewHolder() ;
			//
			holderview.ivKeyframe		= (ImageView)convertView.findViewById(R.id.ugv2_task_keyframe_view);
			holderview.rlKeyframeLayout = (RelativeLayout)convertView.findViewById(R.id.ugv2_task_keyframe_layout);
			//
			holderview.tvTaskDelete     = (TextView)convertView.findViewById(R.id.ugv2_task_delete_view);
			holderview.rlFailedLayout   = (RelativeLayout)convertView.findViewById(R.id.ugv2_task_failed_layout);
			holderview.tvFailedText     = (TextView)convertView.findViewById(R.id.ugv2_task_failed_text);
			holderview.rlFinishLayout   = (RelativeLayout)convertView.findViewById(R.id.ugv2_task_finish_layout);
			//
			holderview.tvTaskTitle      = (TextView)convertView.findViewById(R.id.ugv2_task_title_view);
			holderview.tvTaskDetail     = (TextView)convertView.findViewById(R.id.ugv2_task_time_view);
			
			holderview.rlStatusLayout   = (RelativeLayout)convertView.findViewById(R.id.ugv2_task_status_layout);
			
			holderview.rlProgressLayout = (RelativeLayout)convertView.findViewById(R.id.ugv2_task_progress_layout);
			holderview.progressBar      = (ProgressBar) convertView.findViewById(R.id.ugv2_task_progress_view);
			holderview.progressText     = (TextView) convertView.findViewById(R.id.ugv2_task_progress_text);

			convertView.setTag(holderview);
			holderview.tvTaskDelete.setTag(holderview);
			
			holderview.tvTaskDelete.setOnClickListener(onDeleteClickListener);
			//convertView.setOnTouchListener(onTaskTouchListener);
			
			HolderViews.add(holderview);
		} else {
			holderview = (TaskViewHolder)convertView.getTag();
		}
		if(position == currentDelPosition){
			//顯示刪除按鈕
			showDelOperation(holderview);
		}else {
			hideDelOperation(holderview);
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
			/*holderview.taks_state_layout.setVisibility(View.GONE);
			holderview.history_state_layout.setVisibility(View.GONE);
			holderview.remove_layout.setVisibility(View.VISIBLE);
			
			holderview.remove_edit.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					removeTask(task);
				}
			}) ;*/
			break ;
		}
		//
		return convertView;
	}
	

	private final void updateHolderView(TaskViewHolder holderview,final Task task,boolean hasListener){
		
		Metadata meta  	= task.getMetadata() ;
		int state 		= task.getState() ;
		
		switch(state){
		case Task.STATE.NEW :
		case Task.STATE.RUNNING :
			holderview.tvTaskTitle.setText(meta != null ? meta.getTitle() : "");
			holderview.tvTaskDetail.setText(getUploadDetail(task));
			holderview.rlFailedLayout.setVisibility(View.GONE);
			holderview.rlFinishLayout.setVisibility(View.GONE);
			holderview.rlProgressLayout.setVisibility(View.VISIBLE);
			holderview.progressText.setText(task.getTaskProgress() + "%");
			break ;
		case Task.STATE.STOP :
			holderview.tvTaskTitle.setText(meta != null ? meta.getTitle() : "");
			holderview.tvTaskDetail.setText(getUploadDetail(task));
			holderview.rlFailedLayout.setVisibility(View.VISIBLE);
			holderview.rlFinishLayout.setVisibility(View.GONE);
			holderview.tvFailedText.setText(R.string.ugv2_txt_task_stop);
			holderview.rlProgressLayout.setVisibility(View.GONE);
			break ;
		case Task.STATE.WAIT :
			holderview.tvTaskTitle.setText(meta != null ? meta.getTitle() : "");
			holderview.tvTaskDetail.setText(getUploadDetail(task));
			holderview.rlFailedLayout.setVisibility(View.VISIBLE);
			holderview.rlFinishLayout.setVisibility(View.GONE);
			holderview.tvFailedText.setText(R.string.ugv2_txt_task_wait);
			holderview.rlProgressLayout.setVisibility(View.GONE);
			break ;
		case Task.STATE.DONE :
			holderview.tvTaskTitle.setText(meta != null ? meta.getTitle() : "");
			holderview.tvTaskDetail.setText("");
			holderview.rlFailedLayout.setVisibility(View.GONE);
			holderview.rlFinishLayout.setVisibility(View.VISIBLE);
			holderview.rlProgressLayout.setVisibility(View.GONE);
			break ;
		}
	}
	
	public final void updateTaskState(UploadMessage msg){
		
		String 		tuid 	= msg.mTuid ;
		int	state 			= msg.mState ;
		final ArrayList<TaskViewHolder> views = HolderViews ;
		
		for(TaskViewHolder holder : views){
			
			Task task = holder.task ;
			
			if(task != null && task.getTransferUID().equals(tuid)){
				
				task.setState(state);
				task.setDetail(msg.mMessage);
				
				/*if(state == Task.STATE.NEW || state == Task.STATE.RUNNING){
					holder.tvTaskStatus.setBackgroundResource(R.drawable.ugv2_task_doing);
				} else if(state == Task.STATE.WAIT){
					holder.tvTaskStatus.setBackgroundResource(R.drawable.ugv2_task_failed);
				} else if(state == Task.STATE.STOP){
					holder.tvTaskStatus.setBackgroundResource(R.drawable.ugv2_task_failed);
					holder.tvTaskDetail.setText(msg.mMessage);
				} else if(state == Task.STATE.DONE){
					notifyTaskListChange();
				}*/
				notifyTaskListChange();
				
				break ;
			}
		}
	}

	@Override
	public void onClick(View v) {
		Object obj = v.getTag() ;
		if(obj instanceof Task){
			changeTaskState((Task)obj);
		}
	}

	private void updataTaskIcon(TaskViewHolder holderview,Task task){
		int type	= task.getType() ;
		//
		if(type == Task.TYPE.AUDIO){
			holderview.ivKeyframe.setBackgroundResource(R.drawable.ic_ioffer_content_category_a_icon);
		} else if (type == Task.TYPE.VIDEO || type == Task.TYPE.IMAGE){
			Bitmap taskThumbnail = getTaskThumbnail(task);
			if(null != taskThumbnail){
				holderview.ivKeyframe.setBackgroundDrawable(new BitmapDrawable(taskThumbnail));
			}
		}
	}

	private Bitmap getTaskThumbnail(Task task) {
		String taskPath = task.getTaskPath();
		String cacheKey = ChecksumUtil.buildChecksum(taskPath);
		Bitmap taskThumbnail = ImageCacheProxy.getBitmap(mContext, cacheKey);
		if(null != taskThumbnail){
			return taskThumbnail;
		}
		
		/*if(Task.TYPE.VIDEO == task.getType()){
			taskThumbnail = TaskThumnailUtil.getVideoThumbnail(taskPath);
		}else if(Task.TYPE.IMAGE == task.getType()){
			taskThumbnail = TaskThumnailUtil.getImageThumbnail(task.getTaskPath());
		}*/
		taskThumbnail = TaskThumnailUtil.getImageThumbnail(task.getTaskPath());
		if(null == taskThumbnail){
			taskThumbnail = TaskThumnailUtil.getVideoThumbnail(taskPath);
		}
		
		if(null != taskThumbnail){
			ImageCacheProxy.putBitmap(mContext, cacheKey, taskThumbnail);
		}
		return taskThumbnail;
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
		final ArrayList<TaskViewHolder> views = HolderViews ;
		for(TaskViewHolder holder : views){
			Task task = holder.task ;
			if(task != null && task.getTransferUID().equals(uid)){
				task.setTaskLength(length);
				task.setTaskProgress(pro);
				task.setState(Task.STATE.RUNNING);
				if(pro >= 100){
					text = mContext.getString(R.string.ugc_label_task_verify) ;
				} else {
					text = getUploadDetail(task);///*+ " " + mContext.getString(R.string.label_task_live_speed)*/ + " " + pro + "%";
				}
				//
				//holder.tvTaskStatus.setText(pro + "%");
				//holder.task_state_bnt.setBackgroundResource(R.drawable.ugc_ic_ioffer_upload_pause_icon);
				holder.rlFailedLayout.setVisibility(View.GONE);
				holder.rlFinishLayout.setVisibility(View.GONE);
				holder.rlProgressLayout.setVisibility(View.VISIBLE);
				holder.progressText.setText(task.getTaskProgress() + "%");
				
				holder.tvTaskDetail.setText(text);
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
	
	
	public final void changeTaskState(Task task){
		
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
	
	final class TaskViewHolder {
		Task		task ;
		ImageView 	ivKeyframe ;
		RelativeLayout rlKeyframeLayout ;
		TextView    tvTaskTitle;
		TextView    tvTaskDetail;
		TextView    tvTaskDelete;
		RelativeLayout rlFailedLayout;
		TextView    tvFailedText;
		RelativeLayout rlFinishLayout;
		RelativeLayout rlProgressLayout;
		RelativeLayout rlStatusLayout;
		ProgressBar progressBar;
		TextView    progressText;
	}
}
