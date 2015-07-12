package cn.dezhisoft.cloud.mi.newugc.ioffer.upload.ui;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.widget.TextView;

import cn.dezhisoft.cloud.mi.newugc.R;

import com.sobey.android.ui.ActivityManager;
import cn.dezhisoft.cloud.mi.newugc.ioffer.upload.view.DownFileAdapter;
import cn.dezhisoft.cloud.mi.newugc.ioffer.upload.view.DownFileListView;
import cn.dezhisoft.cloud.mi.newugc.ioffer.upload.view.UploadFileAdapter;
import cn.dezhisoft.cloud.mi.newugc.ugc.core.down.IFileDownload;
import cn.dezhisoft.cloud.mi.newugc.ugc.core.down.IFileDownload.DownloadMessage;
import cn.dezhisoft.cloud.mi.newugc.ugc.core.down.IFileDownload.IDownloadCallback;
import cn.dezhisoft.cloud.mi.newugc.ugc.core.down.UGCDownFileService;
import cn.dezhisoft.cloud.mi.newugc.ugc.core.model.AssetsType;
import cn.dezhisoft.cloud.mi.newugc.ugc.core.model.AssetsType.DownloadState;

/**
 * 
 * @author Rosson Chen
 *
 */
public final class UGCDownListActivity extends UGCBaseActivity {
	
	static final int MSG_PROGRESS	= 1 ;
	static final int MSG_UPDATE		= 2 ;

	IFileDownload mDownProxy ;
	DownFileListView mListView ;
	TextView		mBntEdit ;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ugc_down_file_list_layout);
		
		mListView	= (DownFileListView)findViewById(R.id.down_list_view);
		mBntEdit	= (TextView)findViewById(R.id.bnt_down_edit);
		mListView.setStateListener(listener);
		
		mDownProxy	= UGCDownFileService.getUGCFileDownloadImpl();
		
		if(mDownProxy != null){
			mDownProxy.setCallback(callback);
			mListView.loadAssetsTypeList(mDownProxy.getHistoryList());
		}
	}

	public void buttonOnclick(View v){
		final int id = v.getId() ;
		
		// �˳�
		if(id == R.id.bnt_down_back){
			ActivityManager.getActivityManager().backPrevious() ;
		}
		// �༭ģʽ
		else if(id == R.id.bnt_down_edit){
			
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
		
		if(mDownProxy != null){
			mDownProxy.setCallback(null);
		}
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		return false;
	}
	
	private IDownloadCallback callback = new IDownloadCallback() {
		
		@Override
		public void progress(AssetsType asset, int id, int percent, long total) {
			handler.obtainMessage(MSG_PROGRESS, percent, (int)total, asset.getDownloadUrl()).sendToTarget();
		}
		
		@Override
		public void stateCallback(AssetsType asset, String msg) {
			DownloadMessage m = new DownloadMessage() ;
			m.mUrl		= asset.getDownloadUrl() ;
			m.mMessage 	= msg ;
			m.mState	= asset.getState() ;
			m.mProgress = asset.getProgress() ;
			m.assets	= asset ;
			handler.obtainMessage(MSG_UPDATE,m).sendToTarget() ;
		}
	};
	
	private final IDownStateListener listener = new IDownStateListener() {
		
		@Override
		public void remove(AssetsType task) {
			if(mDownProxy != null){
				mDownProxy.removeAssetsType(task);
			}
		}
		
		@Override
		public void change(AssetsType task, int oldState, int newState) {
			if(newState == DownloadState.STOP){
				mDownProxy.stopDownload() ;
			} else if(newState == DownloadState.WAIT){
				mDownProxy.resumeAssetsType(task);
			}
		}
	};
	
	private final Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			
			final DownFileAdapter adapter = mListView.getAdapter() ;
			
			switch(msg.what){
			case MSG_PROGRESS :
				adapter.updateProgress(msg.obj.toString(), msg.arg1, msg.arg2);
				break ;
			case MSG_UPDATE :
				adapter.updateTaskState((DownloadMessage)msg.obj);
				break ;
			}
		}
		
	} ;
}
