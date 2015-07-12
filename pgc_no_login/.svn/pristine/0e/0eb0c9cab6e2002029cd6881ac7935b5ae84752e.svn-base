package cn.dezhisoft.cloud.mi.newugc.ioffer.ui;

import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;

/**
 * ͼƬ������
 * 
 * @author Rosson Chen
 *
 */
public class BitmapUtil {

	/** Сͼ*/
	public static final int MODE_SMALL_BITMAP		= 1 ;
	
	/** �е�ͼ*/
	public static final int MODE_MEDIUM_BITMAP		= MODE_SMALL_BITMAP + 1 ;
	
	/** ��ͼ*/
	public static final int MODE_BIG_BITMAP			= MODE_MEDIUM_BITMAP + 1 ;
	
	/** icon ���*/
	public static final int ICON_WIDTH			= 48 ;
	
	/** icon �߶�*/
	public static final int ICON_HEIGHT			= 40 ;
	
	/** icon ���*/
	public static final int ADVERT_WIDTH		= 176 ;
	
	/** icon �߶�*/
	public static final int ADVERT_HEIGHT		= 144 ;
	
	/** icon ���*/
	public static final int PREVIEW_WIDTH		= 320 ;
	
	/** icon �߶�*/
	public static final int PREVIEW_HEIGHT		= 240 ;

	/**
	 * ͨ��byte[]����ͼƬ
	 * @param mode
	 * @param data
	 * @return
	 */
	public static Bitmap createBitmap(int mode, byte[] data) {
		int minSideLength 	= 0 ;
		Options opts  		= new BitmapFactory.Options();
		switch (mode) {
		case MODE_SMALL_BITMAP:
			opts.inJustDecodeBounds  = true;//ȷ��ͼƬ�����ص��ڴ�
            BitmapFactory.decodeByteArray(data, 0, data.length,opts);
            // ����ͼƬ���ű���
            minSideLength 	 		 = Math.min(ICON_WIDTH, ICON_HEIGHT);
            opts.inSampleSize 		 = computeSampleSize(opts, minSideLength, ICON_WIDTH * ICON_HEIGHT);
            opts.inJustDecodeBounds  = false;
            opts.inInputShareable 	 = true;
            opts.inPurgeable 		 = true;
            opts.inPreferredConfig 	 = Config.RGB_565 ;
			break;
		case MODE_MEDIUM_BITMAP:
			opts.inJustDecodeBounds = true;// ȷ��ͼƬ�����ص��ڴ�
			BitmapFactory.decodeByteArray(data, 0, data.length,opts);
			// ����ͼƬ���ű���
			minSideLength 			= Math.min(PREVIEW_WIDTH, PREVIEW_HEIGHT);
			opts.inSampleSize 		= computeSampleSize(opts,minSideLength, PREVIEW_WIDTH * PREVIEW_HEIGHT);
			opts.inJustDecodeBounds = false;
			opts.inInputShareable 	= true;
			opts.inPurgeable 		= true;
			opts.inPreferredConfig 	= Config.RGB_565;
			break;
		case MODE_BIG_BITMAP:
			opts.inJustDecodeBounds = true;//ȷ��ͼƬ�����ص��ڴ�
            BitmapFactory.decodeByteArray(data, 0, data.length,opts);
            // ����ͼƬ���ű���
            minSideLength 			= Math.min(ADVERT_WIDTH, ADVERT_HEIGHT);
            opts.inSampleSize 		= computeSampleSize(opts, minSideLength, ADVERT_WIDTH * ADVERT_HEIGHT);
            opts.inJustDecodeBounds = false;
            opts.inInputShareable 	= true;
            opts.inPurgeable 		= true;
            opts.inPreferredConfig 	= Config.RGB_565 ;
			break;
		}
		// ����ͼƬ
		return BitmapFactory.decodeByteArray(data, 0, data.length, opts);
	}
	
