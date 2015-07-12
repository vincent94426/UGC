package cn.dezhisoft.cloud.mi.newugc.ugv2.activity;

import java.util.ArrayList;

import android.os.Handler;
import android.os.Message;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import cn.dezhisoft.cloud.mi.newugc.R;
import cn.dezhisoft.cloud.mi.newugc.common.util.ToastUtil;
import cn.dezhisoft.cloud.mi.newugc.ioffer.upload.ui.ITaskStateListener;
import cn.dezhisoft.cloud.mi.newugc.ugc.core.model.Task;
import cn.dezhisoft.cloud.mi.newugc.ugc.core.model.Task.STATE;
import cn.dezhisoft.cloud.mi.newugc.ugc.core.upload.IFileUpload;
import cn.dezhisoft.cloud.mi.newugc.ugc.core.upload.IFileUpload.IFileUploadCallback;
import cn.dezhisoft.cloud.mi.newugc.ugc.core.upload.IFileUpload.UploadMessage;
import cn.dezhisoft.cloud.mi.newugc.ugc.core.upload.UGCUploadFileService;
import cn.dezhisoft.cloud.mi.newugc.ugv2.base.activity.BaseActivity;
import cn.dezhisoft.cloud.mi.newugc.ugv2.view.adapter.TaskListAdapter;

public class TaskActivity extends BaseActivity {
	static final int	MSG_PROGRESS			= 1 ;
	static final int	MSG_VERIFY_WAIT			= 2 ;
	static final int	MSG_VERIFY_SUCCESS		= 3 ;
	static final int	MSG_VERIFY_FAILED		= 4 ;
	static final int	MSG_VERIFY_TIMEOUT		= 5 ;
	static final int	MSG_STATE_CHANGE		= 6 ;
	
	private IFileUpload 					mUploadProxy ;
	private ListView				        mListView ;
	private TaskListAdapter                mTaskAdapter;
	
	@Override
	protected View onCreateBody() {
		View body = getLayoutInflater().inflate(R.layout.ugv2_task_layout, null);
		
		mListView = (ListView)body.findViewById(R.id.ugv2_task_list_view);
		
		mUploadProxy 	= UGCUploadFileService.getUGCFileUpload() ;
		
		if(mUploadProxy != null){
			ArrayList<Task> taskList = mUploadProxy.getHistoryList();
			mTaskAdapter = new TaskListAdapter(getApplicationContext());
			mTaskAdapter.loadUploadTasks(taskList);
			mTaskAdapter.setTaskStateListener(listener);
			
			mListView.setAdapter(mTaskAdapter);
			/*mListView.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> arg0, View arg1,
						int position, long id) {
					mTaskAdapter.changeTaskState(mTaskAdapter.getItem(position));
					//ToastUtil.showToastShort(TaskActivity.this, "onclick");
				}
			});*/
			mListView.setOnTouchListener(new OnTouchListener() {
				float x, y, ux, uy;
				@Override
				public boolean onTouch(View v, MotionEvent event) {
					switch (event.getAction()) {
					case MotionEvent.ACTION_DOWN:
						x = event.getX();  
		                y = event.getY(); 
						//ToastUtil.showToastShort(TaskActivity.this, "onTouch down");
						break;
					case MotionEvent.ACTION_UP:
						ux = event.getX();  
		                uy = event.getY();  
		                int pos1 = ((ListView)v).pointToPosition((int) x, (int) y);//item的position  
		                int pos2 = ((ListView)v).pointToPosition((int) ux, (int) uy);  
		                if(pos1 >= 0 && pos1 == pos2){//相同Item
		                	if(Math.abs(x - ux) > 10){//滑動
		                		if(mTaskAdapter.getCurrentDelPosition() >= 0){//已經有刪除按鈕
		                			mTaskAdapter.setCurrentDelPosition(-1);
		                		}else {
		                			mTaskAdapter.setCurrentDelPosition(pos2);//沒有刪除按鈕，設置按鈕
		                		}
		                	}else {//點擊
		                		if(mTaskAdapter.getCurrentDelPosition() >= 0){//已經有刪除按鈕
		                			mTaskAdapter.setCurrentDelPosition(-1);
		                		}else {
		                			mTaskAdapter.changeTaskState(mTaskAdapter.getItem(pos2));//沒有刪除按鈕，切換狀態
		                		}
		                	}
		                	mTaskAdapter.notifyDataSetChanged();
		                }else {//down up 不在同一個Item，滑動
		                	if(mTaskAdapter.getCurrentDelPosition() >= 0){//已經有刪除按鈕
		                		mTaskAdapter.setCurrentDelPosition(-1);
		                		mTaskAdapter.notifyDataSetChanged();
		                	}else {//沒有刪除按鈕，do nothing
		                		
		                	}
		                }
						//ToastUtil.showToastShort(TaskActivity.this, "onTouch up");
						break;
					default:
						break;
					}
					return false;
				}
			});
			mUploadProxy.setCallback(callback);
		} else {
			ToastUtil.showToastShort(TaskActivity.this, getString(R.string.ugv2_txt_msg_error_param));
			finish();
		}
		
		setUiTitle(R.string.ugv2_txt_title_task);
		
		return body;
	}
	
	private final IFileUploadCallback callback = new IFileUploadCallback() {
		
		@Override
		public void progress(String tuid,long total,int parent,int id) {
			handler.obtainMessage(MSG_PROGRESS, parent, (int)total, tuid).sendToTarget();
		}
		
		@Override
		public void command(String tuid,int command, int code, String t1) {
			
		}

		@Override
		public void notifyChange(UploadMessage message) {
			handler.obtainMessage(MSG_STATE_CHANGE, message).sendToTarget() ;
		}
	};
	
	private final ITaskStateListener listener = new ITaskStateListener() {
		
		@Override
		public void change(Task task, int oldState, int newState) {
			if(oldState == STATE.RUNNING){
				mUploadProxy.stopCurrentTask() ;
			} else if(oldState == STATE.STOP){
				mUploadProxy.resumeTask(task);
			}
		}

		@Override
		public void remove(Task task) {
			mUploadProxy.removeTask(task) ;
		}
	};
	
	/** handler */
	private final Handler handler = new Handler(){

		@Override
		public void handleMessage(Message msg) {
			switch(msg.what){
			case MSG_PROGRESS :
				mTaskAdapter.updateProgress(msg.obj.toString(), msg.arg1, msg.arg2);
				break ;
			case MSG_STATE_CHANGE :
				mTaskAdapter.updateTaskState((UploadMessage)msg.obj);
				break ;
			}
		}
	} ;
	
	@Override
	protected void onResume() {
		super.onResume();
		topBar.setRightBtnVisibility(View.GONE);
	}

	@Override
	protected void onDestroy() {
		if(mUploadProxy != null){
			mUploadProxy.setCallback(null);
		}
		
		super.onDestroy();
	}

	@Override
	protected void onPause() {
		super.onPause();
	}
}
