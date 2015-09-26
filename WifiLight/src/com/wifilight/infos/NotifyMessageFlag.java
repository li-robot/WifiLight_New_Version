package com.wifilight.infos;

public interface NotifyMessageFlag {
	
	public final static int UI_REFREASH = 0x11111;
	public final static int GET_WIFI_INFO = 0x22222;
	public final static int SCAN_WIFI_START = 0x33333;
	public final static int FIND_DEVICE = 0x44444;
	public final static int INFRA_STUDY_SUCCESS = 0x55555; 
	public final static int GET_INFRA_CODE = 0x66666;
	public final static int NODE_TEST = 0x77777;
	public final static int LOGIN_IN = 0x88888;
	public final static int SHOW_PROGRESS = 0x99999;
	public final static int WIFI_CONFIG_SUCCESS = 0x00000;
	public final static int LOAD_DEVICE_COMPLETE = 0x10;
	public final static int TIME_OUT = 0x11;
	public final static int SWITCH_NETWORK_TIP = 0x12;
	
	
	/* Timeout */
	public final static int START_WIFI_SCAN_TIMEOUT = 0x13;
	public final static int GET_WIFI_INFO_TIMEOUT = 0x14;
	public final static int CONFIG_WIFI_TIMEOUT = 0x15;
	public final static int SERCHING_WIFI_LAMP = 0x16;
	public final static int WAIT_TO_CONNECT = 0x17;
	public final static int NAME_MODIFYED_SUCCESSED = 0x18;
	public final static int WIFI_SOME_OPERATE = 0x19;
	
}
