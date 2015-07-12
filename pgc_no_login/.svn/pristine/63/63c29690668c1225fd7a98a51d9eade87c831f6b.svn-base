package cn.dezhisoft.cloud.mi.newugc.ugc.preview;

import java.util.Observable;
import java.util.Observer;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;

public final class BitmapPreview extends View implements Observer {

	private Paint mPaint = new Paint(Paint.FILTER_BITMAP_FLAG);
	private Rect mRectSrc = new Rect();
	private Rect mRectDst = new Rect();
	private float mAspectQuotient;
	private Bitmap mBitmap;
	private ImageZoomState mZoomState;
	
	public BitmapPreview(Context context) {
		this(context, null);
	}

	public BitmapPreview(Context context, AttributeSet attrs) {
		super(context, attrs);
		
		ImageZoomState zoomState = new ImageZoomState();// ͼƬ���ź��ƶ�״̬��
		SimpleImageZoomListener zoomListener = new SimpleImageZoomListener();// �����¼�������
		zoomListener.setZoomState(zoomState);
		
		setImageZoomState(zoomState);
		setOnTouchListener(zoomListener);
		
		setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
			}
		}) ;
	}

	@Override
	public void update(Observable observable, Object data) {
		this.invalidate();
	}

	@Override
	protected void onDraw(Canvas canvas) {

		if (mBitmap != null && mZoomState != null) {
			int viewWidth 		= this.getWidth();
			int viewHeight 		= this.getHeight();
			int bitmapWidth 	= mBitmap.getWidth();
			int bitmapHeight 	= mBitmap.getHeight();
			float panX 			= mZoomState.getmPanX();
			float panY 			= mZoomState.getmPanY();
			float zoomX = mZoomState.getZoomX(mAspectQuotient) * viewWidth / bitmapWidth;// 相当于viewHeight/bitmapHeight*mZoom
			float zoomY = mZoomState.getZoomY(mAspectQuotient) * viewHeight / bitmapHeight;// 相当于viewWidth/bitmapWidth*mZoom
			// Setup source and destination rectangles

			// 这里假定图片的高和宽都大于显示区域的高和宽，如果不是在下面做调整
			mRectSrc.left 	= (int) (panX * bitmapWidth - viewWidth / (zoomX * 2));
			mRectSrc.top 	= (int) (panY * bitmapHeight - viewHeight / (zoomY * 2));
			mRectSrc.right 	= (int) (mRectSrc.left + viewWidth / zoomX);
			mRectSrc.bottom = (int) (mRectSrc.top + viewHeight / zoomY);
			mRectDst.left 	= this.getLeft();
			mRectDst.top 	= this.getTop();
			mRectDst.right 	= this.getRight();
			mRectDst.bottom = this.getBottom();
			
			// 如果图片宽或高小于显示区域宽或高（组件大小）或�?由于移动或缩放引起的下面条件成立则调整矩形区域边�?

			if (mRectSrc.left < 0) {
				mRectDst.left += -mRectSrc.left * zoomX;
				mRectSrc.left = 0;
			}

			if (mRectSrc.right > bitmapWidth) {
				mRectDst.right -= (mRectSrc.right - bitmapWidth) * zoomX;
				mRectSrc.right = bitmapWidth;
			}

			if (mRectSrc.top < 0) {
				mRectDst.top += -mRectSrc.top * zoomY;
				mRectSrc.top = 0;
			}

			if (mRectSrc.bottom > bitmapHeight) {
				mRectDst.bottom -= (mRectSrc.bottom - bitmapHeight) * zoomY;
				mRectSrc.bottom = bitmapHeight;

			}
			// 把bitmap的一部分(就是src�?��括的部分)绘制到显示区中dst指定的矩形处.关键就是dst,它确定了bitmap要画的大小跟位置
			// 注：两个矩形中的坐标位置是相对于各自本身的�?不是相对于屏幕的�?
			canvas.drawBitmap(mBitmap, mRectSrc, mRectDst, mPaint);
		}

	}

	@Override
	protected void onLayout(boolean changed, int left, int top, int right,int bottom) {
		super.onLayout(changed, left, top, right, bottom);
		
		calculateAspectQuotient();
	}

	public void setImageZoomState(ImageZoomState zoomState) {

		if (mZoomState != null) {

			mZoomState.deleteObserver(this);

		}

		mZoomState = zoomState;

		mZoomState.addObserver(this);

		invalidate();

	}

	public void loadBitmap(Bitmap bitmap) {
		mBitmap = bitmap;
		this.calculateAspectQuotient();
		invalidate();
	}
	
	public void loadBitmap(int rid) {
		Bitmap bitmap = BitmapFactory.decodeResource(getResources(),rid);
		loadBitmap(bitmap);
	}
	
	public void loadBitmap(String path) {
		Bitmap bitmap = BitmapFactory.decodeFile(path);;
		loadBitmap(bitmap);
	}
	
	
	private void calculateAspectQuotient() {
		if (mBitmap != null) {
			mAspectQuotient = (float) (((float) mBitmap.getWidth() / mBitmap
					.getHeight()) / ((float) this.getWidth() / this.getHeight()));

		}
	}

}
