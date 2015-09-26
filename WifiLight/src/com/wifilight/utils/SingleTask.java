package com.wifilight.utils;

import java.util.TimerTask;

public class SingleTask extends TimerTask{
	
	private static SingleTask task = new SingleTask();
	private SingleTask(){
		// do nothing         
	}
	
	public static SingleTask getInstance(){
		if(task == null){
			synchronized(SingleTask.class){
				task = new SingleTask();
			}
		}
		return task;
	}

	@Override
	public void run() {
		if(workTask != null){
			workTask.doWork();
		}
	}
	
	public static WorkTask workTask;
	public interface WorkTask {
		public void doWork();
	}
	
	public void setWorkTask(WorkTask task){
		workTask = task;
	}
	
}
