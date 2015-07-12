package cn.dezhisoft.cloud.mi.newugc.ioffer.upload.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.FontMetrics;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import cn.dezhisoft.cloud.mi.newugc.R;

import cn.dezhisoft.cloud.mi.newugc.ioffer.upload.ui.UGCVideoClipActivity;

/**
 * 
 * @author Rosson Chen
 *
 */
public final class VideoRangeSelectBarView extends View {

	private final int FLAG_THUMB 	= 1 ;
	private final int FLAG_START 	= 2 ;
	private final int FLAG_END 		= 3 ;
	
	private final int FLG_H_PADDING		= 20 ;
	private final int FLG_V_PADDING		= 8 ;
	private static boolean 		debug	= !true ;

	int bgWidth,bgHeight, bgLeft, bgTop, proLeft, proTop, startX, endX;
	float startLeft, startTop, endLeft, endTop;

	final Paint paint 		= new Paint();
	final Rect rootRect 	= new Rect();// 显示矩形区域
	final Rect clipRect 	= new Rect();// 裁剪区域
	final static int mColor	= Color.parseColor("#442e6c") ;

	Bitmap bgIcon, progressIcon, startIcon, endIcon;

	ProgressListener progressListener;
	NeedleListener needleListener;

	float start, end;
	String currentTime = "00:00:00";
	
	int duration ;
	boolean initTimeLine ;
	int[] timeLine ;
	int minTimeX ;
	int maxTimeX ;
	int currentPos ;
	int textSize = 12;

	public VideoRangeSelectBarView(Context context) {
		this(context,null);
	}

	public VideoRangeSelectBarView(Context context, AttributeSet attrs) {
		super(context, attrs);
		paint.setAntiAlias(true);
	}

	@Override
	protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
		super.onLayout(changed, left, top, right, bottom);
		
		rootRect.set(left, top, right, bottom);
		
