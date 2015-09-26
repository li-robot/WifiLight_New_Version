package com.wifilight.interaction;

import java.nio.ByteBuffer;
import java.util.Timer;
import java.util.TimerTask;
import java.util.Vector;

import android.util.Log;

import com.wifilight.infos.DeviceInfo;
import com.wifilight.protocol.DeviceType;
import com.wifilight.protocol.MessageConstants;
import com.wifilight.protocol.RequestMessage;
import com.wifilight.service.ProcessService;
import com.wifilight.utils.ByteUtils;


public class WifiLightOperate {
	
	public final static String TAG = "WifiLightOperate";
	/**
	 * Get device information 
	 * @param index
	 * @param mac
	 * @param ipStr
	 */
	public static void getDeviceInfo(byte index,byte[] mac,String ipStr){

		byte channel = 0x01;
		byte[] cmd = MessageConstants.CMD_GET_DEVICE_INFO;
		byte[] arg = { MessageConstants.DEFAULT_SESSION_ID, index };
		RequestMessage pack = new RequestMessage(mac,channel,cmd,arg);
		ProcessService.sendCmd(pack,ipStr);
	}
	
	/**
	 * Get device status information
	 **/
	public static void getDeviceStatus(Vector<DeviceInfo> deviceList,String ipStr){
		
		byte[] macAddr = null;
		for(int i=0;i<deviceList.size();i++) {
			DeviceInfo device = deviceList.get(i);
			macAddr = device.getMacAddr();
			byte channel = 0x01;
			byte[] cmd = MessageConstants.CMD_GET_DEVICE_STATUS_INFO;
			ByteBuffer buff = ByteBuffer.allocate(9);
			buff.put( MessageConstants.DEFAULT_SESSION_ID );
			buff.put(macAddr);
			byte[] arg = buff.array();
			RequestMessage pack = new RequestMessage(device.getMacAddr(),channel,cmd,arg);
			ProcessService.sendCmd(pack,ipStr);
		}
	}
	
	/**
	 * wifi scan operate
	 * @param mac
	 * @param ipStr
	 */
	public static void startWifiScan(byte[] mac,String ipStr){
		Log.i(TAG, "startWifiScan excute!");
		
		byte[] cmd = MessageConstants.CMD_WIFI_SCAN;
		ByteBuffer buff = ByteBuffer.allocate(2);
		buff.put(MessageConstants.DEFAULT_SESSION_ID);
		/* reserved  */
		buff.put((byte)0x00);
		RequestMessage pack = new RequestMessage(mac,(byte)0x00,cmd,buff.array());
		ProcessService.sendCmd(pack,ipStr);	
	}
	
	/**
	 * get ssid list
	 * @param mac
	 * @param ipStr
	 */
	public static void getWifiInfos(final byte[] mac,final String ipStr){
		
		Timer timer = new Timer();
		TimerTask task = new TimerTask(){
			@Override
			public void run() {
				byte[] cmd = MessageConstants.CMD_GET_WIFI_SSID;
				ByteBuffer buff = ByteBuffer.allocate(2);
				buff.put(MessageConstants.DEFAULT_SESSION_ID);
				/* reserved  */
				buff.put((byte)0x00);
				RequestMessage pack = new RequestMessage(mac,(byte)0x00,cmd,buff.array());
				ProcessService.sendCmd(pack,ipStr);	
			}
		};
		timer.schedule(task,8000);
		
	}
	
	/**
	 * wifi light operate 
	 * @param mac
	 * @param ipStr
	 * @param channel
	 * @param switchVal
	 * @param adjVal
	 * @param adjVal1
	 */
	public static void wifiLightOperate(byte[] mac,String ipStr,byte channel,byte switchVal,byte adjVal,byte adjVal1){
		
		byte[] cmd = MessageConstants.WIFI_SOME_OPERATE;
		ByteBuffer buff = ByteBuffer.allocate(4);
		buff.put(MessageConstants.DEFAULT_SESSION_ID);
		buff.put(switchVal);
		buff.put(adjVal);
		buff.put(adjVal1);
		RequestMessage pack = new RequestMessage(mac,(byte)0x00,cmd,buff.array());
		ProcessService.sendCmd(pack,ipStr);
	}
	
	public static void modifyDeviceName(byte[] mac,String ipStr,String nodeName){
		
		byte[] cmd = MessageConstants.CMD_MODIFY_DEVICE_INFO;
		ByteBuffer buff = ByteBuffer.allocate(93);
		buff.put(MessageConstants.DEFAULT_SESSION_ID);
		buff.put(mac);
		buff.put(DeviceType.WIFI_LAMP);
		/* channel */
		buff.put((byte)0x01);
		buff.put(MessageConstants.DEFAULT_NAME);
		/* reserved */
		buff.put((byte)0x00);
		buff.put(ByteUtils.stringToByteArray(nodeName));
		/* reserved */
		buff.put((byte)0x00);
		RequestMessage pack = new RequestMessage(mac,(byte)0x01,cmd,buff.array());
		ProcessService.sendCmd(pack,ipStr);
	}
	
	public static void configWifiLamp(String ssid,String password){
		
		byte[] cmd = MessageConstants.CMD_WIFI_LIGHT_CONFIG;
		ByteBuffer buff = ByteBuffer.allocate(161);
		buff.put(MessageConstants.DEFAULT_SESSION_ID);
		buff.put(ByteUtils.ssidToByteArray(ssid));
		buff.put(ByteUtils.passwordToByteArray(password));
		RequestMessage pack = new RequestMessage(ProcessService.configMac,(byte)0x00,cmd,buff.array());
		ProcessService.sendCmd(pack,ProcessService.configIp);
	}
}
