package com.wifilight;

import com.example.my_wifilight.R;
import com.wifilight.infos.DeviceInfo;
import com.wifilight.infos.DeviceInfoManager;
import com.wifilight.interaction.WifiLightOperate;
import com.wifilight.listadapter.DeviceListAdapter;
import com.wifilight.service.ProcessService;
import com.wifilight.service.ProcessService.OnUIUpdateListener;
import com.wifilight.utils.ByteUtils;
import com.wifilight.utils.Utils;
import com.wifilight.views.GetPopWindow;
import com.wifilight.views.WifiLightView;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.Toast;

public class DeviceOperateActivity extends Activity {
	public final static int HAS_DEVICE = 0x00;
	
	static WifiLightView view;
	
	PopupWindow deviceListPopWin;
	View contentView;
	ImageView deviceListBtn;
	
	private String currentSelectSSID;
	
	static DeviceListAdapter adapter;
	static Context context;
	static boolean netFlag = true;
	
	
	@SuppressWarnings("deprecation")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setFullscreen();
		setContentView(R.layout.wifi_light_operate_layout);
		context = this;
		
		ProcessService.operateActivityEntered = true;
		Utils.canNetWorkOperateInMainThread();
		int screenWidth  = getWindowManager().getDefaultDisplay().getWidth();      
		int screenHeight = getWindowManager().getDefaultDisplay().getHeight();   
		// device 
		if(DeviceInfoManager.getDeviceList().size() > 0){
			currentSelectSSID = getCurrentSSID();
			DeviceInfo device = DeviceInfoManager.getDeviceList().get(0);
			
			view = new WifiLightView(this,device,screenWidth,screenHeight-100);
			view.setBackgroundResource(R.drawable.wifi_lamp_view_bg);
			view.setDeviceInfo(device);
			view.refresh();
			RelativeLayout topLayout = (RelativeLayout)this.findViewById(R.id.operate_layout);
			topLayout.addView(view);
		} else {
			Toast.makeText(DeviceOperateActivity.this, "No device find, please retry!", Toast.LENGTH_LONG).show();
			startActivity(new Intent(this,SearchActivity.class));
			finish();
			return;
		}
		
		contentView = LayoutInflater.from(DeviceOperateActivity.this).inflate(R.layout.device_list_popwin_layout, null);
		ListView deviceList = (ListView)contentView.findViewById(R.id.device_list);
		
		adapter = new DeviceListAdapter(DeviceOperateActivity.this,DeviceInfoManager.getDeviceList());
		deviceList.setAdapter(adapter);
		
		deviceListPopWin = GetPopWindow.createPopWin(contentView, screenWidth/2, screenHeight/3);
		deviceListBtn = (ImageView)this.findViewById(R.id.deviceListBtn);
		deviceListBtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				deviceListPopWin.showAsDropDown(deviceListBtn, 0, 20);
			}
		});
		
		deviceList.setOnItemClickListener(new OnItemClickListener(){

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				view.setDeviceInfo(DeviceInfoManager.getDeviceList().get(arg2));
				deviceListPopWin.dismiss();
				refresh();
			}
		});
		
		deviceList.setOnItemLongClickListener(new OnItemLongClickListener(){

			@Override
			public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				setDeviceName(DeviceInfoManager.getDeviceList().get(arg2),DeviceInfoManager.getDeviceList().get(arg2).getMacAddr(),DeviceInfoManager.getDeviceList().get(arg2).getIpStr());
				return false;
			}
		});
		
		
		ProcessService.setOnUIUpdateListener(new OnUIUpdateListener(){
			@Override
			public void onUpdate() {
				refresh();
			}
		});
	}
	
	String getCurrentSSID() {
		WifiManager wm = (WifiManager)getSystemService(WIFI_SERVICE);
		android.net.wifi.WifiInfo wifiInfo = wm.getConnectionInfo();
		return wifiInfo.getSSID();
	}
	
	/* get ssid info  */
	public boolean isNetworkChanged(){
		String nowSSID = getCurrentSSID();
		
		if (currentSelectSSID == null) {
			return true;
		}	
		if (!currentSelectSSID.trim().equals(nowSSID))
		{
			return true;
		}
		return false;
	}
	
	
	public void setFullscreen() {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
	}
	
	public static void refresh(){
		if(view != null)
			view.refresh();
		if(adapter != null)
			adapter.notifyDataSetChanged();
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		ProcessService.operateActivityEntered = false;
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		showCloseDialog("Tip","Are you sure?");
		return true;
	}
	
	public void showCloseDialog(String title,String msg){
		
		AlertDialog.Builder closeDialog = new AlertDialog.Builder(this);
		closeDialog.setTitle(title);
		closeDialog.setMessage(msg);
		closeDialog.setPositiveButton("Cancel", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}
		});
		closeDialog.setNegativeButton("Ok", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface arg0, int arg1) {
				DeviceInfoManager.getDeviceList().removeAllElements();
				DeviceOperateActivity.this.stopService(new Intent(DeviceOperateActivity.this,ProcessService.class));
				DeviceOperateActivity.this.finish();
			}
		});
		closeDialog.show();
	}
	
	public static void close(){
		Activity a = (Activity)context;
		a.finish();
	}
	
	public void setDeviceName(final DeviceInfo device,final byte[] mac,final String ipStr){
		AlertDialog.Builder builder = new AlertDialog.Builder(DeviceOperateActivity.this);
		builder.setTitle("Edit Name");
		final EditText nameEdit = new EditText(DeviceOperateActivity.this);
		builder.setView(nameEdit);
		builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface arg0, int arg1) {
				
				if(!nameEdit.getText().toString().equals("")){
					WifiLightOperate.modifyDeviceName(mac, ipStr, nameEdit.getText().toString());
				}
				if(device != null){
					device.getNodes().get(0).setNodeName(ByteUtils.stringToByteArray(nameEdit.getText().toString()));
					adapter.notifyDataSetChanged();
					view.refresh();
				}
			}
		});
		/* builder */
		builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface arg0, int arg1) {
				arg0.dismiss();
			}
		});
		builder.show();
	}
}
