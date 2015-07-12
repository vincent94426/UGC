/*
 * Copyright (c) 2010, Sony Ericsson Mobile Communication AB. All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without modification,
 * are permitted provided that the following conditions are met:
 *
 *    * Redistributions of source code must retain the above copyright notice, this
 *      list of conditions and the following disclaimer.
 *    * Redistributions in binary form must reproduce the above copyright notice,
 *      this list of conditions and the following disclaimer in the documentation
 *      and/or other materials provided with the distribution.
 *    * Neither the name of the Sony Ericsson Mobile Communication AB nor the names
 *      of its contributors may be used to endorse or promote products derived from
 *      this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED.
 * IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT,
 * INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING,
 * BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE,
 * DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE
 * OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED
 * OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package cn.dezhisoft.cloud.mi.newugc.common.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import org.apache.http.HttpResponse;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Environment;
import android.os.SystemClock;
import android.util.Log;
import cn.dezhisoft.cloud.mi.newugc.common.constants.NpmConstants;
import cn.dezhisoft.cloud.mi.newugc.common.net.HttpUtil;


/**
 * Class containing static utility methods for bitmap decoding and scaling
 *
 * @author Andreas Agvard (andreas.agvard@sonyericsson.com)
 */
public class BitmapUtil {

	public static final int ICON_WIDTH = 48;
	public static final int ICON_HEIGHT = 48;
	
	/**
	 * 创建图标从数据流中
	 * @param in
	 * @param scalingLogic
	 * @return
	 */
	public static Bitmap createIconScaledBitmap(InputStream in, ScalingLogic scalingLogic) {
		int width = ICON_WIDTH;
		int height = ICON_HEIGHT;
		final long startTime = SystemClock.uptimeMillis();
		
		Bitmap old = decodeInputStream(in, width, height, scalingLogic);
		
		if(old == null) {
			return old;
		}
		
		// Calculate memory usage and performance statistics
        final int memUsageKb = (old.getRowBytes() * old.getHeight()) / 1024;
        if(memUsageKb < 21) {
        	return old;
        }
        
		Bitmap scaledBitmap = BitmapUtil.createScaledBitmap(old, width, height, ScalingLogic.FIT);
		old.recycle();
        
        final long stopTime = SystemClock.uptimeMillis();

        // Publish results
        Log.d("Icon", "Time taken: " + (stopTime - startTime)
                + " ms. Memory used for scaling: " + memUsageKb + " -> " + (scaledBitmap.getRowBytes() * old.getHeight() / 1024));
        
        return scaledBitmap;
	}
	
	public static Bitmap createThumbnailScaledBitmap(InputStream in, ScalingLogic scalingLogic, int width, int height) {
		final long startTime = SystemClock.uptimeMillis();
		
		Bitmap old = decodeInputStream(in, width, height, scalingLogic);
		
		// Calculate memory usage and performance statistics
        final int memUsageKb = (old.getRowBytes() * old.getHeight()) / 1024;
        if(memUsageKb < 18) {
        	return old;
        }
        
		Bitmap scaledBitmap = BitmapUtil.createScaledBitmap(old, width, height, ScalingLogic.FIT);
		old.recycle();
        
        final long stopTime = SystemClock.uptimeMillis();

        // Publish results
        Log.d("Icon", "Time taken: " + (stopTime - startTime)
                + " ms. Memory used for scaling: " + memUsageKb + " -> " + (scaledBitmap.getRowBytes() * old.getHeight() / 1024));
        
        return scaledBitmap;
	}
	
	public static Bitmap createThumbnailScaledBitmap(byte[] netBytes, ScalingLogic scalingLogic, int width, int height) {
		final long startTime = SystemClock.uptimeMillis();
		
		Bitmap old = decodeFromBytes(netBytes, width, height, scalingLogic);
		
		// Calculate memory usage and performance statistics
        final int memUsageKb = (old.getRowBytes() * old.getHeight()) / 1024;
        if(memUsageKb < 18) {
        	return old;
        }
        
		Bitmap scaledBitmap = BitmapUtil.createScaledBitmap(old, width, height, ScalingLogic.FIT);
		old.recycle();
        
        final long stopTime = SystemClock.uptimeMillis();

        // Publish results
        Log.d("Icon", "Time taken: " + (stopTime - startTime)
                + " ms. Memory used for scaling: " + memUsageKb + " -> " + (scaledBitmap.getRowBytes() * old.getHeight() / 1024));
        
        return scaledBitmap;
	}
	
