package com.wifilight.infos;

import java.io.Serializable;

public class Entity implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private byte[] macAddr;
	private byte deviceType;
	public byte channel;
	
	public byte getStatus() {
		return status;
	}
	public void setStatus(byte status) {
		this.status = status;
	}
	public byte[] nodeAreaName;
	public byte nodeAreaId;
	public byte[] nodeName;
	public byte channelType;
	public byte status;
	
	public boolean isEdit;
	int selectId;
	
	public int getSelectId() {
		return selectId;
	}
	public void setSelectId(int selectId) {
		this.selectId = selectId;
	}
	// getter and setter 
	public byte[] getMacAddr() {
		return macAddr;
	}
	public void setMacAddr(byte[] macAddr) {
		this.macAddr = macAddr;
	}
	public byte getDeviceType() {
		return deviceType;
	}
	public void setDeviceType(byte deviceType) {
		this.deviceType = deviceType;
	}
	public byte getChannel() {
		return channel;
	}
	public void setChannel(byte channel) {
		this.channel = channel;
	}
	public byte[] getNodeAreaName() {
		return nodeAreaName;
	}
	public void setNodeAreaName(byte[] nodeAreaName) {
		this.nodeAreaName = nodeAreaName;
	}
	public byte getNodeAreaId() {
		return nodeAreaId;
	}
	public void setNodeAreaId(byte nodeAreaId) {
		this.nodeAreaId = nodeAreaId;
	}
	public byte[] getNodeName() {
		return nodeName;
	}
	public void setNodeName(byte[] nodeName) {
		this.nodeName = nodeName;
	}
	public byte getChannelType() {
		return channelType;
	}
	public void setChannelType(byte channelType) {
		this.channelType = channelType;
	}
	
	byte adjVal;

	public byte getAdjVal() {
		return adjVal;
	}
	public void setAdjVal(byte adjVal) {
		this.adjVal = adjVal;
	}
	
	byte adjVal1;

	public byte getAdjVal1() {
		return adjVal1;
	}
	public void setAdjVal1(byte adjVal1) {
		this.adjVal1 = adjVal1;
	}
	
	
}
