package cn.dezhisoft.cloud.mi.newugc.ioffer.ui.view;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.Gallery;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import cn.dezhisoft.cloud.mi.newugc.R;

import cn.dezhisoft.cloud.mi.newugc.ugc.model.Advert;

/**
 * The Class PromotionGallery. It contains 2 parts, one is the gallery to show
 * images, the other is 2 indicators on both side which indicates if there are
 * more promotions outside.
 * 
 * @author Rosson Chen
 * 
 */
public final class AdvertGallery extends FrameLayout {

	/** The gallery spacing. */
	protected static final int GALLERY_SPACING = 2;

	/**
	 * Used to calculate the width of gallery image, it is the width of gallery
	 * multiply this ratio.
	 */
	private static final double GALLERY_IMAGE_HORIZONTAL_RATIO = 1.0;

	/**
	 * Used to calculate the height of gallery image, it is the height of
	 * gallery multiply this ratio.
	 */
	private static final double GALLERY_IMAGE_VERTICAL_RATIO = 1.0;

	/** The promotions. */
	private ArrayList<Advert> advertsList;

	/** The gallery to show images. */
	private PromotionImages mAdvertsAdapter;

	/** context */
	private Context mContext;

	/** bottom layout */
	protected LinearLayout mBottomLayout;

	/** default image */
	private Drawable mDefault;

	/** focus image */
	private Drawable mFoucs;

	/** update hint image */
	private boolean updateIcon = false;

	/** promotion count */
	private int mPcount;

	/** image array */
	private ImageView[] icons;
	
	/** tips*/
	private TextView advertTips;
	
	/** advert on click*/
	private IAdvertOnClick onClick ;
	
	/** cache manager*/
	//private CacheManager cacheManager = UGCBaseServiceImpl.getServiceInstance().getCacheManager() ;

