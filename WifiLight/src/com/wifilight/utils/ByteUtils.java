package com.wifilight.utils;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;

public class ByteUtils {
	
	/**
	 * @param i int 参数
	 * @return 一个byte数组
	 */
	public static byte[] intToByteArray(int i) {   
		  byte[] result = new byte[4];   
		  result[0] = (byte)((i >> 24) & 0xFF);
		  result[1] = (byte)((i >> 16) & 0xFF);
		  result[2] = (byte)((i >> 8) & 0xFF); 
		  result[3] = (byte)(i & 0xFF);
		  return result;
	}
	
	public static int byteArrayToInt(byte[] array){
		ByteBuffer buf = ByteBuffer.wrap(array, 0, 4);
		IntBuffer intBuf = buf.asIntBuffer();
		return intBuf.get(0);
	}
	
	/**
	 * crc校验
	 * @param msg
	 * @param len
	 * @return
	 */
	public static byte[] lower_layer_make_crc16(byte[] msg, int len)
	{
	    short crc16 =  (short) 0xFFFF;
	    int i,j=0;
	    boolean c15,bit;
	    for (i = 0; i < len ; i++) {
	        for (j = 0; j < 8; j++) {
	             c15 = ((crc16 >> 15 & 1) == 1);
	             bit = ((msg[i] >> (7 - j) & 1) == 1);
	             crc16 <<= 1;
	             if (c15 ^ bit) {
	                 crc16 ^= 0x1021;
	             } 
	        }
	    }
	    return shortToByteArray(crc16);
	}
	
	public static byte[] shortToByteArray(short s) { 
        byte[] targets = new byte[2];
        for (int i = 0; i < 2; i++) {  
            int offset = (targets.length - 1 - i) * 8;  
            targets[i] = (byte) ((s >>> offset) & 0xff);  
        }
        return targets;
    }
	
	/**
	 * 从原数组中截取一个数组
	 * */
	public static byte[] getSubByteArray(byte[] src,int index,int len){
		if(src == null){
			return null;
		}
		if(len > src.length){
			return null;
		}
		if(index > src.length){
			return null;
		}
		byte[] array = new byte[len];
		for(int i=0;i<len;i++){
			array[i] = src[index];
			index ++;
		}
		return array;
	}
	
	public static boolean byteArrayCompare(byte[] src, byte[] des){
		if(src == null || des == null){
			return false;
		}
		for(int i=0;i<src.length;i++)
			if(src[i] != des[i])
				return false;
		return true;
	}
	
	public static byte[] stringToByteArray(String name){
		if(name == null)
			throw new NullPointerException("name not null");
		if(name.getBytes().length > 40){
			return null;
		}
		ByteBuffer nameBuf = ByteBuffer.allocate(40);
		nameBuf.put(name.getBytes());
		
		return nameBuf.array();
	}
	
	/* ssid */
	public static byte[] ssidToByteArray(String name){
		if(name == null)
			throw new NullPointerException();
		if(name.getBytes().length > 32){
			return null;
		}
		ByteBuffer nameBuf = ByteBuffer.allocate(32);
		nameBuf.put(name.getBytes());
		
		return nameBuf.array();
	}
	/* password */
	public static byte[] passwordToByteArray(String name){
		if(name.getBytes().length > 128){
			return null;
		}
		ByteBuffer nameBuf = ByteBuffer.allocate(128);
		nameBuf.put(name.getBytes());
		
		return nameBuf.array();
	}
	
	public static byte[] stringToByteArray1(String name){
		if(name.getBytes().length > 12){
			return null;
		}
		ByteBuffer nameBuf = ByteBuffer.allocate(12);
		nameBuf.put(name.getBytes());
		
		return nameBuf.array();
	}
	
	
	public static String bytesToHexString(byte[] src){  
		    StringBuilder stringBuilder = new StringBuilder("");  
		    if (src == null || src.length <= 0) {  
		        return null;  
		   }  
		    for (int i = 0; i < src.length; i++) {  
		        int v = src[i] & 0xFF;  
		        String hv = Integer.toHexString(v);
		         if(hv.equals("0")){
		        	 stringBuilder.append(" "+"00");
		         } else if( v <= 15){
		        	 stringBuilder.append(" 0"+hv);
		         } else {
		        	 stringBuilder.append(" "+hv);
		         }
		          
		    }  
		    return stringBuilder.toString();
	}

	public static String getLightName(byte[] mac){
		if(mac != null){
			String macStr = ByteUtils.bytesToHexString(mac).toUpperCase();
			return "朗世-"+macStr.substring(12, 15)+macStr.substring(16, 19);
		}
		return null;
	}
	
}
