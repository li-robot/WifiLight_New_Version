package com.wifilight.listadapter;

import java.util.Vector;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.my_wifilight.R;
import com.wifilight.infos.DeviceInfo;
import com.wifilight.infos.DeviceInfoManager;
import com.wifilight.interaction.WifiLightOperate;
import com.wifilight.protocol.MessageConstants;
import com.wifilight.utils.ByteUtils;

public class DeviceListAdapter extends BaseAdapter{
	
	Context context;
	Vector<DeviceInfo> devices;
	public DeviceListAdapter(Context context, Vector<DeviceInfo> devices){
		this.context = context;
		this.devices = devices;
	}

	@Override
	public int getCount() {
		return devices.size();
	}

	@Override
	public Object getItem(int arg0) {
		return arg0;
	}

	@Override
	public long getItemId(int arg0) {
		return arg0;
	}
	
	@Override
	public View getView(final int arg0, View arg1, ViewGroup arg2) {
		
		final DeviceInfo device = DeviceInfoManager.getDeviceList().get(arg0);
		View view = LayoutInflater.from(context).inflate(R.layout.device_list_item_adapter, null);
		TextView text = (TextView)view.findViewById(R.id.device_name_text);
		final ImageView switchBtn = (ImageView)view.findViewById(R.id.device_switch);
		
		if(devices.get(arg0).getNodes().get(0).channelStatus == 0){
			switchBtn.setBackgroundResource(R.drawable.switch_close);
		} else {
			switchBtn.setBackgroundResource(R.drawable.switch_open);
		}
		
		switchBtn.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(devices.get(arg0).getNodes().get(0).channelStatus == 0){
					WifiLightOperate.wifiLightOperate(device.getMacAddr(), device.getIpStr(), device.getNodes().get(0).channel, (byte)0x01, 
							device.getNodes().get(0).getAdjVal(), device.getNodes().get(0).getAdjVal1());
				} else {
					WifiLightOperate.wifiLightOperate(device.getMacAddr(), device.getIpStr(), device.getNodes().get(0).channel, (byte)0x00, 
							device.getNodes().get(0).getAdjVal(), device.getNodes().get(0).getAdjVal1());
				}
			}
		});
		String macStr = ByteUtils.bytesToHexString(devices.get(arg0).getMacAddr()).toUpperCase();
		System.out.println(macStr);
		text.setText(new String(devices.get(arg0).getNodes().get(0).getNodeName()));
		if(ByteUtils.byteArrayCompare(devices.get(arg0).getNodes().get(0).getNodeName(), MessageConstants.DEFAULT_NAME)){
			text.setText("XX-"+macStr.substring(12, 15)+macStr.substring(16, 19));
		}
		return view;
	}
}
