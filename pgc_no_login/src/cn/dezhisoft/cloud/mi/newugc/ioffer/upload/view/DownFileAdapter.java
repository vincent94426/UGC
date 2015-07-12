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

import cn.dezhisoft.cloud.mi.newugc.ioffer.upload.ui.IDownStateListener;
import cn.dezhisoft.cloud.mi.newugc.ugc.core.down.IFileDownload.DownloadMessage;
import cn.dezhisoft.cloud.mi.newugc.ugc.core.model.AssetsType;
import cn.dezhisoft.cloud.mi.newugc.ugc.core.model.AssetsType.DownloadState;
import cn.dezhisoft.cloud.mi.newugc.ugc.core.model.MediaType;

/**
 * Download File Adapter
 * 
 * @author Rosson Chen
 *
 */
public final class DownFileAdapter extends BaseAdapter implements View.OnClickListener{
	
	public static final int 	ADAPTER_STATE_NORMAL 	= 0 ;
	public static final int 	ADAPTER_STATE_EDIT 		= 1 ;
	static final  int 			B_LENGTH				= 1024 * 1024 ;
	static final DecimalFormat FLOAT_FORMAT				= new DecimalFormat("0.00");
	
	private LayoutInflater 				mInflater ;
	private final ArrayList<AssetsType> mTaskList = new ArrayList<AssetsType>();
	private final ArrayList<AssetsType> mTempList = new ArrayList<AssetsType>();
	private final ArrayList<HolderView> HolderViews = new ArrayList<HolderView>();
	private IDownStateListener 			mTaskStateListener ;
	private int 						mFlag ;
	private Context	mContext ;
	
	protected DownFileAdapter(Context context){
		mTaskList.clear() ;
		mTempList.clear() ;
		mInflater 	= LayoutInflater.from(context);
		mFlag		= ADAPTER_STATE_NORMAL ;
		HolderViews.clear() ;
		mContext	= context ;
	}
	
	protected final void setTaskStateChange(IDownStateListener listener){
		mTaskStateListener = listener ;
	}
	
	@Override
	public int getCount() {
		return mTempList != null ? mTempList.size() : 0;
	}

