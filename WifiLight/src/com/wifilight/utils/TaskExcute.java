package com.wifilight.utils;

import java.util.Timer;

public class TaskExcute {
	
	/* excute once */
	public static void excute(long delayTime){
		Timer timer = SingleTimer.getTimer();
		SingleTask task = SingleTask.getInstance();
		task.setWorkTask(new SingleTask.WorkTask(){
			@Override
			public void doWork() {
				if(workTask != null)
					workTask.doWork();
			}
			
		});
		try{
			timer.schedule(task, delayTime);
		}catch(Exception e){
			System.out.println("Timer is Schedule");
		}
	}
	
	/* excute repeat */
	public static void excuteRepeat(long delayTime,long period){
		Timer timer = SingleTimer.getTimer();
		SingleTask task = SingleTask.getInstance();
		task.setWorkTask(new SingleTask.WorkTask(){
			@Override
			public void doWork() {
				if(workTask != null)
					workTask.doWork();
			}
			
		});
		try{
			timer.schedule(task, delayTime,period);
		}catch(Exception e){
			System.out.println("Timer is Schedule");
		}
	}
	
	public static WorkTask workTask;
	public interface WorkTask {
		public void doWork(); 
	}
	
	public static void setWorkTask(WorkTask task){
		workTask = task;
	}
	
	public static void cancelTask(){
		try{
			SingleTimer.cancel();
		}catch(Exception e){
			System.out.println("Timer is Schedule");
		}
	}
}
