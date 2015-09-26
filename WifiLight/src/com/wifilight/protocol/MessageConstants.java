package com.wifilight.protocol;

public interface MessageConstants {
	
	/**
	 *  发现台灯  
	 **/
	public static byte[] CMD_FIND_WIFI_LIGHT = {0x00,0x00};
	/**
	 *  获取SSID 
	 **/
	public static byte[] CMD_GET_WIFI_SSID = {0x00,0x01};
	/**
	 * 启动Wifi扫描
	 * */
	public static byte[] CMD_WIFI_SCAN = {0x00,0x04};
	/**
	 * wifi台灯配置
	 **/
	public static byte[] CMD_WIFI_LIGHT_CONFIG = {0x00,0x02};
	/**
	 * 获取设备信息
	 **/
	public static byte[] CMD_GET_DEVICE_INFO = {0x00,0x06};
	/**
	 * 修改设备信息
	 **/
	public static byte[] CMD_MODIFY_DEVICE_INFO = {0x00,0x07};
	/**
	 * 获取设备状态信息
	 **/
	public static byte[] CMD_GET_DEVICE_STATUS_INFO = {0x00,0x08}; 
	/**
	 * wifi台灯开操作
	 **/
	public static byte[] WIFI_LAMP_OPEN = {0x01,0x10};
	/**
	 * wifi台灯关操作
	 **/
	public static byte[] WIFI_LAMP_CLOSE = {0x01,0x11};
	/**
	 * wifi some operate
	 **/
	public static byte[] WIFI_SOME_OPERATE = {0x01,0x40};
	/**
	 * 默认名称
	 */
	public static byte[] DEFAULT_NAME = {
		
		0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,
		0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,
		0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,
		0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00 
		
	};
	/**
	 * 主动上报
	 **/
	public static byte[] AUTO_REPORT = {(byte)0xFF,0x21};
	/**
	 * default QID
	 **/
	public static byte DEFAULT_QID = 0x00;
	/**
	 * request DIR
	 **/
	public static byte REQUEST_DIR = 0x00;
	/**
	 * response DIR
	 */
	public static byte RESPONSE_DIR = 0x01;
	/**
	 * 协议报文能够确定的长度
	 **/
	public static byte ENSURE_LENGTH = 19;
	/**
	 * 协议报文data部分的确定长度
	 */
	public static byte DATA_ENSURE_LENGTH = 13;
	/**
	 * Default SessionId
	 **/
	public static byte DEFAULT_SESSION_ID = 0x00;
	
	/* 启动扫描 Flag */
	public static byte CONFIG_TYPE_WIFI_SCAN = 0;
	
	/* 设备发现Flag */
	public static byte CONFIG_TYPE_DEVICE_FIND = 1;
}
