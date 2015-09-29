package com.wifilight.views;

import java.util.Vector;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.view.MotionEvent;
import android.view.View;


import com.example.my_wifilight.R;
import com.wifilight.infos.DeviceInfo;
import com.wifilight.interaction.WifiLightOperate;
import com.wifilight.protocol.MessageConstants;
import com.wifilight.utils.ByteUtils;

public class WifiLightView extends View {

	private DeviceInfo deviceInfo;
	private int bgId;
	private int screenWidth;
	private int screenHeight;
	
	private Bitmap switchBitmap;
	private Bitmap switchBitmap_h;
	private Bitmap addDelBitmap;
	private Bitmap touchPointBitmap_h;
	private Bitmap touchPointBitmap;
	
	private int bitmapWidth;
	private int bitmapHeight;
	
	private int addDelX ;
	private int addDelY ;
	
	private Paint paint;
	
	private float[] top_angles = { 202.5f,225f,247.5f,270f,292.5f,314.5f,337 };
	private float[] bottom_angles = { 157.5f,135,112.5f,90,67.5f,45,22.5f };
	
	private int currentX ;
	private int currentY ;
	
	private int currentX1 ;
	private int currentY1 ;
	
	private boolean isTop ;
	private boolean isBottom;
	
	private byte switchState = 0;
	
	private byte adjVal;
	private byte adjVal1;
	
	private int region ;
	
	private Vector<Integer> pointXs = new Vector<Integer>();
	
	private String deviceName;
	
