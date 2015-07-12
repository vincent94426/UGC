package cn.dezhisoft.cloud.mi.newugc.ugv2.view.metro.view;

import java.util.ArrayList;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.widget.Scroller;
import cn.dezhisoft.cloud.mi.newugc.R;
import cn.dezhisoft.cloud.mi.newugc.ugv2.view.metro.model.MetroItem;

public class MetroView extends ViewGroup {

	public static enum OrientationType {
		All, Vertical, Horizontal
	};

	private static final String TAG = "MetroView";

	private static final LayoutParams FILL_FILL = new LayoutParams(
			LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT);

	/** Default count of visible items */
	private static final int DEF_VISIBLE_ROWS = 5;
	private static final int DEF_VISIBLE_COLS = 2;

	private OrientationType mOrientation = OrientationType.Horizontal;

	// Count of visible items
	protected int visibleRows = DEF_VISIBLE_ROWS;
	protected int visibleCols = DEF_VISIBLE_COLS;

	private Scroller mScroller;
	private VelocityTracker mVelocityTracker;

	private int mCurRow = 0, mCurCol = 0;
	protected int mRowsCount = 0, mColsCount = 0;

	public static final int TOUCH_STATE_REST = 0;
	public static final int TOUCH_STATE_SCROLLING = 1;

	private static final int SNAP_VELOCITY = 600;
	private static final int MAX_SCROLLING_Y = 150;

	public static int mTouchState = TOUCH_STATE_REST;
	private int mTouchSlop;
	private float mLastMotionX, mLastMotionY;

	private MetroListener metroListener;

	protected ArrayList<MetroItem> mMetroItems = new ArrayList<MetroItem>();

	private int mRowHeight, mColWidth;
	private int mGap;
	
	private OnMoveListenner mOnMoveListenner;
	
	public void setOnMoveListenner(OnMoveListenner mOnMoveListenner) {
		this.mOnMoveListenner = mOnMoveListenner;
	}
	
	
	public static interface OnMoveListenner {
		void onLeft();
		void onRight();
	}
	
	public MetroView(Context context) {
		this(context, null);
	}

