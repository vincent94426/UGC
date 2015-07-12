package cn.dezhisoft.cloud.mi.newugc.common.util;

import android.os.SystemClock;

public final class OperationEnableTimer {
	private OperationEnableTimer(){
		
	}
	
	public static class Door {
		long lastTime = 0L;
		
		boolean isOpen() {
			return SystemClock.elapsedRealtime() > lastTime;
		}
		
		void close(long time) {
			if(time > 1000 * 240) {
				lastTime = time;
			} else {
				lastTime = SystemClock.elapsedRealtime() + time;
			}
		}
	}
	
	private static Door door = new Door();

	public static boolean isEnable() {
		return door.isOpen();
	}

	public static void disableOperation() {
		door.close(3000);
	}
	
	public static void disableOperation(long time){
		door.close(time);
	}

	public static void disableClickBackOperation() {
		door.close(450);
	}
}
