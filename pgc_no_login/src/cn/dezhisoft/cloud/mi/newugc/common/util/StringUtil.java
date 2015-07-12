package cn.dezhisoft.cloud.mi.newugc.common.util;

import android.text.TextUtils;

public final class StringUtil {
	private StringUtil(){}
	
	public static boolean isBlank( CharSequence charSequence ){
		if( TextUtils.isEmpty( charSequence ) ){
			return true;
		}
		
		for( int i=0, len= charSequence.length() ; i < len ; i++ ){
			if( !Character.isSpaceChar( charSequence.charAt(i) ) ){
				return false;
			}
		}
		
		return true;
	}

	public static boolean isNotBlank( CharSequence charSequence ){
		return !isBlank( charSequence );
	}
}