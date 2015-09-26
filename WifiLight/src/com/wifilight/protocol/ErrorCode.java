package com.wifilight.protocol;

public interface ErrorCode {
	
	public final static byte SUCCESS = 0x00;
	public final static byte UNSUPPORT = 0x01;
	public final static byte HARDWARE_NOT_EXIST = 0x02;
	public final static byte DATA_FIELD_FORMAT_ERROR = 0x03;
	public final static byte CRC_ERROR = 0x04;
	public final static byte UNKNOW_ERROR = 0x10;

}
