package com.wifilight;

import java.util.Timer;
import java.util.TimerTask;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;
import android.widget.Toast;

import com.example.my_wifilight.R;
import com.wifilight.infos.DeviceInfoManager;
import com.wifilight.infos.WifiInfoManager;
import com.wifilight.interaction.WifiLightOperate;
import com.wifilight.listadapter.WifiListAdapter;
import com.wifilight.log.LogUtils;
import com.wifilight.service.ProcessService;
import com.wifilight.service.ProcessService.OnWifiConfigListener;
import com.wifilight.utils.ByteUtils;
import com.wifilight.utils.CheckDevice;
import com.wifilight.utils.CheckDevice.DeviceFindListener;
import com.wifilight.utils.CheckDeviceGet;
import com.wifilight.utils.TaskExcute;
import com.wifilight.utils.TaskExcute.WorkTask;
import com.wifilight.utils.TimeoutHandler;
import com.wifilight.utils.TimeoutHandler.OnTimeoutListener;
import com.wifilight.views.PullDownRefreshList;
import com.wifilight.views.PullDownRefreshList.OnRefreshListener;


public class WifiInfoActivity extends Activity{
	
	public final static String LOG_TAG = "WifiInfoActivity";

	static Context context;
	public static ProgressDialog progressDialog;
	static boolean isShow = false;
	static RelativeLayout layout;
	
	public static String selectedSSID;
	static WifiListAdapter adapter ;
	static PullDownRefreshList wifiList;
	static boolean isLoad = false;
	 
	public final static int WIFI_LIST_REFRESH = 0x01;
	static Handler handler = new Handler(){
		@Override
		public void dispatchMessage(Message msg) {
			super.dispatchMessage(msg);
			if(msg.what == WIFI_LIST_REFRESH){
				Toast.makeText(context, "刷新失败！", Toast.LENGTH_LONG).show();
				wifiList.onRefreshComplete();
			}
		}
	 };
	static Timer refreshTimeOut;
	static Timer getWifiListTimeout ;
	static AlertDialog.Builder switchNetworkDialog;
	static boolean switchNetworkDialogShow = false;
	
	/* Check device list has devices */
	CheckDevice check;
	boolean isFind = false;
	
	TimeoutHandler handler1;
	boolean isCreate = false;
	Timer deviceFindTimer;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setFullscreen();
		setContentView(R.layout.main_layout);
		context = this;
		layout = (RelativeLayout)this.findViewById(R.id.big_layout);
		
		loadWifiList();
		
