package com.wifilight;

import java.util.Timer;
import java.util.TimerTask;

import com.example.my_wifilight.R;
import com.wifilight.infos.DeviceInfoManager;
import com.wifilight.infos.WifiInfoManager;
import com.wifilight.service.ProcessService;
import com.wifilight.service.ProcessService.OnWifiInfoGetListener;
import com.wifilight.utils.Utils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import android.view.KeyEvent;
import android.view.Window;

@SuppressLint("HandlerLeak")
public class SearchActivity extends Activity {
	
	static Context context;
	public final static int DEVICE_LOAD_FAILED = 0x01;
	public final static int SHOW_PROGRESS = 0x02;
	
	Handler handler = new Handler(){
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			if(msg.what == DEVICE_LOAD_FAILED && ProcessService.searchActivityEntered){
				showAlert();
			}
		}
	};
	
	public static Timer timer;
	public static boolean checkFlag = true;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setFullscreen();
		setContentView(R.layout.splash_screen_layout);
		Utils.canNetWorkOperateInMainThread();
		ProcessService.searchActivityEntered = true;
		context = this;
		/* 启动后台服务   */
		 new Thread(new Runnable(){
			@Override
			public void run() {
				login();	
			}
		 }).start();
		 
		 /* 启动定时器    */
			 timer = new Timer();
			 timer.schedule(new TimerTask(){
				 @Override
				 public void run() {
					 if(checkFlag)
					  process();
				 }
			 
			 }, 10000);
		showProgressDialog("搜索智控台灯");
		
		ProcessService.setOnWifiInfoGetListener(new OnWifiInfoGetListener(){
			@Override
			public void onGet() {
				if(ProcessService.wifiInfoActivtyEntered)
					return;
				ProcessService.wifiInfoActivtyEntered = true;
				
				new Thread(new Runnable(){
					@Override
					public void run() {
						boolean isCheck = true;
						while(isCheck)
							if(WifiInfoManager.getWifiInfoList().size() > 0){
								isCheck = false;
								cancelAlert();
								context.startActivity(new Intent(context,WifiInfoActivity.class));
								SearchActivity.this.finish();
							}
					}
				}).start();
			}
		});
		
	}
	
	public void setFullscreen() {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
	}
	
	public static void login() {
		// start ProcessService        
		context.startService(new Intent(context,ProcessService.class));
	}
	
	public void enterMainPage(){
		Activity a = (Activity)context;
		Intent intent = new Intent(context,DeviceOperateActivity.class);
		a.startActivity(intent);
		a.finish();
	}
	
	public boolean checkDevice(){
		if(DeviceInfoManager.getDeviceList().size() > 0)
			return true;
		return false;
	}
	
	public void process(){
		if(checkDevice()) {
			/* 进入主界面    */
			cancelAlert();
			enterMainPage();
			return;
		} else {
			Message msg = new Message();
			msg.what = DEVICE_LOAD_FAILED;
			handler.sendMessage(msg);
		}
	}
	
	public void showAlert(){
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle("提示");
		builder.setMessage("没有扫描到设备，是否重新扫描？");
		builder.setCancelable(false);
		builder.setPositiveButton("是", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface arg0, int arg1) {
				ProcessService.broadcast();
				/* 启动定时器    */
				 timer = new Timer();
				 timer.schedule(new TimerTask(){
					@Override
					public void run() {
						process();
					}
				 }, 3000);
				 
				 Message msg = new Message();
				 msg.what = SHOW_PROGRESS;
				 handler.sendMessage(msg);
			}
		});
		
		builder.setNegativeButton("否", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				cancelAlert();
				SearchActivity.this.stopService(new Intent(SearchActivity.this,ProcessService.class));
				SearchActivity.this.finish();
			}
		});
		builder.show();
	}
	
	private static boolean isShow = false;
	public static ProgressDialog progressDialog;
	
	public static void showProgressDialog(String msg){
		if(!isShow && context != null) {
			progressDialog = ProgressDialog.show(context, "", msg, true, false);
			isShow = true;
		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		ProcessService.searchActivityEntered = false;
		isShow = false;
	}
	
	/* Show wifi list activity */
	public static void enterMainActivity(){
		/* enter search activity */
	}
	
	public static void cancelAlert(){
		if(progressDialog != null && progressDialog.isShowing()){
			progressDialog.cancel();
		}
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if(keyCode == KeyEvent.KEYCODE_BACK){
			this.stopService(new Intent(this,ProcessService.class));
		}
		return super.onKeyDown(keyCode, event);
	}
}
