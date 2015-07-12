package cn.dezhisoft.cloud.mi.newugc.common.util;

import android.content.Context;
import android.content.SharedPreferences;

public final class SettingUtil {
	private SettingUtil(){}
	private static final String PREFERENCE_NAME = "npm_share_preference";

	public static void writeString(Context context,String name, String value){
		try {
			SharedPreferences sp = context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_WORLD_WRITEABLE);
			SharedPreferences.Editor ed = sp.edit();
			ed.putString(name, value);
			ed.commit();  
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
    public static void writeString(SharedPreferences sp,String name, String value){
        try {
            SharedPreferences.Editor ed = sp.edit();
            ed.putString(name, value);
            ed.commit();  
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
	public static String readString(Context context,String name, String dftValue ){
		String returnValue = "";
		try {
			SharedPreferences sp = context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_WORLD_WRITEABLE);
			returnValue = sp.getString(name, dftValue);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return returnValue;
	}
    public static String readString(SharedPreferences sp,String name, String dftValue ){
        String returnValue = "";
        try {
            returnValue = sp.getString(name, dftValue);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return returnValue;
    }
	public static void writeLong(Context context,String name, long value ){
		try {
			SharedPreferences sp = context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_WORLD_WRITEABLE);
			SharedPreferences.Editor ed = sp.edit();
			ed.putLong( name , value );
			ed.commit();  
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
    public static void writeLong(SharedPreferences sp,String name, long value ){
        try {
            SharedPreferences.Editor ed = sp.edit();
            ed.putLong( name , value );
            ed.commit();  
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
	public static long readLong(Context context,String name, long dftValue ){
		long returnValue;
		try {
			SharedPreferences sp = context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_WORLD_WRITEABLE);
			returnValue = sp.getLong(name, dftValue);
		} catch (Exception e) {
			e.printStackTrace();
			returnValue = 0L;
		}
		return returnValue;
	}
    public static long readLong(SharedPreferences sp,String name, long dftValue ){
        long returnValue;
        try {
            returnValue = sp.getLong(name, dftValue);
        } catch (Exception e) {
            e.printStackTrace();
            returnValue = 0L;
        }
        return returnValue;
    }
	public static void writeBoolean(Context context,String name, boolean value ){
		try {
			SharedPreferences sp = context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_WORLD_WRITEABLE);
			SharedPreferences.Editor ed = sp.edit();
			ed.putBoolean( name , value );
			ed.commit();  
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
    public static void writeBoolean(SharedPreferences sp,String name, boolean value ){
        try {
            SharedPreferences.Editor ed = sp.edit();
            ed.putBoolean( name , value );
            ed.commit();  
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
	public static boolean readBoolean(Context context,String name, boolean dftValue ){
		boolean returnValue;
		try {
			SharedPreferences sp = context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_WORLD_WRITEABLE);
			returnValue = sp.getBoolean(name, dftValue);
		} catch (Exception e) {
			e.printStackTrace();
			returnValue = false;
		}
		return returnValue;
	}
    public static boolean readBoolean(SharedPreferences sp,String name, boolean dftValue ){
        boolean returnValue;
        try {
            returnValue = sp.getBoolean(name, dftValue);
        } catch (Exception e) {
            e.printStackTrace();
            returnValue = false;
        }
        return returnValue;
    }
	public static void writeInt(Context context,String name, int value ){
		try {
			SharedPreferences sp = context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_WORLD_WRITEABLE);
			SharedPreferences.Editor ed = sp.edit();
			ed.putInt( name , value );
			ed.commit();  
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
    public static void writeInt(SharedPreferences sp,String name, int value ){
        try {
            SharedPreferences.Editor ed = sp.edit();
            ed.putInt( name , value );
            ed.commit();  
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
	public static int readInt(Context context,String name, int dftValue ){
		int returnVlaue;
		try {
			SharedPreferences sp = context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_WORLD_WRITEABLE);
			returnVlaue = sp.getInt(name, dftValue);
		} catch (Exception e) {
			e.printStackTrace();
			returnVlaue = 0;
		}
		return returnVlaue;
	}
    public static int readInt(SharedPreferences sp,String name, int dftValue ){
        int returnVlaue;
        try {
            returnVlaue = sp.getInt(name, dftValue);
        } catch (Exception e) {
            e.printStackTrace();
            returnVlaue = 0;
        }
        return returnVlaue;
    }
	public static void remove(Context context,String name){
		try {
			SharedPreferences sp = context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_WORLD_WRITEABLE);
			SharedPreferences.Editor ed = sp.edit();
			ed.remove(name);
			ed.commit();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
    public static void remove(SharedPreferences sp,String name){
        try {
            SharedPreferences.Editor ed = sp.edit();
            ed.remove(name);
            ed.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
