package cn.dezhisoft.cloud.mi.newugc.ugv2.base.activity;

import com.baidu.mobstat.StatService;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.LinearLayout;
import cn.dezhisoft.cloud.mi.newugc.R;
import cn.dezhisoft.cloud.mi.newugc.ugv2.base.app.AppStateListener;
import cn.dezhisoft.cloud.mi.newugc.ugv2.base.app.AppUtils;
import cn.dezhisoft.cloud.mi.newugc.ugv2.base.view.UiTopBar;
import cn.dezhisoft.cloud.mi.newugc.ugv2.base.view.UiTopBar.OnClickTopButtonListener;

public abstract class BaseActivity extends AbstractBuzzActivity implements AppStateListener{
	protected UiTopBar topBar;
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		
		AppUtils.unregisterAppStateListener(this);
	}
	
	@Override
	protected void onResume() {
	    super.onResume();
	    StatService.onResume(this);
	}
	
	@Override
	protected void onPause() {
	    super.onPause();
	    StatService.onPause(this);
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.ugv2_base_layout);
		
		initTopView();
		
		initBodyView();
		
		AppUtils.registerAppStateListener(this);
		Log.d("BaseActivity", getClass().getSimpleName());
	}
	
	private void initTopView() {
		topBar = (UiTopBar)findViewById(R.id.ugv2_header);
		topBar.setBackCallOnClick(new OnClickTopButtonListener() {
			
			@Override
			public void onClickRightBtn(View v) {
				onRightBtnClick(v);
			}
			
			@Override
			public void onClickLeftBtn(View v) {
				onLeftBtnClick(v);
			}
			
			@Override
			public void onClickBackBtn(View v) {
				onBackBtnClick();
			}
		});
	}
	
	public void setUiTitle(int resId){
		topBar.setTitle(resId);
	}
	
	public void setUiTitle(CharSequence title){
		topBar.setTitle(title);
	}

    public void setBackBtnVisibility(int visibility){
    	this.topBar.setBackBtnVisibility(visibility);
    }
    
    public void setTitleVisibility(int visibility){
    	this.topBar.setTitleVisibility(visibility);
    }
    
    public void setLeftBtnVisibility(int visibility){
    	this.topBar.setLeftBtnVisibility(visibility);
    }
    
    public void setRightBtnVisibility(int visibility){
    	this.topBar.setRightBtnVisibility(visibility);
    }
    
	private void initBodyView() {
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
		LinearLayout listBody = (LinearLayout)findViewById(R.id.ugv2_body);
		listBody.addView(onCreateBody(), params);
	}

	protected void onBackBtnClick() {
		finish();
	}
	
	protected void onRightBtnClick(View v) {
		// TODO Auto-generated method stub
		
	}

	protected void onLeftBtnClick(View v) {
		finish();
	}
	
	@Override
	public void onExit() {
		finish();
	}
	
	protected abstract View onCreateBody();
}
