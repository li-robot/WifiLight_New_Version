package com.wifilight.protocol;

import java.nio.ByteBuffer;
import com.wifilight.utils.ByteUtils;

public class ResponseMessage {
	
	
	private byte[] src;
	private short data_length;
	
	public final static int DATA_LENGHT_START = 2;
	public final static int DATA_LENGHT_END = 3;
	
	public final static int MAC_START = 5;
	public final static int MAC_LENGTH = 8;
	
	public final static int CHANNEL_START = 13;
	public final static int QID_START = 4;
	public final static int DIR_START = 14;
	
	public final static int CMD_START = 15;
	public final static int CMD_END = 16;
	public final static int ARGS_START = 17;
	
	
	public ResponseMessage(byte[] src){
		
		this.src = src;
		if(src != null){
			if(src.length >= 4){
				byte[] arr = new byte[2];
				arr[0] = src[ DATA_LENGHT_START ];
				arr[1] = src[ DATA_LENGHT_END ];
				ByteBuffer buffer = ByteBuffer.wrap(arr);
				this.data_length = buffer.getShort();
			}
		}
		
	}
	
	public byte[] getMac(){
		if(src != null){
			if(src.length >= 12){
				return ByteUtils.getSubByteArray(src, MAC_START, MAC_LENGTH);
			}
		}
		return null;
	}
	
	public byte getChannel(){
		if(src != null){
			if(src.length >= 13){
				return src[ CHANNEL_START ];
			}
		}
		return -1;
	}
	
	public byte getQid(){
		if(src != null){
			if(src.length >= 4){
				return src[ QID_START ];
			}
		}
		return -1;
	}
	
	public byte getDir(){
		if(src != null){
			if(src.length >= 14){
				return src[ DIR_START ];
			}
		}
		return -1;
	}
	
	public byte[] getCmd(){
		if(src != null){
			if(src.length >= 16){
				byte[] cmd = new byte[2];
				cmd[0] = src[ CMD_START ];
				cmd[1] = src[ CMD_END ];
				return cmd;
			}
		}
		return null;
	}
	
	public byte[] getArgs(){
		if(src != null){
			if(src.length >= 17 && (data_length-13) > 0){
				return ByteUtils.getSubByteArray(src, ARGS_START, data_length-13);
			}
		}
		return null;
	}
	
	public byte[] getCrc(){
		byte[] crcArray = new byte[2];
		crcArray[0] = src[ 4+data_length ];
		crcArray[1] = src[ 5+data_length ];
		return crcArray;
	}
	
	public byte[] getDataArea() {
		return ByteUtils.getSubByteArray(src, 0, 4+data_length);
	}
	
	public boolean verify(){
		
		byte[] vcrc = ByteUtils.lower_layer_make_crc16(getDataArea(),getDataArea().length);
		if(ByteUtils.byteArrayCompare(vcrc, getCrc())){
			return true;
		}
		return false;
	}

}
