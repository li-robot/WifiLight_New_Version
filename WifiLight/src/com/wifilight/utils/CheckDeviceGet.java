package com.wifilight.utils;

public class CheckDeviceGet {
	private static CheckDevice device = new CheckDevice();
	private CheckDeviceGet(){
	}
	
	public static CheckDevice getInstance(){
		if(device == null)
			device = new CheckDevice();
		return device;
	}
}
