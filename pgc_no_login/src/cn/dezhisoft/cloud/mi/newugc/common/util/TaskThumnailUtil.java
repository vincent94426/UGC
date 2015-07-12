package cn.dezhisoft.cloud.mi.newugc.common.util;

import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.media.ThumbnailUtils;
import android.provider.MediaStore;

public class TaskThumnailUtil {
	//private static final String TAG = "TaskThumnailUtil";
	public static final int DFT_WIDTH = 96;
	public static final int DFT_HEIGHT = 96;
	
	
	public static Bitmap getImageThumbnail(String imagePath, int width, int height) {  
        return BitmapUtil.createSquareScaledThumbnail(imagePath, BitmapUtil.ScalingLogic.FIT, width, height); 
    } 
	
	public static Bitmap getImageThumbnail(String imagePath) {  
        return getImageThumbnail(imagePath, DFT_WIDTH, DFT_HEIGHT); 
    } 
	
	public static Bitmap getVideoThumbnail(String videoPath, int width, int height,  
            int kind) {  
        Bitmap bitmap = null;  
        // 获取视频的缩略图  
        /*bitmap = ThumbnailUtils.createVideoThumbnail(videoPath, kind);  
        Log.d(TAG, "w"+bitmap.getWidth());  
        Log.d(TAG, "h"+bitmap.getHeight()); 
        
        bitmap = ThumbnailUtils.extractThumbnail(bitmap, width, height,  
                ThumbnailUtils.OPTIONS_RECYCLE_INPUT);*/  
        
        MediaMetadataRetriever retriever = new MediaMetadataRetriever(); 
        retriever.setDataSource(videoPath);
        bitmap = retriever.getFrameAtTime(1*1000*1000);
        
        bitmap = ThumbnailUtils.extractThumbnail(bitmap, width, height,  
                ThumbnailUtils.OPTIONS_RECYCLE_INPUT);  
        
        return bitmap;  
    }  
	
	public static Bitmap getVideoThumbnail(String videoPath) {  
        return getVideoThumbnail(videoPath, DFT_WIDTH, DFT_HEIGHT, MediaStore.Images.Thumbnails.MICRO_KIND);  
    }  
}