	public MetroView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public MetroView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs);
		initViewGroup(context);
	}

	private void initViewGroup(Context context) {
		mScroller = new Scroller(context);

		mTouchSlop = ViewConfiguration.get(context).getScaledTouchSlop();

		mGap = context.getResources().getDimensionPixelSize(R.dimen.metro_gap);
	}

	/**
	 * set row and column count for visible item
	 * 0 equals to not change current value
	 * others equal to 
	 * 
	 * @param rowCount
	 * @param colCount
	 */
	public void setVisibleItems(int rowCount, int colCount) {

		if (rowCount < 0 || colCount < 0)
			throw new IllegalArgumentException("visible count < 0");

		if (rowCount != 0)
			visibleRows = rowCount;

		if (colCount != 0)
			visibleCols = colCount;
	}

	public int getVisibleRows() {
		return visibleRows;
	}

	public int getVisibleCols() {
		return visibleCols;
	}

	public void addMetroItem(MetroItem item) {
		mMetroItems.add(item);
		addView(item.getMetroView(), FILL_FILL);

		adjustRowCol(item);
	}

	public boolean deleteMetroItem(MetroItem item) {

		boolean ret = false;

		if (mMetroItems.contains(item)) {
			mMetroItems.remove(item);
			removeView(item.getMetroView());
			ret = true;
		}

		mRowsCount = 0;
		mColsCount = 0;

		for (MetroItem mi : mMetroItems) {
			adjustRowCol(mi);
		}

		return ret;
	}

	private void adjustRowCol(MetroItem item) {
		// adjust rows count
		if (mRowsCount < item.getRow() + item.getRowSpan())
			mRowsCount = item.getRow() + item.getRowSpan();

		// adjust columns count
		if (mColsCount < item.getCol() + item.getColSpan())
			mColsCount = item.getCol() + item.getColSpan();
	}

	public void clearMetroItem() {
		mMetroItems.clear();
		removeAllViews();

		mRowsCount = 0;
		mColsCount = 0;
	}

	public void setOrientation(OrientationType orientation) {
		mOrientation = orientation;
	}

	public OrientationType getOrientation() {
		return mOrientation;
	}

	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {

		final int itemCount = mMetroItems.size();

		if (itemCount != getChildCount())
			throw new IllegalArgumentException("contain unrecorded child");

		for (int i = 0; i < itemCount; i++) {
			final MetroItem item = mMetroItems.get(i);
			final View childView = item.getMetroView();

			if (childView.getVisibility() != View.GONE) {
				final int childLeft = (mColWidth + mGap) * item.getCol();
				final int childTop = (mRowHeight + mGap) * item.getRow();
				final int childWidth = (mColWidth + mGap) * item.getColSpan() - mGap;
				final int childHeight = (mRowHeight + mGap) * item.getRowSpan() - mGap;

				childView.layout(childLeft, childTop, childLeft + childWidth,
						childTop + childHeight);
			}
		}
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);

		final int width = MeasureSpec.getSize(widthMeasureSpec);
		final int height = MeasureSpec.getSize(heightMeasureSpec);

		mRowHeight = (height - (visibleRows - 1) * mGap) / visibleRows;//adjust height of MetroView
		mColWidth = (width * 5/5 - (visibleCols - 1) * mGap) / visibleCols;//adjust width of MetroView 5/5

		// The children are given the same width and height as the scrollLayout
		final int itemCount = mMetroItems.size();

		for (int i = 0; i < itemCount; i++) {

			final MetroItem item = mMetroItems.get(i);
			final View childView = item.getMetroView();

			final int childWidth = MeasureSpec.makeMeasureSpec((mColWidth + mGap) * item.getColSpan() - mGap, MeasureSpec.EXACTLY);
			final int childHeight = MeasureSpec.makeMeasureSpec((mRowHeight + mGap) * item.getRowSpan() - mGap, MeasureSpec.EXACTLY);

			childView.measure(childWidth, childHeight);
		}

		scrollTo((mColWidth + mGap) * mCurCol, (mRowHeight + mGap) * mCurRow);
	}

	/**
	 * According to the position of current layout scroll to the destination
	 * page.
	 */
	public void snapToDestination() {
		final int destRow = (getScrollY() + (mRowHeight + mGap) / 2) / (mRowHeight + mGap);
		final int destCol = (getScrollX() + (mColWidth + mGap) / 2) / (mColWidth + mGap);
		snapTo(destRow, destCol);
	}

	public void snapTo(int whichRow, int whichCol) {

		if (whichRow < 0)
			whichRow = 0;

		if (whichCol < 0)
			whichCol = 0;

		Log.d(TAG, String.format("snap to row:%d, col:%d", whichRow, whichCol));

		boolean needRedraw = false;

		if (mOrientation == OrientationType.Horizontal) {
			whichRow = 0;
			if (whichCol + visibleCols > mColsCount)
				whichCol = Math.max(mColsCount - visibleCols, 0);
		} else if (mOrientation == OrientationType.Vertical) {
			whichCol = 0;
			if (whichRow + visibleRows > mRowsCount)
				whichRow = Math.max(mRowsCount - visibleRows, 0);
		} else if (mOrientation == OrientationType.All) {
			if (whichRow + visibleRows > mRowsCount)
				whichRow = Math.max(mRowsCount - visibleRows, 0);
			if (whichCol + visibleCols > mColsCount)
				whichCol = Math.max(mColsCount - visibleCols, 0);
		}

		int deltaX = whichCol * (mColWidth + mGap);
		int deltaY = whichRow * (mRowHeight + mGap);

		// get the valid layout page
		if (getScrollX() != deltaX) {
			deltaX = deltaX - getScrollX();
			needRedraw = true;
		}

		if (getScrollY() != deltaY) {
			deltaY = deltaY - getScrollY();
			needRedraw = true;
		}
		
		if (needRedraw) {
			mCurRow = whichRow;
			mCurCol = whichCol;
			mScroller.startScroll(getScrollX(), getScrollY(), deltaX, deltaY,Math.max(Math.abs(deltaX), Math.abs(deltaY)) * 2);
			invalidate(); // Redraw the layout
		}
	}

	public int getCurRow() {
		return mCurRow;
	}
	
	public int getCurCol() {
		return mCurCol;
	}

	@Override
	public void computeScroll() {
		if (mScroller.computeScrollOffset()) {
			scrollTo(mScroller.getCurrX(), mScroller.getCurrY());
			postInvalidate();
		}
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {

		if (mVelocityTracker == null) {
			mVelocityTracker = VelocityTracker.obtain();
		}
		mVelocityTracker.addMovement(event);

		final int action = event.getAction();
		float x = event.getX();
		float y = event.getY();

		if (mOrientation == OrientationType.Horizontal)
			y = 0;
		else if (mOrientation == OrientationType.Vertical)
			x = 0;

		switch (action) {
		case MotionEvent.ACTION_DOWN:
			if (!mScroller.isFinished()) {
				mScroller.abortAnimation();
			}
			mLastMotionX = x;
			mLastMotionY = y;
			break;
		case MotionEvent.ACTION_MOVE:
			clearFocus();
			int deltaX = (int) (mLastMotionX - x);
			int deltaY = (int) (mLastMotionY - y);

			mLastMotionX = x;
			mLastMotionY = y;

			//scrollBy(deltaX, deltaY);
			if (Math.abs(getScrollY()) <= MAX_SCROLLING_Y) {
				//scrollBy(0, deltaY);//MetroView scroll
			}
			break;
		case MotionEvent.ACTION_UP:
			final VelocityTracker velocityTracker = mVelocityTracker;
			velocityTracker.computeCurrentVelocity(1000);

			int velocityX = (int) velocityTracker.getXVelocity();
			int velocityY = (int) velocityTracker.getYVelocity();
			
			int row = mCurRow;
			int col = mCurCol;

			if (velocityX > SNAP_VELOCITY && mCurCol >= 0) {
				// Fling enough to move left
				if(null != mOnMoveListenner){
					mOnMoveListenner.onLeft();
				}
				//col--;
			} else if (velocityX < -(SNAP_VELOCITY/2) && mCurCol < mColsCount - 1) {
				// Fling enough to move right
				//not up or down
				if(!((velocityY > SNAP_VELOCITY && mCurRow > 0) || (velocityY < -SNAP_VELOCITY && mCurRow < mRowsCount - 1))) {
					if(null != mOnMoveListenner){
						mOnMoveListenner.onRight();
					}
				}
				
				//col++;
			}

			if (velocityY > SNAP_VELOCITY && mCurRow > 0) {
				// Fling enough to move up
				//row--;
			} else if (velocityY < -SNAP_VELOCITY && mCurRow < mRowsCount - 1) {
				// Fling enough to move down
				//row++;
			}

			if (row == mCurRow && col == mCurCol){
//				this.startAnimation(MetroAnimUtils.getMetroViewShakeAnimation(getContext(),0.0f,0.0f,getScrollY(),0.0f));
				snapToDestination();
			}
			else {
				snapTo(row, col);
				if (metroListener != null)
					metroListener.scrollto(row, col);
			}

			if (mVelocityTracker != null) {
				mVelocityTracker.recycle();
				mVelocityTracker = null;
			}
			mTouchState = TOUCH_STATE_REST;
			break;
		case MotionEvent.ACTION_CANCEL:
			mTouchState = TOUCH_STATE_REST;
			break;
		}
		return true;
	}
	

	@Override
	public boolean onInterceptTouchEvent(MotionEvent ev) {
		final int action = ev.getAction();
		if ((action == MotionEvent.ACTION_MOVE) && (mTouchState != TOUCH_STATE_REST)) {
			return true;
		}

		final float x = ev.getX();
		final float y = ev.getY();

		switch (action) {
		case MotionEvent.ACTION_MOVE:
			final int xDiff = (int) Math.abs(mLastMotionX - x);
			final int yDiff = (int) Math.abs(mLastMotionY - y);
			if (yDiff > mTouchSlop || xDiff > mTouchSlop) {
				mTouchState = TOUCH_STATE_SCROLLING;
			}
			break;
		case MotionEvent.ACTION_DOWN:
			mLastMotionX = x;
			mLastMotionY = y;
			mTouchState = mScroller.isFinished() ? TOUCH_STATE_REST : TOUCH_STATE_SCROLLING;
			break;

		case MotionEvent.ACTION_CANCEL:
		case MotionEvent.ACTION_UP:
			mTouchState = TOUCH_STATE_REST;
			break;
		}
		return mTouchState != TOUCH_STATE_REST;
	}
	
	@Override
	public boolean dispatchTouchEvent(MotionEvent ev) {
		return super.dispatchTouchEvent(ev);
	}
	
	public void setMereoListener(MetroListener metroListener) {
		this.metroListener = metroListener;
	}

	public interface MetroListener {

		public void scrollto(int row, int col);
	}
	
}
