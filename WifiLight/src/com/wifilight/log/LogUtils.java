package com.wifilight.log;

import android.util.Log;

public class LogUtils {
	
	private static boolean logEnable = true;
	
	public static int LOG_LEVEL_WARNING = Log.WARN;
	public static int LOG_LEVEL_DEBUG = Log.DEBUG;
	public static int LOG_LEVEL_INFO = Log.INFO;
	public static int LOG_LEVEL_ERROR = Log.ERROR;
	
	public static void logToConsole(String tag,String msg,int level){
		if(logEnable){
			switch(level){
				case Log.WARN:
					Log.w(tag, msg);
					break;
				case Log.DEBUG:
					Log.d(tag, msg);
					break;
				case Log.INFO:
					Log.i(tag, msg);
					break;
				case Log.ERROR:
					Log.e(tag, msg);
					break;
			}
		}	
	}
	
	public static void logToFile() {
		
	}
	
	public static void setLogEnable(boolean enable) {
		logEnable = enable;
	}
	
}