	public static Bitmap createThumbnailScaledBitmap(String fileName, ScalingLogic scalingLogic, int width, int height) {
		final long startTime = SystemClock.uptimeMillis();
		
		Options options = new Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(fileName, options);
        options.inJustDecodeBounds = false;
        options.inSampleSize = calculateSampleSize(options.outWidth, options.outHeight, width,
        		height, scalingLogic);
        Bitmap unscaledBitmap = BitmapFactory.decodeFile(fileName, options);
        if(null == unscaledBitmap){
        	return null;
        }
        
        Bitmap scaledBitmap = BitmapUtil.createScaledBitmap(unscaledBitmap, width, height, ScalingLogic.FIT);
        if(unscaledBitmap != scaledBitmap){
        	unscaledBitmap.recycle();
        }
        
        return scaledBitmap;
	}
	
	/**
	 * 从文件中生产正方形的缩略图
	 * 
	 * @param fileName
	 * @param scalingLogic
	 * @param width
	 * @param height
	 * @return
	 */
	public static Bitmap createSquareScaledThumbnail(String fileName, ScalingLogic scalingLogic, int width, int height) {
		Bitmap scaledBitmap = createThumbnailScaledBitmap(fileName, scalingLogic, width, height);
		
		if(scaledBitmap == null){
			return null;
		}
		
		return createSquareBitmap(scaledBitmap);
	}

	public static Bitmap createSquareBitmap(Bitmap scaledBitmap) {
		int w = scaledBitmap.getWidth();
		int h = scaledBitmap.getHeight();
		
		int wh = w > h ? h : w;
		
		int retX = w > h ? (w - h) / 2 : 0;
		int retY = w > h ? 0 : (h - w) / 2;
		Bitmap squareBitmap = Bitmap.createBitmap(scaledBitmap, retX, retY, wh, wh, null, false);
		if(squareBitmap != scaledBitmap){
			scaledBitmap.recycle();
		}
		
        return squareBitmap;
	}
	
	public static Bitmap createSquareScaledThumbnail(InputStream in, ScalingLogic scalingLogic, int width, int height) {
		Bitmap scaledBitmap = createThumbnailScaledBitmap(in, scalingLogic, width, height);
		
		return createSquareBitmap(scaledBitmap);
	}
	
	public static Bitmap createSquareScaledThumbnail(byte[] netBytes, ScalingLogic scalingLogic, int width, int height) {
		Bitmap scaledBitmap = createThumbnailScaledBitmap(netBytes, scalingLogic, width, height);
		
		return createSquareBitmap(scaledBitmap);
	}
	
    public static Bitmap decodeInputStream(InputStream in, int dstWidth, int dstHeight,
            ScalingLogic scalingLogic) {
    	
        Options options = new Options();
        options.inJustDecodeBounds = false;
        options.inSampleSize = calculateSampleSize(options.outWidth, options.outHeight, dstWidth,
                dstHeight, scalingLogic);
//        Bitmap unscaledBitmap = BitmapFactory.decodeStream(in, null, options);
        Bitmap unscaledBitmap = null;
		try {
			byte[] netBytes = IOUtil.readBytes(in);
			unscaledBitmap = BitmapFactory.decodeByteArray(netBytes, 0, netBytes.length, options);
		} catch (IOException e) {
			e.printStackTrace();
		}

        return unscaledBitmap;
    }
    