		ProcessService.setWifiConfigListener(new OnWifiConfigListener(){
			@Override
			public void configSuccess() {
				
				check = CheckDeviceGet.getInstance();
				showAlert("等待台灯加入网络");
				LogUtils.logToConsole(LOG_TAG, " configSuccess ", LogUtils.LOG_LEVEL_INFO);
				if(!isCreate){
					handler1 = new TimeoutHandler(25000,Looper.getMainLooper());
					deviceFindTimer = new Timer();
					isCreate = true;
				}
				
				handler1.setOnTimeoutListener(new OnTimeoutListener(){
					@Override
					public void onTimeout() {
						LogUtils.logToConsole(LOG_TAG, " 搜索设备超时！", LogUtils.LOG_LEVEL_INFO);
						check._stop();
						check = null;
						TaskExcute.setWorkTask(new WorkTask(){
							@Override
							public void doWork() {
								DeviceInfoManager.getDeviceList().removeAllElements();
								if(selectedSSID != null)
								if(!selectedSSID.trim().equals(ProcessService.utils.getConnectedSsid())){
									Intent wifiSettingsIntent = new Intent("android.settings.WIFI_SETTINGS");
									context.startActivity(wifiSettingsIntent);
								}
								
								Log.i(LOG_TAG, "deviceFindTimer create ... ");
								deviceFindTimer.schedule(new TimerTask(){
									@Override
									public void run() {
										if(selectedSSID != null)
										if(selectedSSID.trim().equals(ProcessService.utils.getConnectedSsid())){
											ProcessService.broadcast();
											if(DeviceInfoManager.getDeviceList().size() > 0){
												deviceFindTimer.cancel();
												handler1.cancel();
												WifiInfoActivity.this.finish();
												if(!ProcessService.operateActivityEntered)
												WifiInfoActivity.this.startActivity(new Intent(context,DeviceOperateActivity.class));
												ProcessService.operateActivityEntered = true;
												WifiInfoActivity.this.finish();
											}
										}
									}
								}, 1000,2000);
							}
						});
						TaskExcute.excute(1000);
					}
				});
				
				check.setDeviceFindListener(new DeviceFindListener(){
					
					@Override
					public void deviceFind() {
						
						Log.i(LOG_TAG, "DeviceFind device size : " + DeviceInfoManager.getDeviceList().size());
						Log.i(LOG_TAG, ByteUtils.bytesToHexString(DeviceInfoManager.getDeviceList().get(0).getMacAddr()));
						Log.i(LOG_TAG, DeviceInfoManager.getDeviceList().get(0).getIpStr());
						
						if(selectedSSID != null)
						if(selectedSSID.trim().equals(ProcessService.utils.getConnectedSsid())){
							if(isFind)
								return;
							
							check._stop();
							check = null;
							isFind = true;
							handler1.cancel();
							deviceFindTimer.cancel();
							WifiInfoActivity.this.finish();
							if(!ProcessService.operateActivityEntered)
							WifiInfoActivity.this.startActivity(new Intent(context,DeviceOperateActivity.class));
							WifiInfoActivity.this.finish();
							
						} else {
							
							check._stop();
							check = null;
							DeviceInfoManager.getDeviceList().removeAllElements();
							
						}
					}
				});
				check._start();
			}
		});
	}
	
	/* set full screen */
	public void setFullscreen() {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
	}
	
	/* load wifi list */
	public static void loadWifiList(){
		if(isLoad)
			return;
		isLoad = false;
		
		/* Tip Text */
		layout.setBackgroundResource(R.drawable.wifi_list_bg);
		layout.removeAllViews();
		
		RelativeLayout.LayoutParams txtParams = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);
		txtParams.topMargin = 50;
		txtParams.leftMargin = 60;
		TextView tipTxt = new TextView(context);
		
		String macStr = null;
		if(ProcessService.configMac != null){
			macStr = ByteUtils.bytesToHexString(ProcessService.configMac).toUpperCase();
			tipTxt.setText("请为"+"\""+"朗世-"+macStr.substring(12, 15)+macStr.substring(16, 19)+"\""+"选取网络");
		}
		tipTxt.setId(0x1111);
		tipTxt.setTextSize(18);
		tipTxt.setTextColor(0xffffffff);
		layout.addView(tipTxt,txtParams);
		
		wifiList = new PullDownRefreshList(context);
		wifiList.setBackgroundResource(R.drawable.wifi_list_frame);
		Drawable drawable = new ColorDrawable(0x00000000);
		wifiList.setDivider(drawable);
		wifiList.setDividerHeight(0);
		
		RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.MATCH_PARENT);
		params.leftMargin = 35;
		params.rightMargin = 35;
		params.topMargin = 20;
		params.bottomMargin = 200;
		
		params.addRule(RelativeLayout.BELOW, 0x1111);
		
		adapter = new WifiListAdapter(context,WifiInfoManager.getWifiInfoList());
		wifiList.setAdapter(adapter);
		wifiList.setOnItemClickListener(new OnItemClickListener(){
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				selectedSSID = WifiInfoManager.getWifiInfoList().get(arg2-1).getSsid();
				showPasswordInputDialog(WifiInfoManager.getWifiInfoList().get(arg2-1).getSsid());
				Log.i("CurrentSelect SSID", selectedSSID);
			}
		});
		layout.addView(wifiList,params);
		
		wifiList.setonRefreshListener(new OnRefreshListener(){
			@Override
			public void onRefresh() {
				
				WifiLightOperate.startWifiScan(ProcessService.configMac, ProcessService.configIp);
				refreshTimeOut = new Timer();
				refreshTimeOut.schedule(new TimerTask(){
					@Override
					public void run() {
						Message msg = new Message();
						msg.what = WIFI_LIST_REFRESH;
						handler.sendMessage(msg);
					}
				}, 15000);
			}
		});
	}
	
	public static void showAlert(String msg){
			if(!isShow) {
				progressDialog = ProgressDialog.show(context, "", msg, true, false);
				isShow = true;
			}
	}
	
	public static void cancelAlert() {
		if( progressDialog != null ){
			progressDialog.cancel();
			isShow = false;
		}
	}
	
	public static void showPasswordInputDialog(final String ssid){
		AlertDialog.Builder inputDialog = new AlertDialog.Builder(context);
		inputDialog.setTitle("请输入\""+ssid+"\"的密码");
		final EditText input = new EditText(context);
		input.setFocusable(true);
		input.requestFocus();
		input.setInputType(InputType.TYPE_NUMBER_FLAG_DECIMAL);
		inputDialog.setView(input);
		inputDialog.setNegativeButton("取消", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}
		});
		
		inputDialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				if(!input.getText().toString().trim().equals("")){
					WifiLightOperate.configWifiLamp(ssid.trim(),input.getText().toString().trim());
					dialog.dismiss();
				}
			}
		});
		inputDialog.show();
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		ProcessService.wifiInfoActivtyEntered = false;
		WifiInfoManager.getWifiInfoList().removeAllElements();
		isShow = false;
		selectedSSID = null;
		if(handler1 != null){
			handler1.cancel();
		}
	}
	
	public static void showTips(String title,String msg){
		if( context == null)
			return;
		
		AlertDialog.Builder tips = new AlertDialog.Builder(context);
		tips.setTitle(title);
		tips.setMessage(msg);
		tips.setPositiveButton("确定", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}
		});
		tips.show();
	}
	
	public static void closeAppDialog(String title,String msg){
		AlertDialog.Builder tips = new AlertDialog.Builder(context);
		tips.setTitle(title);
		tips.setMessage(msg);
		tips.setPositiveButton("确定", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				
			}
		});
		tips.show();
	}
	
	public static void switchNetworkDialog(String title,String msg){
		if( !switchNetworkDialogShow ){
		switchNetworkDialog = new AlertDialog.Builder(context);
		switchNetworkDialog.setTitle(title);
		switchNetworkDialog.setMessage(msg);
		switchNetworkDialog.setPositiveButton("取消", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// WifiInfoActivity.this.finish();
			}
		});
		switchNetworkDialog.setNegativeButton("切换", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface arg0, int arg1) {
				switchNetwork();
			}
		});
		switchNetworkDialog.show();
		}
	}
	
	public static void switchNetwork(){
		Intent wifiSettingsIntent = new Intent("android.settings.WIFI_SETTINGS");
		context.startActivity(wifiSettingsIntent);
	}
	
	public static void refreshWifiList() {
		if(adapter != null && wifiList != null){
			wifiList.onRefreshComplete();
			adapter.notifyDataSetChanged();
		}
		if(refreshTimeOut != null)
			refreshTimeOut.cancel();
	}
	
}
