package cn.dezhisoft.cloud.mi.newugc.ugv2.view.metro.fragment;

import java.util.Timer;
import java.util.TimerTask;

import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.animation.LayoutAnimationController;
import android.widget.TextView;
import android.widget.Toast;
import cn.dezhisoft.cloud.mi.newugc.R;
import cn.dezhisoft.cloud.mi.newugc.ugv2.view.metro.anim.Rotate3dAnimation;
import cn.dezhisoft.cloud.mi.newugc.ugv2.view.metro.view.AllSyncMetroItemView;
import cn.dezhisoft.cloud.mi.newugc.ugv2.view.metro.view.AutoFillMetroView;
import cn.dezhisoft.cloud.mi.newugc.ugv2.view.metro.view.AutoFillMetroView.MetroEvent;
import cn.dezhisoft.cloud.mi.newugc.ugv2.view.metro.view.MetroItemView;
import cn.dezhisoft.cloud.mi.newugc.ugv2.view.metro.view.MetroView;
import cn.dezhisoft.cloud.mi.newugc.ugv2.view.metro.view.MetroView.OnMoveListenner;
import cn.dezhisoft.cloud.mi.newugc.ugv2.view.metro.view.MetroView.OrientationType;


public class MetroFragment extends Fragment {
	private static final int METRO_CONTACT_ID = R.drawable.metro_main_contact;
	//delay, long period
	private final static int TIMER_FIRST_DELAY = 3000;
	private final static int TIMER_EVENT_PERIOD = 1000;
	private final static int WHAT_TIMER = 1;
	private final static int WHAT_EVENT = 2;
	private final static int WHAT_EVENT_UPDATE_ACCOUNT = 3;
	
	private static final int TWO_SPAN = 2;
	private static final int ONE_SPAN = 1;
	
	private View mContent;
	private LayoutInflater inflater;
	
	private OnTouchListener mOnMetroItemTouchListener;
	private TextView mTextView;
	
	private boolean mTimerEnabled = false;
	private Timer mTimer;
	private OnMoveListenner mOnMoveListenner ;
	private boolean intoContactCheck = false;
	private MetroEvent mContactEvent = null;
	
	
	private Handler mHandler = new Handler() {
		
		@Override
		public void handleMessage(Message msg) {
			if(msg.what == WHAT_EVENT) {
				onEventMessage(msg);
			} else if(msg.what == WHAT_TIMER) {
				Integer ticket = (Integer) msg.obj;
				onTimerMessage(msg, ticket);
			} else if(msg.what == WHAT_EVENT_UPDATE_ACCOUNT) {
				if(msg.obj != null)
					updateAccountName((String) msg.obj);
			}
		}
	};
	private AutoFillMetroView mMetro;
	
	/*public void setOnMetroItemClickListenner(OnClickListener mOnMetroItemClickListenner) {
		this.mOnMetroItemClickListenner = mOnMetroItemClickListenner;
	}*/
	
	public void setOnMetroItemTouchListener(OnTouchListener mOnMetroItemTouchListener){
		this.mOnMetroItemTouchListener = mOnMetroItemTouchListener;
	}
	
	public void setOnMoveListenner(OnMoveListenner mOnMoveListenner) {
		this.mOnMoveListenner = mOnMoveListenner;
	}
	
	protected void onEventMessage(Message msg) {
	}
	
	protected void onTimerMessage(Message msg, Integer ticket) {
		if(ticket % 5 == 0) {
			//updateAccountName(Devices.userId);
		}
		
		if(ticket % 5 == 0) {
			if(!intoContactCheck) {
				intoContactCheck = true;
				checkAndShowContactAnimation();
			}
		}
		
		if(ticket % 3 == 0) {
			mMetro.onUpdateMetreItem(METRO_CONTACT_ID, mContactEvent);
		}
	}
	
	private void checkAndShowContactAnimation(){
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
		mContent = inflater.inflate(R.layout.ugv2_navigation_layout, container, false);
		this.inflater = inflater;
		return mContent;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		initViews();
		
		mTimer = new Timer("Heart#Timer");
		mTimer.schedule(new TimerTask() {
			private int ticket = 0;
			
			@Override
			public void run() {
				if(mTimerEnabled) {
					Message message = mHandler.obtainMessage(WHAT_TIMER, Integer.valueOf(ticket++));
					message.sendToTarget();
				}
			}
			
		}, TIMER_FIRST_DELAY, TIMER_EVENT_PERIOD);
		updateAccountName("用户名称");
		runAnimation(mMetro, new LayoutAnimationController(Rotate3dAnimation.GetFlyInAnimation()));
	}
	
