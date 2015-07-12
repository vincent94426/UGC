package cn.dezhisoft.cloud.mi.newugc.ugv2.view.metro.anim;

import android.content.Context;
import android.view.View;
import android.view.ViewParent;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.view.animation.RotateAnimation;
import android.view.animation.TranslateAnimation;
import cn.dezhisoft.cloud.mi.newugc.R;

public class MetroAnimUtils {

	public static Animation getMetroItemRotateAnimation() {
		AnimationSet set = new AnimationSet(true);
		RotateAnimation rotate = new RotateAnimation(0, 360,Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,0.5f);
		rotate.setFillAfter(true);
		rotate.setDuration(750);
		AlphaAnimation alpha = new AlphaAnimation(1.0f, 0.0f);
		alpha.setFillAfter(true);
		alpha.setDuration(750);
		set.addAnimation(alpha);
		set.addAnimation(rotate);
		return set;
	}

	
	public static Animation getRotate3DAnimation(){
		return new MetroItemRotate3DAnimation();
	}
	
	public static Animation getMetroItemAllSyncAnimation(Context context){
		AnimationSet set = new AnimationSet(true);
		Animation scale = AnimationUtils.loadAnimation(context,R.anim.metro_allsync_scale);
		scale.setFillAfter(true);
		AlphaAnimation alpha = new AlphaAnimation(1.0f, 0.0f);
		alpha.setFillAfter(true);
		alpha.setDuration(750);
		set.addAnimation(alpha);
		set.addAnimation(scale);
		return set;
	}
	
	
	public static Animation getMetroItemAllSyncAnimation(Context context,View v){
		ViewParent vParent = v.getParent(); 
		 Animation a = new TranslateAnimation(0.0f,((View) vParent).getWidth() - v.getWidth()
				 - ((View) vParent).getPaddingLeft() - ((View) vParent).getPaddingRight(), 0.0f, 0.0f);
	     a.setDuration(1000);
	     a.setInterpolator(AnimationUtils.loadInterpolator(context,android.R.anim.bounce_interpolator));
	     return a;
	}
	
	public static Animation getMetroViewShakeAnimation(Context context,float fromX,float toX,float fromY,float toY){
//		ViewParent vParent = v.getParent(); 
		 Animation a = new TranslateAnimation(fromX,toX,fromY,toY);
	     a.setDuration(1000);
//	     a.setInterpolator(AnimationUtils.loadInterpolator(context,android.R.anim.bounce_interpolator));
	     a.setInterpolator(new MetroShakeInterpolator());
	     return a;
	}
	
}
