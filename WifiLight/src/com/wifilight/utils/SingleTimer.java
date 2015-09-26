package com.wifilight.utils;

import java.util.Timer;
import java.util.TimerTask;

import com.wifilight.utils.SingleTask.WorkTask;

public class SingleTimer {
	private static Timer timer = new Timer();
	private SingleTimer(){
		// do nothing 
	}
	
	public static Timer getTimer(){
		if(timer == null){
			synchronized(SingleTimer.class){
				timer = new Timer();
			}
		}
		return timer;
	}
	
	public static void cancel(){
		try {
			if(timer != null)
				timer.cancel();
		} catch(IllegalStateException e){
			System.out.println("timer already cancel!");
		}
	}
}