	/**
	 * ͨ���ļ�·������ͼƬ
	 * @param mode
	 * @param path
	 * @return
	 */
	public static Bitmap createBitmap(int mode, String path) {
		int minSideLength 	= 0 ;
		Options opts  		= new BitmapFactory.Options();
		switch (mode) {
		case MODE_SMALL_BITMAP:
			opts.inJustDecodeBounds  = true;//ȷ��ͼƬ�����ص��ڴ�
            BitmapFactory.decodeFile(path,opts);
            // ����ͼƬ���ű���
            minSideLength 	 		 = Math.min(ICON_WIDTH, ICON_HEIGHT);
            opts.inSampleSize 		 = computeSampleSize(opts, minSideLength, ICON_WIDTH * ICON_HEIGHT);
            opts.inJustDecodeBounds  = false;
            opts.inInputShareable 	 = true;
            opts.inPurgeable 		 = true;
            opts.inPreferredConfig 	 = Config.RGB_565 ;
			break;
		case MODE_MEDIUM_BITMAP:
			opts.inJustDecodeBounds = true;// ȷ��ͼƬ�����ص��ڴ�
			BitmapFactory.decodeFile(path,opts);
			// ����ͼƬ���ű���
			minSideLength 			= Math.min(PREVIEW_WIDTH, PREVIEW_HEIGHT);
			opts.inSampleSize 		= computeSampleSize(opts,minSideLength, PREVIEW_WIDTH * PREVIEW_HEIGHT);
			opts.inJustDecodeBounds = false;
			opts.inInputShareable 	= true;
			opts.inPurgeable 		= true;
			opts.inPreferredConfig 	= Config.RGB_565;
			break;
		case MODE_BIG_BITMAP:
			opts.inJustDecodeBounds = true;//ȷ��ͼƬ�����ص��ڴ�
            BitmapFactory.decodeFile(path,opts);
            // ����ͼƬ���ű���
            minSideLength 			= Math.min(ADVERT_WIDTH, ADVERT_HEIGHT);
            opts.inSampleSize 		= computeSampleSize(opts, minSideLength, ADVERT_WIDTH * ADVERT_HEIGHT);
            opts.inJustDecodeBounds = false;
            opts.inInputShareable 	= true;
            opts.inPurgeable 		= true;
            opts.inPreferredConfig 	= Config.RGB_565 ;
			break;
		}
		// ����ͼƬ
		return BitmapFactory.decodeFile(path,opts);
	}
	
	/**
	 * 
	 * @param options
	 * @param minSideLength
	 * @param maxNumOfPixels
	 * @return
	 */
	public final static int computeSampleSize(BitmapFactory.Options options,int minSideLength, int maxNumOfPixels) {
	    int initialSize = computeInitialSampleSize(options, minSideLength, maxNumOfPixels);

	    int roundedSize;
	    if (initialSize <= 8) {
	        roundedSize = 1;
	        while (roundedSize < initialSize) {
	            roundedSize <<= 1;
	        }
	    } else {
	        roundedSize = (initialSize + 7) / 8 * 8;
	    }
	    return roundedSize;
	}
	
	/**
	 * 
	 * @param options
	 * @param minSideLength
	 * @param maxNumOfPixels
	 * @return
	 */
	private final static int computeInitialSampleSize(BitmapFactory.Options options, int minSideLength, int maxNumOfPixels) {
	    double w = options.outWidth;
	    double h = options.outHeight;

	    int lowerBound = (maxNumOfPixels == -1) ? 1 : (int) Math.ceil(Math.sqrt(w * h / maxNumOfPixels));
	    int upperBound = (minSideLength == -1) ? 128 : (int) Math.min(Math.floor(w / minSideLength), Math.floor(h / minSideLength));

	    if (upperBound < lowerBound) {
	        // return the larger one when there is no overlapping zone.
	        return lowerBound;
	    }

	    if ((maxNumOfPixels == -1) && (minSideLength == -1)) {
	        return 1;
	    } else if (minSideLength == -1) {
	        return lowerBound;
	    } else {
	        return upperBound;
	    }
	}
}
