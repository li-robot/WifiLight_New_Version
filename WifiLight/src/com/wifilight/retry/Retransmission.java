package com.wifilight.retry;


import java.util.HashMap;
import java.util.Map;

public class Retransmission extends Thread{
	
	private static  Map<String,MMessage> map = new HashMap<String,MMessage>();
	private boolean isRunning = true;
	private int delayTime = 50;
	public int getDelayTime() {
		return delayTime;
	}

	public void setDelayTime(int delayTime) {
		this.delayTime = delayTime;
	}

	public void setRetryCount(int retryCount) {
		this.retryCount = retryCount;
	}

	public void setTimeOut(int timeOut) {
		this.timeOut = timeOut;
	}

	public void setRetry(RetryListener retry) {
		this.retry = retry;
	}

	private int retryCount = 3;
	private int timeOut = 300;
	
	public Retransmission(){
		// do nothing
	}
	
	public void clearQueue(){
		map.clear();
	}
	
	public void putMessage(String token,Object msg){
		MMessage mmsg = new MMessage(token);
		mmsg.setToken(token);
		mmsg.setTimeStamp(System.currentTimeMillis());
		mmsg.setObj(msg);
		map.put(mmsg.getToken(), mmsg);
	}
	
	public void rmMessage(String token){
		map.remove(token);
	}

	@Override
	public void run(){
		
		while(isRunning){
			try {
				sleep(delayTime);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			
			for(String key : map.keySet()){
				MMessage msg = map.get(key);
				if(msg.getRetryCount() < retryCount 
						&& (System.currentTimeMillis() - msg.getTimeStamp()) > timeOut
						){
					if(retry != null){
						retry.retry(msg.getObj());
						msg.countAdd();
					}
				} else if(msg.getRetryCount() > retryCount){
					rmMessage(msg.getToken());
				}
			}
		}
		return;
	}
	
	private RetryListener retry;
	public interface RetryListener{
		public void retry(Object msg);
	}
	
	public void setRetryListener(RetryListener listener){
		this.retry = listener;
	}
	
	public void stopRetry(){
		isRunning = false;
	}
}