	public WifiLightView(Context context,DeviceInfo device,int screenWidth , int screenHeight) {
		super(context);
		
		this.screenHeight = screenHeight;
		this.screenWidth = screenWidth;
		
		this.setBackgroundResource(bgId);
		switchBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.switch_center_bg);
		switchBitmap_h = BitmapFactory.decodeResource(getResources(), R.drawable.switch_center_bg);
		addDelBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.add_del);
		touchPointBitmap_h = BitmapFactory.decodeResource(getResources(),R.drawable.wifi_light_touch_point_h);
		touchPointBitmap = BitmapFactory.decodeResource(getResources(),R.drawable.wifi_light_touch_point);
		
		bitmapWidth = switchBitmap.getWidth();
		bitmapHeight = switchBitmap.getHeight();
		paint = new Paint();
		
		this.deviceInfo = device;
		
		for(int i = 0;i<top_angles.length;i ++){
			float[] point = calcPos(addDelBitmap.getWidth()/2,top_angles[i]);
			pointXs.add((int)(point[0] + screenWidth/2));
		}
		
		region = (addDelBitmap.getWidth()/6) - 30;
		
		
		this.refresh();
	}
	
	public int getScreenWidth() {
		return screenWidth;
	}

	public void setScreenWidth(int screenWidth) {
		this.screenWidth = screenWidth;
	}

	public int getScreenHeight() {
		return screenHeight;
	}

	public void setScreenHeight(int screenHeight) {
		this.screenHeight = screenHeight;
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		
		addDelX = (screenWidth-addDelBitmap.getWidth())/2;
		addDelY = (screenHeight-addDelBitmap.getHeight())/2;
		
		canvas.drawBitmap(addDelBitmap, addDelX, addDelY, paint);
		if(switchState == 0){
			canvas.drawBitmap(switchBitmap, (screenWidth-bitmapWidth)/2, (screenHeight-bitmapHeight)/2, paint);
			
			for(int i = 0;i<top_angles.length;i ++){
				float[] point = calcPos(addDelBitmap.getWidth()/2,top_angles[i]);
				canvas.drawBitmap(touchPointBitmap, point[0]+screenWidth/2 - 8, point[1]+screenHeight/2 - 8, paint);
			}
			
			for(int i = 0;i<bottom_angles.length;i ++){
				float[] point = calcPos(addDelBitmap.getWidth()/2,bottom_angles[i]);
				canvas.drawBitmap(touchPointBitmap, point[0]+screenWidth/2 - 8, point[1]+screenHeight/2 - 8, paint);
			}
			
		} else {
			canvas.drawBitmap(switchBitmap_h, (screenWidth-bitmapWidth)/2, (screenHeight-bitmapHeight)/2, paint);
		
		/* draw top */
		for(int i = 0;i<top_angles.length;i ++){
			float[] point = calcPos(addDelBitmap.getWidth()/2,top_angles[i]);
			if(currentX > (point[0] + screenWidth/2)){
				canvas.drawBitmap(touchPointBitmap_h, point[0]+screenWidth/2 - 8, point[1]+screenHeight/2 - 8, paint);
			} else {
				canvas.drawBitmap(touchPointBitmap, point[0]+screenWidth/2 - 8, point[1]+screenHeight/2 - 8, paint);
			}
		}
		
		/* draw bottom */
		for(int i = 0;i<bottom_angles.length;i ++){
			
			float[] point = calcPos(addDelBitmap.getWidth()/2,bottom_angles[i]);
			if(currentX1 > (point[0] + screenWidth/2)){
				canvas.drawBitmap(touchPointBitmap_h, point[0]+screenWidth/2 - 8, point[1]+screenHeight/2 - 8, paint);
			} else {
				canvas.drawBitmap(touchPointBitmap, point[0]+screenWidth/2 - 8, point[1]+screenHeight/2 - 8, paint);
			}
		}
		
		}
		// bottom draw
		
		paint.setColor(Color.WHITE);
		paint.setTextSize(35);
		paint.setTypeface(Typeface.DEFAULT_BOLD);
		String nodeName = new String(deviceInfo.getNodes().get(0).getNodeName()).trim();
		int strWidth = (int)paint.measureText(nodeName);
		canvas.drawText(nodeName, (screenWidth-strWidth)/2, screenHeight/8, paint);
		if(ByteUtils.byteArrayCompare(deviceInfo.getNodes().get(0).getNodeName(),MessageConstants.DEFAULT_NAME)){
			strWidth = (int)paint.measureText(ByteUtils.getLightName(deviceInfo.getMacAddr()));
			canvas.drawText(ByteUtils.getLightName(deviceInfo.getMacAddr()), (screenWidth-strWidth)/2, screenHeight/8, paint);
		}
	}
	
	public void setBackgroundId(int bgId){
		this.bgId = bgId;
	}
	
	private float[] calcPos(float r, float angle) {
		 float radian = (float)Math.toRadians(angle);  
		 float x = (float)(r * Math.cos(radian));  
		 float y = (float)(r * Math.sin(radian));  
		 return new float[] { x, y };  
    }

	@Override
	public boolean onTouchEvent(MotionEvent event) {
	
		if(isCanTouchRegionTop((int)event.getX(),(int)event.getY())){
			currentX = (int)event.getX();
			currentY = (int)event.getY();
			refresh();
			isTop = true;
			isBottom = false;
			adjVal = (byte)setProgress((int)event.getX());
			
			if(currentX < pointXs.get(1) && currentX1 < pointXs.get(1)){
				currentX = pointXs.get(1) + 5;
				refresh();
			}
		}
		
		if(isCanTouchRegionBottom((int)event.getX(),(int)event.getY())){
			currentX1 = (int)event.getX();
			currentY1 = (int)event.getY();
			refresh();
			isTop = false;
			isBottom = true;
			
			adjVal1 = (byte)setProgress((int)event.getX());
			
			if(currentX < pointXs.get(1) && currentX1 < pointXs.get(1)){
				currentX1 = pointXs.get(1) + 5;
				refresh();
			}
		}
		
		if(isCanTouchRegionCenter((int)event.getX(),(int)event.getY()) && event.getAction() == MotionEvent.ACTION_DOWN){
			
			if(switchState == 0){
				switchState = 1;
			} else {
				switchState = 0;
			}
			WifiLightOperate.wifiLightOperate(deviceInfo.getMacAddr(),deviceInfo.getIpStr(),deviceInfo.getNodes().get(0).getChannel(), switchState, adjVal, adjVal1);
			
		}
		
		if(event.getAction() == MotionEvent.ACTION_UP
				&& (isCanTouchRegionTop((int)event.getX(),(int)event.getY()) || isCanTouchRegionBottom((int)event.getX(),(int)event.getY())) ){
			
			if( deviceInfo != null && switchState == 1){
				WifiLightOperate.wifiLightOperate(deviceInfo.getMacAddr(), deviceInfo.getIpStr(),deviceInfo.getNodes().get(0).getChannel(), switchState, adjVal, adjVal1);
			}
		}
		return true ;
	}
	
	boolean isCanTouchRegionTop(int x , int y) {
		
		if( x > addDelX - 60  &&  x < addDelBitmap.getWidth() + addDelX + 60
				&&  y < addDelY - region  && y > addDelY - addDelBitmap.getWidth()/2 - region ){
			return true;
		}
		return false;
	}
	
	boolean isCanTouchRegionBottom(int x , int y){
		
		if( x > addDelX - 60  &&  x < addDelBitmap.getWidth() + addDelX + 60
				&&  y > addDelY + region && y < addDelY + addDelBitmap.getWidth()/2 + region ) {
			return true;
		}
		
		return false;
	}
	
	boolean isCanTouchRegionCenter(int x, int y){
		if( x > addDelX + region  &&  x < addDelBitmap.getWidth() + addDelX - region
				&&  y > addDelY - region && y < addDelY + region ) {
			return true;
		}
		
		return false;
	}
	
	/* Top */
	public void setTopCurrentProgress(int var) {
		currentX = progressToCurrentX(var);
	}
	
	/* Bottom */
	public void setBottomCurrentProgress(int var) {
		currentX1 = progressToCurrentX(var);
	}
	
	public int setProgress(int x) {
		
		if(x > pointXs.get(0) && x < pointXs.get(1))
			return 0;
		
		if(x > pointXs.get(1) && x < pointXs.get(2))
			return 10;
		
		if(x > pointXs.get(2) && x < pointXs.get(3))
			return 20;
		
		if(x > pointXs.get(3) && x < pointXs.get(4))
			return 40;
		
		if(x > pointXs.get(4) && x < pointXs.get(5))
			return 60;
		
		if(x > pointXs.get(5) && x < pointXs.get(6))
			return 80;
		if(x > pointXs.get(6))
			return 100;
		
		return -1;
	}
	
	
	public int progressToCurrentX( int progress ){
		if(progress >= 0 && progress < 10){
			return pointXs.get(0)+5;
		}
		if(progress >= 10 && progress < 20){
			return pointXs.get(1)+5;
		}
		if(progress >= 20 && progress < 40){
			return pointXs.get(2)+5;
		}
		if(progress >= 40 && progress < 60){
			return pointXs.get(3)+5;
		}
		if(progress >= 60 && progress < 80){
			return pointXs.get(4)+5;
		}
		if(progress >= 80 && progress < 100){
			return pointXs.get(5)+5;
		}
		if(progress == 100){
			return pointXs.get(6)+5;
		}
		return -1;
	}
	
	public void refresh(){
		
		if( deviceInfo != null ) {
			adjVal = deviceInfo.getNodes().get(0).getAdjVal();
			adjVal1 = deviceInfo.getNodes().get(0).getAdjVal1();
		}
		switchState = deviceInfo.getNodes().get(0).getChannelStatus();
		currentX = progressToCurrentX(adjVal);
		currentX1 = progressToCurrentX(adjVal1);
		
		invalidate();
	}
	
	public void setDeviceInfo(DeviceInfo device){
		this.deviceInfo = device;
	}
	
	public DeviceInfo getDeviceInfo(){
		return deviceInfo;
	}
	
	public void setLightName(String name){
		this.deviceName = name;
	}
	
}
