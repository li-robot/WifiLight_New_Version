package com.wifilight.utils;

import java.util.Timer;
import java.util.TimerTask;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;

public class TimeoutHandler {
	private Timer timer ;
	MyHandler handler;
	
	public TimeoutHandler(long timeout,Looper looper){
		handler = new MyHandler(looper);
		timer = new Timer();
		timer.schedule(new TimerTask(){
			@Override
			public void run() {
				Message msg = new Message();
				handler.sendMessage(msg);
			}
		}, timeout);
	}
	
	class MyHandler extends Handler{

		public MyHandler(Looper looper){
			super(looper);
		}
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			if(onTimeout != null){
				onTimeout.onTimeout();
			}
		}
	}
	
	public interface OnTimeoutListener {
		public void onTimeout();
	}
	
	public OnTimeoutListener onTimeout;
	
	public void setOnTimeoutListener(OnTimeoutListener onTimeout){
		this.onTimeout = onTimeout;
	}
	
	public void cancel(){
		if(timer != null){
			timer.cancel();
			System.out.println("timer cancel !");
		}
	}
}
