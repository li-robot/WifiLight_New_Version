package com.wifilight.protocol;

public interface DeviceType {

	/* Wifi 台灯类型 */
	public final static byte WIFI_LAMP = 0x21;

	public final static byte _1_RELAY = 0x01;
	
	public final static byte _2_RELAY = 0x02;
	
	public final static byte _3_RELAY = 0x03;
	
	public final static byte _4_RELAY = 0x04;
	
	public final static byte _1_MOTOR = 0x05;
	
	public final static byte _2_MOTOR = 0x06;
	
	/**
		0x01	1路开关面板
		0x02	2路开关面板
		0x03	3路开关面板
		0x04	4路开关面板
		0x05	1路电机
		0x06	2路电机
		0x07	智能插座A
		0x08	智能插座B
		0x09	4路红外转发
		0x0A	1路情景面板
		0x0B	2路情景面板
		0x0C	3路情景面板
		0x0D	4路情景面板
		0x0E	人体红外传感器
		0x0F	温湿度传感器
		0x10	光照强度
		0x11	1路调光面板
		0x12	2路调光面板
		0x13	1路调光模块
		0x14	1路串口232
		0x15	2路串口232
		0x16	1路联动开关面板
		0x17	2路联动开关面板
		0x18	3路联动开关面板
		0x19	1路联动电机面板
		0x1A	2路联动电机面板
		0x1D	1路调光调色功能
		0x1E	2路调光调色功能
		0x1F	1路调色功能
		0x20	2路调色功能
		0x21	1路双控操作
	 */
	
}
