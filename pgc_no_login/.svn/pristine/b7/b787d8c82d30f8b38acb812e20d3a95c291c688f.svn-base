package cn.dezhisoft.cloud.mi.newugc.ugv2.view.metro.fragment;

import java.util.HashMap;
import java.util.Map;

import android.app.Activity;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import cn.dezhisoft.cloud.mi.newugc.R;
import cn.dezhisoft.cloud.mi.newugc.ugv2.view.metro.view.MetroView.OnMoveListenner;

public abstract class BaseFragment extends Fragment {
	private static final int RES_NULL = 0;
	protected LayoutInflater inflater;
	protected Activity context ;
	private LinearLayout root;

	private String activityName;
	protected boolean isBackup;
	private boolean stackTop = true;
	private boolean autoReaper = true;


	protected int taskTypeValue, taskModuleValue;
	protected Map<String, String> statisticsData = new HashMap<String, String>();
	protected OnMoveListenner mOnMoveListenner;
	 
	public void setOnMoveListenner(OnMoveListenner mOnMoveListenner) {
		this.mOnMoveListenner = mOnMoveListenner;
	}
	
	@Override
	public void onCreate(Bundle savedBundle) {
		super.onCreate(savedBundle);
		context = getActivity();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
		if (null == container || null == inflater)
			throw new IllegalArgumentException("Fragment container is null");

		this.inflater = inflater;

		root = (LinearLayout) inflater.inflate(R.layout.lesync_common_fragment_container, container, false);
		LinearLayout fragmentContent = (LinearLayout)root.findViewById(R.id.lesync_common_fragment_content);

		LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.MATCH_PARENT,
				LinearLayout.LayoutParams.MATCH_PARENT);
		if (getChildView()!=null) {
			fragmentContent.addView(getChildView(),layoutParams);
		}
		
		onCreateViewBody(inflater, root, savedInstanceState);
		
		View view = root.findViewById(R.id.fragment_bottom_sawtooth);
		
		return root;
	}

	protected View getChildView() {
		View child = inflater.inflate(getChildResId(), null);
		LinearLayout numberLayout = (LinearLayout) child.findViewById(R.id.number_layout);
		
		if (getCloudResId() != RES_NULL) {
			numberLayout.addView(inflater.inflate(getCloudResId(), null));
		}

		if (getNumberResId() != RES_NULL) {
			numberLayout.addView(inflater.inflate(getNumberResId(), null));
		}
		
		return child;
	}

	protected int getCloudResId() {
		return 0;
	}

	protected int getChildResId() {
		return R.layout.lesync_fragment_item;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		
	}
	
	public boolean isStackTop(){
		return stackTop;
	}

	@Override
	public void onResume() {
		super.onResume();
	}
	
	@Override
	public void onPause() {
		super.onPause();
		
	}
	

	public void setAutoReaper(boolean autoReaper) {
		this.autoReaper = autoReaper;
	}

	public void setActivityName(String activityName) {
		this.activityName = activityName;
	}

	private String getActivityName() {
		if (TextUtils.isEmpty(activityName)) {
			String calssName = this.getClass().getName();
			int idx = -1;
			if (!TextUtils.isEmpty(calssName) && (idx = calssName.lastIndexOf(".")) != -1) {
				activityName = calssName.substring(idx + 1);
			} else {
				activityName = "";
			}
		}
		return activityName;
	}

//	protected void showLeftBackButton() {
//		leftBackbutton.setVisibility(View.VISIBLE);
//	}
//
//	protected void hiddenLeftBackButton() {
//		leftBackbutton.setVisibility(View.GONE);
//	}

	protected void toast(int resId, int duration) {
		Toast.makeText(getActivity(), resId, duration).show();
	}

	protected void toast(String text, int duration) {
		Toast.makeText(getActivity(), text, duration).show();
	}

	private void setTitle(int title) {
		/*TextView titleView = (TextView) root.findViewById(R.id.lesync_common_base_fragment_main_title);
		if (titleView != null) {
			titleView.setText(title);
		}*/
	}

	OnClickListener leftBackButtonListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			/*if(mOnMoveListenner != null) {
				mOnMoveListenner.onLeft();
			}
			sm.showMenu();
			*/
		}
	};

	
	public void dismissDialog(DialogInterface... dialogs) {
		if(dialogs == null) return;
		
		for (DialogInterface dialog : dialogs) {
			try {
				dialog.dismiss();
			} catch(Exception e) {}
		}
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
	}

	protected abstract int getTitleResId();
	protected abstract int getNumberResId();
	protected abstract CharSequence getProgressDialogTitile();
	protected abstract void onCreateViewBody(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState);
	protected abstract void refreshCloudData(Map<String, String> statisticsData);
	protected abstract void startTask(String realm, String lpsust);
	protected abstract void onStartBackup();
	protected abstract void onStartRestore();
}