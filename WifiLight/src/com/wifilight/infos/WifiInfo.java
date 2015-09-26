package com.wifilight.infos;

import java.io.Serializable;

@SuppressWarnings("rawtypes")
public class WifiInfo implements Serializable,Comparable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public String ssid;
	public byte signal_strength;
	
	
	public String getSsid() {
		return ssid;
	}
	public void setSsid(String ssid) {
		this.ssid = ssid;
	}
	public byte getSignal_strength() {
		return signal_strength;
	}
	public void setSignal_strength(byte signal_strength) {
		this.signal_strength = signal_strength;
	}
	@Override
	public int compareTo(Object arg0) {
		WifiInfo tmp = (WifiInfo)arg0;
		return (tmp.signal_strength - this.signal_strength);
	}
	
}
