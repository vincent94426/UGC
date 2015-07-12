package cn.dezhisoft.cloud.mi.newugc.ugv2.view.metro.view;

import java.util.HashMap;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;
import cn.dezhisoft.cloud.mi.newugc.R;
import cn.dezhisoft.cloud.mi.newugc.ugv2.view.metro.model.AutoFillMetroItem;
import cn.dezhisoft.cloud.mi.newugc.ugv2.view.metro.model.MetroItem;

public class AutoFillMetroView extends MetroView {

	private boolean isHorizontal = false;

	private int mRows = 0, mCols = 0;

	private HashMap<Integer, Boolean> mMetroPool = new HashMap<Integer, Boolean>();

	public static class MetroEvent {
		public int id;
		public String message;
		public int count;
		
		public MetroEvent(int id, String message, int count) {
			super();
			this.id = id;
			this.message = message;
			this.count = count;
		}
	}
	
	public AutoFillMetroView(Context context) {
		super(context);
		initViewGroup(context);
	}

	public AutoFillMetroView(Context context, AttributeSet attrs) {
		super(context, attrs);
		initViewGroup(context);
	}

	public AutoFillMetroView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		initViewGroup(context);
	}

	private void initViewGroup(Context context) {
		mRows = 0;
		mCols = 0;

		mMetroPool.clear();
	}
	
	/**
	 * 
	 * @param metroId
	 * @param event	if null, clear metro event
	 */
	public void onUpdateMetreItem(int metroId, MetroEvent event) {
		View view = findViewById(metroId);
		
		if(view == null) {
			return;
		}
		
		View animation = view.findViewById(R.id.lesync_metro_item_animation_layout);
		if(event == null) {
			animation.setVisibility(View.GONE);
			animation.setTag(null);
			return;
		}
		
		TextView text = (TextView) animation.findViewById(R.id.lesync_metro_item_animation_text);
		text.setText(event.message);
		
		Integer times = (Integer) animation.getTag();
		if(times != null && times > 0) {
			animation.setTag(--times);
			return;
		}
		
		TextView num = (TextView) animation.findViewById(R.id.lesync_metro_item_animation_text_num);
		if(num != null) {
			String tmp = Integer.toString(event.count);
			num.setText(tmp);
			if(tmp.length() > 3) {
				num.setTextSize(10);
			} else {
				num.setTextSize(11);
			}
			num.setVisibility(event.count > 0 ? View.VISIBLE : View.GONE);
		}
		
		if(View.VISIBLE == animation.getVisibility()) {
			animation.setVisibility(View.GONE);
			Animation loadAnimation = AnimationUtils.loadAnimation(getContext(), R.anim.slide_out_up);
			animation.startAnimation(loadAnimation);
			animation.setTag(Integer.valueOf(0));
		} else {
			animation.setVisibility(View.VISIBLE);
			Animation loadAnimation = AnimationUtils.loadAnimation(getContext(), R.anim.slide_in_up);
			animation.startAnimation(loadAnimation);
			animation.setTag(Integer.valueOf(1));
		}
	}
	
	private int[] getPosition(View v, int rowspan, int colspan) {

		if (v == null)
			throw new IllegalArgumentException("invalid item");

		int[] position = new int[2];
		if (isHorizontal) {
			// check rowspan
			if (rowspan > visibleRows)
				throw new IllegalArgumentException("invalid row span");

			getPositionByCol(v, rowspan, colspan, position);
		} else {
			// check colspan
			if (colspan > visibleCols)
				throw new IllegalArgumentException("invalid col span");

			getPositionByRow(v, rowspan, colspan, position);
		}

		return position;
	}

	private void updateItemPosition(MetroItem item) {
		int[] position = getPosition(item.getMetroView(), item.getRowSpan(),
				item.getColSpan());
		addMark(position[0], item.getRowSpan(), position[1], item.getColSpan());
		item.setPosition(position[0], position[1]);
	}

	private void getPositionByRow(View v, int rowspan, int colspan,
			int[] position) {

		boolean isAppended = false;

		// id = x + cols * y
		for (int y = 0; y < mRows; y++) {
			for (int x = 0; x < visibleCols; x++) {

				// check span
				if (x + colspan > visibleCols)
					break;

				boolean hasSpace = true;
				for (int row = 0; row < rowspan; row++) {
					for (int col = 0; col < colspan; col++) {

						int id = (x + col) + visibleCols * (y + row);

						if (mMetroPool.containsKey(id)) {
							hasSpace = false;
							break;
						}
					}

					if (!hasSpace)
						break;
				}

				if (hasSpace) {

					position[0] = y;
					position[1] = x;

					isAppended = true;
					break;
				}
			}

			if (isAppended) {
				break;
			}
		}

		if (!isAppended) {
			position[0] = mRows;
			position[1] = 0;
		}
	}

	private void getPositionByCol(View v, int rowspan, int colspan,
			int[] position) {

		boolean isAppended = false;

		// id = x * rows + y
		for (int x = 0; x < mCols; x++) {
			for (int y = 0; y < visibleRows; y++) {

				// check span
				if (y + rowspan > visibleRows)
					break;

				boolean hasSpace = true;
				for (int row = 0; row < rowspan; row++) {
					for (int col = 0; col < colspan; col++) {

						int id = (x + col) * visibleRows + (y + row);

						if (mMetroPool.containsKey(id)) {
							hasSpace = false;
							break;
						}
					}

					if (!hasSpace)
						break;
				}

				if (hasSpace) {

					position[0] = y;
					position[1] = x;

					isAppended = true;
					break;
				}
			}

			if (isAppended) {
				break;
			}
		}

		if (!isAppended) {
			position[0] = 0;
			position[1] = mCols;
		}
	}

	private void addMark(int row, int rowspan, int col, int colspan) {

		if (isHorizontal)
			mCols = Math.max(mCols, col + colspan);
		else
			mRows = Math.max(mRows, row + rowspan);

		// add flag
		for (int x = col; x < col + colspan; x++)
			for (int y = row; y < row + rowspan; y++) {

				int id = 0;

				if (isHorizontal)
					id = x * visibleRows + y;
				else
					id = x + visibleCols * y;

				mMetroPool.put(id, true);
			}
	}

	@Override
	public void setVisibleItems(int rowCount, int colCount) {
		super.setVisibleItems(rowCount, colCount);

		// update item position
		initViewGroup(getContext());
		for (MetroItem item : mMetroItems) {
			updateItemPosition(item);
		}

		// update count
		if (isHorizontal)
			mColsCount = mCols;
		else
			mRowsCount = mRows;

		// goto 0,0
		snapTo(0, 0);
	}

	@Override
	public void setOrientation(OrientationType orientation) {

		if (orientation != OrientationType.Horizontal&& orientation != OrientationType.Vertical)
			throw new IllegalArgumentException("invalid orientation type");

		super.setOrientation(orientation);

		if (orientation == OrientationType.Horizontal)
			isHorizontal = true;
		else if (orientation == OrientationType.Vertical)
			isHorizontal = false;
	}

	public void addAutoFillMetroItem(AutoFillMetroItem item) {
		int[] position = getPosition(item.getMetroView(), item.getRowSpan(),item.getColSpan());
		addMark(position[0], item.getRowSpan(), position[1], item.getColSpan());
		addMetroItem(new MetroItem(item.getMetroView(), position[0],
				item.getRowSpan(), position[1], item.getColSpan()));
		
	}
}
