package com.wifilight.protocol;

public interface MessageConstants {
	
	/**
	 *  ����̨��  
	 **/
	public static byte[] CMD_FIND_WIFI_LIGHT = {0x00,0x00};
	/**
	 *  ��ȡSSID 
	 **/
	public static byte[] CMD_GET_WIFI_SSID = {0x00,0x01};
	/**
	 * ����Wifiɨ��
	 * */
	public static byte[] CMD_WIFI_SCAN = {0x00,0x04};
	/**
	 * wifį������
	 **/
	public static byte[] CMD_WIFI_LIGHT_CONFIG = {0x00,0x02};
	/**
	 * ��ȡ�豸��Ϣ
	 **/
	public static byte[] CMD_GET_DEVICE_INFO = {0x00,0x06};
	/**
	 * �޸��豸��Ϣ
	 **/
	public static byte[] CMD_MODIFY_DEVICE_INFO = {0x00,0x07};
	/**
	 * ��ȡ�豸״̬��Ϣ
	 **/
	public static byte[] CMD_GET_DEVICE_STATUS_INFO = {0x00,0x08}; 
	/**
	 * wifį�ƿ�����
	 **/
	public static byte[] WIFI_LAMP_OPEN = {0x01,0x10};
	/**
	 * wifį�ƹز���
	 **/
	public static byte[] WIFI_LAMP_CLOSE = {0x01,0x11};
	/**
	 * wifi some operate
	 **/
	public static byte[] WIFI_SOME_OPERATE = {0x01,0x40};
	/**
	 * Ĭ������
	 */
	public static byte[] DEFAULT_NAME = {
		
		0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,
		0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,
		0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,
		0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00 
		
	};
	/**
	 * �����ϱ�
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
	 * Э�鱨���ܹ�ȷ���ĳ���
	 **/
	public static byte ENSURE_LENGTH = 19;
	/**
	 * Э�鱨��data���ֵ�ȷ������
	 */
	public static byte DATA_ENSURE_LENGTH = 13;
	/**
	 * Default SessionId
	 **/
	public static byte DEFAULT_SESSION_ID = 0x00;
	
	/* ����ɨ�� Flag */
	public static byte CONFIG_TYPE_WIFI_SCAN = 0;
	
	/* �豸����Flag */
	public static byte CONFIG_TYPE_DEVICE_FIND = 1;
}
