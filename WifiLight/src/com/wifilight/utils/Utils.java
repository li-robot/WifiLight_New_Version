package com.wifilight.utils;

import android.annotation.SuppressLint;
import android.os.Build;
import android.os.StrictMode;

public class Utils {
	@SuppressLint("NewApi")
	public static void canNetWorkOperateInMainThread(){
		if (Build.VERSION.SDK_INT >= 11) {
		   StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().detectDiskReads().detectDiskWrites().detectNetwork().penaltyLog().build());
		   StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder().detectLeakedSqlLiteObjects().detectLeakedClosableObjects().penaltyLog().penaltyDeath().build());
		}
	}
}