	@Override
	public AssetsType getItem(int position) {
		return mTempList != null ? mTempList.get(position) : null;
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
		final AssetsType task = getItem(position);
		//
		holderview.task = task ;
		// icon
		updataTaskIcon(holderview,task);
		//
		switch(mFlag){
		case ADAPTER_STATE_NORMAL :
			updateHolderView(holderview,task,true);
			break ;
		case ADAPTER_STATE_EDIT :
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
	
	private final void updateHolderView(HolderView holderview,final AssetsType assets,boolean hasListener){
		
		int state 		= assets.getState() ;
		
		switch(state){
		case DownloadState.NEW :
		case DownloadState.RUNNING :
			holderview.history_layout.setVisibility(View.GONE);
			holderview.history_state_layout.setVisibility(View.GONE);
			holderview.remove_layout.setVisibility(View.GONE);
			holderview.task_content_layout.setVisibility(View.VISIBLE);
			holderview.taks_state_layout.setVisibility(View.VISIBLE);
			holderview.task_title.setText(assets.getIndentifier());
			holderview.task_state_bnt.setBackgroundResource(R.drawable.ugc_ic_ioffer_upload_pause_icon);
			holderview.task_progress.setProgress(assets.getProgress());
			holderview.task_detail.setText(getUploadDetail(assets));
			if(hasListener) {
				holderview.task_state_bnt.setTag(assets);
				holderview.task_state_bnt.setOnClickListener(this) ;
			} else {
				holderview.task_state_bnt.setTag(null);
				holderview.task_state_bnt.setOnClickListener(null);
			}
			break ;
		case DownloadState.STOP :
			holderview.history_layout.setVisibility(View.GONE);
			holderview.history_state_layout.setVisibility(View.GONE);
			holderview.remove_layout.setVisibility(View.GONE);
			holderview.task_content_layout.setVisibility(View.VISIBLE);
			holderview.taks_state_layout.setVisibility(View.VISIBLE);
			holderview.task_title.setText(assets.getIndentifier());
			holderview.task_state_bnt.setBackgroundResource(R.drawable.ugc_ic_ioffer_upload_resume_icon);
			holderview.task_progress.setProgress(assets.getProgress());
			holderview.task_detail.setText(getUploadDetail(assets));
			if(hasListener) {
				holderview.task_state_bnt.setTag(assets);
				holderview.task_state_bnt.setOnClickListener(this) ;
			} else {
				holderview.task_state_bnt.setTag(null);
				holderview.task_state_bnt.setOnClickListener(null);
			}
			break ;
		case DownloadState.WAIT :
			holderview.history_layout.setVisibility(View.GONE);
			holderview.history_state_layout.setVisibility(View.GONE);
			holderview.remove_layout.setVisibility(View.GONE);
			holderview.task_content_layout.setVisibility(View.VISIBLE);
			holderview.taks_state_layout.setVisibility(View.VISIBLE);
			holderview.task_title.setText(assets.getIndentifier());
			holderview.task_state_bnt.setBackgroundResource(R.drawable.ugc_ic_ioffer_down_wait_icon);
			holderview.task_progress.setProgress(assets.getProgress());
			holderview.task_detail.setText(getUploadDetail(assets));
			if(hasListener) {
				holderview.task_state_bnt.setTag(assets);
				holderview.task_state_bnt.setOnClickListener(this) ;
			} else {
				holderview.task_state_bnt.setTag(null);
				holderview.task_state_bnt.setOnClickListener(null);
			}
			break ;
		case DownloadState.DONE :
			holderview.remove_layout.setVisibility(View.GONE);
			holderview.taks_state_layout.setVisibility(View.GONE);
			holderview.task_content_layout.setVisibility(View.GONE);
			holderview.history_layout.setVisibility(View.VISIBLE);
			holderview.history_state_layout.setVisibility(View.VISIBLE);
			holderview.history_title.setText(assets.getIndentifier());
			holderview.history_comment.setText("");
			holderview.history_state.setText(R.string.ugc_label_task_complete);
			holderview.task_state_bnt.setBackgroundResource(R.drawable.ugc_ic_ioffer_upload_resume_icon);
			updataTaskIcon(holderview,assets);
			updataTaskDoneCategory(holderview,assets);
			break ;
		}
	}
	
	@Override
	public void onClick(View v) {
		Object obj = v.getTag() ;
		if(obj instanceof AssetsType){
			updateTaskState((AssetsType)obj);
		}
	}

	private void updataTaskIcon(HolderView holderview,AssetsType task){
		MediaType type	= task.getMediaType() ;
		if(type == MediaType.AUDIO){
			holderview.common_icon.setBackgroundResource(R.drawable.ugc_ic_ioffer_content_category_a_icon);
		} else if(type == MediaType.VIDEO){
			holderview.common_icon.setBackgroundResource(R.drawable.ugc_ic_ioffer_content_category_v_icon);
		}else if(type == MediaType.IMAGE){
			holderview.common_icon.setBackgroundResource(R.drawable.ugc_ic_ioffer_content_category_i_icon);
		}
	}
	
	private void updataTaskDoneCategory(HolderView holderview,AssetsType task){
		MediaType type	= task.getMediaType() ;
		if(type == MediaType.AUDIO){
			holderview.history_category.setBackgroundResource(R.drawable.ugc_ic_ioffer_content_s_audio);
		} else if(type == MediaType.VIDEO){
			holderview.history_category.setBackgroundResource(R.drawable.ugc_ic_ioffer_content_s_video);
		} else if(type == MediaType.IMAGE){
			holderview.history_category.setBackgroundResource(R.drawable.ugc_ic_ioffer_content_s_image);
		}
	}
	
	private String getUploadDetail(AssetsType task){
		String detail 	= null ;
		int total		= task.getTotal() ;
		int prog		= task.getProgress() ;
		if(total > 0) {
			float p = (prog < 0 ) ? (0.00f) : ((float)prog / 100) ;
			detail  = FLOAT_FORMAT.format((p * total) / B_LENGTH) + "MB / " + FLOAT_FORMAT.format(((float)total / B_LENGTH)) + "MB";
		}
		return detail != null ? detail : task.getDetail() ;
	}
	
	public final void loadUploadTasks(ArrayList<AssetsType> list){
		
		if(list != null){
			
			final ArrayList<AssetsType> 	l = mTaskList ;
			
			for(AssetsType task: list){
				l.add(task) ;
			}
			
			// 数据变化
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
	
	public synchronized final void updateProgress(String url,int progress,int length){
		
		int pro = progress >= 100 ? 100 : progress ;
		String text  = "";
		final ArrayList<HolderView> views = HolderViews ;
		
		for(HolderView holder : views){
			AssetsType task = holder.task ;
			if(task != null && task.getDownloadUrl().equals(url)){
				
				// 基本信息
				task.setTotal(length);
				task.setProgress(pro);
				
				if(pro >= 100){
					task.setState(DownloadState.DONE);
					text = mContext.getString(R.string.ugc_label_task_complete) ;
				} else {
					task.setState(DownloadState.RUNNING);
					text = getUploadDetail(task) + " " + pro + "%";
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
	
	public final void updateCompleteTask(boolean verify,String url){
		
		final ArrayList<AssetsType> 	list = mTaskList ;
		
		for(AssetsType task : list){
			if(task.getDownloadUrl().equals(url)){
				task.setProgress(100);
				if(verify)
					task.setState(DownloadState.DONE);
				else
					task.setState(DownloadState.STOP);
				break ;
			}
		}
		
		// 数据变化
		notifyTaskListChange() ;
	}
	
	public final void updateTaskState(DownloadMessage msg){
		
		String 		url 	= msg.mUrl ;
		int	state 			= msg.mState ;
		final ArrayList<HolderView> views = HolderViews ;
		
		for(HolderView holder : views){
			
			AssetsType task = holder.task ;
			
			if(task != null && task.getDownloadUrl().equals(url)){
				
				task.setProgress(msg.mProgress);
				task.setState(state);
				task.setDetail(msg.mMessage);
				
				if(state == DownloadState.NEW || state == DownloadState.RUNNING){
					holder.task_state_bnt.setBackgroundResource(R.drawable.ugc_ic_ioffer_upload_pause_icon);
					holder.task_state_bnt.invalidate() ;
				} else if(state == DownloadState.WAIT){
					holder.task_state_bnt.setBackgroundResource(R.drawable.ugc_ic_ioffer_upload_wait_icon);
					holder.task_state_bnt.invalidate() ;
				} else if(state == DownloadState.STOP){
					holder.task_state_bnt.setBackgroundResource(R.drawable.ugc_ic_ioffer_upload_resume_icon);
					holder.task_detail.setText(msg.mMessage);
					holder.task_detail.invalidate() ;
					holder.task_state_bnt.invalidate() ;
				} else if(state == DownloadState.DONE){
					notifyTaskListChange() ;
				}
				break ;
			}
		}
	}
	
	private final void updateTaskState(AssetsType task){
		
		final IDownStateListener listener = mTaskStateListener ;
		int state	= task.getState() ;
		
		// 运行状态
		if(state == DownloadState.NEW 
				|| state == DownloadState.RUNNING 
				|| state == DownloadState.WAIT){
			
			if(listener != null) 
				listener.change(task, state, DownloadState.STOP);
			
			task.setState(DownloadState.STOP);
			
		} 
		// 停止状态
		else if(state == DownloadState.STOP){
			
			if(listener != null)
				listener.change(task, state, DownloadState.WAIT);
			
			task.setState(DownloadState.WAIT);
		}
		
		notifyDataSetChanged();
	}
	
	private final void removeTask(AssetsType task){
		
		final ArrayList<AssetsType> 	list = mTaskList ;
		final ArrayList<AssetsType> 	temp = mTempList ;
		final IDownStateListener listener = mTaskStateListener ;
		
		if(listener != null) listener.remove(task);
		
		list.remove(task);
		temp.remove(task);
		
		notifyDataSetChanged();
	}
	
	/** 重新排序 */
	private void notifyTaskListChange(){
		
		mTempList.clear() ;
		
		final ArrayList<AssetsType> list = mTaskList ;
		final ArrayList<AssetsType> temp = mTempList ;
		
		// 排序
		Collections.sort(list, new Comparator<AssetsType>() {

			@Override
			public int compare(AssetsType t1, AssetsType t2) {
				if (t1.getId() < t2.getId()) {
					return 1;
				} else {
					return -1;
				}
			}
		});
		
		// 正在上传
		for(AssetsType task : list){
			if(task.getState() == DownloadState.RUNNING){
				temp.add(task);
			}
		}
		// 等待
		for(AssetsType task : list){
			int state = task.getState() ;
			if(!temp.contains(task) && (state == DownloadState.WAIT || state == DownloadState.NEW)){
				temp.add(task);
			}
		}
		// 停止
		for(AssetsType task : list){
			if(!temp.contains(task) && (task.getState() == DownloadState.STOP)){
				temp.add(task);
			}
		}
		// 完成
		for(AssetsType task : list){
			if(!temp.contains(task) && (task.getState() == DownloadState.DONE)){
				temp.add(task);
			}
		}
		//
		notifyDataSetChanged() ;
	}
	
	final class HolderView {
		AssetsType	task ;
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
