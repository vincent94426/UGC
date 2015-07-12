package cn.dezhisoft.cloud.mi.newugc.ioffer.upload.ui;

import java.util.ArrayList;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import cn.dezhisoft.cloud.mi.newugc.R;

import com.sobey.android.ui.ActivityManager;
import cn.dezhisoft.cloud.mi.newugc.ioffer.upload.view.UploadFileAdapter;
import cn.dezhisoft.cloud.mi.newugc.ioffer.upload.view.UploadFileListView;
import cn.dezhisoft.cloud.mi.newugc.ugc.core.model.Metadata;
import cn.dezhisoft.cloud.mi.newugc.ugc.core.model.Task;
import cn.dezhisoft.cloud.mi.newugc.ugc.core.model.Task.STATE;
import cn.dezhisoft.cloud.mi.newugc.ugc.core.upload.IFileUpload;
import cn.dezhisoft.cloud.mi.newugc.ugc.core.upload.IFileUpload.IFileUploadCallback;
import cn.dezhisoft.cloud.mi.newugc.ugc.core.upload.IFileUpload.UploadMessage;
import cn.dezhisoft.cloud.mi.newugc.ugc.core.upload.UGCUploadFileService;

/**
 * UGC Upload List Activity
 * 
 * @author Rosson Chen
 *
 */
public final class UGCUploadListActivity extends UGCBaseActivity {
	
	static final int	MSG_PROGRESS			= 1 ;
	static final int	MSG_VERIFY_WAIT			= 2 ;
	static final int	MSG_VERIFY_SUCCESS		= 3 ;
	static final int	MSG_VERIFY_FAILED		= 4 ;
	static final int	MSG_VERIFY_TIMEOUT		= 5 ;
	static final int	MSG_STATE_CHANGE		= 6 ;
	
	IFileUpload 					mUploadProxy ;
	UploadFileListView				mListView ;
	ArrayList<Task> 				mList ;
	ArrayList<Metadata> 			mMetadataList ;	
	TextView						mBntEdit ;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON); 
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ugc_upload_file_list_layout);
		//
		mListView		= (UploadFileListView) findViewById(R.id.upload_list_view);
		mBntEdit		= (TextView)findViewById(R.id.bnt_upload_edit);
		mListView.setStateListener(listener) ;
		
		mUploadProxy 	= UGCUploadFileService.getUGCFileUpload() ;
		
		if(mUploadProxy != null){
			mListView.loadUploadList(mUploadProxy.getHistoryList());
			mUploadProxy.setCallback(callback);
		} else {
			System.out.println("����û������");
		}
	}
	
	public void buttonOnclick(View v){
		
		final int id = v.getId() ;
		
		if(id == R.id.bnt_upload_back){
			ActivityManager.getActivityManager().backPrevious() ;
		} else if(id == R.id.bnt_upload_edit){
			mListView.getAdapter().changeEditState() ;
			
			int state = mListView.getAdapter().getEditState() ;
			if(state == UploadFileAdapter.ADAPTER_STATE_EDIT){
				mBntEdit.setText(R.string.ugc_label_upload_edit_finish);
			} else {
				mBntEdit.setText(R.string.ugc_label_upload_edit);
			}
		}
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		
		if(mUploadProxy != null){
			mUploadProxy.setCallback(null);
		}
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		return false;
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
				mListView.getAdapter().updateProgress(msg.obj.toString(), msg.arg1, msg.arg2);
				break ;
			case MSG_STATE_CHANGE :
				mListView.getAdapter().updateTaskState((UploadMessage)msg.obj);
				break ;
			}
		}
	} ;
}