    public static Bitmap decodeFromBytes(byte[] netBytes, int dstWidth, int dstHeight,
            ScalingLogic scalingLogic) {
    	
        Options options = new Options();
        options.inJustDecodeBounds = false;
        options.inSampleSize = calculateSampleSize(options.outWidth, options.outHeight, dstWidth,
                dstHeight, scalingLogic);
        Bitmap unscaledBitmap = BitmapFactory.decodeByteArray(netBytes, 0, netBytes.length, options);

        return unscaledBitmap;
    }
    /**
     * Utility function for decoding an image resource. The decoded bitmap will
     * be optimized for further scaling to the requested destination dimensions
     * and scaling logic.
     *
     * @param res The resources object containing the image data
     * @param resId The resource id of the image data
     * @param dstWidth Width of destination area
     * @param dstHeight Height of destination area
     * @param scalingLogic Logic to use to avoid image stretching
     * @return Decoded bitmap
     */
    public static Bitmap decodeResource(Resources res, int resId, int dstWidth, int dstHeight,
            ScalingLogic scalingLogic) {
        Options options = new Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(res, resId, options);
        options.inJustDecodeBounds = false;
        options.inSampleSize = calculateSampleSize(options.outWidth, options.outHeight, dstWidth,
                dstHeight, scalingLogic);
        Bitmap unscaledBitmap = BitmapFactory.decodeResource(res, resId, options);

        return unscaledBitmap;
    }

    /**
     * Utility function for creating a scaled version of an existing bitmap
     *
     * @param unscaledBitmap Bitmap to scale
     * @param dstWidth Wanted width of destination bitmap
     * @param dstHeight Wanted height of destination bitmap
     * @param scalingLogic Logic to use to avoid image stretching
     * @return New scaled bitmap object
     */
    public static Bitmap createScaledBitmap(Bitmap unscaledBitmap, int dstWidth, int dstHeight,
            ScalingLogic scalingLogic) {
        Rect srcRect = calculateSrcRect(unscaledBitmap.getWidth(), unscaledBitmap.getHeight(),
                dstWidth, dstHeight, scalingLogic);
        Rect dstRect = calculateDstRect(unscaledBitmap.getWidth(), unscaledBitmap.getHeight(),
                dstWidth, dstHeight, scalingLogic);
        Bitmap scaledBitmap = Bitmap.createBitmap(dstRect.width(), dstRect.height(),
                Config.RGB_565);
        Canvas canvas = new Canvas(scaledBitmap);
        canvas.drawBitmap(unscaledBitmap, srcRect, dstRect, new Paint(Paint.FILTER_BITMAP_FLAG));

        return scaledBitmap;
    }

    /**
     * ScalingLogic defines how scaling should be carried out if source and
     * destination image has different aspect ratio.
     *
     * CROP: Scales the image the minimum amount while making sure that at least
     * one of the two dimensions fit inside the requested destination area.
     * Parts of the source image will be cropped to realize this.
     *
     * FIT: Scales the image the minimum amount while making sure both
     * dimensions fit inside the requested destination area. The resulting
     * destination dimensions might be adjusted to a smaller size than
     * requested.
     */
    public static enum ScalingLogic {
        CROP, FIT
    }

    /**
     * Calculate optimal down-sampling factor given the dimensions of a source
     * image, the dimensions of a destination area and a scaling logic.
     *
     * @param srcWidth Width of source image
     * @param srcHeight Height of source image
     * @param dstWidth Width of destination area
     * @param dstHeight Height of destination area
     * @param scalingLogic Logic to use to avoid image stretching
     * @return Optimal down scaling sample size for decoding
     */
    public static int calculateSampleSize(int srcWidth, int srcHeight, int dstWidth, int dstHeight,
            ScalingLogic scalingLogic) {
        if (scalingLogic == ScalingLogic.FIT) {
            final float srcAspect = (float)srcWidth / (float)srcHeight;
            final float dstAspect = (float)dstWidth / (float)dstHeight;

            if (srcAspect > dstAspect) {
                return srcWidth / dstWidth;
            } else {
                return srcHeight / dstHeight;
            }
        } else {
            final float srcAspect = (float)srcWidth / (float)srcHeight;
            final float dstAspect = (float)dstWidth / (float)dstHeight;

            if (srcAspect > dstAspect) {
                return srcHeight / dstHeight;
            } else {
                return srcWidth / dstWidth;
            }
        }
    }

