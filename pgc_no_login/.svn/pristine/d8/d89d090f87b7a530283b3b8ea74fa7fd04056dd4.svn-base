package cn.dezhisoft.cloud.mi.newugc.ioffer.common;

import java.io.ByteArrayOutputStream;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.sobey.sdk.encoder.AbstractEncoder.VideoQuality;
import cn.dezhisoft.cloud.mi.newugc.ugc.core.model.MediaType;

/**
 * 工具类
 * 
 * @author Rosson Chen
 *
 */
public final class Util {

	private final static String regEx	= "？<:：*\">\\”/?|";
	private final static char[] regExs	= regEx.toCharArray() ;
	
	private Util(){
		
	}
	
	/** 检测非法字符: 如果返回不为null,包含了非法字符 */
	public final static String checkInput(String... input){
		final int regSize   = regExs.length ;
		final int inputSize = input.length ;
		for(int s = inputSize - 1 ; s >= 0 ; s--){
			for(int o = regSize -1 ; o >=0 ; o--){
				if(input[s].indexOf(regExs[o]) >= 0)
					return input[s] ;
			}
		}
		return null ;
	}
	
	/** 只允许: 大小写字母,数字,下划线*/
	public final static boolean checkText(String content){
		boolean isRight = false;
		if(content != null && content.trim().length()>0){
			Pattern pattern = Pattern.compile("([0-9]|_|[a-zA-Z])+");
			Matcher matcher = pattern.matcher(content);
			isRight= matcher.matches();
		}
		return isRight;
	}

	
	/** Bitmap 转化成Base64 */
	public static final String bitmapToBase64(Bitmap bitmap){
		try{
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			bitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
			return new String(Base64.encode(out.toByteArray(), Base64.DEFAULT));
		}catch(Exception e){
			e.printStackTrace() ;
			return "" ;
		}
	}
	
	/** Base64转化成Bitmap */
	public static final Bitmap base64ToBitmap(String base64){
		try{
			byte[] out = Base64.decode(base64, Base64.DEFAULT);
			return BitmapFactory.decodeByteArray(out, 0, out.length);
		}catch(Exception e){
			e.printStackTrace() ;
		}
		return null ;
	}
	
	/** 计算比特率 */
	public final static int calculateBitrate(int width, int height, int quality) {
		
		if(quality == VideoQuality.VIDEO_QUALITY_LOW){
			return identicalBitrate(width * height * 2);
		} else if(quality == VideoQuality.VIDEO_QUALITY_MEDIUM){
			return identicalBitrate(width * height * 4);
		} else if(quality == VideoQuality.VIDEO_QUALITY_HEIGH){
			return identicalBitrate(width * height * 8);
		} else {
			return identicalBitrate(width * height * 2);
		}
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
	
	/** 隐藏键盘 */
	public static void hideVirtualKeyPad(Context context, View view) {
		InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
		if (view != null && view.getWindowToken() != null)
			imm.hideSoftInputFromWindow(view.getWindowToken(),InputMethodManager.HIDE_NOT_ALWAYS);
	}
	
	/** 创建媒体类型 */
	public static MediaType createMediaType(String type){
		
		if(type == null) return MediaType.UNKOWN ;
		
		if(type.equals("V")){
			return MediaType.VIDEO ;
		} else if(type.equals("A")){
			return MediaType.AUDIO ;
		} else if(type.equals("F")){
			return MediaType.IMAGE ;
		} else {
			return MediaType.UNKOWN ;
		}
	}
	
	/** 通过轨道类型创建媒体类型 */
	public static MediaType createMediaTypeByTrack(String type){
		
		if(type == null) return MediaType.UNKOWN ;
		
		if(type.equals("V")){
			return MediaType.VIDEO ;
		} else if(type.equals("F")){
			return MediaType.IMAGE ;
		} else {
			return MediaType.AUDIO ;
		}
	}
}
