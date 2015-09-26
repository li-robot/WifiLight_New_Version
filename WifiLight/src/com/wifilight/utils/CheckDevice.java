package com.wifilight.utils;

import com.wifilight.infos.DeviceInfoManager;
import com.wifilight.log.LogUtils;
import com.wifilight.service.ProcessService;

public class CheckDevice extends Thread{
	
	public final static String LOG_TAG = "CheckDevice";
	private boolean isRun = true;
	@Override
	public void run() {
		
		while(isRun){
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			// condition 1
			if(ProcessService.utils != null && !ProcessService.utils.isWifiConnect() ){
				ProcessService.broadcast();
			}
			// condition 2
			if(ProcessService.utils != null 
					&& ProcessService.utils.isWifiConnect() 
					&& !ProcessService.utils.getConnectedSsid().trim().equals(ProcessService.configSsid)){
				//LogUtils.logToConsole(LOG_TAG, " Current SSid " + ProcessService.utils.getConnectedSsid(),LogUtils.LOG_LEVEL_INFO);
				ProcessService.broadcast();
			}
			
			if(DeviceInfoManager.getDeviceList().size() > 0 && listener != null && ProcessService.utils.isWifiConnect())
				listener.deviceFind();
			LogUtils.logToConsole(LOG_TAG, " CheckDevice Run ",LogUtils.LOG_LEVEL_INFO);
			LogUtils.logToConsole(LOG_TAG, " Device Size : " + DeviceInfoManager.getDeviceList().size(),LogUtils.LOG_LEVEL_INFO);
			
		}
		
		return ;
	}
	
	public DeviceFindListener listener;
	public interface DeviceFindListener{
		public void deviceFind();
	}
	
	public void setDeviceFindListener(DeviceFindListener listener){
		this.listener = listener;
	}
	
	public void _stop(){
		isRun = false;
	}
	
	public void _start(){
		if(!isAlive()){
			start();
		}
	}
}
