package com.wifilight.infos;

import java.io.Serializable;
import java.util.Vector;

public class DeviceInfo implements Serializable {
	/**
	 */
	private static final long serialVersionUID = 1L;
	
	private byte[] macAddr;
	private byte deviceType;
	
	private String ipStr;
	
	public String getIpStr() {
		return ipStr;
	}

	public void setIpStr(String ipStr) {
		this.ipStr = ipStr;
	}

	private Vector<Node> nodeVc = new Vector<Node>();
	
	private Vector<Entity> entityVc = new Vector<Entity>();
	
	public boolean isEdit ;
	public DeviceInfo() {
		// do nothing
	}
	
	public DeviceInfo(byte[] macAddr,byte device_type){
		this.setMacAddr(macAddr);
		this.deviceType = device_type;
	}
	
	
	public byte getDeviceType() {
		return deviceType;
	}
	
	public void setDeviceType(byte deviceType) {
		this.deviceType = deviceType;
	}

	public Vector<Node> getNodes() {
		return nodeVc;
	}
	
	public void addNodes(Node node) {
		nodeVc.add(node);
	}

	public byte[] getMacAddr() {
		return macAddr;
	}

	public void setMacAddr(byte[] macAddr) {
		this.macAddr = macAddr;
	}
	
	public  Vector<Entity> getEntitys() {
		entityVc.removeAllElements();
		if(nodeVc.size() > 0){
			
			for(int i=0;i<nodeVc.size();i++) {
				Entity entity = new Entity();
				entity.setChannel(nodeVc.get(i).getChannel());
				entity.setChannelType(nodeVc.get(i).getChannelType());
				entity.setDeviceType(deviceType);
				entity.setMacAddr(macAddr);
				entity.setNodeAreaId(nodeVc.get(i).getNodeAreaId());
				entity.setNodeAreaName(nodeVc.get(i).getNodeAreaName());
				entity.setNodeName(nodeVc.get(i).getNodeName());
				entity.setStatus(nodeVc.get(i).getChannelStatus());
				entity.isEdit = nodeVc.get(i).isEdit;
				entity.selectId = nodeVc.get(i).selectId;
				entity.adjVal = nodeVc.get(i).adjVal;
				entity.adjVal1 = nodeVc.get(i).adjVal1;
				entityVc.add(entity);
			}
		}
		return entityVc;
	}
	
	
	//**=====**//
	// modify count 
	public int count;
	
	public int getCount(){
		return count;
	}
	
	public void countAdd(){
		count ++;
	}
	
	public void countReset(){
		count = 0;
	}
	
}

