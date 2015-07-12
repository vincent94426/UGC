package cn.dezhisoft.cloud.mi.newugc.common.util;

import java.math.BigDecimal;

import android.text.TextUtils;

public class CommonUtils {
	public static final String TRUE = "1";
	public static final String FALSE = "0";

	public static boolean isChecked(String isChecked) {
		return TRUE.equals(isChecked);
	}

	public static String isChecked(boolean isChecked) {
		return isChecked ? TRUE : FALSE;
	}

	/**
	 * 模糊匹配
	 * 
	 * @param content
	 * @param keyword
	 * @return
	 */
	public static boolean match(String content, String keyword) {
		// 匹配字符串为空,不需要抛出运行时异常
		if (content == null) {
			//throw new RuntimeException("Content is empty.");
			return false;
		}
		// 关键词为空
		if (keyword == null) {
			return false;
		}
		// 关键词全匹配
		if (keyword.equalsIgnoreCase(content)) {
			return true;
		}
		// 匹配字符串包含关键词
		if (content.contains(keyword)) {
			return true;
		}
		return false;
	}

	private static final long KB = 1024;
	private static final long MB = 1024 * 1024;
	private static final long GB = 1024 * 1024 * 1024;

	public static boolean DEBUG = false;
	
	public static String toLargeUnit(long size,int dot) {
		if(size < MB) {
			return String.format("%."+dot+"fKB", (double)size / KB);
		} else if(size < GB) {
			return String.format("%."+dot+"fMB", (double)size / MB);
		} else {
			return String.format("%."+dot+"fGB", (double)size / GB);
		}
	}
	
	public static String toLargeUnit(long size) {
		return toLargeUnit(size, 2);
	}
	
	//将字节转换成兆
	public static double getMBSize(long size) {
		BigDecimal byteSize = new BigDecimal(size);
		return byteSize.divide(new BigDecimal(1024*1024)).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
	}
	
	//将字节转换成兆
	public static String getMBSizeStr(long size) {
		return toLargeUnit(size);
	}
	
	/** 计算比特率 */
	public final static int calculateBitrate(int width, int height, int quality) {
		return identicalBitrate(width * height * quality);
	}
	
	/** 取整 */
	private final static int identicalBitrate(int bitrate){
		// <= 200K
		if(bitrate <= 200 * 1024){
			int n		= bitrate / 1024 ;
			int decade 	= n / 10 ;
			int unit	= n % 10 ;
			return unit > 6 ? ((decade + 1) * 10) * 1024 : (decade * 10) * 1024 ;
		} 
		// <= 2M: 1.6 * 1024*1024
		else if(bitrate <= 2 * 1024 * 1024){
			int n		= bitrate / 1024 ;
			int decade 	= n / 100 ;
			int unit	= n % 100 ;
			return unit > 60 ? ((decade + 1) * 100) * 1024 : (decade * 100) * 1024 ;
		} 
		// > 2M
		else if(bitrate > 2 * 1024 * 1024){
			int n		= bitrate / 1024 ;
			int decade 	= n / 1024 ;
			int unit	= n % 1024 ;
			return unit > 600 ? ((decade + 1) * 1024) * 1024 : (decade * 1024) * 1024 ;
		} else {
			return bitrate ;
		}
	}
	
	/** 比特率文本格式化*/
	public static String formatBitrate(int bitrate){
		if(bitrate < 1024 * 1024){
			return (bitrate / 1024) + "kbps" ; 
		} else {
			return (bitrate / (1024 * 1024)) + "Mbps" ; 
		}
	}
	
	/**
	 * 仅根据文件后缀判断是否是图片格式
	 * @param path
	 * @return
	 */
	public static boolean isPictureFile(String path) {
		boolean isPic = false;
		if(!TextUtils.isEmpty(path) && 
				(path.endsWith(".jpg")
				|| path.endsWith(".jpeg")
				|| path.endsWith(".png")
				|| path.endsWith(".gif")
				|| path.endsWith(".bmp")
				|| path.endsWith(".tif"))){
			isPic = true;
		}
		return isPic;
	}
}
