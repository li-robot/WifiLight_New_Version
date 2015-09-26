package com.wifilight.protocol;

import java.nio.ByteBuffer;

import com.wifilight.utils.ByteUtils;

public class RequestMessage {
	
	public byte qid = MessageConstants.DEFAULT_QID;
	public byte[] mac;
	public byte channel;
	public byte dir = MessageConstants.REQUEST_DIR;
	public byte[] cmd;
	public byte[] args; 
	
	public RequestMessage(){
		// do nothing 
	}
	
	public RequestMessage(
			
			byte[] mac, /* MAC 地址*/
			byte channel, /* 通道   */
			byte[] cmd, /* 操作命令  */
			byte[] args /* 请求参数    */
			
			)
	{
		this.mac = mac;
		this.channel = channel;
		this.cmd = cmd;
		this.args = args;
	}
	
	public byte[] toByteArray(){
		short arg_length = (short) ( args.length );
		ByteBuffer buff = ByteBuffer.allocate( arg_length + MessageConstants.ENSURE_LENGTH );
		
		buff.put((byte)0xDD);
		buff.put((byte)0xEE);
		buff.put(ByteUtils.shortToByteArray((short)( arg_length + MessageConstants.DATA_ENSURE_LENGTH )));
		buff.put(qid);
		buff.put(mac);
		buff.put(channel);
		buff.put(dir);
		buff.put(cmd);
		buff.put(args);
		
		byte[] crc_result = ByteUtils.lower_layer_make_crc16(
				
				ByteUtils.getSubByteArray(buff.array(),
				4,
				arg_length + MessageConstants.DATA_ENSURE_LENGTH ),
				arg_length + MessageConstants.DATA_ENSURE_LENGTH
				
				);
		
		buff.put(crc_result);
		return buff.array();
	}
	
}
