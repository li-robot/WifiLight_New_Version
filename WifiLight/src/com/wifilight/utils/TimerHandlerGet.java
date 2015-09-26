package com.wifilight.utils;

import android.os.Looper;

public class TimerHandlerGet {
	
	private static TimeoutHandler timeoutHandler ;
	private TimerHandlerGet(){
		// do nothing
	}
	
	public static TimeoutHandler getInstance(long timeDelay,Looper looper){
		if(timeoutHandler == null){
			timeoutHandler = new TimeoutHandler(timeDelay,looper);
			return timeoutHandler;
		}
		return timeoutHandler;
	}
	
}