	/** */
	Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			switch(msg.what){
			//case UGCBaseMessageDefine.MSG_DOWNLOAD_ADVERT_SUCCESS :
			//	mAdvertsAdapter.refreshPromotion();
			//	break ;
			}
		}

	};

	/**
	 * 
	 * @param context
	 */
	public AdvertGallery(Context context) {
		this(context, null);
	}

	/**
	 * 
	 * @param context
	 * @param attrs
	 */
	public AdvertGallery(Context context, AttributeSet attrs) {
		super(context, attrs);
		mContext = context;
		mAdvertsAdapter = new PromotionImages(context);
		LayoutParams layoutParams = new LayoutParams(android.view.ViewGroup.LayoutParams.FILL_PARENT,android.view.ViewGroup.LayoutParams.FILL_PARENT);
		layoutParams.bottomMargin = 20;
		this.addView(mAdvertsAdapter, layoutParams);
		LayoutInflater inflater = ((Activity) context).getLayoutInflater();
		View indicator = inflater.inflate(R.layout.sobey_promotion_indicator,null);
		this.addView(indicator);
		//
		mBottomLayout = (LinearLayout) indicator.findViewById(R.id.sobey_promotion_index_layout);
		advertTips	  = (TextView) indicator.findViewById(R.id.sobey_promotion_tip);
	}

	/**
	 * Sets the promotions.
	 * 
	 * @param promotions
	 */
	public final void loadAdvertList(ArrayList<Advert> adverts) {
		advertsList = adverts;
		((BaseAdapter) mAdvertsAdapter.getAdapter()).notifyDataSetChanged();
		// load bottom hint icon
		loadBottomIcon(getResources().getDrawable(R.drawable.sobey_promotion_select_default),
				getResources().getDrawable(R.drawable.sobey_promotion_select_focus));
	}
	
	/**
	 * 
	 * @param click
	 */
	public final void setAdverOnClick(IAdvertOnClick click){
		onClick = click ;
	}

	/**
	 * get promotion size
	 * 
	 * @return
	 */
	private final int getPromotionsCount() {
		return mAdvertsAdapter != null ? mAdvertsAdapter.getAdapter().getCount() : 0;
	}

	/**
	 * load promotion hint
	 * 
	 * @param def
	 * @param focus
	 */
	public final void loadBottomIcon(Drawable def, Drawable focus) {
		if (advertsList != null && advertsList.size() > 1) {
			mBottomLayout.setVisibility(View.VISIBLE);
		} else {
			mBottomLayout.setVisibility(View.INVISIBLE);
		}

		if (def != null && focus != null) {
			mDefault = def;
			mFoucs = focus;
			updateIcon = true;
			initBottomIcons();
		}
	}

	/**
	 * init bottom promotion hint
	 */
	private final void initBottomIcons() {
		mPcount = getPromotionsCount();
		icons = new ImageView[mPcount];
		mBottomLayout.removeAllViews();
		mBottomLayout.setWeightSum(mPcount);
		//
		for (int i = 0; i < mPcount; i++) {
			icons[i] = new ImageView(mContext);
			icons[i].setBackgroundDrawable(mDefault);
			mBottomLayout.addView(icons[i]);
		}
	}

	/**
	 * 
	 * @param selected
	 */
	private final void promotionSwich(int selected) {
		// set tip text
		advertTips.setText(advertsList != null ? advertsList.get(selected).getTips() : "");
		// icon
		if (updateIcon) {
			if (icons == null) {
				initBottomIcons();
			}
			for (int i = 0; i < mPcount; i++) {
				if (selected == i) {
					icons[i].setBackgroundDrawable(mFoucs);
				} else {
					icons[i].setBackgroundDrawable(mDefault);
				}
			}
		}
	}

	/**
	 * Update indicator's state (visible or invisible).
	 * 
	 * @param selectedItemPosition
	 *            the selected item position
	 */
	private void updateIndicator(int selectedItemPosition) {
		// set switch
		promotionSwich(selectedItemPosition);
	}

	/**
	 * The Class PromotionImages which is a gallery to show the image of
	 * promotion.
	 */
	class PromotionImages extends Gallery {

		/** */
		PromotionImagesAdapter promotionAdapter;

		/**
		 * Instantiates a new PromotionImages gallery.
		 * 
		 * @param context
		 *            the context
		 */
		public PromotionImages(Context context) {
			super(context);
			this.setSpacing(GALLERY_SPACING);
			promotionAdapter = new PromotionImagesAdapter(context);
			this.setAdapter(promotionAdapter);
			// set on item click
			this.setOnItemClickListener(new OnItemClickListener() {
				@Override
				public void onItemClick(AdapterView<?> parent, View view,
						int position, long id) {
					if(onClick != null)
						onClick.onClickAdvert(advertsList.get(position));
				}
			});
			// set select listener
			this.setOnItemSelectedListener(new OnItemSelectedListener() {
				@Override
				public void onItemSelected(AdapterView<?> parent, View view,
						int position, long id) {
					updateIndicator(position);
				}

				@Override
				public void onNothingSelected(AdapterView<?> parent) {
					
				}
			});
		}

		public void refreshPromotion() {
			promotionAdapter.notifyDataSetChanged();
		}

		/**
		 * The Class PromotionImagesAdapter.
		 */
		private final class PromotionImagesAdapter extends BaseAdapter {

			/** The context. */
			protected Context mContext;

			/**
			 * Instantiates a new gallery adapter.
			 * 
			 * @param context
			 *            the context
			 */
			public PromotionImagesAdapter(Context context) {
				mContext = context ;
			}

			/**
			 * @see android.widget.Adapter#getCount()
			 */
			@Override
			public int getCount() {
				return advertsList == null ? 0 : advertsList.size();
			}

			/**
			 * @see android.widget.Adapter#getItem(int)
			 */
			@Override
			public Object getItem(int position) {
				return position;
			}

			/**
			 * @see android.widget.Adapter#getItemId(int)
			 */
			@Override
			public long getItemId(int position) {
				return position;
			}

			/**
			 * get promotion view
			 */
			@Override
			public View getView(final int position, final View convertView,ViewGroup parent) {
				
				final Advert promotion = advertsList.get(position);
				ImageView promotionImage = (ImageView) convertView;
				if (promotionImage == null) {
					promotionImage = new ImageView(mContext);
				}
				//
				int width = (int) (AdvertGallery.this.getWidth() * GALLERY_IMAGE_HORIZONTAL_RATIO);
				int height = (int) (AdvertGallery.this.getHeight() * GALLERY_IMAGE_VERTICAL_RATIO);
				promotionImage.setLayoutParams(new Gallery.LayoutParams(width,height));
				promotionImage.setScaleType(ImageView.ScaleType.CENTER);
				
				Bitmap bitmap = null;//cacheManager.getBitmap(promotion);
				if (bitmap != null) {
					promotionImage.setScaleType(ImageView.ScaleType.FIT_XY);
					promotionImage.setImageBitmap(bitmap);
				} else {
					/*promotionImage.setImageResource(R.anim);
					promotionImage.setFocusableInTouchMode(false);
					promotionImage.setFocusable(false);
					promotion.setDownloading(false);
					// set load animation
					final ImageView image = promotionImage;
					handler.postDelayed(new Runnable() {
						@Override
						public void run() {
							((AnimationDrawable) image.getDrawable()).run();
						}
					}, 200);*/
					// need down bitmap
					//if(promotion.getUrl() != null)
					//	UGCBaseServiceImpl.getServiceInstance().downloadBitmapAdvert(promotion);
				}
				return promotionImage;
			}
		}
	}
	
	/**
	 * 
	 * @author Rosson Chen
	 *
	 */
	public static interface IAdvertOnClick {
		
		/**
		 * advert click
		 * 
		 * @param advert
		 */
		public void onClickAdvert(Advert advert) ;
	}
}