    /**
     * Calculates source rectangle for scaling bitmap
     *
     * @param srcWidth Width of source image
     * @param srcHeight Height of source image
     * @param dstWidth Width of destination area
     * @param dstHeight Height of destination area
     * @param scalingLogic Logic to use to avoid image stretching
     * @return Optimal source rectangle
     */
    public static Rect calculateSrcRect(int srcWidth, int srcHeight, int dstWidth, int dstHeight,
            ScalingLogic scalingLogic) {
        if (scalingLogic == ScalingLogic.CROP) {
            final float srcAspect = (float)srcWidth / (float)srcHeight;
            final float dstAspect = (float)dstWidth / (float)dstHeight;

            if (srcAspect > dstAspect) {
                final int srcRectWidth = (int)(srcHeight * dstAspect);
                final int srcRectLeft = (srcWidth - srcRectWidth) / 2;
                return new Rect(srcRectLeft, 0, srcRectLeft + srcRectWidth, srcHeight);
            } else {
                final int srcRectHeight = (int)(srcWidth / dstAspect);
                final int scrRectTop = (int)(srcHeight - srcRectHeight) / 2;
                return new Rect(0, scrRectTop, srcWidth, scrRectTop + srcRectHeight);
            }
        } else {
            return new Rect(0, 0, srcWidth, srcHeight);
        }
    }

    /**
     * Calculates destination rectangle for scaling bitmap
     *
     * @param srcWidth Width of source image
     * @param srcHeight Height of source image
     * @param dstWidth Width of destination area
     * @param dstHeight Height of destination area
     * @param scalingLogic Logic to use to avoid image stretching
     * @return Optimal destination rectangle
     */
    public static Rect calculateDstRect(int srcWidth, int srcHeight, int dstWidth, int dstHeight,
            ScalingLogic scalingLogic) {
        if (scalingLogic == ScalingLogic.FIT) {
            final float srcAspect = (float)srcWidth / (float)srcHeight;
            final float dstAspect = (float)dstWidth / (float)dstHeight;

            if (srcAspect > dstAspect) {
                int computedstHeight = ((int)(dstWidth / srcAspect) == 0) ? srcHeight : (int)(dstWidth / srcAspect);
				return new Rect(0, 0, dstWidth, computedstHeight);
            } else {
                int computedstWidth = ((int)(dstHeight * srcAspect) == 0) ? srcWidth : (int)(dstHeight * srcAspect);
				return new Rect(0, 0, computedstWidth, dstHeight);
            }
        } else {
            return new Rect(0, 0, dstWidth, dstHeight);
        }
    }
    
    public static Drawable loadDrawableFromHttpUrl(String httpUrl) {
		Drawable drawable = null;
		Log.d(NpmConstants.NPM_DEBUG_TAG, "getDrawable from http:" + httpUrl);
		URL url;
		try {
			url = new URL(httpUrl);
			drawable = Drawable.createFromStream(url.openStream(), "");
			drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
		} catch (Exception e) {
			DebugUtil.traceThrowableLog(e);
		}
		return drawable;
	}

    public static Bitmap loadBitmapFromHttpUrl(String httpUrl) {
		Bitmap drawable = null;
		Log.d(NpmConstants.NPM_DEBUG_TAG, "loadBitmapFromHttpUrl:" + httpUrl);
		try {
			String bitmapTmpPath = Environment.getExternalStorageDirectory().getAbsoluteFile() + NpmConstants.PRIVATE_DATA_PATH + "/tmp/";
			File dirBitmapTmp = new File(bitmapTmpPath);
			if(!dirBitmapTmp.exists()){
				dirBitmapTmp.mkdirs();
			}
			
			String bitmapTmpFile = bitmapTmpPath + "/" + httpUrl.hashCode() + ".jpg";
			FileOutputStream tmpFos = new FileOutputStream(bitmapTmpFile);
			HttpResponse resp = HttpUtil.doGetResponse(httpUrl);
			IOUtil.fromTo(resp.getEntity().getContent(), tmpFos);
			
			File bitFile = new File(bitmapTmpFile);
			Options option = new Options();
			if(bitFile.length() > 1000 * 1024){
				option.inSampleSize = 8;
			} else if(bitFile.length() > 500 * 1024){
				option.inSampleSize = 4;
			} else if(bitFile.length() > 100 * 1024){
				option.inSampleSize = 2;
			}
			if(option.inSampleSize > 1){
				drawable = BitmapFactory.decodeStream(new FileInputStream(bitFile), null, option);
			}else {
				drawable = BitmapFactory.decodeStream(new FileInputStream(bitFile));
			}
			
			DebugUtil.traceLog("net bitmap:" + drawable);
		} catch (Exception e) {
			DebugUtil.traceThrowableLog(e);
		} 
		return drawable;
	}
}
