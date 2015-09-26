package com.wifilight.service;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.util.Log;

import com.wifilight.infos.DeviceInfo;
import com.wifilight.infos.DeviceInfoManager;
import com.wifilight.infos.Node;
import com.wifilight.infos.NotifyMessageFlag;
import com.wifilight.infos.WifiInfoManager;
import com.wifilight.interaction.WifiLightOperate;
import com.wifilight.log.LogUtils;
import com.wifilight.protocol.DeviceType;
import com.wifilight.protocol.ErrorCode;
import com.wifilight.protocol.MessageConstants;
import com.wifilight.protocol.RequestMessage;
import com.wifilight.protocol.ResponseMessage;
import com.wifilight.protocol.args.DeviceFindArg;
import com.wifilight.utils.ByteUtils;
import com.wifilight.utils.TaskExcute;
import com.wifilight.utils.TimeoutHandler;
import com.wifilight.utils.TimeoutHandler.OnTimeoutListener;
import com.wifilight.utils.TimerHandlerGet;
import com.wifilight.utils.Utils;
import com.wifilight.utils.WifiInfoUtils;

public class ProcessService extends Service implements Runnable{
	
	public final static String LOG_TAG = "ProcessService";
	
	private final static int STATE_FIND_DEVICE = 0x00;
	private final static int STATE_WIFI_SCAN = 0x01;
	private final static int STATE_WIFI_LIGHT_CONFIG = 0x02;
	private final static int STATE_DEVICE_OPERATE = 0x03;
	private final static int STATE_START = 0x04;
	
	private int mState = STATE_START;
	
	byte deviceCount = 0;
	public final static int WIFI_LIGHT_NET_PORT = 55555;
	public final static int PHONE_NET_PORT = 55556;
	
	public static DatagramSocket socket;
	private boolean isReceiving ;
	
	private Context sContext;
	
	/* identification the session */
	private static byte sequenceId = 0;
	
	/* judge activity is entered */
	public static boolean wifiInfoActivtyEntered = false;
	public static boolean searchActivityEntered = false;
	public static boolean operateActivityEntered = false;
	
	public static byte[] configMac;
	public static String configIp;
	public static String configSsid;
	
	static TimeoutHandler broadcastTimeout;
	public static WifiInfoUtils utils;
	@Override
	public void onCreate() {
		super.onCreate();
		sContext = this;
		Utils.canNetWorkOperateInMainThread();
		try {
			socket = new DatagramSocket(PHONE_NET_PORT);
		} catch (SocketException e) {
			e.printStackTrace();
		}
		utils = new WifiInfoUtils(this);
	}
	
	@Override
	public int onStartCommand(Intent intent, int flags, int startId){
		isReceiving = true;
		/* start the receive thread */
		new Thread(this).start();
		/* process */
		broadcast();
		return super.onStartCommand(intent, flags, startId);
	}

	@Override
	public IBinder onBind(Intent arg0) {
		return null;
	}
	
