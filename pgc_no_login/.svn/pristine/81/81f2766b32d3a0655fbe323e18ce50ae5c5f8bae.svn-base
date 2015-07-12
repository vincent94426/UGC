package cn.dezhisoft.cloud.mi.newugc.ugv2.view.metro.view;

import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.ImageView;
import android.widget.TextView;
import cn.dezhisoft.cloud.mi.newugc.R;
import cn.dezhisoft.cloud.mi.newugc.ugv2.view.metro.model.AutoFillMetroItem;

/**
 * Metro风格界面上显示的Item
 * @author chenhp1
 */
public class MetroItemView extends AutoFillMetroItem{
	private Drawable backgroundDrawable;
	private int textColor;
	
	private int iconDrawable;
	private int iconPostion;
	
	private int textTitle;
	private int textAnimation;
	private String itemTag;
	
	private boolean itemAnimation;

	public MetroItemView(MetroItemLayout v, int rowspan, int colspan) {
		super(v, rowspan, colspan);
	}

	protected LayoutInflater inflater;
	
	public MetroItemView(LayoutInflater inflater, int rowspan, int colspan, Drawable backgroundDrawable, int iconDrawable, int textTitle, int textAnimation, OnTouchListener listener) {
		super(null, rowspan, colspan);
		
		this.inflater = inflater;
		this.backgroundDrawable = backgroundDrawable;
		this.iconDrawable = iconDrawable;
		this.textTitle = textTitle;
		this.textAnimation = textAnimation;
		
		initMetroItemView(listener);
	}
	

	void initMetroItemView(OnTouchListener listener){
		mMetroView = (MetroItemLayout) this.inflater.inflate(getLayoutId(),null);
//		mMetroView.setOnMetroItemTouchListener(listener);
		mMetroView.setOnTouchListener(listener);
		mMetroView.setId(iconDrawable);
		mMetroView.setTag(this);
		
		if(backgroundDrawable != null){
//			View contentView = (View)mMetroView.findViewById(R.id.lesync_metro_item_content);
//			contentView.setBackgroundResource(backgroundColor);
			mMetroView.setBackgroundDrawable(backgroundDrawable);
		}

		if(textColor != 0){
			TextView tv = (TextView)mMetroView.findViewById(R.id.lesync_metro_item_content_text);
			tv.setTextColor(textColor);
		}
		
		if(textTitle != 0){
			TextView tv = (TextView)mMetroView.findViewById(R.id.lesync_metro_item_content_text);
			tv.setText(textTitle);
		}else {
			TextView tv = (TextView)mMetroView.findViewById(R.id.lesync_metro_item_content_text);
			tv.setVisibility(View.GONE);
		}
		
		if(textAnimation != 0){
			TextView tv = (TextView)mMetroView.findViewById(R.id.lesync_metro_item_animation_text);
			tv.setText(textAnimation);
		}
		
		if(iconDrawable != 0){
			ImageView iv = (ImageView)mMetroView.findViewById(R.id.lesync_metro_item_content_icon);
			iv.setImageResource(iconDrawable);
		}
	}

	protected int getLayoutId() {
		return R.layout.lesync_metro_item_view;
	}
/*	
	public void setOnClickListener(OnClickListener listener){
		if(null != getMetroView())
			getMetroView().setOnClickListener(listener);
	}
	*/
	public Drawable getBackgroundDrawable() {
		return backgroundDrawable;
	}

	public void setBackgroundDrawable(Drawable backgroundDrawable) {
		this.backgroundDrawable = backgroundDrawable;
	}

	public int getTextColor() {
		return textColor;
	}

	public void setTextColor(int textColor) {
		this.textColor = textColor;
	}

	public int getIconDrawable() {
		return iconDrawable;
	}

	public void setIconDrawable(int iconDrawable) {
		this.iconDrawable = iconDrawable;
	}

	public int getTextTitle() {
		return textTitle;
	}

	public void setTextTitle(int textTitle) {
		this.textTitle = textTitle;
	}

	public int getTextAnimation() {
		return textAnimation;
	}

	public void setTextAnimation(int textAnimation) {
		this.textAnimation = textAnimation;
	}

	public int getIconPostion() {
		return iconPostion;
	}

	public void setIconPostion(int iconPostion) {
		this.iconPostion = iconPostion;
	}

	public String getItemTag() {
		return itemTag;
	}

	public void setItemTag(String itemTag) {
		this.itemTag = itemTag;
	}

	public boolean isItemAnimation() {
		return itemAnimation;
	}

	public void setItemAnimation(boolean itemAnimation) {
		this.itemAnimation = itemAnimation;
	}
	
	public View getMetroItemRootView(){
		return mMetroView;
	}

}
