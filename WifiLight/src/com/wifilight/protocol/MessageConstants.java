package com.wifilight.protocol;

public interface MessageConstants {
	
	/**
	 *  Device find
	 **/
	public static byte[] CMD_FIND_WIFI_LIGHT = {0x00,0x00};
	/**
	 *  Get SSID 
	 **/
	public static byte[] CMD_GET_WIFI_SSID = {0x00,0x01};
	/**
	 * Scan Wifi
	 * */
	public static byte[] CMD_WIFI_SCAN = {0x00,0x04};
	/**
	 * wifi device config 
	 **/
	public static byte[] CMD_WIFI_LIGHT_CONFIG = {0x00,0x02};
	/**
	 * get device info
	 **/
	public static byte[] CMD_GET_DEVICE_INFO = {0x00,0x06};
	/**
	 * modify device info 
	 **/
	public static byte[] CMD_MODIFY_DEVICE_INFO = {0x00,0x07};
	/**
	 * get device status info
	 **/
	public static byte[] CMD_GET_DEVICE_STATUS_INFO = {0x00,0x08}; 
	/**
	 * open device
	 **/
	public static byte[] WIFI_LAMP_OPEN = {0x01,0x10};
	/**
	 * close device
	 **/
	public static byte[] WIFI_LAMP_CLOSE = {0x01,0x11};
	/**
	 * wifi some operate
	 **/
	public static byte[] WIFI_SOME_OPERATE = {0x01,0x40};
	/**
	 * default name
	 */
	public static byte[] DEFAULT_NAME = {
		
		0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,
		0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,
		0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,
		0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00 
		
	};
	/**
	 * auto-report
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
	 * the protocol data length
	 **/
	public static byte ENSURE_LENGTH = 19;
	/**
	 * XXX
	 */
	public static byte DATA_ENSURE_LENGTH = 13;
	/**
	 * Default SessionId
	 **/
	public static byte DEFAULT_SESSION_ID = 0x00;
	
	/**
	 * the flag of wifi-scan
	 */
	public static byte CONFIG_TYPE_WIFI_SCAN = 0;
	
	/**
	 * the flag of device find
	 */
	public static byte CONFIG_TYPE_DEVICE_FIND = 1;
}
