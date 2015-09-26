package com.wifilight.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;

public class WifiInfoUtils {
	
	Context context;
	public WifiInfoUtils(Context context){
		this.context = context;
	}
	
	/* ���Wifi����  */
	public boolean isWifiConnect() { 
		ConnectivityManager connManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);  
		NetworkInfo mWifi = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
		return mWifi.isConnected();  
	} 
	
	/* ��ȡSSID��Ϣ  */
	public String getConnectedSsid(){
		WifiManager wm = (WifiManager)context.getSystemService(Context.WIFI_SERVICE);
		android.net.wifi.WifiInfo wifiInfo = wm.getConnectionInfo();
		return wifiInfo.getSSID();
	}

}