	/**
	 *  UI Refresh 
	 */
	private static Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch(msg.what){
				case NotifyMessageFlag.UI_REFREASH:
					if(uiRefreshListener != null){
						uiRefreshListener.onRefresh(msg.what);
					}
					break;
				case NotifyMessageFlag.NAME_MODIFYED_SUCCESSED:
					if(onNameModifyedListener != null){
						onNameModifyedListener.onNameModifyed();
					}
					break;
				case NotifyMessageFlag.GET_WIFI_INFO:
					if(wifiInfoListener != null){
						wifiInfoListener.onGet();
					}
					break;
				case NotifyMessageFlag.WIFI_SOME_OPERATE:
					if(onUIUpdateListener != null){
						onUIUpdateListener.onUpdate();
					}
					break;
				case NotifyMessageFlag.WIFI_CONFIG_SUCCESS:
					LogUtils.logToConsole(LOG_TAG, "WIFI_CONFIG_SUCCESS", LogUtils.LOG_LEVEL_INFO);
					if(onWifiConfigListener != null){
						onWifiConfigListener.configSuccess();
					}
					break;
				case NotifyMessageFlag.FIND_DEVICE:
					/* Cancel timeout handler */
					if(broadcastTimeout != null){
						broadcastTimeout.cancel();
					}
					break;
			}
		}
	};
	
	private static OnWifiConfigListener onWifiConfigListener;
	public interface OnWifiConfigListener {
		public void configSuccess();
	}
	public static void setWifiConfigListener(OnWifiConfigListener arg){
		onWifiConfigListener = arg ;
	}
	
	/* when ui update call this */
	private static OnUIUpdateListener onUIUpdateListener;
	public interface OnUIUpdateListener{
		public void onUpdate();
	}
	public static void setOnUIUpdateListener(OnUIUpdateListener arg){
		onUIUpdateListener = arg ;
	}
	
	private static OnWifiInfoGetListener wifiInfoListener;
	public interface OnWifiInfoGetListener{
		public void onGet();
	}
	
	public static void setOnWifiInfoGetListener(OnWifiInfoGetListener arg){
		wifiInfoListener = arg ;
	}
	
	/* when ui refreshed use it */
	private static OnUIRefreshListener uiRefreshListener;
	public interface OnUIRefreshListener{
		public void onRefresh(int what);
	}
	
	public static void setOnUIRefreshListener(OnUIRefreshListener arg){
		uiRefreshListener = arg ;
	}
	
	/* when device name modified use this */
	private static OnNameModifyedListener onNameModifyedListener;
	public interface OnNameModifyedListener{
		public void onNameModifyed();
	}
	
	public static void setOnNameModifyedListener(OnNameModifyedListener arg){
		onNameModifyedListener = arg;
	}
	
	@Override
	public void run() {
		while(isReceiving){
			byte[] array = new byte[1024];
			DatagramPacket pack = new DatagramPacket(array,array.length);
			if(socket != null) {
				try {
					socket.receive(pack);
					if(pack.getPort() == 55555){
						
					}
					byte[] dataBuff = pack.getData();
					if(dataBuff != null){
						feedBackProcess(pack);
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
			} else {
				System.out.println("socket null");
				return ;
			}
		}
	}
	
	public void feedBackProcess(DatagramPacket pack){
		
		byte[] data = pack.getData();
		ResponseMessage parseData = new ResponseMessage(data);
		/* Get the feedback */
		byte[] cmd = parseData.getCmd();
		byte[] arg = parseData.getArgs();
		String ipStr = pack.getAddress().getHostAddress();
		int port = pack.getPort();
		
		/* Find device feedback  */
		if(ByteUtils.byteArrayCompare(cmd, MessageConstants.CMD_FIND_WIFI_LIGHT)){
			if(arg != null){
				if(arg[0] == ErrorCode.SUCCESS){
					DeviceFindArg dfArg = new DeviceFindArg(arg);
					
					if(dfArg.configType == MessageConstants.CONFIG_TYPE_WIFI_SCAN){
						configMac = dfArg.mac;
						configIp = pack.getAddress().getHostAddress();
						configSsid = utils.getConnectedSsid().trim();
						Log.i("Config ssid ", configSsid);
						/* Start wifi scan */
						WifiLightOperate.startWifiScan(configMac, configIp);
						Log.i(LOG_TAG, "start wifi scan");
						
						/* 开始WIFI扫描  */
						mState = STATE_WIFI_SCAN;
						//process(configMac,configIp);
						
					} else if(dfArg.configType == MessageConstants.CONFIG_TYPE_DEVICE_FIND){
						/* Start device find */
						Log.i(LOG_TAG, "start device discover");
						WifiLightOperate.getDeviceInfo((byte)0x00, dfArg.mac, ipStr);
						
						Log.i(LOG_TAG, "Device Mac : " + ByteUtils.bytesToHexString(dfArg.mac));
						Log.i(LOG_TAG, "Device IP : " + ipStr);
						
						/* 设备发现状态  */
						mState = STATE_FIND_DEVICE;
						//process(configMac,configIp);
					}
					Message msg = new Message();
					msg.what = NotifyMessageFlag.FIND_DEVICE;
					handler.sendMessage(msg);
				}
			}
		}
		
		/* 获取Wifi请求列表 */
		if(ByteUtils.byteArrayCompare(cmd, MessageConstants.CMD_GET_WIFI_SSID)) {
			if(arg != null){
				if( arg[1] == ErrorCode.SUCCESS ){
					WifiInfoManager.addWifiInfos(arg);
					/* wifi 扫描状态  */
					mState = STATE_WIFI_SCAN;
					//process(configMac,configIp);
				}
			}
		}
		
		if(ByteUtils.byteArrayCompare(cmd, MessageConstants.CMD_GET_DEVICE_INFO)) {
			if(arg != null){
				if(arg[1] == ErrorCode.SUCCESS) {
					Log.i(LOG_TAG, "start get device information");
					DeviceInfoManager.parseDevice(parseData.getMac(), ipStr, arg);
					if(arg[2] != 0x01){
						WifiLightOperate.getDeviceInfo(deviceCount,parseData.getMac(),pack.getAddress().getHostAddress());
						deviceCount ++;
					} else {
						Log.i(LOG_TAG, "start get device status");
						WifiLightOperate.getDeviceStatus(DeviceInfoManager.getDeviceList(),ipStr);
					}
				}
				Log.i(LOG_TAG, "Device count : " + DeviceInfoManager.getDeviceList().size());
				
			}
		}
		
		if(ByteUtils.byteArrayCompare(cmd, MessageConstants.CMD_GET_DEVICE_STATUS_INFO)){
			if(arg != null){
				if(arg[1] == ErrorCode.SUCCESS){
					byte[] mac = ByteUtils.getSubByteArray(arg, 2, 8);
					DeviceInfo tmpDevice = DeviceInfoManager.getDeviceByMac(DeviceInfoManager.getDeviceList(),mac);
					if(tmpDevice != null){
						if( tmpDevice.getDeviceType() == DeviceType.WIFI_LAMP) {
							byte channel = arg[11];
							byte status = arg[12];
							Node tmpNode = DeviceInfoManager.getNodeByChannel(tmpDevice,channel);
							tmpNode.setChannelStatus(status);
							tmpNode.setAdjVal(arg[13]);
							tmpNode.setAdjVal1(arg[14]);
							Log.i(LOG_TAG, "Status Info : "+"Switch "+ status +" adjval "+ arg[13] + " adjval1 " + arg[14]);
						}
					}
				}
			}
		}
			
		/* start wifi scan*/
		if(ByteUtils.byteArrayCompare(cmd, MessageConstants.CMD_WIFI_SCAN)){
			if(arg != null){
				if(arg[1] == ErrorCode.SUCCESS){
					Log.i(LOG_TAG, "CMD_WIFI_SCAN");
					/* Get wifi ssid list */
					WifiLightOperate.getWifiInfos(parseData.getMac(),pack.getAddress().getHostAddress());
					Message msg = new Message();
					msg.what = NotifyMessageFlag.GET_WIFI_INFO;
					handler.sendMessage(msg);
				}
			}
		}
	
		if(ByteUtils.byteArrayCompare(cmd,MessageConstants.CMD_WIFI_LIGHT_CONFIG)){ 
			Message msg = new Message();
			msg.what = NotifyMessageFlag.WIFI_CONFIG_SUCCESS;
			handler.sendMessage(msg);
		}
		
		if(ByteUtils.byteArrayCompare(cmd,MessageConstants.WIFI_SOME_OPERATE)){
			byte[] mac = ByteUtils.getSubByteArray(arg, 2, 8);
			DeviceInfo device = DeviceInfoManager.getDeviceByMac(DeviceInfoManager.getDeviceList(),mac);
			if(device != null){
				Node node = device.getNodes().get(0);
				if(node != null){
					node.setChannelStatus(arg[12]);
					node.setAdjVal(arg[13]);
					node.setAdjVal1(arg[14]);
				}
			}
			
			Message msg = new Message();
			msg.what = NotifyMessageFlag.WIFI_SOME_OPERATE;
			handler.sendMessage(msg);
			
			if( port == WIFI_LIGHT_NET_PORT ){
				RequestMessage d = new RequestMessage(parseData.getMac(),parseData.getChannel(),parseData.getCmd(),parseData.getArgs());
				broadToOther(d.toByteArray());
				Log.i(LOG_TAG, "Broad To Other !");
			}
			
		}
		
		if(ByteUtils.byteArrayCompare(cmd, MessageConstants.CMD_MODIFY_DEVICE_INFO)){
			
		}
		
		if(ByteUtils.byteArrayCompare(cmd, MessageConstants.AUTO_REPORT)){
			if(arg != null){
				DeviceInfo tmpDevice = DeviceInfoManager.getDeviceByMac(DeviceInfoManager.getDeviceList(),parseData.getMac());
				if(tmpDevice != null){
					if(tmpDevice.getDeviceType() == DeviceType.WIFI_LAMP){
						byte channel = arg[10];
						byte status = arg[11];
						Node tmpNode = DeviceInfoManager.getNodeByChannel(tmpDevice,channel);
						tmpNode.setChannelStatus(status);
						tmpNode.setAdjVal(arg[12]);
						tmpNode.setAdjVal1(arg[13]);
					}
				}
			
				if( port == WIFI_LIGHT_NET_PORT ){
					RequestMessage d = new RequestMessage(parseData.getMac(),parseData.getChannel(),parseData.getCmd(),parseData.getArgs());
					broadToOther(d.toByteArray());
					Log.i(LOG_TAG, "Broad To Other !");
				}
				Message msg = new Message();
				msg.what = NotifyMessageFlag.WIFI_SOME_OPERATE;
				handler.sendMessage(msg);
			}
		}
	}
	
	public void broadToOther(byte[] dataBuf){
		DatagramPacket packet;
		try {
			packet = new DatagramPacket(dataBuf,dataBuf.length,InetAddress.getByName("255.255.255.255"),55556);
			socket.send(packet);
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e){
			e.printStackTrace();
		}
	}
	
	public static void sendCmd(RequestMessage message,String ipStr){
		byte[] dataBuf = message.toByteArray();
		message.qid = sequenceId;
		try {
			DatagramPacket dataPack = new DatagramPacket(dataBuf,dataBuf.length,InetAddress.getByName(ipStr),55555);
			if( socket != null){
				socket.send(dataPack);
			}
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e){
			e.printStackTrace();
		}
		sequenceId ++;
	}
	
	public static void broadcast(){
		byte[] find_mac = { 0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00 };
		byte find_channel = 0x01;
		byte[] args = {0x01,0x00,0x00,0x00};
		RequestMessage cmdData = new RequestMessage(find_mac,find_channel,MessageConstants.CMD_FIND_WIFI_LIGHT,args);
		byte[] bufData = cmdData.toByteArray();
		try {
			DatagramPacket packet = new DatagramPacket(bufData,bufData.length,InetAddress.getByName("255.255.255.255"),55555);
			if(socket != null){
				socket.send(packet);
			}
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		/* Set the timeout handler */
		broadcastTimeout = TimerHandlerGet.getInstance(10000, Looper.getMainLooper());
		broadcastTimeout.setOnTimeoutListener(new OnTimeoutListener(){
			@Override
			public void onTimeout() {
				broadcast();
			}
		});
	}
	
	public void release() {
		isReceiving = false;
		DeviceInfoManager.getDeviceList().removeAllElements();
		if(socket != null){
			socket.close();
		}
	}
	
	@Override
	public void onDestroy() {
		release();
		super.onDestroy();
		Log.i(LOG_TAG, " ProcessService onDestroy ");
	}
	
	@SuppressWarnings("rawtypes")
	public void enterPage(Context context,Class cls){
		Intent intent = new Intent();
		intent.setClass(context,cls);		
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		context.startActivity(intent);
		try {
			Thread.sleep(100);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	/* StateMachine */
	public void process(final byte[] mac,final String ipStr){
		
		switch(mState){
			case STATE_FIND_DEVICE:
				TaskExcute.setWorkTask(new TaskExcute.WorkTask(){
					@Override
					public void doWork() {
						// do find device 
						broadcast();
					}
				});
				TaskExcute.excuteRepeat(3000, 3000);
				break;
			case STATE_WIFI_SCAN:
				TaskExcute.setWorkTask(new TaskExcute.WorkTask(){
					@Override
					public void doWork() {
						// do wifi scan
						WifiLightOperate.startWifiScan(mac, ipStr);
					}
				});
				TaskExcute.excuteRepeat(3000, 10000);
				break;
			case STATE_WIFI_LIGHT_CONFIG:
				TaskExcute.setWorkTask(new TaskExcute.WorkTask(){
					@Override
					public void doWork() {
						// do wifi light config 
						// WifiLightOperate.configWifiLamp(ssid, password)
					}
				});
				TaskExcute.excuteRepeat(3000, 3000);
				break;
			case STATE_DEVICE_OPERATE:
				TaskExcute.setWorkTask(new TaskExcute.WorkTask(){
					@Override
					public void doWork() {
						// do device operate 
						broadcast();
						// WifiLightOperate.getDeviceInfo(index, mac, ipStr) 
					}
				});
				TaskExcute.excuteRepeat(3000, 3000);
				break;
			case STATE_START:
				TaskExcute.setWorkTask(new TaskExcute.WorkTask(){
					@Override
					public void doWork() {
						// do start broadcast 
					}
				});
				TaskExcute.excuteRepeat(3000, 3000);
				break;
		}
	}
}
