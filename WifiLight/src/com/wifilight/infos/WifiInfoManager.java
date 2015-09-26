package com.wifilight.infos;

import java.util.Collections;
import java.util.Vector;
import com.wifilight.utils.ByteUtils;

public class WifiInfoManager {
	private static Vector<WifiInfo> wifiInfos = new Vector<WifiInfo>();
	@SuppressWarnings("unchecked")
	public static void addWifiInfos(byte[] args){
		int count = 3;
		for(int i=0; i<args[2]; i++) {
			WifiInfo winfo = new WifiInfo();
			String tmpSsid = new String(ByteUtils.getSubByteArray(args, count, 32)).trim();
			winfo.setSsid(tmpSsid);
			winfo.setSignal_strength( args[count + 32 ]);
			count += 33;
			if(!checkSsidExist(tmpSsid))
				wifiInfos.add(winfo);
		}
		Collections.sort(wifiInfos);
	}
	
	public static boolean checkSsidExist(String ssid){
		for(WifiInfo info : wifiInfos){
			if(info.getSsid().equals(ssid.trim()))
				return true;
		}
		return false;
	}
	
	public static Vector<WifiInfo> getWifiInfoList(){
		return wifiInfos;
	}
	
	public static void clear(){
		wifiInfos.removeAllElements();
	}
}
