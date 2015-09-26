package com.wifilight.retry;


/* Message indicate the session */
public class MMessage {

	private String token;
	private long timeStamp;
	private int retryCount = 0;
	private Object obj;

	public MMessage(String token) {
		this.token = token;
	}

	public String getToken() {
		return token;
	}

	public Object getObj() {
		return obj;
	}

	public void setObj(Object obj) {
		this.obj = obj;
	}

	public void setToken(String token) {
		this.token = token;
	}

	
	public long getTimeStamp() {
		return timeStamp;
	}

	public void setTimeStamp(long timeStamp) {
		this.timeStamp = timeStamp;
	}

	@Override
	public boolean equals(Object o) {
		MMessage msg = (MMessage) o;
		if (msg.getToken().equals(this.token))
			return true;
		return false;
	}
	
	public void countAdd(){
		retryCount ++;
	}
	
	public int getRetryCount(){
		return retryCount;
	}
	
}