package netty.chat.msg;

import java.util.Date;

public class Message {
	
	private String msg ;
	
	private Date time ;

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public Date getTime() {
		return time;
	}

	public void setTime(Date time) {
		this.time = time;
	}

}
