package cn.dezhisoft.cloud.mi.newugc.ugv2.base.view;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
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
public class ListTopBar extends LinearLayout {
	private Context _context;
    private TextView _titleView;
    private ImageView _backBtn;
    private ImageView _cancelBtn;
    private ImageView _selectBtn;
    private boolean checkAll = false;
    private BackCallOnClickTopView onBackCall;
    
    public ListTopBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        this._context = context;
    }
    
    /**
     * 是否全选状态
     * @return
     */
    public boolean isCheckAll(){
    	return checkAll;
    }
    /**
     * 设置全选状态
     * @param checkAll
     */
    public void setCheckAll(boolean checkAll){
    	this.checkAll = checkAll;
    	if(_selectBtn!=null){
    		if(this.checkAll){
    			_selectBtn.setImageResource(R.drawable.ugv2_top_select_all_selector);
    		}else{
    			_selectBtn.setImageResource(R.drawable.ugv2_top_select_selector);
    		}
    	}    	
    }
    /**
     * 设置按钮点击回调接口
     * @param onBackCall
     */
    public void setBackCallOnClick(BackCallOnClickTopView onBackCall){
    	this.onBackCall = onBackCall;
    }

    public void initInflater() {
        this.initInflater(-1);
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

    public void initInflater(int resLayout) {
        if (resLayout == -1) {
            resLayout = R.layout.ugv2_top_bar;
        }
        View rootView = LayoutInflater.from(this._context).inflate(resLayout, null);
        this.addView(rootView, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT));
        if (rootView != null) {
            this._titleView = (TextView) this.findViewById(R.id.title);
            this._backBtn = (ImageView) this.findViewById(R.id.backBtn);
            this._cancelBtn = (ImageView) this.findViewById(R.id.cancelBtn);
            this._selectBtn = (ImageView) this.findViewById(R.id.selectBtn);
        }
        
        this._backBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(onBackCall!=null)
					onBackCall.onClickBackBtn(v);
			}
		});
        this._cancelBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(onBackCall!=null)
					onBackCall.onClickCancelBtn(v);
			}
		});
        this._selectBtn.setOnClickListener(new OnClickListener() {
	
			@Override
			public void onClick(View v) {
				checkAll=!checkAll;
				Log.i("Test", checkAll+"");
				setCheckAll(checkAll);
				if(onBackCall!=null)
					onBackCall.onClickSelectBtn(v, checkAll);
			}
		});
    }

    
    public void setTitle(int resTitle) {
        this._titleView.setText(resTitle);
    }

	public void setTitle(CharSequence title) {
		this._titleView.setText(title);		
	}
    
    public void showBackBtn(){
    	this._backBtn.setVisibility(View.VISIBLE);
    }
    public void showCancelBtn(){
    	this._cancelBtn.setVisibility(View.VISIBLE);
    }
    public void showSelectBtn(){
    	setCheckAll(false);
    	this._selectBtn.setVisibility(View.VISIBLE);
    }
    
    public void hideBackBtn(){
    	this._backBtn.setVisibility(View.GONE);
    }
    public void hideCancelBtn(){
    	this._cancelBtn.setVisibility(View.GONE);
    }
    public void hideSelectBtn(){
    	this._selectBtn.setVisibility(View.GONE);
    }
    public void showTitle(){
    	this._titleView.setVisibility(View.VISIBLE);
    }
    public void hideTitle(){
    	this._titleView.setVisibility(View.GONE);
    }
    public void enableBackBtn(boolean enable) {
    	this._backBtn.setEnabled(enable);
    }
    public void enableCancelBtn(boolean enable) {
    	this._cancelBtn.setEnabled(enable);
    }
    public void enableSelectBtn(boolean enable) {
    	this._selectBtn.setEnabled(enable);
    }
    /**
     * 显示取消、全选按钮，隐藏返回按钮
     */
    public void showEditButtons() {
    	hideBackBtn();
    	showCancelBtn();
    	showSelectBtn();
    }
    /**
     * 显示返回按钮，隐藏取消、全选按钮
     */
    public void hideEditButtons(){
    	hideCancelBtn();
    	hideSelectBtn();
    	showBackBtn();
    }
    
    public int getBackBtnVisibility(){
    	return this._backBtn.getVisibility();
    }
    public int getCancelBtnVisibility(){
    	return this._cancelBtn.getVisibility();
    }
    public int getSelectBtnVisibility(){
    	return this._selectBtn.getVisibility();
    }
    
    public void setBackBtnBg(int resId){
        this._backBtn.setImageResource(resId);
    }
    public void setCancelBtnBg(int resId){
        this._cancelBtn.setImageResource(resId);
    }
    public void setSelectBtnBg(int resId){
        this._selectBtn.setImageResource(resId);
    }
    
    /**
     * 
     * List Top Bar 点击按钮事件回调接口
     * 
     * @author CLB
     *
     */
    public interface BackCallOnClickTopView{
    	/**
    	 * 点击返回按钮回调
    	 * @param v
    	 */
    	void onClickBackBtn(View v);
    	/**
    	 * 点击取消按钮回调
    	 * @param v
    	 */
    	void onClickCancelBtn(View v);
    	/**
    	 * 点击选择按钮回调
    	 * @param v
    	 * @param checkAll 是否全选状态
    	 */
    	void onClickSelectBtn(View v,boolean checkAll);
    }
}