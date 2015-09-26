package com.wifilight.infos;

import java.io.Serializable;

import com.wifilight.utils.ByteUtils;


public class Node implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public byte channel;
	public byte[] nodeAreaName;
	public byte nodeAreaId;
	public byte[] nodeName;
	public byte channelType;
	public boolean isEdit;
	
	public int selectId;
	
	public int getSelectId() {
		return selectId;
	}

	public void setSelectId(int selectId) {
		this.selectId = selectId;
	}

	public boolean isOperate() {
		return isOperate;
	}

	public void setOperate(boolean isOperate) {
		this.isOperate = isOperate;
	}
	public boolean isAvailable;
	public boolean isOperate;
	
	public byte getChannelStatus() {
		return channelStatus;
	}

	public void setChannelStatus(byte channelStatus) {
		this.channelStatus = channelStatus;
	}
	public byte channelStatus;
	
	public Node(byte[] src){
		channel = src[0];
		nodeAreaName = ByteUtils.getSubByteArray(src, 1, 40);
		nodeAreaId = src[41];
		nodeName = ByteUtils.getSubByteArray(src, 42, 40);
		channelType = src[82];
	}
	
	public Node(){
		// do nothing 
	}
	
	// methods 
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
	public boolean isAvailable() {
		return isAvailable;
	}
	public void setAvailable(boolean isAvailable) {
		this.isAvailable = isAvailable;
	} 
	// nodes 
	
	byte adjVal;

	public byte getAdjVal1() {
		return adjVal1;
	}

	public void setAdjVal1(byte adjVal1) {
		this.adjVal1 = adjVal1;
	}

	public byte getAdjVal() {
		return adjVal;
	}

	public void setAdjVal(byte adjVal) {
		this.adjVal = adjVal;
	}
	
	
	byte adjVal1;
	
}
