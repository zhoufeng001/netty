package netty.chat.msg;

public class ChatMessage extends Message{

	private String sender ;
	private String receiver ;
	private boolean priv ;
	
	public String getSender() {
		return sender;
	}
	public void setSender(String sender) {
		this.sender = sender;
	}
	public String getReceiver() {
		return receiver;
	}
	public void setReceiver(String receiver) {
		this.receiver = receiver;
	}
	public boolean isPriv() {
		return priv;
	}
	public void setPriv(boolean priv) {
		this.priv = priv;
	}
	
}
