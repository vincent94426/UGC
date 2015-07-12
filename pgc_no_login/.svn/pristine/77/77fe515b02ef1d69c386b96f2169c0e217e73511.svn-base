package cn.dezhisoft.cloud.mi.newugc.ugc.preview;

import android.view.MotionEvent;
import android.view.View;

public final class SimpleImageZoomListener implements View.OnTouchListener {

	private ImageZoomState mState;
	private final float SENSIBILITY = 0.8f;
	private float sX;
	private float sY;

	/** 不变的起始点坐标，用于判断手指是否进行了移动，从而在UP事件中判断是否为点击事件 */
	private float sX01;
	private float sY01;

	private float sDistance;
	
	protected SimpleImageZoomListener(){
		
	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {

		int action = event.getAction();
		int pointNum = event.getPointerCount();// 获取触摸点数
		if (pointNum == 1) {// 单点触摸,用来实现图像的移动和相应点击事件
			float mX = event.getX();// 记录不断移动的触摸点x坐标
			float mY = event.getY();// 记录不断移动的触摸点y坐标
			switch (action) {
			case MotionEvent.ACTION_DOWN:
				// 记录起始点坐�?
				sX01 = mX;
				sY01 = mY;
				sX = mX;
				sY = mY;
				return false;// 必须return false 否则不响应点击事�?
			case MotionEvent.ACTION_MOVE:
				float dX = (mX - sX) / v.getWidth();
				float dY = (mY - sY) / v.getHeight();
				mState.setmPanX(mState.getmPanX() - dX * SENSIBILITY);
				mState.setmPanY(mState.getmPanY() - dY * SENSIBILITY);
				mState.notifyObservers();
				// 更新起始点坐�?
				sX = mX;
				sY = mY;
				break;
			case MotionEvent.ACTION_UP:
				if (event.getX() == sX01 && event.getY() == sY01) {
					return false;// return false 执行点击事件
				}
				break;
			}
		}

		if (pointNum == 2) {// 多点触摸，用来实现图像的缩放
			// 记录不断移动的一个触摸点坐标
			float mX0 = event.getX(event.getPointerId(0));
			float mY0 = event.getY(event.getPointerId(0));
			// 记录不断移动的令�?��触摸点坐�?
			float mX1 = event.getX(event.getPointerId(1));
			float mY1 = event.getY(event.getPointerId(1));
			float distance = this.getDistance(mX0, mY0, mX1, mY1);
			switch (action) {
			case MotionEvent.ACTION_POINTER_2_DOWN:
			case MotionEvent.ACTION_POINTER_1_DOWN:
				sDistance = distance;
				break;
			case MotionEvent.ACTION_POINTER_1_UP:
				// 注意：松�?���?��触摸点后的手指滑动就变成了以第二个触摸点为起始点的移动，�?��要以第二个触摸点坐标值为起始点坐标赋�?
				sX = mX1;
				sY = mY1;
				break;
			case MotionEvent.ACTION_POINTER_2_UP:
				// 注意：松�?��二个触摸点后的手指滑动就变成了以第二个触摸点为起始点的移动，�?��要以第一个触摸点坐标值为起始点坐标赋�?
				sX = mX0;
				sY = mY0;
				break;
			case MotionEvent.ACTION_MOVE:
				mState.setmZoom(mState.getmZoom() * distance / sDistance);
				mState.notifyObservers();
				sDistance = distance;
				break;

			}

		}
		return true;// 必须返回true，返回false的话对点击事件的逻辑处理有影响�?具体请查询Android事件拦截机制的相关资�?
	}

	private float getDistance(float mX0, float mY0, float mX1, float mY1) {
		double dX2 = Math.pow(mX0 - mX1, 2);// 两点横坐标差的平�?
		double dY2 = Math.pow(mY0 - mY1, 2);// 两点纵坐标差的平�?
		return (float) Math.pow(dX2 + dY2, 0.5);
	}

	public void setZoomState(ImageZoomState state) {
		mState = state;
	}
}
