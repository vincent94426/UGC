package cn.dezhisoft.cloud.mi.newugc.ugv2.base.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import cn.dezhisoft.cloud.mi.newugc.R;

/**
 * 界面Header区域
 * @author Rosson Chen
 */
public class UiTopBar extends LinearLayout {
	private Context _context;
    private TextView _titleView;
    private ImageView _backBtn;
    private TextView _leftBtn;
    private TextView _rightBtn;
    private OnClickTopButtonListener onBackCall;
    
    public UiTopBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        this._context = context;
        
        initTopView();
    }

    private void initTopView() {
        View rootView = LayoutInflater.from(this._context).inflate(R.layout.ugv2_ui_top_bar, null);
        this.addView(rootView, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT));
        
        if (rootView != null) {
            this._titleView = (TextView) this.findViewById(R.id.ugv2_top_title);
            this._backBtn = (ImageView) this.findViewById(R.id.ugv2_top_backBtn);
            this._leftBtn = (TextView) this.findViewById(R.id.ugv2_top_left_btn);
            this._rightBtn = (TextView) this.findViewById(R.id.ugv2_top_right_btn);
        }
        
        this._backBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(onBackCall!=null)
					onBackCall.onClickBackBtn(v);
			}
		});
        this._leftBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(onBackCall!=null)
					onBackCall.onClickLeftBtn(v);
			}
		});
        this._rightBtn.setOnClickListener(new OnClickListener() {
	
			@Override
			public void onClick(View v) {
				if(onBackCall!=null)
					onBackCall.onClickRightBtn(v);
			}
		});
    }

    
    /**
     * 设置按钮点击回调接口
     * @param onBackCall
     */
    public void setBackCallOnClick(OnClickTopButtonListener onBackCall){
    	this.onBackCall = onBackCall;
    }

    /**
     * 针对top bar底部存在按钮模块
     * 改变top bar背景图
     */
    public void changeBackground(){
    	changeBackground(R.drawable.ugv2_top_bg_t);
    }
    
    public void changeBGtoNormal(){
    	changeBackground(R.drawable.ugv2_top_bg);
    }
    /**
     * 改变top bar背景图
     */
    public void changeBackground(int resId){
    	findViewById(R.id.topBarBg).setBackgroundResource(resId);
    }

    
    public void setTitle(int resTitle) {
        this._titleView.setText(resTitle);
    }

	public void setTitle(CharSequence title) {
		this._titleView.setText(title);		
	}
    
    public void setBackBtnVisibility(int visibility){
    	this._backBtn.setVisibility(visibility);
    }
    public void setLeftBtnVisibility(int visibility){
    	this._leftBtn.setVisibility(visibility);
    }
    public void setRightBtnVisibility(int visibility){
    	this._rightBtn.setVisibility(visibility);
    }
    public void setTitleVisibility(int visibility){
    	this._titleView.setVisibility(visibility);
    }
    
    /**
     * 
     * List Top Bar 点击按钮事件回调接口
     *
     */
    public interface OnClickTopButtonListener{
    	/**
    	 * 点击左侧按钮回调
    	 * @param v
    	 */
    	void onClickLeftBtn(View v);
    	/**
    	 * 点击返回按钮回调
    	 * @param v
    	 */
    	void onClickBackBtn(View v);
    	/**
    	 * 点击右侧按钮回调
    	 * @param v
    	 * @param checkAll 是否全选状态
    	 */
    	void onClickRightBtn(View v);
    }
}