		startX 	= 0;
		end		= 1.0f ;
		initTimeLine 	= false ;
	}

	@Override
	protected void onDraw(Canvas canvas) {
		
		final Bitmap bIcon 		= bgIcon ;
		final Bitmap sIcon 		= startIcon ;
		final Bitmap eIcon 		= endIcon ;
		final Bitmap pIcon 		= progressIcon ;
		final Paint  p			= paint ;
		
		int color = p.getColor();
		
		// 背景
		if (bIcon != null)
			canvas.drawBitmap(bIcon, bgLeft - 2, bgTop, p);

		// 选中矩形区域
		if (startLeft < endLeft) {
			p.setColor(mColor);
			clipRect.set((int)startLeft, bgTop + 3, (int)endLeft, (bgTop + bIcon.getHeight()) - 3);
			canvas.drawRect(clipRect, p);
		}

		// 进度条
		if (pIcon != null) {
			int x = getCurrentPosition() - (pIcon.getWidth() >> 1);
			// 时间文本
			p.setColor(0xFFFFFFFF);
			p.setTextSize(textSize);
			canvas.drawText(currentTime, x, getTextHeight(textSize), p);
			// icon
			canvas.drawBitmap(pIcon, x, proTop, p);
		}
		
		// 开始icon
		if (sIcon != null) {
			canvas.drawBitmap(sIcon, startLeft - (sIcon.getWidth() >> 1), startTop + 1, p);
		}
		
		// 结束icon
		if (eIcon != null) {
			canvas.drawBitmap(eIcon, endLeft - (eIcon.getWidth() >> 1), endTop + 1, p);
		}
		
		// for debug time line
		if(debug && timeLine != null){
			int n	= timeLine.length ;
			p.setColor(Color.RED);
			for(int i = 0; i < n ; i++){
				canvas.drawLine(timeLine[i], bgTop, timeLine[i], bgTop + bIcon.getHeight(), p);
			}
		}
		
		p.setColor(color);
	}

	public synchronized void updatePlayPosition(int progress) {
		
		currentPos	= progress / 1000 ;
		
		invalidate();
	}

	public synchronized void setStartNeedle(int startTime) {
		
		startLeft = findPositionByTime(startTime) ;
		
		// 判断播放游标
		int currentX = getCurrentPosition();
		if (currentX < startLeft) {
			updateThumbBar(false,FLAG_THUMB, startLeft, 0);
		} else if (currentX > endLeft) {
			updateThumbBar(false,FLAG_THUMB, endLeft, 0);
		}
		
		invalidate();
	}

	public synchronized void setEndNeedle(int endTime) {
		
		endLeft = findPositionByTime(endTime) ;
		
		// 判断播放游标
		int currentX = getCurrentPosition();
		if (currentX < startLeft) {
			updateThumbBar(false,FLAG_THUMB, startLeft, 0);
		} else if (currentX > endLeft) {
			updateThumbBar(false,FLAG_THUMB, endLeft, 0);
		}
		
		invalidate();
	}
	
	public int getEndTime(){
		return findTargetTime(findTargetIndex(endLeft)) ;
	}
	
	public int getStartTime(){
		return findTargetTime(findTargetIndex(startLeft)) ;
	}

	public void setDuration(int duration){
		
		if(duration <= 0) return ;
		
		this.duration	= duration ;
		
		int length 		= duration / 1000 ;
		
		loadUIResource(length);
		
		invalidate() ;
	}
	
	private void loadUIResource(int second){
		
		int cw = getResources().getDimensionPixelSize(R.dimen.ugc_video_clip_drag_width) ;
		int ch = getResources().getDimensionPixelSize(R.dimen.ugc_video_clip_drag_height) ;
		bgHeight = getResources().getDimensionPixelSize(R.dimen.ugc_video_clip_rect_height) ;
		textSize = getResources().getDimensionPixelSize(R.dimen.ugc_video_clip_text_size) ;
		
		int viewWidth 	= rootRect.width() ;
		// 背景2边的宽度
		final int padding	= (cw >> 1) + 2 ;
		
		timeLine		= new int[second + 1];
		int tw 			= viewWidth - (padding * 2) ;
		int lw			= tw / second ;
		int offset		= tw % second ;
		
		bgLeft			= padding + (offset >> 1);
		bgWidth			= second * lw ;
		currentPos		= 0 ;// 进度标示
		
		// 计算时间线
		for(int i = 0, size = timeLine.length; i < size ; i++){
			timeLine[i] = bgLeft + (i * lw) ;
		}
		
		minTimeX	= timeLine[0] ;
		maxTimeX	= timeLine[timeLine.length - 1] ;
		
		BitmapFactory.Options opts = new BitmapFactory.Options();
		opts.inDensity 			= getResources().getDisplayMetrics().densityDpi;
		opts.inTargetDensity 	= getResources().getDisplayMetrics().densityDpi;
		Bitmap src				= null ;
		
		// 背景
		if(bgIcon == null){
			Bitmap gb 	= BitmapFactory.decodeResource(getResources(), R.drawable.ugc_ic_ioffer_video_range_select_bar_bg);
			bgIcon 		= Bitmap.createScaledBitmap(gb, bgWidth + 4, bgHeight, true);
			bgTop 		= (rootRect.height() - bgIcon.getHeight()) >> 1;
			proTop 		= 35;
			gb.recycle();
		}
		
		// 开始图标
		if (startIcon == null) {
			src 		= BitmapFactory.decodeResource(getResources(), R.drawable.ugc_ic_ioffer_video_range_select_needle, opts);
			startIcon 	= Bitmap.createScaledBitmap(src, cw, ch, true);
			startLeft 	= timeLine[0];
			startTop 	= rootRect.height() - startIcon.getHeight();
			src.recycle() ;
		}
		
		// 结束图标
		if (endIcon == null) {
			src 		= BitmapFactory.decodeResource(getResources(), R.drawable.ugc_ic_ioffer_video_range_select_needle, opts);
			endIcon 	= Bitmap.createScaledBitmap(src, cw, ch, true);
			endLeft 	= timeLine[timeLine.length - 1];
			endTop 		= rootRect.height() - endIcon.getHeight();
			src.recycle() ;
		}
		
		// 进度图标
		if (progressIcon == null) {
			progressIcon = BitmapFactory.decodeResource(getResources(), R.drawable.ugc_ic_ioffer_video_range_select_thumb, opts);
		}
		
	}

	private final int getCurrentPosition() {
		return timeLine != null && currentPos < timeLine.length ? timeLine[currentPos] : 0;
	}

	public static final int getTextHeight(int size) {
		Paint p = new Paint();
		p.setTextSize(size);
		FontMetrics fm = p.getFontMetrics();
		return (int) Math.ceil(fm.descent - fm.ascent);
	}
	
	public static final int getTextWidth(String str, int size) {
		Paint p = new Paint();
		p.setTextSize(size);
		return (int) p.measureText(str);
	}

	public void setProgressListener(ProgressListener l) {
		progressListener = l;
	}

	public void setNeedleListener(NeedleListener n) {
		needleListener = n;
	}

	public void setCurrentTime(String time) {
		currentTime = time;
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		float touchX 	= event.getX();
		float touchY 	= event.getY();
		int flag 		= 0 ;
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			// check
			flag 	= checkFlag(touchX, touchY) ;
			// update icon
			updateThumbBar(true,flag,touchX, touchY);
			break;
		case MotionEvent.ACTION_MOVE:
			// check
			flag 	= checkFlag(touchX, touchY) ;
			// update icon
			updateThumbBar(true,flag,touchX, touchY);
			//
			needleMove(flag,touchX, touchY);
			break;
		case MotionEvent.ACTION_UP:
			// 修正位置
			startLeft	= (float)fixedTargetPosition(startLeft);
			endLeft		= (float)fixedTargetPosition(endLeft);
			
			if (needleListener != null) {
				int si = findTargetIndex(startLeft) ;
				int ei = findTargetIndex(endLeft) ;
				needleListener.rangeUpdate(findTargetTime(si), findTargetTime(ei));
			}
			
			invalidate() ;
			break;
		case MotionEvent.ACTION_CANCEL:
			break;
		}
		return true;
	}
	
	/** 根据x值来查找最近的时间索引 */
	private int findTargetIndex(float targetX){
		int fixed = Integer.MAX_VALUE ;
		int index = 0 ;
		int temp  = 0 ;
		for(int i = 0 , size = timeLine.length; i < size ; i++){
			temp = (int)Math.abs(timeLine[i] - targetX) ;
			if(temp < fixed){
				fixed	= temp ;
				index	= i ;
			}
		}
		return index ;
	}
	
	/** 根据目标x值来查找最近的时间戳的X值*/
	private int fixedTargetPosition(float targetX){
		return timeLine[findTargetIndex(targetX)];
	}
	
	/** 根据时间戳索引来查找对应的时间 */
	private int findTargetTime(int index){
		return index >= 0 && index < timeLine.length ? index * 1000 : 0 ;
	}
	
	/** 根据时间戳来查找位置*/
	private float findPositionByTime(int time){
		int index = time / 1000 ;
		return index >= 0 && index < timeLine.length ? timeLine[index] : timeLine[0];
	}

	/** check move flag : start|thumb|end*/
	private int checkFlag(float touchX, float touchY){
		
		if (touchX >= bgLeft && touchX <= (bgLeft + bgIcon.getWidth()) && touchY >= bgTop && touchY <= (bgTop + bgHeight))
			return FLAG_THUMB ;
		else if (touchX >= (startLeft - FLG_H_PADDING) && touchX <= (startLeft + startIcon.getWidth() + FLG_H_PADDING * 2) 
				&& touchY >= (startTop -FLG_V_PADDING)  && touchY <= (startTop + startIcon.getHeight()) + FLG_V_PADDING * 2)
			return FLAG_START ;
		else if (touchX >= (endLeft - FLG_H_PADDING) && touchX <= (endLeft + endIcon.getWidth() + FLG_H_PADDING * 2) 
				&& touchY >= (endTop - FLG_V_PADDING) && touchY <= (endTop + endIcon.getHeight()) + FLG_V_PADDING * 2)
			return FLAG_END ;
		
		return 0 ;
	}

	public void updateThumbBar(boolean touch,int flag ,float touchX, float touchY) {
		
		if(touch && (touchX < startLeft || touchX > endLeft)) return ;
		
		if(flag == FLAG_THUMB) {
			
			currentPos 	= findTargetIndex(touchX);
			int time	= findTargetTime(currentPos) ;
			
			if(progressListener != null)
				progressListener.progressUpdate(time);
			
			// 更新时间
			currentTime = UGCVideoClipActivity.toTime(time);
		}
	}

	public void needleMove(int flag ,float touchX, float touchY) {
		
		int target = fixedTargetPosition(touchX) ;
		
		// 开始位置
		if (flag == FLAG_START) {
			if (target < endLeft) {
				int x = (int) touchX - (startIcon.getWidth() >> 1);
				startLeft = x < minTimeX ? minTimeX : x;
			}
		} 
		// 结束位置
		else if (flag == FLAG_END) {
			if (target > (startLeft + startIcon.getWidth())) {
				int x = (int) touchX - (endIcon.getWidth() >> 1);
				endLeft = x > maxTimeX ? maxTimeX : x;
			}
		}
		
		if (needleListener != null) {
			int si = findTargetIndex(startLeft) ;
			int ei = findTargetIndex(endLeft) ;
			needleListener.rangeUpdate(findTargetTime(si), findTargetTime(ei));
		}
		
		// 判断播放游标
		int currentX = getCurrentPosition() ;
		if(currentX < startLeft){
			updateThumbBar(false,FLAG_THUMB,startLeft,touchY);
		} else if(currentX > endLeft){
			updateThumbBar(false,FLAG_THUMB,endLeft,touchY);
		}
		// 更新
		invalidate();
	}

	public static interface ProgressListener {
		public void progressUpdate(int pos);
	}

	public static interface NeedleListener {
		public void rangeUpdate(int start, int end);
	}

	public void resetView() {
		startLeft 	= 1;
		endLeft 	= rootRect.width() - endIcon.getWidth() - 1;
		invalidate();
	}
}
