package netty.chat.dto;

import org.msgpack.annotation.Message;

@Message
public class SendMessageRequest {
	
	private String receiver ;
	private String message ;
	private boolean priv ;
	public String getReceiver() {
		return receiver;
	}
	public void setReceiver(String receiver) {
		this.receiver = receiver;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public boolean isPriv() {
		return priv;
	}
	public void setPriv(boolean priv) {
		this.priv = priv;
	}
	
}
