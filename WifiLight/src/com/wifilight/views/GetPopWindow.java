package com.wifilight.views;

import android.graphics.drawable.BitmapDrawable;
import android.view.View;
import android.widget.PopupWindow;

public class GetPopWindow {
	
	@SuppressWarnings("deprecation")
	public static PopupWindow createPopWin(View contentView,int width,int height){
		PopupWindow popWin = new PopupWindow(contentView,width,height);
		popWin.setBackgroundDrawable(new BitmapDrawable());
		popWin.setFocusable(true);
		return popWin;
	}
}
