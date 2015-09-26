package com.wifilight.protocol.args;

import com.wifilight.utils.ByteUtils;

public class DeviceFindArg {
	
	public byte[] mac;
	public byte deviceType;
	public byte configType;
	
	public DeviceFindArg(byte[] args){
		this.mac = ByteUtils.getSubByteArray(args, 1, 8);
		this.deviceType = args[13];
		this.configType = args[14];
	}
}
