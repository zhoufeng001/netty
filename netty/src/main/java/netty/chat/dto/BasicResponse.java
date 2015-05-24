package netty.chat.dto;

import org.msgpack.annotation.Message;

@Message
public class BasicResponse {
	
	private int code ;

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}
	
}
