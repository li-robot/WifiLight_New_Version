package com.wifilight.listadapter;

import java.util.Vector;

import com.example.my_wifilight.R;
import com.wifilight.infos.WifiInfo;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class WifiListAdapter extends BaseAdapter {
	
	public Context context;
	public Vector<WifiInfo> wifiInfos;
	
	public WifiListAdapter(Context context,Vector<WifiInfo> wifiInfos){
		this.context = context;	
		this.wifiInfos = wifiInfos;
	}

	@Override
	public int getCount() {
		return wifiInfos.size();
	}

	@Override
	public Object getItem(int arg0) {
		return arg0;
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return arg0;
	}

	@Override
	public View getView(int arg0, View arg1, ViewGroup arg2) {
		
		View view = LayoutInflater.from(context).inflate(R.layout.wifi_list_adapter_layout, null);
		TextView textView = (TextView)view.findViewById(R.id.wifi_name_txt);
		textView.setText(wifiInfos.get(arg0).getSsid());
		ImageView signalStrengthIcon = (ImageView)view.findViewById(R.id.signal_strength_img);
		int currentSignal = wifiInfos.get(arg0).getSignal_strength();
		if( currentSignal >= 0 &&  currentSignal <= 30){
			signalStrengthIcon.setImageResource(R.drawable.signal_strength1);
		} else if(currentSignal >= 30 && currentSignal <= 60){
			signalStrengthIcon.setImageResource(R.drawable.signal_strength2);
		} else if(currentSignal >= 60){
			signalStrengthIcon.setImageResource(R.drawable.signal_strength3);
		}
		return view;
	}
	
}
