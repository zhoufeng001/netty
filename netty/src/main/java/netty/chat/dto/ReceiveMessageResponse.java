package netty.chat.dto;

import org.msgpack.annotation.Message;

@Message
public class ReceiveMessageResponse extends BasicResponse{

	private String message ;
	private String sender ;
	private String reciver ;
	private boolean priv ;
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public String getSender() {
		return sender;
	}
	public void setSender(String sender) {
		this.sender = sender;
	}
	public boolean getPriv() {
		return priv;
	}
	public void setPriv(boolean priv) {
		this.priv = priv;
	}
	public String getReciver() {
		return reciver;
	}
	public void setReciver(String reciver) {
		this.reciver = reciver;
	}
	
}
