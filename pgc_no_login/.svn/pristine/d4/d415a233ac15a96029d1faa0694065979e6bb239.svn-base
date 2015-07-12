package cn.dezhisoft.cloud.mi.newugc.common.util;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import android.util.Log;

/**
 * 反射工具类，用于强制读取或设置对象属性
 *            调用方法
 * @author Rosson Chen
 * 2013-5-19
 */
public final class ReflectionUtil {
	private static final String TAG = "ReflectionUtil";
	private ReflectionUtil(){}
	
	public static Object forceGet(Object target, String property) throws NoSuchFieldException{
		if(target == null || property == null){
			throw new IllegalArgumentException("Parameter bean or fieldName can not been null");
		}

		try {
			Field field = null;
			for ( Class<?> clas = target.getClass(); clas != Object.class; clas = clas.getSuperclass() ) {
				try {
					field = clas.getDeclaredField(property);
				} catch (Exception e) {
				}
				if( field != null ){
					field.setAccessible(true);
					return field.get( target );
				}
			}
		} catch ( IllegalArgumentException e){
			Log.d(TAG,"ReflectUtil IllegalArgumentException ", e);
			DebugUtil.traceThrowableLog(e);
		} catch ( IllegalAccessException e){
			Log.d(TAG,"ReflectUtil IllegalAccessException ", e);
			DebugUtil.traceThrowableLog(e);
		}

		throw new NoSuchFieldException(property);
	}
	
	public static void forceSet(Object target, String property, Object value) throws NoSuchFieldException{
		if(target == null || property == null){
			throw new IllegalArgumentException("Parameter bean or fieldName can not been null");
		}

		try {
			Field field = null;
			for ( Class<?> clas = target.getClass(); clas != Object.class; clas = clas.getSuperclass() ) {
				try {
					field = clas.getDeclaredField(property);
				} catch (Exception e) {
				}
				if( field != null ){
					field.setAccessible(true);
					field.set(target, value);
					
					return ;
				}
			}
		} catch ( IllegalArgumentException e){
			Log.d(TAG,"ReflectUtil IllegalArgumentException ", e);
			DebugUtil.traceThrowableLog(e);
		} catch ( IllegalAccessException e){
			Log.d(TAG,"ReflectUtil IllegalAccessException ", e);
			DebugUtil.traceThrowableLog(e);
		}

		throw new NoSuchFieldException(property);
	}
	
	public static Object forceInvoke(Object target, String method, Object...params) throws NoSuchMethodException, InvocationTargetException{
		if(target == null || method == null){
			throw new IllegalArgumentException("Parameter bean or fieldName can not been null");
		}
		
		Class[] clzzParams = getParamsClasses(params);
        
		try {
			Method m = null;
			for ( Class<?> clas = target.getClass(); clas != Object.class; clas = clas.getSuperclass() ) {
				Method[] methods = clas.getDeclaredMethods();
				for(Method me : methods){
					Class<?>[] parameterTypes = me.getParameterTypes();
					if(me.getName().equals(method)){
						return me.invoke(target, params);
					}
				}
			}
		} catch ( IllegalArgumentException e){
			DebugUtil.traceThrowableLog(e);
		} catch ( IllegalAccessException e){
			DebugUtil.traceThrowableLog(e);
		} 
		
		throw new NoSuchMethodException(method);
	}

	private static boolean isMatchParamType(Class[] parameterTypes,
			Object... params) {
		if(null == parameterTypes && null == params){
			return true;
		}
		
		if(null == parameterTypes){
			return false;
		}

		if(null == params){
			return false;
		}
		
		if(params.length != parameterTypes.length){
			return false;
		}
		
		for(int i = 0; i < parameterTypes.length; i++){
			
		}
		
		return false;
	}

	private static Class[] getParamsClasses(Object... params) {
		Class clzzParams[]=null;
        if(params != null){//存在
            int len= params.length;
            clzzParams = new Class[len];
            for(int i=0;i<len;++i){
                clzzParams[i] = params[i].getClass();
            }
        }
		return clzzParams;
	}
}