	@Override
	public void onPause() {
		mTimerEnabled = false;
		super.onPause();
	}

	
	@Override
	public void onResume() {
		super.onResume();
		mTimerEnabled = true;
		
		updateAccountName("用户名称");
		
		if(!intoContactCheck) {
			intoContactCheck = true;
			checkAndShowContactAnimation();
		}
		
	}
	
	public void runAnimation(ViewGroup v,LayoutAnimationController localLayoutAnimationController){
		mTimerEnabled = true;
		localLayoutAnimationController.setOrder(2);
		localLayoutAnimationController.setDelay(0.3F);
		v.setLayoutAnimation(localLayoutAnimationController);
	}
	
	@Override
	public void onDestroy() {
		if(mTimer != null) {
			mTimer.purge();
		}
		super.onDestroy();
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		setRowsColumns(newConfig);
		super.onConfigurationChanged(newConfig);
	}
	
	private void setRowsColumns(Configuration config) {
		mMetro = (AutoFillMetroView)mContent.findViewById(R.id.metro_ui);
		if (config.orientation == Configuration.ORIENTATION_LANDSCAPE)
			mMetro.setVisibleItems(2, 3);
		else
			mMetro.setVisibleItems(8, 2);
	}
	
	private void initViews(){
		mMetro = (AutoFillMetroView)mContent.findViewById(R.id.metro_ui);
		mMetro.setOnMoveListenner(mOnMoveListenner);
		
		mMetro.setOrientation(OrientationType.Vertical);
		setRowsColumns(getResources().getConfiguration());
		Resources resources = getResources();
		mMetro.addAutoFillMetroItem(new MetroItemView(inflater, TWO_SPAN, ONE_SPAN, resources.getDrawable(R.drawable.metro_item_contact_bag_selector), R.drawable.metro_main_contact, R.string.metro_module_contact, 0, mOnMetroItemTouchListener));
		mMetro.addAutoFillMetroItem(new MetroItemView(inflater, TWO_SPAN, ONE_SPAN, resources.getDrawable(R.drawable.metro_item_photo_bag_selector), R.drawable.metro_main_photo, R.string.metro_module_photo, 0, mOnMetroItemTouchListener));
		mMetro.addAutoFillMetroItem(new AllSyncMetroItemView(inflater, TWO_SPAN, TWO_SPAN, resources.getDrawable(R.drawable.metro_item_allsync_bag_selector), R.drawable.metro_main_full_sync, R.string.metro_module_sync, 0, mOnMetroItemTouchListener));
		mMetro.addAutoFillMetroItem(new MetroItemView(inflater, TWO_SPAN, ONE_SPAN, resources.getDrawable(R.drawable.metro_item_doc_bag_selector), R.drawable.metro_main_document, R.string.metro_module_doc, 0, mOnMetroItemTouchListener));
		mMetro.addAutoFillMetroItem(new MetroItemView(inflater, TWO_SPAN, ONE_SPAN, resources.getDrawable(R.drawable.metro_item_app_bag_selector), R.drawable.metro_main_app, R.string.metro_module_app, 0, mOnMetroItemTouchListener));
		mMetro.addAutoFillMetroItem(new MetroItemView(inflater, TWO_SPAN, ONE_SPAN, resources.getDrawable(R.drawable.metro_item_sms_bag_selector), R.drawable.metro_main_sms, R.string.metro_module_sms, 0, mOnMetroItemTouchListener));
		mMetro.addAutoFillMetroItem(new MetroItemView(inflater, ONE_SPAN, ONE_SPAN, resources.getDrawable(R.drawable.metro_item_calllog_bag_selector), R.drawable.metro_main_calllog, 0, 0, mOnMetroItemTouchListener));
		mMetro.addAutoFillMetroItem(new MetroItemView(inflater, ONE_SPAN, ONE_SPAN, resources.getDrawable(R.drawable.metro_item_mms_bag_selector), R.drawable.metro_main_mms, 0, 0, mOnMetroItemTouchListener));
	}
	
	protected void onAccountBtnClick(View v) {
		//PsAuthenServiceL.showAccountPage(getActivity(), AppConstants.CONTACT_RID);
		Toast.makeText(getActivity(), "点击了登录按钮", Toast.LENGTH_SHORT).show();
	}
	
	protected void updateAccountName(String account) {
		if(TextUtils.isEmpty(account)) {
			mTextView.setText("请登录");
		} else {
			mTextView.setText(account);
		}
	}
	
	public MetroView getMetroView(){
		return mMetro;
	}
	
	
	
}
