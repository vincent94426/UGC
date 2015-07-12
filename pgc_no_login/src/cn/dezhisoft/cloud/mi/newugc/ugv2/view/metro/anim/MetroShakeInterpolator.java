package cn.dezhisoft.cloud.mi.newugc.ugv2.view.metro.anim;

import android.content.Context;
import android.util.AttributeSet;
import android.view.animation.Interpolator;

public class MetroShakeInterpolator implements Interpolator{

	 public MetroShakeInterpolator() {
	    }

	    public MetroShakeInterpolator(Context context, AttributeSet attrs) {
	    }

	    private static float bounce(float t) {
	        return t * t * 4.0f;
	    }

	    public float getInterpolation(float t) {
	        // _b(t) = t * t * 8
	        // bs(t) = _b(t) for t < 0.3535
	        // bs(t) = _b(t - 0.54719) + 0.7 for t < 0.7408
	        // bs(t) = _b(t - 0.8526) + 0.9 for t < 0.9644
	        // bs(t) = _b(t - 1.0435) + 0.95 for t <= 1.0
	        // b(t) = bs(t * 1.1226)
	        t *= 1.0f;
	        if (t < 0.4545f) return bounce(t);
	        else if (t < 0.8408f) return bounce(t - 0.64719f) + 0.7f;
	        else if (t < 1.0644f) return bounce(t - 0.9526f) + 0.9f;
	        else return bounce(t - 1.1435f) + 1.05f;
	    }
}
