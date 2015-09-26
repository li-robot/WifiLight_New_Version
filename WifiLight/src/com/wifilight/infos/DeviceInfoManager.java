package com.wifilight.infos;

import java.util.Vector;

import com.wifilight.protocol.DeviceType;
import com.wifilight.utils.ByteUtils;

public class DeviceInfoManager {
	private static Vector<DeviceInfo> deviceInfos = new Vector<DeviceInfo>();
	public static void parseDevice(byte[] mac,String ipStr, byte[] args){
		byte deviceType = args[11];
		DeviceInfo device = new DeviceInfo(mac,deviceType);
		device.setIpStr(ipStr);
		/* 一路节点    */
		if( deviceType == DeviceType.WIFI_LAMP || deviceType == DeviceType._1_RELAY) {
			Node node = new Node(ByteUtils.getSubByteArray(args, 12, 83));
			device.addNodes(node);
			addDevice(device);
		}
		/* 双路节点 */
		if(deviceType == DeviceType._2_RELAY){
			Node node = new Node(ByteUtils.getSubByteArray(args, 12, 83));
			Node node1 = new Node(ByteUtils.getSubByteArray(args, 95, 83));
			device.addNodes(node);
			device.addNodes(node1);
			addDevice(device);
		}
		/* 三路节点 */
		if(deviceType == DeviceType._3_RELAY){
			Node node = new Node(ByteUtils.getSubByteArray(args, 12, 83));
			Node node1 = new Node(ByteUtils.getSubByteArray(args, 95, 83));
			Node node2 = new Node(ByteUtils.getSubByteArray(args, 178, 83));
			device.addNodes(node);
			device.addNodes(node1);
			device.addNodes(node2);
			addDevice(device);
		}
		/* 四路节点 */
		if(deviceType == DeviceType._4_RELAY){
			Node node = new Node(ByteUtils.getSubByteArray(args, 12, 83));
			Node node1 = new Node(ByteUtils.getSubByteArray(args, 95, 83));
			Node node2 = new Node(ByteUtils.getSubByteArray(args, 178, 83));
			Node node3 = new Node(ByteUtils.getSubByteArray(args, 261, 83));
			device.addNodes(node);
			device.addNodes(node1);
			device.addNodes(node2);
			device.addNodes(node3);
			
			addDevice(device);
		}
		
	}
	
	public static void addDevice(DeviceInfo device){
		boolean addFlag = true;
		for(int i=0;i<deviceInfos.size();i++){
			updateDevicesIp(device.getMacAddr(),device.getIpStr());
			if(ByteUtils.byteArrayCompare(device.getMacAddr(),deviceInfos.get(i).getMacAddr())){
				addFlag = false;
				deviceInfos.get(i).countReset();
			}
		}
		if(addFlag)
			deviceInfos.add(device);
	}
	public static void updateDevicesIp(byte[] mac,String ipStr){
		DeviceInfo device = getDeviceByMac(deviceInfos,mac);
		if(device != null){
			device.setIpStr(ipStr);
		}
	}
	
	public static DeviceInfo getDeviceByMac(Vector<DeviceInfo> devices ,byte[] mac){
		
		for(DeviceInfo tmp : devices){
			if(ByteUtils.byteArrayCompare(tmp.getMacAddr(),mac))
				return tmp;
		}
		return null;
	}
	
	public static Node getNodeByChannel(DeviceInfo device , byte channel){
		
		if(device != null){
			for(int i=0;i<device.getNodes().size();i++){
				Node node = device.getNodes().get(i);
				if(node.getChannel() == channel)
					return node;
			}
		}
		return null;
	}
	
	public static Vector<DeviceInfo> getDeviceList(){
		return deviceInfos;
	}
	public static void clear(){
		deviceInfos.removeAllElements();
	}
	
}
